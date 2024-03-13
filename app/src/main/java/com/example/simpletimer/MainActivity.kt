package com.example.simpletimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.simpletimer.ui.screens.MyApp
import com.example.simpletimer.ui.theme.SimpleTimerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            SimpleTimerTheme {
                MyApp()
            }
        }
    }
}





