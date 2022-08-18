/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: News Scraper Application
  Author: Nguyen Nam Cuong (s3891758),Nguyen Ngoc Minh (s3907086), Phan Nhat Minh (s3904422), Tran Mai Nhung (s3879954), Nguyen Vu Thuy Duong (s3865443)
  Created  date: 05/17/2022
  Acknowledgement: https://jsoup.org/
                   https://youtu.be/yw7B85174JQ
 */

package com.application.inte2512gfp.entities;

import javafx.scene.image.Image;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class TuoiTre extends NewsSites {
    // define default url
    private final String rssUrl = websiteURL + "rss/";

    // constructor
    public TuoiTre() {
        super("https://tuoitre.vn/", "dd MMM yyyy HH:mm:ss");
    }

    // get the url then fetch by category
    private String getCategoryURL(Category type) {
        return switch (type) {
            case NEWEST -> rssUrl + "thoi-su.rss";
            case POLITICS -> rssUrl + "phap-luat.rss";
            case BUSINESS -> rssUrl + "kinh-doanh.rss";
            case TECHNOLOGY -> rssUrl + "nhip-song-so.rss";
            case HEALTH -> rssUrl + "suc-khoe.rss";
            case ENTERTAINMENT -> rssUrl + "giai-tri.rss";
            case SPORTS -> rssUrl + "the-thao.rss";
            case WORLD -> rssUrl + "the-gioi.rss";
            case COVID -> websiteURL + "tim-kiem.htm?keywords=covid";
            default -> rssUrl + "xe.rss";
        };
    }
    @Override
    public ArrayList<Article> fetchOneType(Category category) {
        ArrayList<Article> categoryArticleList = new ArrayList<>();
        // if category is newest -> re init article category map of newest in NhanDan
        if (category == Category.NEWEST) {
            newestCategoryArticleMap = new HashMap<>();
        }
        try {
            // init
            String title, articleLink, releaseDate;
            Article article;
            Duration publishDuration;
            Image headerImage;
            int count = 0;
            Document doc = Jsoup.connect(getCategoryURL(category)).get();
            // if fetched category is covid
            if (category == Category.COVID) {
                SimpleDateFormat covidDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                for (Element element : doc.select(".news-item")) {
                    //loop to get only 10 articles then break
                    if (count > 9) {
                        break;
                    }
                    // get title
                    title = element.select("a").attr("title");

                    // get into the article link
                    articleLink = websiteURL + element.select("a").attr("href");
                    // fetch the published date, duration, and image
                    Document articleDoc = Jsoup.connect(articleLink).get();
                    releaseDate = articleDoc.select(".date-time").text();
                    publishDuration = calculatePublishTime(releaseDate, covidDateFormat);
                    headerImage = new Image(element.select("a img").attr("src"));
                    //add those elements above into the article list
                    categoryArticleList.add(new Article(websiteURL, articleLink, title, headerImage, releaseDate, publishDuration));
                    count ++;
                }
            }
            else {
                for (Element element : doc.select("item")) {
                    //loop to get only 10 articles then break
                    if (count > 9) {
                        break;
                    }
                    //get title
                    title = Jsoup.parse(Objects.requireNonNull(element.selectFirst("title")).text()).text();
                    // get link
                    articleLink = Jsoup.parse(Objects.requireNonNull(element.selectFirst("link")).text()).text();

                    // image (image in Description tag, written as CDATA)
                    Document desc = Jsoup.parse(Objects.requireNonNull(element.selectFirst("description")).text());
                    headerImage = null;
                    if (desc.selectFirst("img") != null)
                        headerImage = new Image(Objects.requireNonNull(desc.selectFirst("img")).attr("src"));

                    // find the published date, calculate the duration
                    releaseDate = Jsoup.parse(Objects.requireNonNull(element.selectFirst("pubDate")).text()).text().split(", ")[1];
                    publishDuration = calculatePublishTime(releaseDate);

                    // add article
                    article = new Article(websiteURL, articleLink, title, headerImage, releaseDate, publishDuration);
                    categoryArticleList.add(article);

                    // newest only
                    // if it is the "newest" category, find more category for each article to display in "newest" category
                    addListToNewestMap(category, article);

                    count++;
                }
            }
        } catch (Exception e) {
            System.out.println("In TuoiTre: " + e.getMessage());
        }
        return categoryArticleList;
    }

}