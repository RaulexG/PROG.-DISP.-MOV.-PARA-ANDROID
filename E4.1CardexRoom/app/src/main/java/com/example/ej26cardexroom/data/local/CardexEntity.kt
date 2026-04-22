package com.example.ej26cardexroom.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cardex",
    foreignKeys = [
        ForeignKey(
            entity = MateriaEntity::class,
            parentColumns = ["clave"],
            childColumns = ["materiaClave"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["materiaClave"], unique = true)
    ]
)
data class CardexEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val materiaClave: String,
    val semestreCursado: Int,
    val anio: Int,
    val periodo: String,
    val calificacion: Int
)
