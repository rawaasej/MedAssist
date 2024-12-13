package com.example.rawaaproject.network.PatientService

import com.example.rawaaproject.model.ApiResponse
import com.example.rawaaproject.model.Medicament
import com.example.rawaaproject.model.Patient
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // Créer un patient
    @POST("api/patients")
    fun createPatient(@Body patient: Patient): Call<Patient>

    // Récupérer tous les patients
    @GET("api/patients")
    fun getAllPatients(): Call<List<Patient>>

    // Récupérer un patient par ID
    @GET("api/patients/{patientId}")
    fun getPatientById(@Path("patientId") patientId: String): Call<Patient>

    // Mettre à jour un patient
    @PUT("api/patients/{patientId}")
    fun updatePatient(
        @Path("patientId") patientId: String,
        @Body patient: Patient
    ): Call<Patient>

    // Supprimer un patient
    @DELETE("api/patients/{patientId}")
    fun deletePatient(@Path("patientId") patientId: String): Call<ApiResponse>


    // Ajouter un médicament à un patient
    @POST("api/patients/{patientId}/medicaments")
    fun addMedicament(
        @Path("patientId") patientId: String,
        @Body medicament: Medicament
    ): Call<Medicament>

    // Récupérer les médicaments d'un patient
    @GET("api/patients/{patientId}/medicaments")
    fun getPatientMedicaments(@Path("patientId") patientId: String): Call<List<Medicament>>

    // Mettre à jour un médicament d'un patient
    @PUT("api/patients/{patientId}/medicaments/{medicamentName}")
    fun updateMedicament(
        @Path("patientId") patientId: String,
        @Path("medicamentName") medicamentName: String,
        @Body medicament: Medicament
    ): Call<Medicament>

    // Supprimer un médicament d'un patient
    @DELETE("api/patients/{patientId}/medicaments/{medicamentName}")
    fun deleteMedicament(
        @Path("patientId") patientId: String,
        @Path("medicamentName") medicamentName: String
    ): Call<ApiResponse>


    // Récupérer tous les médicaments de tous les patients
    @GET("api/medicaments")
    fun getAllMedicaments(): Call<List<Medicament>>

    @POST("api/patients/auth")
    fun authenticate(@Body credentials: Map<String, String>): Call<ApiResponse>

}
