package com.mankeu.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mankeu.presentation.ArinaShapes
import com.mankeu.presentation.ArinaSizes
import com.mankeu.presentation.ArinaSpacing
import com.mankeu.presentation.FeedbackEvent
import com.mankeu.presentation.LocalFeedback
import com.mankeu.presentation.LocalMotion
import com.mankeu.presentation.isDarkScheme

/**
 * Hairline border color calibrated per theme:
 * Light uses a crisp subtle warm hairline; Dark uses a soft elevated border without white glow.
 */
@Composable
fun arinaHairlineBorder(): BorderStroke {
    val color = if (isDarkScheme()) {
        MaterialTheme.colorScheme.outlineVariant
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }
    return BorderStroke(ArinaSizes.hairline, color)
}

/**
 * Core Card primitive: uses a single crisp 1dp hairline border with zero muddy drop shadow,
 * consistent 20dp corner radius, and explicit surfaceContainerLow tone across Light and Dark.
 */
@Composable
fun ArinaCard(
    modifier: Modifier = Modifier,
    shape: Shape = ArinaShapes.card,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    border: BorderStroke? = arinaHairlineBorder(),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = containerColor),
            border = border,
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            content = content,
        )
    } else {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = containerColor),
            border = border,
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            content = content,
        )
    }
}

/**
 * Primary filled CTA button: strictly at most one solid filled CTA per screen/dialog/card.
 * Uses optical horizontal padding (slightly tighter on the leading-icon side) and 12dp radius.
 */
@Composable
fun ArinaPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    loading: Boolean = false,
    large: Boolean = false,
    destructive: Boolean = false,
) {
    val height = if (large) ArinaSizes.buttonLarge else ArinaSizes.buttonMedium
    val startPad = if (icon != null || loading) 16.dp else 24.dp
    val endPad = 24.dp
    val verticalPad = if (large) 16.dp else 12.dp
    val colors = if (destructive) {
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    } else {
        primaryActionButtonColors().copy(
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        shape = ArinaShapes.button,
        colors = colors,
        contentPadding = PaddingValues(start = startPad, end = endPad, top = verticalPad, bottom = verticalPad),
        modifier = modifier.heightIn(min = height),
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.width(8.dp))
        } else if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(ArinaSizes.iconMd))
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = if (large) MaterialTheme.typography.titleMedium else MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/**
 * Secondary button (outlined or tonal): equal height to primary (48dp/56dp) so hierarchy comes
 * from visual weight rather than shrinking tap targets.
 */
@Composable
fun ArinaSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    large: Boolean = false,
    tonal: Boolean = false,
) {
    val height = if (large) ArinaSizes.buttonLarge else ArinaSizes.buttonMedium
    val startPad = if (icon != null) 16.dp else 20.dp
    val endPad = 20.dp
    val verticalPad = if (large) 16.dp else 12.dp
    if (tonal) {
        FilledTonalButton(
            onClick = onClick,
            enabled = enabled,
            shape = ArinaShapes.button,
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            contentPadding = PaddingValues(start = startPad, end = endPad, top = verticalPad, bottom = verticalPad),
            modifier = modifier.heightIn(min = height),
        ) {
            if (icon != null) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(ArinaSizes.iconMd))
                Spacer(Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            enabled = enabled,
            shape = ArinaShapes.button,
            border = BorderStroke(ArinaSizes.hairline, MaterialTheme.colorScheme.outline),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary,
            ),
            contentPadding = PaddingValues(start = startPad, end = endPad, top = verticalPad, bottom = verticalPad),
            modifier = modifier.heightIn(min = height),
        ) {
            if (icon != null) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(ArinaSizes.iconMd))
                Spacer(Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/**
 * Concentric segmented control for 2-3 equal-width opposing choices (e.g., Proyeksi / Realisasi,
 * Uang Masuk / Uang Keluar, Report tabs).
 *
 * Geometry: 12dp outer track - 4dp padding = 8dp concentric inner thumb; minimum 48dp touch height.
 */
@Composable
fun <T> ArinaSegmentedControl(
    options: List<Pair<T, String>>,
    selected: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
) {
    val motion = LocalMotion.current
    val feedback = LocalFeedback.current
    val dark = isDarkScheme()
    val trackColor = MaterialTheme.colorScheme.surfaceContainer
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = ArinaShapes.segmentedTrack,
        color = trackColor,
        border = BorderStroke(ArinaSizes.hairline, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup()
                .padding(ArinaSpacing.xxs),
            horizontalArrangement = Arrangement.spacedBy(ArinaSpacing.xxs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            options.forEach { (value, label) ->
                val isSelected = value == selected
                val thumbBg by animateColorAsState(
                    targetValue = when {
                        isSelected && dark -> MaterialTheme.colorScheme.primaryContainer
                        isSelected -> MaterialTheme.colorScheme.surfaceContainerLow
                        else -> Color.Transparent
                    },
                    animationSpec = tween(maxOf(motion.fastMillis, 1), easing = FastOutSlowInEasing),
                    label = "segBg",
                )
                val thumbBorder by animateColorAsState(
                    targetValue = when {
                        isSelected && dark -> MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)
                        isSelected -> MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                        else -> Color.Transparent
                    },
                    animationSpec = tween(maxOf(motion.fastMillis, 1), easing = FastOutSlowInEasing),
                    label = "segBorder",
                )
                val labelColor by animateColorAsState(
                    targetValue = when {
                        isSelected && dark -> MaterialTheme.colorScheme.onPrimaryContainer
                        isSelected -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    animationSpec = tween(maxOf(motion.fastMillis, 1), easing = FastOutSlowInEasing),
                    label = "segLabel",
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = if (maxLines > 1) 48.dp else 40.dp)
                        .clip(ArinaShapes.segmentedThumb)
                        .background(thumbBg)
                        .then(
                            if (isSelected) Modifier.border(ArinaSizes.hairline, thumbBorder, ArinaShapes.segmentedThumb)
                            else Modifier
                        )
                        .selectable(
                            selected = isSelected,
                            role = Role.Tab,
                            onClick = {
                                if (!isSelected) {
                                    feedback(FeedbackEvent.TAP)
                                    onSelected(value)
                                }
                            },
                        )
                        .padding(
                            horizontal = if (maxLines > 1) ArinaSpacing.xs else ArinaSpacing.sm,
                            vertical = ArinaSpacing.xs,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = label,
                        style = if (maxLines > 1) MaterialTheme.typography.labelMedium else MaterialTheme.typography.labelLarge,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = labelColor,
                        textAlign = TextAlign.Center,
                        maxLines = maxLines,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

/**
 * Clean proportional horizontal bar for expense breakdown, income/expense ratios, and stock pacing.
 */
@Composable
fun ArinaRatioBar(
    fraction: Float,
    color: Color,
    modifier: Modifier = Modifier,
    trackColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    height: Dp = 8.dp,
) {
    val clamped = fraction.coerceIn(0f, 1f)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(height / 2))
            .background(trackColor),
    ) {
        if (clamped > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(clamped)
                    .height(height)
                    .clip(RoundedCornerShape(height / 2))
                    .background(color),
            )
        }
    }
}

/**
 * Persistent top label wrapper for boxed form inputs (Master Playbook 6.2).
 */
@Composable
fun ArinaFieldGroup(
    label: String,
    modifier: Modifier = Modifier,
    optionalTag: String? = null,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(ArinaSpacing.labelToInput),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ArinaSpacing.xxs),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
            )
            if (!optionalTag.isNullOrBlank()) {
                Text(
                    text = optionalTag,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        content()
    }
}
