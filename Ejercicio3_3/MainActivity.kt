package com.example.actividad3_3

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.actividad3_3.databinding.ActivityMainBinding

lateinit var binding: ActivityMainBinding
lateinit var viewModel: MainActivityViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inicializarBinding()
        inicializarViewModel()

        binding.btnIniciar.setOnClickListener {
            viewModel.segundosElegidos = Integer.parseInt(binding.txtSegundos.text.toString())
            binding.chrCronometro.visibility = View.VISIBLE
            viewModel.tiempoBase = SystemClock.elapsedRealtime() + viewModel.segundosElegidos * 1000
            binding.chrCronometro.base = viewModel.tiempoBase
            binding.chrCronometro.start()
            viewModel.cronometroActivo = true
            binding.txtSegundos.isEnabled = false
            binding.btnIniciar.isEnabled = false


            binding.chrCronometro.setOnChronometerTickListener {
                val milisegundosTranscurridos = SystemClock.elapsedRealtime()
                if (milisegundosTranscurridos >= viewModel.tiempoBase) {
                    detenerCronometro()
                }
            }
        }
    }

    private fun inicializarBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun inicializarViewModel() {
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }

    private fun detenerCronometro() {
        binding.chrCronometro.stop()
        viewModel.cronometroActivo = false
        binding.txtSegundos.isEnabled = true
        binding.btnIniciar.isEnabled = true
        binding.chrCronometro.visibility = View.INVISIBLE
        Toast.makeText(this, "Cuenta atrás finalizada", Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()
        if (viewModel.cronometroActivo) {
            viewModel.tiempoBase = binding.chrCronometro.base
            binding.chrCronometro.stop()
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.cronometroActivo) {
            restaurarCronometro()
        }
    }

    private fun restaurarCronometro() {
        binding.chrCronometro.base = viewModel.tiempoBase
        binding.chrCronometro.start()
        binding.txtSegundos.isEnabled = false
        binding.btnIniciar.isEnabled = false
        binding.chrCronometro.visibility = View.VISIBLE

        // Asegura que el cronometro pare cuando se haga un cambio estructural
        binding.chrCronometro.setOnChronometerTickListener {
            val milisegundosTranscurridos = SystemClock.elapsedRealtime()
            if (milisegundosTranscurridos >= viewModel.tiempoBase) {
                detenerCronometro()
            }
        }
    }
}
