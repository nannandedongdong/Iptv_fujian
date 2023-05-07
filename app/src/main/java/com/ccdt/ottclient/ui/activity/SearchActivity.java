package com.ccdt.ottclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.ui.fragment.SearchDefaultFragment;
import com.ccdt.ottclient.ui.fragment.SearchKeyboardFragment;
import com.ccdt.ottclient.ui.fragment.SearchResultFragment;
import com.ccdt.ottclient.utils.Utility;

/**
 * 全局搜索页面
 */
public class SearchActivity
        extends BaseActivity
        implements
        SearchKeyboardFragment.SearchKeyboardCallbackListener,
        SearchDefaultFragment.OnItemClickListener,
        View.OnFocusChangeListener {
    private SearchDefaultFragment defaultFragment;
    private SearchResultFragment resultFragment;
    private FragmentManager fragmentManager;
    private String lastKeyword = "";
    private String currentKeyword = "";

    private static final int FRAGMENT_KEYBOARD = 0;
    private static final int FRAGMENT_DEFAULT = 1;
    private static final int FRAGMENT_RESULT = 2;
    public SearchKeyboardFragment keyboardFragment;
    private View viewMiddle;
    public int mKeyCode;
    private boolean isResultFragmentShow = false;
    private boolean isVisible;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    public void initWidget() {
        viewMiddle = findViewById(R.id.view_middle);
        fragmentManager = getSupportFragmentManager();
        showFragment(FRAGMENT_KEYBOARD);
        showFragment(FRAGMENT_DEFAULT);
//        viewMiddle.setOnFocusChangeListener(this);
    }

    /**
     * 搜索面板 回调接口
     *
     * @param keyStr
     */
    @Override
    public void searchKeyboardCallback(String keyStr) {
        currentKeyword = keyStr;
        if (currentKeyword == null) {
            currentKeyword = "";
        }

        if (lastKeyword.equals(currentKeyword)) {
            return;
        }

        if (lastKeyword.length() == 0 && currentKeyword.length() > 0) {
            showFragment(FRAGMENT_RESULT);
        } else if (lastKeyword.length() > 0 && currentKeyword.length() == 0) {
            showFragment(FRAGMENT_DEFAULT);
        } else if (resultFragment != null && lastKeyword.length() > 0 && currentKeyword.length() > 0) {
            resultFragment.setKeyword(currentKeyword);
            resultFragment.refreshResultFragment();
        }
        lastKeyword = currentKeyword;
    }

    /**
     * @param flag {@link SearchActivity#FRAGMENT_KEYBOARD,
     *             SearchActivity#FRAGMENT_DEFAULT,
     *             SearchActivity#FRAGMENT_RESULT}
     */
    private void showFragment(int flag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (flag) {
            case FRAGMENT_KEYBOARD:
                if (keyboardFragment == null) {
                    keyboardFragment = new SearchKeyboardFragment();
                    keyboardFragment.setSearchKeyboardCallbackListener(this);
                }
                transaction.replace(R.id.fragment_container_keyboard, keyboardFragment);
                break;
            case FRAGMENT_DEFAULT:

                if (resultFragment != null) {
                    resultFragment.clearAllData();
                }

                isResultFragmentShow = false;
                if (defaultFragment == null) {
                    defaultFragment = new SearchDefaultFragment();
                    defaultFragment.setOnItemClickListener(this);
                    transaction.add(R.id.fragment_container_content, defaultFragment);
                }
                if (resultFragment != null) {
                    transaction.hide(resultFragment);
                }

                transaction.show(defaultFragment);
                break;
            case FRAGMENT_RESULT:
                isResultFragmentShow = true;
                if (resultFragment == null) {
                    resultFragment = new SearchResultFragment();
                    transaction.add(R.id.fragment_container_content, resultFragment);
                }
                if (defaultFragment != null) {
                    transaction.hide(defaultFragment);
                }
                resultFragment.setKeyword(currentKeyword);
                transaction.show(resultFragment);
                break;
        }

        transaction.commit();
    }


    @Override
    public void setOnItemClickListener(String data) {
        Utility.intoVODDetailActivity(this, "");
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (isVisible) {
            int id = v.getId();
            switch (id) {
                case R.id.view_middle:
                    if (hasFocus) {
                        v.clearFocus();
                    } else {
                        View needFocusView = null;
                        if (mKeyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            // 中间线 获取焦点+左
                            if (keyboardFragment != null) {
                                needFocusView = keyboardFragment.getNeedFocusView();
                            }
                        } else if (mKeyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                             // 中间线 获取焦点+右
                            if (isResultFragmentShow) {
                                // 结果界面
                                if (resultFragment != null) {
                                    needFocusView = resultFragment.getNeedFocusView();
//                                    v.requestFocus(View.FOCUS_RIGHT);
                                }
                            } else {
                                // 热门搜索页面(右侧默认界面)
                                if (defaultFragment != null) {
                                    needFocusView = defaultFragment.getNeedFocusView();
                                }
                            }
                        }
                        if (needFocusView != null) {
                            needFocusView.requestFocus();
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this.mKeyCode = keyCode;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        isVisible = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
    }
}
