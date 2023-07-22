
# Google Authentication

- Before moving forward, Make sure you have follow the previous guide README.md

To use Google authentication in your Android app using Fire, follow these simple steps:

### Add Google Authentication to Your Firebase Project:

- In the Firebase Console, go to your project, and from the left sidebar, select "Authentication."
- Click on the "Sign-in method" tab, then enable the "Google" sign-in provider.

### Add Google Play Services to Your Project
In your project's build.gradle file, add the Google Play Services dependency:
```gradle
  implementation 'com.google.android.gms:play-services-auth:[latest_version]'

```
Replace [latest_version] with the latest version of Google Play Services.

### Add Google method in FireKit

- Update your AuthKitOptions and add SignInMethod.GOOGLE

```kotlin
override fun setOptions(): AuthKitOptions {
        return AuthKitOptions.Builder(
                FirebaseAuth.getInstance(),
                App("Demo", R.drawable.logo)
            )
            .setSignInMethods(arrayOf(SignInMethod.PHONE,SignInMethod.GOOGLE))
            .setCallBacks(this)
            .setCompany(Company("OneStop", R.drawable.logo))
            .build()
    }


```
- Provide google web client Id as-well (make sure it is 'web_client_id')

```kotlin
    override fun setOptions(): AuthKitOptions {
        return AuthKitOptions.Builder(
                FirebaseAuth.getInstance(),
                App("Demo", R.drawable.logo)
            )
            .setSignInMethods(arrayOf(SignInMethod.PHONE,SignInMethod.GOOGLE))
            .setGoogleWebClientId(getString(R.string.default_web_client_id))
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
            .setCompany(Company("OneStop", R.drawable.logo))
            .build()
    }


```

this is all no need for extra code, FireKit will handle from here
## Different Sign-In methods Guide

[Facebook](https://github.com/Wasi-Ibn-Adam/FireKit/FACEBOOK.md)
