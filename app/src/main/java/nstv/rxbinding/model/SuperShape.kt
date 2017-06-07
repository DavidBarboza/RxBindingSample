package nstv.rxbinding.model

import android.graphics.Color
import nstv.rxbinding.R

/**
 * Created by Nicole Terc on 6/2/17.
 */
data class SuperShape(
        var name: String? = null,
        var shape: Int = R.drawable.shape_square,
        var red: Int = 0,
        var green: Int = 0,
        var blue: Int = 0,
        var withBorder: Boolean = false,
        var redBorder: Int = 0,
        var greenBorder: Int = 0,
        var blueBorder: Int = 0,
        var withIcon: Boolean = false,
        var iconDrawable: Int = R.drawable.ic_android) {


    fun getBackgroundColor(): Int {
        return Color.rgb(red, green, blue)
    }

    fun getBorderColor(): Int {
        return Color.rgb(redBorder, greenBorder, blueBorder)
    }
}