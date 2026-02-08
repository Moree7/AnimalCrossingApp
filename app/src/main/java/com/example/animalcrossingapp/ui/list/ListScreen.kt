package com.example.animalcrossingapp.ui.list

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
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

    // Color topbar por categoría
    val appBarColor = remember(collectibleType) { colorForType(collectibleType) }
    val topBarTextColor = Color.White

    // Estado scroll
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Filtro texto (sin rememberSaveable)
    var query by remember { mutableStateOf("") }

    // Sección donados plegable
    var donatedExpanded by remember { mutableStateOf(false) }

    // 1) Observa Room al entrar/cambiar tipo
    LaunchedEffect(collectibleType) {
        vm.load(collectibleType)
    }

    // 2) Pull-to-refresh (gesto: arrastras hacia abajo estando arriba)
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = {
            if (isOnline && !state.isRefreshing) {
                vm.refresh(collectibleType)
            }
        }
    )

    // ---- Filtrado ----
    val filtered = remember(state.items, query) {
        val q = query.trim().lowercase()
        if (q.isBlank()) state.items
        else state.items.filter {
            it.name.lowercase().contains(q) || it.subtitle.lowercase().contains(q)
        }
    }

    // ---- Separar no donados / donados ----
    val notDonated = remember(filtered) { filtered.filter { !it.isDonated } }
    val donated = remember(filtered) { filtered.filter { it.isDonated } }

    // Progreso (sobre TODA la lista real, no el filtrado)
    val donatedCount = state.items.count { it.isDonated }
    val totalCount = state.items.size
    val percent = if (totalCount == 0) 0 else ((donatedCount * 100) / totalCount)

    // Índice aproximado del header "Donados" dentro de LazyColumn:
    // - Si hay noDonated: ocupa notDonated.size items
    // - Si no hay: insertamos 1 item "empty_not_donated"
    val donatedHeaderIndex = remember(notDonated.size) {
        if (notDonated.isNotEmpty()) notDonated.size else 1
    }

    Scaffold(
        containerColor = Color.Transparent, // para ver tu wallpaper global
        topBar = {
            TopAppBar(
                title = {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "${collectibleType.routeValue} ($donatedCount/$totalCount)",
                            color = topBarTextColor
                        )
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
                        enabled = !state.isLoading && !state.isRefreshing
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Añadir", tint = topBarTextColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = appBarColor)
            )
        }
    ) { innerPadding ->

        // pullRefresh va en el contenedor que envuelve la lista
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pullRefresh(pullRefreshState) // ✅ refresco al “tirar hacia arriba estando arriba”
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                // Aviso offline
                if (!isOnline) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Rosa.copy(alpha = 0.92f)),
                        border = BorderStroke(1.dp, Marron)
                    ) {
                        Text(
                            text = "Sin conexión. El refresco por API está deshabilitado.",
                            modifier = Modifier.padding(12.dp),
                            color = Marron_Oscuro
                        )
                    }
                }

                // Card superior: buscar + progreso + estado
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Beige.copy(alpha = 0.92f)),
                    border = BorderStroke(2.dp, Marron)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = query,
                            onValueChange = { query = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            label = { Text("Buscar…", color = Marron_Oscuro) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Marron,
                                unfocusedBorderColor = Marron,
                                focusedLabelColor = Marron_Oscuro,
                                unfocusedLabelColor = Marron_Oscuro,
                                cursorColor = Marron_Oscuro,
                                focusedTextColor = Marron_Oscuro,
                                unfocusedTextColor = Marron_Oscuro,
                                focusedContainerColor = Beige,
                                unfocusedContainerColor = Beige
                            )
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // ✅ Al pulsar el chip: scroll a Donados + expandir
                            AssistChip(
                                onClick = {
                                    donatedExpanded = true
                                    scope.launch {
                                        // baja hasta el header de Donados
                                        listState.animateScrollToItem(donatedHeaderIndex)
                                    }
                                },
                                label = { Text("Progreso: $donatedCount/$totalCount ($percent%)", color = Marron_Oscuro) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = Rosa.copy(alpha = 0.45f),
                                    labelColor = Marron_Oscuro
                                ),
                                border = BorderStroke(1.dp, Marron)
                            )

                            if (state.isRefreshing || state.isLoading) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = Marron_Oscuro
                                    )
                                    Text(
                                        text = if (state.isRefreshing) "Actualizando…" else "Cargando…",
                                        color = Marron_Oscuro
                                    )
                                }
                            }
                        }

                        Text(
                            text = "Tip: para refrescar la API, desliza hacia abajo estando arriba de la lista.",
                            color = Marron_Oscuro,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

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

                // Lista con sección Donados plegable
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // NO DONADOS
                    if (notDonated.isNotEmpty()) {
                        items(items = notDonated, key = { it.id }) { item ->
                            CollectibleItem(
                                item = item,
                                onCheckedChange = { checked -> vm.setDonated(item.id, checked) },
                                onDetailClick = { navController.navigate("detail/${Uri.encode(item.id)}") },
                                onEditClick = { navController.navigate("editItem/${Uri.encode(item.id)}") }
                            )
                        }
                    } else {
                        item(key = "empty_not_donated") {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Beige.copy(alpha = 0.92f)),
                                border = BorderStroke(2.dp, Marron)
                            ) {
                                Text(
                                    text = "No hay items pendientes en esta categoría.",
                                    modifier = Modifier.padding(12.dp),
                                    color = Marron_Oscuro
                                )
                            }
                        }
                    }

                    // HEADER DONADOS (plegable)
                    item(key = "donated_header") {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize()
                        ) {
                            Spacer(Modifier.height(8.dp))

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { donatedExpanded = !donatedExpanded },
                                colors = CardDefaults.cardColors(containerColor = Beige.copy(alpha = 0.92f)),
                                border = BorderStroke(2.dp, Marron)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Donados (${donated.size})",
                                        color = Marron_Oscuro,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Icon(
                                        imageVector = if (donatedExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = if (donatedExpanded) "Plegar" else "Desplegar",
                                        tint = Marron_Oscuro
                                    )
                                }
                            }

                            AnimatedVisibility(visible = donatedExpanded) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    if (donated.isEmpty()) {
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = CardDefaults.cardColors(containerColor = Beige.copy(alpha = 0.92f)),
                                            border = BorderStroke(2.dp, Marron)
                                        ) {
                                            Text(
                                                text = "Aún no has donado ninguno.",
                                                modifier = Modifier.padding(12.dp),
                                                color = Marron_Oscuro
                                            )
                                        }
                                    } else {
                                        donated.forEach { item ->
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
                    }

                    item(key = "bottom_spacer") { Spacer(Modifier.height(24.dp)) }
                }
            }

            // Indicador del pull-to-refresh (arriba, centrado)
            PullRefreshIndicator(
                refreshing = state.isRefreshing,
                state = pullRefreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp),
                backgroundColor = Beige,
                contentColor = Marron_Oscuro
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
