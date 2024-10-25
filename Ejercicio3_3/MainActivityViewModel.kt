package com.example.actividad3_3

import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    var segundosElegidos = 0
    var tiempoBase:Long = 0
    var cronometroActivo = false
}