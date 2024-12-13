package com.example.rawaaproject.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.rawaaproject.model.ApiResponse
import com.example.rawaaproject.model.Medicament
import com.example.rawaaproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MedicamentViewModel : ViewModel() {

    // Fonction pour récupérer la liste des médicaments d'un patient
    fun getMedicaments(
        patientId: String,
        onSuccess: (List<Medicament>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        RetrofitClient.apiService.getPatientMedicaments(patientId)
            .enqueue(object : Callback<List<Medicament>> {
                override fun onResponse(
                    call: Call<List<Medicament>>,
                    response: Response<List<Medicament>>
                ) {
                    if (response.isSuccessful) {
                        val medicamentsList = response.body() ?: emptyList()
                        onSuccess(medicamentsList)
                    } else {
                        Log.e("MedicamentViewModel", "Error: ${response.code()} - ${response.message()}")
                        onFailure("Erreur lors de la récupération des médicaments.")
                    }
                }

                override fun onFailure(call: Call<List<Medicament>>, t: Throwable) {
                    Log.e("MedicamentViewModel", "Network error: ${t.message}")
                    onFailure("Erreur réseau : ${t.message}")
                }
            })
    }

    // Fonction pour ajouter un médicament à un patient
    fun addMedicament(
        patientId: String,
        medicament: Medicament,
        onSuccess: (Medicament) -> Unit,
        onFailure: (String) -> Unit
    ) {
        RetrofitClient.apiService.addMedicament(patientId, medicament)
            .enqueue(object : Callback<Medicament> {
                override fun onResponse(
                    call: Call<Medicament>,
                    response: Response<Medicament>
                ) {
                    if (response.isSuccessful) {
                        onSuccess(response.body()!!)
                    } else {
                        Log.e("MedicamentViewModel", "Error: ${response.code()} - ${response.message()}")
                        onFailure("Erreur lors de l'ajout du médicament.")
                    }
                }

                override fun onFailure(call: Call<Medicament>, t: Throwable) {
                    Log.e("MedicamentViewModel", "Network error: ${t.message}")
                    onFailure("Erreur réseau : ${t.message}")
                }
            })
    }

    // Fonction pour mettre à jour un médicament d'un patient
    fun updateMedicament(
        patientId: String,
        medicamentName: String,
        medicament: Medicament,
        onSuccess: (Medicament) -> Unit,
        onFailure: (String) -> Unit
    ) {
        RetrofitClient.apiService.updateMedicament(patientId, medicamentName, medicament)
            .enqueue(object : Callback<Medicament> {
                override fun onResponse(
                    call: Call<Medicament>,
                    response: Response<Medicament>
                ) {
                    if (response.isSuccessful) {
                        onSuccess(response.body()!!)
                    } else {
                        Log.e("MedicamentViewModel", "Error: ${response.code()} - ${response.message()}")
                        onFailure("Erreur lors de la mise à jour du médicament.")
                    }
                }

                override fun onFailure(call: Call<Medicament>, t: Throwable) {
                    Log.e("MedicamentViewModel", "Network error: ${t.message}")
                    onFailure("Erreur réseau : ${t.message}")
                }
            })
    }

    // Fonction pour supprimer un médicament d'un patient
    fun deleteMedicament(
        patientId: String,
        medicamentName: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        RetrofitClient.apiService.deleteMedicament(patientId, medicamentName)
            .enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null && apiResponse.status == "success") {
                            onSuccess()
                        } else {
                            Log.e("MedicamentViewModel", "Error: ${apiResponse?.message}")
                            onFailure(apiResponse?.message ?: "Erreur lors de la suppression.")
                        }
                    } else {
                        Log.e("MedicamentViewModel", "Error: ${response.code()} - ${response.message()}")
                        onFailure("Erreur lors de la suppression du médicament.")
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e("MedicamentViewModel", "Network error: ${t.message}")
                    onFailure("Erreur réseau : ${t.message}")
                }
            })
    }
}
