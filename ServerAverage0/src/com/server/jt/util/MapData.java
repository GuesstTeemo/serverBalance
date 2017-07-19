package com.server.jt.util;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MapData {

	// 将所有接收到的地址端口号放到mapall里面
	// 监控报告内容
	public static Map<String, Integer> mapall = new HashMap<String, Integer>();
	// 交易前置map集合
	public static Map<String, Integer> maplogin = new HashMap<String, Integer>();
	// 行情报价map集合
	public static Map<String, Integer> mapprice = new HashMap<String, Integer>();
	// 图形服务map集合
	public static Map<String, Integer> mapchar = new HashMap<String, Integer>();
	// 给IPV4请求发送模拟盘数据
	public static Map<String, String> outipv4reality = new HashMap<String, String>();
	// 给IPV6请求发送模拟盘数据
	public static Map<String, String> outipv6reality = new HashMap<String, String>();
//	// 监控报告内容
//	public static Map<String, Integer> mapfuzai = new HashMap<String, Integer>();
	//socket客户端连接信息
	public static Map<String, String> clientsocket = new HashMap<String, String>();
	
	public static int socketnum = 0;
}
