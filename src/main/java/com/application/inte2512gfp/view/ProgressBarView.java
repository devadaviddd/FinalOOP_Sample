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
import com.application.inte2512gfp.controller.ProgressBarController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

/**
 * ProgressBarView class
 *
 * @version 1.0
 */
public class ProgressBarView {

    private Pane progressBar;

    /**
     * constructor
     *
     * @param pcb ProgressBarController
     */
    public ProgressBarView(ProgressBarController pcb) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("progress-indicator-bar.fxml"));
            fxmlLoader.setControllerFactory(c -> {
                return pcb;
            });
            progressBar = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get progress bar
     *
     * @return progres bar
     */
    public Pane getProgressBar() {
        return this.progressBar;
    }
}
