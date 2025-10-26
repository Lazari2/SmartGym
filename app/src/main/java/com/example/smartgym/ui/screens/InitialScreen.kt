package com.example.smartgym.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartgym.ui.components.WorkoutCard
import com.example.smartgym.ui.components.WorkoutData
import com.example.smartgym.ui.theme.DarkRed
import com.example.smartgym.ui.theme.PrimaryBlack
import com.example.smartgym.viewmodel.InitialScreenViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun InitialScreen(initialScreenViewModel: InitialScreenViewModel = viewModel()) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(PrimaryBlack, DarkRed.copy(alpha = 0.3f))
    )

    val offensiveDays by initialScreenViewModel.offensiveDays.collectAsState()

    val activities = listOf(
        WorkoutData("Peito", "12 Sets", "25/10/2025", false, Icons.AutoMirrored.Filled.TrendingUp),
        WorkoutData("Costas", "8 Sets", "23/10/2025", true, Icons.Default.Star),
        WorkoutData("Biceps", "10 Sets", "22/10/2025", true, Icons.Default.FitnessCenter)
    )

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val trainedDates = activities.map { LocalDate.parse(it.date, formatter) }

    Box(modifier = Modifier.fillMaxSize().background(gradientBrush)){
        Scaffold(
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
            ) {
                DisplayOffensive(offensiveDays = offensiveDays)
                Spacer(modifier = Modifier.height(24.dp))
                WeekDaysCarousel(trainedDates = trainedDates)
                LastActivitiesSection(activities = activities)
            }
        }
    }
}


@Composable
fun DisplayOffensive(offensiveDays: Int){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = offensiveDays.toString(),
            fontSize = 96.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "DIAS",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "OFENSIVAS!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

data class DayData(val dayName: String, val dayNumber: Int, val isToday: Boolean, val isTrained: Boolean)

@Composable
fun WeekDaysCarousel(trainedDates: List<LocalDate>) {
    val today = LocalDate.of(2025, 10, 26) // Using a fixed date for verification

    val days = (-3..3).map {
        val date = today.plusDays(it.toLong())
        DayData(
            dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("pt-BR")).replaceFirstChar { char -> char.uppercase() }.removeSuffix("."),
            dayNumber = date.dayOfMonth,
            isToday = date.isEqual(today),
            isTrained = trainedDates.contains(date)
        )
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(days) { day ->
            DayItem(day)
        }
    }
}

@Composable
fun DayItem(day: DayData) {
    val cardColors = if (day.isToday) {
        CardDefaults.cardColors(
            containerColor = DarkRed
        )
    } else {
        CardDefaults.cardColors(
            containerColor = PrimaryBlack.copy(alpha = 0.5f)
        )
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = cardColors
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (day.isTrained) {
                Icon(
                    imageVector = Icons.Default.Whatshot,
                    contentDescription = "Trained Day",
                    modifier = Modifier.size(40.dp),
                    tint = DarkRed.copy(alpha = 0.6f)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = day.dayName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = day.dayNumber.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun LastActivitiesSection(activities: List<WorkoutData>) {
    LazyColumn(
        modifier = Modifier.padding(top = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(activities) { activity ->
            WorkoutCard(workout = activity, onClick = { /* TODO: Handle click */ })
        }
    }
}
