package upgrade;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.ccdt.ottclient.R;
import com.ccdt.ottclient.utils.ShellUtils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * @Package com.ccdt.upgrade
 * @ClassName: UpgradeDialogActivity
 * @Description: 升级弹出框
 * @author hezb
 * @date 2015年6月16日 下午5:20:31
 */

public class UpgradeDialogActivity extends Activity implements DownloadTask.DownloadListener {

    private TextView mAPPName;
    private TextView mAPPInfo;
    private Button mConfirm;
    private Button mCancel;
    private View mDownloadLayout;
    private TextView mDownloadInfo;
    private TextView mDownloadSize;
    private ProgressBar mDownloadProgress;
    
    private String appName, appInfo, downloadUrl;
    
    private DownloadTask mDownloadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_upgrade);

        appName = getIntent().getStringExtra(UPConstants.UPGRADE_APPNAME);
        appInfo = getIntent().getStringExtra(UPConstants.UPGRADE_REMARK);
        downloadUrl = getIntent().getStringExtra(UPConstants.UPGRADE_APKURL);

//        ShellUtils.execCommand("busybox mount -o -remount", true);

        mFindViewById();
        
        initOperator();
        
    }
    
    private void mFindViewById() {
        mAPPName = (TextView) findViewById(R.id.upgrade_app_name);
        mAPPInfo = (TextView) findViewById(R.id.upgrade_app_info);
        mConfirm = (Button) findViewById(R.id.upgrade_confirm);
        mCancel = (Button) findViewById(R.id.upgrade_cancel);
        mDownloadLayout = findViewById(R.id.upgrade_download_layout);
        mDownloadInfo = (TextView) findViewById(R.id.upgrade_download_info);
        mDownloadSize = (TextView) findViewById(R.id.upgrade_download_size);
        mDownloadProgress = (ProgressBar) findViewById(R.id.upgrade_progressbar);
    }
    
    private void initOperator() {
        String fileName = downloadUrl.substring(
                downloadUrl.lastIndexOf("/")+1, downloadUrl.length());
        mDownloadTask = new DownloadTask(fileName);
        mDownloadTask.setDownloadListener(this);
        
        mAPPName.setText(appName);
        mAPPInfo.setText(appInfo);
        mAPPInfo.setMovementMethod(ScrollingMovementMethod.getInstance());
        mConfirm.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                showDownloadView();
            }
        });
        mCancel.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (mDownloadTask != null) {
                    mDownloadTask.cancel(true);
                }
                finish();
            }
        });

    }
    
    private void showDownloadView() {
        mDownloadTask.execute(downloadUrl);
        mConfirm.setText(R.string.pause);
        mConfirm.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (mDownloadTask != null) {
                    if (mDownloadTask.getState() == DownloadTask.DOWNLOADING) {
                        mDownloadTask.onPause();
                        mConfirm.setText(R.string.restart);
                    } else if (mDownloadTask.getState() == DownloadTask.DOWNLOAD_PAUSE) {
                        mDownloadTask.onResume();
                        mConfirm.setText(R.string.pause);
                    }
                }
            }
        });
        mCancel.setText(R.string.stop);
        mDownloadLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnected(int fileSize) {
        float size = fileSize / 1024f / 1024f;
        mDownloadSize.setText(new DecimalFormat(".0").format(size) + "M");
    }

    @Override
    public void onUpdateInfo(String msg, int progress) {
        mDownloadInfo.setText(msg);
        mDownloadProgress.setProgress(progress);
    }

    @Override
    public void onSuccess(String filePath) {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("chmod 777 "+filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(
//                Uri.fromFile(new File(filePath)),
                Uri.parse("file://" + filePath),
                "application/vnd.android.package-archive"
        );
        startActivity(intent);


        mConfirm.setText(R.string.install);
        mConfirm.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
        mCancel.setText(R.string.cancel);
    }

    @Override
    public void onError() {
        mDownloadInfo.setText("下载失败");
    }
}
