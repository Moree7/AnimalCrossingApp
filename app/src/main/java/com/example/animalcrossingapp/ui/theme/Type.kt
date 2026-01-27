package com.example.animalcrossingapp.ui.theme

//import androidx.compose.material3.R
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.animalcrossingapp.R


val FinkHeavyFont = FontFamily(
    Font(
        resId = R.font.fink_heavy,
        weight = FontWeight.Normal
    )
)
fun colorForType(type: String): Color =
    when (type.lowercase()) {
        "peces" -> Azul
        "pesca_submarina" -> Azul_Verde
        "bicho", "bichos" -> Amarillo
        "fosil", "fósil" -> Marron
        "obra_arte", "obra de arte" -> Rosa
        else -> Beige
    }


val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = FinkHeavyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 28.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FinkHeavyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    )
)

    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
