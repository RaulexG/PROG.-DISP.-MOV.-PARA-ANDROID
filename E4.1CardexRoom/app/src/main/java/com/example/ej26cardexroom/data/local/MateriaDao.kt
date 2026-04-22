package com.example.ej26cardexroom.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface MateriaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(materias: List<MateriaEntity>)

    @Query("DELETE FROM materias")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM materias")
    suspend fun countMaterias(): Int

    @Query("SELECT * FROM materias ORDER BY semestrePlan, clave")
    fun getAllMaterias(): Flow<List<MateriaEntity>>

    @Query("""
        SELECT * FROM materias
        WHERE clave NOT IN (SELECT materiaClave FROM cardex)
        ORDER BY semestrePlan, clave
    """)
    fun getMateriasFaltantes(): Flow<List<MateriaEntity>>

    @Transaction
    suspend fun replaceAll(materias: List<MateriaEntity>) {
        deleteAll()
        insertAll(materias)
    }
}
