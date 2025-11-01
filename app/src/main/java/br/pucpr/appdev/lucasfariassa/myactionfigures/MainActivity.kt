package br.pucpr.appdev.lucasfariassa.myactionfigures

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import br.pucpr.appdev.lucasfariassa.myactionfigures.data.ActionFigure
import br.pucpr.appdev.lucasfariassa.myactionfigures.ui.screens.ActionFigureDetailPopup
import br.pucpr.appdev.lucasfariassa.myactionfigures.ui.screens.HomeScreen
import br.pucpr.appdev.lucasfariassa.myactionfigures.ui.screens.MyActionFiguresScreen
import br.pucpr.appdev.lucasfariassa.myactionfigures.ui.screens.MyPublicProfileScreen
import br.pucpr.appdev.lucasfariassa.myactionfigures.ui.theme.MyActionFiguresTheme
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyActionFiguresTheme {
                MyActionFiguresApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun MyActionFiguresApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    var selectedActionFigure by remember { mutableStateOf<ActionFigure?>(null) }

    val actionFigures = remember {
        List(10) {
            ActionFigure(
                id = it.toLong(),
                name = "Action Figure $it",
                work = "Work $it",
                brand = "Brand $it",
                description = "Description $it",
                purchasePrice = (it * 10).toFloat(),
                purchaseDate = Date(),
            )
        }.toMutableStateList()
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            when (currentDestination) {
                AppDestinations.HOME -> HomeScreen(
                    onAddActionFigure = { /* TODO */ },
                    modifier = Modifier.padding(innerPadding)
                )
                AppDestinations.MYACTIONFIGURES -> MyActionFiguresScreen(
                    actionFigures = actionFigures,
                    onActionFigureClick = { selectedActionFigure = it },
                    modifier = Modifier.padding(innerPadding)
                )
                AppDestinations.PROFILE -> MyPublicProfileScreen(
                    actionFigures = actionFigures,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }

    selectedActionFigure?.let {
        ActionFigureDetailPopup(
            actionFigure = it,
            onDismiss = { selectedActionFigure = null },
            onFavoriteClick = { 
                val index = actionFigures.indexOf(it)
                actionFigures[index] = it.copy(isFavorite = !it.isFavorite)
                 selectedActionFigure = actionFigures[index] // Update the selected figure to reflect the change
            },
            onPublicClick = {
                val index = actionFigures.indexOf(it)
                actionFigures[index] = it.copy(isPublic = !it.isPublic)
                selectedActionFigure = actionFigures[index] // Update the selected figure to reflect the change
            }
        )
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Home", Icons.Default.Home),
    MYACTIONFIGURES("My Action Figures", Icons.Default.Favorite),
    PROFILE("Public Profile", Icons.Default.AccountBox),
}