package ru.dropit;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by nikit on 15.03.2016.
 */
public class IpAddress {
    public static InetAddress getIpAddress() {
        try {
            for (Enumeration<NetworkInterface> ifaces =
                 NetworkInterface.getNetworkInterfaces();
                 ifaces.hasMoreElements(); ) {
                NetworkInterface iface = ifaces.nextElement();
                if (!iface.getDisplayName().contains("VirtualBox")) {
                    for (Enumeration<InetAddress> addresses =
                         iface.getInetAddresses();
                         addresses.hasMoreElements(); ) {
                        InetAddress address = addresses.nextElement();
                        if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                            return address;
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
