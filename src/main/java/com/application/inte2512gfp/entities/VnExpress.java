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
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;


public class VnExpress extends NewsSites {
    // define the date format for covid category
    SimpleDateFormat[] covidDateFormatArray = {
            new SimpleDateFormat("dd/MM/yyyy, HH:mm"),
            new SimpleDateFormat("dd/M/yyyy, HH:mm"),
            new SimpleDateFormat("d/M/yyyy, HH:mm"),
            new SimpleDateFormat("d/MM/yyyy, HH:mm")
    };
    // constructor
    public VnExpress() {

        super("https://vnexpress.net/", "dd MMM yyyy HH:mm:ss z");
    }

    // get url for each category display
    private String getCategoryURL(Category type) {
        return switch (type) {
            case NEWEST -> websiteURL + "rss/tin-moi-nhat.rss";
            case POLITICS -> websiteURL + "rss/thoi-su.rss";
            case BUSINESS -> websiteURL + "rss/kinh-doanh.rss";
            case TECHNOLOGY -> websiteURL + "rss/so-hoa.rss";
            case HEALTH -> websiteURL + "rss/suc-khoe.rss";
            case ENTERTAINMENT -> websiteURL + "rss/giai-tri.rss";
            case SPORTS -> websiteURL + "rss/the-thao.rss";
            case WORLD -> websiteURL + "rss/the-gioi.rss";
            case COVID -> websiteURL + "covid-19/tin-tuc";
            default -> websiteURL + "rss/oto-xe-may.rss";
        };

    }
    // fetch articles base on category
    @Override
    public ArrayList<Article> fetchOneType(Category type) {
        ArrayList<Article> categoryArticleList = new ArrayList<>();
        // if type is NEWEST -> find subcategories for each articles
        if (type == Category.NEWEST) newestCategoryArticleMap = new HashMap<>();
        // init
        String title, articleLink, releaseDate;
        Duration publishDuration;
        Image headerImage;
        Article article;
        int count = 0;
        try {
            // get document
            Document doc = Jsoup.connect(getCategoryURL(type)).get();
            if (type == Category.COVID) {
                // if category is covid
                // create element list to store all articles element for fetching
                Elements elements = doc.select(".item-news-common");
                // loop through all articles
                for (Element element : elements) {
                    // if fetch over 10 -> stop fetching
                    if (count > 9)
                        break;
                    // title
                    title = element.select("h3 a").text();
                    // if no title -> skip
                    if (title.isEmpty()) continue;

                    // article link
                    articleLink = element.select("h3 a").attr("href");

                    // fetch image
                    headerImage = null;
                    if (element.select("div.thumb-art").size() == 0)
                        continue;
                    String imageSource = Objects.requireNonNull(element.select(".thumb-art picture img").first())
                                                .attr("src");
                    // special case in image url
                    if (!imageSource.contains("https://"))
                        imageSource = Objects.requireNonNull(element.select(".thumb-art picture img").first())
                                             .attr("data-src");
                    headerImage = new Image(imageSource);

                    // connect article link to get date
                    releaseDate = Jsoup.connect(articleLink).get().select(".date").text().split(", ", 2)[1];
                    publishDuration = calculatePublishTime(releaseDate, covidDateFormatArray);

                    // create article -> add to list
                    categoryArticleList.add(new Article(websiteURL,
                                                        articleLink,
                                                        title,
                                                        headerImage,
                                                        releaseDate,
                                                        publishDuration));
                    count++;
                }

            } else {
                // if category is not covid
                // create element list to store all articles element for fetching
                Elements elementList = doc.select("item");
                // loop through all articles
                for (Element element : elementList) {
                    // if over 10
                    if (count > 9)
                        break;
                    // fetch title
                    title = element.child(0).text();

                    // fetch image
                    headerImage = null;
                    // take image source from element
                    if (element.selectFirst("description") != null) {
                        Document desc = Jsoup.parse(Objects.requireNonNull(element.selectFirst("description")).text());
                        if (desc.selectFirst("img") != null)
                            headerImage = new Image(Objects.requireNonNull(desc.selectFirst("img")).attr("src"));
                    }

                    // fetch publish time
                    publishDuration = calculatePublishTime(releaseDate = element.child(2).text().split(", ", 0)[1]);
                    // fetch article link
                    articleLink = element.child(3).text();
                    // create article -> add to list
                    article = new Article(websiteURL, articleLink, title, headerImage, releaseDate, publishDuration);
                    categoryArticleList.add(article);

                    //newest only
                    // if it is the "newest" category, find more category for each article to display in "newest" category
                    addListToNewestMap(type, article);
                    count++;
                }
            }
        } catch (Exception e) {
            // if exception occurs
            System.out.println("Cannot scrape the news in VnExpress because " + e.getMessage() + ". Please reload the page");
        }
        return categoryArticleList;
    }

}