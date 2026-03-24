package com.example.animalcrossingapp.ui.categories

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.animalcrossingapp.domain.CollectibleType
import com.example.animalcrossingapp.ui.theme.*
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(navController: NavController) {
    val categories = CollectibleType.entries

    Scaffold(
        containerColor = Beige,
        topBar = {
            TopAppBar(
                title = { Text("Categorías", color = Marron_Oscuro) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Beige)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { type ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("list/${type.routeValue}") },
                    colors = CardDefaults.cardColors(containerColor = colorForType(type)),
                    border = BorderStroke(2.dp, Marron)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = titleForType(type),
                            style = MaterialTheme.typography.titleLarge,
                            color = Marron_Oscuro
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Ver lista",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Marron_Oscuro
                        )
                    }
                }
            }
        }
    }
}

private fun titleForType(type: CollectibleType): String = when (type) {
    CollectibleType.PECES -> "Peces"
    CollectibleType.PESCA_SUBMARINA -> "Pesca submarina"
    CollectibleType.BICHOS -> "Bichos"
    CollectibleType.FOSIL -> "Fósiles"
    CollectibleType.OBRA_ARTE -> "Obras de arte"
}

private fun colorForType(type: CollectibleType) = when (type) {
    CollectibleType.PECES -> Azul
    CollectibleType.PESCA_SUBMARINA -> Azul_Verde
    CollectibleType.BICHOS -> Amarillo
    CollectibleType.FOSIL -> Marron
    CollectibleType.OBRA_ARTE -> Rosa
}
