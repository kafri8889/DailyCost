package com.dcns.dailycost.domain.util

sealed class EditUserCredentialType {

	data class ID(val value: String): EditUserCredentialType()
	data class Name(val value: String): EditUserCredentialType()
	data class Email(val value: String): EditUserCredentialType()
	data class Token(val value: String): EditUserCredentialType()
	data class Password(val value: String): EditUserCredentialType()

}