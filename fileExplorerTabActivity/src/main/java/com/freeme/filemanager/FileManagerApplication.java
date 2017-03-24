package com.freeme.filemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import com.droi.sdk.analytics.DroiAnalytics;
import com.droi.sdk.core.Core;
import com.freeme.filemanager.bean.General_config;
import com.freeme.filemanager.model.GlobalConsts;
import com.freeme.filemanager.util.PullParseXML;
import com.freeme.filemanager.util.StorageHelper;
import com.freeme.filemanager.view.FileCategoryFragment.ScannerReceiver;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

//*/ modify by droi xueweili for refresh progressBar  on 20160419
public class FileManagerApplication extends Application {
    public static final String LOG_TAG = "FileManagerApplication";
    private ScannerReceiver mScannerReceiver;
    private List<SDCardChangeListener> listeners = new ArrayList<SDCardChangeListener>();

    private static final int MSG_SDCARD_CHANGED = 0x01;
    
  //*/add by droi liuhaoran for add customized configuration file on 20160428
    public static String mIsHideFTP;
    public static long mMemoryCardInfo;
    private InputStream mInput;
    private InputStream mVersionConfig;
    public static String mIsDaMi ;
    public static String mIsFeiMa;
    public static String mIsNeedRingTone;
    public static String mIsTest;
    private static final String TAG = "Config";
    //*/

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case MSG_SDCARD_CHANGED:
                int flag = msg.arg1;
                Log.i("liuhaoran1", "flag = " + flag );
                for (int i = 0; i < listeners.size(); i++) {
                    SDCardChangeListener listener = listeners.get(i);
                    if (listener != null) {
                        listener.onMountStateChange(flag);
                    }
                }
                break;
            default:
                break;
            }
        };
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Core.initialize(this);
        DroiAnalytics.initialize(this);

        //*/ freeme.liuhaoran , 20170323. for android N about "android.os.FileUriExposedException"
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        //*/

        new Thread(new Runnable() {

            @Override
            public void run() {
                //*/ modify by droi liuhaoran for add customized configuration file on 20160428
                AssetManager asset = getAssets();
                try {
                    File sFile = new File("/system/etc/general_config.xml");
                    File vFile = new File("/vendor/etc/general_config.xml");
                    if(sFile.exists()){
                    mInput = new FileInputStream(sFile);
                    }else if (vFile.exists()) {
                    mInput = new FileInputStream(vFile);
                    }else {
                    mInput = asset.open("general_config.xml");
                    }
                    mVersionConfig = asset.open("version_config.xml");
                    List<General_config> list = PullParseXML.getConfig(mInput);
                    for (General_config value : list) {
                        mIsHideFTP = value.getIsHideFTP();
                        mMemoryCardInfo = value.getMemoryCardInfo();
                        mIsNeedRingTone = value.getIsNeedRingTone();
                        if (mIsNeedRingTone == null) {
                            mIsNeedRingTone = "false";
                        }
                        Log.i(TAG, "mFlag=" + mIsHideFTP + "0");
                    }
                    List<General_config> versionList = PullParseXML.getConfig(mVersionConfig);
                    for (General_config value : versionList) {
                        mIsDaMi = value.getIsDaMi();
                        mIsFeiMa = value.getIsFeiMa();
                        mIsTest = value.getIsTest();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                //*/
            }
                }).start();

        registMountListener();
    }


    public void registMountListener() {

        mScannerReceiver = new ScannerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(1000);
        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_CHECKING);
        intentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        intentFilter.addDataScheme("file");
        registerReceiver(mScannerReceiver, intentFilter);
    }

    public void addSDCardChangeListener(SDCardChangeListener listener) {
        listeners.add(listener);
    }

    public void removeSDCardChangeListener(SDCardChangeListener listener) {
        listeners.remove(listener);
    }

    public interface SDCardChangeListener {
        public int flag_INJECT = 0x01;
        public int flag_UMMOUNT = 0x02;

        public void onMountStateChange(int flag);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(mScannerReceiver);
    }

    public class ScannerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(LOG_TAG, "FilecategoryACtivity, ScannerReceiver onReceive(), intent:  " + intent);
            String action = intent.getAction();
            Log.i("liuhaoran1", "action = " + action);
            //*/ modify by freemeos.liuhaoran on 20160708 for not click mRoundProgressBar2 after upload sd
            if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
            //*/
                StorageHelper.getInstance(FileManagerApplication.this).release();
                StorageHelper.getInstance(FileManagerApplication.this);
                mHandler.obtainMessage(MSG_SDCARD_CHANGED, SDCardChangeListener.flag_INJECT,0).sendToTarget();
            } else if (action.equals(Intent.ACTION_MEDIA_EJECT) || action.equals(Intent.ACTION_MEDIA_UNMOUNTED) || action.equals(Intent.ACTION_MEDIA_BAD_REMOVAL)) {
                StorageHelper.getInstance(FileManagerApplication.this).release();
                StorageHelper.getInstance(FileManagerApplication.this);
                mHandler.obtainMessage(MSG_SDCARD_CHANGED, SDCardChangeListener.flag_UMMOUNT,0).sendToTarget();
            } else if (action.equals(GlobalConsts.BROADCAST_REFRESH)) {
            }
        }
    }
}
//*/
