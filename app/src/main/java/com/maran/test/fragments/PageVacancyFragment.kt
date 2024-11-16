package com.maran.test.fragments

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.maran.test.R
import com.maran.test.database.entites.Vacancy
import com.maran.test.viewModels.PageVacancyViewModel


class PageVacancyFragment : Fragment() {
    val viewModel: PageVacancyViewModel by viewModels()
    lateinit var title: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
       
    ): View? {
        val view = inflater.inflate(R.layout.fragment_page_vacancy, container, false)
        val button = view.findViewById<Button>(R.id.reply_button)

        val vacancy = PageVacancyFragmentArgs.fromBundle(requireArguments()).vacancy
        title = vacancy.title
        val isFavourite = PageVacancyFragmentArgs.fromBundle(requireArguments()).isFavourite

        val buttonFavourite = view.findViewById<ImageButton>(R.id.favourite_button)
        if (isFavourite) {
            buttonFavourite.isSelected = true
        }

        buttonFavourite.setOnClickListener {
            buttonFavourite.setSelected(!buttonFavourite.isSelected)
            if (buttonFavourite.isSelected) {
                viewModel.addFavourite(Vacancy.fromVacancySerializable(vacancy))
            } else {
                viewModel.deleteFavourite(vacancy.id)
            }
        }

        val buttonBack = view.findViewById<ImageButton>(R.id.back_button)
        buttonBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        view.findViewById<TextView>(R.id.title_vacancy).text = vacancy.title
        view.findViewById<TextView>(R.id.vague).text =
            vacancy.salaryFull ?: vacancy.salaryShort ?: "Не указана"
        view.findViewById<TextView>(R.id.experience).text = "Требуемый опыт: " + vacancy.experienceText
        view.findViewById<TextView>(R.id.workday).text = vacancy.schedules.joinToString(", " )
        view.findViewById<TextView>(R.id.applied_number).text = "${vacancy.appliedNumber ?: 0} человек уже откликнулись"
        view.findViewById<TextView>(R.id.looking_number).text = "${vacancy.lookingNumber} человек уже откликнулись"
        view.findViewById<TextView>(R.id.address).text = vacancy.town + ", " + vacancy.street + ", " + vacancy.house
        if (vacancy.description != null) {
            view.findViewById<TextView>(R.id.description).text = vacancy.description
        } else {
            view.findViewById<TextView>(R.id.description).visibility = View.INVISIBLE
        }
        view.findViewById<TextView>(R.id.tasks).text = vacancy.responsibilities

        val descriptionContainer = view.findViewById<LinearLayout>(R.id.description_container)
        for (question in vacancy.questions) {
            val newQuestion = inflater.inflate(R.layout.button_template, descriptionContainer, false)
            newQuestion.findViewById<AppCompatButton>(R.id.template).text = question
            descriptionContainer.addView(newQuestion)
        }


        button.setOnClickListener {
            showBottomDialog()
        }
        return view
    }

    private fun showBottomDialog() {
        val dialog = Dialog(this.requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottoomsheet_layout)
        dialog.findViewById<TextInputLayout>(R.id.cover_letter).visibility = View.INVISIBLE
        dialog.findViewById<TextView>(R.id.button_cover_letter).setOnClickListener {
            dialog.findViewById<TextInputLayout>(R.id.cover_letter).visibility = View.VISIBLE
            dialog.findViewById<TextView>(R.id.button_cover_letter).visibility = View.INVISIBLE
        }

        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.findViewById<AppCompatButton>(R.id.button_reply).setOnClickListener {
            dialog.hide()
            val snackbar = Snackbar.make(this.requireContext(),
                requireView(),
                "Вы откликнулись!",
                BaseTransientBottomBar.LENGTH_SHORT)
            snackbar.setBackgroundTint(ContextCompat.getColor(this.requireContext(), R.color.grey1))
            snackbar.setTextColor(ContextCompat.getColor(this.requireContext(), R.color.white))
            val params = snackbar.view.layoutParams as (FrameLayout.LayoutParams)
            params.gravity = Gravity.CENTER
            params.width = FrameLayout.LayoutParams.WRAP_CONTENT
            snackbar.view.layoutParams = params
            val textView = snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView

            snackbar.show()
        }
        
        dialog.findViewById<TextView>(R.id.vacancy_name).text = title


        dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.shadows)))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
    }
}