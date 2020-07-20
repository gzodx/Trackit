package com.nike.trackit.model

data class Data(
    val active: Boolean,
    val agentId: String,
    val customer: CustomerX,
    val referenceId: String,
    val type: String
)