package com.lassanit.authkit.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.lassanit.extras.classes.Utils
import com.lassanit.extras.customviews.PhoneText
import com.lassanit.firekit.R
import com.raycoarana.codeinputview.CodeInputView
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

class PhoneFragment(private var auth: FirebaseAuth?) : AuthFragment(R.layout.fragment_auth_phone) {
    constructor() : this(null)

    private lateinit var txtPhone: PhoneText
    private lateinit var txtTimer: TextView
    private lateinit var txtResend: TextView
    private lateinit var txtCode: CodeInputView
    private lateinit var btnPhone: Button

    private var time = 60L
    private var timer: Timer? = null
    private var task: TimerTask? = null
    private var resendToken: ForceResendingToken? = null
    private var phoneNumber: String? = null
    private var verificationId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actions.onFragmentLoaded(PhoneFragment::class.java.simpleName, view)
        txtPhone = view.findViewById(R.id.authkit_id_phonetext)
        btnPhone = view.findViewById<Button>(R.id.authkit_id_button_default)
        txtCode = view.findViewById(R.id.txt_code)
        txtResend = view.findViewById(R.id.txt_resend)
        txtTimer = view.findViewById(R.id.txt_timer)

        txtCode.visibility = View.GONE
        txtResend.visibility = View.GONE
        txtTimer.visibility = View.GONE
        btnPhone.setOnClickListener {
            if (!txtPhone.isValid()) {
                txtPhone.editText.error = "Enter Valid Phone Number"
                return@setOnClickListener
            }
            val num: String = txtPhone.getPhoneNumber()
            action(num)
            Utils().hideSoftKeyboard(requireActivity())
        }
        txtCode.addOnCompleteListener { code: String ->
            onPhoneCodeEntered(code)
            txtTimer.visibility = View.GONE
            txtResend.visibility = View.GONE
        }
        txtResend.setOnClickListener { onPhoneCodeResent() }

        txtPhone.setDefaultCountry(requireContext())
    }

    private fun action(num: String) {
        actions.loadingPopup(true)
        actions.backPress(false)
        phoneNumber = num
        timer = Timer()
        task = object : TimerTask() {
            @SuppressLint("SetTextI18n")
            override fun run() {
                requireActivity().runOnUiThread { txtTimer.text = "$time sec" }
                time--
            }
        }
        val options = PhoneAuthOptions.newBuilder(auth!!)
            .setPhoneNumber(num) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // (optional) Activity for callback binding
            // If no activity is passed, reCAPTCHA verification can not be used.
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun onPhoneCodeResent() {
        val options = PhoneAuthOptions.newBuilder(auth!!)
            .setForceResendingToken(resendToken!!)
            .setPhoneNumber(phoneNumber!!) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // (optional) Activity for callback binding
            // If no activity is passed, reCAPTCHA verification can not be used.
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun onPhoneCodeEntered(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private var callbacks: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                actions.log("Phone onVerificationCompleted.")
                txtCode.code = phoneAuthCredential.smsCode
                signInWithPhoneAuthCredential(phoneAuthCredential)
                actions.backPress(true)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                actions.loadingPopup(false)
                actions.backPress(true)
                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        actions.log("Phone onVerificationFailed: FirebaseAuthInvalidCredentialsException.")
                        // Invalid request
                        txtPhone.editText.error = "Invalid Phone number."
                    }

                    is FirebaseTooManyRequestsException -> {
                        actions.log("Phone onVerificationFailed: FirebaseTooManyRequestsException.")
                        // The SMS quota for the project has been exceeded
                        txtPhone.editText.error =
                            "We have blocked all requests from this device due to unusual activity. Try again later."
                    }

                    is FirebaseAuthMissingActivityForRecaptchaException -> {
                        // reCAPTCHA verification attempted with null Activity
                        actions.log(
                            "Phone onVerificationFailed: FirebaseAuthMissingActivityForRecaptchaException."
                        )
                        txtPhone.editText.error = "Try again."
                    }

                    else -> {
                        actions.log("Phone onVerificationFailed: ERROR: " + e.message)
                    }
                }
            }

            override fun onCodeSent(
                verifyId: String,
                token: ForceResendingToken
            ) {
                actions.log("Phone onCodeSent.")
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                actions.loadingPopup(false)
                // by combining the code with a verification ID.
                // Save verification ID and resending token so we can use them later
                verificationId = verifyId
                resendToken = token
                txtPhone.visibility = View.GONE
                btnPhone.visibility = View.GONE
                txtCode.visibility = View.VISIBLE
                txtTimer.visibility = View.VISIBLE
                txtCode.requestFocus()
                txtCode.setEditable(true)
                timer!!.schedule(task, 0, 1000)
            }

            override fun onCodeAutoRetrievalTimeOut(s: String) {
                super.onCodeAutoRetrievalTimeOut(s)
                actions.log("Phone onCodeAutoRetrievalTimeOut.")
                txtTimer.visibility = View.GONE
                txtResend.visibility = View.VISIBLE
                actions.backPress(true)
            }
        }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth?.signInWithCredential(credential)
            ?.addOnSuccessListener { authResult ->
                actions.log("Phone signInWithCredential: SUCCESS.")
                timer!!.cancel()
                val user: FirebaseUser? = authResult.user
                if (user != null) {
                    actions.log("Phone signInWithCredential: SUCCESS USER.")
                    authResult.additionalUserInfo?.let { actions.onAdditionalInformation(it) }
                    if (authResult.additionalUserInfo?.isNewUser == true) {
                        actions.onSignUpComplete(Utils.SignInMethod.PHONE)
                    } else actions.onSignInComplete()
                } else actions.log("Phone signInWithCredential: SUCCESS NULL.")

            }
            ?.addOnFailureListener { e ->
                if (e is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                    actions.log(
                        "Phone signInWithCredential: FirebaseAuthInvalidCredentialsException."
                    )
                    txtCode.error = "Invalid Code"
                    txtCode.setEditable(true)
                    txtCode.visibility = View.VISIBLE
                    txtTimer.visibility = View.VISIBLE
                } else {
                    actions.log("Phone signInWithCredential: ERROR.")
                }
            }
    }

    override fun getDefaultLinker(): HashMap<Int, View> {
        val map: HashMap<Int, View> = super.getDefaultLinker()
        try {
            map[R.string.tag_button_extra] = btnPhone
            map[R.string.tag_edittext_default] = txtPhone
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return map
    }



}