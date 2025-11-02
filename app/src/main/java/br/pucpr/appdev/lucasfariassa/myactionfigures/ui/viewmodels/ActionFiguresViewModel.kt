package br.pucpr.appdev.lucasfariassa.myactionfigures.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.pucpr.appdev.lucasfariassa.myactionfigures.data.ActionFigure
import br.pucpr.appdev.lucasfariassa.myactionfigures.data.ActionFigureDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ActionFiguresViewModel(private val dao: ActionFigureDao) : ViewModel() {

    val allActionFigures: Flow<List<ActionFigure>> = dao.getAll()

    fun insert(actionFigure: ActionFigure) = viewModelScope.launch {
        dao.insert(actionFigure)
    }

    fun update(actionFigure: ActionFigure) = viewModelScope.launch {
        dao.update(actionFigure)
    }

    fun delete(actionFigure: ActionFigure) = viewModelScope.launch {
        dao.delete(actionFigure)
    }
}

class ActionFiguresViewModelFactory(private val dao: ActionFigureDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActionFiguresViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ActionFiguresViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}