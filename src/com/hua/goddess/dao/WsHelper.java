/**
 * 
 */
package com.hua.goddess.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

//import baidu.mapdemo.activity.R;

public class WsHelper {

	// final String WEB_SERVICE_URL =
	// "http://202.206.212.74:8000/WebService1.asmx";
	final String WEB_SERVICE_URL = "http://202.206.212.143:7880/WebService1.asmx";
	final String Namespace = "http://tempuri.org/";// 命名空间

	public interface InterfaceCallBack {
		void RequestCallBack(Object result);
	}

	public InterfaceCallBack WsRequestCallBack = null;

	List<String> selinfolist = new ArrayList<String>();

	@SuppressWarnings("rawtypes")
	public String CallWebService(String MethodName, Map<String, String> Params) {
		// 1、指定webservice的命名空间和调用的方法名
		SoapObject request = new SoapObject(Namespace, MethodName);
		// 2、设置调用方法的参数值，如果没有参数，可以省略，
		if (Params != null) {
			Iterator iter = Params.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				request.addProperty((String) entry.getKey(),
						(String) entry.getValue());
			}
		}
		// 3、生成调用Webservice方法的SOAP请求信息。该信息由SoapSerializationEnvelope对象描述
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER12);
		envelope.bodyOut = request;
		// c#写的应用程序必须加上这句
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(WEB_SERVICE_URL);
		// 使用call方法调用WebService方法
		try {
			ht.call(null, envelope);
		} catch (HttpResponseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		try {
			SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
			if (result != null) {
				Log.d("----收到的回复----", result.toString());
				return result.toString();
			}

		} catch (SoapFault e) {
			Log.e("----发生错误---", e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public List<String> CallWebService1(String MethodName,
			Map<String, String> Params) {
		List<String> resultlist = new ArrayList<String>();
		// 1、指定webservice1的命名空间和调用的方法名

		SoapObject request = new SoapObject(Namespace, MethodName);
		// 2、设置调用方法的参数值，如果没有参数，可以省略，
		if (Params != null) {
			Iterator iter = Params.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				request.addProperty((String) entry.getKey(),
						(String) entry.getValue());
			}
		}
		// 3、生成调用Webservice方法的SOAP请求信息。该信息由SoapSerializationEnvelope对象描述
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER12);
		envelope.bodyOut = request;
		// c#写的应用程序必须加上这句
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(WEB_SERVICE_URL);
		// 或者HttpTransportSE httpTranstation=new HttpTransportSE(WSDL);
		try {

			ht.call(null, envelope);
			SoapObject result = (SoapObject) envelope.getResponse();
			// 下面对结果进行解析，结构类似json对象
			int count = result.getPropertyCount();
			for (int index = 0; index < count; index++) {
				resultlist.add(result.getProperty(index).toString());

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultlist;
	}

	/**
	 * 执行异步任务
	 * 
	 * @param params
	 *            方法名+参数列表（哈希表形式）
	 */
	public void Request3(Object... params) {
		new AsyncTask<Object, Object, String>() {

			@SuppressWarnings("unchecked")
			@Override
			protected String doInBackground(Object... params) {
				if (params != null && params.length == 2) {
					return CallWebService((String) params[0],
							(Map<String, String>) params[1]);
				} else if (params != null && params.length == 1) {
					return CallWebService((String) params[0], null);
				} else {
					return null;
				}
			}

			protected void onPostExecute(String result) {
				if (WsRequestCallBack != null) {
					WsRequestCallBack.RequestCallBack(result);
				}
			};

		}.execute(params);
	}

	public void Request(Object... params) { // 执行异步任务, 参数：方法名+参数列表（哈希表形式）
		new AsyncTask<Object, Object, Object>() {

			@SuppressWarnings("unchecked")
			@Override
			protected String doInBackground(Object... params) {
				if (params != null && params.length == 2) {

					return CallWebService((String) params[0],
							(Map<String, String>) params[1]);

				} else if (params != null && params.length == 1) {

					return CallWebService((String) params[0], null);
				} else {
					return null;
				}
			}

		}.execute(params);
	}

	public void Request2(Object... params) {// 异步方法原型，返回List<String>
		new AsyncTask<Object, Object, Object>() {

			@SuppressWarnings("unchecked")
			@Override
			protected List<String> doInBackground(Object... params) {
				if (params != null && params.length == 2) {
					selinfolist = CallWebService1((String) params[0],
							(Map<String, String>) params[1]);
					return selinfolist;

				} else if (params != null && params.length == 1) {
					selinfolist = CallWebService1((String) params[0], null);
					return selinfolist;
				} else {
					return null;
				}
			}

		}.execute(params);
	}

	public void Request4(Object... params) {// 从数据库返回所要求的list<String>
		new AsyncTask<Object, Object, List<List<String>>>() {
			@SuppressWarnings("unchecked")
			@Override
			protected List<List<String>> doInBackground(Object... params) {
				List<String> selinfolist8 = new ArrayList<String>();
				List<List<String>> para = new ArrayList<List<String>>();
				selinfolist8 = CallWebService1((String) params[0],
						(Map<String, String>) params[1]);
				para.add(selinfolist8);
				para.add((List<String>) params[2]);
				return para;
			}

			@Override
			protected void onPostExecute(List<List<String>> infolist) {
				int cout = infolist.get(0).size();
				infolist.get(1).clear();
				for (int i = 0; i < cout; i++) {
					infolist.get(1).add(i, infolist.get(0).get(i));
				}
				if (WsRequestCallBack != null) {
					WsRequestCallBack.RequestCallBack(infolist.get(1));
				}
			}
		}.execute(params);
	}

	public void Requestwz(Object... params) {// 从数据库返回所要求的list<String>
		new AsyncTask<Object, Object, List<List<String>>>() {
			@SuppressWarnings("unchecked")
			@Override
			protected List<List<String>> doInBackground(Object... params) {
				List<String> selinfolist8 = new ArrayList<String>();
				List<List<String>> para = new ArrayList<List<String>>();
				selinfolist8 = CallWebService1((String) params[0],
						(Map<String, String>) params[1]);
				para.add(selinfolist8);

				return para;
			}

			@Override
			protected void onPostExecute(List<List<String>> infolist) {

				if (WsRequestCallBack != null) {
					WsRequestCallBack.RequestCallBack(infolist.get(0));
				}
			}
		}.execute(params);
	}

	private static byte[] readStream(InputStream inStream) throws Exception {

		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outstream.write(buffer, 0, len);
		}
		outstream.close();
		return outstream.toByteArray();
	}

}
