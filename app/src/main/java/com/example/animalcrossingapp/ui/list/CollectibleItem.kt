package com.example.animalcrossingapp.ui.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.animalcrossingapp.domain.CollectibleType
import com.example.animalcrossingapp.ui.model.CollectibleUi
import com.example.animalcrossingapp.ui.theme.*

@Composable
fun CollectibleItem(
    item: CollectibleUi,
    onCheckedChange: (Boolean) -> Unit,
    onDetailClick: () -> Unit,
    onEditClick: () -> Unit
) {
    val containerColor = colorForType(item.type)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor.copy(alpha = 0.90f)),
        border = BorderStroke(2.dp, Marron),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = item.isDonated,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = Verde,
                    uncheckedColor = Marron,
                    checkmarkColor = Color.White
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Marron_Oscuro
                )
                if (item.subtitle.isNotBlank()) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = item.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Marron_Oscuro
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedButton(
                onClick = onDetailClick,
                border = BorderStroke(2.dp, Marron),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Marron_Oscuro,
                    containerColor = Color.Transparent
                )
            ) {
                Text("Detalles")
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = Marron_Oscuro
                )
            }
        }
    }
}

private fun colorForType(type: CollectibleType): Color = when (type) {
    CollectibleType.PECES -> Azul
    CollectibleType.PESCA_SUBMARINA -> Azul_Verde
    CollectibleType.BICHOS -> Amarillo
    CollectibleType.FOSIL -> Marron
    CollectibleType.OBRA_ARTE -> Rosa
}
