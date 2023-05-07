package com.ccdt.ottclient.ui.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.utils.SPUtils;

import java.io.IOException;


public class SystemHiddenActivity extends BaseActivity implements View.OnClickListener {

    public View btn_sure;
    public View btn_cancel;
    public EditText edt_ip;
    private EditText etSiteid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_system_hidden;
    }

    @Override
    protected void initWidget() {
        btn_sure = findViewById(R.id.btn_sure);
        btn_cancel = findViewById(R.id.btn_cancel);
        edt_ip = ((EditText) findViewById(R.id.edt_ip));
        etSiteid = ((EditText) findViewById(R.id.id_et_siteid));

        btn_sure.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        edt_ip.setText(String.valueOf(SPUtils.get(this, "AUTH_IP", "")));
        etSiteid.setText(String.valueOf(SPUtils.get(this, "SITE_ID","")));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_sure:

                saveSystemConfig();
                reboot();

                break;
            case R.id.btn_cancel:
                this.finish();
                break;
            default:
                break;
        }

    }

    private void saveSystemConfig(){
        String ip = edt_ip.getText().toString();
        String siteId = etSiteid.getText().toString();

        SPUtils.put(this, "AUTH_IP", ip);
        SPUtils.put(this, "SITE_ID", siteId);
    }

    /**
     * 重启应用
     */
    private void reboot(){
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        ScreenManager.getScreenManager().popAllActivityExceptOne(null);

//        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        am.restartPackage(getPackageName());

    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context, SystemHiddenActivity.class);
        context.startActivity(intent);
    }
}
