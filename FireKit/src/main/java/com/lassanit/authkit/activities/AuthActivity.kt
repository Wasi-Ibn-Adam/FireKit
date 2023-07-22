package com.lassanit.authkit.activities

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.AdditionalUserInfo
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.lassanit.extras.WaitingPopUp
import com.lassanit.extras.classes.Utils
import com.lassanit.extras.fragments.FragmentHandler
import com.lassanit.firekit.R
import com.lassanit.extras.classes.Anime
import com.lassanit.authkit.fragments.AuthFragmentHandler
import com.lassanit.authkit.fragments.PhoneFragment
import com.lassanit.authkit.fragments.ResetFragment
import com.lassanit.authkit.fragments.SignInFragment
import com.lassanit.authkit.fragments.SignUpFragment
import com.lassanit.authkit.fragments.SocialFragment
import com.lassanit.authkit.fragments.SplashFragment
import com.lassanit.authkit.interfaces.AuthActions
import com.lassanit.authkit.options.AuthKitOptions

abstract class AuthActivity : AppCompatActivity(), AuthActions {
    abstract fun setOptions(): AuthKitOptions

    private lateinit var options: AuthKitOptions
    private var oneTapClient: SignInClient? = null
    private var request: BeginSignInRequest? = null
    private var launcher: ActivityResultLauncher<IntentSenderRequest>? = null
    private lateinit var topNav: AuthFragmentHandler
    private lateinit var btmNav: AuthFragmentHandler
    private lateinit var popUp: WaitingPopUp

    private lateinit var parent: View
    private lateinit var topFrame: FrameLayout
    private lateinit var btmFrame: FrameLayout

