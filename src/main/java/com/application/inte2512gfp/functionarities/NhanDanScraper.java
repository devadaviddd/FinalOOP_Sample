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

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Nhan Dan articles content scraper
 *
 * @version 1.0
 */
public class NhanDanScraper extends ContentScraper {
    /**
     * constructor
     *
     * @param url String article's content url
     * @throws Exception any exceptions
     */
    public NhanDanScraper(String url) throws Exception {
        super(url);

        // fetch the article content
        fetchArticleContent();

        // initialized useless tags array
        this.uselessTags = new String[]{"iframe", "svg"};

        // initialized useless classes array
        this.uselessClasses = new String[]{"navbar main-menu", "uk-subnav uk-subnav-pill box-header",
                "top-bar",
                "footersite", "sub-navbar", "uk-nav uk-nav-default", "box-widget"};

        // initialized useless ids array
        this.uselessIds = new String[]{"my-modal"};

        // change the related css link of Nhan Dan article to absolute css link
        directLinkToCSS();
    }

    /**
     * this method will replace related css link to absolute css link
     */
    private void directLinkToCSS() {
        // get style element
        Elements cssConnects = cleanContent.getElementsByAttributeValueMatching("rel", "stylesheet");
        // change the absolute css link
        for (Element e : cssConnects) {
            e.attr("href", "https://nhandan.vn" + e.attr("href"));
        }
    }
}

