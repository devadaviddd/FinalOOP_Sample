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
 * Tuoi Tre articles content scraper
 *
 * @version 1.0
 */
public class TuoiTreScraper extends ContentScraper{
    /**
     * constructor
     *
     * @param url String article's content url
     * @throws Exception any exceptions
     */
    public TuoiTreScraper(String url) throws Exception {

        super(url);

        // fetch the article content
        fetchArticleContent();

        // initialized useless tags array
        this.uselessTags = new String[]{"iframe","footer","header", "form"};

        // initialized useless classes array
        this.uselessClasses = new String[]{"network networktop fl is-affixed",
                "slidebar fr", "btn-backtotop",
                "inner-wrapper-sticky","boxNewsHot type2",
                "box_xem_nhieu_detail_column23"};

        // initialized useless Ids array
        this.uselessIds = new String[]{"adsWeb_AdsBottom","rating","content-bottom-detail","sticky-box","tagandnetwork"};
    }
}
