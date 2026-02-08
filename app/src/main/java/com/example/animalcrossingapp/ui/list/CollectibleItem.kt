package com.example.animalcrossingapp.ui.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    val typeColor = colorForType(item.type)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = typeColor.copy(alpha = 0.90f)),
        border = BorderStroke(2.dp, Marron),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Checkbox "Donado"
            Checkbox(
                checked = item.isDonated,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = Verde,
                    uncheckedColor = Marron,
                    checkmarkColor = Color.White
                )
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Imagen pequeña (placeholder). Aquí luego puedes meter AsyncImage/Coil si quieres.
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Blanco.copy(alpha = 0.55f)),
                contentAlignment = Alignment.Center
            ) {
                Text("🖼️", color = Marron_Oscuro)
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Nombre + subtítulo
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

            Spacer(modifier = Modifier.width(10.dp))

            // Iconos en vertical (uno encima del otro)
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // Botón info con fondo circular color categoría
                IconCircleButton(
                    background = typeColor.copy(alpha = 0.25f),
                    onClick = onDetailClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Detalles",
                        tint = Marron_Oscuro
                    )
                }

                // Botón editar con fondo circular color categoría
                IconCircleButton(
                    background = typeColor.copy(alpha = 0.25f),
                    onClick = onEditClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = Marron_Oscuro
                    )
                }
            }
        }
    }
}

@Composable
private fun IconCircleButton(
    background: Color,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.size(40.dp),
        shape = CircleShape,
        color = background,
        border = BorderStroke(1.dp, Marron.copy(alpha = 0.6f))
    ) {
        IconButton(onClick = onClick) {
            content()
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
