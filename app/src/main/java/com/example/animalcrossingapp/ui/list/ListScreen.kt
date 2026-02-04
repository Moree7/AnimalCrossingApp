package com.example.animalcrossingapp.ui.list

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
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
import com.example.animalcrossingapp.viewModel.ListUiState

// ✅ Pull-to-refresh (Material)
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.ui.zIndex

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ListScreen(
    navController: NavController,
    type: String,
    vm: ListViewModel
) {
    val context = LocalContext.current

    // ----------------------------
    // 1) Tipo de colección (Peces/Bichos/...)
    // ----------------------------
    val collectibleType = remember(type) { CollectibleType.fromRoute(type) }

    // ----------------------------
    // 2) Estado de UI y conectividad
    // ----------------------------
    val state: ListUiState by vm.uiState.collectAsState()

    val isOnline by observeIsOnline(context).collectAsState(initial = true)

    // ----------------------------
    // 3) Colores de la TopBar por CATEGORÍA
    // ----------------------------
    val appBarColor = remember(collectibleType) { colorForType(collectibleType) }
    val topBarTextColor = Color.White

    // ----------------------------
    // 4) Progreso calculado desde items (donated/total)
    // ----------------------------
    val donated = remember(state.items) { state.items.count { it.isDonated } }
    val total = remember(state.items) { state.items.size }
    val percent = remember(donated, total) { if (total == 0) 0 else (donated * 100) / total }

    // ----------------------------
    // 5) Cargar/observar datos (Room)
    //    Esto NO refresca API, solo “observa” lo local.
    // ----------------------------
    LaunchedEffect(collectibleType) {
        vm.load(collectibleType)
    }

    // ----------------------------
    // 6) Auto-refresco si la tabla está vacía
    //    - Solo 1 vez por categoría (didAutoRefresh)
    //    - Solo si hay internet
    //    - Solo si no está cargando ya
    // ----------------------------
    var didAutoRefresh by remember(collectibleType) { mutableStateOf(false) }

    LaunchedEffect(
        collectibleType,
        isOnline,
        state.isLoading,
        state.items.size,
        didAutoRefresh
    ) {
        val isEmpty = state.items.isEmpty()
        val canRefresh = isOnline && !state.isLoading

        if (!didAutoRefresh && isEmpty && canRefresh) {
            didAutoRefresh = true
            vm.refresh(collectibleType)
        }
    }

    // ----------------------------
    // 7) Pull-to-refresh (arrastrar hacia abajo)
    // ----------------------------
    val refreshing = state.isRefreshing

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            // Solo refrescamos si hay internet y no está cargando
            if (isOnline && !state.isLoading) {
                vm.refresh(collectibleType)
            }
        }
    )

    // ----------------------------
    // 8) UI principal
    // ----------------------------
    Scaffold(
        containerColor = Color.Transparent, // para ver tu fondo global (LiquidGradientBackground)
        topBar = {
            TopAppBar(
                title = {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "${collectibleType.routeValue} ($donated/$total)",
                            color = topBarTextColor
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = topBarTextColor
                        )
                    }
                },
                actions = {
                    // Botón añadir item manual
                    IconButton(
                        onClick = { navController.navigate("addItem/${collectibleType.routeValue}") },
                        enabled = !state.isLoading
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Añadir",
                            tint = topBarTextColor
                        )
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

            // Banner sin conexión
            if (!isOnline) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Rosa.copy(alpha = 0.92f)),
                    border = BorderStroke(1.dp, Marron)
                ) {
                    Text(
                        text = "Sin conexión. Arrastrar para refrescar está deshabilitado.",
                        modifier = Modifier.padding(12.dp),
                        color = Marron_Oscuro
                    )
                }
            }

            // Chip progreso
            AssistChip(
                onClick = { },
                label = { Text("Progreso: $donated/$total ($percent%)", color = Marron_Oscuro) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = Beige.copy(alpha = 0.92f),
                    labelColor = Marron_Oscuro
                ),
                border = BorderStroke(1.dp, Marron)
            )

            Spacer(Modifier.height(12.dp))

            // Error
            if (state.error != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Rosa.copy(alpha = 0.92f)),
                    border = BorderStroke(2.dp, Marron)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(
                            text = "Error cargando ${collectibleType.routeValue}",
                            color = Marron_Oscuro,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = state.error ?: "Error desconocido",
                            color = Marron_Oscuro,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // ----------------------------
            // 9) Contenedor Pull-to-refresh + Lista
            // ----------------------------
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {

                // Lista
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

                // Indicador “pull to refresh”
                PullRefreshIndicator(
                    refreshing = refreshing,
                    state = pullRefreshState,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .zIndex(10f),
                    contentColor = Marron_Oscuro,
                    backgroundColor = Beige.copy(alpha = 0.92f)
                )
            }
        }
    }
}

// Colores por tipo (categoría)
private fun colorForType(type: CollectibleType): Color = when (type) {
    CollectibleType.PECES -> Azul
    CollectibleType.PESCA_SUBMARINA -> Azul_Verde
    CollectibleType.BICHOS -> Amarillo
    CollectibleType.FOSIL -> Marron
    CollectibleType.OBRA_ARTE -> Rosa
}
