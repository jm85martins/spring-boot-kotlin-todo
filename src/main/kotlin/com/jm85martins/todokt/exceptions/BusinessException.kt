package com.jm85martins.todokt.exceptions

import org.slf4j.LoggerFactory

/**
 * Created by jorgemartins on 20/07/2017.
 */
abstract class BusinessException : RuntimeException {
    val logger = LoggerFactory.getLogger(this.javaClass)

    var developerMessage: String? = null
        private set
    var userMessage: String? = null
        private set

    constructor(userMessage: String) : super(userMessage) {
        this.developerMessage = userMessage
        this.userMessage = userMessage
        this.log()
    }

    constructor(userMessage: String, developerMessage: String) : super(userMessage) {
        this.userMessage = userMessage
        this.developerMessage = developerMessage
        this.log()
    }

    private fun log() {
        this.logger.error(this.developerMessage)
    }
}
