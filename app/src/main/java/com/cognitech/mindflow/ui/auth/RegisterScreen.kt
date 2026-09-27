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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
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
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegistered: () -> Unit,
    onGoToLogin: () -> Unit,
    onGoogleClick: () -> Unit,
) {
    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
    ) {
        Spacer(Modifier.height(40.dp))
        MindFlowLogo()
        Spacer(Modifier.height(24.dp))
        Text("Crea tu cuenta gratis", color = TextPrimary, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(
            "Únete a la plataforma que democratiza el acceso a herramientas de bienestar emocional con IA.",
            color = TextSecondary,
            fontSize = 14.sp,
        )
        Spacer(Modifier.height(24.dp))
        GoogleButton("Registrarse con Google", onClick = onGoogleClick)
        Spacer(Modifier.height(20.dp))
        DividerWithText("o usar correo electrónico")
        Spacer(Modifier.height(20.dp))
        LabeledTextField(
            label = "Nombre Completo",
            value = state.name,
            onValueChange = viewModel::onNameChange,
            placeholder = "Ej. Alex Developer",
            error = state.nameError,
        )
        Spacer(Modifier.height(16.dp))
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
            label = "Crea una contraseña",
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            placeholder = "Mínimo 8 caracteres",
            isPassword = true,
            error = state.passwordError,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            buildAnnotatedString {
                append("Al registrarte, aceptas nuestros ")
                withStyle(SpanStyle(color = LinkBlue, textDecoration = TextDecoration.Underline)) {
                    append("Términos de Servicio")
                }
                append(" y ")
                withStyle(SpanStyle(color = LinkBlue, textDecoration = TextDecoration.Underline)) {
                    append("Política de Privacidad")
                }
                append(" (Datos encriptados con AES-256).")
            },
            color = TextSecondary,
            fontSize = 11.sp,
            lineHeight = 16.sp,
        )
        if (state.generalError != null) {
            Text(state.generalError, color = ErrorRed, fontSize = 13.sp, modifier = Modifier.padding(top = 12.dp))
        }
        Spacer(Modifier.height(24.dp))
        GradientButton(
            text = "Crear mi cuenta",
            onClick = { viewModel.signUp(onRegistered) },
            loading = state.loading,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(20.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text("¿Ya tienes una cuenta? ", color = TextSecondary, fontSize = 13.sp)
            Text(
                "Inicia sesión aquí",
                color = LinkBlue,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(onClick = onGoToLogin),
            )
        }
        Spacer(Modifier.height(32.dp))
    }
}
