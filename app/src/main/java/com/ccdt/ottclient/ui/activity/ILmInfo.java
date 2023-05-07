package com.ccdt.ottclient.ui.activity;

import com.ccdt.ottclient.model.LmInfoObj;


public interface ILmInfo {
    void setLmInfo(LmInfoObj lmInfo);
    LmInfoObj getLmInfo();
    String getLmId();
}
