package com.example.rawaaproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rawaaproject.data.UserStore
import com.example.rawaaproject.ui.theme.RawaaprojectTheme
import com.example.rawaaproject.view.PatientScreen
import com.example.rawaaproject.view.SignInScreen
import com.example.rawaaproject.ui.screens.MedicamentScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Créer une instance de UserStore
        val userStore = UserStore(context = this)

        setContent {
            RawaaprojectTheme {
                val navController = rememberNavController()

                Surface(color = MaterialTheme.colorScheme.background) {
                    // Setup Navigation
                    NavHost(navController = navController, startDestination = "signInScreen") {
                        composable("signInScreen") {
                            SignInScreen(navController = navController, userStore = userStore)
                        }
                        composable("patientScreen") {
                            PatientScreen(navController = navController)  // Écran Admin
                        }
                        composable("medicamentScreen/{patientId}") { backStackEntry ->
                            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
                            MedicamentScreen(navController = navController, patientId = patientId)
                        }
                    }
                }
            }
        }
    }
}
