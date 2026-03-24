package com.example.animalcrossingapp.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.animalcrossingapp.ui.theme.Beige
import com.example.animalcrossingapp.ui.theme.Marron
import com.example.animalcrossingapp.ui.theme.Marron_Oscuro
import com.example.animalcrossingapp.ui.theme.Rosa
import com.example.animalcrossingapp.ui.theme.Verde_Oscuro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechEmergencyDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                color = Marron_Oscuro
            )
        },
        text = {
            Text(
                text = message,
                color = Marron_Oscuro
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Verde_Oscuro,
                    contentColor = Color.White
                )
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                border = BorderStroke(2.dp, Marron),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Marron_Oscuro
                )
            ) {
                Text(dismissText)
            }
        },
        containerColor = Beige,
        tonalElevation = 2.dp
    )
}
