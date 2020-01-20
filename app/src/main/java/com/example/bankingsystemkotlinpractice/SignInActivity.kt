package com.example.bankingsystemkotlinpractice

import android.content.Intent
import android.os.Bundle
import com.example.bankingsystemkotlinpractice.ManagerDashBoard.Companion.CUSTOMER_LIST_REQUEST_CODE
import com.example.bankingsystemkotlinpractice.User.Companion.getUserDetailsFromDataBase
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : BaseActivity() {

    private var roleType: String? = null
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        registerHandlers()
    }

    private fun registerHandlers() {
        buttonLogin!!.setOnClickListener { loginButtonHandler() }

        buttonSignUp!!.setOnClickListener {
            val signUpIntent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivityForResult(signUpIntent, SignUpActivity.USER_ADD_REQUEST_CODE)
        }

    }


    private fun loginButtonHandler() {

        val userName = editTextUserName!!.text.toString().trim()
        val userPassword = editTextPassword!!.text.toString().trim()

        user = getUserDetailsFromDataBase(userName, this)
        when { user != null -> {
                roleType = user!!.roleType
                when (val error = validateInputFields(userName, userPassword)) {
                    null -> showManagerDashBoard()
                    else -> showToast(error)
                }
            }
            else -> showToast(getString(R.string.user_does_not_exist))
        }
    }


    private fun showManagerDashBoard() {
        when (roleType) { "Manager" -> {
            val managerDashBoardIntent = Intent(this@SignInActivity, ManagerDashBoard::class.java)
            startActivityForResult(managerDashBoardIntent, CUSTOMER_LIST_REQUEST_CODE)
            }
            else -> showToast(getString(R.string.customer_cannot_log_in))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CUSTOMER_LIST_REQUEST_CODE) {
            editTextUserName!!.text.clear()
            editTextPassword!!.text.clear()
        }
    }

    private fun validateInputFields(userName: String, userPassword: String): String? {
        return when {
            userName.isEmpty() || userPassword.isEmpty() -> getString(R.string.enter_all_details)
            userPassword.length < 6 -> getString(R.string.password_is_less_than_6)
            user!!.userPassword == userPassword -> null
            else -> getString(R.string.wrong_user_details)
        }
    }

}
