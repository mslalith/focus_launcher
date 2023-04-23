package dev.mslalith.focuslauncher.core.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun HorizontalSpacer(spacing: Dp) = Spacer(modifier = Modifier.width(width = spacing))

@Composable
fun VerticalSpacer(spacing: Dp) = Spacer(modifier = Modifier.height(height = spacing))

@Composable
fun RowScope.FillSpacer() = Spacer(modifier = Modifier.weight(weight = 1f))

@Composable
fun ColumnScope.FillSpacer() = Spacer(modifier = Modifier.weight(weight = 1f))
