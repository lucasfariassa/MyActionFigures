package br.pucpr.appdev.lucasfariassa.myactionfigures.data

import java.util.Date

data class ActionFigure(
    val id: Long,
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
