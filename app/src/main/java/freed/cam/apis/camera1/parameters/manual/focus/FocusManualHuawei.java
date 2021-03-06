/*
 *
 *     Copyright (C) 2015 Ingo Fuchs
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * /
 */

package freed.cam.apis.camera1.parameters.manual.focus;

import android.hardware.Camera.Parameters;
import freed.utils.Log;

import com.troop.freedcam.R;

import freed.cam.apis.basecamera.CameraWrapperInterface;
import freed.cam.apis.camera1.parameters.ParametersHandler;
import freed.utils.AppSettingsManager;

/**
 * Created by GeorgeKiarie on 02/04/2016.
 */
public class FocusManualHuawei extends BaseFocusManual
{
    private final String TAG = FocusManualHuawei.class.getSimpleName();
    public FocusManualHuawei(Parameters parameters, CameraWrapperInterface cameraUiWrapper, AppSettingsManager.TypeSettingsMode settingMode) {
        super(parameters,cameraUiWrapper,settingMode);
    }

    @Override
    public void SetValue(int valueToSet)
    {
        currentInt = valueToSet;

        if (valueToSet == 0)
        {
            cameraUiWrapper.GetParameterHandler().FocusMode.SetValue(cameraUiWrapper.getResString(R.string.auto_), true);
            parameters.set(cameraUiWrapper.GetAppSettingsManager().getResString(R.string.hw_hwcamera_flag),cameraUiWrapper.getResString(R.string.on_));
            parameters.set(cameraUiWrapper.GetAppSettingsManager().getResString(R.string.hw_manual_focus_mode),cameraUiWrapper.getResString(R.string.off_));
        }
        else
        {
            if ((!manualFocusModeString.equals("") || manualFocusModeString == null)&& !cameraUiWrapper.GetParameterHandler().FocusMode.GetValue().equals(manualFocusModeString)) //do not set "manual" to "manual"
                cameraUiWrapper.GetParameterHandler().FocusMode.SetValue(manualFocusModeString, false);
            parameters.set(cameraUiWrapper.getResString(R.string.hw_hwcamera_flag),cameraUiWrapper.getResString(R.string.on_));
            parameters.set(cameraUiWrapper.getResString(R.string.hw_manual_focus_mode),cameraUiWrapper.getResString(R.string.on_));
            parameters.set(key_value, stringvalues[currentInt]);
            Log.d(TAG, "Set " + key_value + " to : " + stringvalues[currentInt]);
            ((ParametersHandler) cameraUiWrapper.GetParameterHandler()).SetParametersToCamera(parameters);
        }
    }
}