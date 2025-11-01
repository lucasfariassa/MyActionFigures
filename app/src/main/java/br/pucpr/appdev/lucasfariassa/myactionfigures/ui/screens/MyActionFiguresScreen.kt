package br.pucpr.appdev.lucasfariassa.myactionfigures.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import br.pucpr.appdev.lucasfariassa.myactionfigures.data.ActionFigure
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun MyActionFiguresScreen(
    actionFigures: List<ActionFigure>,
    onActionFigureClick: (ActionFigure) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(actionFigures) {
            ActionFigureCard(
                actionFigure = it,
                onActionFigureClick = onActionFigureClick
            )
        }
    }
}

@Composable
fun ActionFigureCard(
    actionFigure: ActionFigure,
    onActionFigureClick: (ActionFigure) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onActionFigureClick(actionFigure) }
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(actionFigure.photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Action Figure Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = actionFigure.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Obra: ${actionFigure.work}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Marca: ${actionFigure.brand}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
