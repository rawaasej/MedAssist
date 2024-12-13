package com.example.rawaaproject.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rawaaproject.R
import com.example.rawaaproject.data.UserStore
import com.example.rawaaproject.model.ApiResponse
import com.example.rawaaproject.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun SignInScreen(navController: NavController, userStore: UserStore) {
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val rememberMeChecked = remember { mutableStateOf(false) }

    // Charger les données sauvegardées (si "Remember Me" était activé)
    LaunchedEffect(Unit) {
        scope.launch {
            userStore.getAccessToken.collect { savedEmail ->
                Log.d("SignInScreen", "Retrieved email: $savedEmail")
                email = savedEmail
            }
            userStore.getPassword.collect { savedPassword ->
                Log.d("SignInScreen", "Retrieved password: $savedPassword") // Ajouter ce log pour vérifier la récupération
                password = savedPassword
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Affichage du message d'erreur
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Titre
        Text("Sign In", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.sante), // Remplacez "your_image" par le nom de votre image dans le dossier res/drawable
            contentDescription = "Sign In Image",
            modifier = Modifier
                .size(300.dp) // Ajustez la taille de l'image si nécessaire
                .padding(top = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))


        // Champ Email
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(15.dp))

        // Champ Mot de Passe
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Checkbox "Remember Me"
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = rememberMeChecked.value,
                onCheckedChange = { rememberMeChecked.value = it },
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF6200EE))
            )
            Text(text = "Remember Me", modifier = Modifier.padding(start = 8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Bouton Sign In
        Button(
            onClick = {
                if (email == "admin@admin" && password == "admin") {
                    navController.navigate("PatientScreen")
                } else {
                    val credentials = mapOf("email" to email, "mdp" to password)
                    RetrofitClient.apiService.authenticate(credentials)
                        .enqueue(object : Callback<ApiResponse> {
                            override fun onResponse(
                                call: Call<ApiResponse>,
                                response: Response<ApiResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val apiResponse = response.body()
                                    Log.d("SignInScreen", "Response: ${response.body()}")
                                    if (apiResponse != null && apiResponse.patient != null) {
                                        val patient = apiResponse.patient
                                        val patientIdFromResponse = patient.id
                                        Log.d("SignInScreen", "Patient ID from response: $patientIdFromResponse")
                                        if (patientIdFromResponse != null) {
                                            // Enregistrer les données si "Remember Me" est coché
                                            if (rememberMeChecked.value) {
                                                scope.launch {
                                                    userStore.saveToken(email)
                                                    Log.d("SignInScreen", "email saved: $email")

                                                    userStore.savePassword(password)
                                                    Log.d("SignInScreen", "Password saved: $password")
                                                }
                                            }
                                            navController.navigate("medicamentScreen/$patientIdFromResponse")
                                        } else {
                                            errorMessage = "L'ID du patient est introuvable."
                                        }
                                    } else {
                                        errorMessage = "Données invalides, veuillez réessayer."
                                    }
                                } else {
                                    errorMessage = when (response.code()) {
                                        404 -> "Compte non trouvé. Inscrivez-vous."
                                        else -> "Erreur de connexion."
                                    }
                                }
                                Log.d("SignInScreen", "Response Code: ${response.code()}")
                            }

                            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                                errorMessage = "Erreur réseau : ${t.message}"
                                Log.e("SignInScreen", "Erreur réseau : ${t.message}", t)
                            }
                        })
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign In")
        }
    }
}
