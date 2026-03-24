package com.example.animalcrossingapp.ui.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.animalcrossingapp.ui.theme.Marron
import com.example.animalcrossingapp.ui.theme.Marron_Oscuro
import com.example.animalcrossingapp.viewModel.AddItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    navController: NavController,
    typeRoute: String,
    vm: AddItemViewModel
) {
    val uiState by vm.uiState.collectAsState()

    var name by rememberSaveable { mutableStateOf("") }
    var subtitle by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    var nameError by rememberSaveable { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            vm.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Añadir ítem", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Marron_Oscuro
                )
            )
        }
    ) { innerPadding ->
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
                singleLine = true,
                label = { Text("Nombre") },
                isError = nameError,
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
                singleLine = true,
                label = { Text("Subtítulo") },
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

                    vm.save(
                        typeRoute = typeRoute,
                        name = name.trim(),
                        subtitle = subtitle.trim(),
                        description = description.trim(),
                        onDone = { navController.popBackStack() }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isSaving,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Marron_Oscuro,
                    contentColor = Color.White,
                    disabledContainerColor = Marron_Oscuro.copy(alpha = 0.35f),
                    disabledContentColor = Color.White
                )
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(18.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text("Guardar")
                }
            }
        }
    }
}