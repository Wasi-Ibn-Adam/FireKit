package com.lassanit.testkotlin.activities;

import static com.lassanit.extras.classes.Utils.*;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.lassanit.extras.classes.App;
import com.lassanit.extras.classes.Company;
import com.lassanit.authkit.options.AuthKitOptions;
import com.lassanit.extras.classes.Utils;
import com.lassanit.kit.FireKitCompatActivity;

public class Act extends FireKitCompatActivity implements AuthKitOptions.CallBacks {

    @NonNull
    @Override
    public AuthKitOptions setOptions() {
        return new AuthKitOptions.Builder(FirebaseAuth.getInstance(), new App("OneStop", com.lassanit.firekit.R.drawable.easy_logo))
                .setSignInMethods(new SignInMethod[]{SignInMethod.PHONE})
                .setCallBacks(this)
                .setCompany(new Company("OneStop", com.lassanit.firekit.R.drawable.easy_logo))
                .build();
    }

    @Override
    public void onSignIn() {
        startActivity(new Intent(this, Test.class));
    }

    @Override
    public void onSignUp() {
        startActivity(new Intent(this, Test.class));
    }

    @Override
    public void onEmailVerification(boolean sent) {

    }

    @Override
    public void onFailure(@Nullable Exception exception) {

    }

    @Override
    public void onAdditionalInformation(@NonNull AdditionalUserInfo info) {

    }
}
