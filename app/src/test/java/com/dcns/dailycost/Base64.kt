package com.dcns.dailycost

class Base64 {

    companion object {
        fun encode(input: ByteArray): String {
            return java.util.Base64.getEncoder().encodeToString(input)
        }

        fun decode(str: String): ByteArray {
            return java.util.Base64.getDecoder().decode(str)
        }
    }

}