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
import com.application.inte2512gfp.controller.ArticleSummaryController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * ArticleSummaryPane class
 *
 * @version 1.0
 */
public class ArticleSummaryPane {

    // define FXMLLoader
    private FXMLLoader fxmlLoader;
    private AnchorPane anchorPane;

    /**
     * constructor
     *
     * @param controller ArticleSummaryController
     * @throws IOException null file exception
     */
    public ArticleSummaryPane(ArticleSummaryController controller) throws IOException {
        // load resource from fxml file
        fxmlLoader = new FXMLLoader(Main.class.getResource("article-summary.fxml"));
        // set the controller for the view
        fxmlLoader.setControllerFactory(c -> {
            return controller;
        });
        anchorPane = fxmlLoader.load();
    }

    /**
     * get article summary pane
     *
     * @return AnchorPane article summary Pane
     */
    public AnchorPane getArticleSummaryPane() {
        return anchorPane;
    }

    /**
     * get the controller
     *
     * @return controller ArticleSummaryController
     */
    public ArticleSummaryController getController() {
        return this.fxmlLoader.getController();
    }

}