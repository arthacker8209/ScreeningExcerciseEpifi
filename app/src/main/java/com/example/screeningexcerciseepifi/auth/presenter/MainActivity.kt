package com.example.screeningexcerciseepifi.auth.presenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import com.example.screeningexcerciseepifi.R
import com.example.screeningexcerciseepifi.auth.models.DateOfBirth
import com.example.screeningexcerciseepifi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var binding: ActivityMainBinding
    private val dobModel: DateOfBirth = DateOfBirth()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.panNumber.doOnTextChanged { text, _, _, _ ->
                viewModel.validatePAN(text.toString())
        }

        binding.edtDay.doOnTextChanged { text, _, _, _ ->
            viewModel.validateDate(dobModel.also { it.day = text.toString()})
        }

        binding.edtMonth.doOnTextChanged { text, _, _, _ ->
            viewModel.validateDate(dobModel.also { it.month = text.toString()})
        }

        binding.edtYear.doOnTextChanged { text, _, _, _ ->
            viewModel.validateDate(dobModel.also { it.year = text.toString()})
        }


        viewModel.verifyAllDetailsLiveData.observe(this){
            when (it){
                true -> enableButton()
                false -> disableButton()
            }
        }

        viewModel.dateValidate.observe(this){
            when (it) {
                is ViewState.Success -> dobErrorsVisibility(false)
                is ViewState.Error -> dobErrorsVisibility(true , it.errorMessage)
            }
        }

        viewModel.panValidate.observe(this){
            when (it) {
                is ViewState.Success -> {
                    panErrorsVisibility(false)
                    highlightPanInputField()
                }
                is ViewState.Error -> {
                    panErrorsVisibility(true, it.errorMessage)
                    unhighlightPanInputField()
                }

            }
        }

        onSubmitButtonClicked()
        onNoPanButtonClicked()
    }



    private fun disableButton() {
        binding.submitButton.isEnabled = false
    }

    private fun onSubmitButtonClicked(){
            binding.submitButton.setOnClickListener {
                Toast.makeText(this, "Details submitted successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    private fun onNoPanButtonClicked(){
        binding.btnNoPan.setOnClickListener {
            finish()
        }
    }

    private fun enableButton(){
        binding.submitButton.isEnabled = true
    }

    private fun panErrorsVisibility(isVisible: Boolean , errorMessage: String? = null){
        binding.tvPanError.apply {
            if(isVisible && errorMessage!=null){
                visibility = View.VISIBLE
                text = errorMessage
            }else{
                visibility = View.GONE
            }
        }
    }

    private fun dobErrorsVisibility(isVisible: Boolean, errorMessage: String? = null) {
        binding.tvDateError.apply {
            if(isVisible && errorMessage!=null){
                visibility = View.VISIBLE
                text = errorMessage
            }else{
                visibility = View.GONE
            }
        }
    }

    private fun highlightPanInputField(){
        binding.panNumber.setBackgroundResource(R.drawable.edit_field_background)
    }
    private fun unhighlightPanInputField(){
        binding.panNumber.setBackgroundResource(R.drawable.edt_pan_higlight_background)
    }
}
