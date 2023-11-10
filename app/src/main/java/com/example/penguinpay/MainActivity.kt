package com.example.penguinpay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.penguinpay.compose.PenguinPayApp
import com.example.penguinpay.ui.theme.PenguinPayTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PenguinPayTheme {
                PenguinPayApp()
            }
        }
    }
}