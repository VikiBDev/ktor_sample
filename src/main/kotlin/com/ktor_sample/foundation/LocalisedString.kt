package com.ktor_sample.foundation

import org.slf4j.LoggerFactory

typealias LocalisedString = Map<String, String>

fun LocalisedString.translate(language: Language): String {
    return this[language.selected] ?: kotlin.run {
        this[language.default] ?: kotlin.run {
            val logger = LoggerFactory.getLogger("LocalisedString")
            logger.info("LocalisedString $this doesn't contain a default value for language [${language.default}]")
            ""
        }
    }
}