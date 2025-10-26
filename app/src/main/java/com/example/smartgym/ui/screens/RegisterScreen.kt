package com.example.smartgym.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartgym.viewmodel.login.UiEvent
import com.example.smartgym.viewmodel.register.RegisterEvent
import com.example.smartgym.viewmodel.register.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    Log.d("RegisterScreen", "Composable RegisterScreen foi chamado!")
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val eventFlow = viewModel.eventFlow

    var showSuccessDialog by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = eventFlow) {
        eventFlow.collect { event ->
            when (event) {
                is UiEvent.RegisterSuccess -> {

                    showSuccessDialog = event.message
                }
                is UiEvent.ShowError -> {

                }
                is UiEvent.LoginSuccess -> { }
            }
        }
    }

    if (showSuccessDialog != null) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = null
                onNavigateBack()
            },
            title = { Text("Sucesso!") },
            text = { Text(showSuccessDialog!!) },
            confirmButton = {
                Button(onClick = {
                    showSuccessDialog = null
                    onNavigateBack()
                }) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // <-- Usa a cor de fundo do tema
            // Ou para teste rápido: .background(Color.DarkGray)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Criar Conta", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = uiState.username,
            onValueChange = { viewModel.onEvent(RegisterEvent.OnUsernameChanged(it)) },
            label = { Text("Nome de Usuário") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = uiState.errorMessage?.contains("Username") == true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.email,
            onValueChange = { viewModel.onEvent(RegisterEvent.OnEmailChanged(it)) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            isError = uiState.errorMessage?.contains("Email") == true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.onEvent(RegisterEvent.OnPasswordChanged(it)) },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            isError = uiState.errorMessage?.contains("senha") == true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.confirmPassword,
            onValueChange = { viewModel.onEvent(RegisterEvent.OnConfirmPasswordChanged(it)) },
            label = { Text("Confirmar Senha") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            isError = uiState.errorMessage?.contains("senha") == true
        )

        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = uiState.errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { viewModel.onEvent(RegisterEvent.OnRegisterClick) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cadastrar")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateBack) {
            Text("Já tem uma conta? Faça Login")
        }
    }
}