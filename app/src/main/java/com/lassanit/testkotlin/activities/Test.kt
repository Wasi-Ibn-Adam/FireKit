package com.lassanit.testkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.lassanit.testkotlin.R

class Test : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activty_test)

        findViewById<Button>(R.id.button).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(
                Intent(this, Act::class.java)
                    .setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                    )
            )
        }

    }


}