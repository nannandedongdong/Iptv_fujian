package com.ccdt.ottclient.ui.dialog;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.OrderObj;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.PayTask;
import com.ccdt.ottclient.utils.StringUtil;
import com.ccdt.ottclient.utils.ToastUtil;
import com.slf.frame.tv.widget.component.ColorPhrase;

public class PayDialogActivity extends Activity implements View.OnClickListener, TaskCallback {
    private TextView txtContent;
    private View btnPay;
    private View btnCancel;
    private OrderObj mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity_pay);

        txtContent = ((TextView) findViewById(R.id.txt_content));
        btnPay = findViewById(R.id.btn_pay);
        btnCancel = findViewById(R.id.btn_cancel);

        btnPay.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        Intent intent = getIntent();
        mOrder = intent.getParcelableExtra("order");
        String orderId = "";
        String prodectName = "";
        int price = 0;
        String endTime = "";
        String empty = "";
        if (mOrder != null) {
            orderId = StringUtil.getNotNullStr(mOrder.getOrderId(), empty);
            prodectName = StringUtil.getNotNullStr(mOrder.getProductName(), empty);
            price = mOrder.getProductPrice();
            endTime = mOrder.getEndTime();
        }

        String pattern = String.format("您的订单已生成，订单号【{%s}】，产品名称【{%s}】，需付款人民币{%d}元，有效期从即日起至【{%s}】，请完成付款！", orderId, prodectName, price, endTime);

        CharSequence chars = ColorPhrase.from(pattern).withSeparator("{}").innerColor(0xFFFC9600).outerColor(0xFF465281).format();

        txtContent.setText(chars);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_pay:
                new PayTask(this).execute(String.valueOf(mOrder.getId()));
                break;
            case R.id.btn_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onTaskFinished(TaskResult result) {
        if (result != null) {
            ToastUtil.toast(result.message);
            Intent intent = new Intent();
            intent.putExtra("code", result.code);
            setResult(999, intent);
        } else {
            ToastUtil.toast("支付失败");
        }
        finish();
    }
}
