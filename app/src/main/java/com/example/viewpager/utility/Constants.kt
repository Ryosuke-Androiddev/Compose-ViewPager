package com.example.viewpager.utility

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object Constants {

    const val TOP_SCREEN = "top"

    const val SECOND_SCREEN = "second"

    const val THIRD_SCREEN = "third"
    const val THIRD_SCREEN_ROUTE = "third/{name}"

    const val NAVIGATION_KEY = "name"

    val primaryTextColor = Color(0xFF444444)
    val primaryColor = Color(0xffadf4ff)
    val normalSpace = 16.dp
    val smallSpace = 8.dp
    val noSpace = 0.dp
    val normalTouchableHeight = 44.dp

    val noRadius = 0.dp
    val normalRadius = 16.dp
    val rowHeight = 84.dp
    val deleteIconButtonWidth = 60.dp
    val deleteTextButtonWidth = 80.dp
    val dividerNormalThickness = 1.0.dp

    val cornerRadiusBig = 20.dp
    val normalElevation = 1.dp
    val backSideColor = Color(0xFFfef49c)
    val secondaryColor = Color(0xffb2ffa1)
    val tertiaryColor = Color(0xFFb6caff)

    val cardWidth = 350.dp
    val cardHeight = 380.dp

    const val paddingOffset = 32
    const val LOREM_IPSUM_FRONT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
    const val LOREM_IPSUM_BACK =
        "Integer dolor nisl, finibus eget dignissim sit amet, semper vel ipsum."

    // part. 3
    const val animationTime = 350

    const val BASE_URL = "http://192.168.2.105:8081"
}