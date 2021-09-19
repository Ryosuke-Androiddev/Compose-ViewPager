package com.example.viewpager.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.viewpager.ui.navigation.model.StudyCardDeckEvent
import com.example.viewpager.ui.navigation.model.StudyCardDeckModel
import com.example.viewpager.utility.Constants.primaryColor
import com.example.viewpager.utility.Constants.secondaryColor
import com.example.viewpager.utility.Constants.tertiaryColor
import com.example.viewpager.utility.NiceButton
import kotlinx.coroutines.CoroutineScope

data class StudyCard(
    val index: Int,
    val frontVal: String,
    val backVal: String,
    val frontLang: String = "English",
    val backLang: String = "English"
)

private const val TOP_CARD_INDEX = 0
private const val TOP_Z_INDEX = 100f

private val colors = arrayOf(primaryColor, secondaryColor, tertiaryColor)

private fun calculateScale(idx: Int): Float {
    return 1f - idx * (1f / 10f)
}

private fun calculateOffset(idx: Int): Int {
    return (paddingOffset * (idx + 1)).toInt()
}

@Composable
fun StudyCardDeck(
    coroutineScope: CoroutineScope,
    model: StudyCardDeckModel,
    events: StudyCardDeckEvent
) {

    events.apply {
        flipCard.Init()
        cardsInDeck.Init()
        cardSwipe.Init()
    }
    Box(Modifier.fillMaxSize()) {
        repeat(model.visibleCards) { visibleIndex ->
            val isFront = events.flipCard.isFrontSide()
            val card = model.cardVisible(visibleIndex)
            val cardColor = model.colorForIndex(visibleIndex)
            val cardData = if (isFront) card.frontVal else card.backVal
            val cardLanguage = if (isFront) card.frontLang else card.backLang
            val cardSide = if (visibleIndex > TOP_CARD_INDEX) {
                CardFlipState.FRONT_FACE
            } else {
                events.flipCard.cardSide()
            }
            val cardZIndex = TOP_Z_INDEX - visibleIndex
            val cardModifier = events.makeCardModifier(
                coroutineScope,
                TOP_CARD_INDEX,
                visibleIndex)
                .align(Alignment.TopCenter)
                .zIndex(cardZIndex)
                .size(cardWidth, cardHeight)

            StudyCardView(
                backgroundColor = cardColor,
                side = cardSide,
                modifier = cardModifier,
                content = { frontSideColor ->
                    StudyCardsContent(
                        cardData, frontSideColor
                    )
                },
                bottomBar = { frontSideColor ->
                    if (visibleIndex == TOP_CARD_INDEX) {
                        StudyCardsBottomBar(
                            index = card.index,
                            count = model.count,
                            side = cardSide,
                            frontSideColor = frontSideColor,
                            leftActionHandler = { buttonOnSide ->
                                if (buttonOnSide == CardFlipState.FRONT_FACE) {
                                    events.flipCard.flipToBackSide()
                                } else {
                                    events.flipCard.flipToFrontSide()
                                }
                            },
                            rightActionHandler = {
                                events.playHandler.invoke(cardData, cardLanguage)
                            }
                        )
                    }
                }
            )
            events.cardSwipe.backToInitialState(coroutineScope)
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
        bottomBar = {
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
        //StudyCardDeck(current, visible, data)
    }
}

@Composable
fun TestStudyCardView() {
    val data = arrayListOf(
        StudyCard(0, "1$LOREM_IPSUM_FRONT", "1$LOREM_IPSUM_BACK"),
        StudyCard(1, "2$LOREM_IPSUM_FRONT", "2$LOREM_IPSUM_BACK"),
        StudyCard(2, "3$LOREM_IPSUM_FRONT", "3$LOREM_IPSUM_BACK"),
        StudyCard(3, "4$LOREM_IPSUM_FRONT", "4$LOREM_IPSUM_BACK"),
        StudyCard(4, "5$LOREM_IPSUM_FRONT", "5$LOREM_IPSUM_BACK"),
        StudyCard(5, "6$LOREM_IPSUM_FRONT", "6$LOREM_IPSUM_BACK")
    )
    var topCardIndex by remember { mutableStateOf(0) }
    val model = StudyCardDeckModel(
        current = topCardIndex,
        dataSource = data,
        visible = 3,
        screenWidth = 1200,
        screenHeight = 1600
    )
    val events = StudyCardDeckEvent(
        cardWidth = model.cardWidthPx(),
        cardHeight = model.cardHeightPx(),
        model = model,
        peepHandler = {},
        playHandler = { _, _ ->
        },
        nextHandler = {
            if (topCardIndex < data.lastIndex) {
                topCardIndex += 1
            } else {
                topCardIndex = 0
            }
        }
    )
    val coroutineScope = rememberCoroutineScope()

    Column {
        NiceButton(title = "Test Swipe") {
            events.cardSwipe.animateToTarget(
                coroutineScope,
                CardSwipeState.SWIPED
            ) {
                if (topCardIndex < data.lastIndex) {
                    topCardIndex += 1
                } else {
                    topCardIndex = 0
                }
            }
        }
        StudyCardDeck(coroutineScope, model, events)
    }
}