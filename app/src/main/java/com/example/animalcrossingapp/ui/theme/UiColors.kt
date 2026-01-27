package com.example.animalcrossingapp.ui.theme

import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable

@Composable
fun appOutlinedTextFieldColors(): TextFieldColors =
    OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Marron,
        unfocusedBorderColor = Marron,
        focusedLabelColor = Marron_Oscuro,
        unfocusedLabelColor = Marron_Oscuro,
        cursorColor = Marron_Oscuro,
        focusedContainerColor = Beige,
        unfocusedContainerColor = Beige,
        focusedTextColor = Marron_Oscuro,
        unfocusedTextColor = Marron_Oscuro
    )
