package com.raulcn.ej506api.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class ExchangeRateResponse(
    val tasa: Double,
    val fecha: String
)

object FrankfurterApi {
    suspend fun obtenerTipoCambio(desde: String, hacia: String): ExchangeRateResponse? =
        withContext(Dispatchers.IO) {
            var conexion: HttpURLConnection? = null
            try {
                val url = URL("https://api.frankfurter.dev/v1/latest?base=$desde&symbols=$hacia")
                conexion = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 5000
                    readTimeout = 5000
                }

                if (conexion.responseCode !in 200..299) {
                    return@withContext null
                }

                val respuesta = conexion.inputStream.bufferedReader().use { it.readText() }

                val json = JSONObject(respuesta)
                val tasa = json.getJSONObject("rates").getDouble(hacia)
                val fecha = json.getString("date")

                ExchangeRateResponse(tasa = tasa, fecha = fecha)
            } catch (_: Exception) {
                null
            } finally {
                conexion?.disconnect()
            }
        }
}
