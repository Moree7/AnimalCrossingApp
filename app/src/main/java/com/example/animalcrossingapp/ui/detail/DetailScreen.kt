package com.example.animalcrossingapp.ui.detail

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.animalcrossingapp.domain.CollectibleType
import com.example.animalcrossingapp.ui.common.copyToClipboard
import com.example.animalcrossingapp.ui.common.prettyLink
import com.example.animalcrossingapp.ui.common.shareToWhatsApp
import com.example.animalcrossingapp.ui.theme.*
import com.example.animalcrossingapp.viewModel.DetailViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    itemId: String,
    vm: DetailViewModel
) {
    val context = LocalContext.current
    val state by vm.uiState.collectAsState()

    var showDeleteConfirm by remember { mutableStateOf(false) }

    LaunchedEffect(itemId) {
        vm.load(itemId)
    }

    val topBarColor = state.item?.let { colorForType(it.type) } ?: Verde_Oscuro
    val topBarContentColor = Color.White

    // ✅ Dos links: uno "seguro" (encoded) y otro "bonito" (decoded)
    val linkEncoded = remember(state.item) {
        val item = state.item
        if (item == null) "" else "capturapedia://item/${Uri.encode(item.id)}"
    }
    val linkPretty = remember(linkEncoded) { prettyLink(linkEncoded) }

    // ✅ shareText usa el link bonito (sin símbolos raros)
    val shareText = remember(state.item, linkPretty) {
        val item = state.item
        if (item == null) "" else {
            buildString {
                append("Capturapedia - ${item.name}\n")
                if (item.subtitle.isNotBlank()) append("${item.subtitle}\n")
                if (item.description.isNotBlank()) append("${item.description}\n\n")
                append("Enlace: $linkPretty")
            }
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Detalles", color = topBarContentColor) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = topBarContentColor)
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (state.item == null) return@IconButton

                            // ✅ Copia el link bonito al portapapeles (más usable por humanos)
                            copyToClipboard(context, "item-link", linkPretty)

                            // ✅ WhatsApp recibe texto SIN encoding raro
                            shareToWhatsApp(context, shareText)
                        },
                        enabled = state.item != null
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Compartir", tint = topBarContentColor)
                    }

                    IconButton(
                        onClick = {
                            val item = state.item ?: return@IconButton
                            navController.navigate("editItem/${Uri.encode(item.id)}")
                        },
                        enabled = state.item != null
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = topBarContentColor)
                    }

                    IconButton(
                        onClick = { showDeleteConfirm = true },
                        enabled = state.item != null
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = topBarContentColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = topBarColor)
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            when {
                state.isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Marron_Oscuro)
                    }
                }

                state.error != null -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Rosa.copy(alpha = 0.92f)),
                        border = BorderStroke(2.dp, Marron)
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Text("Error", style = MaterialTheme.typography.titleMedium, color = Marron_Oscuro)
                            Spacer(Modifier.height(6.dp))
                            Text(state.error ?: "Error desconocido", color = Marron_Oscuro)
                        }
                    }
                }

                state.item == null -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Beige.copy(alpha = 0.92f)),
                        border = BorderStroke(2.dp, Marron)
                    ) {
                        Text("No se encontró el item.", modifier = Modifier.padding(14.dp), color = Marron_Oscuro)
                    }
                }

                else -> {
                    val item = state.item!!
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Beige.copy(alpha = 0.92f)),
                        border = BorderStroke(2.dp, Marron),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        val scroll = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .verticalScroll(scroll),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            AssistChip(
                                onClick = { },
                                label = { Text(item.type.routeValue, color = Marron_Oscuro) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = colorForType(item.type).copy(alpha = 0.85f),
                                    labelColor = Marron_Oscuro
                                ),
                                border = BorderStroke(1.dp, Marron)
                            )

                            Text(item.name, style = MaterialTheme.typography.titleLarge, color = Marron_Oscuro)

                            if (item.subtitle.isNotBlank()) {
                                Text(item.subtitle, style = MaterialTheme.typography.titleMedium, color = Marron)
                            }

                            if (item.description.isNotBlank()) {
                                Text(item.description, style = MaterialTheme.typography.bodyMedium, color = Marron_Oscuro)
                            }

                            Divider(color = Marron.copy(alpha = 0.35f))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = item.isDonated,
                                    onCheckedChange = { checked -> vm.setDonated(checked) },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Verde,
                                        uncheckedColor = Marron,
                                        checkmarkColor = Color.White
                                    )
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Donado", color = Marron_Oscuro, style = MaterialTheme.typography.titleMedium)
                            }

                            OutlinedButton(
                                onClick = {
                                    // ✅ Aquí igual: copia bonito, comparte bonito
                                    copyToClipboard(context, "item-link", linkPretty)
                                    shareToWhatsApp(context, shareText)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                border = BorderStroke(2.dp, Marron),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Marron_Oscuro,
                                    containerColor = Color.Transparent
                                )
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Compartir por WhatsApp")
                            }
                        }
                    }
                }
            }

            if (showDeleteConfirm && state.item != null) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirm = false },
                    title = { Text("Borrar item", color = Marron_Oscuro) },
                    text = { Text("¿Seguro que quieres borrar este item? Esta acción no se puede deshacer.", color = Marron_Oscuro) },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDeleteConfirm = false
                                vm.delete { navController.popBackStack() }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Rosa, contentColor = Marron_Oscuro)
                        ) { Text("Borrar") }
                    },
                    dismissButton = {
                        OutlinedButton(
                            onClick = { showDeleteConfirm = false },
                            border = BorderStroke(2.dp, Marron),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Marron_Oscuro)
                        ) { Text("Cancelar") }
                    },
                    containerColor = Beige
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
