package com.ccdt.ottclient.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.utils.NetWorkHelper;


public class NetworkStatusView extends ImageView {


	private static final int NET_CHECK_INTERVAL = 5*1000;

	private int mWifiResources[] = { R.drawable.global_status_bar_info_wifi_0,
			R.drawable.global_status_bar_info_wifi_1, R.drawable.global_status_bar_info_wifi_2, R.drawable.global_status_bar_info_wifi_3, R.drawable.global_status_bar_info_wifi_4 };

	private static final int HANDLER_MSG_CHECK_NETWORK_STATUS = 1;
	
	public NetworkStatusView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mHandler.sendEmptyMessageDelayed(HANDLER_MSG_CHECK_NETWORK_STATUS,0);
	}

	private Handler mHandler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			
			switch(msg.what){
			case HANDLER_MSG_CHECK_NETWORK_STATUS:
				
				if (!NetWorkHelper.isNetworkAvailable(getContext())) {
					setImageResource(R.drawable.global_status_bar_info_wifi_x);
				} else if (NetWorkHelper.isEthernetNetworkAvailable(getContext())) {
					setImageResource(R.drawable.global_status_bar_info_net_1);
				} else if (NetWorkHelper.isWiFiNetworkAvailable(getContext())) {
					int level = NetWorkHelper.getWiFiLevel(getContext());
					for (int i = 0; i < mWifiResources.length; i++) {
						if (level == i) {
							setImageResource(mWifiResources[i]);
							break;
						}
					}
				}
				
				mHandler.sendEmptyMessageDelayed(HANDLER_MSG_CHECK_NETWORK_STATUS, NET_CHECK_INTERVAL);
				break;
			}
			return false;
		}
	});
}
