package com.example.ej26cardexroom.data.local

import kotlinx.coroutines.flow.Flow

class CardexRepository(
    private val materiaDao: MateriaDao,
    private val cardexDao: CardexDao
) {

    val materias: Flow<List<MateriaEntity>> = materiaDao.getAllMaterias()
    val materiasFaltantes: Flow<List<MateriaEntity>> = materiaDao.getMateriasFaltantes()
    val creditosAprobados: Flow<Int> = cardexDao.getCreditosAprobados()
    val promedio: Flow<Double> = cardexDao.getPromedio()

    fun getRegistros(): Flow<List<CardexDetalle>> = cardexDao.getAllDetalles()

    fun getRegistrosBySemestre(semestre: Int): Flow<List<CardexDetalle>> =
        cardexDao.getDetallesBySemestre(semestre)

    suspend fun insertRegistro(registro: CardexEntity) = cardexDao.insert(registro)
    suspend fun updateRegistro(registro: CardexEntity) = cardexDao.update(registro)
    suspend fun deleteRegistro(registro: CardexEntity) = cardexDao.delete(registro)
    suspend fun getRegistroById(id: Int) = cardexDao.getById(id)

    suspend fun seedMateriasSiNecesario() {
        if (materiaDao.countMaterias() < materiasDelKardex.size) {
            materiaDao.insertAll(materiasDelKardex)
        }
    }

    companion object {
        private val materiasDelKardex = listOf(
            MateriaEntity("ACF0901", "Calculo Diferencial", 5, 1),
            MateriaEntity("AED1285", "Fundamentos de Programacion", 5, 1),
            MateriaEntity("ACA0907", "Taller de Etica", 4, 1),
            MateriaEntity("AEF1041", "Matematicas Discretas", 5, 1),
            MateriaEntity("SCH1024", "Taller de Administracion", 4, 1),
            MateriaEntity("ACC0906", "Fundamentos de Investigacion", 4, 1),

            MateriaEntity("AED1286", "Programacion Orientada a Objetos", 5, 2),
            MateriaEntity("ACF0902", "Calculo Integral", 5, 2),
            MateriaEntity("AEC1008", "Contabilidad Financiera", 4, 2),
            MateriaEntity("AEC1058", "Quimica", 4, 2),
            MateriaEntity("ACF0903", "Algebra Lineal", 5, 2),
            MateriaEntity("SCF1006", "Fisica General", 5, 2),
            MateriaEntity("ACD0908", "Desarrollo Sustentable", 5, 2),

            MateriaEntity("ACF0904", "Calculo Vectorial", 5, 3),
            MateriaEntity("AED1026", "Estructura de Datos", 5, 3),
            MateriaEntity("SCA1026", "Taller de Sistemas Operativos", 4, 3),
            MateriaEntity("SCC1013", "Investigacion de Operaciones", 4, 3),
            MateriaEntity("AEF1031", "Fundamentos de Bases de Datos", 5, 3),
            MateriaEntity("SCD1018", "Principios Electricos y Aplicaciones Digitales", 5, 3),
            MateriaEntity("AEF1052", "Probabilidad y Estadistica", 5, 3),

            MateriaEntity("ACF0905", "Ecuaciones Diferenciales", 5, 4),
            MateriaEntity("SCC1017", "Metodos Numericos", 4, 4),
            MateriaEntity("SCD1027", "Topicos Avanzados de Programacion", 5, 4),
            MateriaEntity("SCA1025", "Taller de Bases de Datos", 4, 4),
            MateriaEntity("SCD1022", "Simulacion", 5, 4),
            MateriaEntity("SCD1003", "Arquitectura de Computadoras", 5, 4),
            MateriaEntity("AEC1034", "Fundamentos de Telecomunicaciones", 4, 4),

            MateriaEntity("AEB1055", "Programacion Web", 5, 5),
            MateriaEntity("SCD1021", "Redes de Computadoras", 5, 5),
            MateriaEntity("AEC1061", "Sistemas Operativos", 4, 5),
            MateriaEntity("SCC1007", "Fundamentos de Ingenieria de Software", 4, 5),
            MateriaEntity("SCD1015", "Lenguajes y Automatas I", 5, 5),
            MateriaEntity("SCB1001", "Administracion de Bases de Datos", 5, 5),

            MateriaEntity("SCD1016", "Lenguajes y Automatas II", 5, 6),
            MateriaEntity("SCC1005", "Cultura Empresarial", 4, 6),
            MateriaEntity("SCD1004", "Conmutacion y Enrutamiento de Redes de Datos", 5, 6),
            MateriaEntity("SCD1011", "Ingenieria de Software", 5, 6),
            MateriaEntity("SCC1014", "Lenguajes de Interfaz", 4, 6),
            MateriaEntity("SCC1010", "Graficacion", 4, 6),

            MateriaEntity("SCC1023", "Sistemas Programables", 4, 7),
            MateriaEntity("ACA0909", "Taller de Investigacion I", 4, 7),
            MateriaEntity("SCA1002", "Administracion de Redes", 4, 7),
            MateriaEntity("SCC-1019", "Programacion Logica y Funcional", 4, 7),
            MateriaEntity("SCG1009", "Gestion de Proyectos de Software", 6, 7),
            MateriaEntity("WMD-2301", "Proyecto de Proyectos y Planes Tecnologicos", 5, 7),
            MateriaEntity("WMD-2302", "E-Business", 5, 7)
        )
    }
}
