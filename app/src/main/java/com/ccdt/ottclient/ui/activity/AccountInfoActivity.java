package com.ccdt.ottclient.ui.activity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ccdt.ottclient.Account;
import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.AccountInfo;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.GetAccountInfoTask;
import com.ccdt.ottclient.utils.LogUtil;
import com.ccdt.ottclient.utils.ToastUtil;

public class AccountInfoActivity extends BaseActivity implements TaskCallback, View.OnClickListener {
    private TextView tvName;
    private TextView tvBanlance;
    private TextView tvAccout;
    private TextView tvNum;
    private Button btnSet;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_account;
    }


    @Override
    protected void initWidget() {
        tvName = ((TextView) findViewById(R.id.tv_name));
        tvBanlance = ((TextView) findViewById(R.id.tv_balance));
        tvAccout = ((TextView) findViewById(R.id.tv_acount));
        tvNum = ((TextView) findViewById(R.id.tv_num));
        tvName.setText(Account.getInstance().userName);
        tvAccout.setText(Account.getInstance().userId);
        btnSet = ((Button) findViewById(R.id.id_set));
        btnSet.setOnClickListener(this);

        new GetAccountInfoTask(this).execute();


//        Account acc = Account.getInstance();
//        if (acc != null) {
//            AccountInfo info = acc.info;
//            if (info != null) {
//                tvName.setText(info.terminalAccountName);
//                tvAccout.setText(info.terminalAccount);
//                double bal = 0;
//                if(info.balance > 0){
//                    bal = info.balance;
//                }
//                tvBanlance.setText(String.valueOf(bal) + " 元");
//                tvNum.setText(info.terminalSn);
//            }
//        }
    }

    @Override
    public void onTaskFinished(TaskResult result) {
        if (result != null) {
            switch (result.taskId) {
                case Constants.TASK_GETACCOUNTINFO:
                    if (result.code == 200) {
                        LogUtil.d(TAG, "加载用户信息成功");
                        LogUtil.d(TAG, result.data.toString());
                        if (result.data != null) {
                            AccountInfo info = (AccountInfo) result.data;
                            Account.getInstance().info = info;
                            if (info != null) {
                                tvName.setText(info.terminalAccountName);
                                tvAccout.setText(info.terminalAccount);
                                double bal = 0;
                                if (info.balance > 0) {
                                    bal = info.balance;
                                }
                                tvBanlance.setText(String.valueOf(bal) + " 元");
                                tvNum.setText(info.terminalSn);
                            }

                        }

                    } else {
                        LogUtil.d(TAG, "加载用户信息失败 error:" + result.message);
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*
         183 红
         185 黄
         186 蓝
         184 绿
         */
        if(183 == keyCode){
            Surprise();
        }
        return super.onKeyDown(keyCode, event);
    }

    private long exitTime = 0;
    // 连续按两下红
    public void Surprise() {
        if ((System.currentTimeMillis() - exitTime) > 1000) {
            exitTime = System.currentTimeMillis();
        } else {
            openSystemHiddenActivity();
        }
    }

    private void openSystemHiddenActivity(){
        SystemHiddenActivity.actionStart(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.id_set:
                // 打开设置ip
                openSystemHiddenActivity();
                break;
        }
    }
}
