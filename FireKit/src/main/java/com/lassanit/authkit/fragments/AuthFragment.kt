package com.lassanit.authkit.fragments

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.lassanit.extras.customviews.PhoneText
import com.lassanit.extras.fragments.SuperFragment
import com.lassanit.firekit.R
import com.lassanit.authkit.interfaces.AuthActions


open class AuthFragment(@LayoutRes private var res: Int) : SuperFragment(res) {
    protected lateinit var actions: AuthActions
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AuthActions) {
            actions = context
        } else {
            throw IllegalArgumentException("Activity must implement AuthActions")
        }
    }

    override fun canAnimate(view: View) {}

    override fun handleView(split: Boolean, portrait: Boolean, view: View?) {
        if (view != null)
            hideView(view, R.id.authkit_id_app_logo, split)
    }

    override fun initDesign(view: View) {
        try {
            setButtonDesign(view,R.id.authkit_id_button_default)
            setButtonDesign(view,R.id.authkit_id_button_extra)

            setEdittextDesign(view,R.id.authkit_id_editext_email)
            setEdittextDesign(view,R.id.authkit_id_editext_password)
            setEdittextDesign(view,R.id.authkit_id_editext_password_confirm)
            setEdittextDesign(view,R.id.authkit_id_phonetext)

            setDesign(view.findViewById<PhoneText>(R.id.authkit_id_phonetext))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getDefaultLinker(): HashMap<Int, View> {
        val map = super.getDefaultLinker()
        val img = view?.findViewById<ImageView>(R.id.authkit_id_app_logo)
        if (img != null)
            map[R.string.tag_app_logo] = (img)
        val txt = view?.findViewById<TextView>(R.id.authkit_id_app_name)
        if (txt != null)
            map[R.string.tag_app_name] = (txt)
        return map
    }

}