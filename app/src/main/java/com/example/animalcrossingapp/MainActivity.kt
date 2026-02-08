package com.example.animalcrossingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.animalcrossingapp.navigation.AppNavigation
import com.example.animalcrossingapp.ui.theme.AnimalCrossingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimalCrossingAppTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}
