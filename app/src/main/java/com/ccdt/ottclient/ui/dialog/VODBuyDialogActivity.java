package com.ccdt.ottclient.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.MyApp;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.exception.SuperException;
import com.ccdt.ottclient.model.OrderObj;
import com.ccdt.ottclient.model.ProductInfo;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.PayTask;
import com.ccdt.ottclient.tasks.impl.ProductOrderTask;
import com.ccdt.ottclient.ui.activity.BaseActivity;
import com.ccdt.ottclient.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购买影片弹出框
 */
public class VODBuyDialogActivity extends BaseActivity implements View.OnClickListener, TaskCallback {

    private ListView mProductList;
    private ProductListAdapter mAdapter;
    private View mConfrim;
    private View mBack;

    private String assetId;
    private ProductInfo productInfo;
    private String orderId;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assetId = getIntent().getStringExtra("assetId");
        productInfo = (ProductInfo) getIntent().getSerializableExtra("productInfo");
        Log.d("slf-VODBuyDialog", "productInfo = "+productInfo);
        initOperator();
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_dialog_vod_buy;
    }

    @Override
    protected void initWidget() {
        mProductList = (ListView) findViewById(R.id.product_list);
        mConfrim = findViewById(R.id.confirm);
        mBack = findViewById(R.id.back);
    }


    private void initOperator() {
        mConfrim.requestFocus();
        mConfrim.setOnClickListener(this);
        mBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (productInfo != null && productInfo.getData() != null) {
            mProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mAdapter.setSelect(position);
                }
            });
            mAdapter = new ProductListAdapter(this, productInfo.getData());
            mProductList.setAdapter(mAdapter);
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.confirm:
                if (mAdapter != null && mAdapter.getSelect() != -1
                        && !TextUtils.isEmpty(productId)) {
//                    new OrderTask().execute(productId,assetId);
                    Map<String, String> map = new HashMap<>();
                    map.put("productId", productId);
                    map.put("assetId", assetId);

                    new ProductOrderTask(this).execute(map);

                } else {
                    ToastUtil.toast(R.string.at_least_select_one_product);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onTaskFinished(TaskResult result) {
        if (result != null) {
            switch (result.taskId) {
                case Constants.TASK_PRODUCTORDER:
                    Intent intent = new Intent();
                    if (result.data != null) {
                        if (result.data instanceof OrderObj) {
                            OrderObj order = (OrderObj) result.data;
                            intent.putExtra("order", order);
                        }
                    }
                    intent.putExtra("code", result.code);
                    intent.putExtra("message", result.message);
                    setResult(8888, intent);
//                    ToastUtil.toast(R.string.buy_product_error);
                    finish();
                    break;
                default:
                    break;
            }


        } else {

        }


    }


    public class ProductListAdapter extends BaseAdapter {

        private List<ProductInfo.Product> list;
        private LayoutInflater mInflater;

        private int selectIndex = -1;

        public void setSelect(int index) {
            selectIndex = index;
            productId = list.get(selectIndex).getProductId();
            notifyDataSetChanged();
        }

        public int getSelect() {
            return selectIndex;
        }

        public ProductListAdapter(Context context, List<ProductInfo.Product> list) {
            this.list = list;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return list == null ? null : list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.vod_product_list_item, null);
                holder = new ViewHolder();
                holder.select = (RadioButton) convertView.findViewById(R.id.select);
                holder.detail = (TextView) convertView.findViewById(R.id.info);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ProductInfo.Product info = list.get(position);
            holder.detail.setText(info.getRemark());

            if (selectIndex == position) {
                holder.select.setChecked(true);
                holder.detail.setTextColor(getResources().getColor(R.color.color_95232b));
            } else {
                holder.select.setChecked(false);
                holder.detail.setTextColor(getResources().getColor(R.color.color_474554));
            }

            return convertView;
        }

        class ViewHolder {
            RadioButton select;
            TextView detail;
        }
    }


//    private class OrderTask extends AsyncTask<Object, Void, String> implements TaskCallback {
//
//        @Override
//        protected String doInBackground(Object... params) {
//            try {
//                return MyApp.getOTTApi().productOrder((String) params[0], (String) params[1], "");
//            } catch (Exception e) {
//                if (e instanceof SuperException) {
//                    ToastUtil.toast(((SuperException) e).getErrorDesc());
//                }
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            if (TextUtils.isEmpty(s)) {
//                try {
//                    JSONObject object = new JSONObject(s);
//                    orderId = object.optString("orderId");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                if (TextUtils.isEmpty(orderId)) {
//                    (new PayTask(this)).execute(orderId);
//                }
//
//            }
//            super.onPostExecute(s);
//        }
//
//        @Override
//        public void onTaskFinished(TaskResult result) {
//            if (result.taskId == Constants.TASK_PAY) {
//                setResult(8888);
//                finish();
//            }
//
//        }
//    }


}