package br.pucpr.appdev.lucasfariassa.myactionfigures.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            text = "Meu Perfil Público",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(text = "Total de Action Figures: ${actionFigures.size}")

        Text(
            text = "Favoritos",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 16.dp)
        )
        if (favorites.isEmpty()) {
            Text(text = "Nenhum action figure favoritado.")
        } else {
            favorites.forEach {
                Text(text = "- ${it.name}")
            }
        }

        Text(
            text = "Action Figures Públicos",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 16.dp)
        )
        if (publicFigures.isEmpty()) {
            Text(text = "Nenhum action figure público.")
        } else {
            publicFigures.forEach {
                Text(text = "- ${it.name}")
            }
        }
    }
}