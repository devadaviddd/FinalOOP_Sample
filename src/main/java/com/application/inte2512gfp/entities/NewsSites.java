/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: News Scraper Application
  Author: Nguyen Nam Cuong (s3891758),Nguyen Ngoc Minh (s3907086), Phan Nhat Minh (s3904422), Tran Mai Nhung (s3879954), Nguyen Vu Thuy Duong (s3865443)
  Created  date: 05/17/2022
  Acknowledgement:
  - https://jenkov.com/tutorials/java-internationalization/simpledateformat.html
  - https://www.baeldung.com/java-measure-elapsed-time
 */
package com.application.inte2512gfp.entities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public abstract class NewsSites {
    // store category values
    static Category[] categories = Category.values();
    // store category names of articles fetched from news sites
    static HashMap<Category, String[]> dictionaryMap = getDictionary();
    // store website url of newsite
    protected String websiteURL;
    // for converting dateformat when reading from website
    // store default date format of news sites
    protected SimpleDateFormat dateFormat;

    // for storing articles fetched from news sites with category
    protected HashMap<Category, ArrayList<Article>> newestCategoryArticleMap;


    // constructors
    public NewsSites(String websiteURL, String dateFormatString) {
        this.websiteURL = websiteURL;
        this.dateFormat = new SimpleDateFormat(dateFormatString, Locale.US);
        newestCategoryArticleMap = new HashMap<>();
    }

    public NewsSites(String websiteURL) {
        newestCategoryArticleMap = new HashMap<>();
        this.websiteURL = websiteURL;
    }

    // abstract method
    /**
     * get articles of 1 category
     * @param category category of articles
     * @return List of articles
     */
    public abstract ArrayList<Article> fetchOneType(Category category);


    /**
     * calculate release time of article from news site having default date format
     * @param date release date of article
     * @return Duration of article, publish time
     */
    protected Duration calculatePublishTime(String date) {
        return calculatePublishTime(date, dateFormat);
    }

    // for some special category (covid)

    /**
     * Set the time for the news display on the webview
     * for some special category having special dateformat
     * @param date release date of article
     * @param subDateFormat date format of news site (special case of some categories)
     * @return Duration of article, publish time
     */
    protected Duration calculatePublishTime(String date, DateFormat subDateFormat) {
        try {
            // set timezone vn
            subDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            // convert date as string to date object
            Date release = subDateFormat.parse(date);
            // calculate duration from date to now
            return Duration.between(release.toInstant(),
                    Instant.now());

        } catch (Exception e) {
            System.out.println(websiteURL + " " + e.getMessage());
            //  if you cannot parse date, return duration as current time
            return Duration.between(Instant.now(),Instant.now());
        }

    }

    /**
     * Set the time for the news display on the webview
     * for sites having multiple dateformat (day: 1, 10, ...)
     * @param date released date of news
     * @param dateFormatArray collection formats of date
     * @return time released
     */
    protected Duration calculatePublishTime(String date, SimpleDateFormat[] dateFormatArray) {
        // loop through date formats
        for (SimpleDateFormat dateFormat : dateFormatArray) {
            try {
                // set timezone vn
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                // calculate duration from date to now
                return Duration.between((dateFormat.parse(date)).toInstant(),
                        Instant.now());
            } catch (ParseException e) {
                // ignore this dateformat, try next
            }
        }
        //  if you cannot parse date, return duration as current
        return Duration.between(Instant.now(), Instant.now());

    }


    // dictionary file for adding sub category

    /**
     * dictionary map (category, keywords of that category) for adding sub category
     * to articles from newest
     * @return dictionary map
     */
    private static HashMap<Category, String[]> getDictionary() {
        HashMap<Category, String[]> dictionaryMap = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("./src/main/java/com/application/inte2512gfp/Dictionary/Keywords.txt"));
            String line;
            int categoryIndex = 1;
            while ((line = br.readLine()) != null) {
                String[] words = line.split(", ");
                assert categories != null;
                dictionaryMap.put(categories[categoryIndex ++], words);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Cannot read file:" + e.getMessage());
        }
        return dictionaryMap;
//        this.dictionaryMap = dictionaryMap;
    }

    /**
     * define sub categories to each article from newest
     * @param title title of article
     * @return list of sub categories of article from newest
     */
    static LinkedList<Category> findCategory(String title) {
        // this function use for Newest only
        LinkedList<Category> categoryList = new LinkedList<>();
        // loop through category list
        for(Category category : categories) {
            // if newest / others, skip
            if (category.equals(Category.NEWEST) || category.equals(Category.OTHERS))
                continue;
            // loop through category's keywords in map
            for (String word : dictionaryMap.get(category)) {
                // if title contains keyword, add category to list
                if (title.toLowerCase(Locale.ROOT).contains(word.toLowerCase(Locale.ROOT))) {
//                    return category;
                    categoryList.add(category);
                    break;
                }
            }
            // if category list size is 2 -> return (because we have 2 categories)
            if (categoryList.size() == 2) return categoryList;
        }

        // if no category -> add Others
        if (categoryList.size() == 0) categoryList.add(Category.OTHERS);
//        return Category.OTHERS;
        return categoryList;

    }

    /**
     * add categories to list of articles from newest
     * @param type type of category
     * @param article article to add
     */
    protected void addListToNewestMap(Category type, Article article) {
        // this function use for Newest only
        if (type == Category.NEWEST) {
            // loop through category list
            for (Category subCategory : findCategory(article.getTitle())) {
                // if category is not in map, add it
                newestCategoryArticleMap.computeIfAbsent(subCategory, k -> new ArrayList<>());
                // add article to category map list of articles in newest
                newestCategoryArticleMap.get(subCategory).add(article);
            }

        }
    }


    //getters
    public String getWebsiteURL() {
        return websiteURL;
    }
    public HashMap<Category, ArrayList<Article>> getNewestCategoryArticleMap() {
        return newestCategoryArticleMap;
    }
}