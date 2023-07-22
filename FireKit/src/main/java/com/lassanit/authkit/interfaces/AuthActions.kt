package com.lassanit.authkit.interfaces

import com.lassanit.extras.interfaces.AppCallbacks

interface AuthActions : AppCallbacks, AuthBaseCallbacks {
    fun onPolicyClick()
    fun onTermsAndConditionsClick()
    fun onForgetClick()
    fun onRegisterClick()
}