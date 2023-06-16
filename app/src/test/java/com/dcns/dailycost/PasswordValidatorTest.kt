package com.dcns.dailycost

import com.dcns.dailycost.foundation.common.PasswordValidator
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PasswordValidatorTest {

    @Test
    fun `should return failure`() {
        assert(PasswordValidator.validate("ABC").exceptionOrNull() is PasswordValidator.BelowMinLengthException) {
            "Validation success"
        }

        assert(PasswordValidator.validate("ABCDEFGH").exceptionOrNull() is PasswordValidator.LowerCaseMissingException) {
            "Validation success"
        }

        assert(PasswordValidator.validate("abcdefgh").exceptionOrNull() is PasswordValidator.UpperCaseMissingException) {
            "Validation success"
        }

        assert(PasswordValidator.validate("abcDEFgh").exceptionOrNull() is PasswordValidator.DigitMissingException) {
            "Validation success"
        }
    }

    @Test
    fun `should return success`() {
        assert(PasswordValidator.validate("abcDEFgh123").isSuccess) {
            "Validation failure"
        }
    }

}