package pullscreen;


import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Package com.ccdt.pullscreen
 * @ClassName: SendPacketThread
 * @Description: 发包线程
 * @author hezb
 * @date 2015年6月24日 上午11:09:46
 */

public class SendPacketThread extends Thread {
    private static final String TAG = "hezb";
    
    private String name, url;
    private int position = 0;
    private InetAddress mAddress;
    
    public SendPacketThread(String ip, String name,
            String url) {
        this.name = name;
        this.url = url;
        try {
            mAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.d(TAG, "ip is error, can not create InetAddress!");
        }
    }
    
    public void setPosition(int position) {
        this.position = position;
    }
    
    @Override
    public void run() {
        super.run();
        if (mAddress != null) {
            // 发送盒子当前播放的视频信息
            String msg = url + Constants.DIVIDE + position + Constants.DIVIDE + name;
            Packet packet = new Packet(mAddress, Constants.PHONE_PORT);
            packet.PacketNOTIFICATION(msg);
            try {
                packet.send();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "send packet error!");
            }
        }
    }
}
