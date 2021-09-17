package com.example.viewpager.deck

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.viewpager.utility.Constants.LOREM_IPSUM_BACK
import com.example.viewpager.utility.Constants.LOREM_IPSUM_FRONT
import com.example.viewpager.utility.Constants.cardHeight
import com.example.viewpager.utility.Constants.cardWidth
import com.example.viewpager.utility.Constants.paddingOffset
import com.example.viewpager.utility.Constants.primaryColor
import com.example.viewpager.utility.NiceButton

data class StudyCard(
    val index: Int,
    val frontVal: String,
    val backVal: String,
    val frontLang: String = "English",
    val backLang: String = "English"
)

private val colors = arrayOf(primaryColor, secondaryColor, tertiaryColor)

private fun calculateScale(idx: Int): Float {
    return 1f - idx * (1f / 10f)
}

private fun calculateOffset(idx: Int): Int {
    return (paddingOffset * (idx + 1)).toInt()
}

@Composable
fun StudyCardDeck(
    current: Int,
    visible: Int,
    dataSource: List<StudyCard>
){
    val topCardIdx = 0
    val count =dataSource.size
    val visibleCards: Int = StrictMath.min(visible, dataSource.size - current)

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        repeat(visibleCards) { idx ->

            val index = current + idx
            val card = dataSource[idx]
            val colorIndex = card.index % colors.size
            val cardColor = colors[colorIndex]
            val data = card.frontVal
            val zIndex = 100f - idx
            val scaleX = calculateScale(idx)
            val offsetY = calculateOffset(idx)
            val cardModifier = Modifier
                .scale(scaleX, 1f)
                .offset { IntOffset(0, offsetY) }
                .align(Alignment.TopCenter)
                .zIndex(zIndex)
                .size(cardWidth, cardHeight)

            StudyCardView(
                backgroundColor = cardColor,
                side = CardFlipState.FRONT_FACE,
                modifier = cardModifier,
                content = { frontSideColor ->
                    StudyCardsContent(
                        data, frontSideColor
                    )
                },
                bottomBar = { frontSideColor ->
                    if (idx == topCardIdx) {
                        StudyCardBottomBar(
                            card.index, count, CardFlipState.FRONT_FACE, frontSideColor,
                            leftActionHandler = { buttonOnSide ->
                            },
                            rightActionHandler = { }
                        )
                    }
                }
            )
        }
    }
}

val data = arrayListOf(
    StudyCard(0, "1$LOREM_IPSUM_FRONT", "1$LOREM_IPSUM_BACK"),
    StudyCard(1, "2$LOREM_IPSUM_FRONT", "2$LOREM_IPSUM_BACK"),
    StudyCard(2, "3$LOREM_IPSUM_FRONT", "3$LOREM_IPSUM_BACK"),
    StudyCard(3, "4$LOREM_IPSUM_FRONT", "4$LOREM_IPSUM_BACK"),
    StudyCard(4, "5$LOREM_IPSUM_FRONT", "5$LOREM_IPSUM_BACK"),
    StudyCard(5, "6$LOREM_IPSUM_FRONT", "6$LOREM_IPSUM_BACK")
)

@Composable
fun TestStudyCardDeck() {
    var current by remember { mutableStateOf(0) }
    var visible by remember { mutableStateOf(3) }

    Scaffold(
        topBar = {
            Column {
                Row(Modifier.padding(16.dp)) {
                    NiceButton(title = "Next") {
                        if (current < data.lastIndex)
                            current += 1
                        else
                            current = 0
                    }
                    Spacer(Modifier.weight(1f))
                    NiceButton(title = "Add") {
                        if (visible < data.lastIndex)
                            visible += 1
                        else
                            visible = 1
                    }
                }
            }
        }
    ) {
        StudyCardDeck(current, visible, data)
    }
}