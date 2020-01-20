package com.example.bankingsystemkotlinpractice

import android.app.Activity
import android.app.Activity.*
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_sign_in.editTextPassword
import kotlinx.android.synthetic.main.activity_sign_in.editTextUserName
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity(), ISignUpFormValidator {

    companion object {
        const val USER_ADD_REQUEST_CODE = 1001
    }

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        registerHandlers()
    }

    private fun registerHandlers() {
        spinnerRole.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (parent.getItemAtPosition(position).toString()) {
                    "Customer" -> editTextPhoneNo.visibility = View.VISIBLE
                    else -> editTextPhoneNo.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        buttonSubmit.setOnClickListener { submitButtonHandler() }

        buttonCancel.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun submitButtonHandler() {

        val userName = editTextUserName.text.toString()
        val userPassword = editTextPassword.text.toString()
        val userConfirmPassword = editTextConfirmPassword.text.toString()
        val roleType = spinnerRole.selectedItem.toString()
        val phoneNo = editTextPhoneNo.text.toString()

        val signUpForm = SignUpForm(userName, userPassword, userConfirmPassword, roleType, phoneNo)
        user = User(userName, userPassword, roleType, phoneNo)

        val validator = SignUpFormValidator(this)
        validator.validate(signUpForm, this)
    }

   override fun onValidationFailure(message: String) {
        showToast(message)
    }

    override fun onValidationSuccess(message: String) {
        val isUserDetailsSaved = saveUserDetails(user!!)
        when { isUserDetailsSaved -> { showToast(message)
                                     navigateToSignIn() }
            else -> showToast(getString(R.string.user_not_added))
        }
    }

    private fun navigateToSignIn() {
        setResult(RESULT_OK)
        finish()
    }

    private fun saveUserDetails(user: User): Boolean {
        val bankingSystemDataBaseHelper = BankingSystemDataBaseHelper(this)
        val isUserAdded = bankingSystemDataBaseHelper.addUserDetails(user)
        return when {
            isUserAdded!! -> { showToast(getString(R.string.user_added_successfully))
                                 true
            }
            else -> false
        }
    }
}
