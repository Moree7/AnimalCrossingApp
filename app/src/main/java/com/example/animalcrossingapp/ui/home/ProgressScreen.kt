package com.example.animalcrossingapp.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.animalcrossingapp.data.i18n.OfflineTranslations
import com.example.animalcrossingapp.data.local.DatabaseModule
import com.example.animalcrossingapp.data.remote.NetworkModule
import com.example.animalcrossingapp.data.repository.CollectiblesRepository
import com.example.animalcrossingapp.domain.CollectibleType
import com.example.animalcrossingapp.ui.theme.Amarillo
import com.example.animalcrossingapp.ui.theme.Azul
import com.example.animalcrossingapp.ui.theme.Azul_Verde
import com.example.animalcrossingapp.ui.theme.Beige
import com.example.animalcrossingapp.ui.theme.Marron
import com.example.animalcrossingapp.ui.theme.Marron_Oscuro
import com.example.animalcrossingapp.ui.theme.Rosa
import com.example.animalcrossingapp.ui.theme.Verde_Oscuro
import com.example.animalcrossingapp.viewModel.CategoryProgressUi
import com.example.animalcrossingapp.viewModel.HomeDashboardViewModel
import com.example.animalcrossingapp.viewModel.HomeDashboardViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(navController: NavController) {
    val context = LocalContext.current
    val appContext = remember(context) { context.applicationContext }
    val dao = remember(appContext) { DatabaseModule.db(appContext).collectibleDao() }

    val offline = remember(appContext) {
        runCatching { OfflineTranslations.load(appContext, "es_collectibles.json") }
            .getOrElse { OfflineTranslations.empty() }
    }

    val repo = remember(appContext) {
        CollectiblesRepository(
            api = NetworkModule.api,
            dao = dao,
            offline = offline
        )
    }

    val factory = remember(repo) { HomeDashboardViewModelFactory(repo) }
    val vm: HomeDashboardViewModel = viewModel(factory = factory)
    val state = vm.uiState.collectAsState().value

    LaunchedEffect(Unit) {
        vm.load()
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Progreso del museo", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Verde_Oscuro)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Beige.copy(alpha = 0.94f)),
                border = BorderStroke(2.dp, Marron)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Total: ${state.overall.donated}/${state.overall.total} (${state.overall.percent}%)",
                        color = Marron_Oscuro,
                        style = MaterialTheme.typography.titleMedium
                    )
                    LinearProgressIndicator(
                        progress = { if (state.overall.total == 0) 0f else state.overall.donated.toFloat() / state.overall.total.toFloat() },
                        modifier = Modifier.fillMaxWidth(),
                        color = Verde_Oscuro,
                        trackColor = Marron.copy(alpha = 0.2f)
                    )
                }
            }

            state.categories.forEach { category: CategoryProgressUi ->
                ProgressCategoryCard(category)
            }
        }
    }
}

@Composable
private fun ProgressCategoryCard(category: CategoryProgressUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Beige.copy(alpha = 0.92f)),
        border = BorderStroke(1.dp, Marron)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category.title,
                    color = Marron_Oscuro,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "${category.progress.donated}/${category.progress.total} (${category.progress.percent}%)",
                    color = Marron_Oscuro,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            LinearProgressIndicator(
                progress = { category.progress.percent / 100f },
                modifier = Modifier.fillMaxWidth(),
                color = colorForType(category.type),
                trackColor = Marron.copy(alpha = 0.2f)
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