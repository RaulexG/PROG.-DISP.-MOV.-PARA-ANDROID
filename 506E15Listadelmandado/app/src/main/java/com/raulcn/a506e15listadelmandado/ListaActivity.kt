package com.raulcn.a506e15listadelmandado

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.raulcn.a506e15listadelmandado.ui.theme._506E15ListaDelMandadoTheme

class ListaActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lista =
            intent.getStringArrayListExtra("lista") ?: arrayListOf()

        setContent {
            _506E15ListaDelMandadoTheme {
                ListaActivaScreen(
                    lista = lista,
                    onVolver = { finish() }
                )
            }
        }
    }
}