package com.cognitech.mindflow.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
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
import com.cognitech.mindflow.ui.components.LabeledInput
import com.cognitech.mindflow.ui.components.MindFlowLogo
import com.cognitech.mindflow.ui.theme.CornflowerBlue
import com.cognitech.mindflow.ui.theme.Downy
import com.cognitech.mindflow.ui.theme.Gray
import com.cognitech.mindflow.ui.theme.MineShaft
import com.cognitech.mindflow.ui.theme.Silver
import com.cognitech.mindflow.ui.theme.SunsetOrange
import com.cognitech.mindflow.ui.theme.White

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegistered: () -> Unit,
    onGoToLogin: () -> Unit,
    onGoogleClick: () -> Unit,
) {
    val state = viewModel.state
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = screenHeight - 48.dp)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            MindFlowLogo()
            Spacer(Modifier.height(32.dp))
            Text("Crea tu cuenta gratis", color = MineShaft, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(
                "Únete a la plataforma que democratiza el acceso a herramientas de bienestar emocional con IA.",
                color = Gray,
                fontSize = 15.2.sp,
            )
            Spacer(Modifier.height(32.dp))
            GoogleButton("Registrarse con Google", onClick = onGoogleClick)
            Spacer(Modifier.height(24.dp))
            DividerWithText("o usar correo electrónico")
            Spacer(Modifier.height(24.dp))
            LabeledInput(
                label = "Nombre Completo",
                value = state.name,
                onValueChange = viewModel::onNameChange,
                placeholder = "Ej. Alex Developer",
                error = state.nameError,
            )
            Spacer(Modifier.height(19.2.dp))
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
                label = "Crea una contraseña",
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                placeholder = "Mínimo 8 caracteres",
                isPassword = true,
                error = state.passwordError,
            )
            Spacer(Modifier.height(19.2.dp))
            Text(
                buildAnnotatedString {
                    append("Al registrarte, aceptas nuestros ")
                    withStyle(SpanStyle(color = CornflowerBlue, textDecoration = TextDecoration.Underline)) {
                        append("Términos de Servicio")
                    }
                    append(" y ")
                    withStyle(SpanStyle(color = CornflowerBlue, textDecoration = TextDecoration.Underline)) {
                        append("Política de Privacidad")
                    }
                    append(" (Datos encriptados con AES-256).")
                },
                color = Gray,
                fontSize = 12.sp,
                lineHeight = 16.8.sp,
            )
            if (state.generalError != null) {
                Text(state.generalError, color = SunsetOrange, fontSize = 13.6.sp, modifier = Modifier.padding(top = 12.dp))
            }
            Spacer(Modifier.height(19.2.dp))
            GradientButton(
                text = "Crear mi cuenta",
                onClick = { viewModel.signUp(onRegistered) },
                loading = state.loading,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(24.dp))
            Text(
                buildAnnotatedString {
                    append("¿Ya tienes una cuenta? ")
                    withStyle(SpanStyle(color = CornflowerBlue, fontWeight = FontWeight.SemiBold)) {
                        append("Inicia sesión aquí")
                    }
                },
                color = Gray,
                fontSize = 14.4.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable(onClick = onGoToLogin),
            )
        }

        RegisterFeaturesPanel()
    }
}

/** Bloque inferior del Registro: fondo #2F2F2F con las 3 funcionalidades principales. */
@Composable
private fun RegisterFeaturesPanel() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 600.dp)
            .background(MineShaft)
            .background(
                Brush.radialGradient(
                    0f to CornflowerBlue.copy(alpha = 0.1f),
                    0.7f to CornflowerBlue.copy(alpha = 0f),
                    center = Offset(150f, 170f),
                    radius = 600f,
                )
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        Column(
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 64.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            Text(
                "Empodera tu mente.\nTransforma tus días.",
                color = White,
                fontSize = 35.2.sp,
                lineHeight = 42.24.sp,
                fontWeight = FontWeight.Bold,
            )
            Feature("🧠", "AI Mood Journal", "Procesamos tus registros para ofrecerte contención empática y validación en tiempo real.")
            Feature("📊", "Dynamic Habit Tracker", "Un gestor de tareas que entiende tu nivel de estrés y ajusta la exigencia para evitar el burnout.")
            Feature("🌬️", "Smart Interventions", "Recibe sugerencias automáticas de micro-meditaciones y pausas justo cuando más lo necesitas.")
        }
    }
}

@Composable
private fun Feature(emoji: String, title: String, description: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
        Box(
            Modifier
                .size(40.dp)
                .background(White.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(emoji, fontSize = 19.2.sp)
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.3.dp)) {
            Text(title, color = Downy, fontSize = 17.6.sp, fontWeight = FontWeight.Bold)
            Text(description, color = Silver, fontSize = 14.4.sp, lineHeight = 21.6.sp)
        }
        Spacer(Modifier.width(0.dp))
    }
}
