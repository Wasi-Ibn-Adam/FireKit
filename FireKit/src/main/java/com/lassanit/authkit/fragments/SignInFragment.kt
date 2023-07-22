package com.lassanit.authkit.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.lassanit.extras.classes.Utils
import com.lassanit.firekit.R

class SignInFragment(var auth: FirebaseAuth?) : AuthFragment(R.layout.fragment_auth_signin) {
    constructor() : this(null)
    constructor(auth: FirebaseAuth?, firstTime: Boolean) : this(auth) {
        first = firstTime
    }

    private lateinit var txtEmail: EditText
    private lateinit var txtPass: EditText
    private lateinit var btn: AppCompatButton
    private var first: Boolean = false

    private var e = ""
    private var p = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actions.onFragmentLoaded(SignInFragment::class.java.simpleName, view)
        txtEmail = view.findViewById(R.id.authkit_id_editext_email)
        txtPass = view.findViewById(R.id.authkit_id_editext_password)
        btn = view.findViewById(R.id.authkit_id_button_default)

        btn.setOnClickListener {
            Utils().hideSoftKeyboard(requireActivity())
            val email: String = txtEmail.text.toString()
            val pass: String = txtPass.text.toString()
            if (email.isEmpty()) {
                txtEmail.error = "Missing"
                return@setOnClickListener
            } else if (!email.matches(Patterns.EMAIL_ADDRESS.toRegex())) {
                txtEmail.error = "Invalid"
                return@setOnClickListener
            }
            if (pass.isEmpty()) {
                txtPass.error = "Missing"
                return@setOnClickListener
            } else if (pass.length < 8) {
                txtPass.error = "Invalid"
                return@setOnClickListener
            }
            action(email, pass)
        }

        view.findViewById<View>(R.id.txt_forget).setOnClickListener { actions.onForgetClick() }
        view.findViewById<View>(R.id.txt_reg).setOnClickListener { actions.onRegisterClick() }

    }

    override fun canAnimate(view: View) {
        super.canAnimate(view)
        if (first) {
            first = false
            enterTransition = getTransition(
                requireContext(),
                android.R.transition.move,
                end = {
                    try {
                        actions.onFragmentComplete(SignInFragment::class.java.simpleName)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            )
            exitTransition = getTransition(
                requireContext(),
                android.R.transition.fade
            )
        }
    }

    override fun onPause() {
        super.onPause()
        e = txtEmail.text.toString()
        p = txtPass.text.toString()
    }

    override fun onResume() {
        super.onResume()
        txtEmail.setText(e)
        txtPass.setText(p)
    }

    private fun action(email: String, pass: String) {
        actions.loadingPopup(true)
        auth?.signInWithEmailAndPassword(email, pass)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user: FirebaseUser? = task.result.user
                if (user != null) {
                    if (task.result.additionalUserInfo != null) {
                        actions.onAdditionalInformation(task.result.additionalUserInfo!!)
                    }
                    actions.onSignInComplete()
                }
            }
            actions.loadingPopup(false)
        }?.addOnFailureListener { e ->
            if (e is FirebaseAuthInvalidCredentialsException) {
                actions.log("FirebaseAuthInvalidCredentialsException: " + e.errorCode)
                txtPass.error = "Wrong Password!"
            } else if (e is FirebaseAuthInvalidUserException) {
                actions.log("FirebaseAuthInvalidUserException: " + e.message)
                val errorCode = e.errorCode
                if (errorCode == "ERROR_USER_NOT_FOUND") {
                    txtEmail.error = "Email not Registered."
                } else if (errorCode == "ERROR_USER_DISABLED") {
                    txtEmail.error =
                        "User-Account has been disabled. For more information email us."
                }
            }
            e.printStackTrace()
        }
    }

    override fun getDefaultLinker(): HashMap<Int, View> {
        val map: HashMap<Int, View> = super.getDefaultLinker()
        map[R.string.tag_edittext_default] = txtEmail
        map[R.string.tag_edittext_extra] = txtPass
        map[R.string.tag_button_default] = btn
        return map
    }

}