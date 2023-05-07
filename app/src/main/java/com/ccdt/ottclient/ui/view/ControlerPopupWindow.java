package com.ccdt.ottclient.ui.view;

import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.VisitAddress;
import com.ccdt.ottclient.ui.adapter.RateListAdapter;

import java.util.List;

/**
 * @Package com.ccdt.stb.movies.ui.view
 * @ClassName: ControlerPopupWindow
 * @Description: 多码率切换 弹出窗
 * @author hezb
 * @date 2015年6月19日 下午7:21:29
 */

public class ControlerPopupWindow extends PopupWindow {

    private Context mContext;
    private View mRootView;
    private ListView mRateList;

    private OnRateChangeListener mListener;
    private RateListAdapter adapter;
    
    private final int AUTO_DISMISS_TIME = 5000;
    private Handler mHandler;
    private Runnable autoDismiss = new Runnable() {
        
        @Override
        public void run() {
            dismiss();
        }
    };

    public ControlerPopupWindow(Context context) {
        this.mContext = context;
        mRootView = LayoutInflater.from(context)
                .inflate(R.layout.popupwindow_controler, null);
        setContentView(mRootView);
        setWidth(LayoutParams.WRAP_CONTENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        mRateList = (ListView) mRootView.findViewById(R.id.rate_list);

        initOperator();
    }


    private void initOperator() {
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setAnimationStyle(R.style.PopupAnimation);

        mRateList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                    View view, int position, long id) {
                if (mListener != null && adapter.getSelected() != position) {
                    adapter.setOnSelectedChange(position);
                    mListener.onRateChange(
                            adapter.getItem(position).getBfUrl());
                }
            }
        });
        mRateList.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                    View view, int position, long id) {
                mHandler.removeCallbacks(autoDismiss);
                mHandler.postDelayed(autoDismiss, AUTO_DISMISS_TIME);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                
            }
        });
        mRateList.setOnKeyListener(new OnKeyListener() {
            
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        mHandler = new Handler();
    }
    
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        if (adapter != null && adapter.getCount() != 0) {
            mRateList.requestFocus();
            mHandler.postDelayed(autoDismiss, AUTO_DISMISS_TIME);
        } else {
            dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mHandler.removeCallbacks(autoDismiss);
    }
    
    /**
     * 设置码率地址
     */
     public void setRateList(List<VisitAddress> visitAddress) {
         adapter = new RateListAdapter(mContext, visitAddress);
         mRateList.setAdapter(adapter);
     }


     public void setOnRateChangeListener(OnRateChangeListener listener) {
         this.mListener = listener;
     }

     /**
      * 切换码率回调
      */
     public interface OnRateChangeListener {

         void onRateChange(String playUrl);

     }
}
