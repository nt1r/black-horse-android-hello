package com.example.black_horse_onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.black_horse_onboarding.view_model.ContactVM
import com.google.gson.Gson

class ContactsHomeActivity : AppCompatActivity() {
    private lateinit var detailButton: AppCompatButton
    private val REQUEST_CODE: Int = 10001

    private val TAG = "LIFECYCLE_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contacts_home_layout)
        Log.d(TAG, "Home Activity: onCreate")

        detailButton = findViewById(R.id.detail_button)

        detailButton.setOnClickListener(View.OnClickListener {
            val contactVM = ContactVM(
                "https://www.zooniverse.org/assets/simple-avatar.png",
                "Leqi Shen",
                "18706789189"
            )
            val detailIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, Gson().toJson(contactVM))
            }
            startActivityForResult(detailIntent, REQUEST_CODE)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == 10002) {
            val contactSelectedStr = data?.extras?.getString("contactJson")
            val contactSelected = Gson().fromJson(contactSelectedStr, ContactVM::class.java)
            Toast.makeText(this, "${contactSelected.name}: ${contactSelected.phone}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "Home Activity: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "Home Activity: onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "Home Activity: onRestart")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "Home Activity: onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "Home Activity: onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Home Activity: onDestroy")
    }
}