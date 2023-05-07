package com.ccdt.ottclient.ui.view;


public interface GridLayoutAdapterInterface {
    
    public int getWidthSize(int position);

    public int getHeightSize(int position);

    public int getWidth(int position);

    public int getHeight(int position);

    public int getRightMargin(int position);

    public int getBottomMargin(int position);

    public int getNextFocusUpId();// 用于设置上title的Id
    public int getNextFocusDownId();// 用于设置下title的Id
    
    public static int DEFAULT_WIDTHSIZE = 1;
    public static int DEFAULT_HEIGHTSIZE = 1;
    public static int DEFAULT_WIDTH = 212;//默认宽
    public static int DEFAULT_HEIGHT = 212;//默认高
    
    public static int DEFAULT_RIGHTMARGIN = 8;
    public static int DEFAULT_BOTTOMMARGIN = 8;

}