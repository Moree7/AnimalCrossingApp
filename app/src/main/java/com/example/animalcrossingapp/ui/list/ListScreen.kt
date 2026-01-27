package com.example.animalcrossingapp.ui.list

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.animalcrossingapp.domain.CollectibleType
import com.example.animalcrossingapp.ui.common.observeIsOnline
import com.example.animalcrossingapp.ui.theme.*
import com.example.animalcrossingapp.viewModel.ListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    navController: NavController,
    type: String,
    vm: ListViewModel
) {
    val context = LocalContext.current
    val collectibleType = remember(type) { CollectibleType.fromRoute(type) }

    val state by vm.uiState.collectAsState()
    val isOnline by observeIsOnline(context).collectAsState(initial = true)

    val appBarColor = remember(collectibleType) { colorForType(collectibleType) }
    val topBarTextColor = Color.White

    val donated = state.items.count { it.isDonated }
    val total = state.items.size
    val percent = if (total == 0) 0 else ((donated * 100) / total)

    LaunchedEffect(collectibleType) {
        vm.load(collectibleType)
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("${collectibleType.routeValue} ($donated/$total)", color = topBarTextColor)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = topBarTextColor)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate("addItem/${collectibleType.routeValue}") },
                        enabled = !state.isLoading
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Añadir", tint = topBarTextColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = appBarColor)
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            if (!isOnline) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Rosa.copy(alpha = 0.92f)),
                    border = BorderStroke(1.dp, Marron)
                ) {
                    Text(
                        "Sin conexión. El refresco por API está deshabilitado.",
                        modifier = Modifier.padding(12.dp),
                        color = Marron_Oscuro
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { vm.refresh(collectibleType) },
                    enabled = isOnline && !state.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Marron_Oscuro,
                        disabledContainerColor = Marron_Oscuro.copy(alpha = 0.35f),
                        contentColor = Color.White
                    )
                ) { Text("Refrescar") }

                AssistChip(
                    onClick = { },
                    label = { Text("Progreso: $donated/$total ($percent%)", color = Marron_Oscuro) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Beige.copy(alpha = 0.92f),
                        labelColor = Marron_Oscuro
                    ),
                    border = BorderStroke(1.dp, Marron)
                )

                if (state.isLoading) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = Marron_Oscuro)
                        Text("Cargando…", color = Marron_Oscuro)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            if (state.error != null) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Rosa.copy(alpha = 0.92f)),
                    border = BorderStroke(2.dp, Marron)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text("Error cargando ${collectibleType.routeValue}", color = Marron_Oscuro, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(6.dp))
                        Text(state.error ?: "Error desconocido", color = Marron_Oscuro, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = state.items, key = { it.id }) { item ->
                    CollectibleItem(
                        item = item,
                        onCheckedChange = { checked -> vm.setDonated(item.id, checked) },
                        onDetailClick = { navController.navigate("detail/${Uri.encode(item.id)}") },
                        onEditClick = { navController.navigate("editItem/${Uri.encode(item.id)}") }
                    )
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
