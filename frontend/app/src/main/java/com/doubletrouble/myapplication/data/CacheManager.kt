package com.doubletrouble.myapplication.data

import android.content.Context

class CacheManager(context: Context) {
    private val prefs = context.getSharedPreferences("app_cache", Context.MODE_PRIVATE)

    fun setName(name: String) {
        prefs.edit().apply {
            putString("cached_name", name)
            apply()
        }
    }

    fun setFact(fact: String) {
        prefs.edit().apply {
            putString("cached_fact", fact)
            apply()
        }
    }

    fun setIdealLightHours(lightHours: Int) {
        prefs.edit().apply {
            putInt("cached_ideal_light_hours", lightHours)
            apply()
        }
    }

    fun setIdealSoilHumidity(soilHumidity: Int) {
        prefs.edit().apply {
            putInt("cached_ideal_soil_humidity", soilHumidity)
            apply()
        }
    }

    fun setHealthCondition(healthCondition: Boolean) {
        prefs.edit().apply {
            putString("cached_health_condition", healthCondition.toString())
            apply()
        }
    }

    fun setHealthDescription(healthDescription: String) {
        prefs.edit().apply {
            putString("cached_health_description", healthDescription)
            apply()
        }
    }

    fun setHealthImage(imageUrl: String) {
        prefs.edit().apply {
            putString("cached_health_image", imageUrl)
            apply()
        }
    }

    fun getCachedName(): String? = prefs.getString("cached_name", null)
    fun getCachedFact(): String? = prefs.getString("cached_fact", null)
    fun getCachedIdealLightHours(): Int = prefs.getInt("cached_ideal_light_hours", 0)
    fun getCachedIdealSoilHumidity(): Int = prefs.getInt("cached_ideal_soil_humidity", 0)
    fun getCachedHealthCondition(): String? = prefs.getString("cached_health_condition", null)

    fun getCachedHealthDescription(): String? = prefs.getString("cached_health_description", null)

    fun getCachedHealthImage(): String? = prefs.getString("cached_health_image", null)
}