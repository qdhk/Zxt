/**
 * 
 */
package com.hua.goddess.dao;

/**
 * @author Niurui
 *
 */

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import android.util.Log;

public class WsRequestHelper {
	public interface InterfaceCallBack {
		void RequestCallBack(Object result);
	}

	// 服务器链接
	final String WEB_SERVICE_URL = "http://202.206.212.143:7880/WebService1.asmx?wsdl";
	final String Namespace = "http://tempuri.org/";// 命名空间
	// 请求回调函数
	public InterfaceCallBack WsRequestCallBack = null;

	/**
	 * 构造函数
	 * 
	 * @param wsRequestCallBack
	 */
	public WsRequestHelper(InterfaceCallBack wsRequestCallBack) {
		this.WsRequestCallBack = wsRequestCallBack;
	}

	public WsRequestHelper() {

	}

	/**
	 * 调用WebService
	 * 
	 * @return WebService的返回值
	 * 
	 */
	private Object CallWebService(String MethodName, Map<String, Object> Params) {
		// 1、指定webservice的命名空间和调用的方法名

		SoapObject request = new SoapObject(Namespace, MethodName);
		// 2、设置调用方法的参数值，如果没有参数，可以省略，
		if (Params != null) {
			Iterator iter = Params.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				request.addProperty((String) entry.getKey(),
						(Object) entry.getValue());
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
			// final SoapPrimitive result = (SoapPrimitive)
			// envelope.getResponse();
			Object result = (Object) envelope.getResponse();
			// SoapObject result = (SoapObject) envelope.bodyIn;
			if (result != null) {
				// Log.d("----收到的回复----", result.toString());
				return result;
			}

		} catch (Exception e) {
			// Log.e("----发生错误---", e.getMessage());
			// e.printStackTrace();
		}
		return null;
	}

	/**
	 * 执行异步任务
	 * 
	 * @param params
	 *            方法名+参数列表（哈希表形式）
	 */
	public void Request(Object... params) {
		new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				if (params != null && params.length == 2) {
					return CallWebService((String) params[0],
							(Map<String, Object>) params[1]);
				} else if (params != null && params.length == 1) {
					return CallWebService((String) params[0], null);
				} else {
					return null;
				}
			}

			@Override
			protected void onPostExecute(Object result) {
				if (result != null && WsRequestCallBack != null) {
					WsRequestCallBack.RequestCallBack(result);
				}
			};

		}.execute(params);
	}
}
