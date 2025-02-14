package com.sam.pokemondemo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sam.pokemondemo.R

val Typography = Typography()

val Typography.title: TextStyle
    @Composable
    get() {
        return TextStyle(
            color = colorResource(id = R.color.text_color),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 28.sp,
        )
    }

val Typography.headline1: TextStyle
    @Composable
    get() {
        return TextStyle(
            color = colorResource(id = R.color.text_color),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            lineHeight = 26.sp,
        )
    }

val Typography.headline3: TextStyle
    @Composable
    get() {
        return TextStyle(
            color = colorResource(id = R.color.text_color),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 22.sp,
        )
    }

val Typography.body: TextStyle
    @Composable
    get() {
        return TextStyle(
            color = colorResource(id = R.color.text_color),
            fontWeight = FontWeight.Companion.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        )
    }
