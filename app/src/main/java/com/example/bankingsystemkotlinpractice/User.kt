package com.example.bankingsystemkotlinpractice

import android.content.Context
import java.util.ArrayList

class User {                                              // if object, then constructors of user cannot be created
                                                          // variables and methods become static (singleton class)
    lateinit var userName: String
    lateinit var userPassword: String
    lateinit var roleType: String
    var phoneNo: String? = null
    var accountNo: String? = null
    var balance: Int = 0
    var userId: Int = 0

    constructor(userId: Int, userPassword: String, roleType: String, phoneNo: String, accountNo: String?) {
        this.userId = userId
        this.userPassword = userPassword
        this.roleType = roleType
        this.phoneNo = phoneNo
        this.accountNo = accountNo
    }

    constructor(userName: String, userId: Int) {
        this.userName = userName
        this.userId = userId

    }

    constructor(userName: String, userPassword: String, roleType: String, phoneNo: String) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.roleType = roleType;
        this.phoneNo = phoneNo;
    }

    constructor(userName: String, phoneNo: String, accountNo: String?, balance: Int) {
        this.userName = userName
        this.phoneNo = phoneNo
        this.accountNo = accountNo
        this.balance = balance
    }

    companion object {                                      // static, same as "object",declared within class
        var COLUMN_USER_ID = "userId"
        var COLUMN_USER_NAME = "userName"
        var COLUMN_USER_PASSWORD = "userPassword"
        var COLUMN_USER_ROLE_TYPE = "userRoleType"
        var COLUMN_USER_PHONE_NO = "userPhoneNo"
        var COLUMN_USER_ACCOUNT_NO = "userAccountNo"
        var COLUMN_USER_BALANCE = "userBalance"


        fun getUserDetailsFromDataBase(userName: String, context: Context): User? {
            val bankingSystemDataBaseHelper = BankingSystemDataBaseHelper(context)
            val userDetailsCursor = bankingSystemDataBaseHelper.getUserDetails(userName)
            return when {
                !userDetailsCursor.moveToFirst() -> null
                else -> User(
                    userDetailsCursor.getInt(userDetailsCursor.getColumnIndex(COLUMN_USER_ID)),
                    userDetailsCursor.getString(userDetailsCursor.getColumnIndex(COLUMN_USER_PASSWORD)),
                    userDetailsCursor.getString(userDetailsCursor.getColumnIndex(COLUMN_USER_ROLE_TYPE)),
                    userDetailsCursor.getString(userDetailsCursor.getColumnIndex(COLUMN_USER_PHONE_NO)),
                    null
                )
            }
        }
        fun getCustomers(context: Context): ArrayList<User>? {
            val users = ArrayList<User>()
            val bankingSystemDataBaseHelper = BankingSystemDataBaseHelper(context)
            val userCursor = bankingSystemDataBaseHelper.getCustomers()
            if (userCursor.count == 0) {
                return null
            }

            while (userCursor.moveToNext()) {
                val user = User(userCursor.getString(userCursor.getColumnIndex(COLUMN_USER_NAME)),
                    userCursor.getInt(userCursor.getColumnIndex(COLUMN_USER_ID)))
                users.add(user)
            }
            return users
        }

        internal fun getAccountNosList(context: Context): ArrayList<String>? {
            val accounts = ArrayList<String>()
            val bankingSystemDataBaseHelper = BankingSystemDataBaseHelper(context)
            val userCursor = bankingSystemDataBaseHelper.getAccountNosList()
            if (userCursor.count == 0) {
                return null
            }

            while (userCursor.moveToNext()) {
                val accountNo = userCursor.getString(userCursor.getColumnIndex(COLUMN_USER_ACCOUNT_NO))
                accounts.add(accountNo)
            }
            return accounts
        }

        internal fun getCustomerDetails(customerId: Int, context: Context): User? {
            val bankingSystemDataBaseHelper = BankingSystemDataBaseHelper(context)
            val customerCursor = bankingSystemDataBaseHelper.getCustomerDetails(customerId)

            return if (!customerCursor.moveToFirst()) {
                null
            } else User(customerCursor.getString(customerCursor.getColumnIndex(COLUMN_USER_NAME)),
                customerCursor.getString(customerCursor.getColumnIndex(COLUMN_USER_PHONE_NO)),
                customerCursor.getString(customerCursor.getColumnIndex(COLUMN_USER_ACCOUNT_NO)),
                customerCursor.getInt(customerCursor.getColumnIndex(COLUMN_USER_BALANCE)))

        }

        internal fun addAccountNoToCustomer(customerId: Int, accountNo: String, context: Context): Boolean {
            val bankingSystemDataBaseHelper = BankingSystemDataBaseHelper(context)
            return bankingSystemDataBaseHelper.updateCustomerDetails(customerId, accountNo)
        }

        internal fun depositMoneyToCustomerBalance(customerId: Int, totalMoney: Int, context: Context): Boolean {
            val bankingSystemDataBaseHelper = BankingSystemDataBaseHelper(context)
            return bankingSystemDataBaseHelper.updateCustomerBalance(customerId, totalMoney)
        }


        internal fun getBalance(debitFromAccount: String, context: Context): Int {
            var balance = 0
            val bankingSystemDataBaseHelper = BankingSystemDataBaseHelper(context)
            val userCursor = bankingSystemDataBaseHelper.getBalance(debitFromAccount)

            if (userCursor.count == 0) {
                return 0
            }

            while (userCursor.moveToNext()) {
                balance = userCursor.getInt(userCursor.getColumnIndex(COLUMN_USER_BALANCE))
            }
            return balance
        }

        internal fun updateBalanceOfAccount(debitFromAccount: String, debitedBalance: Int, context: Context): Boolean {
            val bankingSystemDataBaseHelper = BankingSystemDataBaseHelper(context)
            return bankingSystemDataBaseHelper.updateCustomerBalanceOnTheBasisOfAccountNo(debitFromAccount, debitedBalance)
        }
    }
}
