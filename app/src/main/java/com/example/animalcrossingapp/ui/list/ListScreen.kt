package com.example.animalcrossingapp.ui.list

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.animalcrossingapp.domain.CollectibleType
import com.example.animalcrossingapp.ui.common.observeIsOnline
import com.example.animalcrossingapp.ui.model.CollectibleUi
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

    val appBarColor = remember(collectibleType) { colorForType(collectibleType) }
    val topBarTextColor = Color.White

    var showOnlyDonated by rememberSaveable(type) { mutableStateOf(false) }
    var searchQuery by rememberSaveable(type) { mutableStateOf("") }

    val listState = rememberSaveable(type, saver = LazyListState.Saver) {
        LazyListState()
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val donated = state.items.count { it.isDonated }
    val total = state.items.size
    val percent = if (total == 0) 0 else ((donated * 100) / total)

    val filteredAndSortedItems = remember(state.items, showOnlyDonated, searchQuery) {
        val query = searchQuery.trim().lowercase()

        val baseItems = if (showOnlyDonated) {
            state.items.filter { it.isDonated }
        } else {
            state.items
        }

        val searchedItems = if (query.isBlank()) {
            baseItems
        } else {
            baseItems.filter { item ->
                item.name.lowercase().contains(query) ||
                        item.subtitle.lowercase().contains(query) ||
                        item.description.lowercase().contains(query)
            }
        }

        searchedItems.sortedWith(
            compareBy<CollectibleUi> { it.isDonated }
                .thenBy { it.displayOrder }
                .thenBy { it.name.lowercase() }
        )
    }

    val localItems = remember(filteredAndSortedItems) {
        filteredAndSortedItems.toMutableStateList()
    }

    val refreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = {
            if (isOnline && !state.isLoading) {
                vm.refresh(collectibleType)
            }
        }
    )

    LaunchedEffect(collectibleType) {
        vm.load(collectibleType)
    }

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pullRefresh(refreshState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                if (!isOnline) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Rosa.copy(alpha = 0.92f)
                        ),
                        border = BorderStroke(1.dp, Marron)
                    ) {
                        Text(
                            text = "Sin conexión. El refresco por API está deshabilitado.",
                            modifier = Modifier.padding(12.dp),
                            color = Marron_Oscuro
                        )
                    }
                }

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Buscar ítem") },
                    placeholder = { Text("Nombre, subtítulo o descripción") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Marron,
                        unfocusedBorderColor = Marron.copy(alpha = 0.6f),
                        focusedLabelColor = Marron_Oscuro,
                        unfocusedLabelColor = Marron_Oscuro,
                        cursorColor = Marron_Oscuro
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                ProgressCard(
                    donated = donated,
                    total = total
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AssistChip(
                        onClick = { showOnlyDonated = !showOnlyDonated },
                        label = {
                            Text(
                                text = if (showOnlyDonated) {
                                    "Donados ($donated)"
                                } else {
                                    "Progreso: $donated/$total ($percent%)"
                                },
                                color = Marron_Oscuro
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (showOnlyDonated) {
                                Verde.copy(alpha = 0.25f)
                            } else {
                                Beige.copy(alpha = 0.92f)
                            },
                            labelColor = Marron_Oscuro
                        ),
                        border = BorderStroke(1.dp, Marron)
                    )

                    if (state.isLoading) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = Marron_Oscuro
                            )
                            Text("Cargando…", color = Marron_Oscuro)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (state.error != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Rosa.copy(alpha = 0.92f)
                        ),
                        border = BorderStroke(2.dp, Marron)
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(
                                text = "Error cargando ${collectibleType.routeValue}",
                                color = Marron_Oscuro,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = state.error ?: "Error desconocido",
                                color = Marron_Oscuro,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { vm.retryRefresh() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Marron_Oscuro,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }

                if (!state.isLoading && filteredAndSortedItems.isEmpty() && state.error == null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Beige.copy(alpha = 0.92f)
                        ),
                        border = BorderStroke(1.dp, Marron)
                    ) {
                        Text(
                            text = if (showOnlyDonated && searchQuery.isBlank()) {
                                "Todavía no hay ítems donados."
                            } else if (searchQuery.isNotBlank()) {
                                "No hay resultados para \"$searchQuery\"."
                            } else {
                                "No hay ítems todavía. Arrastra hacia abajo para refrescar o espera a la carga automática."
                            },
                            modifier = Modifier.padding(12.dp),
                            color = Marron_Oscuro
                        )
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(
                            items = localItems,
                            key = { _, item -> item.id }
                        ) { index, item ->
                            Column(
                                modifier = Modifier.animateContentSize()
                            ) {
                                CollectibleItem(
                                    item = item,
                                    modifier = Modifier,
                                    onCheckedChange = { checked ->
                                        vm.setDonated(item.id, checked)

                                        if (checked) {
                                            scope.launch {
                                                val result = snackbarHostState.showSnackbar(
                                                    message = "Marcado como donado",
                                                    actionLabel = "Deshacer",
                                                    duration = SnackbarDuration.Short
                                                )

                                                if (result == SnackbarResult.ActionPerformed) {
                                                    vm.setDonated(item.id, false)
                                                }
                                            }
                                        }
                                    },
                                    onDetailClick = {
                                        navController.navigate("detail/${Uri.encode(item.id)}")
                                    },
                                    onEditClick = {
                                        navController.navigate("editItem/${Uri.encode(item.id)}")
                                    },
                                    onDeleteSwipe = {
                                        vm.deleteItem(item.id) { success ->
                                            if (success) {
                                                scope.launch {
                                                    val result = snackbarHostState.showSnackbar(
                                                        message = "Ítem eliminado",
                                                        actionLabel = "Deshacer",
                                                        duration = SnackbarDuration.Long
                                                    )

                                                    if (result == SnackbarResult.ActionPerformed) {
                                                        vm.restoreLastDeletedItem()
                                                    }
                                                }
                                            }
                                        }
                                    }
                                )

                                if (!item.isDonated) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        TextButton(
                                            enabled = index > 0,
                                            onClick = {
                                                val mutable = localItems.toMutableList()
                                                val moved = mutable.removeAt(index)
                                                mutable.add(index - 1, moved)
                                                vm.reorderItems(mutable)
                                            }
                                        ) {
                                            Text("↑")
                                        }

                                        TextButton(
                                            enabled = index < localItems.lastIndex,
                                            onClick = {
                                                val mutable = localItems.toMutableList()
                                                val moved = mutable.removeAt(index)
                                                mutable.add(index + 1, moved)
                                                vm.reorderItems(mutable)
                                            }
                                        ) {
                                            Text("↓")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = state.isLoading,
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                contentColor = Marron_Oscuro
            )
        }
    }
}

@Composable
private fun ProgressCard(
    donated: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    val progress = if (total == 0) 0f else donated.toFloat() / total.toFloat()
    val percent = (progress * 100).toInt()

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = "progress_animation"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Beige.copy(alpha = 0.96f)
        ),
        border = BorderStroke(2.dp, Marron),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Progreso del museo",
                style = MaterialTheme.typography.titleMedium,
                color = Marron_Oscuro
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "$donated / $total completados",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Marron_Oscuro
                )

                Text(
                    text = "$percent%",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Verde
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp),
                color = Verde,
                trackColor = Marron.copy(alpha = 0.18f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = when {
                    percent == 100 -> "¡Colección completada!"
                    percent >= 75 -> "Ya casi lo tienes"
                    percent >= 50 -> "Buen progreso"
                    percent > 0 -> "Sigue así"
                    else -> "Empieza tu colección"
                },
                style = MaterialTheme.typography.bodySmall,
                color = Marron_Oscuro
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