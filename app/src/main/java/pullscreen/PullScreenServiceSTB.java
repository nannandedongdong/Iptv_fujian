package pullscreen;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * 
 * @Package com.ccdt.pullscreen
 * @ClassName: PullScreenServiceSTB
 * @Description: 拉屏服务
 * @author hezb
 * @date 2015年6月24日 上午11:50:17
 */
public class PullScreenServiceSTB extends Service {
	private static final String TAG = "hezb";
	
	private int exceptionTimes = 0;
	private boolean stopped = false;
	private DatagramSocket mReceClent;
	private DatagramPacket mRecvPacket;
	
	private PullScreenBinder binder = new PullScreenBinder();
	
	@Override
	public void onCreate() {
		super.onCreate();
		GetPackage();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		stopped = true;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}
	
	/**
	 * 收包线程
	 */
	public void GetPackage() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Log.d(TAG, "盒子收包服务正在启动！！！");
				int offset = 12;
				byte[] recvBuf = new byte[20480];
				byte[] ccdt = new byte[4];
				try {
					mReceClent = new DatagramSocket(Constants.STB_PORT);
				} catch (SocketException e1) {
					e1.printStackTrace();
					Log.d(TAG, "盒子收包服务创建失败！！！");
					return;
				}
				while (!stopped) {
					try {
						mRecvPacket = new DatagramPacket(recvBuf, recvBuf.length);
						mReceClent.receive(mRecvPacket);
						Log.d(TAG,"----收到包了！！！");
						int dataLength = mRecvPacket.getLength();
						byte[] recvData = mRecvPacket.getData();
						ccdt[0] = recvData[0];
						ccdt[1] = recvData[1];
						ccdt[2] = recvData[2];
						ccdt[3] = recvData[3];
						String ccdtStr = new String(ccdt);

						if(!ccdtStr.equals("CCDT")){
							Log.d(TAG,"不是CCDT的包");
							continue;
						}
						
						if(recvData[5]==8){
							Log.d(TAG,"收到 8 包！！！");
							byte[] message = new byte[dataLength-offset-1];
							for(int i=0;i<message.length;i++){
								message[i] = recvData[i+offset];
							}
							String messageStr = new String(message);
							Log.d(TAG, "收到----messageStr:"+messageStr);
							if(messageStr.equals(Constants.PULL_SCREEN)){
								Log.d(TAG, "pull screen!!!!!!");
								if (binder.getListener() != null) {
                                    binder.getListener().onPullScreen(
                                            mRecvPacket.getAddress().getHostAddress());
                                }
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						exceptionTimes++;
						if(exceptionTimes>10){
							stopped = true;
						}
					}
				}
			}
		}).start();
	}

}
