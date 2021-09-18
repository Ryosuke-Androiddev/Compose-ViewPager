package com.example.viewpager.ui.navigation

sealed class Screen(val route: String){

    object TopScreen: Screen("top_screen")
    object SecondScreen: Screen("second_screen")
    object ThirdScreen: Screen("third_screen")

    fun withArgs(vararg args: String): String{
        return buildString {
            append(route)
            args.forEach { arg->
                append("/$arg")
            }
        }
    }
}
