package com.example.bankingsystemkotlinpractice

interface ISignUpFormValidator {

    fun onValidationSuccess(message: String)
    fun onValidationFailure(message: String)

}
