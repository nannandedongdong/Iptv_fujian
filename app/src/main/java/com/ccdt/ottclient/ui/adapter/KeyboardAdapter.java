package com.ccdt.ottclient.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccdt.ottclient.R;

public class KeyboardAdapter extends RecyclerView.Adapter<KeyboardAdapter.ViewHolder> {

    private boolean isNeedReqeustFocus;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_search_keyboard, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.txtKey.setText(KEYBORAD_KKEYS[position]);
        holder.txtKey.setId(KEYBORAD_KKEYS_IDS[position]);
        holder.txtKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mKeyboardCallbackListener != null) {
                    mKeyboardCallbackListener.keyboardCallback(KEYBORAD_KKEYS[position]);
                }
            }
        });
        if (position % 6 == 5) {
            holder.txtKey.setNextFocusRightId(R.id.view_middle);
        }
        if(isNeedReqeustFocus && position == 0){
            holder.txtKey.requestFocus();
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (KEYBORAD_KKEYS != null) {
            ret = KEYBORAD_KKEYS.length;
        }
        return ret;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtKey;

        public ViewHolder(View itemView) {
            super(itemView);
            txtKey = (TextView) itemView.findViewById(R.id.txt_item_search_keyboard_key);
        }
    }

    private Context mContext;

    public KeyboardAdapter(Context context) {
        this.isNeedReqeustFocus = true;
        this.mContext = context;
    }

    private KeyboardCallbackListener mKeyboardCallbackListener;

    public void setKeyboardCallbackListener(KeyboardCallbackListener keyboardCallbackListener) {
        this.mKeyboardCallbackListener = keyboardCallbackListener;
    }

    public interface KeyboardCallbackListener {
        void keyboardCallback(String key);
    }

    private static final String[] KEYBORAD_KKEYS = new String[]{
            "A", "B", "C", "D", "E", "F"
            , "G", "H", "I", "J", "K", "L"
            , "M", "N", "O", "P", "Q", "R"
            , "S", "T", "U", "V", "W", "X"
            , "Y", "Z", "0", "1", "2", "3"
            , "4", "5", "6", "7", "8", "9"};
    private static final int[] KEYBORAD_KKEYS_IDS = new int[]{
            R.id.search_keyboard_key_A,
            R.id.search_keyboard_key_B,
            R.id.search_keyboard_key_C,
            R.id.search_keyboard_key_D,
            R.id.search_keyboard_key_E,
            R.id.search_keyboard_key_F,
            R.id.search_keyboard_key_G,
            R.id.search_keyboard_key_H,
            R.id.search_keyboard_key_I,
            R.id.search_keyboard_key_J,
            R.id.search_keyboard_key_K,
            R.id.search_keyboard_key_L,
            R.id.search_keyboard_key_M,
            R.id.search_keyboard_key_N,
            R.id.search_keyboard_key_O,
            R.id.search_keyboard_key_P,
            R.id.search_keyboard_key_Q,
            R.id.search_keyboard_key_R,
            R.id.search_keyboard_key_S,
            R.id.search_keyboard_key_T,
            R.id.search_keyboard_key_U,
            R.id.search_keyboard_key_V,
            R.id.search_keyboard_key_W,
            R.id.search_keyboard_key_X,
            R.id.search_keyboard_key_Y,
            R.id.search_keyboard_key_Z,
            R.id.search_keyboard_key_0,
            R.id.search_keyboard_key_1,
            R.id.search_keyboard_key_2,
            R.id.search_keyboard_key_3,
            R.id.search_keyboard_key_4,
            R.id.search_keyboard_key_5,
            R.id.search_keyboard_key_6,
            R.id.search_keyboard_key_7,
            R.id.search_keyboard_key_8,
            R.id.search_keyboard_key_9
    };
}
