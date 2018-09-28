package com.dcard.reactionsample.second

open class InterpolatedCalculator {

    protected fun calculateInterpolatedValue(start: Float, end: Float, fraction: Float) =
            start + fraction * (end - start)

    protected fun calculateInterpolatedValue(start: Int, end: Int, fraction: Float) =
            start + fraction * (end - start)
}