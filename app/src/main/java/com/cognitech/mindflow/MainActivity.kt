package com.cognitech.mindflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.cognitech.mindflow.ui.navigation.AppNavigation
import com.cognitech.mindflow.ui.theme.MindFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MindFlowTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(application as MindFlowApplication)
                }
            }
        }
    }
}
