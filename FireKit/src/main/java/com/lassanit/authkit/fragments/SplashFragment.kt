package com.lassanit.authkit.fragments

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.lassanit.extras.classes.Utils
import com.lassanit.firekit.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SplashFragment(private var delayMillis: Long) :
    AuthFragment(R.layout.fragment_auth_splash) {
    constructor() : this(1500)

    private val policyTermsConditions = "policy_and terms"
    private lateinit var consent: View
    private lateinit var name: View
    private lateinit var btn: Button
    private lateinit var policy: TextView
    private lateinit var terms: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            actions.onFragmentLoaded(this.javaClass.name, view)

            name = view.findViewById<View>(R.id.authkit_id_app_name)
            consent = view.findViewById<View>(R.id.layout_user_consent)
            policy = view.findViewById<TextView>(R.id.txt_policy)
            terms = view.findViewById<TextView>(R.id.txt_terms)
            btn = view.findViewById<Button>(R.id.authkit_id_button_default)


            consent.visibility = View.INVISIBLE

            policy.setOnClickListener { if (consent.visibility == VISIBLE) actions.onPolicyClick() }
            terms.setOnClickListener { if (consent.visibility == VISIBLE) actions.onTermsAndConditionsClick() }
            btn.setOnClickListener { if (consent.visibility == VISIBLE) userAgreed() }

            CoroutineScope(Dispatchers.Default).launch {
                delay(delayMillis)

                withContext(Dispatchers.Main) {
                    checkUserConsent()
                }
            }

            // handleView(handleSplit, handleOrientation, view)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkUserConsent() {
        try {
            val context = requireContext()
            if (context.getSharedPreferences(context.packageName, AppCompatActivity.MODE_PRIVATE)
                    .getBoolean(
                        policyTermsConditions,
                        false
                    )
            ) workDone()
            else requireUserConsent()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun canAnimate(view: View) {
        super.canAnimate(view)
        view.startAnimation(Utils().getScaleAnimation())
    }

    private fun requireUserConsent() {
        try {
            consent.visibility = View.VISIBLE
            consent.startAnimation(Utils().getScaleAnimation(true, 300))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun userAgreed() {
        try {
            val context = requireContext()
            context.getSharedPreferences(context.packageName, AppCompatActivity.MODE_PRIVATE).edit()
                .putBoolean(policyTermsConditions, true).apply()
            workDone()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun workDone() {
        try {
            actions.onFragmentComplete(SplashFragment::class.java.simpleName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getDefaultLinker(): HashMap<Int, View> {
        val map: HashMap<Int, View> = super.getDefaultLinker()
        try {
            map[R.string.tag_button_default] = btn
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return map
    }
}