package com.example.animalcrossingapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animalcrossingapp.data.repository.CollectiblesRepository
import com.example.animalcrossingapp.domain.CollectibleType
import com.example.animalcrossingapp.ui.model.CollectibleUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeDashboardViewModel(
    private val repo: CollectiblesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeDashboardUiState(isLoading = true))
    val uiState: StateFlow<HomeDashboardUiState> = _uiState.asStateFlow()

    fun load() {
        viewModelScope.launch {
            val fishFlow = repo.observeByType(CollectibleType.PECES.routeValue)
            val seaFlow = repo.observeByType(CollectibleType.PESCA_SUBMARINA.routeValue)
            val bugsFlow = repo.observeByType(CollectibleType.BICHOS.routeValue)
            val fossilFlow = repo.observeByType(CollectibleType.FOSIL.routeValue)
            val artFlow = repo.observeByType(CollectibleType.OBRA_ARTE.routeValue)

            combine(
                fishFlow,
                seaFlow,
                bugsFlow,
                fossilFlow,
                artFlow
            ) { fish, sea, bugs, fossils, art ->

                val allItems: List<CollectibleUi> = fish + sea + bugs + fossils + art

                val total = allItems.size
                val donated = allItems.count { it.isDonated }
                val percent = if (total == 0) 0 else (donated * 100 / total)

                fun summary(items: List<CollectibleUi>): ProgressSummaryUi {
                    val itemTotal = items.size
                    val itemDonated = items.count { it.isDonated }
                    val itemPercent = if (itemTotal == 0) 0 else (itemDonated * 100 / itemTotal)
                    return ProgressSummaryUi(
                        donated = itemDonated,
                        total = itemTotal,
                        percent = itemPercent
                    )
                }

                val overall = summary(allItems)

                val categories = listOf(
                    CategoryProgressUi(
                        type = CollectibleType.PECES,
                        title = "Peces",
                        progress = summary(fish)
                    ),
                    CategoryProgressUi(
                        type = CollectibleType.PESCA_SUBMARINA,
                        title = "Pesca submarina",
                        progress = summary(sea)
                    ),
                    CategoryProgressUi(
                        type = CollectibleType.BICHOS,
                        title = "Bichos",
                        progress = summary(bugs)
                    ),
                    CategoryProgressUi(
                        type = CollectibleType.FOSIL,
                        title = "Fósiles",
                        progress = summary(fossils)
                    ),
                    CategoryProgressUi(
                        type = CollectibleType.OBRA_ARTE,
                        title = "Obras de arte",
                        progress = summary(art)
                    )
                )

                val achievements = listOf(
                    AchievementUi(
                        title = "Primer donado",
                        description = "Marca 1 ítem como donado",
                        unlocked = donated >= 1
                    ),
                    AchievementUi(
                        title = "Coleccionista novato",
                        description = "Marca 10 ítems como donados",
                        unlocked = donated >= 10
                    ),
                    AchievementUi(
                        title = "Media colección",
                        description = "Alcanza el 50% de progreso",
                        unlocked = percent >= 50
                    ),
                    AchievementUi(
                        title = "Casi completo",
                        description = "Alcanza el 75% de progreso",
                        unlocked = percent >= 75
                    ),
                    AchievementUi(
                        title = "Museo completo",
                        description = "Completa el 100% de la colección",
                        unlocked = percent >= 100
                    )
                )

                HomeDashboardUiState(
                    total = total,
                    donated = donated,
                    percentage = percent,
                    isLoading = false,
                    achievements = achievements,
                    overall = overall,
                    categories = categories
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}