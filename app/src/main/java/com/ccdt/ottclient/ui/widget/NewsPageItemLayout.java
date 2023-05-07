package com.ccdt.ottclient.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ccdt.ottclient.R;

public class NewsPageItemLayout extends RelativeLayout {

    private TextView mTxtNewTitle;
    private TextView mTxtNewFrom;
    private TextView mTxtNewTime;
    private ImageView mImgNews;

    private String title;
    private String from;
    private String time;
    private Drawable img;


    public NewsPageItemLayout(Context context) {
        super(context);
        init(context, null);
    }

    public NewsPageItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NewsPageItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.item_news_main_right, this, true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NewsPageItemLayout);
        title = a.getString(R.styleable.NewsPageItemLayout_news_title);
        from = a.getString(R.styleable.NewsPageItemLayout_news_from);
        time = a.getString(R.styleable.NewsPageItemLayout_news_time);
        img = a.getDrawable(R.styleable.NewsPageItemLayout_news_img);

        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        this.mTxtNewTitle = (TextView) this.findViewById(R.id.txtNewsTitle);
        this.mTxtNewFrom = (TextView) this.findViewById(R.id.txtNewsFrom);
        this.mTxtNewTime = (TextView) this.findViewById(R.id.txtNewsTime);
        this.mImgNews = (ImageView) this.findViewById(R.id.imgNews);

        if (this.mImgNews != null) {
            setNewsDrawable(this.img);
        }

        if (this.mTxtNewTitle != null) {
            setNewsTitle(this.title);
        }

        if (this.mTxtNewFrom != null) {
            setNewsFrom(this.from);
        }

        if (this.mTxtNewTime != null) {
            setNewsTime(this.time);
        }
    }

    public void setNewsDrawableResource(int resourceId) {
        this.mImgNews.setImageResource(resourceId);
    }

    public void setNewsDrawable(Drawable drawable) {
        this.mImgNews.setImageDrawable(drawable);
    }

    public void setNewsTitle(String title) {
        this.mTxtNewTitle.setText(title);
    }

    public void setNewsFrom(String from) {
        this.mTxtNewFrom.setText(from);
    }

    public void setNewsTime(String time) {
        this.mTxtNewTime.setText(time);
    }

    public ImageView getNewsImageView(){
        return this.mImgNews;
    }
}
