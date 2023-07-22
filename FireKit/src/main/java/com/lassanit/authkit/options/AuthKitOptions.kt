package com.lassanit.authkit.options

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.lassanit.extras.classes.Designs
import com.lassanit.extras.classes.Utils
import com.lassanit.extras.classes.App
import com.lassanit.extras.classes.Company

class AuthKitOptions {
    private lateinit var auth: FirebaseAuth
    private lateinit var app: App
    private var callBacks: CallBacks? = null
    private var emailVerify: Boolean = false
    private var splashDelay: Long = 1500
    private var wid: String? = null
    private var signInMethods: Array<Utils.SignInMethod>? = null
    private var design: Designs? = null
    private var tag: String = "FireAuthKit"
    private var company: Company? = null

    class Builder(auth: FirebaseAuth, app: App) {
        private val options: AuthKitOptions = AuthKitOptions()

        constructor(firebaseApp: FirebaseApp, app: App) : this(FirebaseAuth.getInstance(firebaseApp), app)

        init {
            options.auth = auth
            options.app = app
        }

        /**
         * @implNote set debug TAG for debugging Log.d(TAG,"msg");
         * <br></br>
         * default  value is 'FireAuthKit'
         */
        fun setCustomTag(tag: String): Builder {
            options.tag = tag
            return this
        }

        /**
         * @implNote set company name and logo which can be shown in main page right after splash time
         */
        fun setCompany(company: Company?): Builder {
            options.company = company
            return this
        }

        /**
         * @implNote set custom background resource designs for Buttons, EditText, Activity(only background view)
         */
        fun setCustomDesign(des: Designs?): Builder {
            options.design = des
            return this
        }

        /**
         * @implNote Email/Password is mandatory Method which will always be primary
         * in this function pass extra methods to SIGN-IN
         */
        fun setSignInMethods(signInMethods: Array<Utils.SignInMethod>): Builder {
            options.signInMethods = signInMethods
            return this
        }

        /**
         * @implSpec to setup GOOGLE authentication you need to register you project in
         * url="[ Google Cloud](https://console.cloud.google.com/)"
         * and from OAuth 2.0 Client IDs get *'WEB CLIENT ID'*
         * if you don't have any OAuth 2.0 Client IDs you can create from above given button 'Create Credentials' in that console
         * @implNote make sure it is web client id and not android client id
         */
        fun setGoogleWebClientId(wid: String): Builder {
            options.wid = wid
            return this
        }

        /**
         * @implNote show Splash Screen for how long
         * <br></br>
         * default time is 1500L = 1.5sec
         */
        fun setSplashDelay(millis: Long): Builder {
            options.splashDelay = millis
            return this
        }

        /**
         * @implSpec either allow app to verify email before going home page <br></br>
         * if true it will always stuck until email is verified by clicking link in the email sent
         */
        fun verifyEmail(ver: Boolean): Builder {
            options.emailVerify = ver
            return this
        }

        fun setCallBacks(callBacks: CallBacks): Builder {
            options.callBacks = callBacks
            return this
        }

        fun build(): AuthKitOptions {
            if (options.callBacks == null)
                throw Exception("AuthKitOptions.Callbacks are Mandatory")
            return options
        }
    }

    interface CallBacks {
        /**
         * @implNote when user log-in second time or simply and old user logged in
         * <br></br>
         * if profile info is set it will be a callback just to inform that user is logged-in and
         * now heading towards profile info setup
         * @see .onSignUp
         */
        fun onSignIn()

        /**
         * @implNote when a new user log-in
         * <br></br>
         * if profile info is set it will be a callback just to inform that user is logged-in and
         * now heading towards profile info setup
         * @see .onSignIn
         */
        fun onSignUp()

        /**
         * @param sent either verification email was sent successful or not
         * @implSpec perform action when email sent to user for Verification
         */
        fun onEmailVerification(sent: Boolean)

        /**
         * @implNote if any error occur while performing action by SSO module
         */
        fun onFailure(exception: Exception?)
        fun onPolicyClick()
        fun onTermsAndConditionsClick()
    }

    fun getAuth(): FirebaseAuth {
        return auth
    }

    fun getApp(): App {
        return app
    }

    fun getCompany(): Company? {
        return company
    }

    fun getDesign(): Designs? {
        return design
    }

    fun getMethods(): Array<Utils.SignInMethod>? {
        return signInMethods
    }

    fun getWebClientId(): String? {
        return wid
    }

    fun getDelay(): Long {
        return splashDelay
    }

    fun getTag(): String {
        return tag
    }

    fun getCallBacks(): CallBacks {
        return callBacks!!
    }


}