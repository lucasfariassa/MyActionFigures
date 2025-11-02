package br.pucpr.appdev.lucasfariassa.myactionfigures.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ActionFigureDao {
    @Query("SELECT * FROM action_figures ORDER BY name ASC")
    fun getAll(): Flow<List<ActionFigure>>

    @Insert
    suspend fun insert(actionFigure: ActionFigure)

    @Update
    suspend fun update(actionFigure: ActionFigure)

    @Delete
    suspend fun delete(actionFigure: ActionFigure)
}