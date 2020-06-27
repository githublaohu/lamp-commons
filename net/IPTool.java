package com.lamp.commons.lang.net;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

public class IPTool {
    public final static String hostIp = getHostIp();

    static String getHostIp() {
	Enumeration<NetworkInterface> ifaces;
	try {
	    ifaces = NetworkInterface.getNetworkInterfaces();
	    while (ifaces.hasMoreElements()) {
		NetworkInterface iface = ifaces.nextElement();
		if (iface.getName().indexOf("eth") == -1) {
		    continue;
		}
		Enumeration<InetAddress> addresses = iface.getInetAddresses();

		while (addresses.hasMoreElements()) {
		    InetAddress addr = addresses.nextElement();
		    if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
			return addr.getHostAddress();
		    }
		}
	    }
	    return "127.0.0.1";
	} catch (SocketException e) {
	    return "127.0.0.1";
	}

    }

    public final static String nicknameIp = getNickname();

    static String getNickname() {
	try {
	    URL url = new URL("http://ip.chinaz.com/getip.aspx");
	    URLConnection connection = url.openConnection();
	    connection.connect();
	    int length = connection.getContentLength();
	    byte[] by = new byte[length];
	    connection.getInputStream().read(by);
	    String str = new String(by);
	    int ipIndex = str.indexOf("ip:'") + 4;
	    str = str.substring(ipIndex, str.indexOf('\'', ipIndex));
	    return str;
	} catch (IOException e) {
	    // TODO 自动生成的 catch 块
	    e.printStackTrace();
	}
	return null;
    }

}
