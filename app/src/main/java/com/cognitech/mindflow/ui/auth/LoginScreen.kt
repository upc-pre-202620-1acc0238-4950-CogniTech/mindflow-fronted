package com.cognitech.mindflow.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cognitech.mindflow.ui.components.DividerWithText
import com.cognitech.mindflow.ui.components.GoogleButton
import com.cognitech.mindflow.ui.components.GradientButton
import com.cognitech.mindflow.ui.components.LabeledInput
import com.cognitech.mindflow.ui.components.MindFlowLogo
import com.cognitech.mindflow.ui.theme.CornflowerBlue
import com.cognitech.mindflow.ui.theme.Gray
import com.cognitech.mindflow.ui.theme.MindGradient
import com.cognitech.mindflow.ui.theme.MineShaft
import com.cognitech.mindflow.ui.theme.SunsetOrange
import com.cognitech.mindflow.ui.theme.White

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoggedIn: () -> Unit,
    onGoToRegister: () -> Unit,
    onGoogleClick: () -> Unit,
) {
    val state = viewModel.state
    var showForgotDialog by remember { mutableStateOf(false) }
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState()),
    ) {
        // Formulario (alto de pantalla completa, centrado verticalmente como en el Figma)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = screenHeight - 48.dp)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            MindFlowLogo()
            Spacer(Modifier.height(32.dp))
            Text("Bienvenido de nuevo", color = MineShaft, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(
                "Tu espacio seguro para la reflexión y el crecimiento personal.",
                color = Gray,
                fontSize = 15.2.sp,
            )
            Spacer(Modifier.height(32.dp))
            GoogleButton("Iniciar sesión con Google", onClick = onGoogleClick)
            Spacer(Modifier.height(24.dp))
            DividerWithText("o ingresa con tu correo")
            Spacer(Modifier.height(24.dp))
            LabeledInput(
                label = "Correo Electrónico",
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                placeholder = "ejemplo@correo.com",
                keyboardType = KeyboardType.Email,
                error = state.emailError,
            )
            Spacer(Modifier.height(19.2.dp))
            LabeledInput(
                label = "Contraseña",
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                placeholder = "••••••••",
                isPassword = true,
                error = state.passwordError,
            )
            Spacer(Modifier.height(11.2.dp))
            Text(
                "¿Olvidaste tu contraseña?",
                color = CornflowerBlue,
                fontSize = 13.6.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { showForgotDialog = true },
            )
            if (state.generalError != null) {
                Text(state.generalError, color = SunsetOrange, fontSize = 13.6.sp, modifier = Modifier.padding(top = 12.dp))
            }
            Spacer(Modifier.height(24.dp))
            GradientButton(
                text = "Ingresar a MindFlow",
                onClick = { viewModel.signIn(onLoggedIn) },
                loading = state.loading,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(24.dp))
            Text(
                buildAnnotatedString {
                    append("¿Aún no tienes una cuenta? ")
                    withStyle(SpanStyle(color = CornflowerBlue, fontWeight = FontWeight.SemiBold)) {
                        append("Regístrate gratis")
                    }
                },
                color = Gray,
                fontSize = 14.4.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable(onClick = onGoToRegister),
            )
        }

        LoginTestimonialPanel()
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

/** Bloque inferior del Login: fondo degradado, "Retoma el control de tu calma interior." y testimonio. */
@Composable
private fun LoginTestimonialPanel() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .background(MindGradient)
            .clipToBounds(),
    ) {
        Box(
            Modifier
                .offset(x = maxWidth * 0.52f, y = (-60).dp)
                .size(width = maxWidth * 0.58f, height = 330.dp)
                .blur(20.dp)
                .background(White.copy(alpha = 0.1f), RoundedCornerShape(200.dp))
        )
        Box(
            Modifier
                .offset(x = -maxWidth * 0.1f, y = 460.dp)
                .size(width = maxWidth * 0.43f, height = 200.dp)
                .blur(25.dp)
                .background(White.copy(alpha = 0.15f), RoundedCornerShape(150.dp))
        )
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Text(
                "Retoma el control de tu calma interior.",
                color = White,
                fontSize = 40.sp,
                lineHeight = 48.sp,
                fontWeight = FontWeight.Bold,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                    .border(1.dp, White.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                    .padding(33.dp),
            ) {
                Text(
                    "\"MindFlow no solo es un diario, es como tener un asistente emocional. La retroalimentación de la IA me ha ayudado a reducir mis picos de ansiedad durante los cierres de proyecto.\"",
                    color = White,
                    fontSize = 19.2.sp,
                    lineHeight = 30.72.sp,
                    fontStyle = FontStyle.Italic,
                )
            }
        }
    }
}
