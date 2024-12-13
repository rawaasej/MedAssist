package com.example.rawaaproject.model

import com.google.gson.annotations.SerializedName

data class Medicament(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("dosage")
    val dosage: String,

    @field:SerializedName("frequency")
    val frequency: String,

    @field:SerializedName("startDate")
    val startDate: String,

    @field:SerializedName("endDate")
    val endDate: String
)
