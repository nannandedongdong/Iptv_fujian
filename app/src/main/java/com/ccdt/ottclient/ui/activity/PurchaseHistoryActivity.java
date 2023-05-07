package com.ccdt.ottclient.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ccdt.ottclient.Account;
import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.AccountInfo;
import com.ccdt.ottclient.model.InvokeResult;
import com.ccdt.ottclient.model.MonthOrderObj;
import com.ccdt.ottclient.model.NumOrderObj;
import com.ccdt.ottclient.model.PurchaseHistoryObj;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.GetMyMonthOrderTask;
import com.ccdt.ottclient.tasks.impl.GetMyNumOrderTask;
import com.ccdt.ottclient.tasks.impl.PayTask;
import com.ccdt.ottclient.tasks.impl.SubscribeTask;
import com.ccdt.ottclient.tasks.impl.UnsubscribeTask;
import com.ccdt.ottclient.ui.adapter.PurchaseHistoryAdapter;
import com.ccdt.ottclient.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 购买记录Activity
 */
public class PurchaseHistoryActivity extends BaseActivity implements TaskCallback, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, View.OnClickListener {

    private Dialog mDialog;
    private PurchaseHistoryObj mHistoryObj;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PurchaseHistoryActivity.class);
        context.startActivity(intent);
    }

    private ListView mListViewPurchaseHistory;
    private List<PurchaseHistoryObj> mDataSet = new ArrayList<>();
    private PurchaseHistoryAdapter adapter;
    private boolean refreshFlag = true;
    private TextView txtBalance;
    private PurchaseHistoryAdapter.ViewHolder mHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);
        initWidget();
//        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
//        mDialog = mBuilder.create();
        mDialog = new Dialog(this,R.style.CustomDialog);


        View view_dialog = LayoutInflater.from(this).inflate(R.layout.dialog_purhis, null);
        View btnOk = view_dialog.findViewById(R.id.btn_ok);
        View btnCancel = view_dialog.findViewById(R.id.btn_cancel);
//        mBuilder.setView(view_dialog);
//        mBuilder.setCancelable(true);
//        Window window = mDialog.getWindow();
//
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.alpha = 0.9f;
//        window.setAttributes(lp);

        mDialog.setContentView(view_dialog);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        adapter = new PurchaseHistoryAdapter(this, mDataSet);
        mListViewPurchaseHistory.setAdapter(adapter);
        mListViewPurchaseHistory.setVerticalScrollBarEnabled(false);
        mListViewPurchaseHistory.setOnItemSelectedListener(this);
        mListViewPurchaseHistory.setOnItemClickListener(this);

        initData();
    }

    private void initData() {
        mDataSet.clear();
        AccountInfo info = Account.getInstance().info;
        double bal = 0;
        if (info != null) {
            if (info.balance > 0) {
                bal = info.balance;
            }
        }

        txtBalance.setText(String.valueOf(bal) + " 元");

        Map<String, String> params = new HashMap<>();
        params.put("pageSize", "2000");
        new GetMyMonthOrderTask(this).execute(params);
        new GetMyNumOrderTask(this).execute(params);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_purchase_history;
    }

    @Override
    public void initWidget() {
        txtBalance = ((TextView) findViewById(R.id.txtBalance));
        mListViewPurchaseHistory = ((ListView) findViewById(R.id.purchase_history_lv));
    }

