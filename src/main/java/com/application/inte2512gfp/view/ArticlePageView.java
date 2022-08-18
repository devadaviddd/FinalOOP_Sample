/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: News Scraper Application
  Author: Nguyen Nam Cuong (s3891758),Nguyen Ngoc Minh (s3907086), Phan Nhat Minh (s3904422), Tran Mai Nhung (s3879954), Nguyen Vu Thuy Duong (s3865443)
  Created  date: 05/17/2022
  Acknowledgement:
 */
package com.application.inte2512gfp.view;

import com.application.inte2512gfp.Main;
import com.application.inte2512gfp.controller.ArticlePageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * ArticlePageView class
 *
 * @version 1.0
 */
public class ArticlePageView {
    // define FXMLLoader
    private FXMLLoader fxmlLoader;
    private Parent root = null;

    /**
     * constructor
     *
     * @param controller ArticlePageController
     * @throws IOException null file exception
     */
    public ArticlePageView(ArticlePageController controller) throws IOException {
        // load resource from fxml file
        fxmlLoader = new FXMLLoader(Main.class.getResource("article-page.fxml"));
        // set the controller for the view
        fxmlLoader.setControllerFactory( c -> {return controller;});
        root = fxmlLoader.load();
    }

    /**
     * getter
     *
     * @return Parent ArticlePageView
     */
    public Parent getArticlePage () {
        return root;
    }

    /**
     * getter
     *
     * @return controller
     */
    public ArticlePageController getController()
    {
        return this.fxmlLoader.getController();
    }
}
