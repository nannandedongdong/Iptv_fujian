package upgrade;

import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import com.ccdt.ottclient.MyApp;
import com.ccdt.ottclient.utils.LogUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author hezb
 * @Package com.ccdt.upgrade
 * @ClassName: DownLoadTask
 * @Description: 单线程下载安装包
 * @date 2015年6月13日 下午2:55:07
 */

public class DownloadTask extends AsyncTask<String, Integer, Boolean> {

    public static final int DOWNLOAD_INIT = 0;
    public static final int DOWNLOADING = 1;
    public static final int DOWNLOAD_PAUSE = 2;
    public static final int DOWNLOADED = 3;
    public static final int DOWNLOAD_ERROR = 3;

    public static final String DOWNLOADING_TEXT = "正在下载：";
    public static final String DOWNLOAD_PAUSE_TEXT = "已暂停";
    public static final String DOWNLOADED_TEXT = "下载完成";
    public static final String DOWNLOADED_ERROR_TEXT = "下载失败";

    private int state = DOWNLOAD_INIT;
    private String filePath;
    private String fileName;
    private DownloadListener mListener;

    private int lastProgress;// 上一次的进度，减少频繁更新。

    public DownloadTask(String fileName) {
        this.fileName = fileName;
    }

    @Override
    protected void onPreExecute() {
//        filePath = "/Download";
        filePath = Environment.getDownloadCacheDirectory().getAbsolutePath();

//        if (Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {
//            filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";
//        } else {
//            filePath = Environment.getDownloadCacheDirectory() + "/Download";
//        }

        if (TextUtils.isEmpty(fileName)) {
            fileName = System.currentTimeMillis() + ".apk";
        } else {
            fileName = System.currentTimeMillis() + "_" + fileName;
        }
        lastProgress = -1;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.connect();

            int remoteFileLenght = connection.getContentLength();// 文件长度
            publishProgress(remoteFileLenght);

            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            if (file.exists()) {
                if (file.isFile()
                        && file.length() == remoteFileLenght) {
                    publishProgress(100);
                    return true;
                }
            } else {
                file.createNewFile();
            }
            file.setReadable(true, false);

            InputStream input = new BufferedInputStream(connection.getInputStream());
            OutputStream output = new FileOutputStream(file);

            byte data[] = new byte[1024];
            int count = 0;
            long total = 0;
            while ((count = input.read(data)) != -1) {

                if (state == DOWNLOAD_PAUSE) {
                    publishProgress((int) (total * 100 / remoteFileLenght));
                }
                while (state == DOWNLOAD_PAUSE) {
                    synchronized (this) {
                        wait(500);
                    }
                }

                total += count;
                int progress = (int) (total * 100 / remoteFileLenght);
                if (lastProgress != progress) {
                    lastProgress = progress;
                    publishProgress(lastProgress);
                }
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();

            return (total > 0);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (mListener != null) {
            if (state == DOWNLOAD_INIT) {
                mListener.onConnected(values[0]);
                onResume();
            } else if (state == DOWNLOAD_PAUSE) {
                mListener.onUpdateInfo(DOWNLOAD_PAUSE_TEXT, values[0]);
            } else {
                mListener.onUpdateInfo(DOWNLOADING_TEXT + values[0] + "%", values[0]);
            }
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) { // 下载完成
            state = DOWNLOADED;
            if (mListener != null) {
                mListener.onUpdateInfo(DOWNLOADED_TEXT, 100);
                mListener.onSuccess(filePath + "/" + fileName);
            }
        } else { // 下载失败
            state = DOWNLOAD_ERROR;
            if (mListener != null) {
                mListener.onUpdateInfo(DOWNLOADED_ERROR_TEXT, 100);
                mListener.onError();
            }
        }
    }

    @Override
    protected void onCancelled() {
        //取消任务后执行
    }

    /**
     * 暂停
     */
    public void onPause() {
        state = DOWNLOAD_PAUSE;
    }

    /**
     * 继续
     */
    public void onResume() {
        state = DOWNLOADING;
    }

    public int getState() {
        return state;
    }

    public void setDownloadListener(DownloadListener listener) {
        mListener = listener;
    }

    public interface DownloadListener {

        void onConnected(int fileSize);

        void onUpdateInfo(String msg, int progress);

        void onSuccess(String filePath);

        void onError();
    }
}
