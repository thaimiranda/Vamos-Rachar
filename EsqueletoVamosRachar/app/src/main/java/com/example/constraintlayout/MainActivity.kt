package com.example.constraintlayout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() , TextWatcher, TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var edtConta: EditText
    private lateinit var edtPessoas: EditText
    private lateinit var edtResult: TextView
    private var ttsSucess: Boolean = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtConta = findViewById<EditText>(R.id.edtConta)
        edtConta.addTextChangedListener(this)

        edtPessoas = findViewById<EditText>(R.id.edtPessoas)
        edtPessoas.addTextChangedListener(this)
        edtResult = findViewById<TextView>(R.id.edtResult)
        // Initialize TTS engine
        tts = TextToSpeech(this, this)


    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        Log.d("PDM24", "Antes de mudar")

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d("PDM24", "Mudando")
    }

    override fun afterTextChanged(s: Editable?) {
        Log.d("PDM24", "Depois de mudar")

       val conta= edtConta.text.toString()
         val pessoas= edtPessoas.text.toString()

         if(conta.isNotEmpty() && pessoas.isNotEmpty()){
             try {
                 val numberConta= conta.toDouble()
                 val numberPessoas= pessoas.toInt()
                 val resultado = (numberConta/numberPessoas).toDouble()
                 edtResult.text= "R$ %.2f".format(resultado)
             } catch (ex: NumberFormatException){
                 edtResult.text="Valor Invalido"
             }
         } else{
             edtResult.text=""
         }
    }


    fun clickFalar(v: View) {
        if (tts.isSpeaking) {
            tts.stop()
        }
        if (ttsSucess) {
            tts.speak("Resultado final foi" + edtResult.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
        }


    }
    fun clickCompartilhar(v: View){
        val conta = edtConta.text.toString()
        val pessoas = edtPessoas.text.toString()
        val resultado = edtResult.text.toString()

        try {
            val numberConta = conta.toDouble()
            val numberPessoas = pessoas.toInt()

            if (numberConta == 0.0 || numberPessoas == 0) {
                return
            }

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "O valor ficou $resultado pra cada um.")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

        } catch (e: NumberFormatException) {
            return
        }
    }

    override fun onDestroy() {
        // Release TTS engine resources
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // TTS engine is initialized successfully
            tts.language = Locale.getDefault()
            ttsSucess = true
            Log.d("PDM23", "Sucesso na Inicialização")
        } else {
            // TTS engine failed to initialize
            Log.e("PDM23", "Failed to initialize TTS engine.")
            ttsSucess = false
        }
    }


}

