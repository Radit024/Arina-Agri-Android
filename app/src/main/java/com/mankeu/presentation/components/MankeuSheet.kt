package com.mankeu.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mankeu.R
import com.mankeu.presentation.ArinaShapes
import com.mankeu.presentation.ArinaSizes
import com.mankeu.presentation.ArinaSpacing
import com.mankeu.presentation.LocalMotion
import com.mankeu.presentation.LocalMankeuSemanticColors
import com.mankeu.presentation.FeedbackEvent
import com.mankeu.presentation.LocalFeedback

/**
 * The app's one modal surface.
 *
 * Everything that asks a question used to be an `AlertDialog` — a box in the middle of the screen
 * with its actions at the top of the thumb's reach — while the transaction form was already a
 * bottom sheet. Two different answers to the same question in one app. This is the single
 * definition.
 *
 * Three things about its layout are deliberate:
 *
 * - **The actions never scroll away.** The content column takes the space that is left over
 *   (`weight(1f, fill = false)`, so a short sheet still wraps its content) and scrolls inside it,
 *   while the action row sits below it. Save and Batal are therefore on screen at every snap
 *   point, including half-expanded and with the keyboard up.
 * - **The sheet has snap points.** `skipPartiallyExpanded = false` gives a tall sheet a
 *   half-expanded stop the user can drag between, instead of one all-or-nothing height. Content
 *   shorter than half the screen simply wraps and never shows a partial state.
 * - **The keyboard pushes, it does not cover.** `imePadding()` on the whole column shrinks the
 *   sheet by the keyboard's height; because the content is scrollable, Compose's own
 *   bring-into-view then scrolls the focused field into what is left.
 *
 * [confirmLabel] `null` renders a single dismiss action, for sheets that only present information.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MankeuSheet(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    confirmLabel: String? = stringResource(R.string.action_save),
    confirmEnabled: Boolean = true,
    destructive: Boolean = false,
    warning: Boolean = false,
    dismissLabel: String = stringResource(R.string.action_cancel),
    // Multi-step sheets need the dismiss button to step back while a swipe-down still closes.
    onDismissClick: () -> Unit = onDismiss,
    onConfirm: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val motion = LocalMotion.current
    val semantic = LocalMankeuSemanticColors.current
    // Kept internal: `rememberModalBottomSheetState` is experimental, and as a default parameter it
    // is evaluated at every call site, which would force @OptIn on every caller.
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val feedback = LocalFeedback.current
    ModalBottomSheet(
        // Swipe-down, scrim tap and back all land here; the buttons below call their own handlers.
        onDismissRequest = { feedback(FeedbackEvent.TAP); onDismiss() },
        sheetState = sheetState,
        modifier = modifier,
        shape = ArinaShapes.sheet,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 0.dp,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .imePadding()
                .navigationBarsPadding()
                .padding(horizontal = ArinaSpacing.lg)
                .padding(bottom = ArinaSpacing.md),
            verticalArrangement = Arrangement.spacedBy(ArinaSpacing.md),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ArinaSpacing.xs),
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = { feedback(FeedbackEvent.TAP); onDismiss() }) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = stringResource(R.string.action_close),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(ArinaSizes.iconMd),
                    )
                }
            }
            Column(
                (if (motion.expressive) Modifier.animateContentSize(tween(motion.mediumMillis, easing = FastOutSlowInEasing)) else Modifier)
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(ArinaSpacing.md),
                content = content,
            )
            HorizontalDivider(thickness = ArinaSizes.hairline, color = MaterialTheme.colorScheme.outlineVariant)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ArinaSpacing.sm),
            ) {
                ArinaSecondaryButton(
                    text = dismissLabel,
                    onClick = onDismissClick,
                    modifier = Modifier.weight(1f),
                )
                if (confirmLabel != null && onConfirm != null) {
                    if (warning) {
                        Button(
                            onClick = onConfirm,
                            enabled = confirmEnabled,
                            shape = ArinaShapes.button,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = semantic.warning,
                                contentColor = semantic.onWarning,
                            ),
                            modifier = Modifier.weight(1f).heightIn(min = ArinaSizes.buttonMedium),
                        ) {
                            Text(confirmLabel, fontWeight = FontWeight.SemiBold, maxLines = 1)
                        }
                    } else {
                        ArinaPrimaryButton(
                            text = confirmLabel,
                            onClick = onConfirm,
                            enabled = confirmEnabled,
                            destructive = destructive,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}
