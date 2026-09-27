package com.cognitech.mindflow.ui.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cognitech.mindflow.MindFlowApplication
import com.cognitech.mindflow.ui.auth.AuthViewModel
import com.cognitech.mindflow.ui.auth.LoginScreen
import com.cognitech.mindflow.ui.auth.RegisterScreen
import com.cognitech.mindflow.ui.home.HomeScreen
import com.cognitech.mindflow.ui.home.HomeViewModel

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
}

@Composable
fun AppNavigation(app: MindFlowApplication, navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val googleNotAvailable = {
        Toast.makeText(context, "El acceso con Google estará disponible pronto", Toast.LENGTH_SHORT).show()
    }
    val authFactory = viewModelFactory { initializer { AuthViewModel(app.authRepository) } }
    val start = if (app.authRepository.isLoggedIn()) Routes.HOME else Routes.LOGIN

    NavHost(navController = navController, startDestination = start) {
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = viewModel(factory = authFactory),
                onLoggedIn = { navController.goHome() },
                onGoToRegister = { navController.navigate(Routes.REGISTER) { launchSingleTop = true } },
                onGoogleClick = googleNotAvailable,
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel = viewModel(factory = authFactory),
                onRegistered = { navController.goHome() },
                onGoToLogin = {
                    if (!navController.popBackStack(Routes.LOGIN, inclusive = false)) {
                        navController.navigate(Routes.LOGIN) { popUpTo(0) }
                    }
                },
                onGoogleClick = googleNotAvailable,
            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                viewModel = viewModel(
                    factory = viewModelFactory { initializer { HomeViewModel(app.authRepository, app.journalRepository) } },
                ),
                onLogout = { navController.navigate(Routes.LOGIN) { popUpTo(0) } },
            )
        }
    }
}

private fun NavHostController.goHome() = navigate(Routes.HOME) { popUpTo(0) }
