package com.smile.codec;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 一个对long类型编码的工具
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-27 10:03 上午
 */
public class BruteForceCoding {
    private static byte byteVal = 101;
    private static short shortVal = 10001;
    private static int intVal = 100000001;
    private static long longVal = 1000000000001L;
    // byte字节数
    private final static int BYTE_SIZE = 1;
    // short字节数
    private final static int SHORT_SIZE = 2;
    // int字节数
    private final static int INT_SIZE = 4;
    // long字节数
    private final static int LONG_SIZE = 8;
    // 8bit
    private final static int BYTE_MASK = 0xFF;

    public static String byteArrayToDecimalString(byte[] byteArray) {
        StringBuilder rtn = new StringBuilder();
        for (byte b : byteArray) {
            rtn.append(b & BYTE_MASK).append(" ");
        }
        return rtn.toString();
    }

    public static int encodeIntBigEndian(byte[] dst, long val, int offset, int size) {
        for (int i = 0; i < size; i++) {
            int shift = (size - i - 1) * BYTE_SIZE;
            dst[offset++] = (byte) (val >> shift);
        }
        return offset;
    }

    public static long decodeIntBigEndian(byte[] val, int offset, int size) {
        long rtn = 0;
        for (int i = 0; i < size; i++) {
            rtn = (rtn << Byte.SIZE) | ((long) val[offset + i] & BYTE_MASK);
        }
        return rtn;
    }

    public static void main(String[] args) throws IOException {
        byte[] message = new byte[BYTE_SIZE + SHORT_SIZE + INT_SIZE + LONG_SIZE];
        System.out.println(message.length);
        // 放入byte
        int offset = encodeIntBigEndian(message, byteVal, 0, BYTE_SIZE);
        // 放入short
        offset = encodeIntBigEndian(message, shortVal, offset, SHORT_SIZE);
        // 放入int
        offset = encodeIntBigEndian(message, intVal, offset, INT_SIZE);
        // 放入long
        encodeIntBigEndian(message, longVal, offset, LONG_SIZE);
        System.out.println(message.length);
        System.out.println("Encode message:" + byteArrayToDecimalString(message));

        // Decode several fields
        long value = decodeIntBigEndian(message, BYTE_SIZE, SHORT_SIZE);
        System.out.println("Decoded short = " + value);
        value = decodeIntBigEndian(message, BYTE_SIZE + SHORT_SIZE + INT_SIZE, LONG_SIZE);
        System.out.println("Decoded long = " + value);

        // demonstrate dangers of conversaion
        offset = 4;
        value = decodeIntBigEndian(message, offset, BYTE_SIZE);
        System.out.println("Decoded value (offset" + offset + ", size + " + BYTE_SIZE + ") = " + value);
        byte bValue = (byte) decodeIntBigEndian(message, offset, BYTE_SIZE);
        System.out.println("Same value as byte = " + bValue);

        // Test JDK utils
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(buf);

        out.writeByte(byteVal);
        out.writeShort(shortVal);
        out.writeInt(intVal);
        out.writeLong(longVal);
        out.flush();
        byte [] msg = buf.toByteArray();
        System.out.println("Encode message:" + byteArrayToDecimalString(msg));
    }

}
