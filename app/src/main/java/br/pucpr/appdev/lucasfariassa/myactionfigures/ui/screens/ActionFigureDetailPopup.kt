package br.pucpr.appdev.lucasfariassa.myactionfigures.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.pucpr.appdev.lucasfariassa.myactionfigures.R
import br.pucpr.appdev.lucasfariassa.myactionfigures.data.ActionFigure
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ActionFigureDetailPopup(
    actionFigure: ActionFigure,
    onDismiss: () -> Unit,
    onFavoriteClick: () -> Unit,
    onPublicClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(actionFigure.photoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.action_figure_photo),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = actionFigure.name, style = MaterialTheme.typography.titleLarge)
                    Text(text = stringResource(R.string.work) + ": ${actionFigure.work}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = stringResource(R.string.brand) + ": ${actionFigure.brand}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = stringResource(R.string.description) + ": ${actionFigure.description}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = stringResource(R.string.value_with_money) + "${actionFigure.purchasePrice}", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = stringResource(R.string.purchase_date) + ": ${actionFigure.purchaseDate.toFormattedString()}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onFavoriteClick) {
                            Icon(
                                imageVector = if (actionFigure.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (actionFigure.isFavorite) stringResource(R.string.unfavorite) else stringResource(
                                    R.string.favorite_action
                                )
                            )
                        }
                        IconButton(onClick = onPublicClick) {
                            Icon(
                                painter = painterResource(id = if (actionFigure.isPublic) R.drawable.ic_public_globe else R.drawable.ic_no_public_globe),
                                contentDescription = if (actionFigure.isPublic) stringResource(R.string.hide_from_public_profile) else stringResource(
                                    R.string.display_on_public_profile
                                )
                            )
                        }
                        IconButton(onClick = onEditClick) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = stringResource(R.string.edit)
                            )
                        }
                        IconButton(onClick = onDeleteClick) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(id = R.string.delete)
                            )
                        }
                    }
                    Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.close))
                    }
                }
            }
        }
    }
}

fun java.util.Date.toFormattedString(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(this)
}
