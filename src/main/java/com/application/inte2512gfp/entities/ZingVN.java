/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: News Scraper Application
  Author: Nguyen Nam Cuong (s3891758),Nguyen Ngoc Minh (s3907086), Phan Nhat Minh (s3904422), Tran Mai Nhung (s3879954), Nguyen Vu Thuy Duong (s3865443)
  Created  date: 05/17/2022
  Acknowledgement:
    - https://stackoverflow.com/questions/8257641/java-how-to-convert-a-string-hhmmss-to-a-duration
    - https://jsoup.org/
    - https://youtu.be/yw7B85174JQ
 */
package com.application.inte2512gfp.entities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javafx.scene.image.Image;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class ZingVN extends NewsSites{

    /**
     * List contain possible format date that could be appeared on ZingVN
     */
    SimpleDateFormat[] dateFormatArray = {
            new SimpleDateFormat("HH:mm dd/MM/yyyy"),
            new SimpleDateFormat("HH:mm dd/M/yyyy"),
            new SimpleDateFormat("HH:mm d/M/yyyy"),
            new SimpleDateFormat("HH:mm d/MM/yyyy")
    };

    /*
    Constructor
     */
    public ZingVN() {
        super("https://zingnews.vn");
    }

    /**
     *
     * @param link link of the input article
     * @return Article that contains all the demand information: Image, category, title, time
     * @throws ParseException
     */
    public Article fetchContent(String link)  {
        try {
            Image image;
            // Get the document to scrape
            Document doc = Jsoup.connect(link).get();
            // Category
            HashSet<Category> categories = new HashSet<>();
            Elements cate = doc.getElementsByClass("the-article-category");
            for (Element c : cate) {
                String cate1 = c.getElementsByTag("a").attr("href");
                Category category = checkCategory(cate1);
                if (category == null) {
                    if (doc.getElementsContainingText("covid") != null) {
                        categories.add(Category.COVID);
                    }
                }
                categories.add(category);
            }

            // get title
            String title = doc.getElementsByClass("the-article-title").text();

            // get image url -> get image
            String picSource = doc.select(".pic img").attr("data-src");
            image = null;
            if (!picSource.equals("")) image = new Image(picSource);
            else {
                picSource = doc.select(".article-thumbnail a img").attr("data-src");
                if (!picSource.equals("")) image = new Image(picSource);
            }

            // Released Date + pulish duration
            Duration publishDuration;
            String time = doc.getElementsByClass("the-article-friendly-time").text();
            // check time format
            if (time.contains("trước")) {
                // only get duration
                String[] times = time.split(" ", 0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < times.length - 1; i ++) {
                    if (i % 2 != 0) {
                        if (times[i].equals("giờ"))
                            times[i] = "H";
                        else if (times[i].equals("phút"))
                            times[i] = "M";
                    }
                    sb.append(times[i]);
                }
                // convert string to duration
                publishDuration = Duration.parse("PT" + sb.toString());
            }
            // If the time format is as default
            else publishDuration = calculatePublishTime(time, dateFormatArray);

            return new Article(websiteURL, link, title, image, time, publishDuration);
        } catch (IOException e) {
            System.out.println("In ZingVn: " + e.getMessage());
        }
        return null;
    }

    /**
     *
     * @param input category type, name of category
     * @return Category as default of app
     */
    private Category checkCategory(String input) {
        Category category = null;
        switch (input) {
            case "thế giới": category = Category.WORLD; break;
            case "kinh-doanh-tai-chinh": category = Category.BUSINESS; break;
            case "the-thao": category = Category.SPORTS; break;
            case "suc-khoe": category = Category.HEALTH; break;
            case "phap-luat": category = Category.POLITICS; break;
            case "giai-tri": category = Category.ENTERTAINMENT; break;
            case "cong-nghe": category = Category.TECHNOLOGY; break;
            case "giao-duc": category = Category.OTHERS; break;
        }
        return category;
    }

    /**
     *
     * @param category one category
     * @return return array of article links
     */
    public ArrayList<String> fetchArticleList(Category category) {
        try {
            HashMap<String, String> allCategories = fetchAllCategory();
            String cateLink = allCategories.get(category.toString().toLowerCase());
            Elements elements = Jsoup.connect(cateLink).get().getElementsByClass("article-title");
            ArrayList<String> articleLinks = new ArrayList<>();
            for (Element e : elements) {
                String articleLink = e.getElementsByTag("a").attr("href");
                if (articleLink.contains("video"))
                    continue;
                articleLinks.add(websiteURL + articleLink); // attach html tag to the original/home link of website

                if (articleLinks.size() == 10) break;

            }
            return articleLinks;
        } catch (IOException e) {
            System.out.println("In ZingVn: " + e.getMessage());
        }
        return null;
    }

    /**
     * function to collect all the required categories of Zing news
     * @return list with each entity containing name of category and category link
     */
    private HashMap<String, String> fetchAllCategory() {
        HashMap<String, String> categoryList = new HashMap<>();
        try {
            Elements elements = Jsoup.connect(websiteURL).get().getElementsByClass("parent");
            ArrayList<String> tails = new ArrayList<>();
            for (Element element : elements) {
                String tail = element.getElementsByTag("a").attr("href");

                // Prevent duplicated
                if (!tails.contains(tail)) {
                    tails.add(tail);
                } else continue;

                if (tail.contains("kinh-doanh-tai-chinh")) {
                    categoryList.put("business", this.websiteURL + tail);
                } else if (tail.contains("the-thao")) {
                    categoryList.put("sports", this.websiteURL + tail);
                } else if (tail.contains("suc-khoe")) {
                    categoryList.put("health", this.websiteURL + tail);
                } else if (tail.contains("phap-luat")) {
                    categoryList.put("politics", this.websiteURL + tail);
                } else if (tail.contains("giai-tri")) {
                    categoryList.put("entertainment", this.websiteURL + tail);
                } else if (tail.contains("the-gioi")) {
                    categoryList.put("world", this.websiteURL + tail);
                } else if (tail.contains("cong-nghe")) {
                    categoryList.put("technologyy", this.websiteURL + tail);
                } else if (tail.contains("giao-duc")) {
                    categoryList.put("others", this.websiteURL + tail);
                }

                categoryList.put("covid", this.websiteURL + "/covid-tim-kiem.html");
                categoryList.put("newest", this.websiteURL);
            }
            return categoryList;
        } catch (IOException e) {
            System.out.println("In ZingVn: " + e.getMessage());
        }
        return null;
    }

    /**
     *
     * @param type  category type
     * @return a arraylist contain article list
     */
    @Override
    public ArrayList<Article> fetchOneType(Category type) {
        ArrayList<Article> categoryArticleList = new ArrayList<>();
        try {
            // if category is newest -> re init article category map of newest in NhanDan
            if (type == Category.NEWEST) {
                newestCategoryArticleMap = new HashMap<>();
            }
            Article article;
            for (String articleLink : fetchArticleList(type)) {
                article = fetchContent(articleLink);
                categoryArticleList.add(article);

                // newest category only
                // add articles fetched from newest category to newestCategoryArticleMap
                addListToNewestMap(type, article);
            }
        } catch (Exception e) {
            System.out.println("Error in ZingVn: " + e.getMessage());
        }
        return categoryArticleList;
    }

}