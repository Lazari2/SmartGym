package com.example.smartgym.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartgym.ui.theme.DarkRed
import com.example.smartgym.ui.theme.PrimaryBlack
import com.example.smartgym.viewmodel.UiEvent
import com.example.smartgym.viewmodel.addworkout.AddWorkoutEvent
import com.example.smartgym.viewmodel.addworkout.AddWorkoutViewModel
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.FloatingActionButton

data class ExerciseSet(val exercise: String, val weight: String, val reps: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkoutScreen(
    onNavigateBack: (workoutWasSaved: Boolean) -> Unit,
    onNavigateToChat: () -> Unit,
    viewModel: AddWorkoutViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val eventFlow = viewModel.eventFlow
    val context = LocalContext.current
    val outlinedTextFieldColors = TextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color.White,
        focusedIndicatorColor = Color.White,
        unfocusedIndicatorColor = Color.LightGray,
        errorIndicatorColor = MaterialTheme.colorScheme.error,
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.LightGray,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
        disabledTextColor = Color.LightGray,
        disabledLabelColor = Color.LightGray,
        disabledIndicatorColor = Color.DarkGray
    )
    val scope = rememberCoroutineScope()
    var showExerciseSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()


    LaunchedEffect(key1 = eventFlow) {
        eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is UiEvent.NavigateSaveBack -> {
                    Log.d("AddWorkoutScreen", "Evento NavigateSaveBack recebido! saved=${event.saved}")
                    onNavigateBack(event.saved)
                }
                else -> {  }
            }
        }
    }

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(PrimaryBlack, DarkRed.copy(alpha = 0.3f))
    )

    Box(modifier = Modifier.fillMaxSize().background(gradientBrush)) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Adicionar Novo Treino") },
                    navigationIcon = {
                        IconButton(onClick = {
                            onNavigateBack(false)
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = Color.White)
                        }
                    },
                    actions = {
                        Button(
                            onClick = {
                                viewModel.onEvent(AddWorkoutEvent.OnSaveWorkout)
                            },
                            modifier = Modifier.padding(end = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkRed),
                            enabled = !uiState.isSaving
                        ) {
                            if (uiState.isSaving) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                            } else {
                                Text("Salvar", color = Color.White)
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onNavigateToChat() },
                    containerColor = DarkRed
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Gerar treino com IA",
                        tint = Color.White
                    )
                }
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp) // Padding lateral
                    .padding(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = uiState.workoutTitle,
                    onValueChange = { viewModel.onEvent(AddWorkoutEvent.OnTitleChanged(it)) },
                    label = { Text("Título do Treino") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedTextFieldColors
                )


                OutlinedTextField(
                    value = uiState.weekday,
                    onValueChange = {},
                    label = { Text("Dia da Semana") },
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedTextFieldColors
                )

                Divider(color = Color.Gray)

                ExerciseInputSection(
                    uiState = uiState,
                    onEvent = viewModel::onEvent,
                    onShowExercisePicker = {
                        Log.d("AddWorkoutScreen", "Mostrando o popup de exercícios")
                        showExerciseSheet = true
                    }
                )

                if (uiState.addedExercises.isNotEmpty()) {
                    Divider(color = Color.Gray)
                    Text(
                        "Exercícios Adicionados",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Column {
                        uiState.addedExercises.forEach { exercise ->
                            ExerciseListItem(
                                exercise = exercise,
                                onRemove = {
                                    viewModel.onEvent(AddWorkoutEvent.OnRemoveExercise(exercise))
                                }
                            )
                        }
                    }
                }

            }
        }
        if (showExerciseSheet) {
            ModalBottomSheet(
                onDismissRequest = { showExerciseSheet = false },
                sheetState = sheetState,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                containerColor = PrimaryBlack,
                dragHandle = {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .width(40.dp)
                            .height(4.dp)
                            .background(Color.Gray, RoundedCornerShape(2.dp))
                    )
                }
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 32.dp)
                ) {
                    Text(
                        text = "Selecione um Exercício",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (uiState.isLoadingExercises) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = Color.White)
                                }
                            }
                        }

                        items(uiState.exercisesForGroup) { template ->
                            DropdownMenuItem(
                                text = { Text(template.name, color = Color.White) },
                                onClick = {
                                    viewModel.onEvent(AddWorkoutEvent.OnExerciseTemplateSelected(template))

                                    scope.launch {
                                        sheetState.hide()
                                    }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showExerciseSheet = false
                                        }
                                    }
                                },
                                modifier = Modifier.clip(RoundedCornerShape(8.dp))
                            )
                        }
                    }
                }
            }
        }
    }
}