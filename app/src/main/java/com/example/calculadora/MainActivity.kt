package com.example.calculadora

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val vizualizar = findViewById<TextView>(R.id.vizualizar)
        val botao0 = findViewById<Button>(R.id.botao0)
        val botao1 = findViewById<Button>(R.id.botao1)
        val botao2 = findViewById<Button>(R.id.botao2)
        val botao3 = findViewById<Button>(R.id.botao3)
        val botao4 = findViewById<Button>(R.id.botao4)
        val botao5 = findViewById<Button>(R.id.botao5)
        val botao6 = findViewById<Button>(R.id.botao6)
        val botao7 = findViewById<Button>(R.id.botao7)
        val botao8 = findViewById<Button>(R.id.botao8)
        val botao9 = findViewById<Button>(R.id.botao9)
        val botaoapagar = findViewById<Button>(R.id.botaoApagar)
        val botaosub = findViewById<Button>(R.id.botaosub)
        val botaodivi = findViewById<Button>(R.id.botaodivi)
        val botaomult = findViewById<Button>(R.id.botaomult)
        val botaoadicao = findViewById<Button>(R.id.botaoadicao)
        val botaovirgula = findViewById<Button>(R.id.botaovirgula)
        val botaoresultado = findViewById<Button>(R.id.botaoresultado)

        botao0.setOnClickListener {
            if (vizualizar.text.toString() == "0") {
                vizualizar.text = ""
            }
            vizualizar.append("0")
        }

        botao1.setOnClickListener {
            if (vizualizar.text.toString() == "0") {
                vizualizar.text = ""
            }
            vizualizar.append("1")
        }

        botao2.setOnClickListener {
            if (vizualizar.text.toString() == "0") {
                vizualizar.text = ""
            }
            vizualizar.append("2")
        }

        botao3.setOnClickListener {
            if (vizualizar.text.toString() == "0") {
                vizualizar.text = ""
            }
            vizualizar.append("3")
        }

        botao4.setOnClickListener {
            if (vizualizar.text.toString() == "0") {
                vizualizar.text = ""
            }
            vizualizar.append("4")
        }

        botao5.setOnClickListener {
            if (vizualizar.text.toString() == "0") {
                vizualizar.text = ""
            }
            vizualizar.append("5")
        }

        botao6.setOnClickListener {
            if (vizualizar.text.toString() == "0") {
                vizualizar.text = ""
            }
            vizualizar.append("6")
        }

        botao7.setOnClickListener {
            if (vizualizar.text.toString() == "0") {
                vizualizar.text = ""
            }
            vizualizar.append("7")
        }

        botao8.setOnClickListener {
            if (vizualizar.text.toString() == "0") {
                vizualizar.text = ""
            }
            vizualizar.append("8")
        }

        botao9.setOnClickListener {
            if (vizualizar.text.toString() == "0") {
                vizualizar.text = ""
            }
            vizualizar.append("9")
        }

        botaoadicao.setOnClickListener {
            vizualizar.append("+")
        }

        botaosub.setOnClickListener {
            vizualizar.append("-")
        }

        botaomult.setOnClickListener {
            vizualizar.append("x")
        }

        botaodivi.setOnClickListener {
            vizualizar.append("÷")
        }

        botaovirgula.setOnClickListener {
            val textoAtual = vizualizar.text.toString()
            if (textoAtual.last() == ',') {
                return@setOnClickListener
            }
            vizualizar.append(",")
        }

        botaoapagar.setOnLongClickListener {
            vizualizar.text = "0"
            true
        }

        botaoapagar.setOnClickListener {
            val textoAtual = vizualizar.text.toString()
            if (textoAtual.isNotEmpty()) {
                vizualizar.text = textoAtual.dropLast(1)
            }
            if (textoAtual.length == 1) {
                vizualizar.text = "0"
            }
        }

        botaoresultado.setOnClickListener {
            val expressao = vizualizar.text.toString()

            try {
                val resultado = avaliarExpressao(expressao)
                vizualizar.text = resultado.toString()
            } catch (e: Exception) {
                vizualizar.text = "Erro"
            }
        }
    }

    private fun avaliarExpressao(expressao: String): BigDecimal {
        return object : Any() {
            var pos = -1
            var char: Int = 0
            fun nextChar() {
                char = if (++pos < expressao.length) expressao[pos].toInt() else -1
            }

            fun eatWhite() {
                while (Character.isWhitespace(char.toChar())) nextChar()
            }

            fun parse(): BigDecimal {
                nextChar()
                val x = parseExpression()
                if (pos < expressao.length) throw RuntimeException("Unexpected: " + char.toChar())
                return x
            }

            fun parseExpression(): BigDecimal {
                var x = parseTerm()
                while (true) {
                    when (char.toChar()) {
                        '+' -> {
                            nextChar()
                            x = x.add(parseTerm())
                        }
                        '-' -> {
                            nextChar()
                            x = x.subtract(parseTerm())
                        }
                        else -> return x
                    }
                }
            }

            fun parseTerm(): BigDecimal {
                var x = parseFactor()
                while (true) {
                    when (char.toChar()) {
                        'x' -> {
                            nextChar()
                            x = x.multiply(parseFactor())
                        }
                        '÷' -> {
                            nextChar()
                            x = x.divide(parseFactor(), 10, BigDecimal.ROUND_HALF_UP)
                        }
                        else -> return x
                    }
                }
            }

            fun parseFactor(): BigDecimal {
                eatWhite()
                val startPos = pos
                when (char.toChar()) {
                    '(' -> {
                        nextChar()
                        val x = parseExpression()
                        if (char.toChar() != ')') throw RuntimeException("Expected ')'")
                        nextChar()
                        return x
                    }
                    in '0'..'9' -> {
                        while (char.toChar() in '0'..'9') nextChar()
                        return BigDecimal(expressao.substring(startPos, pos))
                    }
                    else -> throw RuntimeException("Unexpected: " + char.toChar())
                }
            }
        }.parse()
    }
}
