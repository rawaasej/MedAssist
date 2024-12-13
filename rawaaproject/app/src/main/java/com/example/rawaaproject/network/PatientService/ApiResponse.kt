package com.example.rawaaproject.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    val status: String,
    val message: String,
    val token: String?, // Ajout du champ token, nullable au cas où il ne serait pas toujours présent
    val patient: Patient? // Champ patient de type Patient
)
