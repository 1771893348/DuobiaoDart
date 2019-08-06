package com.duobiao.mainframedart.network.File.loader;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.bumptech.glide.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xyl
 * @date 创建时间：2016年5月6日 下午4:51:51
 * @TODO 下载文件
 */
public class FileDownloader {
    private static FileDownloader sInstance;
    private DiskLruCache mDiskLruCache;
    public static final int LOAD_SUCCESS = 0;
    public static final int LOAD_FAILE = -1;
    public static final int LOAD_UPDATE = 1;
    // 线程池
    private ExecutorService mLoadThreadPool;
    private HashMap<String, ArrayList<FileLoadListenner>> callBackMap;

//    public static FileDownloader getInstance() {
//        if (sInstance == null) {
//            sInstance = new FileDownloader();
//        }
//        return sInstance;
//    }
//
//    private FileDownloader() {
//        initDiskLruCache();
//        getThreadPool();
//        callBackMap = new HashMap<String, ArrayList<FileLoadListenner>>();
//    }
//
//    public void getThreadPool() {
//        if (mLoadThreadPool == null) {
//            synchronized (ExecutorService.class) {
//                if (mLoadThreadPool == null) {
//                    mLoadThreadPool = Executors.newFixedThreadPool(3);
//                }
//            }
//        }
//    }
//
//    private Handler mHandler = new Handler() {
//        public void handleMessage(Message message) {
//            ArrayList<FileLoadListenner> callBacks = callBackMap.get(message.obj);
//            if (callBacks == null) {
//                return;
//            }
//            switch (message.what) {
//                case LOAD_FAILE:
//                    callBackMap.remove(message.obj);
//                    for (FileLoadListenner fileLoadListenner : callBacks) {
//                        fileLoadListenner.loadCallBack(null);
//                    }
//                    callBacks.clear();
//                    callBacks = null;
//                    break;
//                case LOAD_SUCCESS:
//                    callBackMap.remove(message.obj);
//                    String result = message.getData().getString("data");
//                    for (FileLoadListenner fileLoadListenner : callBacks) {
//                        fileLoadListenner.loadCallBack(result);
//                    }
//                    callBacks.clear();
//                    callBacks = null;
//                    break;
//                case LOAD_UPDATE:
//                    int progress = message.getData().getInt("data");
//                    for (FileLoadListenner fileLoadListenner : callBacks) {
//                        if (fileLoadListenner instanceof ProgressFileLoadListenner) {
//                            ((ProgressFileLoadListenner) fileLoadListenner).updateProgress((String) message.obj, progress);
//                        }
//                    }
//                    break;
//
//            }
//        }
//    };
//
//    public void initDiskLruCache() {
//        try {
//            File cacheFile = new File(IMConstance.FILE_FOLDER);
//            if (!cacheFile.exists() || !cacheFile.isDirectory()) cacheFile.mkdirs();
//            mDiskLruCache = DiskLruCache.open(cacheFile, AppHelper.getInstance().getVersionCode(), 1, 10 * 1024 * 1024);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    public String getCachePath(String fileUrl) {
//        String videoCachePath = null;
//        String key = MD5Helper.getInstance().encrypt(fileUrl);
//        try {
//            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
//            if (snapShot != null) {
//                videoCachePath = snapShot.getPath(0);
//            }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return videoCachePath;
//    }
//
//    public void loadFileFromUrl(String url, FileLoadListenner fileLoadListenner) {
//        if (TextUtils.isEmpty(url) || fileLoadListenner == null) {
//            return;
//        }
//        if (mDiskLruCache == null) {
//            initDiskLruCache();
//        }
//        if (mDiskLruCache == null) {
//            fileLoadListenner.loadCallBack(null);
//            return;
//        }
//        String videoPath = getCachePath(url);
//        if (!TextUtils.isEmpty(videoPath)) {
//            fileLoadListenner.loadCallBack(videoPath);
//        } else {
//            if (!NetUtils.checkNetworkState(HeibaApplication.getInstance())) {
//                fileLoadListenner.loadCallBack(null);
//                return;
//            }
//            if (callBackMap == null) {
//                callBackMap = new HashMap<String, ArrayList<FileLoadListenner>>();
//            }
//            String key = MD5Helper.getInstance().encrypt(url);
//            ArrayList<FileLoadListenner> callBacks = null;
//            if (callBackMap.containsKey(key)) {
//                callBacks = callBackMap.get(key);
//            } else {
//                callBacks = new ArrayList<FileLoadListenner>();
//                callBackMap.put(key, callBacks);
//            }
//            if (!callBacks.contains(fileLoadListenner)) {
//                callBacks.add(fileLoadListenner);
//            }
//            if (mLoadThreadPool == null) {
//                getThreadPool();
//            }
//            LoadThread loadThread = new LoadThread(url);
//            if (fileLoadListenner instanceof ProgressFileLoadListenner) {
//                loadThread.setHasUpdateProgress(true);
//            }
//            mLoadThreadPool.execute(loadThread);
//        }
//    }
//
//    public class LoadThread extends Thread {
//        private String url;
//        private boolean hasUpdateProgress;
//        private String loadKey;
//
//        public LoadThread(String url) {
//            this.url = url;
//        }
//
//        public boolean hasUpdateProgress() {
//            return hasUpdateProgress;
//        }
//
//        public void setHasUpdateProgress(boolean hasUpdateProgress) {
//            this.hasUpdateProgress = hasUpdateProgress;
//        }
//
//        public void run() {
//            String result = null;
//            loadKey = MD5Helper.getInstance().encrypt(url);
//            DiskLruCache.Editor editor;
//            try {
//                editor = mDiskLruCache.edit(loadKey);
//                if (editor != null) {
//                    OutputStream outputStream = editor.newOutputStream(0);
//                    if (loadFileFromUrl(outputStream, url)) {
//                        editor.commit();
//                        result = editor.getCachePath(0);
//                    } else {
//                        editor.abort();
//                    }
//                }
//                mDiskLruCache.flush();
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                result = null;
//            }
//            Message message = mHandler.obtainMessage();
//            if (TextUtils.isEmpty(result)) {
//                message.what = LOAD_FAILE;
//            } else {
//                message.what = LOAD_SUCCESS;
//            }
//            message.obj = loadKey;
//            Bundle data = new Bundle();
//            data.putString("data", result);
//            message.setData(data);
//            mHandler.sendMessage(message);
//        }
//
//        private boolean loadFileFromUrl(OutputStream outputStream, String url) {
//            boolean result;
//            HttpURLConnection huc = null;
//            BufferedOutputStream out = null;
//            BufferedInputStream in = null;
//            try {
//                URL m = new URL(url);
//                huc = (HttpURLConnection) m.openConnection();
//                huc.setConnectTimeout(5000);
//                huc.setReadTimeout(10000);
//                huc.setRequestMethod("GET");
//                huc.setDoInput(true);
//                huc.connect();
//                in = new BufferedInputStream(huc.getInputStream(), 8 * 1024);
//                out = new BufferedOutputStream(outputStream, 8 * 1024);
//                byte[] buffer = new byte[1024 * 4];
//                int fileLength = huc.getContentLength();
//                int downedFileLength = 0;
//                int num = 0;
//                while ((num = in.read(buffer)) != -1) {
//                    out.write(buffer, 0, num);
//                    if (hasUpdateProgress) {
//                        downedFileLength += num;
//                        sendLoadProgress((int) (downedFileLength * 1.0 / fileLength * 100));
//                    }
//                }
//                result = true;
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                result = false;
//            } finally {
//                if (huc != null) {
//                    huc.disconnect();
//                }
//                try {
//                    if (out != null) {
//                        out.close();
//                    }
//                    if (in != null) {
//                        in.close();
//                    }
//                } catch (final IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return result;
//        }
//
//        private void sendLoadProgress(int progress) {
//            if (progress < 0) {
//                progress = 0;
//            } else if (progress > 100) {
//                progress = 100;
//            }
//            Message message = mHandler.obtainMessage();
//            message.what = LOAD_UPDATE;
//            message.obj = loadKey;
//            Bundle data = new Bundle();
//            data.putInt("data", progress);
//            message.setData(data);
//            mHandler.sendMessage(message);
//        }
//    }
}