//    @Override
//    public void onClick(View v) {
//        Map<String, String> params = new HashMap<>();
//        params.put("pageSize", "1000");
//        int id = v.getProductId();
//        switch (id) {
//            case R.id.btnGetMyMonthOrder:
//                new GetMyMonthOrderTask(this).execute(params);
//                break;
//            case R.id.btnGetMyNumOrder:
//                new GetMyNumOrderTask(this).execute(params);
//                break;
//            default:
//                break;
//        }
//        params = null;
//    }

    @Override
    public void onTaskFinished(TaskResult result) {
        PurchaseHistoryObj purchaseHistoryObj;
        if (result != null) {
            int taskId = result.taskId;
            switch (taskId) {
                case Constants.TASK_GETMYMONTHORDER:
                    if (200 == result.code) {
                        if (result.data != null) {
                            InvokeResult<MonthOrderObj> invokeResult = (InvokeResult<MonthOrderObj>) result.data;
                            List<MonthOrderObj> monthOrderObjs = invokeResult.getdList();
                            if (monthOrderObjs != null && monthOrderObjs.size() > 0) {
                                for (MonthOrderObj obj : monthOrderObjs) {
                                    if (obj != null) {
                                        purchaseHistoryObj = new PurchaseHistoryObj();
                                        purchaseHistoryObj.setProductId(obj.getProductId());
                                        purchaseHistoryObj.setName(obj.getProductName());
                                        purchaseHistoryObj.setStatus(obj.getSubscribeFlag());
                                        purchaseHistoryObj.setStatusText(obj.getSubscribeText());
                                        purchaseHistoryObj.setOrderTime(obj.getOrderTime());
                                        purchaseHistoryObj.setPrice(obj.getOrderPrice());
                                        purchaseHistoryObj.setFlag("0");
                                        purchaseHistoryObj.setOrderId(obj.getId());

                                        purchaseHistoryObj.setModelNumStr(obj.getModelNumStr());
                                        purchaseHistoryObj.setPayType(obj.getPayType());
                                        purchaseHistoryObj.setPayFlag(obj.getPayFlag());
                                        purchaseHistoryObj.setDeficitFlag(obj.getDeficitFlag());
                                        purchaseHistoryObj.setBeginTime(obj.getBeginTime());
                                        purchaseHistoryObj.setEndTime(obj.getEndTime());

                                        mDataSet.add(purchaseHistoryObj);
                                    }
                                }
                            }
                        }
                    }
                    refreshFlag = !refreshFlag;
                    refreshListView(refreshFlag);
                    break;
                case Constants.TASK_GETMYNUMORDER:
                    if (200 == result.code) {
                        if (result.data != null) {
                            InvokeResult<NumOrderObj> invokeResult = (InvokeResult<NumOrderObj>) result.data;
                            List<NumOrderObj> monthOrderObjs = invokeResult.getdList();
                            if (monthOrderObjs != null && monthOrderObjs.size() > 0) {
                                for (NumOrderObj obj : monthOrderObjs) {
                                    if (obj != null) {
                                        purchaseHistoryObj = new PurchaseHistoryObj();
                                        purchaseHistoryObj.setProductId(obj.getAssetId());
                                        purchaseHistoryObj.setName(obj.getAssetName());
                                        purchaseHistoryObj.setStatus(obj.getStateCode());
                                        purchaseHistoryObj.setStatusText(obj.getStateText());
                                        purchaseHistoryObj.setOrderTime(obj.getOrderTime());
                                        purchaseHistoryObj.setPrice(obj.getOrderPrice());
                                        purchaseHistoryObj.setFlag("1");
                                        purchaseHistoryObj.setOrderId(obj.getId());

                                        purchaseHistoryObj.setModelNumStr(obj.getModelNumStr());
                                        purchaseHistoryObj.setPayType(obj.getPayType());
                                        purchaseHistoryObj.setPayFlag(obj.getPayFlag());
                                        purchaseHistoryObj.setDeficitFlag(obj.getDeficitFlag());
                                        purchaseHistoryObj.setBeginTime(obj.getBeginTime());
                                        purchaseHistoryObj.setEndTime(obj.getEndTime());
                                        mDataSet.add(purchaseHistoryObj);
                                    }
                                }
                            }
                        }
                    }
                    refreshFlag = !refreshFlag;
                    refreshListView(refreshFlag);
                    break;
                case Constants.TASK_PAY:
                    // 支付
                    if(200 == result.code){
                        ToastUtil.toast("支付成功");
                    } else {
                        ToastUtil.toast(result.message);
                    }

                    initData();
                    break;
                case Constants.TASK_UNSUBSCRIBE:
                    // 退订
                    if(200 == result.code){
                        ToastUtil.toast("退订成功");
                    } else {
                        ToastUtil.toast(result.message);
                    }

                    initData();
                    break;
                case Constants.TASK_SUBSCRIBE:
                    // 续订
                    if(200 == result.code){
                        ToastUtil.toast("续订成功");
                    } else {
                        ToastUtil.toast(result.message);
                    }

                    initData();
                    break;
                default:
                    break;
            }
//            LogUtil.i("---购买记录---", mDataSet.toString());
        }
    }

    private void refreshListView(boolean isRefresh) {
        if (isRefresh) {
            adapter.notifyDataSetChanged();
            mListViewPurchaseHistory.requestFocus();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (mHolder != null) {
            mHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
            mHolder.txtOperation.setBackgroundColor(Color.TRANSPARENT);
        }

        if (view != null) {
            Object tag = view.getTag();
            if (tag != null) {
                if (tag instanceof PurchaseHistoryAdapter.ViewHolder) {
                    mHolder = (PurchaseHistoryAdapter.ViewHolder) tag;
                    mHolder.itemView.setBackgroundDrawable(adapter.getSelectedDrawable());
                    mHolder.txtOperation.setBackgroundResource(R.drawable.bg_buy_history_btn);
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onDestroy() {
        adapter.recycleBitmap();
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        flag = -1;
        mHistoryObj = mDataSet.get(position);
        if (mHistoryObj != null) {
            String orderId = String.valueOf(mHistoryObj.getOrderId());
            String strOperation = "";
            if ("1".equals(mHistoryObj.getPayFlag())) {
                // 已支付
                if ("by".equals(mHistoryObj.getProductId())) {
                    // 包月
                    if ("1".equals(mHistoryObj.getStatus())) {
                        //包月订单状态，1表示下个月自动续订、0表示退订
                        strOperation = "按OK键退订";

                        flag = FLAG_UNSUBSCRIBE;
                    } else {
                        strOperation = "按OK键续订";

                        flag = FLAG_SUBSCRIBE;
                    }
                } else {
                    // 按次
                    strOperation = "已支付";
                }
            } else {
                // 未支付
                strOperation = "按OK键支付";

                flag = FLAG_PAY;
            }
            if (flag != -1) {
                mDialog.show();


            }
        }
    }

    private int flag = -1;
    private static final int FLAG_PAY = 0;
    private static final int FLAG_SUBSCRIBE = 1;
    private static final int FLAG_UNSUBSCRIBE = 2;

    private void sure() {
        switch (flag) {
            case FLAG_PAY:
                new PayTask(this).execute(String.valueOf(mHistoryObj.getOrderId()));
                break;
            case FLAG_SUBSCRIBE:
                new SubscribeTask(this).execute(mHistoryObj.getProductId());
                break;
            case FLAG_UNSUBSCRIBE:
                new UnsubscribeTask(this).execute(mHistoryObj.getProductId());
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_ok:
                sure();
                mDialog.dismiss();
                break;
            case R.id.btn_cancel:
                flag = -1;
                mDialog.dismiss();
                break;
            default:
                break;
        }
    }
}
