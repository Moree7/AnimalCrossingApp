package com.example.animalcrossingapp.ui.edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.animalcrossingapp.domain.CollectibleType
import com.example.animalcrossingapp.ui.theme.*
import com.example.animalcrossingapp.viewModel.EditItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemScreen(
    navController: NavController,
    itemId: String,
    vm: EditItemViewModel
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(itemId) {
        vm.load(itemId)
    }

    // TopBar con color POR ITEM si existe; si no, fallback
    val topBarColor = state.item?.let { colorForType(it.type) } ?: Verde_Oscuro
    val topBarContentColor = Color.White

    // Colores de TextField coherentes (Beige + Marrón)
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Marron,
        unfocusedBorderColor = Marron,
        focusedLabelColor = Marron_Oscuro,
        unfocusedLabelColor = Marron_Oscuro,
        cursorColor = Marron_Oscuro,

        focusedContainerColor = Beige,
        unfocusedContainerColor = Beige,

        focusedTextColor = Marron_Oscuro,
        unfocusedTextColor = Marron_Oscuro
    )

    Scaffold(
        // Transparente para que se vea tu LiquidGradientBackground global
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Editar item", color = topBarContentColor) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = topBarContentColor
                        )
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
                            Text(
                                text = "Error",
                                style = MaterialTheme.typography.titleMedium,
                                color = Marron_Oscuro
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = state.error ?: "Error desconocido",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Marron_Oscuro
                            )
                        }
                    }
                }

                state.item == null -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Beige.copy(alpha = 0.92f)),
                        border = BorderStroke(2.dp, Marron)
                    ) {
                        Text(
                            text = "No se encontró el item.",
                            modifier = Modifier.padding(14.dp),
                            color = Marron_Oscuro
                        )
                    }
                }

                else -> {
                    val item = state.item!!

                    // Campos editables: usamos los overrides existentes (pueden ser null)
                    var userName by remember(item.id) { mutableStateOf(item.userName ?: "") }
                    var userSubtitle by remember(item.id) { mutableStateOf(item.userSubtitle ?: "") }
                    var userDescription by remember(item.id) { mutableStateOf(item.userDescription ?: "") }

                    val scroll = rememberScrollState()

                    // Scroll a nivel de pantalla (más robusto) + imePadding para teclado
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scroll)
                            .imePadding(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Beige.copy(alpha = 0.92f)),
                            border = BorderStroke(2.dp, Marron),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {

                                Text(
                                    text = "Original: ${item.originalName}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Marron_Oscuro
                                )

                                if (item.originalSubtitle.isNotBlank()) {
                                    Text(
                                        text = "Subtítulo original: ${item.originalSubtitle}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Marron_Oscuro
                                    )
                                }

                                OutlinedTextField(
                                    value = userName,
                                    onValueChange = { userName = it },
                                    label = { Text("Nombre", color = Marron_Oscuro) },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    colors = fieldColors
                                )

                                OutlinedTextField(
                                    value = userSubtitle,
                                    onValueChange = { userSubtitle = it },
                                    label = { Text("Subtítulo", color = Marron_Oscuro) },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    colors = fieldColors
                                )

                                OutlinedTextField(
                                    value = userDescription,
                                    onValueChange = { userDescription = it },
                                    label = { Text("Descripción", color = Marron_Oscuro) },
                                    modifier = Modifier.fillMaxWidth(),
                                    minLines = 3,
                                    colors = fieldColors
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            vm.saveEdits(item.id, userName, userSubtitle, userDescription)
                                            navController.popBackStack()
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Marron_Oscuro,
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text("Guardar")
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            vm.restoreDefaults(item.id)
                                            navController.popBackStack()
                                        },
                                        modifier = Modifier.weight(1f),
                                        border = BorderStroke(2.dp, Marron),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = Marron_Oscuro,
                                            containerColor = Beige
                                        )
                                    ) {
                                        Text("Restaurar")
                                    }
                                }

                                AssistChip(
                                    onClick = { },
                                    label = {
                                        Text(
                                            text = "Deja un campo vacío para volver al valor original",
                                            color = Marron_Oscuro
                                        )
                                    },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = Beige,
                                        labelColor = Marron_Oscuro
                                    ),
                                    border = BorderStroke(1.dp, Marron)
                                )
                            }
                        }

                        Spacer(Modifier.height(18.dp))
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
