/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: News Scraper Application
  Author: Nguyen Nam Cuong (s3891758),Nguyen Ngoc Minh (s3907086), Phan Nhat Minh (s3904422), Tran Mai Nhung (s3879954), Nguyen Vu Thuy Duong (s3865443)
  Created  date: 05/17/2022
  Acknowledgement: https://jsoup.org/
                   https://youtu.be/yw7B85174JQ
                   https://stackoverflow.com/questions/8257641/java-how-to-convert-a-string-hhmmss-to-a-duration
 */

package com.application.inte2512gfp.entities;

import javafx.scene.image.Image;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class ThanhNien extends NewsSites {

    //Constructor
    public ThanhNien() {
        super("https://thanhnien.vn/", "dd MMM yyyy HH:mm:ss");
    }

    //Generate the tail url of each category url
    private String getURL(Elements link, String key) {
        for(Element e: link)
        {
            if(e.attr("href").contains(key)) {
                return e.attr("href");
            }
        }
        return null;
    }

    //Generate category URL
    private String getCategoryURL(Category type) {
        try {
            Document doc = Jsoup.connect("https://thanhnien.vn/rss.html").get();
            Elements link = doc.getElementsByTag("a");
            if (type == Category.NEWEST) {
                return websiteURL + getURL(link, "home");
            } else if (type == Category.COVID) {
                return websiteURL + "covid-19/";
            } else if (type == Category.POLITICS) {
                return getURL(link, "chinh-tri-227");
            } else if (type == Category.BUSINESS) {
                return getURL(link, "tai-chinh-kinh-doanh-49");
            } else if (type == Category.TECHNOLOGY) {
                return getURL(link, "cong-nghe-game-315");
            } else if (type == Category.HEALTH) {
                return getURL(link, "suc-khoe-65");
            } else if (type == Category.SPORTS) {
                return getURL(link, "the-thao-318");
            } else if (type == Category.ENTERTAINMENT) {
                return getURL(link, "giai-tri-285");
            } else if (type == Category.WORLD) {
                return getURL(link, "the-gioi-348");
            } else if (type == Category.OTHERS) {
                return getURL(link, "doi-song-17");
            }
        } catch (IOException e) {
            System.out.println("Cannot scrape the news because " + e.getMessage() + ". Please reload the page");
        }
        return "";
    }

    //Fetch each article of given category
    public ArrayList<Article> fetchOneType(Category type) {
        ArrayList<Article> articleList = new ArrayList<>();
        // if category is newest -> re init article category map of newest in NhanDan
        if (type == Category.NEWEST) newestCategoryArticleMap = new HashMap<>();
        try {
            // initialize
            String title, articleLink, smallDescription, releaseDate;
            Duration publishDuration;
            Article article;
            // get doc
            Document doc = Jsoup.connect(getCategoryURL(type)).get();

            int count = 0;
            //Scrape non RSS url
            if (!getCategoryURL(type).contains("rss")) {
                // create covid date format
                SimpleDateFormat covidDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                Elements elementList = doc.getElementsByClass("story");
                // loop through element
                for (Element element : elementList) {
                    if(count > 9)
                        break;
                    // get title
                    title = element.getElementsByClass("story__title cms-link").text();
                    // get image link -> create image if exist
                    String  imageURL = element.select( "img").attr("src");
                    if(!imageURL.contains("http"))
                    {
                        imageURL = element.select("img").attr("data-src");
                    }
                    Image headerImage = new Image(imageURL);

                    // get article link
                    articleLink = element.getElementsByClass("story__thumb").attr("href");
                    // get time of publish
                    String timeFetch = element.getElementsByClass("time").text();
                    // only get duration
                    if (timeFetch.contains("trước")) {

                        String[] times = timeFetch.split(" ", 0);
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
                    //Normal date format
                    else {
                        publishDuration = calculatePublishTime(timeFetch, covidDateFormat);
                    }

                    releaseDate = "";

                    article = new Article(websiteURL, articleLink, title, headerImage, releaseDate, publishDuration);
                    articleList.add(article);
                    count++;
                }
            }
            else { //Scrape RSS URL
                // init
                Image headerImage;
                Elements elementList = doc.select("item");
                // loop through item elements
                for (Element element : elementList) {
                    if (count > 9)
                        break;
                    // get title
                    title = element.child(0).text();

                    // header image
                    headerImage = null;
                    if (element.child(1).tagName().equals("image")) headerImage = new Image(element.child(1).text());

                    // get duration
                    publishDuration = calculatePublishTime(releaseDate = element.child(3).text().split(", ", 0)[1]);
                    // get article link
                    articleLink = element.child(6).text();

                    article = new Article(websiteURL, articleLink, title, headerImage, releaseDate, publishDuration);
                    articleList.add(article);

                    // newest category only
                    // add articles fetched from newest category to newestCategoryArticleMap
                    addListToNewestMap(type, article);
                    count++;

                }
            }
        }
        catch (Exception e) {
            System.out.println("Cannot scrape the news in ThanhNien because " + e.getMessage() + ". Please reload the page");
        }
        return articleList;
    }

}