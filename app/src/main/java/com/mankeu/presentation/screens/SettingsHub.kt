package com.mankeu.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mankeu.R
import com.mankeu.presentation.*
import com.mankeu.presentation.components.ArinaCard

/** A single scrolling overview; account identity and business profile remain separate. */
@Composable
internal fun SettingsHub(
    accountName: String,
    themeMode: ThemeMode,
    language: AppLanguage,
    motionMode: MotionMode,
    signedIn: Boolean,
    onOpenPage: (SettingsPage) -> Unit,
    onOpenHelpCenter: () -> Unit,
) {
    val dark = isDarkScheme()
    fun accent(family: AccentFamily) = accentFamilyColors(family, dark)
    LazyColumn(
        state = rememberLazyListState(),
        modifier = Modifier.fillMaxSize().testTag("settings-home"),
        contentPadding = PaddingValues(ArinaSpacing.md),
        verticalArrangement = Arrangement.spacedBy(ArinaSpacing.lg),
    ) {
        item("profile") {
            ArinaCard(onClick = { onOpenPage(SettingsPage.PROFILE) }, modifier = Modifier.testTag("settings-profile")) {
                Row(Modifier.padding(ArinaSpacing.lg), verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                        Box(Modifier.size(52.dp), contentAlignment = Alignment.Center) {
                            Text(
                                accountName.trim().take(1).uppercase().ifBlank { "A" },
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                    Column(Modifier.weight(1f).padding(horizontal = ArinaSpacing.md), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(accountName.ifBlank { stringResource(R.string.profile_title) }, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(stringResource(R.string.settings_edit_profile), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        item("experience") {
            SettingsGroup(stringResource(R.string.settings_experience)) {
                SettingsLinkRow(stringResource(R.string.settings_display), stringResource(themeMode.labelRes), Icons.Default.Palette, accent(AccentFamily.BLUE), { onOpenPage(SettingsPage.APPEARANCE) }, Modifier.testTag("settings-appearance"))
                SettingsGroupDivider()
                SettingsLinkRow(stringResource(R.string.settings_language), language.label, Icons.Default.Language, accent(AccentFamily.MINT), { onOpenPage(SettingsPage.LANGUAGE) }, Modifier.testTag("settings-language"))
                SettingsGroupDivider()
                SettingsLinkRow(stringResource(R.string.settings_motion), stringResource(motionMode.labelRes), Icons.Default.Animation, accent(AccentFamily.LAVENDER), { onOpenPage(SettingsPage.MOTION) }, Modifier.testTag("settings-motion"))
                SettingsGroupDivider()
                SettingsLinkRow(stringResource(R.string.settings_feedback), stringResource(R.string.settings_feedback_summary), Icons.Default.Vibration, accent(AccentFamily.SAND), { onOpenPage(SettingsPage.FEEDBACK) }, Modifier.testTag("settings-feedback"))
            }
        }
        item("account") {
            SettingsGroup(stringResource(R.string.settings_optional_account)) {
                SettingsLinkRow(stringResource(R.string.settings_google_account), stringResource(if (signedIn) R.string.settings_google_connected else R.string.account_signed_out_status), Icons.Default.AccountCircle, accent(AccentFamily.MINT), { onOpenPage(SettingsPage.ACCOUNT) }, Modifier.testTag("settings-account"))
            }
        }
        item("help") {
            SettingsGroup(stringResource(R.string.help_center_title)) {
                SettingsLinkRow(stringResource(R.string.help_open_center), stringResource(R.string.settings_help_summary), Icons.AutoMirrored.Filled.HelpOutline, accent(AccentFamily.TEAL), onOpenHelpCenter, Modifier.testTag("settings-help"))
                SettingsGroupDivider()
                SettingsLinkRow(stringResource(R.string.settings_tour_title), stringResource(R.string.settings_guides_summary), Icons.Default.Explore, accent(AccentFamily.BLUE), { onOpenPage(SettingsPage.GUIDES) }, Modifier.testTag("settings-guides"))
            }
        }
        item("about") {
            SettingsGroup(stringResource(R.string.settings_about_entry)) {
                SettingsLinkRow(stringResource(R.string.settings_about_entry), stringResource(R.string.settings_about_summary), Icons.Default.Info, accent(AccentFamily.TERRACOTTA), { onOpenPage(SettingsPage.ABOUT) }, Modifier.testTag("settings-about"))
            }
        }
    }
}

@Composable
private fun SettingsGroupDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 68.dp, end = ArinaSpacing.md),
        thickness = ArinaSizes.hairline,
        color = MaterialTheme.colorScheme.outlineVariant,
    )
}

@Composable
private fun SettingsGroup(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(ArinaSpacing.xs)) {
        Text(
            title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = ArinaSpacing.xxs),
        )
        ArinaCard {
            Column(content = content)
        }
    }
}

@Composable
internal fun SettingsLinkRow(title: String, value: String, icon: ImageVector, accent: MetricAccentColors, onClick: () -> Unit, modifier: Modifier = Modifier) {
    ListItem(
        modifier = modifier.fillMaxWidth().heightIn(min = 64.dp).clickable(role = Role.Button, onClick = onClick),
        headlineContent = { Text(title, fontWeight = FontWeight.SemiBold) },
        supportingContent = { Text(value, color = MaterialTheme.colorScheme.onSurfaceVariant) },
        leadingContent = {
            Surface(color = accent.container, shape = ArinaShapes.tile) {
                Icon(icon, null, Modifier.padding(10.dp).size(ArinaSizes.iconMd), tint = accent.icon)
            }
        },
        trailingContent = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
    )
}
