/*
* Copyright (C) 2013 The OmniROM Project
* Copyright (C) 2021 The dot OS Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/
package com.awaken.device.DeviceExtras;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.os.SystemProperties;
import android.provider.Settings;
import androidx.preference.PreferenceManager;

public class Startup extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent bootintent) {
        PackageManager pm = context.getApplicationContext().getPackageManager();
        ComponentName componentName = new ComponentName("com.awaken.device.DeviceExtras",
    "com.awaken.device.DeviceExtras.DCModeTileService");
        if (SystemProperties.get("ro.overlay.device", "").equals("instantnoodlep")) {
            pm.setComponentEnabledSetting(componentName,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP);
        } else {
            pm.setComponentEnabledSetting(componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP);
        }    
        boolean enabled = false;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_HBM_SWITCH, false);
        if (enabled) {
        restore(HBMModeSwitch.getFile(), enabled);
               }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_DC_SWITCH, false);
        if (enabled) {
        restore(DCModeSwitch.getFile(), enabled);
               }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_FPS_INFO, false);
        if (enabled) {
            context.startService(new Intent(context, FPSInfoService.class));
               }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_GAME_SWITCH, false);
        if (enabled) {
        restore(GameModeSwitch.getFile(), enabled);
               }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_OTG_SWITCH, false);
        if (enabled) {
        restore(OTGModeSwitch.getFile(), enabled);
               }
        AdrenoBoostPreference.restore(context);
        com.awaken.device.DeviceExtras.doze.DozeUtils.checkDozeService(context);
        VibratorStrengthPreference.restore(context);
    }

    private void restore(String file, boolean enabled) {
        if (file == null) {
            return;
        }
        if (enabled) {
            FileUtils.writeValue(file, "1");
        }
    }

    private void restore(String file, String value) {
        if (file == null) {
            return;
        }
        FileUtils.writeValue(file, value);
    }
}
