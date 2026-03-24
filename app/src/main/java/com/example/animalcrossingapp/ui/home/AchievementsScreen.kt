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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.animalcrossingapp.data.i18n.OfflineTranslations
import com.example.animalcrossingapp.data.local.DatabaseModule
import com.example.animalcrossingapp.data.remote.NetworkModule
import com.example.animalcrossingapp.data.repository.CollectiblesRepository
import com.example.animalcrossingapp.ui.theme.Marron
import com.example.animalcrossingapp.ui.theme.Marron_Oscuro
import com.example.animalcrossingapp.ui.theme.Rosa
import com.example.animalcrossingapp.ui.theme.Verde_Oscuro
import com.example.animalcrossingapp.viewModel.AchievementUi
import com.example.animalcrossingapp.viewModel.HomeDashboardViewModel
import com.example.animalcrossingapp.viewModel.HomeDashboardViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(navController: NavController) {
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

    androidx.compose.runtime.LaunchedEffect(Unit) {
        vm.load()
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Logros", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Marron_Oscuro)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            state.achievements.forEach { achievement: AchievementUi ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (achievement.unlocked) {
                            Rosa.copy(alpha = 0.25f)
                        } else {
                            Color.White.copy(alpha = 0.85f)
                        }
                    ),
                    border = BorderStroke(1.dp, Marron)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = if (achievement.unlocked) {
                                Icons.Default.Star
                            } else {
                                Icons.Default.Lock
                            },
                            contentDescription = null,
                            tint = if (achievement.unlocked) Verde_Oscuro else Marron_Oscuro
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = achievement.title,
                                color = Marron_Oscuro,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = achievement.description,
                                color = Marron_Oscuro,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Text(
                            text = if (achievement.unlocked) "OK" else "Pendiente",
                            color = if (achievement.unlocked) Verde_Oscuro else Marron_Oscuro,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}