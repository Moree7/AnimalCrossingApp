package com.example.animalcrossingapp.navigation

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.animalcrossingapp.data.i18n.OfflineTranslations
import com.example.animalcrossingapp.data.local.DatabaseModule
import com.example.animalcrossingapp.data.remote.NetworkModule
import com.example.animalcrossingapp.data.repository.CollectiblesRepository
import com.example.animalcrossingapp.ui.add.AddItemScreen
import com.example.animalcrossingapp.ui.common.LiquidGradientBackground
import com.example.animalcrossingapp.ui.detail.DetailScreen
import com.example.animalcrossingapp.ui.edit.EditItemScreen
import com.example.animalcrossingapp.ui.home.HomeScreen
import com.example.animalcrossingapp.ui.list.ListScreen
import com.example.animalcrossingapp.ui.login.LoginScreen
import com.example.animalcrossingapp.ui.theme.Azul
import com.example.animalcrossingapp.ui.theme.Azul_Verde
import com.example.animalcrossingapp.viewModel.AddItemViewModel
import com.example.animalcrossingapp.viewModel.AddItemViewModelFactory
import com.example.animalcrossingapp.viewModel.DetailViewModel
import com.example.animalcrossingapp.viewModel.DetailViewModelFactory
import com.example.animalcrossingapp.viewModel.EditItemViewModel
import com.example.animalcrossingapp.viewModel.EditItemViewModelFactory
import com.example.animalcrossingapp.viewModel.ListViewModel
import com.example.animalcrossingapp.viewModel.ListViewModelFactory

@Composable
fun AppNavigation(navController: NavHostController) {
    val context = LocalContext.current
    val appContext = remember(context) { context.applicationContext }

    val dao = remember(appContext) { DatabaseModule.db(appContext).collectibleDao() }

    // ✅ NUNCA crashea: si falta el JSON o está mal, se queda vacío
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

    Box(modifier = Modifier.fillMaxSize()) {

        // ✅ Wallpaper seguro (tu LiquidGradientBackground nuevo, abajo te lo doy)
        LiquidGradientBackground(
            modifier = Modifier.fillMaxSize(),
            colorA = Azul,
            colorB = Azul_Verde
        )

        NavHost(
            navController = navController,
            startDestination = "login"
        ) {
            composable("login") { LoginScreen(navController) }
            composable("home") { HomeScreen(navController) }

            composable("list/{type}") { backStackEntry ->
                val type = backStackEntry.arguments?.getString("type").orEmpty()
                val factory = remember(repo) { ListViewModelFactory(repo) }
                val vm: ListViewModel = viewModel(factory = factory)

                // IMPORTANTÍSIMO: ListScreen debe aceptar vm y NO crear otro vm dentro.
                ListScreen(navController = navController, type = type, vm = vm)
            }

            composable("detail/{id}") { backStackEntry ->
                val idEncoded = backStackEntry.arguments?.getString("id") ?: return@composable
                val itemId = Uri.decode(idEncoded)

                val factory = remember(repo) { DetailViewModelFactory(repo) }
                val vm: DetailViewModel = viewModel(factory = factory)

                DetailScreen(navController = navController, itemId = itemId, vm = vm)
            }

            composable("editItem/{id}") { backStackEntry ->
                val idEncoded = backStackEntry.arguments?.getString("id") ?: return@composable
                val itemId = Uri.decode(idEncoded)

                val factory = remember(repo) { EditItemViewModelFactory(repo) }
                val vm: EditItemViewModel = viewModel(factory = factory)

                EditItemScreen(navController = navController, itemId = itemId, vm = vm)
            }

            composable("addItem/{type}") { backStackEntry ->
                val typeRoute = backStackEntry.arguments?.getString("type") ?: "Peces"

                val factory = remember(repo) { AddItemViewModelFactory(repo) }
                val vm: AddItemViewModel = viewModel(factory = factory)

                AddItemScreen(navController = navController, typeRoute = typeRoute, vm = vm)
            }
        }
    }
}
