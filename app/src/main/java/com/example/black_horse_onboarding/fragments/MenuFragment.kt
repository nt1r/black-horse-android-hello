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
    private lateinit var androidArticleFragment: AndroidArticleFragment
    private lateinit var javaArticleFragment: JavaArticleFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_menu_layout, container, false)

        androidButton = view.findViewById(R.id.android_button)
        javaButton = view.findViewById(R.id.java_button)

        androidArticleFragment = AndroidArticleFragment()
        javaArticleFragment = JavaArticleFragment()

        transact(androidArticleFragment)

        androidButton.setOnClickListener {
            transact(androidArticleFragment)
        }
        javaButton.setOnClickListener {
            transact(javaArticleFragment)
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