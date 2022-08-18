/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: News Scraper Application
  Author: Nguyen Nam Cuong (s3891758),Nguyen Ngoc Minh (s3907086), Phan Nhat Minh (s3904422), Tran Mai Nhung (s3879954), Nguyen Vu Thuy Duong (s3865443)
  Created  date: 05/17/2022
  Acknowledgement:
    - https://jenkov.com/tutorials/java-internationalization/simpledateformat.html
    - https://www.tabnine.com/code/java/methods/java.time.Duration/toDays
    - https://stackoverflow.com/questions/24378658/number-of-days-conversion-in-java
    - https://www.javaiq.in/2020/02/hashset-equals-method-in-java.html
    - https://stackoverflow.com/questions/2265503/why-do-i-need-to-override-the-equals-and-hashcode-methods-in-java
 */
package com.application.inte2512gfp.entities;

import javafx.scene.image.Image;
import java.time.Duration;

public class Article {
    String websiteURL;
    String articleURL;
    String title;
    Image headerImage;
    String releaseDate;
    Duration publishDuration;
    Category subCategory;


    // constructor no category list
    public Article(String websiteURL, String articleURL, String title, Image headerImage, String releaseDate, Duration publishDuration) {
        this.websiteURL = websiteURL;
        this.articleURL = articleURL;
        this.title = title;
        this.headerImage = headerImage;
        this.releaseDate = releaseDate;
        this.publishDuration = publishDuration;
        this.subCategory = null;
    }
    // getters
    public String getTitle() {
        return title;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public String getArticleURL() {
        return articleURL;
    }

    public Image getHeaderImage() {
        return headerImage;
    }

    public Duration getPublishDuration() {
        return publishDuration;
    }

    /**
     * convert duration to readable date format as string
     * @return return time published of article as string
     */
    public String convertDurationToString() {
        // convert duration to days
        long days = publishDuration.toDays();
        // find remaining duration
        publishDuration = publishDuration.minusDays(days);
        // convert remaining duration to hours
        long hours = publishDuration.toHours();
        // calculate remaining duration
        publishDuration = publishDuration.minusHours(hours);
        // convert remaining duration to minutes
        long minutes = publishDuration.toMinutes();
        // converts days to years
        int years = (int) (days / 365);
        // converts days to months
        int months = (int) (days / 30.41);
        // converts days to weeks
        int weeks = (int) (days /7);

        // if only minutes
        if (hours == 0 && days == 0 && weeks == 0 && months == 0 && years == 0)
            return String.format("%02d minutes ago", minutes);
        // if have hours
        else if (hours < 24 && days == 0 && weeks == 0 && months == 0 && years == 0)
            return String.format("%02dh %02d%c ago", hours, minutes > 0 ? minutes : 0, minutes > 1 ? 's' : '\0');
        // if over than 24 hours
        else  if (days > 0 && weeks == 0 && months == 0 && years == 0)
            return String.format("%02d day%c ago", days, days > 1 ? 's' : '\0');
        // if over than 7 days
        else if (weeks > 0 && months == 0 && years == 0)
            return String.format("%d week%c ago", weeks, weeks > 1? 's' : '\0');
        // if over than 30 days
        else if (months > 0 && years == 0)
            return String.format("%d month%c ago", months, months > 1? 's' : '\0');
        // if over than 365 days
        else if (years > 0)
            return String.format("%d year%c ago", years, years > 1? 's' : '\0');
        // cannot calculate
        else
            return "long ago";
    }

    /**
     * for checking if same article exist in set of articles (by title)
     * @param obj article to be checked
     * @return true if same article already exist, false if not
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Article))
            return false;
        if (obj == this)
            return true;
        return this.getTitle().equals(((Article) obj).getTitle());
    }

    /**
     * for checking if same article exist in set of articles (by title) (hashcode)
     * @return hashcode of title
     */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((title == null) ? 0 : title.hashCode());
        return result;
    }


}