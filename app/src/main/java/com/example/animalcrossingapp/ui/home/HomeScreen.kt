package com.example.animalcrossingapp.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.animalcrossingapp.data.remote.NetworkModule
import com.example.animalcrossingapp.data.repository.FishRepository
import com.example.animalcrossingapp.ui.theme.*
import com.example.animalcrossingapp.viewModel.FishViewModel
import com.example.animalcrossingapp.viewModel.FishViewModelFactory

@Composable
fun HomeScreen(navController: NavController) {

    // MVVM: Repository + Factory + ViewModel (CORRECTO: FishViewModel)
    val repository: FishRepository = remember { FishRepository(NetworkModule.api) }
    val factory: FishViewModelFactory = remember { FishViewModelFactory(repository) }
    val vm: FishViewModel = viewModel(factory = factory)

    // Estado observable
    val state by vm.uiState.collectAsState()

    // Cargar una vez
    LaunchedEffect(Unit) {
        vm.loadFish()
    }

    Scaffold(containerColor = Color.Transparent) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Museo",
                    color = Verde_Oscuro,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Animal Crossing",
                    color = Marron,
                    fontFamily = FinkHeavyFont,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                when {
                    state.isLoading -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = Verde_Oscuro
                            )
                            Text(
                                text = "Cargando datos…",
                                color = Marron_Oscuro,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    state.error != null -> {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            colors = CardDefaults.cardColors(containerColor = Rosa),
                            border = BorderStroke(1.dp, Marron_Oscuro)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "No se pudo cargar la API",
                                    color = Marron_Oscuro,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = state.error ?: "Error desconocido",
                                    color = Marron_Oscuro,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    else -> {
                        if (state.fish.isNotEmpty()) {
                            AssistChip(
                                onClick = { /* no-op */ },
                                label = {
                                    Text(
                                        text = "API OK: ${state.fish.size} peces cargados",
                                        color = Marron_Oscuro
                                    )
                                },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = Azul_Verde,
                                    labelColor = Marron_Oscuro
                                ),
                                border = BorderStroke(1.dp, Marron)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        } else {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    HomeMenuButton(
                        text = "Peces",
                        containerColor = Azul,
                        borderColor = Marron,
                        onClick = { navController.navigate("list/Peces") }
                    )

                    HomeMenuButton(
                        text = "Pesca submarina",
                        containerColor = Azul_Verde,
                        borderColor = Marron,
                        onClick = { navController.navigate("list/Pesca_submarina") }
                    )

                    HomeMenuButton(
                        text = "Bichos",
                        containerColor = Amarillo,
                        borderColor = Marron,
                        textColor = Marron_Oscuro,
                        onClick = { navController.navigate("list/Bichos") }
                    )

                    HomeMenuButton(
                        text = "Fósiles",
                        containerColor = Marron,
                        borderColor = Marron_Oscuro,
                        onClick = { navController.navigate("list/Fosil") }
                    )

                    HomeMenuButton(
                        text = "Obras de arte",
                        containerColor = Marron_Oscuro,
                        borderColor = Marron,
                        onClick = { navController.navigate("list/Obra_Arte") }
                    )
                }
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    // overlay opcional
                }
            }
        }
    }
}

@Composable
private fun HomeMenuButton(
    text: String,
    containerColor: Color,
    borderColor: Color,
    textColor: Color = Color.White,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(2.dp, borderColor),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = textColor
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}
