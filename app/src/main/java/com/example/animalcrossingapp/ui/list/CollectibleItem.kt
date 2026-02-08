package com.example.animalcrossingapp.ui.list

import androidx.compose.foundation.BorderStroke
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Checkbox (Donado)
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

            // Placeholder imagen pequeña (para que luego puedas meter una imagen real)
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                // “tarjetita” beige dentro
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Beige.copy(alpha = 0.92f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Marron)
                ) {}
            }

            Spacer(modifier = Modifier.width(12.dp))

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

            // Iconos apilados (Info + Edit) para “hacer la tarjeta más grande”
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // INFO con fondo circular del color de la categoría
                Surface(
                    onClick = onDetailClick,
                    shape = CircleShape,
                    color = typeColor, // ✅ color categoría
                    border = BorderStroke(2.dp, Marron),
                    tonalElevation = 2.dp,
                    shadowElevation = 0.dp
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Detalles",
                            tint = Color.White
                        )
                    }
                }

                // EDIT (lápiz) con fondo beige (más neutro)
                Surface(
                    onClick = onEditClick,
                    shape = CircleShape,
                    color = Beige.copy(alpha = 0.92f),
                    border = BorderStroke(2.dp, Marron),
                    tonalElevation = 2.dp,
                    shadowElevation = 0.dp
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp),
                        contentAlignment = Alignment.Center
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
}

private fun colorForType(type: CollectibleType): Color = when (type) {
    CollectibleType.PECES -> Azul
    CollectibleType.PESCA_SUBMARINA -> Azul_Verde
    CollectibleType.BICHOS -> Amarillo
    CollectibleType.FOSIL -> Marron
    CollectibleType.OBRA_ARTE -> Rosa
}
