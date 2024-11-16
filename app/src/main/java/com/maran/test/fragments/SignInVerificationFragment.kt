package com.maran.test.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alimuzaffar.lib.pin.PinEntryEditText
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.maran.test.R


class SignInVerificationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in_verification, container, false)
        val nav = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val email = SignInVerificationFragmentArgs.fromBundle(requireArguments()).email
        val titleSignIn = view.findViewById<TextView>(R.id.title_sign_in)
        titleSignIn.text = getString(R.string.email_sent) + " " + email

        val pinEntry = view.findViewById(R.id.txt_pin_entry) as PinEntryEditText?


        val buttonProve = view.findViewById<AppCompatButton>(R.id.prove_button)
        buttonProve.setOnClickListener {
            if (pinEntry?.text != null && pinEntry.text!!.length == 4) {
                nav?.menu?.forEachIndexed { index, item ->
                    if (!item.isChecked) {
                        item.setEnabled(true)
                    }
                }

                val preferenceFile = this.activity?.getPreferences(Context.MODE_PRIVATE)
                if (preferenceFile != null) {
                    with(preferenceFile.edit()) {
                        putBoolean(getString(R.string.signed_key), true)
                        putString(
                            getString(R.string.date_signed_key),
                            java.time.LocalDateTime.now().toString()
                        )
                        apply()
                    }
                }

                findNavController().navigate(SignInVerificationFragmentDirections.actionSignInVerificationFragmentToMainFragment())
            } else {
                pinEntry?.error = "Введите код"
            }
        }

        return view
    }
}