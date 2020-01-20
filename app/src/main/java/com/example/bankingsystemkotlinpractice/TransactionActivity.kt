package com.example.bankingsystemkotlinpractice

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_manager_dash_board.buttonTransfer
import kotlinx.android.synthetic.main.activity_transaction.*

import java.util.ArrayList

class TransactionActivity : BaseActivity() {

    private var accountNos: ArrayList<String>? = ArrayList()
    internal lateinit var accountNoFrom: String
    internal lateinit var accountNoTo: String
    internal lateinit var debitFromAccount: String
    internal lateinit var creditToAccount: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        getAccountNosList()

        registerHandlers()
    }

    private fun registerHandlers() {
        buttonTransfer!!.setOnClickListener { buttonTransferHandler() }
    }

    private fun buttonTransferHandler() {
        debitFromAccount = spinnerFromAccount.selectedItem.toString()
        creditToAccount = spinnerToAccount.selectedItem.toString()
        val moneyToTransfer = Integer.parseInt(editTextAmountToDebit.text.toString())

        val isTransactionDone = doTransaction(debitFromAccount, creditToAccount, moneyToTransfer)
        if (isTransactionDone) {
            showToast(getString(R.string.balance_updated))
            navigateBackToManagerDashBoard()
        } else {
            showToast(getString(R.string.not_enough_balance))
        }
    }

    private fun navigateBackToManagerDashBoard() {
        setResult(RESULT_OK)
        finish()
    }

    private fun doTransaction(debitFromAccount: String, creditToAccount: String, moneyToTransfer: Int): Boolean {
        val debitAccountBalance = User.getBalance(debitFromAccount, this)
        val creditAccountBalance = User.getBalance(creditToAccount, this)

        if (debitAccountBalance > moneyToTransfer) {
            val debitedBalance = debitAccountBalance - moneyToTransfer
            User.updateBalanceOfAccount(debitFromAccount, debitedBalance, this)

            val creditedBalance = creditAccountBalance + moneyToTransfer
            User.updateBalanceOfAccount(creditToAccount, creditedBalance, this)
            return true
        } else {
            return false
        }


    }

    private fun getAccountNosList() {
        accountNos = User.getAccountNosList(this)
        if (accountNos != null) {
            getListOfAccountNosForFromAccount()
            getListOfAccountNosForToAccount()
        } else {
            showToast(getString(R.string.account_no_not_assigned))
        }
    }


    private fun getListOfAccountNosForFromAccount() {
        val accountNosListAdapter = AccountNosListAdapter(this, accountNos!!)
        spinnerFromAccount.adapter = accountNosListAdapter
        spinnerFromAccountItemClickHandler()
    }

    private fun spinnerFromAccountItemClickHandler() {
        spinnerFromAccount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>, arg1: View,
                position: Int, arg3: Long
            ) {
                accountNoFrom = spinnerFromAccount!!.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {}
        }
    }

    private fun getListOfAccountNosForToAccount() {
        val accountNosListAdapter = AccountNosListAdapter(this, accountNos!!)
        spinnerToAccount.adapter = accountNosListAdapter
        spinnerToAccountItemClickHandler()

    }

    private fun spinnerToAccountItemClickHandler() {
        spinnerToAccount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, arg3: Long) {
                accountNoTo = spinnerToAccount.getItemAtPosition(position) as String
            }
            override fun onNothingSelected(arg0: AdapterView<*>) {}
        }
    }

    companion object {
        val BALANCE_ADD_REQUEST_CODE = 1002
    }


}

