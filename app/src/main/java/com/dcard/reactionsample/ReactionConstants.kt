package com.dcard.reactionsample

object ReactionConstants {

    const val SPACING = 8
    const val SIZE_SMALL = 30
    const val SIZE_LARGE = 70
    const val DURATION_TRANSACTION = 120L
    const val DURATION_HOVER = 100L

    fun getReactionViewSize(size: Int) =
            SIZE_LARGE + (size - 1) * SIZE_SMALL + (size + 1) * SPACING

    fun getNormalSize(size: Int) =
            (SIZE_LARGE + (size - 1) * SIZE_SMALL) / size

}