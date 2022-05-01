package com.jetpack.animatenotification

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jetpack.animatenotification.ui.theme.AnimateNotificationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimateNotificationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "Animate Notification",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            )
                        }
                    ) {
                        AnimateNotification()
                    }
                }
            }
        }
    }
}

@Composable
fun AnimateNotification() {
    var needsAnimate by remember {
        mutableStateOf(NotificationDefinition.NotificationState.Idle)
    }
    val swayAnim by animateFloatAsState(
        targetValue = if (isMoveMode(needsAnimate)) 1f else 0f,
        animationSpec = NotificationDefinition.swayAnimation
    )
    val swayAnimReverse by animateFloatAsState(
        targetValue = if (isMoveMode(needsAnimate)) 1f else 0f,
        animationSpec = NotificationDefinition.swayReverse
    )

    LaunchedEffect(true) {
        needsAnimate = if (isMoveMode(needsAnimate)) NotificationDefinition.NotificationState.Idle else NotificationDefinition.NotificationState.Move
    }

    val containerHeight = 120.dp
    val height = 100.dp
    val clapperHeight = containerHeight.times(0.9f)
    val notificationHeight = containerHeight.times(0.1f)

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                needsAnimate = if (isMoveMode(needsAnimate)) NotificationDefinition.NotificationState.Idle else NotificationDefinition.NotificationState.Move
            }
        ) {
            Text(text = "Repeat Animation!")
        }

        Box(
            modifier = Modifier
                .padding(top = 15.dp)
                .size(containerHeight)
                .align(CenterHorizontally)
                .clickable {
                    needsAnimate =
                        if (isMoveMode(needsAnimate)) NotificationDefinition.NotificationState.Idle else NotificationDefinition.NotificationState.Move
                }
        ) {
            Svg(
                swayAnim = swayAnim,
                height = height,
                modifier = Modifier.align(Center)
            )
            Clapper(
                swayAnimReverse = swayAnimReverse,
                clapperHeight = clapperHeight,
                height = notificationHeight,
                swayAnim = swayAnim,
                modifier = Modifier
                    .align(Center)
            )
        }
    }
}


object NotificationDefinition {
    enum class NotificationState {
        Move, Idle
    }

    val swayAnimation = keyframes<Float> {
        durationMillis = 1000
        -10f at 0 with LinearEasing
        10f at 250 with LinearEasing
        -10f at 500 with LinearEasing
        5f at 750 with LinearEasing
        0f at 1000 with LinearEasing
    }

    val swayReverse = keyframes<Float> {
        durationMillis = 1000
        5f at 0 with LinearEasing
        -5f at 250 with LinearEasing
        5f at 500 with LinearEasing
        -5f at 750 with LinearEasing
        0f at 1000 with LinearEasing
    }
}

fun isMoveMode(needsAnimate: NotificationDefinition.NotificationState) =
    needsAnimate == NotificationDefinition.NotificationState.Move

@Composable
fun Clapper(
    swayAnimReverse: Float,
    clapperHeight: Dp,
    height: Dp,
    swayAnim: Float,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = CenterHorizontally, modifier = modifier.graphicsLayer(
            rotationZ = swayAnimReverse
        )
    ) {
        Box(
            Modifier
                .width(16.dp)
                .height(clapperHeight)
                .background(Color.Transparent)
        )
        Box(
            Modifier
                .width(24.dp)
                .height(height)
                .graphicsLayer(rotationZ = swayAnim)
                .background(
                    Color.Red,
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 15.dp,
                        bottomEnd = 15.dp
                    )
                )
        )
    }
}

@Composable
fun Svg(
    swayAnim: Float,
    height: Dp,
    modifier: Modifier
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_notification),
        contentDescription = "Notification Icon",
        tint = if (isSystemInDarkTheme()) Color.White else Color(0xFF0B0B31),
        modifier = modifier
            .size(height)
            .graphicsLayer(
                transformOrigin = TransformOrigin(0.5f, 0f),
                rotationZ = swayAnim
            )
    )
}
























