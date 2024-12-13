package com.example.rawaaproject.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rawaaproject.model.Medicament
import com.example.rawaaproject.viewmodel.MedicamentViewModel
import androidx.compose.foundation.layout.Column as Column1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicamentScreen(navController: NavController, patientId: String) {
    // Accéder au ViewModel
    val medicamentViewModel: MedicamentViewModel = viewModel()

    // État pour gérer la liste des médicaments et les erreurs
    var medicaments by remember { mutableStateOf<List<Medicament>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // État pour gérer l'ouverture du dialog
    var isDialogOpen by remember { mutableStateOf(false) }

    // Récupérer les médicaments au démarrage
    LaunchedEffect(patientId) {
        medicamentViewModel.getMedicaments(patientId, onSuccess = {
            medicaments = it
        }, onFailure = {
            errorMessage = it
        })
    }

    // UI
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Liste des Médicaments") },
                actions = {
                    // Bouton de déconnexion dans la TopAppBar, aligné à droite
                    IconButton(onClick = {
                        // Code de déconnexion (par exemple, nettoyage des données ou de la session)
                        navController.navigate("signInScreen") {
                            // Clear the back stack and navigate to the signInScreen
                            popUpTo("medicamentScreen") { var inclusive = true }
                            launchSingleTop = true // Prevent adding a new instance if already on top
                        }
                    }) {
                        // Icône de déconnexion à droite
                        Icon(Icons.Default.ExitToApp, contentDescription = "Déconnexion")
                    }
                }
            )

        },
        content = { padding ->
            Column1(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Affichage des médicaments
                if (errorMessage != null) {
                    Text(text = errorMessage ?: "Erreur inconnue", color = Color.Red)
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    items(medicaments) { medicament ->
                        MedicamentItem(
                            medicament = medicament,
                            onDelete = {
                                val updatedList = medicaments.filter { it.name != medicament.name }
                                medicaments = updatedList // Mise à jour immédiate de la liste dans l'UI
                                medicamentViewModel.deleteMedicament(patientId, medicament.name, onSuccess = {
                                    // Vous pouvez gérer un rafraîchissement ici si nécessaire
                                }, onFailure = {
                                    errorMessage = it
                                })
                            }
,

                                    onUpdate = { newName, newDosage, newFrequency, newStartDate, newEndDate ->
                                medicamentViewModel.updateMedicament(
                                    patientId,
                                    medicament.name,
                                    Medicament(newName, newDosage, newFrequency, newStartDate, newEndDate),
                                    onSuccess = {
                                        medicamentViewModel.getMedicaments(patientId, onSuccess = {
                                            medicaments = it
                                        }, onFailure = {
                                            errorMessage = it
                                        })
                                    },
                                    onFailure = {
                                        errorMessage = it
                                    })
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Bouton pour ouvrir le Dialog
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { isDialogOpen = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ajouter un médicament")
                }

                // Dialog pour ajouter un médicament
                if (isDialogOpen) {
                    MedicamentDialog(
                        onDismiss = { isDialogOpen = false },
                        onAdd = { name, dosage, frequency, startDate, endDate ->
                            if (name.isNotEmpty() && dosage.isNotEmpty() &&
                                frequency.isNotEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty()) {
                                medicamentViewModel.addMedicament(
                                    patientId,
                                    Medicament(name, dosage, frequency, startDate, endDate),
                                    onSuccess = {
                                        medicamentViewModel.getMedicaments(patientId, onSuccess = {
                                            medicaments = it
                                        }, onFailure = {
                                            errorMessage = it
                                        })
                                        isDialogOpen = false
                                    },
                                    onFailure = { errorMessage = it }
                                )
                            }
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun MedicamentDialog(onDismiss: () -> Unit, onAdd: (String, String, String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    // État pour afficher les messages d'erreur
    var errorMessage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Ajouter un Médicament") },
        text = {
            Column1 {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom du médicament") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
                TextField(
                    value = dosage,
                    onValueChange = { dosage = it },
                    label = { Text("Dosage") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
                TextField(
                    value = frequency,
                    onValueChange = { frequency = it },
                    label = { Text("Fréquence") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
                TextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    label = { Text("Date de début") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
                TextField(
                    value = endDate,
                    onValueChange = { endDate = it },
                    label = { Text("Date de fin") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                // Affichage du message d'erreur s'il y en a
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Validation des champs avant l'ajout
                    if (name.isEmpty() || dosage.isEmpty() || frequency.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                        errorMessage = "Tous les champs doivent être remplis"
                    } else {
                        errorMessage = ""
                        onAdd(name, dosage, frequency, startDate, endDate)
                    }
                }
            ) {
                Text("Ajouter")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}


@Composable
fun MedicamentItem(medicament: Medicament, onDelete: () -> Unit, onUpdate: (String, String, String, String, String) -> Unit) {
    var name by remember { mutableStateOf(medicament.name) }
    var dosage by remember { mutableStateOf(medicament.dosage) }
    var frequency by remember { mutableStateOf(medicament.frequency) }
    var startDate by remember { mutableStateOf(medicament.startDate) }
    var endDate by remember { mutableStateOf(medicament.endDate) }
    var isEditing by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
    ) {
        Column1(modifier = Modifier.padding(16.dp)) {
            if (isEditing) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom du médicament") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
                TextField(
                    value = dosage,
                    onValueChange = { dosage = it },
                    label = { Text("Dosage") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
                TextField(
                    value = frequency,
                    onValueChange = { frequency = it },
                    label = { Text("Fréquence") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
                TextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    label = { Text("Date de début") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
                TextField(
                    value = endDate,
                    onValueChange = { endDate = it },
                    label = { Text("Date de fin") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
                Row {
                    Button(
                        onClick = {
                            onUpdate(name, dosage, frequency, startDate, endDate)
                            isEditing = false
                        }
                    ) {
                        Text("Enregistrer")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { isEditing = false }
                    ) {
                        Text("Annuler")
                    }
                }
            } else {
                Text(text = "Nom: $name", style = MaterialTheme.typography.headlineMedium)
                Text(text = "Dosage: $dosage", style = MaterialTheme.typography.headlineMedium)
                Text(text = "Fréquence: $frequency", style = MaterialTheme.typography.headlineSmall)
                Text(text = "Date de début: $startDate", style = MaterialTheme.typography.headlineMedium)
                Text(text = "Date de fin: $endDate", style = MaterialTheme.typography.headlineMedium)
                Row {
                    Button(onClick = { isEditing = true }) {
                        Text("Modifier")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onDelete) {
                        Text("Supprimer")
                    }
                }
            }
        }
    }
}
