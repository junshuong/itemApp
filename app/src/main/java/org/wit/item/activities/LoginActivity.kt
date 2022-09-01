package org.wit.item.activities


import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import org.wit.item.R

/**
 * A Login Form Example in Kotlin Android
 */
class LoginActivity : AppCompatActivity() {
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // get reference to all views
        var et_user_name = findViewById(R.id.et_user_name) as EditText
        var et_password = findViewById(R.id.et_password) as EditText
        var btn_reset = findViewById(R.id.btn_reset) as Button
        var btn_submit = findViewById(R.id.btn_submit) as Button

        btn_reset.setOnClickListener {
            // clearing user_name and password edit text views on reset button click
            et_user_name.setText("")
            et_password.setText("")
        }

        // set on-click listener
        btn_submit.setOnClickListener {
            val user_name = et_user_name.text;
            val password = et_password.text;
            Toast.makeText(this@LoginActivity, user_name, Toast.LENGTH_LONG).show()

            val launcherIntent = Intent(this, ItemListActivity::class.java)
            refreshIntentLauncher.launch(launcherIntent)
            // your code to validate the user_name and password combination
            // and verify the same

        }
    }
}