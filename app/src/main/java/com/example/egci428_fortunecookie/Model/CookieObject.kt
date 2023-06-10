package com.example.egci428_fortunecookie.Model

import java.io.Serializable

data class CookieObject(val items: ArrayList<Cookie>): Serializable{
    public fun get(): ArrayList<Cookie> {
        return items
    }
}
