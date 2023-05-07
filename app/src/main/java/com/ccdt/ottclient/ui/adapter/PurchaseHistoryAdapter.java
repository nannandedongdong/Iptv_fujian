package com.ccdt.ottclient.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.PurchaseHistoryObj;
import com.ccdt.ottclient.utils.LogUtil;

import java.util.List;

public class PurchaseHistoryAdapter extends BaseAdapter {

    private static final String TAG = "PurchaseHistoryAdapter";

    private Context mContext;
    private List<PurchaseHistoryObj> mDataSet;
    private BitmapDrawable drawable2;
    private BitmapDrawable drawable1;
    private GradientDrawable selectedDrawable;
    private Bitmap bitmap1;
    private Bitmap bitmap2;


    public PurchaseHistoryAdapter(Context context, List<PurchaseHistoryObj> dataSet) {
        this.mContext = context;
        this.mDataSet = dataSet;
        initBackgroundDrawable();
    }


    @Override
    public int getCount() {
        int ret = 0;
        if (mDataSet != null) {
            ret = mDataSet.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return mDataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_purchase_history, parent, false);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            holder.txtOrderTime = (TextView) convertView.findViewById(R.id.txtOrderTime);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
            holder.txtOperation = (TextView) convertView.findViewById(R.id.txtOperation);
            holder.itemView = convertView.findViewById(R.id.layout_bg);
            holder.position = position;
            convertView.setTag(holder);
        }

        if (position % 2 == 0) {
            convertView.setBackgroundDrawable(drawable1);
        } else {
            convertView.setBackgroundDrawable(drawable2);
        }

        PurchaseHistoryObj obj = mDataSet.get(position);
        if (obj != null) {
            holder.txtName.setText(obj.getName());
            holder.txtPrice.setText(String.valueOf(obj.getPrice()) + ("0".equals(obj.getFlag()) ? "元/月" : "元/次"));
            holder.txtOrderTime.setText(obj.getOrderTime());
            holder.txtStatus.setText(obj.getStatusText());
            String strOperation = "";
            if("1".equals(obj.getPayFlag())){
                // 已支付
                if ("by".equals(obj.getProductId())) {
                    // 包月
                    if("1".equals(obj.getStatus())){
                        //包月订单状态，1表示下个月自动续订、0表示退订
                        strOperation = "按OK键退订";
                    } else {
                        strOperation = "按OK键续订";
                    }
                } else {
                    // 按次
                    strOperation = "已支付";
                }
            } else {
                // 未支付
                strOperation = "按OK键支付";
            }
            holder.txtOperation.setText(strOperation);
        }
        return convertView;
    }


    public static class ViewHolder {
        public int position;
        public View itemView;
        public TextView txtName;
        public TextView txtPrice;
        public TextView txtOrderTime;
        public TextView txtStatus;
        public TextView txtOperation;
    }

    public void initBackgroundDrawable() {
        Paint paint_1_1 = new Paint();
        paint_1_1.setColor(mContext.getResources().getColor(R.color.PurchaseHistory_1_1));
//        paint_1_1.setColor(Color.parseColor("#0c172e"));
//        paint_1_1.setAlpha(51);
        paint_1_1.setStyle(Paint.Style.FILL_AND_STROKE);

        Paint paint_1_2 = new Paint();
        paint_1_2.setColor(mContext.getResources().getColor(R.color.PurchaseHistory_1_2));
//        paint_1_2.setColor(Color.parseColor("#0c172e"));
//        paint_1_1.setAlpha(178);
        paint_1_2.setStyle(Paint.Style.FILL_AND_STROKE);

        Paint paint_2_1 = new Paint();
        paint_2_1.setColor(mContext.getResources().getColor(R.color.PurchaseHistory_2_1));
//        paint_2_1.setColor(Color.parseColor("#afc2ff"));
        paint_2_1.setStyle(Paint.Style.FILL_AND_STROKE);

        Paint paint_2_2 = new Paint();
        paint_2_2.setColor(mContext.getResources().getColor(R.color.PurchaseHistory_2_2));
        paint_2_2.setStyle(Paint.Style.FILL_AND_STROKE);

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        int itemWidth = width * 9 / 10;
        int width_1 = itemWidth * 12 / 41;
        int width_2 = itemWidth * 5 / 41;
        int width_3 = itemWidth * 12 / 41;
        int width_4 = itemWidth * 6 / 41;
        int width_5 = itemWidth * 6 / 41;

        int width_12 = width_1 + width_2;
        int width_123 = width_12 + width_3;
        int width_1234 = width_123 + width_4;

        bitmap1 = Bitmap.createBitmap(itemWidth, 70, Bitmap.Config.RGB_565);
        Canvas canvas1 = new Canvas(bitmap1);
        canvas1.drawRect(0, 0, width_1, 70, paint_1_1);
        canvas1.drawRect(width_1, 0, width_12 + width_2, 70, paint_1_2);
        canvas1.drawRect(width_12, 0, width_123, 70, paint_1_1);
        canvas1.drawRect(width_123, 0, width_1234, 70, paint_1_2);
        canvas1.drawRect(width_1234, 0, itemWidth, 70, paint_1_1);
        drawable2 = new BitmapDrawable(mContext.getResources(), bitmap1);

        bitmap2 = Bitmap.createBitmap(itemWidth, 70, Bitmap.Config.RGB_565);
        Canvas canvas2 = new Canvas(bitmap2);
        canvas2.drawRect(0, 0, width_1, 70, paint_2_1);
        canvas2.drawRect(width_1, 0, width_12 + width_2, 70, paint_2_2);
        canvas2.drawRect(width_12, 0, width_123, 70, paint_2_1);
        canvas2.drawRect(width_123, 0, width_1234, 70, paint_2_2);
        canvas2.drawRect(width_1234, 0, itemWidth, 70, paint_2_1);
        drawable1 = new BitmapDrawable(mContext.getResources(), bitmap2);

        int colors[] = {
                mContext.getResources().getColor(R.color.PurchaseHistory_selector_left),
                mContext.getResources().getColor(R.color.PurchaseHistory_selector_right)
        };
        selectedDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        selectedDrawable.setStroke(2, Color.parseColor("#02c1ff"));
    }

    public void recycleBitmap() {
        if (bitmap1 != null) {
            bitmap1.recycle();
            LogUtil.d(TAG, "回收 bitmap1");
        }

        if (bitmap2 != null) {
            bitmap2.recycle();
            LogUtil.d(TAG, "回收 bitmap2");
        }
    }

    public GradientDrawable getSelectedDrawable() {
        return selectedDrawable;
    }

    public BitmapDrawable getDrawable1() {
        return drawable1;
    }

    public BitmapDrawable getDrawable2() {
        return drawable2;
    }
}
