/*
 * Copyright (C) 2023 Romain Guy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("FunctionName", "OPT_IN_USAGE", "KotlinRedundantDiagnosticSuppress")
@file:OptIn(ExperimentalSplitPaneApi::class)

package dev.romainguy.kotlin.explorer

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.splitpane.*
import java.awt.Cursor

private fun Modifier.cursorForHorizontalResize(): Modifier =
    pointerHoverIcon(PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR)))

private fun Modifier.cursorForVerticalResize(): Modifier =
    pointerHoverIcon(PointerIcon(Cursor(Cursor.N_RESIZE_CURSOR)))

fun SplitterScope.HorizontalSplitter() {
    visiblePart {
        Box(
            Modifier
                .width(5.dp)
                .fillMaxHeight()
        )
    }
    handle {
        Box(
            Modifier
                .markAsHandle()
                .cursorForHorizontalResize()
                .width(5.dp)
                .fillMaxHeight()
        )
    }
}

fun SplitterScope.VerticalSplitter() {
    visiblePart {
        Box(
            Modifier
                .height(5.dp)
                .fillMaxWidth()
        )
    }
    handle {
        Box(
            Modifier
                .markAsHandle()
                .cursorForVerticalResize()
                .height(5.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun MultiSplitter(modifier: Modifier = Modifier, panels: List<@Composable () -> Unit>) {
    val size = panels.size
    if (size == 1) {
        panels[0]()
    } else {
        HorizontalSplitPane(
            modifier = modifier,
            splitPaneState = rememberSplitPaneState(initialPositionPercentage = 1.0f / size)
        ) {
            first { panels[0]() }
            second { MultiSplitter(modifier = modifier, panels.drop(1)) }
            splitter { HorizontalSplitter() }
        }
    }
}

@Composable
fun VerticalOptionalPanel(
    modifier: Modifier = Modifier,
    showOptionalPanel: Boolean = false,
    optionalPanel: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    if (showOptionalPanel) {
        VerticalSplitPane(
            modifier = modifier,
            splitPaneState = rememberSplitPaneState(initialPositionPercentage = 4f / 5f)
        ) {
            first { content() }
            second { optionalPanel() }
            splitter { VerticalSplitter() }
        }
    } else {
        content()
    }
}