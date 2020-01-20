package com.example.bankingsystemkotlinpractice

import android.content.Context

class SignUpFormValidator{

    private var validator: ISignUpFormValidator

   constructor(validator: ISignUpFormValidator){
        this.validator = validator
    }

    fun validate(signUpForm: SignUpForm, context: Context) {
        val message: String
        if (signUpForm.name.isEmpty() || signUpForm.password.isEmpty()
            || signUpForm.confirmPassword.isEmpty() || signUpForm.roleType.isEmpty()) {
            message = context.getString(R.string.enter_all_details)
            validator.onValidationFailure(message)
        } else if (!signUpForm.password.equals(signUpForm.confirmPassword)) {
            message = context.getString(R.string.password_mis_matched)
            validator.onValidationFailure(message)
        } else {
            message = context.getString(R.string.input_fields_validated)
            validator.onValidationSuccess(message)
        }
    }

}
