package br.com.firstsoft.target.server.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.firstsoft.target.server.ui.ColorTokens.AlmostVisibleGray
import br.com.firstsoft.target.server.ui.ColorTokens.DarkGray

@Composable
fun SliderThumb() {
    Spacer(
        Modifier
            .size(20.dp)
            .border(2.dp, DarkGray, CircleShape)
            .indication(
                interactionSource = remember { MutableInteractionSource() },
                indication = androidx.compose.material.ripple.rememberRipple(
                    bounded = false,
                    radius = 20.dp
                )
            )
            .hoverable(interactionSource = remember { MutableInteractionSource() })
            .background(Color.White, CircleShape)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
val SliderState.coercedValueAsFraction
    get() = calcFraction(
        valueRange.start,
        valueRange.endInclusive,
        value.coerceIn(valueRange.start, valueRange.endInclusive)
    )

fun calcFraction(a: Float, b: Float, pos: Float) =
    (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)

fun DrawScope.drawTrack(
    tickFractions: FloatArray,
    activeRangeStart: Float,
    activeRangeEnd: Float,
    inactiveTrackColor: Color,
    activeTrackColor: Color,
    inactiveTickColor: Color,
    activeTickColor: Color,
    textMeasurer: TextMeasurer
) {
    val isRtl = layoutDirection == LayoutDirection.Rtl
    val sliderLeft = Offset(0f, center.y)
    val sliderRight = Offset(size.width, center.y)
    val sliderStart = if (isRtl) sliderRight else sliderLeft
    val sliderEnd = if (isRtl) sliderLeft else sliderRight
    val tickSize = 2.dp.toPx()
    val trackStrokeWidth = 12.dp.toPx()

    drawLine(
        inactiveTrackColor,
        sliderStart,
        sliderEnd,
        trackStrokeWidth,
        StrokeCap.Round
    )
    val sliderValueEnd = Offset(
        sliderStart.x +
                (sliderEnd.x - sliderStart.x) * activeRangeEnd,
        center.y
    )

    val sliderValueStart = Offset(
        sliderStart.x +
                (sliderEnd.x - sliderStart.x) * activeRangeStart,
        center.y
    )

    drawLine(
        activeTrackColor,
        sliderValueStart,
        sliderValueEnd,
        trackStrokeWidth,
        StrokeCap.Round
    )

    val style = TextStyle(
        fontSize = 13.sp,
        color = AlmostVisibleGray,
        lineHeight = 0.sp,
        fontWeight = FontWeight.Normal,
    )

    for ((i, tick) in tickFractions.withIndex()) {
        val outsideFraction = tick > activeRangeEnd || tick < activeRangeStart
        drawCircle(
            color = if (outsideFraction) inactiveTickColor else activeTickColor,
            center = Offset(lerp(sliderStart, sliderEnd, tick).x, center.y),
            radius = tickSize / 2f
        )
        val layoutResult = textMeasurer.measure("${i.times(10)}", style)
        val lastOffset = if (i == tickFractions.size - 1) 10.5f else 0f
        drawText(
            textMeasurer = textMeasurer,
            text = "${i.times(10)}",
            style = if (tick != activeRangeEnd) style else style.copy(color = DarkGray),
            topLeft = Offset(
                (lerp(sliderStart, sliderEnd, tick).x - layoutResult.size.width / 2) - lastOffset,
                center.y + 12
            )
        )
    }
}