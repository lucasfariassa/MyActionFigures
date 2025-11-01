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
                    contentDescription = "Action Figure Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = actionFigure.name, style = MaterialTheme.typography.titleLarge)
                    Text(text = "Obra: ${actionFigure.work}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Marca: ${actionFigure.brand}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Descrição: ${actionFigure.description}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Valor: R$${actionFigure.purchasePrice}", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "Data da compra: ${actionFigure.purchaseDate.toFormattedString()}",
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
                                contentDescription = if (actionFigure.isFavorite) "Desfavoritar" else "Favoritar"
                            )
                        }
                        IconButton(onClick = onPublicClick) {
                            if (actionFigure.isPublic) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_public_globe),
                                    contentDescription = "Ocultar do perfil público"
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_no_public_globe),
                                    contentDescription = "Exibir no perfil público"
                                )
                            }
                        }
                    }
                    Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                        Text("Fechar")
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
