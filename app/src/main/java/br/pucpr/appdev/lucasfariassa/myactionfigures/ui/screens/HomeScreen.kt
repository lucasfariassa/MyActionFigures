package br.pucpr.appdev.lucasfariassa.myactionfigures.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import br.pucpr.appdev.lucasfariassa.myactionfigures.R
import br.pucpr.appdev.lucasfariassa.myactionfigures.ui.theme.*

@Composable
fun HomeScreen(
    onAddActionFigure: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_my_action_figures),
            contentDescription = "Logo",
            modifier = Modifier
                .size(325.dp)
                .padding(bottom = 18.dp)
        )
        Button(
            onClick = onAddActionFigure,
            colors = ButtonDefaults.buttonColors(BlueMAF)) {
            Text(stringResource(R.string.addNewActionFigure))
        }
    }
}
