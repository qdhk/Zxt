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

/**
 * @author Administrator
 * 
 */
public class GetNotiData {
	public final static String Url = "http://202.206.217.109/index.php/";
	public final static String Url1 = "http://202.206.217.109";
	public HashMap<String, String> COOKIES = new HashMap<String, String>();

	public static List<Noti> getNotis(int id) {
		List<Noti> notis = new ArrayList<Noti>();
		try {
			Document document = Jsoup.connect(Url).post();

			// Elements elements = document.getElementsByClass("mainNewsa");
			Element mid_content = document.select("div.mid_content").first();
			Element right_content = document.select("div.right_content")
					.first();
			// Elements items = element2.getElementsByTag("li");
			Elements items_0 = mid_content.child(0).getElementsByTag("li");// 通知公告
			Elements items_1 = mid_content.child(1).getElementsByTag("li");// 学生活动
			Elements items_2 = right_content.child(1).getElementsByTag("li");// 新闻动态
			switch (id) {
			case 0:
				for (Element element : items_0) {
					Noti noti = new Noti();
					noti.setTitle(element.select("a").text());
					noti.setDate(element.select("span").text());
					noti.setURL(Url1
							+ element.select("a").attr("href").toString());
					notis.add(noti);
				}
				break;
			case 1:
				for (Element element : items_1) {
					Noti noti = new Noti();
					noti.setTitle(element.select("a").text());
					noti.setDate(element.select("span").text());
					noti.setURL(Url1
							+ element.select("a").attr("href").toString());
					notis.add(noti);
				}
				break;
			case 2:
				for (Element element : items_2) {
					Noti noti = new Noti();
					noti.setTitle(element.select("a").text());
					noti.setDate(element.select("span").text());
					noti.setURL(Url1
							+ element.select("a").attr("href").toString());
					notis.add(noti);
				}

				break;

			default:
				break;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return notis;
	}
}
