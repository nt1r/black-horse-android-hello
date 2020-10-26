package com.example.black_horse_onboarding

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import com.example.black_horse_onboarding.view_model.ContactVM
import com.google.gson.Gson
import java.net.URI

class ContactsDetailActivity : AppCompatActivity() {
    private lateinit var manImageView: AppCompatImageView
    private lateinit var womanImageView: AppCompatImageView
    private lateinit var manNameTextView: TextView
    private lateinit var womanNameTextView: TextView
    private lateinit var manPhoneTextView: TextView
    private lateinit var womanPhoneTextView: TextView
    private lateinit var dialogButton: AppCompatButton

    private val TAG = "LIFECYCLE_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contacts_detail_layout)

        Log.d(TAG, "Detail Activity: onCreate")

        val contactMan: ContactVM = ContactVM("", "Dwight", "12345678910")
        val contactWoman: ContactVM = ContactVM("", "Claudette", "10987654321")

        manImageView = findViewById(R.id.avatar_man)
        womanImageView = findViewById(R.id.avatar_woman)
        manNameTextView = findViewById(R.id.name_man_text_view)
        womanNameTextView = findViewById(R.id.name_woman_text_view)
        manPhoneTextView = findViewById(R.id.phone_man_text_view)
        womanPhoneTextView = findViewById(R.id.phone_woman_text_view)
        dialogButton = findViewById(R.id.dialog_button)

        val contactStr: String? = intent.extras?.getString(Intent.EXTRA_TEXT)
        val contactReceived: ContactVM = Gson().fromJson(contactStr, ContactVM::class.java)

        manNameTextView.text = contactMan.name
        manPhoneTextView.text = contactMan.phone
        womanNameTextView.text = contactWoman.name
        womanPhoneTextView.text = contactWoman.phone

        manImageView.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.putExtra("contactJson", Gson().toJson(contactMan))
            setResult(10002, intent)
            finish()
        })
        womanImageView.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.putExtra("contactJson", Gson().toJson(contactWoman))
            setResult(10002, intent)
            finish()
        })

        dialogButton.setOnClickListener(View.OnClickListener {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Message Dialog")
                .setPositiveButton(R.string.confirm,
                    DialogInterface.OnClickListener { dialog, id ->

                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            builder.create()
            builder.show()
        })
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "Detail Activity: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "Detail Activity: onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "Detail Activity: onRestart")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "Detail Activity: onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "Detail Activity: onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Detail Activity: onDestroy")
    }
}