package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.DpSize

/**
 * Digunakan untuk mengukur composable view,
 * hasil pengukuran bisa dipakai didalam [content]
 */
@Composable
fun Measurer(
    contentToMeasure: @Composable () -> Unit,
    content: @Composable (DpSize) -> Unit
) {
    SubcomposeLayout { constraints ->
        val measuredSize = subcompose("contentToMeasure", contentToMeasure)[0]
            .measure(Constraints())

        val contentPlaceable = subcompose("content") {
            content(DpSize(measuredSize.width.toDp(), measuredSize.height.toDp()))
        }[0].measure(constraints)

        layout(contentPlaceable.width, contentPlaceable.height) {
            contentPlaceable.place(0, 0)
        }
    }
}
