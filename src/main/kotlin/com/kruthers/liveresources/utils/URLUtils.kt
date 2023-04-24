package com.kruthers.liveresources.utils

import java.lang.Exception
import java.net.URL

class URLUtils {
    companion object {
        fun isValid(url: String): Boolean {
            return try {
                URL(url).toURI()
                true;
            } catch (e: Exception) {
                false
            }
        }


    }
}