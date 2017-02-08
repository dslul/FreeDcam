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

package freed.cam.apis.camera1.parameters.modes;

import android.hardware.Camera;

import com.troop.freedcam.R;

import freed.cam.apis.KEYS;
import freed.cam.apis.basecamera.CameraWrapperInterface;
import freed.cam.apis.camera1.parameters.ParametersHandler;

/**
 * Created by troop on 10.06.2016.
 */
public class NightModeXiaomi extends BaseModeParameter
{
    final String TAG = NightModeZTE.class.getSimpleName();
    private boolean visible = true;
    private String state = "";
    private String format = "";
    private String curmodule = "";

    public NightModeXiaomi(Camera.Parameters parameters, CameraWrapperInterface cameraUiWrapper) {
        super(parameters, cameraUiWrapper);
        if(parameters.get(KEYS.MORPHO_HHT) != null && parameters.get(cameraUiWrapper.GetAppSettingsManager().getResString(R.string.ae_bracket_hdr)) != null) {
            isSupported = true;
            isVisible = true;
            cameraUiWrapper.GetModuleHandler().addListner(this);
            cameraUiWrapper.GetParameterHandler().PictureFormat.addEventListner(this);
        }
    }

    @Override
    public boolean IsSupported()
    {
        return isSupported;
    }

    @Override
    public void SetValue(String valueToSet, boolean setToCam)
    {
        if (valueToSet.equals(KEYS.ON)) {
            parameters.set(KEYS.MORPHO_HDR, KEYS.FALSE);
            cameraUiWrapper.GetParameterHandler().HDRMode.onValueHasChanged(KEYS.OFF);
            parameters.set("capture-burst-exposures","-10,0,10");
            parameters.set(cameraUiWrapper.GetAppSettingsManager().getResString(R.string.ae_bracket_hdr), cameraUiWrapper.GetAppSettingsManager().getResString(R.string.ae_bracket_hdr_values_aebracket));
            parameters.set(KEYS.MORPHO_HHT, KEYS.TRUE);
        } else {
            parameters.set(cameraUiWrapper.GetAppSettingsManager().getResString(R.string.ae_bracket_hdr), cameraUiWrapper.GetAppSettingsManager().getResString(R.string.ae_bracket_hdr_values_aebracket));
            parameters.set(KEYS.MORPHO_HHT, KEYS.FALSE);
        }
        ((ParametersHandler) cameraUiWrapper.GetParameterHandler()).SetParametersToCamera(parameters);
        onValueHasChanged(valueToSet);

    }

    @Override
    public String GetValue()
    {
        if (parameters.get(KEYS.MORPHO_HHT).equals(KEYS.TRUE)
                && parameters.get(cameraUiWrapper.GetAppSettingsManager().getResString(R.string.ae_bracket_hdr)).equals(cameraUiWrapper.GetAppSettingsManager().getResString(R.string.ae_bracket_hdr_values_off)))
            return KEYS.ON;
        else
            return KEYS.OFF;
    }

    @Override
    public String[] GetValues()
    {
        return new String[] {KEYS.OFF,KEYS.ON};
    }

    @Override
    public void onModuleChanged(String module)
    {
        curmodule = module;
        switch (module)
        {
            case KEYS.MODULE_VIDEO:
            case KEYS.MODULE_HDR:
                Hide();
                break;
            default:
                if (format.contains(KEYS.JPEG)) {
                    Show();
                    onIsSupportedChanged(true);
                }
        }
    }

    @Override
    public void onParameterValueChanged(String val)
    {
        format = val;
        if (val.contains(KEYS.JPEG)&&!visible &&!curmodule.equals(KEYS.MODULE_HDR))
            Show();

        else if (!val.contains(KEYS.JPEG)&& visible) {
            Hide();
        }
    }

    private void Hide()
    {
        state = GetValue();
        visible = false;
        SetValue(KEYS.OFF,true);
        onValueHasChanged(KEYS.OFF);
        onIsSupportedChanged(visible);
    }

    private void Show()
    {
        visible = true;
        SetValue(state,true);
        onValueHasChanged(state);
        onIsSupportedChanged(visible);
    }
}
