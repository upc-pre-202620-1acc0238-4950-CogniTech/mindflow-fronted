package com.cognitech.mindflow.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cognitech.mindflow.ui.components.DividerWithText
import com.cognitech.mindflow.ui.components.GoogleButton
import com.cognitech.mindflow.ui.components.GradientButton
import com.cognitech.mindflow.ui.components.LabeledTextField
import com.cognitech.mindflow.ui.components.MindFlowLogo
import com.cognitech.mindflow.ui.theme.ErrorRed
import com.cognitech.mindflow.ui.theme.LinkBlue
import com.cognitech.mindflow.ui.theme.TextPrimary
import com.cognitech.mindflow.ui.theme.TextSecondary

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoggedIn: () -> Unit,
    onGoToRegister: () -> Unit,
    onGoogleClick: () -> Unit,
) {
    val state = viewModel.state
    var showForgotDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(Modifier.height(48.dp))
        MindFlowLogo()
        Spacer(Modifier.height(28.dp))
        Text("Bienvenido de nuevo", color = TextPrimary, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(
            "Tu espacio seguro para la reflexión y el crecimiento personal.",
            color = TextSecondary,
            fontSize = 14.sp,
        )
        Spacer(Modifier.height(24.dp))
        GoogleButton("Iniciar sesión con Google", onClick = onGoogleClick)
        Spacer(Modifier.height(20.dp))
        DividerWithText("o ingresa con tu correo")
        Spacer(Modifier.height(20.dp))
        LabeledTextField(
            label = "Correo Electrónico",
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            placeholder = "ejemplo@correo.com",
            keyboardType = KeyboardType.Email,
            error = state.emailError,
        )
        Spacer(Modifier.height(16.dp))
        LabeledTextField(
            label = "Contraseña",
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            placeholder = "••••••••",
            isPassword = true,
            error = state.passwordError,
        )
        Text(
            "¿Olvidaste tu contraseña?",
            color = LinkBlue,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 8.dp)
                .clickable { showForgotDialog = true },
        )
        if (state.generalError != null) {
            Text(state.generalError, color = ErrorRed, fontSize = 13.sp, modifier = Modifier.padding(top = 12.dp))
        }
        Spacer(Modifier.height(24.dp))
        GradientButton(
            text = "Ingresar a MindFlow",
            onClick = { viewModel.signIn(onLoggedIn) },
            loading = state.loading,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(20.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text("¿Aún no tienes una cuenta? ", color = TextSecondary, fontSize = 13.sp)
            Text(
                "Regístrate gratis",
                color = LinkBlue,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(onClick = onGoToRegister),
            )
        }
        Spacer(Modifier.height(32.dp))
    }

    if (showForgotDialog) {
        AlertDialog(
            onDismissRequest = { showForgotDialog = false },
            confirmButton = { TextButton(onClick = { showForgotDialog = false }) { Text("Entendido") } },
            title = { Text("Recuperar contraseña") },
            text = { Text("La recuperación de contraseña por correo estará disponible cuando la app se conecte al servidor de MindFlow.") },
        )
    }
}
