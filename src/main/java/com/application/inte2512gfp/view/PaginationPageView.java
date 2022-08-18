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
import com.application.inte2512gfp.controller.PaginationPageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;


import java.io.IOException;

/**
 * PaginationPageView class
 *
 * @version 1.0
 */
public class PaginationPageView {

    // define
    private FXMLLoader fxmlLoader;
    private ScrollPane scrollPane;

    /**
     * constructor
     *
     * @param controller PaginationPageController
     */
    public PaginationPageView(PaginationPageController controller)
    {
        try {
            fxmlLoader = new FXMLLoader(Main.class.getResource("pagination-page.fxml"));
            fxmlLoader.setControllerFactory(c->{return controller;});
            scrollPane = fxmlLoader.load();
            scrollPane.setStyle("-fx-border-color: #ffffff; -fx-border-width: 2;");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get paginationPane
     *
     * @return ScrollPane scroll pane
     */
    public ScrollPane getPaginationPane () {
        return scrollPane;
    }
}
