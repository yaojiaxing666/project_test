package com.test.mainTest;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalAddress {

    public static void main(String[] args) throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        String hostAddress = localHost.getHostAddress();
        String hostName = localHost.getHostName();
    }
}
