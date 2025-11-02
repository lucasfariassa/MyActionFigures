package br.pucpr.appdev.lucasfariassa.myactionfigures

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import br.pucpr.appdev.lucasfariassa.myactionfigures.data.ActionFigure
import br.pucpr.appdev.lucasfariassa.myactionfigures.data.AppDatabase
import br.pucpr.appdev.lucasfariassa.myactionfigures.ui.components.DeleteConfirmationDialog
import br.pucpr.appdev.lucasfariassa.myactionfigures.ui.screens.*
import br.pucpr.appdev.lucasfariassa.myactionfigures.ui.theme.MyActionFiguresTheme
import br.pucpr.appdev.lucasfariassa.myactionfigures.ui.viewmodels.ActionFiguresViewModel
import br.pucpr.appdev.lucasfariassa.myactionfigures.ui.viewmodels.ActionFiguresViewModelFactory

class MainActivity : ComponentActivity() {
    private val db by lazy { AppDatabase.getDatabase(this) }
    private val viewModel: ActionFiguresViewModel by viewModels {
        ActionFiguresViewModelFactory(db.actionFigureDao())
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyActionFiguresTheme {
                MyActionFiguresApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyActionFiguresApp(viewModel: ActionFiguresViewModel) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    var selectedActionFigure by remember { mutableStateOf<ActionFigure?>(null) }
    var editingActionFigure by remember { mutableStateOf<ActionFigure?>(null) }
    var isAddingOrEditing by rememberSaveable { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    val actionFigures by viewModel.allActionFigures.collectAsState(initial = emptyList())

    if (isAddingOrEditing) {
        FormScreen(
            actionFigure = editingActionFigure,
            onSave = { figure ->
                if (editingActionFigure == null) { // Adicionando
                    viewModel.insert(figure)
                } else { // Editando
                    viewModel.update(figure)
                }
                isAddingOrEditing = false
                editingActionFigure = null
            },
            onCancel = {
                isAddingOrEditing = false
                editingActionFigure = null
            }
        )
    } else {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                AppDestinations.entries.forEach {
                    item(
                        icon = { Icon(it.icon, contentDescription = it.label) },
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
                        onAddActionFigure = { isAddingOrEditing = true },
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
    }

    selectedActionFigure?.let { actionFigure ->
        ActionFigureDetailPopup(
            actionFigure = actionFigure,
            onDismiss = { selectedActionFigure = null },
            onFavoriteClick = { 
                viewModel.update(actionFigure.copy(isFavorite = !actionFigure.isFavorite))
                selectedActionFigure = actionFigure.copy(isFavorite = !actionFigure.isFavorite)
            },
            onPublicClick = {
                viewModel.update(actionFigure.copy(isPublic = !actionFigure.isPublic))
                selectedActionFigure = actionFigure.copy(isPublic = !actionFigure.isPublic)
            },
            onEditClick = {
                editingActionFigure = actionFigure
                selectedActionFigure = null
                isAddingOrEditing = true
            },
            onDeleteClick = {
                showDeleteConfirmation = true
            }
        )
    }

    if (showDeleteConfirmation) {
        DeleteConfirmationDialog(
            onConfirm = {
                selectedActionFigure?.let { viewModel.delete(it) }
                showDeleteConfirmation = false
                selectedActionFigure = null
            },
            onDismiss = { showDeleteConfirmation = false }
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