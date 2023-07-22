package com.lassanit.extras.classes

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.lassanit.firekit.R

class Designs(
    var activity: Activity? = null,
    var btn: Button? = null,
    var editText: EditText? = null
) {
    object Params {
        const val DEFAULT_ACTIVITY = 1
        const val DEFAULT_BUTTON = 2
        const val DEFAULT_EDITTEXT = 4
    }

    constructor(flags: Int)
            : this(
        activity = if ((flags and Params.DEFAULT_ACTIVITY) != 0) Activity() else null,
        btn = if ((flags and Params.DEFAULT_BUTTON) != 0) Button() else null,
        editText = if ((flags and Params.DEFAULT_EDITTEXT) != 0) EditText() else null
    ) {
    }

    class Activity(@DrawableRes var background: Int = R.drawable.authkit_shape_default_activity_background) {

    }

    class EditText(
        @DrawableRes var background: Int = R.drawable.authkit_shape_default_edittext,
        @ColorRes var textColor: Int = R.color.optheme,
        @ColorRes var hintColor: Int = R.color.center,
        @ColorRes var drawableTint: Int = R.color.optheme
    ) {}

    class Button(
        @DrawableRes var background: Int = R.drawable.authkit_shape_default_button,
        @ColorRes var textColor: Int = R.color.theme,
        @ColorRes var tintDrawable: Int = R.color.theme
    ) {}

}