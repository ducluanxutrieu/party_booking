package com.uit.party.util

import com.uit.party.R


fun CharSequence?.usernameErrorMes(): String {
    return when {
        this.isNullOrEmpty() -> UiUtil.getString(R.string.this_field_required)
        this.contains(" ") -> UiUtil.getString(R.string.this_field_cannot_contain_space)
        else -> ""
    }
}

fun CharSequence?.phoneErrorMes(): String {
    return when {
        this.isNullOrEmpty() -> UiUtil.getString(R.string.this_field_required)
        !android.util.Patterns.PHONE.matcher(this)
            .matches() -> UiUtil.getString(R.string.phone_not_valid)
        this.trim().length < 9 -> UiUtil.getString(R.string.phone_number_too_short)
        else -> ""
    }
}

fun CharSequence?.emailErrorMes(): String {
    return when {
        this.isNullOrEmpty() -> UiUtil.getString(R.string.this_field_required)
        !android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches() -> UiUtil.getString(R.string.email_not_valid)
        else -> ""
    }
}

fun CharSequence?.requireFieldErrorMes(): String {
    return when {
        this.isNullOrEmpty() -> UiUtil.getString(R.string.this_field_required)
        else -> ""
    }
}

fun CharSequence?.passwordErrorMes(): String {
    return when {
        this.isNullOrEmpty() -> UiUtil.getString(R.string.this_field_required)
        this.contains(" ") -> UiUtil.getString(R.string.this_field_cannot_contain_space)
        else -> ""
    }
}

fun CharSequence?.confirmPasswordErrorMes(password: String): String {
    return when {
        this.isNullOrEmpty() -> UiUtil.getString(R.string.this_field_required)
        password != this.toString() -> UiUtil.getString(R.string.not_matched_with_password)
        else -> ""
    }
}