package com.mmoney.apps

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.mmoney.apps.core.data.repository.UserPreferencesRepository
import com.mmoney.apps.ui.MMoneyApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Observe language preference and update locale
        lifecycleScope.launch {
            userPreferencesRepository.userPreferences.collect { preferences ->
                val locale = Locale(preferences.language.code)
                Locale.setDefault(locale)
                val config = Configuration(resources.configuration)
                config.setLocale(locale)
                resources.updateConfiguration(config, resources.displayMetrics)
            }
        }
        
        setContent {
            MMoneyApp()
        }
    }
}