package com.example.app.proto_datastore

data class AppSettings(
    val language: Language = Language.ENGLISH
)

enum class Language{
    ENGLISH, KOREAN, JAPENESE
}