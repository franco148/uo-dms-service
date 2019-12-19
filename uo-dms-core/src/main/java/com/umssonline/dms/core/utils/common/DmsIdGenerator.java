package com.umssonline.dms.core.utils.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.UUID;

/**
 * @author franco.arratia@umssonline.com
 */
public class DmsIdGenerator {

    //region Fields
    private static Logger logger = LoggerFactory.getLogger(DmsIdGenerator.class);

    private static final String MAC_ADDRESS = getMacAddress();
    private static final String HYPHEN = "-";
    private static final String FORMAT_STRING = "%02X%s";
    private static final String EMPTY_STRING = "";
    //endregion

    //region Constructors
    private DmsIdGenerator() {

    }
    //endregion

    //region Methods
    public static String createDmsUniqueId() {

        UUID uuid = UUID.randomUUID();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(uuid.toString());
        stringBuffer.append(MAC_ADDRESS);

        return stringBuffer.toString();
    }
    //endregion

    //region Utilities
    private static String getMacAddress(){

        StringBuffer stringBuffer = new StringBuffer();

        try {
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface nInterface = NetworkInterface.getByInetAddress(address);

            if (nInterface != null) {
                byte[] hardwareAddress = nInterface.getHardwareAddress();
                int length = hardwareAddress.length;

                for (int i = 0; i < length; i++) {
                    stringBuffer.append(String.format(FORMAT_STRING, hardwareAddress[i], (i < length-1) ? HYPHEN : EMPTY_STRING));
                }
            } else {
                Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();

                while (networks.hasMoreElements()) {
                    NetworkInterface network = networks.nextElement();
                    byte[] macAddress = network.getHardwareAddress();

                    if (macAddress != null) {
                        for (int i= 0; i < macAddress.length; i++) {
                            stringBuffer.append(String.format(FORMAT_STRING, macAddress[i], (i < macAddress.length - 1) ? HYPHEN : EMPTY_STRING));
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("MacAddress couldn't be gotten to build DMS ID");
        }

        return stringBuffer.toString();
    }
    //endregion
}
