package com.raulcn.a506e15listadelmandado

data class Producto(
    val id: Long = System.currentTimeMillis() + (0..1000).random(),
    val nombre: String,
    val categoria: String,
    val seleccionado: Boolean = false
)