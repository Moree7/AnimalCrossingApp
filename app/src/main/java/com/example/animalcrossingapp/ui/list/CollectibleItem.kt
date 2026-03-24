package com.example.animalcrossingapp.ui.list

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.material3.SwipeToDismissBoxValue.EndToStart
import androidx.compose.material3.SwipeToDismissBoxValue.Settled
import androidx.compose.material3.SwipeToDismissBoxValue.StartToEnd
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.animalcrossingapp.domain.CollectibleType
import com.example.animalcrossingapp.ui.model.CollectibleUi
import com.example.animalcrossingapp.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectibleItem(
    item: CollectibleUi,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit,
    onDetailClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteSwipe: () -> Unit
) {
    val containerColor = colorForType(item.type)
    val context = LocalContext.current

    var visible by remember(item.id) { mutableStateOf(true) }

    val dismissState = rememberSwipeToDismissBoxState()

    LaunchedEffect(dismissState.currentValue) {
        when (dismissState.currentValue) {
            StartToEnd -> {
                onCheckedChange(true)
                dismissState.snapTo(Settled)
            }
            EndToStart -> {
                visible = false
                delay(220)
                onDeleteSwipe()
                dismissState.snapTo(Settled)
            }
            Settled -> Unit
        }
    }

    AnimatedVisibility(
        visible = visible,
        exit = fadeOut(animationSpec = tween(220)) +
                shrinkVertically(animationSpec = tween(220))
    ) {
        SwipeToDismissBox(
            state = dismissState,
            enableDismissFromStartToEnd = true,
            enableDismissFromEndToStart = true,
            backgroundContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentAlignment = when (dismissState.targetValue) {
                        StartToEnd -> Alignment.CenterStart
                        EndToStart -> Alignment.CenterEnd
                        else -> Alignment.Center
                    }
                ) {
                    when (dismissState.targetValue) {
                        StartToEnd -> {
                            Text(
                                text = "✔ Donado",
                                color = Verde,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        EndToStart -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = Rosa
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Eliminar",
                                    color = Rosa,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                        else -> Unit
                    }
                }
            }
        ) {
            Card(
                modifier = modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = containerColor.copy(alpha = 0.90f)
                ),
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

                    item.imageResId?.let { imageRes ->
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = item.name,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }

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

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = onEditClick) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = Marron_Oscuro
                            )
                        }

                        IconButton(
                            onClick = {
                                val shareText = buildShareText(item)

                                val whatsappIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                    `package` = "com.whatsapp"
                                }

                                try {
                                    context.startActivity(whatsappIntent)
                                } catch (_: Exception) {
                                    val fallbackIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, shareText)
                                    }

                                    context.startActivity(
                                        Intent.createChooser(
                                            fallbackIntent,
                                            "Compartir ítem"
                                        )
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Compartir por WhatsApp",
                                tint = Marron_Oscuro
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        IconButton(onClick = onDetailClick) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Detalles",
                                tint = Marron_Oscuro
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun buildShareText(item: CollectibleUi): String {
    return buildString {
        append("🌿 Animal Crossing\n\n")
        append(item.name)

        if (item.subtitle.isNotBlank()) {
            append("\n${item.subtitle}")
        }

        if (item.description.isNotBlank()) {
            append("\n\n${item.description}")
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