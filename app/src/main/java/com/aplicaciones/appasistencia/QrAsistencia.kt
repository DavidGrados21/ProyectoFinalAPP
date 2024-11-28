package com.aplicaciones.appasistencia

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.util.*

class QrAsistencia : AppCompatActivity() {

    private lateinit var qrImageView: ImageView
    private lateinit var tvInfo: TextView
    private val handler = Handler(Looper.getMainLooper())
    private var idAsistencia: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_asistencia)

        idAsistencia = intent.getIntExtra("ID_Asistencia", -1)
        tvInfo = findViewById(R.id.tvInfoQR)
        qrImageView = findViewById(R.id.ivQR)

        tvInfo.text = "Generando QR para Asistencia ID: $idAsistencia"

        // Inicia la actualización periódica del QR
        startQrUpdates()
    }

    private fun startQrUpdates() {
        handler.post(object : Runnable {
            override fun run() {
                generateQrCode()
                handler.postDelayed(this, 4000) // Actualiza cada 3 segundos
            }
        })
    }

    private fun generateQrCode() {
        try {
            // Genera contenido dinámico para el QR (puedes usar cualquier lógica)
            val randomContent = "$idAsistencia-${UUID.randomUUID()}"
            val barcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barcodeEncoder.encodeBitmap(
                randomContent,
                BarcodeFormat.QR_CODE,
                500,
                500
            )
            qrImageView.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // Detener actualizaciones al destruir la actividad
    }
}
