package com.example.black_horse_onboarding.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentTransaction
import com.example.black_horse_onboarding.R

class MenuFragment : Fragment() {
    private lateinit var androidButton: AppCompatButton
    private lateinit var javaButton: AppCompatButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_menu_layout, container, false)

        androidButton = view.findViewById(R.id.android_button)
        javaButton = view.findViewById(R.id.java_button)

        transact(AndroidArticleFragment())

        androidButton.setOnClickListener {
            transact(AndroidArticleFragment())
        }
        javaButton.setOnClickListener {
            transact(JavaArticleFragment())
        }
        return view
    }

    private fun transact(fragment: Fragment) {
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.right_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}