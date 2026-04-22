package com.example.ej26cardexroom

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ej26cardexroom.data.local.AppDatabase
import com.example.ej26cardexroom.data.local.CardexDetalle
import com.example.ej26cardexroom.data.local.CardexRepository
import com.example.ej26cardexroom.data.local.MateriaEntity
import com.example.ej26cardexroom.ui.theme.Ej26CardexRoomTheme
import com.example.ej26cardexroom.viewmodel.CardexViewModel
import com.example.ej26cardexroom.viewmodel.ModoFiltro
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: CardexViewModel by viewModels {
        val db = AppDatabase.getDatabase(this)
        val repository = CardexRepository(
            materiaDao = db.materiaDao(),
            cardexDao = db.cardexDao()
        )
        CardexViewModel.Factory(repository)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Ej26CardexRoomTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CardexScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardexScreen(
    viewModel: CardexViewModel = viewModel()
) {
    val materiasDisponibles by viewModel.materiasFaltantes.collectAsStateWithLifecycle()
    val registros by viewModel.registros.collectAsStateWithLifecycle()
    val faltantes by viewModel.materiasFaltantes.collectAsStateWithLifecycle()
    val creditosMostrados by viewModel.creditosMostrados.collectAsStateWithLifecycle()
    val etiquetaCreditos by viewModel.etiquetaCreditos.collectAsStateWithLifecycle()
    val promedio by viewModel.promedio.collectAsStateWithLifecycle()
    val modoFiltro by viewModel.modoFiltroState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var semestreFiltroTexto by remember { mutableStateOf("") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text(
                        "Cardex con Room",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FormularioCardex(
                materiasDisponibles = materiasDisponibles,
                materiaSeleccionada = viewModel.materiaSeleccionada,
                semestreCursado = viewModel.semestreCursado,
                anio = viewModel.anio,
                periodo = viewModel.periodo,
                calificacion = viewModel.calificacion,
                editando = viewModel.editandoId != null,
                onMateriaSeleccionada = { viewModel.seleccionarMateria(it) },
                onSemestreChange = { viewModel.semestreCursado = it },
                onAnioChange = { viewModel.anio = it },
                onPeriodoChange = { viewModel.periodo = it },
                onCalificacionChange = { viewModel.calificacion = it },
                onGuardar = {
                    scope.launch {
                        val mensaje = viewModel.guardar()
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                    }
                },
                onLimpiar = { viewModel.limpiarFormulario() }
            )

            ResumenCard(
                creditos = creditosMostrados,
                etiquetaCreditos = etiquetaCreditos,
                promedio = promedio
            )

            FiltrosCard(
                modoFiltro = modoFiltro,
                semestreFiltroTexto = semestreFiltroTexto,
                onModoSeleccionado = { viewModel.setModoFiltro(it) },
                onSemestreFiltroChange = {
                    semestreFiltroTexto = it
                    viewModel.setSemestreFiltro(it)
                }
            )

            when (modoFiltro) {
                ModoFiltro.FALTANTES -> {
                    Text(
                        text = "Materias faltantes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    ListaFaltantes(faltantes)
                }

                else -> {
                    Text(
                        text = "Registros del cardex",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    ListaRegistros(
                        registros = registros,
                        onEditar = { viewModel.cargarParaEditar(it) },
                        onEliminar = { viewModel.eliminar(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun FormularioCardex(
    materiasDisponibles: List<MateriaEntity>,
    materiaSeleccionada: MateriaEntity?,
    semestreCursado: String,
    anio: String,
    periodo: String,
    calificacion: String,
    editando: Boolean,
    onMateriaSeleccionada: (MateriaEntity) -> Unit,
    onSemestreChange: (String) -> Unit,
    onAnioChange: (String) -> Unit,
    onPeriodoChange: (String) -> Unit,
    onCalificacionChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onLimpiar: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = if (editando) "Editar registro" else "Nuevo registro",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Selecciona una materia y captura tus datos.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text("Materia", style = MaterialTheme.typography.labelLarge)
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = materiaSeleccionada?.let { "${it.clave} - ${it.nombre}" }
                                ?: if (materiasDisponibles.isEmpty()) "No hay materias disponibles" else "Selecciona una materia",
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Icon(
                            painter = painterResource(android.R.drawable.arrow_down_float),
                            contentDescription = "Abrir materias"
                        )
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    materiasDisponibles.forEach { materia ->
                        DropdownMenuItem(
                            text = { Text("${materia.clave} - ${materia.nombre}") },
                            onClick = {
                                onMateriaSeleccionada(materia)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Text(
                text = "${materiasDisponibles.size} materias disponibles",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = semestreCursado,
                onValueChange = { onSemestreChange(it.filter(Char::isDigit)) },
                label = { Text("Semestre en que se curso") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = anio,
                onValueChange = { onAnioChange(it.filter(Char::isDigit)) },
                label = { Text("Año en que se curso") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            Text("Periodo", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = periodo == "Enero-Junio",
                    onClick = { onPeriodoChange("Enero-Junio") },
                    label = { Text("Enero-Junio") }
                )
                FilterChip(
                    selected = periodo == "Agosto-Diciembre",
                    onClick = { onPeriodoChange("Agosto-Diciembre") },
                    label = { Text("Agosto-Diciembre") }
                )
            }

            OutlinedTextField(
                value = calificacion,
                onValueChange = { onCalificacionChange(it.filter(Char::isDigit)) },
                label = { Text("Calificacion") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onGuardar,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(if (editando) "Actualizar" else "Guardar")
                }
                TextButton(onClick = onLimpiar) {
                    Text("Limpiar")
                }
            }
        }
    }
}

@Composable
fun ResumenCard(
    creditos: Int,
    etiquetaCreditos: String,
    promedio: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                "Resumen academico",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                "$etiquetaCreditos: $creditos",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                "Promedio: %.2f".format(promedio),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun FiltrosCard(
    modoFiltro: ModoFiltro,
    semestreFiltroTexto: String,
    onModoSeleccionado: (ModoFiltro) -> Unit,
    onSemestreFiltroChange: (String) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Filtros",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = modoFiltro == ModoFiltro.TODOS,
                    onClick = { onModoSeleccionado(ModoFiltro.TODOS) },
                    label = { Text("Todos") }
                )
                FilterChip(
                    selected = modoFiltro == ModoFiltro.POR_SEMESTRE,
                    onClick = { onModoSeleccionado(ModoFiltro.POR_SEMESTRE) },
                    label = { Text("Por semestre") }
                )
                FilterChip(
                    selected = modoFiltro == ModoFiltro.FALTANTES,
                    onClick = { onModoSeleccionado(ModoFiltro.FALTANTES) },
                    label = { Text("Faltantes") }
                )
            }

            if (modoFiltro == ModoFiltro.POR_SEMESTRE) {
                OutlinedTextField(
                    value = semestreFiltroTexto,
                    onValueChange = onSemestreFiltroChange,
                    label = { Text("Semestre a filtrar") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )
            }
        }
    }
}

@Composable
fun ListaRegistros(
    registros: List<CardexDetalle>,
    onEditar: (CardexDetalle) -> Unit,
    onEliminar: (CardexDetalle) -> Unit
) {
    if (registros.isEmpty()) {
        Text("No hay registros.")
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        registros.forEach { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        "${item.materiaClave} - ${item.nombre}",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text("Creditos: ${item.creditos}")
                    Text("Semestre cursado: ${item.semestreCursado}")
                    Text("Anio: ${item.anio}")
                    Text("Periodo: ${item.periodo}")
                    Text("Calificacion: ${item.calificacion}")

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = { onEditar(item) }) {
                            Text("Editar")
                        }
                        TextButton(onClick = { onEliminar(item) }) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListaFaltantes(faltantes: List<MateriaEntity>) {
    if (faltantes.isEmpty()) {
        Text("No hay materias faltantes.")
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        faltantes.forEach { materia ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        "${materia.clave} - ${materia.nombre}",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text("Creditos: ${materia.creditos}")
                    Text("Semestre del plan: ${materia.semestrePlan}")
                }
            }
        }
    }
}
