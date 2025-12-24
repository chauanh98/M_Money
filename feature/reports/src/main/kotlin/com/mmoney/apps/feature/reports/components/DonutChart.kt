package com.mmoney.apps.feature.reports.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.mmoney.apps.feature.reports.CategoryReportItem

@Composable
fun DonutChart(
    items: List<CategoryReportItem>,
    modifier: Modifier = Modifier
) {
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(items) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxWidth()) {
            val chartSize = size.minDimension
            val radius = chartSize / 2f
            val strokeWidth = 40.dp.toPx()
            val center = Offset(size.width / 2f, size.height / 2f)

            var startAngle = -90f

            items.forEach { item ->
                val sweepAngle = 360f * item.percentage * animationProgress.value

                drawArc(
                    color = item.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - radius + strokeWidth / 2, center.y - radius + strokeWidth / 2),
                    size = Size(chartSize - strokeWidth, chartSize - strokeWidth),
                    style = Stroke(width = strokeWidth)
                )

                startAngle += sweepAngle
            }
        }
    }
}