package com.duobiao.mainframedart.network.nio;


import com.duobiao.mainframedart.network.nio.structures.message.MessageBuffer;
import com.duobiao.mainframedart.network.nio.structures.pools.MessagePool;

/**
 * description  :   客户端全局数据
 */

public final class GClient {

    private static boolean isInitialized = false;

    public static final void init() {
        if (!isInitialized) {
            MessagePool.init(6);
            MessageBuffer.init(8 * MessageBuffer.KB, 64 * MessageBuffer.KB, 1 * MessageBuffer.MB, 5, 2, 0, 2);
            isInitialized = true;
        }
    }
}
