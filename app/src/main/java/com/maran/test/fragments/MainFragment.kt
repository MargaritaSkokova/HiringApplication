package com.maran.test.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.maran.test.R
import com.maran.test.database.VacancyRepository
import com.maran.test.database.entites.Vacancy
import com.maran.test.database.entites.VacancySerializable
import com.maran.test.viewModels.MainViewModel
import kotlinx.coroutines.launch
import java.util.UUID

class MainFragment : Fragment() {
    private val viewModel: MainViewModel by viewModels()
    private var isInflated: Boolean = false
    private val db: VacancyRepository = VacancyRepository.get()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        isInflated = false

        val callback = requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.isVacancyAllShown) {
                        showVacancyShort(view)
                        viewModel.isVacancyAllShown = false
                        if (!isInflated) {
                            viewLifecycleOwner.lifecycleScope.launch {
                                getRecommendations(view, inflater)
                                getVacancies(view, inflater, false)
                            }

                            isInflated = true
                        } else {
                            iterateVacancyBlock(view.findViewById(R.id.vacancy_block_layout_2))
                        }
                    } else {
                        isEnabled = false
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                }
            })

        if (!viewModel.isVacancyAllShown) {
            showVacancyShort(view)
            viewLifecycleOwner.lifecycleScope.launch {
                getRecommendations(view, inflater)
                getVacancies(view, inflater, false)
            }
        } else {
            showVacancyAll(view)
            viewLifecycleOwner.lifecycleScope.launch {
                getVacancies(view, inflater, true)
            }
        }


        view.findViewById<AppCompatButton>(R.id.more_vacancies_button).setOnClickListener {
            viewModel.isVacancyAllShown = true

            showVacancyAll(view)
            if (!isInflated) {
                viewLifecycleOwner.lifecycleScope.launch {
                    getVacancies(view, inflater, true)
                }

                isInflated = true
            } else {
                iterateVacancyBlock(view.findViewById(R.id.vacancy_block_layout))
            }
        }

        return view
    }

    fun iterateVacancyBlock(vacancyBlockLayout: LinearLayout) {
        for (vacancy in vacancyBlockLayout.children) {
            viewLifecycleOwner.lifecycleScope.launch {
                val isFavourite = viewModel.isFavourite(UUID.fromString(vacancy.tag.toString()))
                if (isFavourite) {
                    vacancy.findViewById<AppCompatImageButton>(R.id.button_img).isSelected = true
                }
            }
        }
    }

    suspend fun getRecommendations(view: View, inflater: LayoutInflater) {
        val file = requireContext().assets.open("offers.json")
        val recommendationBlockLayout =
            view.findViewById<LinearLayout>(R.id.recommendation_block_layout)
        val offers = viewModel.getOffers(file)
        for (offer in offers) {
            val newOffer = inflater.inflate(
                R.layout.recommendation_element,
                recommendationBlockLayout,
                false
            )
            newOffer.findViewById<TextView>(R.id.title).text = offer.title
            if (offer.button != null) {
                newOffer.findViewById<TextView>(R.id.button_text).text = offer.button.text
            } else {
                newOffer.findViewById<TextView>(R.id.button_text).visibility = View.GONE
            }

            if (offer.id != null) {
                val imageName: String
                val backgroundName: String
                if (offer.id == "near_vacancies") {
                    imageName = "map_pin"
                    backgroundName = "blue_button_background"
                } else if (offer.id == "level_up_resume") {
                    imageName = "star"
                    backgroundName = "green_button_background"
                } else {
                    imageName = "vacancy"
                    backgroundName = "green_button_background"
                }
                newOffer.findViewById<ImageView>(R.id.icon).setImageResource(
                    resources.getIdentifier(
                        imageName,
                        "drawable",
                        requireContext().packageName
                    )
                )
                newOffer.findViewById<ImageView>(R.id.icon).setBackgroundResource(
                    resources.getIdentifier(
                        backgroundName,
                        "drawable",
                        requireContext().packageName
                    )
                )
            } else {
                newOffer.findViewById<ImageView>(R.id.icon).visibility = View.INVISIBLE
            }

            recommendationBlockLayout.addView(
                newOffer
            )
        }
    }

    suspend fun getVacancies(view: View, inflater: LayoutInflater, isFull: Boolean) {
        val file = requireContext().assets.open("vacancies.json")
        val vacancies = viewModel.getVacancies(file)
        val vacancyBlockLayout: LinearLayout = if (isFull) {
            view.findViewById(R.id.vacancy_block_layout_2)
        } else {
            view.findViewById(R.id.vacancy_block_layout)
        }

        val number = if (!isFull) {
            3
        } else {
            vacancies.size
        }

        for (i in 0..<number) {
            val vacancy = vacancies[i]
            val newVacancy =
                inflater.inflate(R.layout.vacancy_element, vacancyBlockLayout, false)
            val button = newVacancy.findViewById<ImageButton>(R.id.button_img)
            val isFavourite = viewModel.isFavourite(vacancy.id)
            if (isFavourite) {
                button.isSelected = true
            }
            button.setOnClickListener {
                button.setSelected(!button.isSelected)
                if (button.isSelected) {
                    viewModel.addFavourite(Vacancy.fromNetworkVacancy(vacancy))
                } else {
                    viewModel.deleteFavourite(vacancy.id)
                }
            }

            newVacancy.setOnClickListener {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToPageVacancyFragment(
                        VacancySerializable.fromVacancyNetwork(
                            vacancy
                        ),
                        isFavourite
                    )
                )
            }

            newVacancy.findViewById<AppCompatButton>(R.id.button_offer).setOnClickListener {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToPageVacancyFragment(
                        VacancySerializable.fromVacancyNetwork(
                            vacancy
                        ),
                        isFavourite
                    )
                )
            }

            newVacancy.findViewById<TextView>(R.id.looking).text =
                newVacancy.findViewById<TextView>(R.id.looking).text.toString() + " " + vacancy.lookingNumber.toString() + " человек"
            newVacancy.findViewById<TextView>(R.id.title).text = vacancy.title
            newVacancy.findViewById<TextView>(R.id.city).text = vacancy.address.town
            newVacancy.findViewById<TextView>(R.id.company).text = vacancy.company
            newVacancy.findViewById<TextView>(R.id.experience).text = vacancy.experience.previewText
            newVacancy.findViewById<TextView>(R.id.date).text =
                newVacancy.findViewById<TextView>(R.id.date).text.toString() + " " + vacancy.publishedDate

            newVacancy.tag = vacancy.id
            vacancyBlockLayout.addView(newVacancy)
        }

        view.findViewById<AppCompatButton>(R.id.more_vacancies_button).text =
            "Еще " + (vacancies.size - 3).toString() + " вакансии"
        view.findViewById<TextView>(R.id.number_vacancies).text =
            vacancies.size.toString() + " вакансий"
    }

    fun showVacancyShort(view: View) {
        view.findViewById<HorizontalScrollView>(R.id.recommendation_block).visibility =
            View.VISIBLE
        view.findViewById<TextView>(R.id.vacancy_title).visibility = View.VISIBLE
        view.findViewById<ScrollView>(R.id.scrollView).visibility = View.VISIBLE
        view.findViewById<AppCompatButton>(R.id.more_vacancies_button).visibility =
            View.VISIBLE

        view.findViewById<ScrollView>(R.id.scrollView2).visibility = View.GONE
        view.findViewById<ImageView>(R.id.arrow_image).visibility = View.GONE
        view.findViewById<TextView>(R.id.textView2).visibility = View.GONE
        view.findViewById<TextView>(R.id.number_vacancies).visibility = View.GONE
        view.findViewById<LinearLayout>(R.id.map).visibility = View.GONE
    }

    fun showVacancyAll(view: View) {
        view.findViewById<HorizontalScrollView>(R.id.recommendation_block).visibility = View.GONE
        view.findViewById<TextView>(R.id.vacancy_title).visibility = View.GONE
        view.findViewById<ScrollView>(R.id.scrollView).visibility = View.GONE
        view.findViewById<AppCompatButton>(R.id.more_vacancies_button).visibility = View.GONE

        view.findViewById<ScrollView>(R.id.scrollView2).visibility = View.VISIBLE
        view.findViewById<ImageView>(R.id.arrow_image).visibility = View.VISIBLE
        view.findViewById<TextView>(R.id.textView2).visibility = View.VISIBLE
        view.findViewById<TextView>(R.id.number_vacancies).visibility = View.VISIBLE
        view.findViewById<LinearLayout>(R.id.map).visibility = View.VISIBLE
    }
}