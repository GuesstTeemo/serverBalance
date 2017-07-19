package com.server.jt.service;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.server.jt.util.CommPara;

public class Server extends Thread{

	private ServerSocket serverSocket;
	public static ServerSocketThread serverSocketThread;
	public static List<ServerSocketThread> list = new ArrayList<ServerSocketThread>();
	public static InetAddress IP;
	public static String startTime;

	public void run(){
		System.out.println("服务器启动...");
		Server server = new Server();
		server.init();
	}

	/**
	 * 负载均衡方向
	 * 
	 * @author Teemo
	 */
	public void init() {
		System.out.println("=======负载均衡方向=======");
		int count = 0;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
			startTime = df.format(new Date());// new Date()为获取当前系统时间
			serverSocket = new ServerSocket(CommPara.serverPort);
			while (true) {
				count++;
				System.out.println("i--->" + count);
				// 一旦有堵塞, 则表示服务器与客户端获得了连接
				Socket client = serverSocket.accept();
				// 获取客户端的IP
				InetAddress Ip = client.getInetAddress();
				// 显示ip
				System.out.println("连接地址ip：" + Ip);
				// 启动一个新的线程，接管与当前客户端的交互会话
				// new ServerThread(client);
				serverSocketThread =  new ServerSocketThread(client);
				 list.add(serverSocketThread);
			}
		} catch (Exception e) {
			System.out.print("服务器异常: " + e.getMessage());
		}
	}
}