package com.ccdt.ottclient.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.ui.adapter.KeyboardAdapter;
import com.ccdt.ottclient.utils.ToastUtil;

public class SearchKeyboardFragment extends BaseFragment implements View.OnClickListener, KeyboardAdapter.KeyboardCallbackListener {
    private RecyclerView recycler;
    private EditText txtSearch;
    private StringBuilder sbKeys;

    private static final int KEYBOARD_KEY = 1;
    private static final int KEYBOARD_DEL = 0;
    private static final int KEYBOARD_CLEAR = -1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_keyboard, container, false);

//        gridKeyBoard = ((GridView) v.findViewById(R.id.grid_search_keyboard));
        recycler = ((RecyclerView) v.findViewById(R.id.grid_search_keyboard));
        txtSearch = ((EditText) v.findViewById(R.id.txtSearch));
        View btnDel = v.findViewById(R.id.btn_delete);
        View btnClear = v.findViewById(R.id.btn_clear);
        btnDel.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 6);

        KeyboardAdapter adapter = new KeyboardAdapter(mContext);
        adapter.setKeyboardCallbackListener(this);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
        sbKeys = new StringBuilder();

        return v;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_delete:
//                ToastUtil.toast("点击了：删除");
                keyboardEvent(KEYBOARD_DEL, null);
                break;
            case R.id.btn_clear:
//                ToastUtil.toast("点击了：清空");
                keyboardEvent(KEYBOARD_CLEAR, null);
                break;
            default:
                break;
        }
    }

    public View getNeedFocusView() {
        View ret = null;
        RecyclerView.ViewHolder holder = recycler.findViewHolderForLayoutPosition(0);
        if (holder != null) {
            ret = holder.itemView;
        }
        return ret;
    }

    private void keyboardEvent(int eventType, String msg) {
        int len = sbKeys.length();
        switch (eventType) {
            case KEYBOARD_KEY:
                sbKeys.append(msg);
                break;
            case KEYBOARD_DEL:
                if (len > 0) {
                    sbKeys.deleteCharAt(len - 1);
                }
                break;
            case KEYBOARD_CLEAR:
                if (len > 0) {
                    sbKeys.delete(0, len);
                }
                break;
        }
        txtSearch.setText(sbKeys.toString());
        if (mSearchKeyboardCallbackListener != null) {
            mSearchKeyboardCallbackListener.searchKeyboardCallback(sbKeys.toString());
        }
    }

    @Override
    public void keyboardCallback(String key) {
//        ToastUtil.toast("点击了：" + key);
        keyboardEvent(KEYBOARD_KEY, key);

    }

    private SearchKeyboardCallbackListener mSearchKeyboardCallbackListener;

    public interface SearchKeyboardCallbackListener {
        void searchKeyboardCallback(String keyStr);
    }

    public void setSearchKeyboardCallbackListener(SearchKeyboardCallbackListener listener) {
        this.mSearchKeyboardCallbackListener = listener;
    }

}
