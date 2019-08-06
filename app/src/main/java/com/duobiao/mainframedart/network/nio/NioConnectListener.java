package com.duobiao.mainframedart.network.nio;


import com.duobiao.mainframedart.network.nio.processor.NioReadWriteProcessor;

import java.io.IOException;
import java.nio.channels.SocketChannel;


/**
 * description  :   连接状态回调
 */

public interface NioConnectListener {

    void onConnectSuccess(NioReadWriteProcessor mSocketProcessor, SocketChannel socketChannel) throws IOException;

    void onConnectFailed(NioReadWriteProcessor mSocketProcessor);

}