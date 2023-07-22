package com.lassanit.extras.interfaces

import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.annotation.IdRes
import com.lassanit.extras.classes.Designs
import com.lassanit.extras.customviews.PhoneText
import com.lassanit.extras.fragments.SuperFragment
import com.lassanit.extras.classes.App
import com.lassanit.extras.classes.Company

interface FragmentCallbacks {
    fun canAnimate(view: View)

    fun handleView(split: Boolean, portrait: Boolean, view: View?=null)

    fun setButtonDesign(view: View,@IdRes id:Int)
    fun setEdittextDesign(view: View,@IdRes id:Int)

    fun initApp(view: View)
    fun initCompany(view: View)
    fun initDesign(view: View)

    fun setApp(app: App): SuperFragment
    fun setCompany(company: Company?): SuperFragment
    fun setDesign(design: Designs): SuperFragment

    fun setDesign(button: Button?)
    fun setDesign(editText: EditText?)
    fun setDesign(phoneText: PhoneText?)


    fun hideView(parent: View,@IdRes id:Int, gone: Boolean=true)

    fun getDefaultLinker(): HashMap<Int, View>
}