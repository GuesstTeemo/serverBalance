package com.server.jt.util;

public class ByteUtil {
	
	//byte数组截取
	 public static byte[] cutOutByte(byte[] b,int j){  
	        if(b.length==0 || j==0){  
	            return null;  
	        }  
	        byte[] bjq = new byte[j];  
	        for(int i = 0; i<j;i++){  
	            bjq[i]=b[i];  
	        }  
	        return bjq;  
	    }  
	 
	 //byte数组合并
	 public static byte[] getMergeBytes(byte[] pByteA, byte[] pByteB){  
	        int aCount = pByteA.length;  
	        int bCount = pByteB.length;  
	        byte[] b = new byte[aCount + bCount];  
	        for(int i=0;i<aCount;i++){  
	            b[i] = pByteA[i];  
	        }  
	        for(int i=0;i<bCount;i++){  
	            b[aCount + i] = pByteB[i];  
	        }  
	        return b;  
	    }  
	 
	public static byte[] intTobyte(int res) {
		byte[] targets = new byte[4];
		targets[3] = (byte) (res & 0xff);// 最低位
		targets[2] = (byte) ((res >> 8) & 0xff);// 次低位
		targets[1] = (byte) ((res >> 16) & 0xff);// 次高位
		targets[0] = (byte) (res >>> 24);// 最高位,无符号右移。
		return targets;
	}
	public static byte[] intTobyte2(int res) {
		byte[] targets = new byte[4];
		targets[0] = (byte) (res & 0xff);// 最低位
		targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
		targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
		targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
		return targets;
	}
	/*
	public static int byte2int(byte[] res) {
		// 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000 
		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
		| ((res[2] << 24) >>> 8) | (res[3] << 24);
		return targets;
	}
	*/
	
	public static int byte2int(byte[] res) {
		// 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000 
		int targets = (res[3] & 0xff) | ((res[2] << 8) & 0xff00) // | 表示安位或
		| ((res[1] << 24) >>> 8) | (res[0] << 24);
		return targets;
	}
	
	/*
	public static byte[] intbyte(int len){
		byte[] len_str = new byte[4];
		len_str[0] = (byte)(0xff & len);
		len_str[1] = (byte)((0xff00 & len) >> 8);
		len_str[2] = (byte)((0xff0000 & len) >> 16);
		len_str[3] = (byte)((0xff000000 & len) >> 24);
        return len_str;
	}
	
	public static int byteint(byte[] lpBuf){
		int datalen = lpBuf[0] & 0xFF;
		datalen |= ((lpBuf[1] << 8) & 0xFF00);
		datalen |= ((lpBuf[2] << 16) & 0xFF0000);
		datalen |= ((lpBuf[3] << 24) & 0xFF000000);
        return datalen;
	}
	*/
	
}
