package com.education.common.utils; 

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 百度编辑器工具类

public class UeditorUtils {

     private static final String regexScript = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
     private static final String regexStyle = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
     private static final String regexHtml = "<[^>]+>"; //定义HTML标签的正则表达式

    /**
     * 获取html中的纯文本内容
     * @param htmlContent
     * @return
     */
     public static String getContentFromHtml(String htmlContent) {
         Pattern script = Pattern.compile(regexScript, Pattern.CASE_INSENSITIVE);
         Matcher scriptMatcher = script.matcher(htmlContent);
         htmlContent = scriptMatcher.replaceAll(""); //过滤script标签

         Pattern style = Pattern.compile(regexStyle, Pattern.CASE_INSENSITIVE);
         Matcher styleMatcher = style.matcher(htmlContent);
         htmlContent = styleMatcher.replaceAll(""); //过滤style标签

         Pattern html = Pattern.compile(regexHtml,Pattern.CASE_INSENSITIVE);
         Matcher htmlMatcher = html.matcher(htmlContent);
         htmlContent = htmlMatcher.replaceAll(""); //过滤html标签
         return htmlContent.trim(); //返回文本字符串
     }

    public static List<String> getImgHtmlStr(String htmlStr) {
        List<String> list = new ArrayList<>();
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        Pattern imagePattern = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);;
        Matcher matcher = imagePattern.matcher(htmlStr);
        String img = null;
        while (matcher.find()) {
            // 得到<img />数据
            img = matcher.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                list.add(m.group(1));
            }
        }
        return list;
    }

    public static String parserVideo(String html) {
         if (ObjectUtils.isNotEmpty(html)) {
             Document document = Jsoup.parse(html);
             Elements elements = document.getElementsByTag("video");
             // 设置视频标签取消下载功能
             elements.forEach(item -> {
                 item.attr("controls", "");
                 item.attr("oncontextmenu", "return false;"); // 禁用鼠标右键弹出下载菜单
                 item.attr("controlslist", "nodownload");
             });
             return document.body().html();
         }
         return null;
    }
}
