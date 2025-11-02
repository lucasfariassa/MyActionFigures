package br.pucpr.appdev.lucasfariassa.myactionfigures.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.pucpr.appdev.lucasfariassa.myactionfigures.R
import br.pucpr.appdev.lucasfariassa.myactionfigures.data.ActionFigure

@Composable
fun MyPublicProfileScreen(
    actionFigures: List<ActionFigure>,
    modifier: Modifier = Modifier
) {
    val favorites = actionFigures.filter { it.isFavorite }
    val publicFigures = actionFigures.filter { it.isPublic }

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.my_public_profile),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(text = stringResource(R.string.action_figures_total) + ": ${actionFigures.size}")

        Text(
            text = stringResource(R.string.favorite),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 16.dp)
        )
        if (favorites.isEmpty()) {
            Text(text = stringResource(R.string.none_action_figure_favorited))
        } else {
            favorites.forEach {
                Text(text = "- ${it.name}")
            }
        }

        Text(
            text = stringResource(R.string.public_action_figures),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 16.dp)
        )
        if (publicFigures.isEmpty()) {
            Text(text = stringResource(R.string.none_public_action_figure))
        } else {
            publicFigures.forEach {
                Text(text = "- ${it.name}")
            }
        }
    }
}