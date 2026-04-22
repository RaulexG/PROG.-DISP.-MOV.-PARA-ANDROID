package com.example.ej26cardexroom.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "materias")
data class MateriaEntity(
    @PrimaryKey
    val clave: String,
    val nombre: String,
    val creditos: Int,
    val semestrePlan: Int
)
