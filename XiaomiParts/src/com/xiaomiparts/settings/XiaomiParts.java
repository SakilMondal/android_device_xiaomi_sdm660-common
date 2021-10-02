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

package com.xiaomiparts.settings;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceFragment;
import androidx.preference.Preference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import androidx.preference.TwoStatePreference;

import com.xiaomiparts.settings.doze.DozeSettingsActivity;
import com.xiaomiparts.settings.kcal.KCalSettingsActivity;
import com.xiaomiparts.settings.preference.SecureSettingSwitchPreference;
import com.xiaomiparts.settings.speaker.ClearSpeakerActivity;
import com.xiaomiparts.settings.vibration.VibratorStrengthPreference;

public class XiaomiParts extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    public static final String PREF_USB_FASTCHARGE = "fastcharge";
    public static final String USB_FASTCHARGE_PATH = "/sys/kernel/fast_charge/force_fast_charge";
    private static final String PREF_CLEAR_SPEAKER = "clear_speaker_settings";
    public static final String PREF_KEY_FPS_INFO = "fps_info";

    private SwitchPreference mUsbFastCharger;
    private Preference mClearSpeakerPref;

    private static Context mContext;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.xiaomiparts, rootKey);

        mContext = this.getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        Preference mDozePref = findPreference("doze");
        mDozePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), DozeSettingsActivity.class);
                startActivity(intent);
                return true;
            }
        });

        mClearSpeakerPref = (Preference) findPreference(PREF_CLEAR_SPEAKER);
        mClearSpeakerPref.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), ClearSpeakerActivity.class);
            startActivity(intent);
            return true;
        });

        Preference mKCal = findPreference("device_kcal");
        mKCal.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity().getApplicationContext(), KCalSettingsActivity.class);
                startActivity(intent);
                return true;
            }
        });

        VibratorStrengthPreference mVibratorStrength = findPreference("vib_strength");
        mVibratorStrength.setEnabled(VibratorStrengthPreference.isSupported());

        if (FileUtils.fileWritable(USB_FASTCHARGE_PATH)) {
            mUsbFastCharger = findPreference(PREF_USB_FASTCHARGE);
            mUsbFastCharger.setEnabled(UsbFastCharge.isSupported());
            mUsbFastCharger.setChecked(UsbFastCharge.isCurrentlyEnabled(this.getContext()));
            mUsbFastCharger.setOnPreferenceChangeListener(new UsbFastCharge(getContext()));
        } else {
            getPreferenceScreen().removePreference(findPreference(PREF_USB_FASTCHARGE));
        }

        SwitchPreference fpsInfo = (SwitchPreference) findPreference(PREF_KEY_FPS_INFO);
        fpsInfo.setChecked(prefs.getBoolean(PREF_KEY_FPS_INFO, false));
        fpsInfo.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final String key = preference.getKey();
        switch (key) {
	    case PREF_KEY_FPS_INFO:
               boolean enabled = (Boolean) newValue;
               Intent fpsinfo = new Intent(this.getContext(), com.xiaomiparts.settings.FPSInfoService.class);
               if (enabled) {
                   this.getContext().startService(fpsinfo);
               } else {
                   this.getContext().stopService(fpsinfo);
               }
               break;

	    default:
                break;
	}
    return true;
    }
}
