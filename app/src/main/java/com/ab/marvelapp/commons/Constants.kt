package com.ab.marvelapp.commons

import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp

object Constants {

    const val BASE_URL = "https://gateway.marvel.com"

    val timeStamp = Timestamp(System.currentTimeMillis()).time.toString()

    const val API_KEY = "67f19e2b8b3eb62f7ef8d33791b51142"

    const val PRIVATE_KEY = "c7d733c3f5137981a0cb1ebd3f8be35f1536df08"

    const val limit = 20

    fun hash():String{
        val input = "$timeStamp$PRIVATE_KEY$API_KEY"
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32,'0')
    }
}