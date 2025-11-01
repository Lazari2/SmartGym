package com.example.smartgym.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartgym.ui.components.WorkoutCard
import com.example.smartgym.ui.components.WorkoutData
import com.example.smartgym.ui.theme.DarkRed
import com.example.smartgym.ui.theme.PrimaryBlack
import com.example.smartgym.viewmodel.InitialScreenEvent
import java.time.LocalDate
import com.example.smartgym.viewmodel.InitialScreenUiState
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun InitialScreen(
    uiState: InitialScreenUiState,
    onEvent: (InitialScreenEvent) -> Unit
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(PrimaryBlack, DarkRed.copy(alpha = 0.3f))
    )


    Box(modifier = Modifier.fillMaxSize().background(gradientBrush)) {
        Scaffold(
            containerColor = Color.Transparent
        ) { innerPadding ->
            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                uiState.errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = uiState.errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp),
                    ) {
//                        DisplayOffensive(offensiveDays = uiState.offensiveDays)
//                        Spacer(modifier = Modifier.height(24.dp))
                            ScreenTitle(title = "Meus Treinos")
                            Spacer(modifier = Modifier.height(24.dp))
                        WeekDaysCarousel(
                            trainedDates = uiState.trainedDates,
                            selectedDayName = uiState.selectedWeekday,
                            onDaySelected = { diaCompleto ->
                                onEvent(InitialScreenEvent.OnWeekdaySelected(diaCompleto))
                            }
                        )

                        LastActivitiesSection(activities = uiState.filteredWorkouts)
                    }
                }
            }
        }
    }
}


@Composable
fun DisplayOffensive(offensiveDays: Int) {
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

data class DayData(
    val dayNameShort: String,
    val dayNumber: Int,
    val isToday: Boolean,
    val isTrained: Boolean,
    val fullDayName: String
)

@Composable
fun WeekDaysCarousel(
    trainedDates: List<LocalDate>,
    selectedDayName: String,
    onDaySelected: (String) -> Unit
) {
    val today = LocalDate.now()
    val locale = Locale.forLanguageTag("pt-BR")
    val daysToSubtract = today.dayOfWeek.value % 7L
    val firstDayOfWeek = today.minusDays(daysToSubtract)

    val days = (0..6).map {
        val date = firstDayOfWeek.plusDays(it.toLong())
        val dayNameShort = date.dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
            .replaceFirstChar { char -> char.uppercase() }.removeSuffix(".")
        val fullDayName = date.dayOfWeek.getDisplayName(TextStyle.FULL, locale)
            .replaceFirstChar { char -> char.uppercase() }

        DayData(
            dayNameShort = dayNameShort,
            dayNumber = date.dayOfMonth,
            isToday = date.isEqual(today),
            isTrained = trainedDates.contains(date),
            fullDayName = fullDayName
        )
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(days) { day ->
            DayItem(
                day = day,
                isSelected = day.fullDayName == selectedDayName,
                onClick = { onDaySelected(day.fullDayName) }
            )
        }
    }
}

@Composable
fun DayItem(
    day: DayData,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val cardColors = if (isSelected) {
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
        colors = cardColors,
        modifier = Modifier.clickable(onClick = onClick)
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
                    text = day.dayNameShort,
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
            WorkoutCard(workout = activity, onClick = { /* TODO: Lidar com clique no treino */ })
        }
    }
}

@Composable
fun ScreenTitle(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}