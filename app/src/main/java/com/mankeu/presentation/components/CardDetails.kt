package com.mankeu.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mankeu.R
import com.mankeu.presentation.ArinaShapes
import com.mankeu.presentation.ArinaSizes
import com.mankeu.presentation.ArinaSpacing

@Composable
fun CardDetails(
    stateKey: String,
    showLabel: String,
    hideLabel: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var open by rememberSaveable(stateKey) { mutableStateOf(false) }
    val description = stringResource(
        if (open) R.string.card_expanded else R.string.card_collapsed,
    )
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(ArinaSpacing.xs)) {
        TextButton(
            onClick = { open = !open },
            shape = ArinaShapes.button,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = ArinaSizes.minTouchTarget)
                .semantics { stateDescription = description },
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = if (open) hideLabel else showLabel,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.width(ArinaSpacing.xxs))
                Icon(
                    imageVector = if (open) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
        if (open) content()
    }
}
