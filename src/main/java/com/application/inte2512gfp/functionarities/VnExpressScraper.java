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
 * VnExpress articles content scraper
 *
 * @version 1.0
 */
public class VnExpressScraper extends ContentScraper{
    /**
     * constructor
     *
     * @param url String article's content url
     * @throws Exception any exceptions
     */
    public VnExpressScraper(String url) throws Exception {
        super(url);

        // fetch article content
        fetchArticleContent();

        // initialize useless classes
        this.uselessClasses = new String[]{
                "section top-header",
                "parent",
                "social_left",
                "sidebar-2",
                "popup-zoom",
                "footer-content  width_common",
                "section page-detail middle-detail",
                "banner-ads", "section page-detail bottom-detail"};

        // initialize useless ids
        this.uselessIds = new String[]{"to_top", "video"};

        // initialize useless tags
        this.uselessTags = new String[] {"header", "iframe", "footer", "form", "svg"};
    }
}