    private var backPress = true
    private var first = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_authkit)
        options = setOptions()

        setActivityView()

        //loadFrags
        topNav = AuthFragmentHandler(this, supportFragmentManager, 0, R.id.top_frame)
        topNav.setApp(options.getApp())
            .setDesign(options.getDesign())
        topNav.setFragmentAnime(Anime())
        topNav.addFragmentChangeListener(object : FragmentHandler.FragmentChangeListener() {
            override fun onChange(name: String?, attached: Boolean) {
                try {
                    if (name == null)
                        return
                    when (name) {
                        SignInFragment::class.java.simpleName -> {
                            // btmFrame.visibility = VISIBLE
                            if (isPortrait() && !isSplit()) {
                                if (btmFrame.visibility == GONE) {
                                    // only when split screen was on (was in split mode but now full screen)
                                    btmFrame.visibility = VISIBLE
                                }
                                btmNav.show(SocialFragment::class.java)
                            }
                        }

                        ResetFragment::class.java.simpleName, PhoneFragment::class.java.simpleName -> {
                            btmNav.hide(SocialFragment::class.java)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        topNav.push(SplashFragment(options.getDelay()))
        btmNav = AuthFragmentHandler(this, supportFragmentManager, 1, R.id.btm_frame)
        btmNav.setApp(options.getApp())
            .setCompany(options.getCompany())
            .setDesign(options.getDesign())
        btmNav.setFragmentAnime(Anime(R.anim.show, R.anim.hide))
        setGoogle()
        popUp = WaitingPopUp(this)
    }

    private fun setGoogle() {
        try {
            oneTapClient = Identity.getSignInClient(this)
            request = BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(options.getWebClientId()!!).build()
            ).build()
            launcher =
                registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                    if (result.resultCode != RESULT_OK) {
                        return@registerForActivityResult
                    }
                    try {
                        val credential =
                            oneTapClient!!.getSignInCredentialFromIntent(result.data)
                        val authCredential =
                            GoogleAuthProvider.getCredential(credential.googleIdToken, null)
                        options.getAuth().signInWithCredential(authCredential)
                            .addOnSuccessListener { authResult ->
                                if (authResult.user != null) {
                                    authResult.additionalUserInfo?.let { onAdditionalInformation(it) }
                                    if (authResult.additionalUserInfo?.isNewUser == true)
                                        onSignUpComplete(Utils.SignInMethod.GOOGLE)
                                    else
                                        onSignInComplete()
                                }
                            }.addOnFailureListener { }
                    } catch (e: ApiException) {
                        when (e.statusCode) {
                            CommonStatusCodes.CANCELED -> log("One-tap dialog was closed.")
                            CommonStatusCodes.NETWORK_ERROR ->
                                log("One-tap encountered a network error.")

                            else -> log("Couldn't get credential from result." + e.localizedMessage)
                        }
                    } catch (e: java.lang.Exception) {
                        log("One-tap error: " + e.localizedMessage)
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setActivityView() {
        parent = findViewById(R.id.include)
        topFrame = findViewById(R.id.top_frame)
        btmFrame = findViewById(R.id.btm_frame)

        if (options.getDesign()?.activity != null) {
            parent.setBackgroundResource(options.getDesign()?.activity!!.background)
            parent.background.alpha = 124
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (backPress && !topNav.handleBackPress()) {
            finishAndRemoveTask()
        }
    }

    private fun isPortrait(): Boolean {
        return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    private fun isSplit(): Boolean {
        return isInMultiWindowMode
    }

    override fun onResume() {
        super.onResume()
        handleScreenView(
            isInMultiWindowMode,
            (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
        )
    }

    override fun log(any: Any) {
        Log.d(options.getTag(), any.toString())
    }

    override fun handleScreenView(split: Boolean, portrait: Boolean) {
        topNav.handleView(split, portrait)
        btmNav.handleView(split, portrait)

        btmFrame.visibility = if (portrait) (if (split) GONE else VISIBLE) else GONE
    }

    override fun onPolicyClick() {
        options.getCallBacks().onPolicyClick()
    }

    override fun onTermsAndConditionsClick() {
        options.getCallBacks().onTermsAndConditionsClick()
    }

    override fun onForgetClick() {
        topNav.push(ResetFragment(options.getAuth()), SignInFragment::class.java)
    }

    override fun onRegisterClick() {
        topNav.push(SignUpFragment(options.getAuth()), SignInFragment::class.java)
    }

    override fun loginWithProvider(provider: OAuthProvider) {
        try {
            options.getAuth().startActivityForSignInWithProvider(this, provider)
                .addOnCompleteListener { task ->
                    log(
                        "loginWithProvider " + provider.providerId + " startActivityForSignInWithProvider: COMPLETE"
                    )
                    if (task.isSuccessful && task.result.user != null) {
                        log(
                            "loginWithProvider " + provider.providerId + " startActivityForSignInWithProvider: SUCCESS"
                        )
                        task.result.additionalUserInfo?.let { onAdditionalInformation(it) }
                    }
                }
                .addOnFailureListener { e ->
                    if (e is FirebaseAuthUserCollisionException) {
                        Toast.makeText(
                            this,
                            "This email is Registered with different Sign-In Provider." +
                                    " Sign in using a provider associated with this email address",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        log(
                            "loginWithProvider " + provider.providerId + " startActivityForSignInWithProvider: ERROR: " + e.message
                        )
                        options.getAuth().signOut()
                        options.getCallBacks().onFailure(e)
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onFacebookClick() {

    }

    override fun onGoogleClick() {
        try {
            if (request != null)
                oneTapClient?.beginSignIn(request!!)
                    ?.addOnSuccessListener { beginSignInResult ->
                        try {
                            log("One Tap beginSignIn.")
                            val intentSenderRequest =
                                IntentSenderRequest.Builder(
                                    beginSignInResult.pendingIntent.intentSender
                                ).build()
                            launcher!!.launch(intentSenderRequest)
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                    ?.addOnFailureListener { e1 ->
                        log("One Tap beginSignIn: ERROR: " + e1.message)
                        if (e1.localizedMessage != null && e1.localizedMessage
                                .equals(
                                    "16: Caller has been temporarily blocked due to too many canceled sign-in prompts.",
                                    true
                                )
                        ) {
                            Toast.makeText(
                                this,
                                "Too many Wrong Attempts, try again later.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            e1.printStackTrace()
                        }
                    }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPhoneClick() {
        topNav.push(PhoneFragment(options.getAuth()), SignInFragment::class.java)
    }

    override fun onFragmentLoaded(str: String, view: View) {

    }

    override fun onFragmentComplete(str: String) {
        log(str)
        if (str == SplashFragment::class.java.simpleName) {
            if (options.getAuth().currentUser != null)
                onSignInComplete()
            else
                topNav.push(SignInFragment(options.getAuth(), true), SplashFragment::class.java)
        } else if (str == SignInFragment::class.java.simpleName && btmNav.size() == 0) {
            btmNav.push(SocialFragment(options.getMethods()), delayMillis = 500)
        }
    }

    override fun backPress(allow: Boolean) {
        backPress = allow
    }

    override fun loadingPopup(show: Boolean) {
        if (show)
            popUp.show()
        else
            popUp.hide()
    }

    override fun onResetLinkSent() {
        Toast.makeText(
            this,
            "Password Reset link is sent to your email.",
            Toast.LENGTH_SHORT
        ).show()
        topNav.pop()
    }

    override fun onSignInComplete() {
        options.getCallBacks().onSignIn()
    }

    override fun onSignUpComplete(signInMethod: Utils.SignInMethod) {
        topNav.pop()
        options.getCallBacks().onSignUp()
    }

    override fun onAdditionalInformation(info: AdditionalUserInfo) {
        try {
            first = info.isNewUser
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

