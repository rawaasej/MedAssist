package com.example.rawaaproject.model

import com.google.gson.annotations.SerializedName

data class Patient(
    @field:SerializedName("_id")  // Utilisez "_id" pour faire référence à l'ID généré par MongoDB
    val id: String? = null,  // Champ auto-généré par MongoDB

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("mdp")
    val mdp: String,

    @field:SerializedName("age")
    val age: Int,

    @field:SerializedName("num")
    val num: Int,

    @field:SerializedName("address")
    val address: String,

    @field:SerializedName("medicaments")
    val medicaments: List<Medicament> = emptyList()  // Liste de médicaments, peut être vide
)
