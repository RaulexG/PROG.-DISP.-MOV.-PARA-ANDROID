package com.raulcn.ej506api

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.raulcn.ej506api.data.FrankfurterApi
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

private val monedasDisponibles = listOf("MXN", "USD", "EUR")

@Composable
fun CurrencyConverterScreen(modifier: Modifier = Modifier) {
    var cantidad by remember { mutableStateOf("1") }
    var monedaOrigen by remember { mutableStateOf("USD") }
    var monedaDestino by remember { mutableStateOf("MXN") }
    var resultadoTexto by remember { mutableStateOf("Ingresa una cantidad.") }
    var fechaCambio by remember { mutableStateOf("") }
    var estaCargando by remember { mutableStateOf(false) }
    val alcance = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Conversor de Monedas",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    OutlinedTextField(
                        value = cantidad,
                        onValueChange = { cantidad = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Cantidad") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CurrencyDropdown(
                            label = "De",
                            monedaSeleccionada = monedaOrigen,
                            alSeleccionarMoneda = { monedaOrigen = it },
                            modifier = Modifier.weight(1f)
                        )

                        CurrencyDropdown(
                            label = "A",
                            monedaSeleccionada = monedaDestino,
                            alSeleccionarMoneda = { monedaDestino = it },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            val cantidadNumero = cantidad
                                .replace(',', '.')
                                .toDoubleOrNull()
                            if (cantidadNumero == null) {
                                resultadoTexto = "Cantidad no valida."
                                fechaCambio = ""
                                return@Button
                            }

                            if (monedaOrigen == monedaDestino) {
                                resultadoTexto =
                                    "${formatearCantidad(cantidadNumero)} $monedaOrigen = ${formatearCantidad(cantidadNumero)} $monedaDestino"
                                fechaCambio = ""
                                return@Button
                            }

                            alcance.launch {
                                estaCargando = true
                                val respuesta = FrankfurterApi.obtenerTipoCambio(
                                    desde = monedaOrigen,
                                    hacia = monedaDestino
                                )
                                estaCargando = false

                                if (respuesta == null) {
                                    resultadoTexto = "Error al consultar."
                                    fechaCambio = ""
                                } else {
                                    val totalConvertido = cantidadNumero * respuesta.tasa
                                    resultadoTexto =
                                        "${formatearCantidad(cantidadNumero)} $monedaOrigen = ${formatearCantidad(totalConvertido)} $monedaDestino"
                                    fechaCambio = respuesta.fecha
                                }
                            }
                        },
                        enabled = !estaCargando,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        if (estaCargando) {
                            CircularProgressIndicator(
                                modifier = Modifier.height(18.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Convertir")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Resultado",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = resultadoTexto,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    if (fechaCambio.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = fechaCambio,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyDropdown(
    label: String,
    monedaSeleccionada: String,
    alSeleccionarMoneda: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandido by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = !expandido },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = monedaSeleccionada,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido)
            },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            monedasDisponibles.forEach { moneda ->
                DropdownMenuItem(
                    text = { Text(moneda) },
                    onClick = {
                        alSeleccionarMoneda(moneda)
                        expandido = false
                    }
                )
            }
        }
    }
}

private fun formatearCantidad(valor: Double): String {
    val simbolos = DecimalFormatSymbols(Locale.US)
    val formato = DecimalFormat("#,##0.00", simbolos)
    return formato.format(valor)
}
