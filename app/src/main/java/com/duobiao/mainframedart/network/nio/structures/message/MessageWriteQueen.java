package com.duobiao.mainframedart.network.nio.structures.message;

import java.util.LinkedList;

/**
 * description  :   写队列
 */

public final class MessageWriteQueen {

    private MessageBuffer mWriteMessageBuffer = new MessageBuffer();
    public LinkedList<Message> mWriteQueen = new LinkedList<>();//真正的消息队列

    public Message build(byte[] src, int offset, int length) {
        Message msg = mWriteMessageBuffer.build(src, offset, length);
        return msg;
    }

    public void add(Message msg) {
        if (null != msg) {
            mWriteQueen.add(msg);
        }
    }

    public void remove(Message msg) {
        if (null != msg) {
            mWriteQueen.remove(msg);
            mWriteMessageBuffer.release(msg);
        }
    }
}
