package com.example.ej26cardexroom.data.local

data class CardexDetalle(
    val id: Int,
    val materiaClave: String,
    val nombre: String,
    val creditos: Int,
    val semestrePlan: Int,
    val semestreCursado: Int,
    val anio: Int,
    val periodo: String,
    val calificacion: Int
)
