package com.cognitech.mindflow.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cognitech.mindflow.R
import com.cognitech.mindflow.ui.theme.ErrorRed
import com.cognitech.mindflow.ui.theme.FieldBackground
import com.cognitech.mindflow.ui.theme.FieldBorder
import com.cognitech.mindflow.ui.theme.MindBlue
import com.cognitech.mindflow.ui.theme.MindGradient
import com.cognitech.mindflow.ui.theme.TextHint
import com.cognitech.mindflow.ui.theme.TextPrimary
import com.cognitech.mindflow.ui.theme.TextSecondary

@Composable
fun MindFlowLogo(modifier: Modifier = Modifier) {
    Text(
        text = "MindFlow",
        modifier = modifier,
        style = TextStyle(
            brush = MindGradient,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        ),
    )
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    height: Int = 48,
    fontSize: Int = 15,
) {
    val shape = RoundedCornerShape(8.dp)
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
        ),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp),
        modifier = modifier
            .height(height.dp)
            .alpha(if (enabled) 1f else 0.6f)
            .background(MindGradient, shape),
    ) {
        if (loading) {
            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
        } else {
            Text(text, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = fontSize.sp)
        }
    }
}

@Composable
fun GoogleButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, FieldBorder),
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
    ) {
        Image(painter = painterResource(R.drawable.ic_google), contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(10.dp))
        Text(text, color = TextPrimary, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}

@Composable
fun DividerWithText(text: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        HorizontalDivider(Modifier.weight(1f), color = FieldBorder)
        Text(text, color = TextSecondary, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 12.dp))
        HorizontalDivider(Modifier.weight(1f), color = FieldBorder)
    }
}

@Composable
fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    error: String? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(label, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = TextHint, fontSize = 14.sp) },
            singleLine = true,
            isError = error != null,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else keyboardType),
            shape = RoundedCornerShape(8.dp),
            colors = mindFlowFieldColors(),
            textStyle = TextStyle(fontSize = 14.sp, color = TextPrimary),
            modifier = Modifier.fillMaxWidth(),
        )
        if (error != null) {
            Text(error, color = ErrorRed, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
fun mindFlowFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = FieldBackground,
    unfocusedContainerColor = FieldBackground,
    errorContainerColor = FieldBackground,
    focusedBorderColor = MindBlue,
    unfocusedBorderColor = FieldBorder,
    cursorColor = MindBlue,
)

@Composable
fun GradientAvatar(initial: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(40.dp)
            .background(MindGradient, androidx.compose.foundation.shape.CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(initial, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun Chip(text: String, background: Color, contentColor: Color, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(background, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text, color = contentColor, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}
