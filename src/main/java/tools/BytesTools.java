package tools;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.lang.Math;

import org.apache.commons.codec.binary.Hex;


public class BytesTools {
	
	public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
	
    /**
     * Creates a copy of bytes and appends b to the end of it
     */
    public static byte[] appendByte(byte[] bytes, byte b) {
        byte[] result = Arrays.copyOf(bytes, bytes.length + 1);
        result[result.length - 1] = b;
        return result;
    }

    /**
     * The regular {@link java.math.BigInteger#toByteArray()} method isn't quite what we often need: 
     * it appends a leading zero to indicate that the number is positive and may need padding.
     *
     * @param b the integer to format into a byte array
     * @param numBytes the desired size of the resulting byte array
     * @return numBytes byte long array.
     */
    public static byte[] bigIntegerToBytes(BigInteger b, int numBytes) {
		if (b == null)
			return null;
        byte[] bytes = new byte[numBytes];
        byte[] biBytes = b.toByteArray();
        int start = (biBytes.length == numBytes + 1) ? 1 : 0;
        int length = Math.min(biBytes.length, numBytes);
        System.arraycopy(biBytes, start, bytes, numBytes - length, length);
        return bytes;        
    }

    /**
     * Omitting sign indication byte.
     * <br><br>
     * Instead of {@link org.spongycastle.util.BigIntegers#asUnsignedByteArray(BigInteger)} 
     * <br>we use this custom method to avoid an empty array in case of BigInteger.ZERO
     *
     * @param value - any big integer number. A <code>null</code>-value will return <code>null</code>
     * @return A byte array without a leading zero byte if present in the signed encoding. 
     * 		BigInteger.ZERO will return an array with length 1 and byte-value 0.
     */
    public static byte[] bigIntegerToBytes(BigInteger value) {
        if (value == null)
            return null;
        
        byte[] data = value.toByteArray();

        if (data.length != 1 && data[0] == 0) {
            byte[] tmp = new byte[data.length - 1];
            System.arraycopy(data, 1, tmp, 0, tmp.length);
            data = tmp;
        }
        return data;
    }
	
	//byte数组转char数组
	public static char[] bytesTochars(byte[] b) {
		char[] c = new char[b.length];
		for(byte i:b) {
			c[i]= (char) b[i];
		}
		return c;
	}
	//byte 与 int 的相互转换
	public static byte intToByte(int x) {
		return (byte) x;
	}
	
	public static int byteToInt(byte b) {
		//Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
		return b & 0xFF;
	}
	
	//byte 数组与 int 的相互转换
	public static int bytesToIntLE(byte[] a) {
		byte[] b = BytesTools.invertArray(a);
	    return   b[3] & 0xFF |
	            (b[2] & 0xFF) << 8 |
	            (b[1] & 0xFF) << 16 |
	            (b[0] & 0xFF) << 24;
	}
	
	/**
	 * Cast hex encoded value from byte[] to int
	 * 
	 * Limited to Integer.MAX_VALUE: 2^32-1 (4 bytes)
	 * 
	 * @param b array contains the values
	 * @return unsigned positive int value. 
	 */
	public static int bytesToIntBE(byte[] b) {
		if (b == null || b.length == 0)
			return 0;
		return new BigInteger(1, b).intValue();
	}
 
	public static byte[] intToByteArray(int a) {
	    return new byte[] {
	        (byte) ((a >> 24) & 0xFF),
	        (byte) ((a >> 16) & 0xFF),   
	        (byte) ((a >> 8) & 0xFF),   
	        (byte) (a & 0xFF)
	    };
	}
 
	//byte 数组与 long 的相互转换

    public static byte[] longToBytes(long x) {
    	ByteBuffer buffer = ByteBuffer.allocate(8);  
        buffer.putLong(0, x);
        return buffer.array();
    }
 
	public static long bytes8ToLong(byte[] input, boolean littleEndian){
	    long value=0;
	    for(int  count=0;count<8;++count){
	        int shift=(littleEndian?count:(7-count))<<3;
	        value |=((long)0xff<< shift) & ((long)input[count] << shift);
	    }
	    return value;
	}
    
    public static long bytes4ToLongLE(byte[] bytes) {

    	return ByteBuffer.wrap(bytes)
    	                   .order(ByteOrder.LITTLE_ENDIAN).getInt() & 0xFFFFFFFFL;
    }
    
    public static long bytes4ToLongBE(byte[] bytes) {
    	return ByteBuffer.wrap(invertArray(bytes))
    	                   .order(ByteOrder.LITTLE_ENDIAN).getInt() & 0xFFFFFFFFL;
    }
    
    public static String bytesToHexStringLE(byte[] data) {
    	byte[] bytes = BytesTools.invertArray(data);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    /**
     * Convert a byte-array into a hex String.<br>
     * Works similar to {@link Hex#toHexString} 
     * but allows for <code>null</code>
     * 
     * @param string - byte-array to convert to a hex-string
     * @return hex representation of the data.<br>
     * 		Returns an empty String if the input is <code>null</code>
     *
     * @see Hex#toHexString
     */
    public static String bytesToHexStringBE(byte[] b) {
        return Hex.encodeHexString(b);
    }
    
    
    /** 
     * hex字符串转byte数组 
     * @param inHex 待转换的Hex字符串 
     * @return  转换后的byte数组结果 
     */  
    public static byte[] hexToByteArray(String inHex){  
        int hexlen = inHex.length();  
        byte[] result;  
        if (hexlen % 2 == 1){  
            //奇数  
            hexlen++;  
            result = new byte[(hexlen/2)];  
            inHex="0"+inHex;  
        }else {  
            //偶数  
            result = new byte[(hexlen/2)];  
        }  
        int j=0;  
        for (int i = 0; i < hexlen; i+=2){  
            result[j]= (byte)Integer.parseInt(inHex.substring(i,i+2),16);
            j++;  
        }  
        return result;   
    }  
    
    public static Date bytesToDate(byte[] b) {
    	   int i = b[3] & 0xFF |
    	            (b[2] & 0xFF) << 8 |
    	            (b[1] & 0xFF) << 16 |
    	            (b[0] & 0xFF) << 24;   	
    	   long l = i*1000;   	   
    	   Date t = new Date(l);	       	   
    	   return t;
    }
    
    public static byte[] invertArray(byte[] a){
        byte[] b = new byte[a.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = a[b.length - i - 1];
        }
        return b;
    }
    public static String bytesToBinaryString(byte[] b){
    	String s = "";
	    for(int i=0;i<b.length;i++) {
	    s = s+Integer.toBinaryString(b[i]);
	    }
		return s;
    }
    
    public static byte[] bytesMerger(byte[] bt1, byte[] bt2){  
        byte[] bt3 = new byte[bt1.length+bt2.length];  
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);  
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);  
        return bt3;  
    } 
    
	public static byte[] bytesMerger(ArrayList<byte[]> bytesList) {
		Iterator<byte[]> iter= bytesList.iterator();
		int len=0;
		while(iter.hasNext())
			len += iter.next().length;
		
		byte[] all = new byte[len];
		int decPos = 0;
		Iterator<byte[]> iter1= bytesList.iterator();
		while(iter1.hasNext()) {
			byte[] src = iter1.next();
			System.arraycopy(src, 0, all, decPos, src.length);
			decPos += src.length;
		}	
		return all;	
	}
	
 
}


