package com.app.wpf.wfilemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.app.wpf.filetools.Util.Config;
import com.app.wpf.wfilemanager.Utils.ConfigSharePreference;

public class SetActivity extends AppCompatActivity implements
    Switch.OnCheckedChangeListener {

    private Switch aSwitch_yc,aSwitch_yx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        init();
    }

    private void init() {
        aSwitch_yc = (Switch) findViewById(R.id.switch_yc);
        aSwitch_yx = (Switch) findViewById(R.id.switch_yx);
        aSwitch_yc.setOnCheckedChangeListener(this);
        aSwitch_yx.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        aSwitch_yc.setChecked(ConfigSharePreference.getBooleanYC(this));
        aSwitch_yx.setChecked(ConfigSharePreference.getBooleanYX(this));
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(compoundButton == aSwitch_yc) {
            ConfigSharePreference.setBooleanYC(this, b);
            Config.canScanHideFile = ConfigSharePreference.getBooleanYC(this);
        } else if(compoundButton == aSwitch_yx) {
            ConfigSharePreference.setBooleanYX(this, b);
            Config.preferentialDisplayFolder = ConfigSharePreference.getBooleanYX(this);
        }
    }
}
