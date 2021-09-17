package com.example.viewpager.deck

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.viewpager.utility.Constants.LOREM_IPSUM_FRONT
import com.example.viewpager.utility.Constants.cardHeight
import com.example.viewpager.utility.Constants.cardWidth
import com.example.viewpager.utility.Constants.normalSpace
import com.example.viewpager.utility.Constants.primaryColor
import com.example.viewpager.utility.Constants.smallSpace
import com.example.viewpager.utility.NiceButton

enum class CardFlipState {
    FRONT_FACE,
    BACK_FACE
}

val cornerRadiusBig = 20.dp
val normalElevation = 1.dp
val backSideColor = Color(0xFFfef49c)
val secondaryColor = Color(0xffb2ffa1)
val tertiaryColor = Color(0xFFb6caff)

@Composable
fun StudyCardView(
    modifier: Modifier = Modifier,
    side: CardFlipState = CardFlipState.FRONT_FACE,
    backgroundColor: Color = backSideColor,
    content: @Composable (Color) -> Unit,
    bottomBar: @Composable (Color) -> Unit
){
    val color = if (side == CardFlipState.FRONT_FACE){
        backgroundColor
    } else {
        backSideColor
    }
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(cornerRadiusBig),
        color = color,
        content = {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {},
                bottomBar = { bottomBar(backgroundColor) },
                content = { content(color) }
            )
        }
    )
}

@Composable
fun StudyCardsContent(
    data: String,
    backgroundColor: Color
){
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
fun StudyCardBottomBar(
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
        val buttonColor = if (side == CardFlipState.FRONT_FACE){
            backSideColor
        } else {
            frontSideColor
        }
        val leftTitle = if (side == CardFlipState.FRONT_FACE){
            "GO"
        } else {
            "Back"
        }
        val rightTitle = "Say"
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
            StudyCardBottomBar(
                0, 1, CardFlipState.FRONT_FACE, frontSideColor,
                leftActionHandler = { },
                rightActionHandler = { }
            )
        }
    )
}