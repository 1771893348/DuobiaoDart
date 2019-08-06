package com.duobiao.mainframedart.network.nio.structures;

/**
 * description  :   Server地址
 */

public class UdpAddress {
    public String ip;
    public int port;

    public UdpAddress(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}