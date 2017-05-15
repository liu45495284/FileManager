package com.freeme.filemanager.util;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;


import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DroiSDCardManager {
    private static String Sdstate;

    public static boolean isSDCardMount(){
        final String state = getSDCardStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            return true;
        }
        return false;
    }  
    
    private static String getSDCardStorageState() {
        try {
            if (Environment.MEDIA_MOUNTED.equals(Sdstate)) {
                return Environment.MEDIA_MOUNTED;
            }
            return Environment.MEDIA_UNKNOWN;
        } catch (Exception e) {
            android.util.Log.e("Sunny", "failed to read sd state:" + e);
            return Environment.MEDIA_REMOVED;
        }
    }
    
    
    public static File[] getSDCardStoragePath(Context mContext) {
        Class<?> storageVolumeClazz = null;
        File[] list = null;
        try {

            StorageManager mStorageManager = (StorageManager) mContext
                    .getSystemService(Context.STORAGE_SERVICE);
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method getState = storageVolumeClazz.getMethod("getState");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            list = new File[length];
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                File file = new File(path);
                list[i] = file;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return list;
    }
}
