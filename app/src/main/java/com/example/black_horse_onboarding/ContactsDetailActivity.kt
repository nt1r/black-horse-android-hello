package com.example.black_horse_onboarding

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contacts_detail_layout)

        val contactMan: ContactVM = ContactVM("", "Dwight", "12345678910")
        val contactWoman: ContactVM = ContactVM("", "Claudette", "10987654321")

        manImageView = findViewById(R.id.avatar_man)
        womanImageView = findViewById(R.id.avatar_woman)
        manNameTextView = findViewById(R.id.name_man_text_view)
        womanNameTextView = findViewById(R.id.name_woman_text_view)
        manPhoneTextView = findViewById(R.id.phone_man_text_view)
        womanPhoneTextView = findViewById(R.id.phone_woman_text_view)

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
    }
}