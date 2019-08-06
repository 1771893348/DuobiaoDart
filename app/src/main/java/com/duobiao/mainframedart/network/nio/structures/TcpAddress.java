package com.duobiao.mainframedart.network.nio.structures;

/**
 * description  :   Server地址
 */

public class TcpAddress {
    public String ip;
    public int port;

    public TcpAddress(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}