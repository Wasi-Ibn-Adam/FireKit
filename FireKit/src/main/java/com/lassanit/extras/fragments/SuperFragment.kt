package com.lassanit.extras.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.lassanit.extras.classes.Designs
import com.lassanit.extras.customviews.PhoneText
import com.lassanit.extras.interfaces.AppCallbacks
import com.lassanit.extras.interfaces.FragmentCallbacks
import com.lassanit.firekit.R
import com.lassanit.extras.classes.App
import com.lassanit.extras.classes.Company


abstract class SuperFragment(@LayoutRes private var res: Int) : Fragment(), FragmentCallbacks {

    private lateinit var appCallbacks: AppCallbacks
    private var design: Designs? = null
    private var company: Company? = null
    protected var app: App? = null
    var handleSplit: Boolean = false
    var handleOrientation: Boolean = false
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AppCallbacks) {
            appCallbacks = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Set the shared element enter transition
        sharedElementEnterTransition = getTransition(
            requireContext(),
            android.R.transition.move
        )
        sharedElementReturnTransition = getTransition(
            requireContext(),
            android.R.transition.move
        )
        val view = inflater.inflate(res, container, false)
        canAnimate(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initApp(view)
        initCompany(view)
        initDesign(view)
        handleView(handleSplit, handleOrientation, view)
    }

    override fun hideView(parent: View, id: Int, gone: Boolean) {
        try {
            parent.findViewById<View>(id)?.visibility = if (gone) View.GONE else View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun getTransition(
        context: Context?, rid: Int,
        start: Runnable? = null,
        end: Runnable? = null,
        cancel: Runnable? = null,
        pause: Runnable? = null,
        resume: Runnable? = null
    ): Transition {
        return TransitionInflater.from(context).inflateTransition(rid)
            .apply {
                addListener(object : Transition.TransitionListener {
                    override fun onTransitionStart(transition: Transition) {
                        // Called when the transition starts
                        start?.run()
                    }

                    override fun onTransitionEnd(transition: Transition) {
                        // Called when the transition ends
                        end?.run()
                    }

                    override fun onTransitionCancel(transition: Transition) {
                        // Called when the transition is canceled
                        cancel?.run()
                    }

                    override fun onTransitionPause(transition: Transition) {
                        // Called when the transition is paused
                        pause?.run()
                    }

                    override fun onTransitionResume(transition: Transition) {
                        // Called when the transition is resumed
                        resume?.run()
                    }
                })
            }
    }

    override fun setDesign(design: Designs): SuperFragment {
        this.design = design
        return this
    }

    override fun setCompany(company: Company?): SuperFragment {
        this.company = company
        return this
    }

    override fun setApp(app: App): SuperFragment {
        this.app = app
        return this
    }

    override fun setButtonDesign(view: View, id: Int) {
        try {
            setDesign(view.findViewById<Button>(id))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setEdittextDesign(view: View, id: Int) {
        try {
            setDesign(view.findViewById<EditText>(id))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun initApp(view: View) {
        try {
            if (app != null) {
                view.findViewById<ImageView>(R.id.authkit_id_app_logo)?.setImageResource(app!!.res)
                view.findViewById<TextView>(R.id.authkit_id_app_name)?.text = app!!.name
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun initCompany(view: View) {
        try {
            if (company != null) {
                view.findViewById<ImageView>(R.id.authkit_id_company_logo)
                    ?.setImageResource(company!!.res)
                view.findViewById<TextView>(R.id.authkit_id_company_name)?.text = company!!.name
            }
            view.findViewById<View>(R.id.authkit_id_company)?.visibility =
                if (company == null) View.INVISIBLE else View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    override fun setDesign(editText: EditText?) {
        try {
            if (design == null || design!!.editText == null) return
            editText?.setBackgroundResource(design!!.editText!!.background)
            editText?.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    design!!.editText!!.textColor
                )
            )
            editText?.setHintTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    design!!.editText!!.hintColor
                )
            )
            editText?.compoundDrawableTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        design!!.editText!!.drawableTint
                    )
                )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setDesign(phoneText: PhoneText?) {
        try {
            if (design == null) return
            phoneText?.setDesign(design!!.editText!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    override fun setDesign(button: Button?) {
        try {
            if (design == null || design!!.btn == null) return
            button?.setBackgroundResource(design!!.btn!!.background)
            button?.setTextColor(ContextCompat.getColor(requireContext(), design!!.btn!!.textColor))
            button?.compoundDrawableTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    design!!.btn!!.tintDrawable
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getDefaultLinker(): HashMap<Int, View> {
        return HashMap()
    }

}