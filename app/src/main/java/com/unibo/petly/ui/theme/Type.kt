package com.unibo.petly.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.unibo.petly.R

// Set of Material typography styles to start with
val BrightFontFamily = FontFamily(
    Font(R.font.bright_demo, FontWeight.Normal) // Associa il file del font
)

val Typography = Typography(
    displayMedium = TextStyle(
        fontFamily = BrightFontFamily, // Usa il font Bright DEMO
        fontWeight = FontWeight.Bold,
        fontSize = 50.sp, // Regola la dimensione del testo a tuo piacimento
        lineHeight = 34.sp,
        letterSpacing = 0.sp,
        color = Brown
    ),
    bodyLarge = TextStyle(
        fontFamily = BrightFontFamily, // Usa Bright DEMO
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = BrightFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = BrightFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    // Puoi aggiungere altri stili, se necessario
)