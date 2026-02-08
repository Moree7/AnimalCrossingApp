package com.example.animalcrossingapp.ui.common

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

/**
 * Copia texto al portapapeles
 */
fun copyToClipboard(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
    Toast.makeText(context, "Copiado al portapapeles", Toast.LENGTH_SHORT).show()
}

/**
 * Comparte texto directamente a WhatsApp.
 * Si WhatsApp no está instalado, abre el chooser normal.
 */
fun shareToWhatsApp(context: Context, text: String) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
        // Intentamos forzar WhatsApp:
        setPackage("com.whatsapp")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    try {
        context.startActivity(sendIntent)
    } catch (e: Exception) {
        // Si no está WhatsApp o falla, abrimos el chooser genérico:
        val chooser = Intent.createChooser(
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            },
            "Compartir"
        )
        context.startActivity(chooser)
    }
}

/**
 * Convierte un link codificado (con %7C, %20, etc.) en un link "bonito" para humanos.
 * Ejemplo: capturapedia://item/Peces%7Canchovy -> capturapedia://item/Peces|anchovy
 */
fun prettyLink(encodedLink: String): String = Uri.decode(encodedLink)
