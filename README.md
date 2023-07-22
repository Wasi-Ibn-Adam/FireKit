
# FireKit

Android Firebase Authentication Library

## Key Features

- Easy integration with Firebase Authentication and multiple sign-in providers.
- Customizable UI elements, such as backgrounds, buttons, and EditText.
- Splash screen customization and adjustable duration.
- Support for both portrait and landscape orientations and split-screen functionality.
- Developer-friendly with customizable debug tags and helpful callbacks.
- Built-in logic for app privacy policy and terms and conditions.
- Light/dark mode toggle

## Getting Started

### Set Up Firebase Project:

- Go to the Firebase Console, sign in with your Google account, and create a new Firebase project.
- Follow the instructions to add your Android app to the - Firebase project, providing the package name of your Android app.
- Download the google-services.json file and place it in your app module's root directory (app/).

### Add Sign-In Providers to Your Firebase Project:

- In the Firebase Console, go to your project, and from the left sidebar, select "Authentication."
- Click on the "Sign-in method" tab, then enable the desired sign-in provider
- Email/password method is mandatory as the basic method for Authentication in FireKit is Email/Password
#### Note:  Google, Facebook and Phone require some extra work after integrating this project you can either google respective .md file or click on link below

### Dependencies

To integrate FireKit into your Android app, Add the FireKit dependency to your project.

```gradle
  implementation 'com.example.firekit:firekit:1.0.0'
```
Also require a few more dependencies

```gradle
  implementation 'androidx.appcompat:appcompat:1.6.1'
  implementation 'com.google.firebase:firebase-auth-ktx:22.0.0'
```

### Manifest File

Add a uses-permission element to the manifest for internet access
```
    <uses-permission android:name="android.permission.INTERNET"/>
```


### Implement FireKit:

In your app's main activity, This should be your first activity/launcher as it has integrated splash screen

```kotlin
class MainActivity : FireKitCompatActivity() {
    // Your activity code here
}
```

Implement the setOptions() function to configure FireKit with your app's settings and preferences:
```kotlin
override fun setOptions(): AuthKitOptions {
        return AuthKitOptions.Builder(
                FirebaseAuth.getInstance(),
                App("Demo", R.drawable.logo)
            )
            .setSignInMethods(arrayOf(SignInMethod.PHONE))
            .setCallBacks(object: CallBacks{
                override fun onSignIn() {

                }

                override fun onSignUp() {
                    
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

# FireKit

Android Firebase Authentication Library

## Key Features

- Easy integration with Firebase Authentication and multiple sign-in providers.
- Customizable UI elements, such as backgrounds, buttons, and EditText.
- Splash screen customization and adjustable duration.
- Support for both portrait and landscape orientations and split-screen functionality.
- Developer-friendly with customizable debug tags and helpful callbacks.
- Built-in logic for app privacy policy and terms and conditions.
- Light/dark mode toggle

## Getting Started

### Set Up Firebase Project:

- Go to the Firebase Console, sign in with your Google account, and create a new Firebase project.
- Follow the instructions to add your Android app to the - Firebase project, providing the package name of your Android app.
- Download the google-services.json file and place it in your app module's root directory (app/).

### Add Sign-In Providers to Your Firebase Project:

- In the Firebase Console, go to your project, and from the left sidebar, select "Authentication."
- Click on the "Sign-in method" tab, then enable the desired sign-in provider
- Email/password method is mandatory as the basic method for Authentication in FireKit is Email/Password
#### Note:  Google, Facebook and Phone require some extra work after integrating this project you can either google respective .md file or click on link below

### Dependencies

To integrate FireKit into your Android app, Add the FireKit dependency to your project.

```gradle
  implementation 'com.example.firekit:firekit:1.0.0'
```
Also require a few more dependencies

```gradle
  implementation 'androidx.appcompat:appcompat:1.6.1'
  implementation 'com.google.firebase:firebase-auth-ktx:22.0.0'
```

### Implement FireKit:

In your app's main activity, This should be your first activity/launcher as it has integrated splash screen

```kotlin
class MainActivity : FireKitCompatActivity() {
    // Your activity code here
}
```

Implement the setOptions() function to configure FireKit with your app's settings and preferences:
```kotlin
override fun setOptions(): AuthKitOptions {
        return AuthKitOptions.Builder(
                FirebaseAuth.getInstance(),
                App("Demo", R.drawable.logo)
            )
            .setSignInMethods(arrayOf(SignInMethod.PHONE))
            .setCallBacks(object: CallBacks{
                override fun onSignIn() {

                }

                override fun onSignUp() {
                    
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
## Different Sign-In methods Guide


[Google](https://github.com/Wasi-Ibn-Adam/FireKit/GOOGLE.md)

[Facebook](https://github.com/Wasi-Ibn-Adam/FireKit/FACEBOOK.md)

