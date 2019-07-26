package com.duobiao.mainframedart.ble;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;

/**
 * Created by sky on 2017/11/14.
 */

public class AppContext
{
    public static String NAME;
    public static String Notyfi_UUIDs;
    public static String SERVICE_UUID;
    public static String WRITE_UUIDs;
    protected static final char[] hexArray;

    static {
        AppContext.SERVICE_UUID = "D3FAFFF0-3D39-4DE9-B9B4-BFFCE90EEEB5";
            AppContext.Notyfi_UUIDs = "D3FAACE4-3D39-4DE9-B9B4-BFFCE90EEEB5";
        AppContext.WRITE_UUIDs = "D3FAACE3-3D39-4DE9-B9B4-BFFCE90EEEB5";
        AppContext.NAME = "EDGLEDART";
        hexArray = "0123456789ABCDEF".toCharArray();
    }

    public static String byteArrayToHexString(final byte[] array) {
        final char[] array2 = new char[array.length * 2];
        for (int i = 0; i < array.length; ++i) {
            final int n = array[i] & 0xFF;
            array2[i * 2] = AppContext.hexArray[n >>> 4];
            array2[i * 2 + 1] = AppContext.hexArray[n & 0xF];
        }
        return new String(array2);
    }

    private static ArrayList<byte[]> getScanRecordMsg(final byte[] array) {
        final int length = array.length;
        int n = -1;
        int n2 = 0;
        final ArrayList<byte[]> list = new ArrayList<byte[]>();
        Boolean b = false;
        byte[] array2 = new byte[0];
        byte[] array3;
        int n4;
        for (int i = 0; i < length; ++i, array2 = array3, n = n4) {
            if (b) {
                int n3 = n2;
                if (n != 0) {
                    array2[n2] = array[i];
                    n3 = n2 + 1;
                }
                array3 = array2;
                if ((n2 = n3) >= (n4 = n)) {
                    b = false;
                    list.add(array2);
                    n4 = n;
                    n2 = n3;
                    array3 = array2;
                }
            }
            else {
                n4 = array[i];
                array3 = new byte[n4];
                b = true;
                n2 = 0;
            }
        }
        return list;
    }

    public static String getSerialNumber(byte[] array) {
        if (array == null || array.length == 0) {
            return "";
        }
        final ArrayList<byte[]> scanRecordMsg = getScanRecordMsg(array);
        array = new byte[6];
        for (final byte[] array2 : scanRecordMsg) {
            if (array2 != null && array2.length != 0 && (array2[0] & 0xFF) == 0xFF) {
                for (int i = 0; i < 6; ++i) {
                    array[i] = array2[i + 4];
                }
            }
        }
        return byteArrayToHexString(array);
    }

    public static byte[] hexStringToByteArray(final String s) {
        final int length = s.length();
        final byte[] array = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            array[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return array;
    }

    public static boolean isMyDevcie(final BluetoothDevice bluetoothDevice, final byte[] array) {
        String name;
        if ((name = bluetoothDevice.getName()) == null) {
            name = "";
        }
        reverse(array);
        return byteArrayToHexString(array).indexOf(AppContext.SERVICE_UUID.toString().replace("-", "").toUpperCase()) != -1 || name.indexOf(AppContext.NAME.toString()) != -1;
    }

    public static void reverse(final byte[] array) {
        if (array != null) {
            reverse(array, 0, array.length);
        }
    }

    public static void reverse(final byte[] array, int n, int i) {
        if (array != null) {
            if (n < 0) {
                n = 0;
            }
            byte b;
            for (i = Math.min(array.length, i) - 1; i > n; --i, ++n) {
                b = array[i];
                array[i] = array[n];
                array[n] = b;
            }
        }
    }

    enum CommandType
    {
        Def,
        Script;
    }

    public enum ConnectionState
    {
        CONNECTED,
        DISCONNECTED,
        ERROR;
    }
}
