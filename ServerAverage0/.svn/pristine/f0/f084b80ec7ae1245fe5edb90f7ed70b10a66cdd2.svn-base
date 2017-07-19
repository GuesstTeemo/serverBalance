package com.server.jt.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.server.jt.util.ByteUtil;
import com.server.jt.util.MapData;

import ench.ARPInterface;
import ench.JTM;

public class ServerSocketThread extends Observable implements Runnable {

//	JsonS js = new JsonS();
	Socket Socket = null;
	Logger log = Logger.getLogger(ServerSocketThread.class);
	private OutputStream out;
	private InputStream in;
	private Timer heartBeatTimer;
	private TimerTask heartBeatTask;
	public static String startTime;
	public static InetAddress Ip;
	int sno;
	InetAddress comeip = null;
	String messout = null;

	public ServerSocketThread(Socket socket1) {
		System.out.println("线程启动...");
		System.out.println("这是线程:"+ MapData.socketnum++);
		this.Socket = socket1;
//		comeip = socket1.getInetAddress();
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			in = Socket.getInputStream();
			out = Socket.getOutputStream();
			this.Socket.setSoTimeout(180000);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
			startTime = df.format(new Date());// new Date()为获取当前系统时间
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		startHeartBeatThread();// 启动心跳定时器
		while (true) {
			try {
				// Socket.sendUrgentData(0);
				byte[] header = new byte[4];
				int headernext = 0;
				int len = 0;
				while (headernext < 4
						&& (len = in.read(header, headernext, 4 - headernext)) >= 0) {
					// int len = in.read(header, headernext, 4 - headernext);
					headernext += len;
				}
				int contextnext = 0;
				int len1 = 0;
				int protlen = ByteUtil.byte2int(header);
				if (protlen != 0) {
					byte[] protobuf = new byte[protlen];
					while (contextnext < protlen
							&& (len1 = in.read(protobuf, contextnext, protlen
									- contextnext)) >= 0) {
						// len1 = in.read(protobuf, contextnext, protlen
						// - contextnext);
						contextnext += len1;
					}
					doRead(protobuf);// 得到完整报文后进行解析

				} else {
					log.info("【serversocket】只有消息头的一个空包");
				}

			} catch (SocketTimeoutException e) {
				log.error("【serversocket】连接超时", e);
				closeSocketServer();// 关闭socket
				if (heartBeatTimer != null) {
					heartBeatTimer.cancel();// 关闭之前的定时器
				}
				// doBusiness();// 通知观察者

			} catch (IOException e) {
				log.error("【serversocket】read出现错误");
				closeSocketServer();// 关闭socket
				if (heartBeatTimer != null) {
					heartBeatTimer.cancel();
				}
				// doBusiness();
				break;
			}
		}
	}

	public void doBusiness() {
		// System.out.println("【serversocket】启用观察者");
		super.setChanged();
		notifyObservers("serversocket");

	}

	public void writeToSocket(byte[] b) throws IOException {
		if (out != null) {
			out.write(b);
			out.flush();
		}
	}

	// public void sendHandshakeReq() {
	//
	// try {
	// // ================ 用proto buffer 编写与核心服务的登录握手报文=================
	//
	// ARPInterface.ARPMessage.Builder arpmessageBuilder =
	// ARPInterface.ARPMessage
	// .newBuilder();
	// ARPInterface.ARPMessage.Service.Builder serviceBuider3 =
	// ARPInterface.ARPMessage.Service
	// .newBuilder();
	// ARPInterface.ARPMessage.LoginRsp.Builder loginrspBuilder =
	// ARPInterface.ARPMessage.LoginRsp
	// .newBuilder();
	//
	// serviceBuider3.setServiceNo(8);
	// System.out.println("我有握手");
	//
	// loginrspBuilder.setServerNo(11);// 服务编号
	// loginrspBuilder.addServices(serviceBuider3);
	//
	// arpmessageBuilder.setLoginRsp(loginrspBuilder);
	// ARPInterface.ARPMessage arpmessage = arpmessageBuilder.build();
	//
	// byte[] b = arpmessage.toByteArray();
	// byte[] header = ByteUtil.intTobyte(b.length);
	// byte[] buf = ByteUtil.getMergeBytes(header, b);
	//
	// // if (buf != null) {
	// this.writeToSocket(buf);
	// // }
	//
	// } catch (IOException e) {
	//
	// System.out.println("【serversocket】握手write出现错误");
	// }
	// }

	public void startHeartBeatThread() {
		// 启动心跳线程
		heartBeatTimer = new Timer();
		heartBeatTask = new TimerTask() {

			@Override
			public void run() {

				try {
					// System.out.println("我有心跳！！！");
					// 心跳的 实际内容
					byte[] header2 = ByteUtil.intTobyte(0);
					// System.out.println("serversocket心跳=====" +
					// header2.length);
					writeToSocket(header2);

				} catch (IOException e) {
					log.error("【serversocket】心跳包write出现错误");
				}
			}
		};
		heartBeatTimer.schedule(heartBeatTask, 5000, 30000); // 启动5s后开始执行，间隔30s
	}

