package com.lassanit.testkotlin.activities

import android.content.Intent
import com.google.firebase.auth.AdditionalUserInfo
import com.google.firebase.auth.FirebaseAuth
import com.lassanit.authkit.options.AuthKitOptions
import com.lassanit.authkit.options.AuthKitOptions.CallBacks
import com.lassanit.extras.classes.App
import com.lassanit.extras.classes.Company
import com.lassanit.extras.classes.Utils.SignInMethod
import com.lassanit.firekit.R
import com.lassanit.kit.FireKitCompatActivity

class MainActivity : FireKitCompatActivity(){
    override fun setOptions(): AuthKitOptions {
        return AuthKitOptions.Builder(
                FirebaseAuth.getInstance(),
                App("OneStop", R.drawable.easy_logo)
            )
            .setSignInMethods(arrayOf(SignInMethod.PHONE))

            .setCallBacks(object: CallBacks{
                override fun onSignIn() {
                    startActivity(Intent(this@MainActivity, Test::class.java))
                }

                override fun onSignUp() {
                    startActivity(Intent(this@MainActivity, Test::class.java))
                }

                override fun onEmailVerification(sent: Boolean) {

                }

                override fun onFailure(exception: Exception?) {

                }

                override fun onPolicyClick() {

                }

                override fun onTermsAndConditionsClick() {

                }

            })
            .setCompany(Company("OneStop", R.drawable.easy_logo))
            .build()
    }
}