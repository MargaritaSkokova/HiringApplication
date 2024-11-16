package com.maran.test.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.maran.test.R
import java.time.LocalDateTime


class SignInFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        val nav = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val preferenceFile = this.activity?.getPreferences(Context.MODE_PRIVATE)
        val isSigned = preferenceFile?.getBoolean(getString(R.string.signed_key), false)
        val dateSigned = preferenceFile?.getString(getString(R.string.date_signed_key), LocalDateTime.now().minusDays(1).toString())
        if (!isSigned!! || LocalDateTime.parse(dateSigned).plusDays(1).isBefore(LocalDateTime.now())) {
            nav?.menu?.forEachIndexed { index, item ->
                if (!item.isChecked) {
                    item.setEnabled(false)
                }
            }
        } else {
            nav?.menu?.forEachIndexed { index, item ->
                if (!item.isChecked) {
                    item.setEnabled(true)
                } else {
                    item.setEnabled(false)
                }
            }

            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToMainFragment())
        }

        val buttonSignIn = view.findViewById<AppCompatButton>(R.id.continue_button)
        val buttonPassword = view.findViewById<TextView>(R.id.password_login_button)

        val emailTextInput = view.findViewById<TextInputEditText>(R.id.email_input)
        val passwordTextInput = view.findViewById<TextInputEditText>(R.id.password_input)
        val email = view.findViewById<TextInputLayout>(R.id.email)
        val password = view.findViewById<TextInputLayout>(R.id.password)
        password.visibility = View.GONE

        emailTextInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                if (!input.isEmpty()) {
                    if (!input.matches(Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))) {
                        email.error = "Неправильный формат"
                    } else {
                        email.error = null
                    }
                }
            }
        })

        buttonPassword.setOnClickListener {
            password.visibility = View.VISIBLE
            buttonSignIn.text = "Войти"
            buttonPassword.visibility = View.GONE
        }

        buttonSignIn.setOnClickListener {
            if (password.visibility == View.VISIBLE) {
                if (emailTextInput.text != null && passwordTextInput.text != null) {
                    if (emailTextInput.text!!.isNotEmpty() && passwordTextInput.text!!.isNotEmpty()) {
                        if (emailTextInput.text!!.matches(Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))) {
                            nav?.menu?.forEachIndexed { index, item ->
                                if (!item.isChecked) {
                                    item.setEnabled(true)
                                }
                            }

                            with (preferenceFile.edit()) {
                                putBoolean(getString(R.string.signed_key), true)
                                putString(getString(R.string.date_signed_key), LocalDateTime.now().toString())
                                apply()
                            }
                            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToMainFragment())
                        }
                    }
                }

                if (emailTextInput.text != null && emailTextInput.text!!.isNotEmpty()) {
                    if (!emailTextInput.text!!.matches(Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))) {
                        email.error = "Неправильный формат"
                    }
                } else {
                    email.error = "Обязательное поле"
                }

                if (passwordTextInput.text == null || passwordTextInput.text!!.isEmpty()) {
                    password.error = "Обязательное поле"
                }
            } else {
                if (emailTextInput.text != null && emailTextInput.text!!.isNotEmpty()) {
                    if (!emailTextInput.text!!.matches(Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))) {
                        email.error = "Неправильный формат"
                    } else {
                        findNavController().navigate(
                            SignInFragmentDirections.actionSignInFragmentToSignInVerificationFragment(
                                emailTextInput.text.toString()
                            )
                        )
                    }
                } else {
                    email.error = "Обязательное поле"
                }
            }

        }

        return view
    }
}