package com.cognitech.mindflow.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cognitech.mindflow.data.model.JournalCategories
import com.cognitech.mindflow.data.model.JournalEntry
import com.cognitech.mindflow.ui.components.Chip
import com.cognitech.mindflow.ui.components.GradientAvatar
import com.cognitech.mindflow.ui.components.GradientButton
import com.cognitech.mindflow.ui.components.MindFlowLogo
import com.cognitech.mindflow.ui.components.mindFlowFieldColors
import com.cognitech.mindflow.ui.theme.AiCardBackground
import com.cognitech.mindflow.ui.theme.CardBackground
import com.cognitech.mindflow.ui.theme.ChipBackground
import com.cognitech.mindflow.ui.theme.FieldBackground
import com.cognitech.mindflow.ui.theme.FieldBorder
import com.cognitech.mindflow.ui.theme.LinkBlue
import com.cognitech.mindflow.ui.theme.MindBlue
import com.cognitech.mindflow.ui.theme.MindGreen
import com.cognitech.mindflow.ui.theme.ScreenBackground
import com.cognitech.mindflow.ui.theme.TextHint
import com.cognitech.mindflow.ui.theme.TextPrimary
import com.cognitech.mindflow.ui.theme.TextSecondary
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val SpanishLocale = Locale("es", "PE")
private const val RECENT_LIMIT = 3

@Composable
fun HomeScreen(viewModel: HomeViewModel, onLogout: () -> Unit) {
    val state = viewModel.state
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val firstName = state.user?.name?.substringBefore(" ").orEmpty().ifBlank { "Usuario" }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = Color.White) {
                Column(Modifier.padding(24.dp)) {
                    MindFlowLogo()
                    Spacer(Modifier.height(4.dp))
                    Text(state.user?.email.orEmpty(), color = TextSecondary, fontSize = 13.sp)
                }
                HorizontalDivider(color = FieldBorder)
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                    selected = true,
                    onClick = { scope.launch { drawerState.close() } },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                )
                NavigationDrawerItem(
                    label = { Text("Cerrar sesión") },
                    icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) },
                    selected = false,
                    onClick = {
                        viewModel.logout()
                        onLogout()
                    },
                    modifier = Modifier.padding(horizontal = 12.dp),
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ScreenBackground)
                .systemBarsPadding()
                .imePadding(),
        ) {
            Header(
                firstName = firstName,
                onMenuClick = { scope.launch { drawerState.open() } },
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            ) {
                MoodCard(
                    draft = state.draft,
                    category = state.category,
                    saving = state.saving,
                    lastAiResponse = state.lastAiResponse,
                    onDraftChange = viewModel::onDraftChange,
                    onCategoryChange = viewModel::onCategoryChange,
                    onSave = viewModel::save,
                )
                Spacer(Modifier.height(16.dp))
                RecentConversations(
                    entries = state.entries,
                    showAll = state.showAllHistory,
                    onToggle = viewModel::toggleHistory,
                )
            }
        }
    }
}

@Composable
private fun Header(firstName: String, onMenuClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onMenuClick) {
            Icon(Icons.Filled.Menu, contentDescription = "Menú", tint = TextPrimary)
        }
        Column(Modifier.weight(1f)) {
            Text("Hola, $firstName", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(
                "${todayLabel()} · Tu IA está lista para escucharte.",
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 14.sp,
            )
        }
        GradientAvatar(firstName.take(1).uppercase(), Modifier.padding(horizontal = 8.dp))
    }
    HorizontalDivider(color = FieldBorder)
}

@Composable
private fun MoodCard(
    draft: String,
    category: String,
    saving: Boolean,
    lastAiResponse: String?,
    onDraftChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onSave: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardBackground, RoundedCornerShape(12.dp))
            .border(1.dp, FieldBorder, RoundedCornerShape(12.dp))
            .padding(16.dp),
    ) {
        Text("¿Cómo te sientes en este momento?", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .background(ChipBackground, RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier
                    .size(6.dp)
                    .background(MindGreen, CircleShape)
            )
            Spacer(Modifier.width(4.dp))
            Text("MindFlow AI Activa", color = MindBlue, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = draft,
            onValueChange = onDraftChange,
            placeholder = {
                Text(
                    "Escribe aquí tus pensamientos. Este es un espacio seguro y encriptado...",
                    color = TextHint,
                    fontSize = 13.sp,
                )
            },
            shape = RoundedCornerShape(8.dp),
            colors = mindFlowFieldColors(),
            textStyle = TextStyle(fontSize = 14.sp, color = TextPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 110.dp),
        )
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
        ) {
            CategorySelector(category, onCategoryChange, Modifier.weight(1f).fillMaxHeight())
            GradientButton(
                text = "Guardar Registro",
                onClick = onSave,
                enabled = draft.isNotBlank(),
                loading = saving,
                height = 40,
                fontSize = 13,
                modifier = Modifier.weight(1f),
            )
        }
        Spacer(Modifier.height(16.dp))
        AiResponseCard(lastAiResponse)
    }
}

