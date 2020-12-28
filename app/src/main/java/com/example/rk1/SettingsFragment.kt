package com.example.rk1

import android.os.Bundle
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_fragment)

        val editNumDaysVal = findPreference<EditTextPreference>(getString(R.string.num_key))
        editNumDaysVal?.setOnPreferenceChangeListener { _, newValue ->
            val newVal = newValue.toString()
            try {
                val value = newVal.toInt()
                if (value < 1 || value > 100) {
                    Toast.makeText(context, "Enter number from 1 to 100", Toast.LENGTH_SHORT).show()
                    return@setOnPreferenceChangeListener false
                }
                Toast.makeText(context, "Number of days changed", Toast.LENGTH_SHORT).show()
                true
            } catch (e: NumberFormatException) {
                Toast.makeText(context, "Enter number from 1 to 100", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }
}