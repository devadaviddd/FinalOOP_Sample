/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: News Scraper Application
  Author: Nguyen Nam Cuong (s3891758),Nguyen Ngoc Minh (s3907086), Phan Nhat Minh (s3904422), Tran Mai Nhung (s3879954), Nguyen Vu Thuy Duong (s3865443)
  Created  date: 05/17/2022
  Acknowledgement: learn pagination tool - https://jenkov.com/tutorials/javafx/pagination.html
 */
package com.application.inte2512gfp.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * the class is the controller for the paging system
 *
 * @author Cuong Nguyen
 * @version 1.0
 */
public class PaginationPageController implements Initializable {

    @FXML
    public GridPane mainGridPane;

    @FXML
    public ScrollPane scrollPane;

    private GridPane gp;

    /**
     * instructor of PaginationPageController class
     *
     * @param gridPane GridPane object which is the layout of articles' summary on each page
     */
    public PaginationPageController(GridPane gridPane) {
        // set the grid pane
        this.gp = gridPane;
    }

    /**
     * the method is used to load the articles' summary onto the ui before it is displayed
     *
     * @param location  URL object
     * @param resources ResourceBundle object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scrollPane.setContent(gp);
    }
}
