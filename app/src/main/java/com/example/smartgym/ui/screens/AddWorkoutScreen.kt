package com.example.smartgym.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.smartgym.ui.theme.DarkRed
import com.example.smartgym.ui.theme.PrimaryBlack
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class ExerciseSet(val exercise: String, val weight: String, val reps: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkoutScreen(onNavigateBack: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    var dateText by remember { mutableStateOf(LocalDate.now().format(formatter)) }

    val exerciseSets = remember { mutableStateListOf<ExerciseSet>() }

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(PrimaryBlack, DarkRed.copy(alpha = 0.3f))
    )

    Box(modifier = Modifier.fillMaxSize().background(gradientBrush)) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Adicionar Novo Treino") },
                    actions = {
                        Button(
                            onClick = {
                                // TODO: Logic to save the new workout with title, date, and exerciseSets
                                onNavigateBack()
                            },
                            modifier = Modifier.padding(end = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkRed)
                        ) {
                            Text("Salvar", color = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título do Treino") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dateText,
                    onValueChange = { dateText = it },
                    label = { Text("Data") },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, "Selecionar data no calendário")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Divider(color = Color.Gray)

                ExerciseInputSection(onAddExercise = {
                    exerciseSets.add(it)
                })

                if (exerciseSets.isNotEmpty()) {
                    Divider(color = Color.Gray)
                    Text("Exercícios Adicionados", style = MaterialTheme.typography.titleMedium)
                    LazyColumn {
                        items(exerciseSets) { set ->
                            ExerciseListItem(set = set, onRemove = { exerciseSets.remove(it) })
                        }
                    }
                }
            }
        }
    }


    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = Instant.now().toEpochMilli())
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
                            dateText = selectedDate.format(formatter)
                        }
                        showDatePicker = false
                    }
                ) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") } }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseInputSection(onAddExercise: (ExerciseSet) -> Unit) {
    var exerciseText by remember { mutableStateOf("") }
    var weightText by remember { mutableStateOf("") }
    var repsText by remember { mutableStateOf("") }
    var exerciseExpanded by remember { mutableStateOf(false) }
    val exercises = listOf("Supino", "Agachamento", "Levantamento Terra", "Rosca Direta", "Remada Curvada", "Desenvolvimento Militar")

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color.White,
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.LightGray,
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ExposedDropdownMenuBox(
                expanded = exerciseExpanded,
                onExpandedChange = { exerciseExpanded = !it },
                modifier = Modifier.weight(2.5f)
            ) {
                TextField(
                    value = exerciseText,
                    onValueChange = { exerciseText = it; exerciseExpanded = true },
                    modifier = Modifier.menuAnchor(),
                    placeholder = { Text("Exercício", color = Color.LightGray) },
                    colors = textFieldColors
                )
                val filteredOptions = exercises.filter { it.contains(exerciseText, ignoreCase = true) }
                if (filteredOptions.isNotEmpty()) {
                    ExposedDropdownMenu(expanded = exerciseExpanded, onDismissRequest = { exerciseExpanded = false }) {
                        filteredOptions.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    exerciseText = selectionOption
                                    exerciseExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            TextField(value = weightText, onValueChange = { weightText = it }, modifier = Modifier.weight(1.2f), placeholder = { Text("Peso", color = Color.LightGray) }, colors = textFieldColors, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            TextField(value = repsText, onValueChange = { repsText = it }, modifier = Modifier.weight(1.4f), placeholder = { Text("Rep.", color = Color.LightGray) }, colors = textFieldColors, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            IconButton(onClick = {
                if (exerciseText.isNotBlank() && weightText.isNotBlank() && repsText.isNotBlank()) {
                    onAddExercise(ExerciseSet(exerciseText, weightText, repsText))
                    // Clear fields
                    exerciseText = ""
                    weightText = ""
                    repsText = ""
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Exercício", tint = Color.White)
            }
        }
    }
}

@Composable
fun ExerciseListItem(set: ExerciseSet, onRemove: (ExerciseSet) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()
    ) {
        Text(set.exercise, modifier = Modifier.weight(1f), color = Color.White)
        Text(text = "${set.weight}kg x ${set.reps}", color = Color.White)
        IconButton(onClick = { onRemove(set) }, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Remover Exercício", tint = MaterialTheme.colorScheme.error)
        }
    }
}