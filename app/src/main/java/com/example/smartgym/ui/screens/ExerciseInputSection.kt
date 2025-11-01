package com.example.smartgym.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.smartgym.ui.theme.DarkRed
import com.example.smartgym.ui.theme.PrimaryBlack
import com.example.smartgym.viewmodel.addworkout.AddWorkoutEvent
import com.example.smartgym.viewmodel.addworkout.AddWorkoutUiState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ExerciseInputSection(
    uiState: AddWorkoutUiState,
    onEvent: (AddWorkoutEvent) -> Unit,
    onShowExercisePicker: () -> Unit
) {
    Log.d("ExerciseInputSection", "COMPOSABLE ExerciseInputSection FOI CHAMADO (desenhado)")

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent, disabledIndicatorColor = Color.Transparent,
        focusedTextColor = Color.White, unfocusedTextColor = Color.White,
        cursorColor = Color.White, focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.LightGray,
    )

    val outlinedTextFieldColors = TextFieldDefaults.colors(
        focusedTextColor = Color.White, unfocusedTextColor = Color.White,
        cursorColor = Color.White, focusedIndicatorColor = Color.White,
        unfocusedIndicatorColor = Color.LightGray, errorIndicatorColor = MaterialTheme.colorScheme.error,
        focusedLabelColor = Color.White, unfocusedLabelColor = Color.LightGray,
        focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent, errorContainerColor = Color.Transparent,
        disabledTextColor = Color.White,
        disabledLabelColor = Color.LightGray,
        disabledIndicatorColor = Color.LightGray,
        disabledTrailingIconColor = Color.White
    )

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Text(
            text = "Selecione um Grupo Muscular",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiState.muscleGroups.forEach { groupName ->
                MuscleGroupChip(
                    text = groupName,
                    isSelected = uiState.selectedMuscleGroup == groupName,
                    onClick = {
                        Log.d("ChipClick", "Chip '$groupName' clicado!")
                        onEvent(AddWorkoutEvent.OnMuscleGroupSelected(groupName))
                    }
                )
            }
        }

        if (uiState.selectedMuscleGroup != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = {
                            Log.d("DropdownClick", "Box EXERCÍCIO clicado!")
                            onShowExercisePicker()
                        }
                    )
            ) {

                OutlinedTextField(
                    value = uiState.selectedTemplate?.name ?: "Selecione um Exercício",
                    onValueChange = { },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, "Selecionar exercício") },
                    enabled = false,
                    colors = outlinedTextFieldColors,
                    placeholder = {
                        if (uiState.isLoadingExercises) {
                            Text("Carregando exercícios...")
                        }
                    }
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = uiState.currentSets,
                onValueChange = { newValue ->
                    val filteredValue = newValue.filter { it.isDigit() }
                    onEvent(AddWorkoutEvent.OnSetsChanged(filteredValue))
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Sets", color = Color.LightGray) },
                colors = textFieldColors,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            TextField(
                value = uiState.currentReps,
                onValueChange = { newValue ->
                    val filteredValue = newValue.filter { it.isDigit() }
                    onEvent(AddWorkoutEvent.OnRepsChanged(filteredValue))
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Reps", color = Color.LightGray) },
                colors = textFieldColors,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            TextField(
                value = uiState.currentWeight,
                onValueChange = { newValue ->
                    val filteredValue = newValue.filter { it.isDigit() }
                    onEvent(AddWorkoutEvent.OnWeightChanged(filteredValue))
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Peso", color = Color.LightGray) },
                colors = textFieldColors,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            IconButton(
                onClick = { onEvent(AddWorkoutEvent.OnAddExerciseToList) },
                enabled = !uiState.isLoadingExercises
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Exercício", tint = Color.White)
            }
        }

        OutlinedTextField(
            value = uiState.currentNotes,
            onValueChange = { onEvent(AddWorkoutEvent.OnNotesChanged(it)) },
            label = { Text("Notas (opcional)") },
            modifier = Modifier.fillMaxWidth(),
            colors = outlinedTextFieldColors
        )
    }
}

@Composable
private fun MuscleGroupChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) DarkRed else PrimaryBlack.copy(alpha = 0.5f)
    val borderColor = if (isSelected) DarkRed else Color.LightGray

    Surface(
        modifier = Modifier
            .clickable(onClick = onClick)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        contentColor = Color.White
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}