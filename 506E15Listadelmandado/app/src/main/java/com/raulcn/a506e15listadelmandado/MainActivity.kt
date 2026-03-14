package com.raulcn.a506e15listadelmandado

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.raulcn.a506e15listadelmandado.ui.theme._506E15ListaDelMandadoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            _506E15ListaDelMandadoTheme {
                PantallaPrincipal()
            }
        }
    }
}

