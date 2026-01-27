package com.example.animalcrossingapp.ui.add

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.animalcrossingapp.domain.CollectibleType
import com.example.animalcrossingapp.ui.theme.*
import com.example.animalcrossingapp.viewModel.AddItemViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    navController: NavController,
    typeRoute: String,
    vm: AddItemViewModel
) {
    val state by vm.uiState.collectAsState()

    var name by rememberSaveable { mutableStateOf("") }
    var subtitle by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    val collectibleType = remember(typeRoute) { CollectibleType.fromRoute(typeRoute) }
    val appBarColor = colorForType(collectibleType)
    val titleColor = Color.White

    // Texto del botón: automático para contraste
    val saveTextColor = if (appBarColor == Marron || appBarColor == Verde_Oscuro) Color.White else Marron_Oscuro

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
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Añadir a $typeRoute", color = titleColor) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = titleColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = appBarColor)
            )
        }
    ) { padding ->

        val scroll = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scroll),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (state.error != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Rosa),
                    border = BorderStroke(2.dp, Marron)
                ) {
                    Text(
                        text = state.error!!,
                        modifier = Modifier.padding(12.dp),
                        color = Marron_Oscuro
                    )
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre (obligatorio)", color = Marron_Oscuro) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = fieldColors
            )

            OutlinedTextField(
                value = subtitle,
                onValueChange = { subtitle = it },
                label = { Text("Subtítulo", color = Marron_Oscuro) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = fieldColors
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción", color = Marron_Oscuro) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                colors = fieldColors
            )

            Spacer(Modifier.height(6.dp))

            Button(
                onClick = {
                    vm.save(typeRoute, name, subtitle, description) {
                        navController.popBackStack()
                    }
                },
                enabled = !state.isSaving,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = appBarColor,   // color por categoría
                    contentColor = saveTextColor
                )
            ) {
                if (state.isSaving) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = saveTextColor
                        )
                        Text("Guardando…")
                    }
                } else {
                    Text("Guardar")
                }
            }

            AssistChip(
                onClick = { },
                label = { Text("Se añadirá en: $typeRoute", color = Marron_Oscuro) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = Beige,
                    labelColor = Marron_Oscuro
                ),
                border = BorderStroke(1.dp, Marron)
            )
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
