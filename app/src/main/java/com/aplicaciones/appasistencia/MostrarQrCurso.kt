package com.aplicaciones.appasistencia

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Bitmap
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
class MostrarQrCurso : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mostrar_qr_curso)
        // Obtener el ID del curso del Intent
        val cursoId = intent.getIntExtra("ID_Curso", -1)

        // Verificar que el ID sea válido
        if (cursoId != -1) {
            // Generar el QR
            val barcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barcodeEncoder.encodeBitmap(
                cursoId.toString(),
                BarcodeFormat.QR_CODE,
                400,
                400
            )

            // Mostrar el QR en un ImageView
            val qrImageView: ImageView = findViewById(R.id.ivQrCode)
            qrImageView.setImageBitmap(bitmap)
        }
    }
}