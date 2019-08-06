package com.duobiao.mainframedart.network.File.loader;

/**
 * @author xyl
 * @date 创建时间：2016年5月6日 下午4:51:51
 * @TODO 文件加载器
 */
public class FileLoader {
    private static FileLoader sInstance;

    public static FileLoader getInstance() {
        if (sInstance == null) {
            sInstance = new FileLoader();
        }
        return sInstance;
    }

    public void loadFileFromUrl(String url, FileLoadListenner fileLoadListenner) {
//        FileDownloader.getInstance().loadFileFromUrl(url, fileLoadListenner);
    }
}
