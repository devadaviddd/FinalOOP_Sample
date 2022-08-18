/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: News Scraper Application
  Author: Nguyen Nam Cuong (s3891758),Nguyen Ngoc Minh (s3907086), Phan Nhat Minh (s3904422), Tran Mai Nhung (s3879954), Nguyen Vu Thuy Duong (s3865443)
  Created  date: 05/17/2022
  Acknowledgement: canvas
 */
package com.application.inte2512gfp.functionarities;

/**
 * Zing articles content scraper
 *
 * @version 1.0
 */
public class ZingScraper extends ContentScraper{

    /**
     * constructor
     *
     * @param url String article's content url
     * @throws Exception any exceptions
     */
    public ZingScraper(String url) throws Exception {
        super(url);
        // fetch article content
        fetchArticleContent();

        // initialize useless classes
        this.uselessClasses = new String[]{"section has-sidebar", "section recommendation has-sidebar", "section-title", "the-article-tags"};

        // initialize useless ids
        this.uselessIds = new String[]{"pushed_popup","mostview-articles"};

        // initialize useless tags
        this.uselessTags = new String[] { "footer", "svg"};
    }
}
