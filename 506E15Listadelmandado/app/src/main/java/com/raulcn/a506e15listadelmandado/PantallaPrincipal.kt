package com.raulcn.a506e15listadelmandado

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PantallaPrincipal() {
    val context = LocalContext.current
    val categorias = listOf("Abarrotes", "Limpieza", "Farmacia", "Frutas")
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val categoriaSeleccionada = categorias[selectedTabIndex]


    val productos = remember {
        mutableStateListOf(
            // Abarrotes
            Producto(nombre = "Aceite", categoria = "Abarrotes"),
            Producto(nombre = "Arroz", categoria = "Abarrotes"),
            Producto(nombre = "Frijol", categoria = "Abarrotes"),
            Producto(nombre = "Azúcar", categoria = "Abarrotes"),
            Producto(nombre = "Sal de mesa", categoria = "Abarrotes"),

            // Limpieza
            Producto(nombre = "Cloro", categoria = "Limpieza"),
            Producto(nombre = "Jabón de trastes", categoria = "Limpieza"),
            Producto(nombre = "Detergente en polvo", categoria = "Limpieza"),
            Producto(nombre = "Limpiador de pisos", categoria = "Limpieza"),
            Producto(nombre = "Esponjas", categoria = "Limpieza"),

            // Farmacia
            Producto(nombre = "Paracetamol", categoria = "Farmacia"),
            Producto(nombre = "Ibuprofeno", categoria = "Farmacia"),
            Producto(nombre = "Alcohol etílico", categoria = "Farmacia"),
            Producto(nombre = "Caja de curitas", categoria = "Farmacia"),
            Producto(nombre = "Gasas estériles", categoria = "Farmacia"),

            // Frutas
            Producto(nombre = "Manzana", categoria = "Frutas"),
            Producto(nombre = "Plátano", categoria = "Frutas"),
            Producto(nombre = "Naranja", categoria = "Frutas"),
            Producto(nombre = "Papaya", categoria = "Frutas"),
            Producto(nombre = "Sandía", categoria = "Frutas")
        )
    }

    var mostrarDialogo by remember { mutableStateOf(false) }
    var nuevoProductoNombre by remember { mutableStateOf("") }
    val seleccionados = productos.filter { it.seleccionado }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "506 E1.5 Lista del Mandado",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        TabRow(selectedTabIndex = selectedTabIndex) {
            categorias.forEachIndexed { index, titulo ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(titulo) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // LISTA CON GESTIÓN DE ARTÍCULOS
        LazyColumn(modifier = Modifier.weight(1f)) {
            val listaFiltrada = productos.filter { it.categoria == categoriaSeleccionada }
            items(listaFiltrada, key = { it.id }) { producto ->
                ProductoItem(
                    producto = producto,
                    onCheckedChange = { valor ->
                        val index = productos.indexOf(producto)
                        if (index != -1) {
                            productos[index] = producto.copy(seleccionado = valor)
                        }
                    },
                    onDelete = { productos.remove(producto) }
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
        ) {
            Button(onClick = { mostrarDialogo = true }) {
                Text("Agregar")
            }

            Button(
                enabled = seleccionados.isNotEmpty(),
                onClick = {
                    val intent = Intent(context, ListaActivity::class.java)
                    intent.putStringArrayListExtra("lista", ArrayList(seleccionados.map { it.nombre }))
                    context.startActivity(intent)
                }
            ) {
                Text("Filtrar (${seleccionados.size})")
            }
        }
    }

    if (mostrarDialogo) {
        DialogoAgregar(
            mostrar = mostrarDialogo,
            nombre = nuevoProductoNombre,
            onNombreChange = { nuevoProductoNombre = it },
            onGuardar = {
                if (nuevoProductoNombre.isNotBlank()) {
                    productos.add(Producto(nombre = nuevoProductoNombre, categoria = categoriaSeleccionada))
                    nuevoProductoNombre = ""
                    mostrarDialogo = false
                }
            },
            onCancelar = {
                nuevoProductoNombre = ""
                mostrarDialogo = false
            }
        )
    }
}