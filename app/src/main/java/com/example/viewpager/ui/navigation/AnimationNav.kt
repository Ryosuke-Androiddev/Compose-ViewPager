package com.example.viewpager.ui.navigation

import android.text.style.BackgroundColorSpan
import android.widget.Space
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.viewpager.R
import com.example.viewpager.ui.onboard.OnBoardingItem
import com.example.viewpager.utility.Constants.NAVIGATION_KEY
import com.example.viewpager.utility.Constants.SECOND_SCREEN
import com.example.viewpager.utility.Constants.THIRD_SCREEN
import com.example.viewpager.utility.Constants.THIRD_SCREEN_ROUTE
import com.example.viewpager.utility.Constants.TOP_SCREEN
import com.example.viewpager.utility.Constants.backSideColor
import com.example.viewpager.utility.Constants.cornerRadiusBig
import com.example.viewpager.utility.Constants.normalElevation
import com.example.viewpager.utility.Constants.normalSpace
import com.example.viewpager.utility.Constants.primaryColor
import com.example.viewpager.utility.Constants.smallSpace
import com.example.viewpager.utility.NiceButton
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import kotlin.collections.mutableListOf

@ExperimentalPagerApi
@Composable
fun ViewPagerAndNavigation(){

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.TopScreen.route
    ){
        composable(route = Screen.TopScreen.route){
            ViewPagerLayout(navController = navController)
        }
        composable(route = Screen.SecondScreen.route){
            SecondLayout(navController = navController)
        }
        composable(
            route = Screen.ThirdScreen.route + "/{name}",
            arguments = listOf(
                navArgument(NAVIGATION_KEY){
                    type = NavType.StringType
                    defaultValue = "You"
                    nullable = true
                }
            )
        ){ entry ->
            ThirdScreen(name = entry.arguments?.getString(NAVIGATION_KEY))
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
            if (state.currentPage >= 2){
                navController.navigate(Screen.SecondScreen.route)
            } else {
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
            onClick = {navController.navigate(Screen.SecondScreen.route)},
            modifier = Modifier.align(Alignment.CenterStart) // change Alignment value to understand crystal clear.
        ) {
            Icon(Icons.Outlined.KeyboardArrowLeft,null)
        }

        //skip button
        TextButton(
            onClick = {navController.navigate(Screen.SecondScreen.route)},
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
    onNextClicked: ()->Unit,
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
fun SecondLayout(navController: NavController){
    var text by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            imageVector = Icons.Filled.Person,
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .offset(y = 50.dp)
        )
        Text(
            text = "Vocabulary",
            fontSize = 30.sp,
            modifier = Modifier
                .offset(y = 40.dp),
            fontWeight = FontWeight.Bold
            )

        Spacer(Modifier.padding(100.dp))

        Text(
            text = "put your name",
            modifier = Modifier.offset(x = -105.dp, y = -5.dp),
            fontFamily = FontFamily.Serif
        )
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
            },
            label = {
                    Text(text = "name")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Go
            ),
            leadingIcon= {
                           IconButton(onClick = {}) {
                               Icon(
                                   imageVector = Icons.Filled.Person,
                                   contentDescription = "people")
                           }
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
        )
        ReturnToastMessage(text = text)
        RoundedButton(text = text, navController = navController)
    }
}

@Composable
fun ReturnToastMessage(
    text: String
){
    if (text.isEmpty()){

    }
}

@Composable
fun RoundedButton(
    text: String,
    navController: NavController,
){
        Button(
            onClick = {
                navController.navigate(Screen.ThirdScreen.withArgs(text))
            },
            shape = CircleShape,
            elevation = ButtonDefaults.elevation(0.dp,0.dp),
            contentPadding = PaddingValues(20.dp,12.dp),
            modifier = Modifier.offset(x=120.dp, y=16.dp)
        ) {
            Text(text = "Send")
        }
}

// Third Screen

@Composable
fun ThirdScreen(name: String?){
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "$name Memorize these phases.",
            Modifier.padding(50.dp))

        
        TestStudyCardView()
    }
}

enum class CardFlipState {
    FRONT_FACE,
    BACK_FACE,
    FLIP_BACK,
    FLIP_FRONT
}

enum class CardSwipeState {
    INITIAL,
    SWIPED,
    DRAGGING
}

@Composable
fun StudyCardView(
    modifier: Modifier = Modifier,
    side: CardFlipState = CardFlipState.FRONT_FACE,
    backgroundColor: Color = backSideColor,
    content: @Composable (Color) -> Unit,
    bottomBar: @Composable (Color) -> Unit
){
    val color = if (side == CardFlipState.FRONT_FACE) backgroundColor
    else backSideColor
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(cornerRadiusBig),
            color = color,
            elevation = normalElevation,
            content = {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    topBar = {},
                    bottomBar = {bottomBar(backgroundColor)},
                    content = {content(color)}
                )
            }
        )
}

@Composable
fun StudyCardsContent(data: String, backgroundColor: Color){
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(backgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = data,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(normalSpace),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun StudyCardsBottomBar(
    index: Int,
    count: Int,
    side: CardFlipState = CardFlipState.FRONT_FACE,
    frontSideColor: Color,
    leftActionHandler: (CardFlipState) -> Unit = {},
    rightActionHandler: () -> Unit = {}
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.padding(smallSpace)
    ) {
        val buttonColor = if (side == CardFlipState.FRONT_FACE) backSideColor else frontSideColor
        val leftTitle = if (side == CardFlipState.FRONT_FACE) "Peep" else "Back"
        val rightTitle = "Go"
        NiceButton(
            title = leftTitle,
            backgroundColor = buttonColor,
            onClick = { leftActionHandler.invoke(side) }
        )
        Spacer(Modifier.weight(1f))
        Text("${index + 1} of $count")
        Spacer(Modifier.weight(1f))
        NiceButton(
            title = rightTitle,
            backgroundColor = buttonColor,
            onClick = { rightActionHandler.invoke() }
        )
    }
}

val cardWidth = 350.dp
val cardHeight = 380.dp

const val paddingOffset = 32f
const val LOREM_IPSUM_FRONT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
const val LOREM_IPSUM_BACK =
    "Integer dolor nisl, finibus eget dignissim sit amet, semper vel ipsum."

@Composable
fun TestStudyCardFrontView() {
    StudyCardView(
        backgroundColor = primaryColor,
        side = CardFlipState.FRONT_FACE,
        modifier = Modifier.size(cardWidth, cardHeight),
        content = { frontSideColor ->
            StudyCardsContent(
                LOREM_IPSUM_FRONT,
                frontSideColor
            )
        },
        bottomBar = { frontSideColor ->
            StudyCardsBottomBar(
                0, 1, CardFlipState.FRONT_FACE, frontSideColor,
                leftActionHandler = { },
                rightActionHandler = { }
            )
        }
    )
}

