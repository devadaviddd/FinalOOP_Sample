/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: News Scraper Application
  Author: Nguyen Nam Cuong (s3891758),Nguyen Ngoc Minh (s3907086), Phan Nhat Minh (s3904422), Tran Mai Nhung (s3879954), Nguyen Vu Thuy Duong (s3865443)
  Created  date: 05/17/2022
  Acknowledgement:
 */
package com.application.inte2512gfp.functionarities;
/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: News Scraper Application
  Author: Nguyen Nam Cuong (s3891758),Nguyen Ngoc Minh (s3907086), Phan Nhat Minh (s3904422), Tran Mai Nhung (s3879954), Nguyen Vu Thuy Duong (s3865443)
  Created  date: 05/17/2022
  Acknowledgement: canvas

 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * the parent class of all content scraper
 *
 * @version 1.0
 */
public abstract class ContentScraper {

    protected String articleURL;
    // define tag with these classes will be removed
    protected String[] uselessClasses;
    // define html tags must be removed
    protected String[] uselessTags;
    // define the tag has these ids will be removed
    protected String[] uselessIds;
    // define clean article content
    protected Document cleanContent;

    /**
     * constructor
     *
     * @param url String url of the article's content
     */
    public ContentScraper(String url) {
        articleURL = url;
    }

    /**
     * this method is used to fetch the article content as html format
     *
     * @throws Exception any exception
     */
    protected void fetchArticleContent() throws Exception {
        // get raw content by sending the GET request to the article content server
        String rawContent = Jsoup.connect(articleURL).get().toString();
        // initialize the clean content
        cleanContent = Jsoup.parse(rawContent);
    }

    /**
     * this method is used to disable all hyperlinks in the article content
     */
    protected void disableHyperlink() {
        // get all Elements with the attribute href
        Elements hyperLinks = cleanContent.getElementsByAttribute("href");
        // add the disabled class which will prevent mouse click action to all the hyperlink
        for (Element e : hyperLinks) {
            e.addClass("disabled");
        }
    }

    /**
     * This method is used the scrape the raw document
     *
     * @return Document clean content after scrape useless html part and disable the hyperlink
     */
    public Document scrape() {
        // define i to 0
        int i = 0;
        //  loop until all part in the uselessTags array, uselessClasses array and uselessIds array are removed
        while (i < this.uselessTags.length || i < this.uselessClasses.length || i < this.uselessIds.length) {

            // removed the useless tags
            if (i < this.uselessTags.length) {
                try {
                    cleanContent.getElementsByTag(this.uselessTags[i]).remove();
                } catch (Exception e) {

                }
            }

            //remove the useless class
            if (i < this.uselessClasses.length) {
                try {
                    cleanContent.getElementsByClass(this.uselessClasses[i]).remove();
                } catch (Exception e) {
                }
            }

            //remove the useless id
            if (i < this.uselessIds.length) {
                try {
                    cleanContent.getElementsByAttributeValueMatching("id", this.uselessIds[i]).remove();
                } catch (Exception e) {
                }
            }
            i++;
        }

        // disable the hyperlink
        disableHyperlink();
        return cleanContent;
    }
}
