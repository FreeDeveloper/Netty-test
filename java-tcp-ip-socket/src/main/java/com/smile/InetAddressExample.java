package com.smile;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * InetAddress的实例代码
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-26 9:18 下午
 */
public class InetAddressExample {
    public static void main(String[] args) throws SocketException, UnknownHostException {
        // 获取网卡列表
        Enumeration<NetworkInterface> interfaceList = NetworkInterface.getNetworkInterfaces();

        if (interfaceList == null) {
            System.out.println("--No interfaces found--");
        } else {
            while (interfaceList.hasMoreElements()) {
                NetworkInterface iface = interfaceList.nextElement();
                System.out.println("Interface " + iface.getDisplayName() + ":");
                Enumeration<InetAddress> addrList = iface.getInetAddresses();
                if (addrList.hasMoreElements()) {
                    InetAddress address = addrList.nextElement();
                    String version = (address instanceof Inet4Address) ? "(v4)"
                            : (address instanceof Inet6Address) ? "(v6)" : "(?)";
                    System.out.println("\tAddress " + version + ":" + address.getHostAddress());
                }
            }
        }

        String[] hosts = new String[]{"www.baidu.com", "blah.blah", "129.35.69.7"};

        for (String host : hosts) {
            System.out.println(host + ":");
            InetAddress[] addressList = InetAddress.getAllByName(host);
            for (InetAddress address : addressList) {
                System.out.println("\t" + address.getHostName() + "/" + address.getHostAddress());
            }
        }
    }
}
