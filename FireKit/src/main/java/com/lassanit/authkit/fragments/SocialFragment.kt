package com.lassanit.authkit.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.firebase.auth.OAuthProvider
import com.lassanit.extras.classes.Utils
import com.lassanit.extras.classes.Utils.SignInMethod.*
import com.lassanit.firekit.R

class SocialFragment(private var signInMethods: Array<Utils.SignInMethod>?) :
    AuthFragment(R.layout.fragment_auth_social) {
    constructor() : this(null)

    private lateinit var methodLayout: LinearLayout

    var enable: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actions.onFragmentLoaded(SocialFragment::class.java.simpleName, view)
        setupMethods(view)
    }

    override fun canAnimate(view: View) {
        super.canAnimate(view)
        view.startAnimation(Utils().getMoveAnimation(view.y))
    }

    private fun setupMethods(view: View) {
        view.findViewById<View>(R.id.layout_alter).visibility =
            if (signInMethods == null || signInMethods!!.isEmpty()) View.INVISIBLE else View.VISIBLE

        methodLayout = view.findViewById(R.id.layout_methods)

        if (signInMethods != null) for (method in signInMethods!!) {
            methodLayout.addView(getMethodView(method))
        }
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    private fun getMethodView(signInMethod: Utils.SignInMethod): View? {
        val imgView = ImageView(requireContext())
        imgView.maxWidth = 30
        imgView.maxHeight = 30
        val dp10 = dpToPx(requireContext(), 7)
        val layoutParams = LinearLayout.LayoutParams(dp10 * 4, dp10 * 4)
        layoutParams.setMargins(dp10, dp10, dp10, dp10)
        imgView.layoutParams = layoutParams

        when (signInMethod) {
            PHONE -> {
                imgView.setImageResource(R.drawable.phone)
                imgView.setOnClickListener {
                    if (!enable) return@setOnClickListener
                    actions.onPhoneClick()
                }
            }

            GITHUB -> {
                imgView.setImageResource(R.drawable.git)
                imgView.setOnClickListener {
                    if (!enable) return@setOnClickListener
                    val provider = OAuthProvider.newBuilder("github.com")
                    val scopes: List<String> = object : ArrayList<String>() {
                        init {
                            add("user:email")
                        }
                    }
                    provider.scopes = scopes
                    actions.loginWithProvider(provider.build())
                }
            }

            MICROSOFT -> {
                imgView.setImageResource(R.drawable.microsoft)
                imgView.setOnClickListener {
                    if (!enable) return@setOnClickListener
                    actions.loginWithProvider(
                        OAuthProvider.newBuilder("microsoft.com").build()
                    )
                }
            }

            TWITTER -> {
                imgView.setImageResource(R.drawable.twitter)
                imgView.setOnClickListener {
                    if (!enable) return@setOnClickListener

                    actions.loginWithProvider(
                        OAuthProvider.newBuilder("twitter.com").build()
                    )
                }
            }

            YAHOO -> {
                imgView.setImageResource(R.drawable.yahoo)
                imgView.setOnClickListener {
                    if (!enable) return@setOnClickListener
                    actions.loginWithProvider(
                        OAuthProvider.newBuilder("yahoo.com").build()
                    )
                }
            }

            FACEBOOK -> {
                imgView.setImageResource(R.drawable.fb)
                imgView.setOnClickListener {
                    if (!enable) return@setOnClickListener
                    actions.onFacebookClick()
                }
            }

            GOOGLE -> {
                imgView.setImageResource(R.drawable.google)
                imgView.setOnClickListener {
                    if (!enable) return@setOnClickListener
                    actions.onGoogleClick()
                }
            }

            else -> {
                return null
            }
        }
        return imgView
    }

}