/**
 * 
 */
package com.hua.goddess.base.communicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hua.goddess.entity.Noti;
import com.hua.goddess.entity.NotiDetail;

/**
 * @author Administrator
 * 
 */
public class GetNotiDataDetail {

	public HashMap<String, String> COOKIES = new HashMap<String, String>();

	public static NotiDetail getNotiDetail(String URL) {
		NotiDetail notiDetail = new NotiDetail();
		try {

			Document document = Jsoup.connect(URL).timeout(10000).post();
			Element title = document.getElementsByClass("title").first();
			Element info = document.getElementsByClass("info").first();
			Element content = document.getElementsByClass("content").first();
			Elements content_children = content.children();

			String content_str = "";
			notiDetail.setTitle(title.text());
			notiDetail.setInfo(info.text());

			// Element mid_content = document.select("div.mid_content").first();

			for (Element item : content_children) {
				content_str += item.text();
				content_str += "\n";
			}
			notiDetail.setContent(content_str);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return notiDetail;
	}

}
