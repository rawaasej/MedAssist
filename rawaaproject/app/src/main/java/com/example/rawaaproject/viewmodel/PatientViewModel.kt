package com.example.rawaaproject.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rawaaproject.model.ApiResponse
import com.example.rawaaproject.model.Patient
import com.example.rawaaproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatientViewModel : ViewModel() {

    private val _patients = MutableLiveData<List<Patient>>()
    val patients: LiveData<List<Patient>> get() = _patients

    private val _patient = MutableLiveData<Patient>()
    val patient: LiveData<Patient> get() = _patient

    private val _selectedPatient = MutableLiveData<Patient?>()
    val selectedPatient: LiveData<Patient?> get() = _selectedPatient

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Récupérer tous les patients
    fun getAllPatients() {
        RetrofitClient.apiService.getAllPatients().enqueue(object : Callback<List<Patient>> {
            override fun onResponse(call: Call<List<Patient>>, response: Response<List<Patient>>) {
                if (response.isSuccessful) {
                    _patients.value = response.body()
                } else {
                    _error.value = "Erreur: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<List<Patient>>, t: Throwable) {
                _error.value = "Erreur de connexion: ${t.message}"
            }
        })
    }

    // Récupérer un patient par ID
    fun getPatientById(patientId: String) {
        RetrofitClient.apiService.getPatientById(patientId).enqueue(object : Callback<Patient> {
            override fun onResponse(call: Call<Patient>, response: Response<Patient>) {
                if (response.isSuccessful) {
                    _patient.value = response.body()
                } else {
                    _error.value = "Erreur: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<Patient>, t: Throwable) {
                _error.value = "Erreur de connexion: ${t.message}"
            }
        })
    }

    // Ajouter un patient
    fun createPatient(patient: Patient) {
        RetrofitClient.apiService.createPatient(patient).enqueue(object : Callback<Patient> {
            override fun onResponse(call: Call<Patient>, response: Response<Patient>) {
                if (response.isSuccessful) {
                    // Optionnel: vous pouvez mettre à jour la liste des patients si nécessaire
                    getAllPatients()
                } else {
                    _error.value = "Erreur: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<Patient>, t: Throwable) {
                _error.value = "Erreur de connexion: ${t.message}"
            }
        })
    }

    // Mettre à jour un patient
    fun updatePatient(patientId: String, patient: Patient) {
        // Vérifiez que l'ID du patient est valide
        if (patientId.isNotEmpty() && patient.id != null) {
            RetrofitClient.apiService.updatePatient(patientId, patient)
                .enqueue(object : Callback<Patient> {
                    override fun onResponse(call: Call<Patient>, response: Response<Patient>) {
                        if (response.isSuccessful) {
                            _patient.value = response.body() // Le patient mis à jour
                            // Optionnellement, vous pouvez également mettre à jour la liste des patients
                            getAllPatients()
                        } else {
                            _error.value = "Erreur: ${response.message()}"
                        }
                    }

                    override fun onFailure(call: Call<Patient>, t: Throwable) {
                        _error.value = "Erreur de connexion: ${t.message}"
                    }
                })
        } else {
            _error.value = "ID du patient invalide ou non fourni."
        }
    }

    // Supprimer un patient
    fun deletePatient(patientId: String) {
        RetrofitClient.apiService.deletePatient(patientId).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        // Vous pouvez accéder à apiResponse.status et apiResponse.message
                        Log.d(
                            "Delete Patient",
                            "Status: ${apiResponse.status}, Message: ${apiResponse.message}"
                        )
                        // Optionnel: mettre à jour la liste des patients si nécessaire
                        getAllPatients()
                    }
                } else {
                    _error.value = "Erreur: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                _error.value = "Erreur de connexion: ${t.message}"
            }
        })
    }

    // Sélectionner un patient pour la mise à jour
    fun selectPatient(patient: Patient) {
        _selectedPatient.value = patient
    }

    // Réinitialiser la sélection du patient
    fun clearSelectedPatient() {
        _selectedPatient.value = null
    }
}
