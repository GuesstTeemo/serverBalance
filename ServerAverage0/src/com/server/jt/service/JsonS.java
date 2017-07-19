package com.server.jt.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.server.jt.util.MapData;

import net.sf.json.JSONObject;

/**
 * 服务端提供JSON数据接口： http://10.3.2.15:8080/ServerAverage0/JsonS?compare=android
 * http://10.3.2.15:80880/ServerAverage0/JsonS?compare=ios
 * http://10.3.2.15:8080/ServerAverage0/JsonS?compare=login
 * http://112.124.59.86:8099/ServerAverage0/JsonS?compare=ios
 * http://112.124.59.86:8099/ServerAverage0/JsonS?compare=login
 * http://10.3.1.32:8080/ServerAverage0/JsonS?compare=login
 * 
 * @author Teemo
 * @date： 日期：2016年11月10日 时间：上午10:20:32
 */
public class JsonS extends HttpServlet {

	public static MonitorSocketThread monitorSocketThread;
	static Map<String, String> versioncompare = new HashMap<String, String>();
	private static Properties props = new Properties();
	private static Logger log = Logger.getLogger(JsonS.class);

	public static ObserverListener observer = new ObserverListener();// 创建观察者;
	String ip = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8"); // 解决中文乱码问题
		ip = request.getRemoteAddr();// 得到来访者的IP地址
//		System.out.println("得到来访者的IP地址:" + ip);
//		log.info("得到来访者的IP地址:" + ip);
		if (ip.replaceAll("\\d", "").length() == 3) {
			IPV4(request, response);
			split(MapData.mapall);
//			System.out.println("IPV4");
		} else {
			IPV6(request, response);
//			System.out.println("IPV6");
			split(MapData.mapall);
		}
	}

	private void IPV6(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		props.load(getClass().getClassLoader().getResourceAsStream(
				"version.properties"));
		PrintWriter out1 = response.getWriter();
		String compare = request.getParameter("compare");

		// 先进行版本号比对
		if ("android".equals(compare)) {
			// 安卓版本比对
			String androidVersion1 = request.getParameter("androidVersion");
			String androidVersion2 = props.getProperty("androidVersion");
			BigDecimal data1 = new BigDecimal(androidVersion1);
			BigDecimal data2 = new BigDecimal(androidVersion2);
			if (data1.compareTo(data2) < 0) {
				versioncompare.put("version", "false");
				versioncompare.put("address",
						"http://112.124.59.86:8099/ServerAverage0/jt.apk");
			} else {
				versioncompare.put("version", "true");
			}
			JSONObject json1 = JSONObject.fromObject(versioncompare);
//			System.out.println("json--android:" + json1.toString());
			out1.write(json1.toString());
		} else if ("ios".equals(compare)) {
			// ios版本比对
			String iosVersion1 = request.getParameter("iosVersion");
			String iosVersion2 = props.getProperty("iosVersion");
			BigDecimal data1 = new BigDecimal(iosVersion1);
			BigDecimal data2 = new BigDecimal(iosVersion2);
			if (data1.compareTo(data2) > 0) {
				versioncompare.put("version", "false");
			} else {
				versioncompare.put("version", "true");
			}
			JSONObject json1 = JSONObject.fromObject(versioncompare);
//			System.out.println("json--ios:" + json1.toString());
			out1.write(json1.toString());
		} else if ("login".equals(compare)) {
			// 正常登陆
			if (MapData.outipv6reality.containsKey("loginPort")
					&& MapData.outipv6reality.containsKey("loginIp")
					&& MapData.outipv6reality.containsKey("chartIp")
					&& MapData.outipv6reality.containsKey("chartPort")
					&& MapData.outipv6reality.containsKey("priceIp")
					&& MapData.outipv6reality.containsKey("pricePort")) {
				JSONObject json1 = JSONObject.fromObject(MapData.outipv6reality);
				log.info("给["+ip+"]发送ipv6实盘数据："+json1.toString());
				out1.write(json1.toString());
			}
			// else {
			// JSONObject json1 = JSONObject.fromObject("{}");
			// out1.write(json1.toString());
			// }
		}
		out1.flush();
		out1.close();
	}

	private void IPV4(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		props.load(getClass().getClassLoader().getResourceAsStream(
				"version.properties"));
		PrintWriter out1 = response.getWriter();
		String compare = request.getParameter("compare");

		// 先进行版本号比对
		if ("android".equals(compare)) {
			// 安卓版本比对
			String androidVersion1 = request.getParameter("androidVersion");
			String androidVersion2 = props.getProperty("androidVersion");
			if (androidVersion1 != null && !androidVersion1.equals("")) {
				BigDecimal data1 = new BigDecimal(androidVersion1);
				BigDecimal data2 = new BigDecimal(androidVersion2);
				if (data1.compareTo(data2) < 0) {
					versioncompare.put("version", "false");
					versioncompare.put("address",
							"http://112.124.59.86:8099/ServerAverage0/jt.apk");
				} else {
					versioncompare.put("version", "true");
				}
				JSONObject json1 = JSONObject.fromObject(versioncompare);
			//	System.out.println("json--android:" + json1.toString());
				out1.write(json1.toString());
			}
		} else if ("ios".equals(compare)) {
			// ios版本比对
			String iosVersion1 = request.getParameter("iosVersion");
		//	System.out.println("apple-----" + iosVersion1);
			String iosVersion2 = props.getProperty("iosVersion");
			if (iosVersion1 != null && !iosVersion1.equals("")) {
				BigDecimal data1 = new BigDecimal(iosVersion1);
				BigDecimal data2 = new BigDecimal(iosVersion2);
				if (data1.compareTo(data2) < 0) {
					versioncompare.put("version", "false");
				} else {
					versioncompare.put("version", "true");
				}
				JSONObject json1 = JSONObject.fromObject(versioncompare);
			//	System.out.println("json--ios:" + json1.toString());
				out1.write(json1.toString());
			}
		} else if ("login".equals(compare)) {
			// 正常登陆
			if (MapData.outipv4reality.containsKey("loginPort")
					&& MapData.outipv4reality.containsKey("loginIp")
					&& MapData.outipv4reality.containsKey("chartIp")
					&& MapData.outipv4reality.containsKey("chartPort")
					&& MapData.outipv4reality.containsKey("priceIp")
					&& MapData.outipv4reality.containsKey("pricePort")) {
				JSONObject json1 = JSONObject.fromObject(MapData.outipv4reality);
			//	System.out.println("json实盘:" + json1.toString());
				log.info("给["+ip+"]发送ipv4实盘数据："+json1.toString());
				out1.write(json1.toString());
			}
		}
		out1.flush();
		out1.close();
	}

	/**
	 * map按照value排序
	 * 
	 * @param oldMap
	 * @return
	 * @author Teem
	 */
	public static Object sortMap(Map<String, Integer> oldMap) {
		if (oldMap == null)
			return null;
		Collection<Integer> c = oldMap.values();
		Object[] obj = c.toArray();
		Arrays.sort(obj);
		for (Map.Entry entry : oldMap.entrySet()) {
			if (obj[0].equals(entry.getValue()))
				return entry.getKey();

		}
		return null;
	}

	/**
	 * 
	 * @param map
	 * @author Teemo
	 */
	public void split(Map map) {
		// 分类
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String ip = (String) entry.getKey();
			int load = (Integer) entry.getValue();
			type(ip, load);
		}

		/**
		 * 实盘组合
		 */
		if (!MapData.maplogin.isEmpty()) {
//			System.out.println("maplogin有值！！！");
			String addr11 = (String) sortMap(MapData.maplogin);
			String[] login1 = addr11.split(",");
			MapData.outipv4reality.put("loginIp", login1[0]);
			MapData.outipv4reality.put("loginPort", login1[2]);
			MapData.outipv6reality.put("loginIp", login1[1]);
			MapData.outipv6reality.put("loginPort", login1[2]);
		}
		if (!MapData.mapprice.isEmpty()) {
//			System.out.println("mapprice有值！！！");
			String addr12 = (String) sortMap(MapData.mapprice);
			// System.out.println(c + "==mapprice:" + addr12);
			String[] price1 = addr12.split(",");
			MapData.outipv4reality.put("priceIp", price1[0]);
			MapData.outipv4reality.put("pricePort", price1[2]);
			MapData.outipv6reality.put("priceIp", price1[1]);
			MapData.outipv6reality.put("pricePort", price1[2]);
		}
		if (!MapData.mapchar.isEmpty()) {
//			System.out.println("mapchar有值！！！");
			String addr13 = (String) sortMap(MapData.mapchar);
			// 测试
//			Iterator it4 = MapData.mapchar.entrySet().iterator();
//			while (it4.hasNext()) {
//				Map.Entry entry = (Map.Entry) it4.next();
//				String ip = (String) entry.getKey();
//				int load = (Integer) entry.getValue();
//				System.out.println("mapchar有值:" + ip + ":" + load);
//			}
//			System.out.println("==mapchar有值:" + addr13);

			String[] chart1 = addr13.split(",");
			MapData.outipv4reality.put("chartIp", chart1[0]);
			MapData.outipv4reality.put("chartPort", chart1[2]);
			MapData.outipv6reality.put("chartIp", chart1[1]);
			MapData.outipv6reality.put("chartPort", chart1[2]);
//			System.out.println("chart1[0]" + chart1[0]);
//			System.out.println("chart1[1]" + chart1[1]);
//			System.out.println("chart1[2]" + chart1[2]);
		}

	}

	/**
	 * 服务器分类2
	 * 
	 * @param T
	 * @author Teemo
	 * @param map
	 */
	private void type(String ip, int load) {
		String[] login = ip.split(",");
		// System.out.println("login[0]" + b + ":" + login[0]);// IPV4
		// System.out.println("login[1]" + b + ":" + login[1]);// IPV6
		// System.out.println("login[2]" + b + ":" + login[2]);// PORT
//		System.out.println("login[3]" + ":" + login[3]);// 服务器号

		// 模拟盘交易前置
		if (("5").equals(login[3])) {
			MapData.maplogin.put(login[0] + "," + login[1] + "," + login[2],
					load);
		}
		// 模拟盘行情报价
		else if ("3".equals(login[3])) {
			MapData.mapprice.put(login[0] + "," + login[1] + "," + login[2],
					load);
		}
		// 模拟盘图形服务
		else if ("10".equals(login[3])) {
			MapData.mapchar.put(login[0] + "," + login[1] + "," + login[2],
					load);
		} else {
			System.out.println("没有收到你发过来的地址或发送地址错误！！！");
		}
	}

	public void init() throws ServletException {

		Thread s = new Server();
		s.setDaemon(true);// 设置线程为后台线程，tomcat不会被hold,启动后依然一直监听。
		s.start();
		if (null == monitorSocketThread) {// 判断监控服务是否启动
			monitorSocketThread = new MonitorSocketThread();
			monitorSocketThread.addObserver(observer);
			new Thread(monitorSocketThread).start();
		}

//		 mapgroup04.put("loginIp", "115.28.3.31");
//		 mapgroup04.put("loginPort", "9401");
//		 mapgroup04.put("chartIp", "112.124.59.66");
//		 mapgroup04.put("chartPort", "8006");
//		 mapgroup04.put("priceIp", "112.124.59.89");
//		 mapgroup04.put("pricePort", "7006");

		System.out.println("=====================开始=====================");
	}
}
