package com.mankeu.presentation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/**
 * Arina Agri spatial, radius, and sizing tokens (4pt / 8pt grid + concentric corner geometry).
 *
 * Design Read: Offline-first mobile financial and farm-business app in Modern Balanced
 * Fintech-Agri style (Arina Green + Forest Charcoal), dial ENERGY 2 / RHYTHM 2 / MOTION 2.
 *
 * Concentric radius rule: Inner Radius = Outer Radius - Padding.
 * - Hero card (24.dp) with 16.dp-20.dp padding -> inner pill/tile 8.dp-12.dp
 * - Standard card (20.dp) with 12.dp-16.dp padding -> inner tile 8.dp-12.dp
 * - Segmented track (12.dp) with 4.dp padding -> active pill 8.dp
 */
object ArinaSpacing {
    val xxs = 4.dp
    val xs = 8.dp
    val sm = 12.dp
    val md = 16.dp
    val lg = 20.dp
    val xl = 24.dp
    val xxl = 32.dp

    // Tiered proximity spacing (Law of Proximity)
    val labelToInput = 8.dp
    val fieldToField = 16.dp
    val lastFieldToCta = 24.dp
    val titleToContent = 12.dp
    val cardGap = 12.dp
    val sectionGap = 24.dp

    val screenHorizontalCompact = 16.dp
    val screenHorizontalExpanded = 24.dp
    val fabListClearance = 96.dp
}

object ArinaRadius {
    val xs = 8.dp
    val sm = 12.dp
    val md = 16.dp
    val lg = 20.dp
    val xl = 24.dp
}

object ArinaShapes {
    val badge = RoundedCornerShape(ArinaRadius.xs)
    val segmentedTrack = RoundedCornerShape(ArinaRadius.sm)
    val segmentedThumb = RoundedCornerShape(ArinaRadius.xs) // 12 - 4 = 8dp concentric
    val input = RoundedCornerShape(ArinaRadius.sm)
    val button = RoundedCornerShape(ArinaRadius.sm)
    val tile = RoundedCornerShape(ArinaRadius.sm)
    val tileSmall = RoundedCornerShape(ArinaRadius.xs)
    val card = RoundedCornerShape(ArinaRadius.lg)
    val hero = RoundedCornerShape(ArinaRadius.xl)
    val sheet = RoundedCornerShape(topStart = ArinaRadius.xl, topEnd = ArinaRadius.xl)
}

object ArinaSizes {
    val minTouchTarget = 48.dp
    val buttonSmall = 40.dp
    val buttonMedium = 48.dp
    val buttonLarge = 56.dp
    val iconSm = 16.dp
    val iconMd = 20.dp
    val iconLg = 24.dp
    val hairline = 1.dp
}
