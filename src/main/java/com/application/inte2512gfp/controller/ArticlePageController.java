/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: News Scraper Application
  Author: Nguyen Nam Cuong (s3891758),Nguyen Ngoc Minh (s3907086), Phan Nhat Minh (s3904422), Tran Mai Nhung (s3879954), Nguyen Vu Thuy Duong (s3865443)
  Created  date: 05/17/2022
  Acknowledgement: learn javafx webview - https://openplanning.net/11151/javafx-webview
 */
package com.application.inte2512gfp.controller;

import com.application.inte2512gfp.Main;
import com.application.inte2512gfp.functionarities.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * this class is the controller of the ArticlePageView object
 *
 * @version 1.0
 */
public class ArticlePageController implements Initializable {

    @FXML
    WebView webView;

    @FXML
    Button backToMainButton;

    private Scene mainScene;
    private String articleURL;
    private WebEngine engine;

    /**
     * this is the constructor of the ArticlePageController
     *
     * @param articleURL String url of the article's main content
     * @param mainScene  Scene object which is the main scene
     */
    public ArticlePageController(String articleURL, Scene mainScene) {
        this.mainScene = mainScene;
        this.articleURL = articleURL;
    }

    /**
     * this method is used to load the artilce's content into the ui before the ui is displayed
     *
     * @param location  URL object
     * @param resources ResourceBundle object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // define webView engine
        engine = webView.getEngine();
        //get the style file which will disable all of the hyperlink
        engine.setUserStyleSheetLocation(Objects.requireNonNull(Main.class.getResource("styles/href-disabled.css")).toString());
        // scrape article based on it publisher
        try {
            if (articleURL.contains("vnexpress")) {
                loadVnExpressArticle();
            } else if (articleURL.contains("nhandan")) {
                loadNhanDanArticle();
            } else if (articleURL.contains("thanhnien")) {
                loadThanhNienArticle();
            } else if (articleURL.contains("tuoitre")) {
                loadTuoiTreArticle();
            } else if (articleURL.contains("zing")) {
                loadZingArticle();
            } else {
                loadErrorPage();
            }
        } catch (Exception e) {
            System.out.println(e);
            // load error page if the url is invalid
            loadErrorPage();
        }
    }

    /**
     * this method is used to load the 404 error page into the webview
     */
    private void loadErrorPage() {
        engine.loadContent("<h1>Error 404: Article not found</h1><h2>Please back to main page.</h2>");
    }

    /**
     * load article's content from Thanh Nien publisher
     *
     * @throws Exception any exception
     */
    private void loadThanhNienArticle() throws Exception {
        engine.loadContent(new ThanhNienScraper(articleURL).scrape().toString());

    }

    /**
     * load article's content from Tuoi Tre publisher
     *
     * @throws Exception any exception
     */
    private void loadTuoiTreArticle() throws Exception {
        engine.loadContent(new TuoiTreScraper(articleURL).scrape().toString());
    }

    /**
     * load article's content from Nhan Dan publisher
     *
     * @throws Exception any exception
     */
    private void loadNhanDanArticle() throws Exception {
        engine.loadContent(new NhanDanScraper(articleURL).scrape().toString());
    }

    /**
     * load article's content from VNExpress publisher
     *
     * @throws Exception any exception
     */
    private void loadVnExpressArticle() throws Exception {
        //return clean content
        engine.loadContent(new VnExpressScraper(articleURL).scrape().toString());
    }

    /**
     * load article's content from Zing publisher
     *
     * @throws Exception any exception
     */
    private void loadZingArticle() throws Exception {
        engine.loadContent(new ZingScraper(articleURL).scrape().toString());
    }

    /**
     * this method is called when the "back to main" button is clicked, it re-direct the user to main menu
     */
    public void backToMainHandler() {
        Stage window = (Stage) backToMainButton.getScene().getWindow();
        // switch scene to main menu
        window.setScene(mainScene);
        // set main menu window unresizable
        window.setResizable(false);
        window.show();
    }
}
