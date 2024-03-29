package com.duobiao.mainframedart.network.nio.structures.message;

import java.util.LinkedList;

/**
 * description  :   读队列
 */

public final class MessageReadQueen {

    private MessageBuffer mReadMessageBuffer = new MessageBuffer();
    public LinkedList<Message> mReadQueen = new LinkedList<>();//真正的消息队列

    public Message build(byte[] src, int offset, int length) {
        Message msg = mReadMessageBuffer.build(src, offset, length);
        return msg;
    }

    public void add(Message msg) {
        if (null != msg) {
            mReadQueen.add(msg);
        }
    }

    public void remove(Message msg) {
        if (null != msg) {
            mReadQueen.remove(msg);
            mReadMessageBuffer.release(msg);
        }
    }
}