@Composable
private fun CategorySelector(category: String, onCategoryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(FieldBackground, RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                .clickable { expanded = true }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(category, color = TextPrimary, fontSize = 13.sp, modifier = Modifier.weight(1f))
            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Categoría", tint = TextSecondary)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            JournalCategories.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onCategoryChange(option)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun AiResponseCard(response: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(AiCardBackground, RoundedCornerShape(8.dp)),
    ) {
        Box(
            Modifier
                .width(3.dp)
                .fillMaxHeight()
                .background(MindGreen, RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
        )
        Column(Modifier.padding(14.dp)) {
            Text("✨ MindFlow AI", color = LinkBlue, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text(
                response ?: "Hola, estoy aquí para escucharte. Cuéntame cómo te sientes hoy y te daré una respuesta personalizada.",
                color = TextPrimary,
                fontSize = 13.sp,
                lineHeight = 19.sp,
            )
        }
    }
}

@Composable
private fun RecentConversations(entries: List<JournalEntry>, showAll: Boolean, onToggle: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardBackground, RoundedCornerShape(12.dp))
            .border(1.dp, FieldBorder, RoundedCornerShape(12.dp))
            .padding(16.dp),
    ) {
        Text("Conversaciones Recientes", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        if (entries.size > RECENT_LIMIT) {
            Text(
                if (showAll) "Ver solo las recientes" else "Ver historial completo de conversaciones",
                color = LinkBlue,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable(onClick = onToggle),
            )
        }
        Spacer(Modifier.height(12.dp))
        if (entries.isEmpty()) {
            Text(
                "Aún no tienes registros. Escribe cómo te sientes para empezar.",
                color = TextSecondary,
                fontSize = 13.sp,
            )
        }
        val visible = if (showAll) entries else entries.take(RECENT_LIMIT)
        visible.forEachIndexed { index, entry ->
            if (index > 0) HorizontalDivider(color = FieldBorder, modifier = Modifier.padding(vertical = 10.dp))
            EntryRow(entry)
        }
    }
}

@Composable
private fun EntryRow(entry: JournalEntry) {
    Column(Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(relativeTime(entry.createdAt), color = TextSecondary, fontSize = 11.sp, modifier = Modifier.weight(1f))
            Chip(entry.category, ChipBackground, MindBlue)
        }
        Spacer(Modifier.height(4.dp))
        Text(
            entry.content,
            color = TextPrimary,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private fun todayLabel(): String =
    SimpleDateFormat("EEEE, d 'de' MMMM", SpanishLocale).format(Date())
        .split(" ")
        .joinToString(" ") { word -> if (word == "de") word else word.replaceFirstChar { it.titlecase(SpanishLocale) } }

private fun relativeTime(timestamp: Long): String {
    val diffMinutes = (System.currentTimeMillis() - timestamp) / 60_000
    val time = SimpleDateFormat("h:mm a", Locale.US).format(Date(timestamp))
    return when {
        diffMinutes < 1 -> "Justo ahora"
        diffMinutes < 60 -> "Hace $diffMinutes min"
        diffMinutes < 24 * 60 && isSameDay(timestamp, 0) -> "Hace ${diffMinutes / 60} " + if (diffMinutes / 60 == 1L) "hora" else "horas"
        isSameDay(timestamp, -1) -> "Ayer, $time"
        else -> SimpleDateFormat("d MMM, ", SpanishLocale).format(Date(timestamp)) + time
    }
}

private fun isSameDay(timestamp: Long, dayOffset: Int): Boolean {
    val target = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, dayOffset) }
    val other = Calendar.getInstance().apply { timeInMillis = timestamp }
    return target.get(Calendar.YEAR) == other.get(Calendar.YEAR) &&
        target.get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR)
}
