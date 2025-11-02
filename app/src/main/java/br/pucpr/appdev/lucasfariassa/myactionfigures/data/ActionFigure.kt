package br.pucpr.appdev.lucasfariassa.myactionfigures.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "action_figures")
data class ActionFigure(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val work: String,
    val brand: String,
    val photoUrl: String? = null,
    val description: String,
    val purchasePrice: Float,
    val purchaseDate: Date,
    var isFavorite: Boolean = false,
    var isPublic: Boolean = false
)
