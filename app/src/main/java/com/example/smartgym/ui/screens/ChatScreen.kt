package com.example.smartgym.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.smartgym.ui.theme.DarkRed
import com.example.smartgym.ui.theme.PrimaryBlack
import java.util.UUID

enum class Sender {
    USER, AI
}

data class Message(val text: String, val sender: Sender, val id: UUID = UUID.randomUUID())

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(PrimaryBlack, DarkRed.copy(alpha = 0.3f))
    )

    var messageText by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            Message("Olá! Como posso ajudar você a atingir seus objetivos de fitness hoje?", Sender.AI)
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(gradientBrush)) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("SmartGym IA", fontFamily = FontFamily.Monospace) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White
                    )
                )
            },
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Digite sua mensagem...") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = PrimaryBlack.copy(alpha = 0.5f),
                            unfocusedContainerColor = PrimaryBlack.copy(alpha = 0.5f),
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(24.dp),
                        singleLine = true
                    )
                    IconButton(onClick = {
                        if (messageText.isNotBlank()) {
                            messages.add(Message(messageText, Sender.USER))
                            messages.add(Message("Ótima pergunta! Vamos detalhar seu plano de treino.", Sender.AI))
                            messageText = ""
                        }
                    }) {
                        Icon(Icons.Default.Send, contentDescription = "Enviar", tint = Color.White)
                    }
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                reverseLayout = true
            ) {
                items(messages.reversed()) { message ->
                    MessageBubble(message = message, isLastAiMessage = message.id == messages.lastOrNull { it.sender == Sender.AI }?.id)
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message, isLastAiMessage: Boolean) {
    val isUser = message.sender == Sender.USER
    val horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    val shape = RoundedCornerShape(16.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = horizontalArrangement
    ) {
        when {
            isUser -> {
                // User message bubble
                Box(
                    modifier = Modifier
                        .clip(shape)
                        .background(DarkRed)
                        .padding(16.dp)
                ) {
                    Text(text = message.text, color = Color.White)
                }
            }
            isLastAiMessage -> {
                // Last AI message with animated border
                val infiniteTransition = rememberInfiniteTransition(label = "shimmer-animation")
                val offset by infiniteTransition.animateFloat(
                    initialValue = -500f,
                    targetValue = 1500f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "shimmer-offset"
                )

                val borderBrush = Brush.linearGradient(
                    colors = listOf(
                        Color.Transparent, DarkRed, Color.Red,
                        Color(0xFFFFA500), Color.Red, DarkRed, Color.Transparent
                    ),
                    start = Offset(offset, 0f),
                    end = Offset(offset + 500f, 0f)
                )

                Box(
                    modifier = Modifier
                        .border(width = 2.dp, brush = borderBrush, shape = shape)
                        .clip(shape)
                        .background(PrimaryBlack.copy(alpha = 0.5f))
                        .padding(16.dp)
                ) {
                    Text(text = message.text, color = Color.White)
                }
            }
            else -> {
                // Older AI message bubble (static)
                Box(
                    modifier = Modifier
                        .clip(shape)
                        .background(PrimaryBlack.copy(alpha = 0.5f))
                        .padding(16.dp)
                ) {
                    Text(text = message.text, color = Color.White)
                }
            }
        }
    }
}