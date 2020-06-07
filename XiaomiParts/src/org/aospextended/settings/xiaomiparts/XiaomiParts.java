/*
 * Copyright (C) 2015 The CyanogenMod Project
 *               2017-2020 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.aospextended.settings.xiaomiparts;

import android.content.Intent;
import android.os.Bundle;
import androidx.preference.PreferenceFragment;
import androidx.preference.Preference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import androidx.preference.TwoStatePreference;

import org.aospextended.settings.xiaomiparts.doze.DozeSettingsActivity;
import org.aospextended.settings.xiaomiparts.vibration.VibratorStrengthPreference;

public class XiaomiParts extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    public static final String PREF_USB_FASTCHARGE = "fastcharge";
    public static final String USB_FASTCHARGE_PATH = "/sys/kernel/fast_charge/force_fast_charge";

    private SwitchPreference mUsbFastCharger;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.xiaomiparts, rootKey);
        Preference mDozePref = findPreference("doze");
        mDozePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), DozeSettingsActivity.class);
                startActivity(intent);
                return true;
            }
        });

        VibratorStrengthPreference mVibratorStrength = findPreference("vib_strength");
        mVibratorStrength.setEnabled(VibratorStrengthPreference.isSupported());

        mUsbFastCharger = findPreference(PREF_USB_FASTCHARGE);
        mUsbFastCharger.setEnabled(FileUtils.fileWritable(USB_FASTCHARGE_PATH));
        mUsbFastCharger.setChecked(FileUtils.getFileValueAsBoolean(USB_FASTCHARGE_PATH, true));
        mUsbFastCharger.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mUsbFastCharger) {
            FileUtils.setValue(USB_FASTCHARGE_PATH, (boolean) newValue);
        return true;
        }
    return false;
    }
}
