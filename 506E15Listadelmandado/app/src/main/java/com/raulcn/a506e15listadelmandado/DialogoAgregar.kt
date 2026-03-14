package com.raulcn.a506e15listadelmandado

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun DialogoAgregar(
    mostrar: Boolean,
    nombre: String,
    onNombreChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit
) {
    if (mostrar) {
        AlertDialog(
            onDismissRequest = onCancelar,
            title = { Text("Nuevo Producto") },
            text = {
                TextField(
                    value = nombre,
                    onValueChange = onNombreChange,
                    label = { Text("Nombre del producto") }
                )
            },
            confirmButton = {
                Button(onClick = onGuardar) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                Button(onClick = onCancelar) {
                    Text("Cancelar")
                }
            }
        )
    }
}