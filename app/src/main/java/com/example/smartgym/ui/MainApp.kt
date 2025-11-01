package com.example.smartgym.ui

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smartgym.Routes
import com.example.smartgym.ui.layout.MainScaffold
import com.example.smartgym.ui.screens.AddWorkoutScreen
import com.example.smartgym.ui.screens.ChatScreen
import com.example.smartgym.ui.screens.InitialScreen
import com.example.smartgym.ui.screens.ProfileScreen
import com.example.smartgym.ui.theme.DarkRed
import com.example.smartgym.viewmodel.InitialScreenViewModel
import com.example.smartgym.data.model.ExerciseRequest
import com.example.smartgym.viewmodel.addworkout.AddWorkoutEvent
import com.example.smartgym.viewmodel.addworkout.AddWorkoutViewModel
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavBackStackEntry

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    val initialScreenViewModel: InitialScreenViewModel = hiltViewModel()

    val initialScreenUiState by initialScreenViewModel.uiState.collectAsStateWithLifecycle()

    val onNavigate: (String) -> Unit = { route ->

        navController.navigate(route) {
            if (currentRoute == Routes.ADD_WORKOUT && route != Routes.ADD_WORKOUT) {
                popUpTo(Routes.INITIAL) { inclusive = false }
            } else {
                launchSingleTop = true
                popUpTo(navController.graph.startDestinationId) { saveState = true }
                restoreState = true
            }
        }
    }

    MainScaffold(
        currentRoute = currentRoute ?: Routes.INITIAL,
        onNavigate = onNavigate,

        floatingActionButton = {

            if (currentRoute == Routes.INITIAL) {
                FloatingActionButton(
                    onClick = {

                        val selectedDay = initialScreenUiState.selectedWeekday

                        navController.navigate(
                            "${Routes.ADD_WORKOUT}/${selectedDay}"
                        )
                    },
                    containerColor = DarkRed
                ) {
                    Icon(Icons.Default.Add, "Adicionar Treino", tint = Color.White)
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.INITIAL,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.INITIAL) {
                InitialScreen(
                    uiState = initialScreenUiState,
                    onEvent = initialScreenViewModel::onEvent,
                    onLogoutClicked = {
                        initialScreenViewModel.onLogoutClicked {
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        }
                    }
                )
            }
            composable(Routes.CHAT) {
                ChatScreen(
                    onNavigateBack = { generatedData ->
                        Log.d("MainAppNav", "onNavigateBack do Chat. Dados recebidos: $generatedData")
                        if (generatedData != null) {
                            try {
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("ia_workout_data", generatedData)

                                Log.i("MainAppNav", "Dados enviados para a tela anterior via SavedStateHandle com SUCESSO.")

                            } catch (e: Exception) {
                                Log.e(
                                    "MainAppNav",
                                    "ERRO AO ENVIAR DADOS! A classe List<ExerciseRequest> é Parcelable?",
                                    e
                                )
                            }
                        } else {
                            Log.w("MainAppNav", "Nenhum dado gerado para enviar de volta.")
                        }
                        navController.popBackStack()
                    }
                )
            }
            composable(Routes.PROFILE) { ProfileScreen() }

            composable(
                route = "${Routes.ADD_WORKOUT}/{${Routes.ADD_WORKOUT_ARG_WEEKDAY}}",
                arguments = listOf(
                    navArgument(Routes.ADD_WORKOUT_ARG_WEEKDAY) {
                        type = NavType.StringType
                    }
                )
            ) { navBackStackEntry ->

                val addWorkoutViewModel: AddWorkoutViewModel = hiltViewModel(navBackStackEntry)
                val lifecycleOwner = LocalLifecycleOwner.current
                LaunchedEffect(navBackStackEntry, lifecycleOwner) {

                    val resultLiveData = navBackStackEntry.savedStateHandle
                        .getLiveData<List<ExerciseRequest>>("ia_workout_data")

                    resultLiveData.observe(lifecycleOwner) { data ->

                        if (data != null) {
                            Log.i(
                                "AddWorkoutScreen",
                                "DADOS DA IA RECEBIDOS (via LiveData): $data"
                            )


                            addWorkoutViewModel.onEvent(AddWorkoutEvent.OnIaDataReceived(data))

                            navBackStackEntry.savedStateHandle.remove<List<ExerciseRequest>>("ia_workout_data")
                        }
                    }
                }
                AddWorkoutScreen(
                    viewModel = addWorkoutViewModel,
                    onNavigateBack = { workoutWasSaved ->
                        Log.d("MainApp", "Callback onNavigateBack recebido! workoutWasSaved = $workoutWasSaved")
                        if (workoutWasSaved) {
                            Log.d("MainApp", "Novo treino salvo! Recarregando dados...")
                            initialScreenViewModel.loadWorkouts()
                        }
                        Log.d("MainApp", "Chamando navController.popBackStack() AGORA")
                        navController.popBackStack()
                    },
                    onNavigateToChat = {
                        navController.navigate(Routes.CHAT)
                    }
                )
            }
        }
    }
}