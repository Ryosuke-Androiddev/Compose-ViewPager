package com.example.viewpager.ui.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.viewpager.ui.onboard.OnBoardingItem
import com.example.viewpager.utility.Constants.SECOND_SCREEN
import com.example.viewpager.utility.Constants.TOP_SCREEN
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@ExperimentalPagerApi
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

@ExperimentalPagerApi
@Composable
fun ViewPagerLayout(navController: NavController){
    TopSection(navController = navController)
    val scope = rememberCoroutineScope()

    Column(
        Modifier.fillMaxSize()
    ) {
        TopSection(navController = navController)

        val items = OnBoardingItem.get()
        val state = rememberPagerState(pageCount = items.size)

        HorizontalPager(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .weight(0.8f)
        ) { page ->

            OnBoardingItems(items[page])
            // page means index i think so
        }

        BottomSections(
            size = items.size,
            index = state.currentPage
        ) {
            if (state.currentPage + 1 < items.size) {
                scope.launch {
                    state.scrollToPage(state.currentPage + 1)
                }
            }
        }
    }
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

@Composable
fun BottomSections(
    size: Int,
    index: Int,
    onNextClicked: ()->Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {

        //indicators
        Indicators(size = size, index = index)

        //next button
        TextButton(
            onClick = onNextClicked,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Text("Get Started",color = MaterialTheme.colors.onBackground)
        }
    }
}

@Composable
fun BoxScope.Indicators(size: Int, index: Int){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.align(Alignment.CenterStart)
    ) {
        // This part is difficult
        repeat(size){
            Indicator(isSelected = it == index)
        }
    }
}

@Composable
fun Indicator(isSelected: Boolean){

    val width = animateDpAsState(
        targetValue = if (isSelected) 25.dp else 10.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Box(
        modifier = Modifier
            .height(10.dp)
            .width(width.value) // variable value
            .clip(CircleShape)
            .background(
                if (isSelected) MaterialTheme.colors.primary
                else MaterialTheme.colors.onBackground.copy(alpha = 0.5f)
            )
    ) {

    }
}

@Composable
fun OnBoardingItems(
    item: OnBoardingItem
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        Image(painter = painterResource(id = item.image), contentDescription = null)

        Text(
            text = stringResource(id = item.title),
            fontSize = 24.sp,
            color = MaterialTheme.colors.onBackground,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(id = item.text),
            color = MaterialTheme.colors.onBackground.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
    }
}

// SECOND

@Composable
fun SecondLayout(){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Congrats!")
    }
}

