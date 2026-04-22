package com.example.ej26cardexroom.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CardexDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(cardex: CardexEntity)

    @Update
    suspend fun update(cardex: CardexEntity)

    @Delete
    suspend fun delete(cardex: CardexEntity)

    @Query("SELECT * FROM cardex WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): CardexEntity?

    @Query("""
        SELECT 
            c.id AS id,
            c.materiaClave AS materiaClave,
            m.nombre AS nombre,
            m.creditos AS creditos,
            m.semestrePlan AS semestrePlan,
            c.semestreCursado AS semestreCursado,
            c.anio AS anio,
            c.periodo AS periodo,
            c.calificacion AS calificacion
        FROM cardex c
        INNER JOIN materias m ON m.clave = c.materiaClave
        ORDER BY c.semestreCursado, c.anio, c.periodo, c.materiaClave
    """)
    fun getAllDetalles(): Flow<List<CardexDetalle>>

    @Query("""
        SELECT 
            c.id AS id,
            c.materiaClave AS materiaClave,
            m.nombre AS nombre,
            m.creditos AS creditos,
            m.semestrePlan AS semestrePlan,
            c.semestreCursado AS semestreCursado,
            c.anio AS anio,
            c.periodo AS periodo,
            c.calificacion AS calificacion
        FROM cardex c
        INNER JOIN materias m ON m.clave = c.materiaClave
        WHERE c.semestreCursado = :semestre
        ORDER BY c.anio, c.periodo, c.materiaClave
    """)
    fun getDetallesBySemestre(semestre: Int): Flow<List<CardexDetalle>>

    @Query("""
        SELECT COALESCE(SUM(m.creditos), 0)
        FROM cardex c
        INNER JOIN materias m ON m.clave = c.materiaClave
        WHERE c.calificacion >= 70
    """)
    fun getCreditosAprobados(): Flow<Int>

    @Query("""
        SELECT COALESCE(AVG(calificacion), 0.0)
        FROM cardex
    """)
    fun getPromedio(): Flow<Double>
}
