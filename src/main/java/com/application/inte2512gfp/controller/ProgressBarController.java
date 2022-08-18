/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: News Scraper Application
  Author: Nguyen Nam Cuong (s3891758),Nguyen Ngoc Minh (s3907086), Phan Nhat Minh (s3904422), Tran Mai Nhung (s3879954), Nguyen Vu Thuy Duong (s3865443)
  Created  date: 05/17/2022
  Acknowledgement: learn progress bar tool - https://jenkov.com/tutorials/javafx/progressbar.html#:~:text=The%20JavaFX%20ProgressBar%20is%20a,scene.control.ProgressBar%20class.
                   learn to bind the progress bar tool -
 */
package com.application.inte2512gfp.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * ProgressBarController is the controller of the progress bar pane.
 *
 * @version 1.0
 */
public class ProgressBarController implements Initializable {

    @FXML
    public ProgressBar progressBar;
    @FXML
    public Label progressText;
    // define task progress property
    private MainController.FetchCategorySitesTask target;

    /**
     * constructor of the ProgressBarController
     *
     * @param target thread which the progress bar
     */
    public ProgressBarController(MainController.FetchCategorySitesTask target)
    {
        this.target = target;
    }

    /**
     * the method is used to bind the progress of the target thread to the progress bar progress
     *
     * @param location URL object
     * @param resources ResourceBundle object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // unbind the progress bar from the default progress prob
        progressBar.progressProperty().unbind();
        // bind the progress bar to the thread progress
        progressBar.progressProperty().bind(this.target.progressProperty());
        // unbind the text label to the default state
        progressText.textProperty().unbind();
        // bind label the target thread message property
        progressText.textProperty().bind(this.target.messageProperty());
    }
}
