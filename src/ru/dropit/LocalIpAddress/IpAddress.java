package ru.dropit.LocalIpAddress;

import ru.dropit.LocalIpAddress.Tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

/**
 * Created by nikit on 15.03.2016.
 */
public class IpAddress {
    private static String _gateway;
    private static String _ip;

    public static InetAddress getIpAddress() {
        InetAddress inetAddress;
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
                    inetAddress = InetAddress.getByName(_ip);
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
