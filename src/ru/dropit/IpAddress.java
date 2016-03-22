package ru.dropit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by nikit on 15.03.2016.
 */
public class IpAddress {
    private static String _gateway;
    private static String _ip;

    public static InetAddress getIpAddress() {
        InetAddress inetAddress;
       /* try {
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
        return null;*/
        try {
            Process pro = Runtime.getRuntime().exec("cmd.exe /c route print");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                String[] tokens = Tokenizer.parse(line, ' ', true, true);// line.split(" ");
                if (tokens.length == 5 && tokens[0].equals("0.0.0.0")) {
                    _gateway = tokens[2];
                    _ip = tokens[3];
                    System.out.println(_ip);
                    inetAddress = InetAddress.getByAddress(_ip.getBytes());
                    return inetAddress;
                }
            }
            //pro.waitFor();
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
        }
        return null;
    }
}
