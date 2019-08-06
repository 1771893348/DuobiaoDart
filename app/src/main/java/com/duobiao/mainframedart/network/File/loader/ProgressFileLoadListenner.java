package com.duobiao.mainframedart.network.File.loader;
/**
 * @author xyl
 * @date 创建时间：2016年5月6日 下午5:13:39
 * @TODO 带下载进度加载文件回调
 */
public interface ProgressFileLoadListenner extends FileLoadListenner {
	public void updateProgress(String key, int progress);
}
