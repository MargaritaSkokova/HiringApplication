package com.maran.test.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.maran.test.R
import com.maran.test.database.entites.VacancySerializable
import com.maran.test.viewModels.FavouriteViewModel
import kotlinx.coroutines.launch

class FavouriteFragment : Fragment() {
    val viewModel: FavouriteViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            val vacancies = viewModel.getFavourite()
            val vacancyBlockLayout: LinearLayout = view.findViewById(R.id.vacancy_block_layout)
            var textForNumbers = if (vacancies.size % 100 / 10 == 1) {
                "й"
            } else if (vacancies.size % 10 == 1) {
                "я"
            } else if (vacancies.size % 10 == 2 || vacancies.size % 10 == 3 || vacancies.size % 10 == 4) {
                "и"
            } else {
                "й"
            }
            view.findViewById<TextView>(R.id.number_vacancies).text =
                vacancies.size.toString() + " ваканси" + textForNumbers

            for (vacancy in vacancies) {
                val newVacancy =
                    inflater.inflate(R.layout.vacancy_element, vacancyBlockLayout, false)
                val button = newVacancy.findViewById<ImageButton>(R.id.button_img)
                val isFavourite = viewModel.isFavourite(vacancy.id)
                if (isFavourite) {
                    button.isSelected = true
                }

                newVacancy.setOnClickListener {
                    findNavController().navigate(
                        FavouriteFragmentDirections.actionFavouriteFragmentToPageVacancyFragment(
                            VacancySerializable.fromVacancyEntity(
                                vacancy
                            ),
                            isFavourite
                        )
                    )
                }

                newVacancy.findViewById<AppCompatButton>(R.id.button_offer).setOnClickListener {
                    findNavController().navigate(
                        FavouriteFragmentDirections.actionFavouriteFragmentToPageVacancyFragment(
                            VacancySerializable.fromVacancyEntity(
                                vacancy
                            ),
                            isFavourite
                        )
                    )
                }

                button.setOnClickListener {
                    button.setSelected(!button.isSelected)
                    if (button.isSelected) {
                        viewModel.addFavourite(vacancy)
                    } else {
                        newVacancy.visibility = View.GONE

                        val numberVacancies = view.findViewById<TextView>(R.id.number_vacancies).text
                        val numbers = numberVacancies.substring(0, numberVacancies.indexOfFirst { it == ' ' }).toInt() - 1
                        textForNumbers = if (numbers % 100 / 10 == 1) {
                            "й"
                        } else if (numbers % 10 == 1) {
                            "я"
                        } else if (numbers % 10 == 2 || numbers % 10 == 3 || numbers % 10 == 4) {
                            "и"
                        } else {
                            "й"
                        }

                        view.findViewById<TextView>(R.id.number_vacancies).text =
                            numbers.toString() + " ваканси" + textForNumbers

                        viewModel.deleteFavourite(vacancy.id)
                    }
                }

                newVacancy.findViewById<TextView>(R.id.looking).text =
                    newVacancy.findViewById<TextView>(R.id.looking).text.toString() + " " + vacancy.lookingNumber.toString() + " человек"
                newVacancy.findViewById<TextView>(R.id.title).text = vacancy.title
                newVacancy.findViewById<TextView>(R.id.city).text = vacancy.town
                newVacancy.findViewById<TextView>(R.id.company).text = vacancy.company
                newVacancy.findViewById<TextView>(R.id.experience).text =
                    vacancy.experiencePreviewText
                newVacancy.findViewById<TextView>(R.id.date).text =
                    newVacancy.findViewById<TextView>(R.id.date).text.toString() + " " + vacancy.publishedDate

                newVacancy.tag = vacancy.id
                vacancyBlockLayout.addView(newVacancy)
            }
        }

        return view
    }
}