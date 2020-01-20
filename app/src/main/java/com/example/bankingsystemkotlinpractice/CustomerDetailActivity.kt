package com.example.bankingsystemkotlinpractice

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_customer_detail.*

class CustomerDetailActivity : BaseActivity() {

    private var customerId: Int = 0
    private var customer: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_detail)

        val intent = intent
        customerId = intent.getIntExtra("CUSTOMER_ID", -1)
        if (customerId != -1) {
            showCustomerDetails(customerId)
        }
        registerHandlers()

    }

    private fun showCustomerDetails(customerId: Int) {
        customer = User.getCustomerDetails(customerId, this)
        editTextCustomerName.setText(customer!!.userName)
        editTextPhoneNo.setText(customer!!.phoneNo)
        editTextBalance.setText((customer!!.balance).toString())
        editTextAccountNo.setText(customer!!.accountNo)
    }

    private fun setAccountNoToCustomer(): String {
        var accountNo = editTextAccountNo.text.toString()
        accountNo = "NXT$accountNo"
        val isAccountNoUpdated = User.addAccountNoToCustomer(customerId, accountNo, this)
        return if (isAccountNoUpdated) {
            getString(R.string.account_no_updated)
        } else {
            getString(R.string.account_no_not_updated)
        }
    }

    private fun registerHandlers() {
        buttonDeposit.setOnClickListener { depositButtonHandler() }
        buttonSubmit.setOnClickListener { submitButtonHandler() }
    }

    private fun submitButtonHandler() {
        if (customer!!.accountNo == null) {
            val message: String = setAccountNoToCustomer()
            showToast(message)
            return
        } else { navigateToManagerDashBoard()
        }
    }

    private fun navigateToManagerDashBoard() {
        setResult(RESULT_OK)
        finish()
    }

    private fun depositButtonHandler() {
        val moneyToAdd = Integer.parseInt(editTextAddMoney.text.toString())
        val totalMoney = moneyToAdd + customer!!.balance
        val isBalanceUpdated = User.depositMoneyToCustomerBalance(customerId, totalMoney, this)
        if (isBalanceUpdated) {
            showToast(getString(R.string.balance_updated))
        } else {
            showToast(getString(R.string.balance_not_updated))
        }
    }
}

