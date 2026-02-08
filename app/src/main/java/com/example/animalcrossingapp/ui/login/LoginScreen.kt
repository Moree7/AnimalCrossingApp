package com.example.animalcrossingapp.ui.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.animalcrossingapp.R
import com.example.animalcrossingapp.navigation.Screen
import com.example.animalcrossingapp.ui.theme.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    val scroll = rememberScrollState()
    // Transparente para ver el liquid background del AppNavigation
    Scaffold(containerColor = Color.Transparent) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {

            Card(
                modifier = Modifier.fillMaxWidth()
                ,
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Beige.copy(alpha = 0.92f)
                ),
                border = BorderStroke(2.dp, Marron),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {

                // ==== AQUÍ TU BLOQUE EXACTO ====
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scroll)
                        .padding(horizontal = 22.dp, vertical = 26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo
                    Image(
                        painter = painterResource(id = R.drawable.museo_logo),
                        contentDescription = "Museo",
                        //contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .size(240.dp)
                            /*.clip(CircleShape)
                            .border(
                                width = 4.dp,
                                color = Marron,
                                shape = CircleShape
                            )*/
                    )

                    //Spacer(modifier = Modifier.height(1.dp))

                    // Título principal
                    Text(
                        text = "Capturapedia",
                        color = Marron_Oscuro,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Subtítulo
                    Text(
                        text = "Animal Crossing",
                        color = Marron,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontFamily = FinkHeavyFont
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Texto descriptivo
                    Text(
                        text = "Tu guía completa para conocer \ntodo de Animal Crossing.",
                        color = Marron_Oscuro,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    // Botón entrar
                    Button(
                        onClick = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Marron_Oscuro,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 2.dp
                        )
                    ) {
                        Text(
                            text = "Entrar",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.3.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    AssistChip(
                        onClick = { /* no-op */ },
                        label = { Text("Modo offline disponible", color = Marron_Oscuro) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Rosa,
                            labelColor = Marron_Oscuro
                        ),
                        border = BorderStroke(1.dp, Marron)
                    )
                }
            }
        }
    }
}
