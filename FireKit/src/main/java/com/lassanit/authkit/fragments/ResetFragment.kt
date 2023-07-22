package com.lassanit.authkit.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.lassanit.extras.classes.Utils
import com.lassanit.firekit.R

class ResetFragment(private var auth: FirebaseAuth?) : AuthFragment(R.layout.fragment_auth_reset) {
    constructor() : this(null)

    private lateinit var txtEmail: EditText
    private lateinit var btn: Button
    private var e = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actions.onFragmentLoaded(ResetFragment::class.java.simpleName, view)
        txtEmail = view.findViewById<EditText>(R.id.authkit_id_editext_email)
        btn = view.findViewById<Button>(R.id.authkit_id_button_default)
        btn.setOnClickListener {
            Utils().hideSoftKeyboard(requireActivity())
            val email: String = txtEmail.text.toString()
            if (email.isEmpty()) {
                txtEmail.error = "Missing"
                return@setOnClickListener
            } else if (!email.matches(Patterns.EMAIL_ADDRESS.toRegex())) {
                txtEmail.error = "Invalid"
                return@setOnClickListener
            }
            action(email)
        }
    }

    override fun onPause() {
        super.onPause()
        e = txtEmail.text.toString()
    }

    override fun onResume() {
        super.onResume()
        txtEmail.setText(e)
    }

    private fun action(email: String) {
        actions.loadingPopup(true)
        auth?.sendPasswordResetEmail(email)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    actions.log("sendPasswordResetEmail: SUCCESS.")
                    actions.onResetLinkSent()
                }
                actions.loadingPopup(false)
            }
            ?.addOnFailureListener { e ->
                actions.log("sendPasswordResetEmail: ERROR: " + e.message)
                if (e.localizedMessage != null && e.localizedMessage
                        .equals(
                            "There is no user record corresponding to this identifier. The user may have been deleted.",
                            true
                        )
                ) txtEmail.error = "Email not registered."
                e.printStackTrace()
            }
    }

    override fun handleView(split: Boolean, portrait: Boolean, view: View?) {
        if (!portrait)
            super.handleView(split, portrait, view)
    }

    override fun getDefaultLinker(): HashMap<Int, View> {
        val map: HashMap<Int, View> = super.getDefaultLinker()
        map[R.string.tag_edittext_default] = txtEmail
        map[R.string.tag_button_default] = btn
        return map
    }

}