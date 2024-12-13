package com.example.rawaaproject.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rawaaproject.model.Patient
import com.example.rawaaproject.viewmodel.PatientViewModel

@Composable
fun PatientScreen(navController: NavController, viewModel: PatientViewModel = viewModel()) {
    Row(
        modifier = Modifier.fillMaxWidth(), // Ensures the Row takes full width
        horizontalArrangement = Arrangement.End // Aligns children to the end (right)
    ) {
        IconButton(
            onClick = {
                // Handle log out here, like clearing session or token
                // Navigate to the Login Screen (SIN)
                navController.navigate("signInScreen") {
                    // Pop up the current screen from the back stack to avoid going back to it
                    popUpTo("medicamentScreen") { inclusive = true }
                }
            },
            modifier = Modifier.padding(top = 16.dp) // Adds top padding
        ) {
            Icon(
                imageVector = Icons.Filled.ExitToApp, // Use the exit icon from the Material icons
                contentDescription = "Déconnexion"
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getAllPatients()
    }

    // Observe the patients list and error state from the ViewModel
    val patients by viewModel.patients.observeAsState(emptyList())
    val error by viewModel.error.observeAsState("")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Display any error message
        if (error.isNotEmpty()) {
            Text(
                text = "Error: $error",
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Add Patient Form
        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var mdp by remember { mutableStateOf("") }
        var age by remember { mutableStateOf("") }
        var address by remember { mutableStateOf("") }
        var num by remember { mutableStateOf("") }

        fun clearFields() {
            name = ""
            email = ""
            mdp = ""
            age = ""
            address = ""
            num = ""
        }

        Text("Add New Patient", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(Modifier.padding(16.dp)) {
                    if (name.isEmpty()) Text("Name")
                    innerTextField()
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(Modifier.padding(16.dp)) {
                    if (email.isEmpty()) Text("Email")
                    innerTextField()
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = mdp,
            onValueChange = { mdp = it },
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(Modifier.padding(16.dp)) {
                    if (mdp.isEmpty()) Text("Password")
                    innerTextField()
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = age,
            onValueChange = { age = it },
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(Modifier.padding(16.dp)) {
                    if (age.isEmpty()) Text("Age")
                    innerTextField()
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = address,
            onValueChange = { address = it },
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(Modifier.padding(16.dp)) {
                    if (address.isEmpty()) Text("Address")
                    innerTextField()
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = num,
            onValueChange = { num = it },
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(Modifier.padding(16.dp)) {
                    if (num.isEmpty()) Text("Number")
                    innerTextField()
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val patient = Patient(
                    name = name,
                    email = email,
                    mdp = mdp,
                    age = age.toIntOrNull() ?: 0,
                    address = address,
                    num = num.toIntOrNull() ?: 0,  // Ensure num is an integer
                    medicaments = emptyList()  // assuming empty list for now
                )
                viewModel.createPatient(patient)
                clearFields()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Patient")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display list of patients
        Text("Patient List", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            this.items(patients) { patient ->
                PatientItem(patient, viewModel)
            }
        }
    }
}

@Composable
fun PatientItem(patient: Patient, viewModel: PatientViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    // Dialog state for editing
    var name by remember { mutableStateOf(patient.name) }
    var email by remember { mutableStateOf(patient.email) }
    var mdp by remember { mutableStateOf(patient.mdp) }
    var age by remember { mutableStateOf(patient.age.toString()) }
    var address by remember { mutableStateOf(patient.address) }
    var num by remember { mutableStateOf(patient.num.toString()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Text("Name: ${patient.name}")
        Text("Email: ${patient.email}")
        Text("Age: ${patient.age}")
        Text("Address: ${patient.address}")
        Text("Number: ${patient.num}")

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                // Logic for deleting a patient
                patient.id?.let { viewModel.deletePatient(it) }
            }) {
                Text("Delete")
            }

            Button(onClick = {
                // Open the update dialog
                showDialog = true
            }) {
                Text("Update")
            }
        }
    }

    // Update Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Update Patient") },
            text = {
                Column {
                    BasicTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            Box(Modifier.padding(16.dp)) {
                                if (name.isEmpty()) Text("Name")
                                innerTextField()
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    BasicTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            Box(Modifier.padding(16.dp)) {
                                if (email.isEmpty()) Text("Email")
                                innerTextField()
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    BasicTextField(
                        value = mdp,
                        onValueChange = { mdp = it },
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            Box(Modifier.padding(16.dp)) {
                                if (mdp.isEmpty()) Text("Password")
                                innerTextField()
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    BasicTextField(
                        value = age,
                        onValueChange = { age = it },
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            Box(Modifier.padding(16.dp)) {
                                if (age.isEmpty()) Text("Age")
                                innerTextField()
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    BasicTextField(
                        value = address,
                        onValueChange = { address = it },
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            Box(Modifier.padding(16.dp)) {
                                if (address.isEmpty()) Text("Address")
                                innerTextField()
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    BasicTextField(
                        value = num,
                        onValueChange = { num = it },
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            Box(Modifier.padding(16.dp)) {
                                if (num.isEmpty()) Text("Number")
                                innerTextField()
                            }
                        }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val updatedPatient = patient.copy(
                        name = name,
                        email = email,
                        mdp = mdp,
                        age = age.toIntOrNull() ?: 0,
                        address = address,
                        num = num.toIntOrNull() ?: 0
                    )
                    // Update the patient via ViewModel
                    patient.id?.let { viewModel.updatePatient(it, updatedPatient) }
                    showDialog = false
                }) {
                    Text("Update")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