	public void closeSocketServer() {
		try {

			if (null != Socket && !Socket.isClosed()) {
				SocketOut();
				Socket.close();
				log.error("【serversocket】已经关闭");
			}
		} catch (IOException e) {
			log.error("【serversocket】关闭出现错误");
		}
	}

	/*
	 * 剔除该socket已经传过来的信息
	 */
	public void SocketOut() {
		String address = MapData.clientsocket.get(messout);
		if (address != null && !address.isEmpty()) {
			// 删除对应监控报告内容
			// 删除所有接受的信息中与该socket对应的信息
			MapData.mapall.remove(address);
			// 删除对应的分类集合里面的相关信息
			// IPV4,IPV6,PORT,服务器号
			String[] login = address.split(",");
			// 模拟盘交易前置
			if (("5").equals(login[3])) {
				MapData.maplogin.remove(login[0] + "," + login[1] + ","
						+ login[2]);
				MapData.outipv4reality.remove("loginIp");
				MapData.outipv4reality.remove("loginPort");
				MapData.outipv6reality.remove("loginIp");
				MapData.outipv6reality.remove("loginPort");
			}
			// 模拟盘行情报价
			else if ("3".equals(login[3])) {
				MapData.mapprice.remove(login[0] + "," + login[1] + ","
						+ login[2]);
				MapData.outipv4reality.remove("priceIp");
				MapData.outipv4reality.remove("pricePort");
				MapData.outipv6reality.remove("priceIp");
				MapData.outipv6reality.remove("pricePort");
			}
			// 模拟盘图形服务
			else if ("10".equals(login[3])) {
				MapData.mapchar.remove(login[0] + "," + login[1] + ","
						+ login[2]);
				MapData.outipv4reality.remove("chartIp");
				MapData.outipv4reality.remove("chartPort");
				MapData.outipv6reality.remove("chartIp");
				MapData.outipv6reality.remove("chartPort");
			}
		}
		MapData.clientsocket.remove(comeip);
	}

	public void doRead(byte[] protobuf) {
		try {

			ARPInterface.ARPMessage arpmessage = ARPInterface.ARPMessage
					.parseFrom(protobuf);
			ARPInterface.ARPMessage.Service.Builder serviceBuider = ARPInterface.ARPMessage.Service
					.newBuilder();
			ARPInterface.ARPMessage.Builder arpmessageBuilder = ARPInterface.ARPMessage
					.newBuilder();
			ARPInterface.ARPMessage.LoginRsp.Builder loginrsp = ARPInterface.ARPMessage.LoginRsp
					.newBuilder();

			if (arpmessage.hasLoginReq()) {

				System.out.println("请求握手的响应");
				ARPInterface.ARPMessage loginse1 = ARPInterface.ARPMessage
						.parseFrom(arpmessage.toByteArray());
				// System.out.println("loginse:"+loginse);
				sno = loginse1.getLoginReq().getServerNo();
				
				serviceBuider.setServiceNo(8);
				loginrsp.setServerNo(11);
				loginrsp.addServices(serviceBuider);
				arpmessageBuilder.setLoginRsp(loginrsp);
				ARPInterface.ARPMessage arp = arpmessageBuilder.build();

				byte[] b = arp.toByteArray();
				byte[] header = ByteUtil.intTobyte(b.length);
				byte[] buf = ByteUtil.getMergeBytes(header, b);

				this.writeToSocket(buf);

			} else {
				JTM.JTMessage jtmessage = JTM.JTMessage.parseFrom(arpmessage
						.getServiceRouteNty().getServiceData());
				// System.out.println("Receive:" + jtmessage);

				int EndFlag = jtmessage.getMsgHead().getEndFlag();
				// System.out.println("EndFlag----" + EndFlag);
				int RequestID = jtmessage.getMsgHead().getRequestID();
				// System.out.println("RequestID----" + RequestID);
				// IP
				String servaddr = jtmessage.getMsgBody().getLoadNty()
						.getServaddr();
				 System.out.println("IP:" + servaddr);
				 System.out.println("服务器号:" + sno);
				// 负载值
				int load = jtmessage.getMsgBody().getLoadNty().getLoad();
				 System.out.println("负载量：" + load);
				 System.out.println("--------------------------------------------------------");
				// 实盘模拟盘flag
				int envflag = jtmessage.getMsgBody().getLoadNty().getEnvflag();
				// System.out.println("envflag----" + envflag);
				// “IPV4,IPV6,PORT,服务器号”
				// System.out.println("sno++++" + sno);
				if (servaddr != null && !servaddr.equals("")) {
					String addrno = servaddr + "," + sno;
					// MapData.mapfuzai.put(addrno, load);
					// 分实盘模拟盘
					MapData.mapall.put(addrno, load);
//					String[] login = servaddr.split(",");
					//messout=IPV4,sno(服务器号)
					messout = comeip + "," + sno;
					MapData.clientsocket.put(messout, addrno);
//					js.split(MapData.mapall);
				}
			}
			// }
		} catch (IOException e) {
			log.error("【serversocket】解析json包出现错误");
		}
	}

}
