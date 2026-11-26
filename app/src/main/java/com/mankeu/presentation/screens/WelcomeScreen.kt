package com.mankeu.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.border
import androidx.compose.ui.unit.dp
import com.mankeu.presentation.ArinaShapes
import com.mankeu.presentation.ArinaSizes
import com.mankeu.presentation.ArinaSpacing
import com.mankeu.presentation.components.primaryActionButtonColors
import com.mankeu.R

/** Presentation only: the app shell owns authentication, errors and first-run completion. */
@Composable
fun WelcomeScreen(
    signingIn: Boolean,
    onSignInGoogle: () -> Unit,
    onContinueOffline: () -> Unit,
) {
    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(Modifier.safeDrawingPadding().fillMaxSize(), contentAlignment = Alignment.Center) {
            val minHeight = maxHeight.coerceAtMost(900.dp)
            Column(
                Modifier.widthIn(max = 480.dp).fillMaxWidth().heightIn(max = 900.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    Modifier.widthIn(max = 480.dp).fillMaxWidth().heightIn(min = minHeight).padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Image(
                            painterResource(R.drawable.ic_arina_logo), null, Modifier.size(40.dp),
                            colorFilter = if (MaterialTheme.colorScheme.background.luminance() < 0.5f) {
                                ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            } else null,
                        )
                        Text("Arina Agri", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(Modifier.weight(1f))
                    BookkeepingArtwork(Modifier.padding(top = 24.dp, bottom = 16.dp))
                    Text(
                        stringResource(R.string.welcome_title),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.semantics { heading() },
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        stringResource(R.string.welcome_description),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.weight(1f))
                    Spacer(Modifier.height(32.dp))
                    Button(
                        onClick = onSignInGoogle,
                        enabled = !signingIn,
                        colors = primaryActionButtonColors().copy(
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                        shape = ArinaShapes.button,
                        modifier = Modifier.fillMaxWidth().heightIn(min = ArinaSizes.buttonLarge),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    ) {
                        if (signingIn) {
                            CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(Modifier.width(ArinaSpacing.sm))
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.ic_google_logo),
                                contentDescription = null,
                                tint = androidx.compose.ui.graphics.Color.Unspecified,
                                modifier = Modifier.size(20.dp),
                            )
                            Spacer(Modifier.width(ArinaSpacing.sm))
                        }
                        Text(
                            stringResource(if (signingIn) R.string.welcome_signing_in else R.string.welcome_google),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                        )
                    }
                    Spacer(Modifier.height(ArinaSpacing.sm))
                    OutlinedButton(
                        onClick = onContinueOffline,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                        border = BorderStroke(ArinaSizes.hairline, MaterialTheme.colorScheme.outline),
                        shape = ArinaShapes.button,
                        modifier = Modifier.fillMaxWidth().heightIn(min = ArinaSizes.buttonLarge),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    ) {
                        Text(
                            stringResource(R.string.welcome_offline),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}

/** Local, decorative shapes: no sample balance that could be mistaken for a real account. */
@Composable
private fun BookkeepingArtwork(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val isDark = colors.background.luminance() < 0.5f
    Box(modifier.size(208.dp, 184.dp).clearAndSetSemantics {}, contentAlignment = Alignment.Center) {
        Box(Modifier.size(168.dp).background(colors.primaryContainer, CircleShape))
        Column(
            Modifier.rotate(-8f).size(112.dp, 144.dp)
                .then(if (isDark) Modifier else Modifier.shadow(4.dp, RoundedCornerShape(16.dp)))
                .background(colors.surfaceContainerLow, RoundedCornerShape(16.dp))
                .border(ArinaSizes.hairline, colors.outlineVariant, RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(Modifier.width(32.dp).height(6.dp).background(colors.primary, RoundedCornerShape(4.dp)))
            repeat(3) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Check, null, Modifier.size(16.dp), tint = colors.primary)
                    Box(Modifier.weight(1f).height(4.dp).background(colors.outlineVariant, RoundedCornerShape(4.dp)))
                }
            }
        }
        Surface(
            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 12.dp, bottom = 12.dp).size(56.dp),
            shape = RoundedCornerShape(16.dp), color = colors.primary,
        ) {
            Icon(Icons.AutoMirrored.Filled.TrendingUp, null, Modifier.padding(14.dp), tint = colors.onPrimary)
        }
        Icon(Icons.Default.Eco, null, Modifier.align(Alignment.TopStart).padding(start = 12.dp, top = 12.dp).size(32.dp), tint = colors.primary)
    }
}
