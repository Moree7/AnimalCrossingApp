package com.example.animalcrossingapp.ui.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.animalcrossingapp.ui.theme.Beige
import com.example.animalcrossingapp.ui.theme.Marron
import com.example.animalcrossingapp.ui.theme.Marron_Oscuro
import com.example.animalcrossingapp.viewModel.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    itemId: String,
    vm: DetailViewModel
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(itemId) {
        vm.load(itemId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.item?.name ?: "Detalles",
                        color = Color.White
                    )
                },
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

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Marron_Oscuro)
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Beige),
                        border = BorderStroke(1.dp, Marron)
                    ) {
                        Text(
                            text = state.error ?: "Error desconocido",
                            modifier = Modifier.padding(16.dp),
                            color = Marron_Oscuro
                        )
                    }
                }
            }

            state.item != null -> {
                val item = state.item!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    item.imageResId?.let { imageRes ->
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = item.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Beige.copy(alpha = 0.95f)
                        ),
                        border = BorderStroke(2.dp, Marron)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.headlineSmall,
                                color = Marron_Oscuro
                            )

                            if (item.subtitle.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = item.subtitle,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Marron_Oscuro
                                )
                            }

                            if (item.description.isNotBlank()) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = item.description,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Marron_Oscuro
                                )
                            }
                        }
                    }
                }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No se encontró el ítem", color = Marron_Oscuro)
                }
            }
        }
    }
}