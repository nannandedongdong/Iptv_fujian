package pullscreen;

import android.os.Binder;

/**
 * @Package com.ccdt.pullscreen
 * @ClassName: PullScreenBinder
 * @Description: 拉屏服务与播放页进行交互的binder
 * @author hezb
 * @date 2015年6月24日 上午10:57:50
 */

public class PullScreenBinder extends Binder {

    private OnPullScreenListener listener;

    public OnPullScreenListener getListener() {
        return listener;
    }

    public void setListener(OnPullScreenListener listener) {
        this.listener = listener;
    }

    /**
     * 收到拉屏通知回调相应IP
     */
    public interface OnPullScreenListener {
        void onPullScreen(String ip);
    }
}
