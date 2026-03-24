package com.example.animalcrossingapp.ui.edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.animalcrossingapp.ui.theme.*
import com.example.animalcrossingapp.viewModel.EditItemViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemScreen(
    navController: NavController,
    itemId: String,
    vm: EditItemViewModel
) {
    val state by vm.uiState.collectAsState()

    var name by rememberSaveable { mutableStateOf("") }
    var subtitle by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    var initialized by rememberSaveable { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    var nameError by rememberSaveable { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(itemId) {
        vm.load(itemId)
    }

    LaunchedEffect(state.item) {
        val item = state.item
        if (item != null && !initialized) {
            name = item.name
            subtitle = item.subtitle
            description = item.description
            initialized = true
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text("Editar ítem", color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Marron_Oscuro
                )
            )
        }
    ) { innerPadding ->

        when {
            state.isLoading && !initialized -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Marron_Oscuro)
                }
            }

            state.error != null && !initialized -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    Card(border = BorderStroke(1.dp, Marron)) {
                        Text(
                            text = state.error ?: "Error",
                            modifier = Modifier.padding(16.dp),
                            color = Marron_Oscuro
                        )
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            if (it.isNotBlank()) nameError = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Nombre") },
                        isError = nameError,
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Marron,
                            unfocusedBorderColor = Marron.copy(alpha = 0.6f),
                            focusedLabelColor = Marron_Oscuro,
                            unfocusedLabelColor = Marron_Oscuro,
                            cursorColor = Marron_Oscuro
                        )
                    )

                    if (nameError) {
                        Text(
                            text = "El nombre es obligatorio",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    OutlinedTextField(
                        value = subtitle,
                        onValueChange = { subtitle = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Subtítulo") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Marron,
                            unfocusedBorderColor = Marron.copy(alpha = 0.6f),
                            focusedLabelColor = Marron_Oscuro,
                            unfocusedLabelColor = Marron_Oscuro,
                            cursorColor = Marron_Oscuro
                        )
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        label = { Text("Descripción") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Marron,
                            unfocusedBorderColor = Marron.copy(alpha = 0.6f),
                            focusedLabelColor = Marron_Oscuro,
                            unfocusedLabelColor = Marron_Oscuro,
                            cursorColor = Marron_Oscuro
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            if (name.isBlank()) {
                                nameError = true
                                return@Button
                            }

                            vm.saveItem(
                                itemId = itemId,
                                name = name.trim(),
                                subtitle = subtitle.trim(),
                                description = description.trim()
                            )

                            navController.popBackStack()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Marron_Oscuro,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Guardar cambios")
                    }

                    OutlinedButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(2.dp, Marron),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Marron_Oscuro
                        )
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Eliminar ítem")
                    }
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Confirmar eliminación", color = Marron_Oscuro) },
                text = {
                    Text(
                        "¿Seguro que quieres eliminar este ítem? Esta acción no se puede deshacer.",
                        color = Marron_Oscuro
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDeleteDialog = false
                            vm.deleteItem(itemId) { success ->
                                if (success) {
                                    navController.popBackStack()

                                    scope.launch {
                                        val result = snackbarHostState.showSnackbar(
                                            message = "Ítem eliminado",
                                            actionLabel = "Deshacer"
                                        )

                                        if (result == SnackbarResult.ActionPerformed) {
                                            vm.restoreDeletedItem()
                                        }
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Rosa,
                            contentColor = Marron_Oscuro
                        )
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { showDeleteDialog = false },
                        border = BorderStroke(1.dp, Marron),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Marron_Oscuro
                        )
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}