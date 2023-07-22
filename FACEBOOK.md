
# Facebook Authentication

Still in testing phase, it is better to not use it for now



- Before moving forward, Make sure you have follow the previous guide README.md

To use Facebook authentication in your Android app using Fire, follow these simple steps:

### Add Facebook Authentication to Your Firebase Project:

- In the Firebase Console, go to your project, and from the left sidebar, select "Authentication."
- Click on the "Sign-in method" tab, then enable the "Facebook" sign-in provider.

### Facebook Developer Console:
Before You Start You will need:
- Your Meta App ID
- Your Meta App Client Token
  to get these you must register your app with [facebook](https://developers.facebook.com/quickstarts/?platform=android)

### Add Facebook Services to Your Project
In your project's build.gradle file, add the Facebook Services dependency:
```gradle
  implementation 'com.facebook.android:facebook-android-sdk:[latest_version]'

```
Replace [latest_version] with the latest version of Facebook SDK.

### Update Your Manifest

Add string elements with the names 'facebook_app_id' and 'facebook_client_token', and set the values to your App ID and Client Token. For example, if your app ID is 1234 and your client token is 56789 your code looks like the following:

```res
<string name="facebook_app_id">1234</string>
<string name="facebook_client_token">56789</string>

```
Add meta-data elements to the application element for your app ID and client token:

```manifiest 
<application android:label="@string/app_name" ...>
    ...
    <meta-data android:name="com.facebook.sdk.ApplicationId" android:value="@string/facebook_app_id"/>
    <meta-data android:name="com.facebook.sdk.ClientToken" android:value="@string/facebook_client_token"/>
    ...
</application>

```


### Add Facebook method in FireKit

- Update your AuthKitOptions and add SignInMethod.FACEBOOK

```kotlin
override fun setOptions(): AuthKitOptions {
        return AuthKitOptions.Builder(
                FirebaseAuth.getInstance(),
                App("Demo", R.drawable.logo)
            )
            .setSignInMethods(arrayOf(SignInMethod.PHONE,SignInMethod.FACEBOOK))
            .setCallBacks(this)
            .setCompany(Company("OneStop", R.drawable.logo))
            .build()
    }


```

this is all no need for extra code, FireKit will handle from here
## Different Sign-In methods Guide


[Google](https://github.com/Wasi-Ibn-Adam/FireKit/GOOGLE.md)
