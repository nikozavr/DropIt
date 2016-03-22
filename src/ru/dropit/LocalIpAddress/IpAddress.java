package ru.dropit.LocalIpAddress;

import ru.dropit.LocalIpAddress.Tokenizer;

import java.io.BufferedReader;
import java.io.FileReader;
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

    private static boolean isWindows ()
    {
        String os = System.getProperty ( "os.name" ).toUpperCase ();
        return os.contains( "WINDOWS" ) ;
    }

    private static boolean isLinux ()
    {
        String os = System.getProperty ( "os.name" ).toUpperCase ();
        return os.contains( "LINUX" )  ;
    }

    private String parse()
    {
        if(isWindows())
        {
            return parseWindows();
        }
        else if(isLinux())
        {
            return parseLinux();
        }
        return null;
    }

    private String parseWindows()
    {
        try
        {
            Process pro = Runtime.getRuntime().exec("cmd.exe /c route print");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));

            String line;
            while((line = bufferedReader.readLine())!=null)
            {
                line = line.trim();
                String [] tokens = Tokenizer.parse(line, ' ', true , true);// line.split(" ");
                if(tokens.length == 5 && tokens[0].equals("0.0.0.0"))
                {
                    _gateway = tokens[2];
                    _ip = tokens[3];
                    return _ip;
                }
            }
            //pro.waitFor();
        }
        catch(IOException e)
        {
            System.err.println(e);
            e.printStackTrace();
        }
        return null;
    }

    private String parseLinux()
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("/proc/net/route"));
            String line;
            while((line = reader.readLine())!=null)
            {
                line = line.trim();
                String [] tokens = Tokenizer.parse(line, '\t', true , true);// line.split(" ");
                if(tokens.length > 1 && tokens[1].equals("00000000"))
                {
                    String gateway = tokens[2]; //0102A8C0
                    if(gateway.length() == 8)
                    {
                        String[] s4 = new String[4];
                        s4[3] = String.valueOf(Integer.parseInt(gateway.substring(0, 2), 16));
                        s4[2] = String.valueOf(Integer.parseInt(gateway.substring(2, 4), 16));
                        s4[1] = String.valueOf(Integer.parseInt(gateway.substring(4, 6), 16));
                        s4[0] = String.valueOf(Integer.parseInt(gateway.substring(6, 8), 16));
                        _gateway = s4[0] + "." + s4[1] + "." + s4[2] + "." + s4[3];
                    }
                    String iface = tokens[0];
                    NetworkInterface nif = NetworkInterface.getByName(iface);
                    Enumeration addrs = nif.getInetAddresses();
                    while(addrs.hasMoreElements())
                    {
                        Object obj = addrs.nextElement();
                        if(obj instanceof Inet4Address)
                        {
                            _ip =  obj.toString();
                            if(_ip.startsWith("/")) _ip = _ip.substring(1);
                            return _ip;
                        }
                    }
                    return null;
                }
            }
            reader.close();
        }
        catch(IOException e)
        {
            System.err.println(e);
            e.printStackTrace();
        }
        return null;
    }

    public InetAddress getIpAddress() {
        try {
            InetAddress inetAddress = InetAddress.getByName(parse());
            return inetAddress;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
