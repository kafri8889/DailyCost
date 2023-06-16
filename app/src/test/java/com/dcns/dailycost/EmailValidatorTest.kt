package com.dcns.dailycost

import com.dcns.dailycost.foundation.common.EmailValidator
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class EmailValidatorTest {

    @Test
    fun `should return failure`() {
        assert(EmailValidator.validate("foo").isFailure) {
            "Validation success"
        }

        assert(EmailValidator.validate("@gmail.com").isFailure) {
            "Validation success"
        }

        assert(EmailValidator.validate("foo@example.com").isFailure) {
            "Validation success"
        }
    }

    @Test
    fun `should return success`() {
        assert(EmailValidator.validate("foo@gmail.com").isSuccess) {
            "Validation failure"
        }
    }

}