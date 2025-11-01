package com.example.smartgym.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.smartgym.viewmodel.addworkout.AddedExercise

@Composable
fun ExerciseListItem(
    exercise: AddedExercise, //
    onRemove: () -> Unit //
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()
    ) {
        Text(exercise.name, modifier = Modifier.weight(1f), color = Color.White)
        val weightText = exercise.weight?.let { "${it}kg" } ?: "..."
        Text(text = "${weightText} x ${exercise.reps}", color = Color.White)

        IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Remover Exercício", tint = MaterialTheme.colorScheme.error)
        }
    }
}