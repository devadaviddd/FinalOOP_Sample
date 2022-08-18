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
 * Thanh Nien articles content scraper
 *
 * @version 1.0
 */
public class ThanhNienScraper extends ContentScraper {
    /**
     * constructor
     *
     * @param url String article's content url
     * @throws Exception any exceptions
     */
    public ThanhNienScraper(String url) throws Exception {
        super(url);
        // fetch the article content
        fetchArticleContent();

        // initialized useless tags array
        this.uselessTags = new String[]{"iframe","footer","header", "form"};

        // initialized useless classes array
        this.uselessClasses = new String[]{"zone__content",
                "ThanhNienPlayer-overlay clear", "control-bar",
                "ThanhNienPlayer-btn-mute", "ai-chat-box-voice-recognition-status-indicator",
                "site-header__tool left btn-group","details__meta","breadcrumbs",
                "site-header__tool right btn-group", "floating-bar float-sticky", "morenews","content-detail-middle"};

        // initialized useless ids array
        this.uselessIds = new String[]{"adsWeb_AdsBottom","rating"};
    }

}
