package com.ccdt.ottclient.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ccdt.ottclient.R;

public class AppPageItemLayout extends RelativeLayout {

    private Context mContext;
    private TextView txtName;
    private ImageView imgIcon;
    public Drawable icon;
    public CharSequence name;
    private String visibility;

    public AppPageItemLayout(Context context) {
        super(context);
        init(context, null);
    }

    public AppPageItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AppPageItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.item_appstore_main, this, true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AppPageItemLayout);
        icon = a.getDrawable(R.styleable.AppPageItemLayout_app_icon);
        name = a.getString(R.styleable.AppPageItemLayout_app_name);
        visibility = a.getString(R.styleable.AppPageItemLayout_app_name_visibility);

        a.recycle();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        this.txtName = (TextView) this.findViewById(R.id.app_name);
        this.imgIcon = (ImageView) this.findViewById(R.id.app_icon);

        if (this.imgIcon != null) {
            setIconDrawable(icon);
        }

        if (this.txtName != null) {
            setName(this.name);
        }

        if(!TextUtils.isEmpty(visibility)){
            if("gone".equals(visibility)){
                setNameVisibility(View.GONE);
            } else if("visible".equals(visibility)){
                setNameVisibility(View.VISIBLE);
            } else if("invisible".equals(visibility)){
                setNameVisibility(View.INVISIBLE);
            }
        }
    }

    public void setIconResource(int resourceId) {
        this.imgIcon.setImageResource(resourceId);
    }

    public void setIconDrawable(Drawable drawable) {
        this.imgIcon.setImageDrawable(drawable);
    }

    public void setName(String name) {
        this.txtName.setText(name);
    }

    public void setName(CharSequence name) {
        this.txtName.setText(name);
    }

    public void setName(int resourceId) {
        this.txtName.setText(resourceId);
    }

    /**
     * @param visibility One of {@link #VISIBLE}, {@link #INVISIBLE}, or {@link #GONE}.
     */
    public void setNameVisibility(int visibility) {
        this.txtName.setVisibility(visibility);
    }
}
