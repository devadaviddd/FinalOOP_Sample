/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: News Scraper Application
  Author: Nguyen Nam Cuong (s3891758),Nguyen Ngoc Minh (s3907086), Phan Nhat Minh (s3904422), Tran Mai Nhung (s3879954), Nguyen Vu Thuy Duong (s3865443)
  Created  date: 05/17/2022
  Acknowledgement:
    - https://jsoup.org/
    - https://youtu.be/yw7B85174JQ
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

public class NhanDan extends NewsSites {
    // date format array of NhanDan only
    SimpleDateFormat[] dateFormatArray = {
            dateFormat,
            new SimpleDateFormat("dd-MM-yyyy HH:mm")
    };
    // constructor
    public NhanDan() {
        super("https://nhandan.vn/", "dd-MM-yyyy, HH:mm");
    }

    /**
     * get website url by category
     * @param type category
     * @return website url
     */
    private String getCategoryURL(Category type) {
        return switch (type) {
            case NEWEST -> websiteURL;
            case POLITICS -> websiteURL + "chinhtri";
            case BUSINESS -> websiteURL + "kinhte";
            case TECHNOLOGY -> websiteURL + "khoahoc-congnghe";
            case HEALTH -> websiteURL + "y-te";
            case ENTERTAINMENT -> websiteURL + "du-lich";
            case SPORTS -> websiteURL + "thethao";
            case WORLD -> websiteURL + "thegioi";
            case COVID -> websiteURL + "tag/Covid19-53";
            default -> websiteURL + "moi-truong";
        };

    }

    /**
     * fetch news list by category
     * @param type category of articles
     * @return list of news
     */
    @Override
    public ArrayList<Article> fetchOneType(Category type) {
        ArrayList<Article> categoryArticleList = new ArrayList<>();
        // if category is newest -> re init article category map of newest in NhanDan
        if (type == Category.NEWEST) newestCategoryArticleMap = new HashMap<>();

        // init
        String title, articleLink, releaseDate;
        Duration publishDuration;
        Image headerImage;
        Article article;

        // have most read news -> use to check if same articles are fetched
        HashSet<String> titleSet = new HashSet<>();
        try {
            // get document
            Document document = Jsoup.connect(getCategoryURL(type)).get();

            // get articles elements only
            Elements elements = document.select("article");
            // for checking amount of articles fetched
            int count = 0;
            for (Element element : elements) {
                if (count > 9) break;
                // get image
                Element imgElement = element.getElementsByTag("img").first();
                // define image source for create an image object
                String imgSource = null;
                if (imgElement != null) imgSource = imgElement.attr("data-src");
                if (imgSource != null && !imgSource.isEmpty()) headerImage = new Image(imgSource);
                else continue; // no image no fetch

                // get title
                title = element.select(".box-img a").attr("title");
                // check if title in popular news
                if (!titleSet.add(title)) continue;

                // get article link, check if contain special link -> if contain, not fetch
                String subArticleLink = element.select(".box-title a").attr("href").substring(1);
                if (subArticleLink.contains("special")) continue;
                // recreate article link in right format
                articleLink = (subArticleLink.contains("://")? "h" : websiteURL) + subArticleLink;

                // access to article link to get date
                Document articleDocument = Jsoup.connect(articleLink).get();
                // date -> find publish duration
                releaseDate = articleDocument.select(".box-date").text().split(", ", 2)[1];
                publishDuration = calculatePublishTime(releaseDate, dateFormatArray);

                // add article to list
                article = new Article(websiteURL, articleLink, title, headerImage, releaseDate, publishDuration);
                categoryArticleList.add(article);
                // newest category only
                // add articles fetched from newest category to newestCategoryArticleMap
                addListToNewestMap(type, article);

                count ++;
            }
        }
        catch (Exception exception) {
            System.out.println("Exception in NhanDan: " + exception.getMessage());
        }
        return categoryArticleList;
    }
}