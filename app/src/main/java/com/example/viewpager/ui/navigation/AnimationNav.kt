package com.example.viewpager.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.viewpager.utility.Constants.SECOND_SCREEN
import com.example.viewpager.utility.Constants.TOP_SCREEN

@Composable
fun ViewPagerAndNavigation(){

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = TOP_SCREEN
    ){
        composable(route = TOP_SCREEN){
            ViewPagerLayout(navController = navController)
        }
        composable(route = SECOND_SCREEN){
            SecondLayout()
        }
    }
}


// TOP

@Composable
fun ViewPagerLayout(navController: NavController){
    TopSection(navController = navController)
}

@Composable
fun TopSection(navController: NavController){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {

        //back button
        IconButton(
            onClick = {navController.navigate(SECOND_SCREEN)},
            modifier = Modifier.align(Alignment.CenterStart) // change Alignment value to understand crystal clear.
        ) {
            Icon(Icons.Outlined.KeyboardArrowLeft,null)
        }

        //skip button
        TextButton(
            onClick = {navController.navigate(SECOND_SCREEN)},
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Text("Skip",color = MaterialTheme.colors.onBackground)
        }
    }

}

// SECOND

@Composable
fun SecondLayout(){
    Text(text = "Congrats!")
}

