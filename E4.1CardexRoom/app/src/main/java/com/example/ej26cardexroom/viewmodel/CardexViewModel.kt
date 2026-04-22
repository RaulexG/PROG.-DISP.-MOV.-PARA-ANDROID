package com.example.ej26cardexroom.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ej26cardexroom.data.local.CardexDetalle
import com.example.ej26cardexroom.data.local.CardexEntity
import com.example.ej26cardexroom.data.local.CardexRepository
import com.example.ej26cardexroom.data.local.MateriaEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

enum class ModoFiltro {
    TODOS,
    POR_SEMESTRE,
    FALTANTES
}

@OptIn(ExperimentalCoroutinesApi::class)
class CardexViewModel(
    private val repository: CardexRepository
) : ViewModel() {

    var materiaSeleccionada by mutableStateOf<MateriaEntity?>(null)
        private set

    var semestreCursado by mutableStateOf("")
    var anio by mutableStateOf("")
    var periodo by mutableStateOf("Enero-Junio")
    var calificacion by mutableStateOf("")
    var editandoId by mutableStateOf<Int?>(null)
        private set

    private val modoFiltro = MutableStateFlow(ModoFiltro.TODOS)
    private val semestreFiltro = MutableStateFlow<Int?>(null)

    val modoFiltroState: StateFlow<ModoFiltro> = modoFiltro.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ModoFiltro.TODOS
    )

    val materias: StateFlow<List<MateriaEntity>> = repository.materias.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val materiasFaltantes: StateFlow<List<MateriaEntity>> = repository.materiasFaltantes.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val registros: StateFlow<List<CardexDetalle>> =
        combine(modoFiltro, semestreFiltro) { modo, semestre ->
            modo to semestre
        }.flatMapLatest { (modo, semestre) ->
            if (modo == ModoFiltro.POR_SEMESTRE && semestre != null) {
                repository.getRegistrosBySemestre(semestre)
            } else {
                repository.getRegistros()
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val creditosAprobados: StateFlow<Int> = repository.creditosAprobados.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0
    )

    val promedio: StateFlow<Double> = repository.promedio.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0.0
    )

    val creditosMostrados: StateFlow<Int> =
        combine(modoFiltro, registros, materiasFaltantes, creditosAprobados) { modo, registrosActuales, faltantesActuales, creditosTotales ->
            when (modo) {
                ModoFiltro.TODOS -> creditosTotales
                ModoFiltro.POR_SEMESTRE -> registrosActuales.sumOf { it.creditos }
                ModoFiltro.FALTANTES -> faltantesActuales.sumOf { it.creditos }
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    val etiquetaCreditos: StateFlow<String> =
        combine(modoFiltro, semestreFiltro) { modo, semestre ->
            when (modo) {
                ModoFiltro.TODOS -> "Creditos aprobados"
                ModoFiltro.POR_SEMESTRE -> {
                    if (semestre != null) "Creditos del semestre $semestre"
                    else "Creditos del filtro"
                }
                ModoFiltro.FALTANTES -> "Creditos por cursar"
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            "Creditos aprobados"
        )

    init {
        viewModelScope.launch {
            repository.seedMateriasSiNecesario()
        }
    }

    fun seleccionarMateria(materia: MateriaEntity) {
        materiaSeleccionada = materia
    }

    fun setModoFiltro(modo: ModoFiltro) {
        modoFiltro.value = modo
    }

    fun setSemestreFiltro(valor: String) {
        semestreFiltro.value = valor.toIntOrNull()
    }

    fun limpiarFormulario() {
        materiaSeleccionada = null
        semestreCursado = ""
        anio = ""
        periodo = "Enero-Junio"
        calificacion = ""
        editandoId = null
    }

    fun cargarParaEditar(detalle: CardexDetalle) {
        materiaSeleccionada = materias.value.firstOrNull { it.clave == detalle.materiaClave }
        semestreCursado = detalle.semestreCursado.toString()
        anio = detalle.anio.toString()
        periodo = detalle.periodo
        calificacion = detalle.calificacion.toString()
        editandoId = detalle.id
    }

    suspend fun guardar(): String {
        val materia = materiaSeleccionada ?: return "Selecciona una materia."
        val semestre = semestreCursado.trim().toIntOrNull() ?: return "Semestre inválido."
        val anioInt = anio.trim().toIntOrNull() ?: return "Año inválido."
        val calif = calificacion.trim().toIntOrNull() ?: return "Calificación inválida."

        if (calif !in 0..100) return "La calificación debe estar entre 0 y 100."

        val registro = CardexEntity(
            id = editandoId ?: 0,
            materiaClave = materia.clave,
            semestreCursado = semestre,
            anio = anioInt,
            periodo = periodo,
            calificacion = calif
        )

        return try {
            if (editandoId == null) {
                repository.insertRegistro(registro)
                limpiarFormulario()
                "Registro guardado correctamente."
            } else {
                repository.updateRegistro(registro)
                limpiarFormulario()
                "Registro actualizado correctamente."
            }
        } catch (_: Exception) {
            "No se pudo guardar. Es posible que esa materia ya exista en el cárdex."
        }
    }

    fun eliminar(detalle: CardexDetalle) {
        viewModelScope.launch {
            repository.deleteRegistro(
                CardexEntity(
                    id = detalle.id,
                    materiaClave = detalle.materiaClave,
                    semestreCursado = detalle.semestreCursado,
                    anio = detalle.anio,
                    periodo = detalle.periodo,
                    calificacion = detalle.calificacion
                )
            )
        }
    }

    class Factory(
        private val repository: CardexRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CardexViewModel(repository) as T
        }
    }
}
