package com.example.smartgym.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartgym.ui.theme.DarkRed
import com.example.smartgym.ui.theme.PrimaryBlack
import com.example.smartgym.viewmodel.profile.ProfileEvent
import com.example.smartgym.viewmodel.profile.ProfileUiEvent
import com.example.smartgym.viewmodel.profile.ProfileViewModel
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val eventFlow = viewModel.eventFlow

    val context = LocalContext.current

    var isEditing by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = eventFlow) {
        eventFlow.collect { event ->
            when (event) {
                is ProfileUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    if (event.message.contains("sucesso")) {
                        isEditing = false
                    }
                }
            }
        }
    }

    val imc = remember(uiState.weight, uiState.height) {
        val weightKg = uiState.weight.toDoubleOrNull() ?: 0.0
        val heightM = uiState.height.toDoubleOrNull() ?: 0.0
        if (weightKg > 0 && heightM > 0) {
            val value = weightKg / (heightM * heightM)
            DecimalFormat("#.#").format(value)
        } else {
            "--"
        }
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = DarkRed,
        unfocusedBorderColor = Color.Gray,
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.Gray,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = DarkRed,
        disabledBorderColor = Color.Gray.copy(alpha = 0.5f),
        disabledLabelColor = Color.Gray,
        disabledTextColor = Color.White.copy(alpha = 0.8f)
    )

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(PrimaryBlack, DarkRed.copy(alpha = 0.3f))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Meu Perfil", color = Color.White, fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    actions = {
                        IconButton(onClick = {
                            if (isEditing) {
                                viewModel.onEvent(ProfileEvent.OnCancelEdit)
                            }
                            isEditing = !isEditing
                        }) {
                            Icon(
                                imageVector = if (isEditing) Icons.Default.Close else Icons.Default.Edit,
                                contentDescription = if (isEditing) "Cancelar Edição" else "Editar Perfil",
                                tint = Color.White
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = DarkRed)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AccountCircle,
                            contentDescription = "Foto do Perfil",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = uiState.name,
                                onValueChange = { viewModel.onEvent(ProfileEvent.OnNameChanged(it)) },
                                label = { Text("Nome") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = textFieldColors,
                                singleLine = true,
                                readOnly = !isEditing
                            )
                            OutlinedTextField(
                                value = uiState.age,
                                onValueChange = { newAge ->
                                    if (newAge.length <= 3 && newAge.all { it.isDigit() }) {
                                        viewModel.onEvent(ProfileEvent.OnAgeChanged(newAge))
                                    }
                                },
                                label = { Text("Idade") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = textFieldColors,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                readOnly = !isEditing
                            )
                        }
                    }

                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = {},
                        label = { Text("E-mail") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors,
                        singleLine = true,
                        readOnly = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Email,
                                contentDescription = "Email",
                                tint = Color.Gray
                            )
                        }
                    )

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        // --- 🎨 COR ALTERADA DE VOLTA 🎨 ---
                        colors = CardDefaults.cardColors(containerColor = PrimaryBlack.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                OutlinedTextField(
                                    value = uiState.weight,
                                    onValueChange = { viewModel.onEvent(ProfileEvent.OnWeightChanged(it)) },
                                    label = { Text("Peso (kg)") },
                                    modifier = Modifier.weight(1f),
                                    colors = textFieldColors,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    readOnly = !isEditing // Só é editável se isEditing = true
                                )
                                OutlinedTextField(
                                    value = uiState.height,
                                    // Validação: Permite n.nn (ex: 1.80)
                                    onValueChange = { newHeight ->
                                        // Regex simples para formato de altura (ex: 1.75, 2.0, 1)
                                        if (newHeight.matches(Regex("^\\d{0,1}(\\.\\d{0,2})?\$"))) {
                                            viewModel.onEvent(ProfileEvent.OnHeightChanged(newHeight))
                                        }
                                    },
                                    label = { Text("Altura (m)") },
                                    modifier = Modifier.weight(1f),
                                    colors = textFieldColors,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    readOnly = !isEditing // Só é editável se isEditing = true
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Seu IMC (Índice de Massa Corporal)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                                Text(
                                    text = imc,
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // --- Campo de Meta (Corrigido) ---
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        // --- 🎨 COR ALTERADA DE VOLTA 🎨 ---
                        colors = CardDefaults.cardColors(containerColor = PrimaryBlack.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 120.dp)
                    ) {
                        OutlinedTextField(
                            value = uiState.goal,
                            onValueChange = { viewModel.onEvent(ProfileEvent.OnGoalChanged(it)) },
                            label = { Text("Minha Meta") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 120.dp),
                            // Tornamos o fundo do TextField transparente para se misturar ao Card
                            colors = OutlinedTextFieldDefaults.colors(
                                // Cores do texto e label (igual ao textFieldColors)
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.Gray,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                disabledLabelColor = Color.Gray,
                                disabledTextColor = Color.White.copy(alpha = 0.8f),
                                cursorColor = DarkRed,

                                // Cores da borda e fundo (transparentes)
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                disabledBorderColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            ),
                            readOnly = !isEditing
                        )
                    }

                    if (isEditing) {
                        Button(
                            onClick = {
                                viewModel.onEvent(ProfileEvent.OnSaveClicked)
                            },
                            enabled = !uiState.isLoadingSave,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkRed)
                        ) {
                            if (uiState.isLoadingSave) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(text = "Salvar Alterações", modifier = Modifier.padding(vertical = 8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}