package com.nike.trackit.model

data class RevereCodingModel(
    val address: Address,
    val boundingbox: List<String>,
    val display_name: String,
    val importance: Double,
    val lat: String,
    val licence: String,
    val lon: String,
    val osm_id: String,
    val osm_type: String,
    val place_id: String
)