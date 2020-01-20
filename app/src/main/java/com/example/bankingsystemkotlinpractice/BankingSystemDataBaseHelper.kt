package com.example.bankingsystemkotlinpractice

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.bankingsystemkotlinpractice.User.Companion.COLUMN_USER_ACCOUNT_NO
import com.example.bankingsystemkotlinpractice.User.Companion.COLUMN_USER_BALANCE
import com.example.bankingsystemkotlinpractice.User.Companion.COLUMN_USER_ID
import com.example.bankingsystemkotlinpractice.User.Companion.COLUMN_USER_NAME
import com.example.bankingsystemkotlinpractice.User.Companion.COLUMN_USER_PASSWORD
import com.example.bankingsystemkotlinpractice.User.Companion.COLUMN_USER_PHONE_NO
import com.example.bankingsystemkotlinpractice.User.Companion.COLUMN_USER_ROLE_TYPE


class BankingSystemDataBaseHelper internal constructor(context: Context?) :
    SQLiteOpenHelper(context, DATABASE, null, 5) {

    override fun onCreate(db: SQLiteDatabase) {

        val createUserTable = "CREATE TABLE " + USER_TABLE + "(" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USER_NAME + " TEXT," +
                COLUMN_USER_PASSWORD + " TEXT," +
                COLUMN_USER_ROLE_TYPE + " TEXT," +
                COLUMN_USER_PHONE_NO + " INTEGER," +
                COLUMN_USER_ACCOUNT_NO + " TEXT UNIQUE," +
                COLUMN_USER_BALANCE + " INTEGER DEFAULT 0)"
        db.execSQL(createUserTable)
    }

   internal fun getCustomers(): Cursor {
        val dataBase = this.readableDatabase
        return dataBase.rawQuery("SELECT * FROM users WHERE userRoleType = \"Customer\"", null)
    }

    internal fun getAccountNosList(): Cursor {
        val dataBase = this.readableDatabase
        return dataBase.rawQuery(" SELECT userAccountNo FROM users WHERE userAccountNo NOTNULL ", null
        )
    }


    internal fun addUserDetails(user: User): Boolean? {
        val sqLiteDatabase = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_USER_NAME, user.userName)
        contentValues.put(COLUMN_USER_PASSWORD, user.userPassword)
        contentValues.put(COLUMN_USER_ROLE_TYPE, user.roleType)
        contentValues.put(COLUMN_USER_PHONE_NO, user.phoneNo)
        val rowIdOfNewlyInsertedRow = sqLiteDatabase.insert(USER_TABLE, null, contentValues)
        return !rowIdOfNewlyInsertedRow.equals(-1)
    }

    internal fun getUserDetails(userName: String): Cursor {
        val dataBase = this.readableDatabase
        return dataBase.rawQuery("SELECT * FROM $USER_TABLE WHERE $COLUMN_USER_NAME LIKE userName", null)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    internal fun getCustomerDetails(customerId: Int): Cursor {
        val dataBase = this.readableDatabase
        return dataBase.rawQuery("select * from users WHERE userId = $customerId ", null)
    }

    internal fun updateCustomerDetails(customerId: Int, accountNo: String): Boolean {
        val sqLiteDatabase = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_USER_ACCOUNT_NO, accountNo)
        val noOfRowsAffected = sqLiteDatabase.update(USER_TABLE, contentValues, COLUMN_USER_ID + " = ?", arrayOf(customerId.toString())).toLong()
        return noOfRowsAffected != 0L
    }

    internal fun updateCustomerBalance(customerId: Int, totalMoney: Int): Boolean {
        val sqLiteDatabase = writableDatabase
        val contentValues = ContentValues()
        contentValues.put((COLUMN_USER_BALANCE), totalMoney)
        val noOfRowsAffected = sqLiteDatabase.update(USER_TABLE, contentValues, COLUMN_USER_ID + " = ?", arrayOf(customerId.toString())).toLong()
        return noOfRowsAffected != 0L
    }

    internal fun getBalance(debitFromAccount: String): Cursor {
        val database = this.readableDatabase
        return database.rawQuery(" SELECT userBalance from $USER_TABLE WHERE $COLUMN_USER_ACCOUNT_NO= ?", arrayOf(debitFromAccount)
        )
    }

    internal fun updateCustomerBalanceOnTheBasisOfAccountNo(debitFromAccount: String, debitedBalance: Int): Boolean {
        val sqLiteDatabase = writableDatabase
        val contentValues = ContentValues()
        contentValues.put((COLUMN_USER_BALANCE), debitedBalance)
        val noOfRowsAffected = sqLiteDatabase.update(USER_TABLE, contentValues, COLUMN_USER_ACCOUNT_NO + " = ?", arrayOf(debitFromAccount)).toLong()
        return noOfRowsAffected != 0L
    }

    companion object {
        private val DATABASE = "bankingSystemDataBase.db"
        private val USER_TABLE = "users"
    }
}

