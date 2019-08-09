package com.liyuan.hicspider.util;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by weiyuan on 2019/7/30/030.
 */
public class BossSpider {
    private List<HashMap<String, Object>> dataList = new ArrayList<>();
    public static String bossUrl = "https://www.zhipin.com/";

    public BossSpider(List<HashMap<String, Object>> dataList) {
        this.dataList = dataList;
    }

    /**
     * 公司页面的地址 https://www.zhipin.com/gongsi/?ka=header_brand
     *
     * @param url
     */
    public void spiderBossCity(String url) {
        long startTime = System.currentTimeMillis();
        SpiderUtil spiderUtil = new SpiderUtil(url);
        spiderUtil.pickData();
        if (spiderUtil.statu_code == 200) {
            spiderUtil.analyzeHTMLByString();
        }
        Elements elements = spiderUtil.getElementsByClass("section-city");
        Iterator<Element> iterator = elements.iterator();
        while (iterator.hasNext()) {
            Element liTags = iterator.next();
            Elements liEs = liTags.getElementsByTag("li");
            Iterator<Element> liIterator = liEs.iterator();
            while (liIterator.hasNext()) {
                Element le = liIterator.next();
                Elements aTags = le.getElementsByTag("a");
                Element atag = aTags.first();
                String href = atag.attr("href");
                String ve = atag.text();
                System.out.println("city:" + ve + "//url" + href);
                //取得code
                String str[] = href.split("_");
                String cityCode = str[str.length - 1].replace("/", "");
                HashMap<String, Object> cityMap = new HashMap<>();
                cityMap.put("cityname", ve);
                cityMap.put("citycode", cityCode);
                cityMap.put("url", href);
                this.dataList.add(cityMap);

            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("总数量" + this.dataList.size());
        System.out.println("总耗时:" + (endTime - startTime));
    }

    public String cityGongsi(String url,String citycode) {
        SpiderUtil spiderUtil = new SpiderUtil(url);
        spiderUtil.pickData();
        if (spiderUtil.statu_code == 200) {
            spiderUtil.analyzeHTMLByString();
        }

        Elements gongsiElements = spiderUtil.getElementsByClass("sub-li");
        Iterator<Element> gongsiIterator = gongsiElements.iterator();
        while (gongsiIterator.hasNext()) {
            Element gongsiElement = gongsiIterator.next();
            Elements gongsiInfos = gongsiElement.getElementsByClass("company-info");
            Element gongsiInfo = gongsiInfos.first();
            //公司在boss的地址
            String gongsInfoHref = gongsiInfo.attr("href");
            String gongsicode = gongsInfoHref.split("/")[2].replace(".html", "");
            Elements imgEs = gongsiElement.getElementsByTag("img");
            Element imgE = imgEs.first();
            //公司图标
            String imgSrc = imgE.attr("src");
            Elements mcEs = gongsiElement.getElementsByTag("h4");
            String gongsiMc = mcEs.first().text();
            Elements pTargEs = gongsiElement.getElementsByTag("p");
            System.out.println(pTargEs.first().before("<span class=\"vline\"></span>"));
            String str[] = pTargEs.first().html().split("<span class=\"vline\"></span>");
            String p1 = str[0];
            String p2 = str[1];
            String zjqk = p1.replace("<p>", "");
            String hy = p2.replace("</p>", "");

            HashMap<String, Object> dataMap = new HashMap<>();
            dataMap.put("citycode", citycode);
            dataMap.put("gscode", gongsicode);
            dataMap.put("gsmc", gongsiMc);
            dataMap.put("tbsrc", imgSrc);
            dataMap.put("zjqk", zjqk);
            dataMap.put("hy", hy);
            this.dataList.add(dataMap);
        }

        String nextenable = null;
        String nextHref = null;
        Elements elements = spiderUtil.getElementsByClass("next");
        Iterator<Element> iterator = elements.iterator();
        if (iterator.hasNext()) {
            System.out.println(111);
            Element element = iterator.next();
            nextenable = element.attr("class");
            nextHref = element.attr("href");
        }
        return nextHref;
    }

    public static void main(String[] args) {
        List<HashMap<String, Object>> cityList = new ArrayList<>();
        BossSpider bossSpider = new BossSpider(cityList);
        //  bossSpider.spiderBossCity("https://www.zhipin.com/gongsi/?ka=header_brand");

        String nextHref = bossSpider.cityGongsi("https://www.zhipin.com/job_detail/?ka=header-job","c101010100");
        /*while (true) {
            if ("javascript:;".equals(nextHref)) {
                break;
            }
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            nextHref = bossSpider.cityGongsi("https://www.zhipin.com" + nextHref,"c101010100");
        }*/

    }
}
