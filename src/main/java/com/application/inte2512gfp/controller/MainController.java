/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: News Scraper Application
  Author: Nguyen Nam Cuong (s3891758),Nguyen Ngoc Minh (s3907086), Phan Nhat Minh (s3904422), Tran Mai Nhung (s3879954), Nguyen Vu Thuy Duong (s3865443)
  Created  date: 05/17/2022
  Acknowledgement: learn Collections sort - https://stackoverflow.com/questions/5932720/how-to-sort-an-attribute-of-an-object-using-collections
                   learn java thread and multi-threads - https://www.w3schools.com/java/java_threads.asp
                                                         https://www.tutorialspoint.com/java/java_thread_synchronization.htm
                                                         https://www.geeksforgeeks.org/synchronization-in-java/
                                                         https://stackoverflow.com/questions/13264726/java-syntax-synchronized-this
                                                         https://stackoverflow.com/questions/27244677/what-is-the-difference-between-thread-join-and-synchronized#:~:text=The%20synchronized%20keyword%20enables%20a,interference%20and%20memory%20consistency%20errors%22.
 */
package com.application.inte2512gfp.controller;

import com.application.inte2512gfp.entities.*;
import com.application.inte2512gfp.view.ArticleSummaryPane;
import com.application.inte2512gfp.view.PaginationPageView;
import com.application.inte2512gfp.view.ProgressBarView;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

/**
 * MainController is the controller of the main menu page.
 *
 * @version 1.0
 */
public class MainController implements Initializable {
    @FXML
    private Pagination mainPagingBar;

    @FXML
    private HBox categoriesHBox;

    @FXML
    private ScrollPane cateScrollPane;

    private Category currentCategorySystem;

    private HashMap<Category, Thread> categoryThreadMap;
    // contain 5 sites
    private NewsSites[] siteList;
    // contain 5 sites' article list
    private List<Article> displayedArticleList;

    private HashMap<Category, List<Article>> newestCategoryArticleList;

    /**
     * reload button function
     */
    @FXML
    private void reloadOnMouseClick() {
        fetchFromCategoryButton(currentCategorySystem.getEngName());
    }

    /**
     * The method is used to load data into the UI is displayed
     *
     * @param url            URL
     * @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // initialize the category thread list for category buttons click event
        categoryThreadMap = new HashMap<>();
        // initialize the article list is displayed for each category page load
        displayedArticleList = new ArrayList<>();
        newestCategoryArticleList = new HashMap<>();
        // initialize the site list
        siteList = new NewsSites[]{new VnExpress(), new NhanDan(), new ThanhNien(), new TuoiTre(), new ZingVN()};
        // fetch the Newest category first when the app is run
        fetchFromCategoryButton("Newest");
        // load and create the categories button
        createToggleButton();

    }

    /**
     * the method create toggle button for the categories
     */
    private void createToggleButton() {
        // Initialize the ToggleGroup for all categories button
        ToggleGroup toggleGroup = new ToggleGroup();
        Category cate[] = Category.values();
        // create all categories button
        for (int i = 0; i < cate.length; i++) {
            // set text for each button
            ToggleButton tb = new ToggleButton(cate[i].getEngName());
            // set style for each button
            tb.setStyle("-fx-base: #f18e8e; -fx-pref-width: 200px; -fx-pref-height: 100px");
            // set onClick action for each category button
            tb.setOnMouseClicked(event -> {
                // stop the button from reload if it is clicked twice
                if (Category.getCategoryFromText(tb.getText()) == currentCategorySystem)
                    return;
                fetchFromCategoryButton(tb.getText());
            });
            // set all categories button into the group
            tb.setToggleGroup(toggleGroup);
            // add buttons to the ui
            categoriesHBox.getChildren().add(tb);
        }
        // newest category button is selected first when the program initiate
        toggleGroup.getToggles().get(0).setSelected(true);
    }

    /**
     * the method is used to load the page of each category
     *
     * @param cate category page
     */
    private void setPagination(Category cate) {
        // implement event handler for the paging tool
        ScrollPane page1;
        ScrollPane page2;
        ScrollPane page3;
        ScrollPane page4;
        ScrollPane page5;

        // set sub pages for each category page
        page1 = getPageForPagination(cate, 1);
        page2 = getPageForPagination(cate, 2);
        page3 = getPageForPagination(cate, 3);
        page4 = getPageForPagination(cate, 4);
        page5 = getPageForPagination(cate, 5);

        // changing page function
        mainPagingBar.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer index) {
                switch (index) {
                    case 0:
                        return page1;
                    case 1:
                        return page2;
                    case 2:
                        return page3;
                    case 3:
                        return page4;
                    case 4:
                        return page5;
                }
                return null;
            }
        });

    }

    /**
     * the method is used to set layout for each page in the pagination
     *
     * @param cate Category object
     * @param page page number
     * @return ScrollPane ScrollPane object which contained article summary panes
     */
    private ScrollPane getPageForPagination(Category cate, int page) {
        // each page has 10 articles
        int from = (page * 10) - 10;
        int to = page * 10;
        // initialize the grid column and row
        int column = 0;
        int row = 1;
        // validating category is updated later in here
        PaginationPageView pPage;
        PaginationPageController pageController;
        GridPane gridPane = new GridPane();
        List<Article> articleList = displayedArticleList;
        try {
            // load the article to the page grid pane
            for (int i = from; i < to; i++) {
                try {
                    Article article = articleList.get(i);
                    ArticleSummaryController ASPController = new ArticleSummaryController(article);
                    ArticleSummaryPane asp = new ArticleSummaryPane(ASPController);
                    // each line has 3 articles
                    if (column == 3) {
                        column = 0;
                        row++;
                    }

                    gridPane.add(asp.getArticleSummaryPane(), column++, row); //(child,column,row)

                    GridPane.setMargin(asp.getArticleSummaryPane(), new Insets(10));

                } catch (IndexOutOfBoundsException e) {
                    continue;
                }
            }
            pageController = new PaginationPageController(gridPane);
            pPage = new PaginationPageView(pageController);
            return pPage.getPaginationPane();

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * this method is used to initialize the progress bar
     *
     * @param fetchCategorySitesTask
     */
    public void progressBarAction(FetchCategorySitesTask fetchCategorySitesTask) {
        // progress bar
        ProgressBarController pbc = new ProgressBarController(fetchCategorySitesTask);
        ProgressBarView pbv = new ProgressBarView(pbc);
        Pane pane = pbv.getProgressBar();
        mainPagingBar.setPageFactory(index -> {
            return pane;
        });
    }



    /**
     * when category button is pressed -> start fetching articles of that category
     * @param buttonCategoryText category name
     */
    public void fetchFromCategoryButton(String buttonCategoryText) {
        // get current category of the button
        Category currentCategoryButton = Category.getCategoryFromText(buttonCategoryText);
        // assign that category to the current category of system
        currentCategorySystem = currentCategoryButton;
        if (currentCategoryButton != null) { // for null pointer exception
            // check if user already clicked this category button
            // and if the app has done fetching data
            if (categoryThreadMap != null) { // has clicked category button before
                // check if the thread is still running
                Thread currentCategoryThread = categoryThreadMap.get(currentCategoryButton);
                if (currentCategoryThread != null && currentCategoryThread.isAlive())
                    return; // do nth
            }

            // if not fetching and not being clicked before
            // start a new task
            FetchCategorySitesTask fetchCategorySitesTask = new FetchCategorySitesTask(currentCategoryButton);
            //  put task into thread to fetch data
            Thread fetchCategorySitesThread = new Thread(fetchCategorySitesTask);

            // put the thread into map
            categoryThreadMap.put(currentCategoryButton, fetchCategorySitesThread);

            // add succeed event handler,  if done thread
            fetchCategorySitesTask.setOnSucceeded(event -> {
                // if the task is finished but not the task of the current category button
                if (!currentCategoryButton.equals(currentCategorySystem)) {
                    // and that category is not Newest
                    return;
                }
                // if the task is finished and the task of the current category button
                // unmodified set
                List<Article> set = new ArrayList<>();
                try {
                    // get the set of articles fetched from all sites of that category
                    set = List.copyOf(((FetchCategorySitesTask) event.getSource()).getValue());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                //convert to modified set for sorting
                displayedArticleList = new ArrayList<>(set);
                // sort from Newest to Oldest by date
                try {
                    displayedArticleList.sort(Comparator.comparing(Article::getPublishDuration));
                }
                catch (Exception e) {
                    System.out.println("sort " + e.getMessage());
                }

                // display by pagination
                setPagination(currentCategoryButton);
            });

            // start thread
            fetchCategorySitesThread.start();
            //start the progress bar
            progressBarAction(fetchCategorySitesTask);
        }
    }
    // class for creating task for fetching category sites
    public class FetchCategorySitesTask extends Task<Set<Article>> {
        // because Task has event listener, so we can use it to set event handler
        private Category category;

        /**
         * @param category
         */
        FetchCategorySitesTask(Category category) {
            this.category = category;
        }

        /**
         * call function for thread
         * @return set of articles of all sites
         */
        @Override
        public Set<Article> call() {
            // multiple threads can fetch data on multiple sites at the same time
            Set<Article> articleList = Collections.synchronizedSet(new HashSet<>());
            try {
                // run all sites
                ArrayList<Thread> eachSiteThreads = new ArrayList<>();
                for (NewsSites site : siteList) {
                    // create new task
                    FetchArticlesTask fetchArticlesTask = new FetchArticlesTask(site, category);
                    // thread for each site
                    Thread fetchArticlesThread = new Thread(fetchArticlesTask);

                    // succeed event handler for each site
                    fetchArticlesTask.setOnSucceeded(event -> {
                        // add articles to list of articles, make sure it is synchronized
                        synchronized (articleList) {
                            // initialize list of articles from newest of all categories
                            ArrayList<Article> newestCategoryArticleList =  site.getNewestCategoryArticleMap().get(category);
                            // if newest is still fetching, wait for it to finish
                            if (category != Category.NEWEST && newestCategoryArticleList != null && !newestCategoryArticleList.isEmpty()) {
                                Thread newestThread;
                                while ((newestThread = categoryThreadMap.get(Category.NEWEST)).isAlive()) {
//                                    try {
//                                        newestThread.join();
//                                    } catch (InterruptedException e) {
//                                        System.out.println("Cannot wait for newest thread");
//                                    }
                                    try {
                                        wait(0);
                                    }
                                    catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }

                                }
                                // add newest articles to list of articles
                                articleList.addAll(site.getNewestCategoryArticleMap().get(category));
                                // add remaining articles fetched from current category to list of articles
                                int i = 0, size = newestCategoryArticleList.size();
                                for (Article articleFetched : ((FetchArticlesTask) event.getSource()).getValue()) {
                                    if (i < (10 - size) && articleList.add(articleFetched)) {
                                        i ++;
                                    }
                                    else break;
                                }
                            }
                            else
                                // just add all articles to list of articles from current category
                                articleList.addAll(((FetchArticlesTask) event.getSource()).getValue());
                        }
                    });

                    // start running thread
                    fetchArticlesThread.start();
                    // add to list
                    eachSiteThreads.add(fetchArticlesThread);
                }
                // wait for threads to finish one by one to avoid conflict when access to the same articleList

                int i = 0;
                for (Thread eachSiteThread : eachSiteThreads) {
                    // update state for progress bar
                    i++;
                    this.updateProgress(i, siteList.length);
                    float percent = (float) i / siteList.length * 100;
                    this.updateMessage(new DecimalFormat("##").format(percent) + "%");
                    // only one thread can access to the same articleList
                    synchronized (this) {
                        // wait for each thread to finish
                        eachSiteThread.join();
                    }
                    Thread.sleep(20);
                }

            } catch (Exception ex) {
                System.out.println("Error when fetching articles from all sites with special category: " + category.getEngName());
            }
            return articleList;
        }
    }

    /**
     * The method is used to change the scroll direction of the scroll categories bar from vertical to horizontal.
     * @param se the scroll event
     */
    public void categoriesOnScrollH(ScrollEvent se) {
        // scroll left if roll upward
        if (se.getDeltaY() > 0) {
            cateScrollPane.setHvalue(cateScrollPane.getHvalue() - 0.5F);
        } else {
            // scroll right if roll backward
            cateScrollPane.setHvalue(cateScrollPane.getHvalue() + 0.5F);
        }
    }

    static class FetchArticlesTask extends Task<List<Article>> {
        private final Category currentCategory;
        private final NewsSites newsSite;

        /**
         * @param newsSite
         * @param currentCategory
         */
        FetchArticlesTask(NewsSites newsSite, Category currentCategory) {
            this.newsSite = newsSite;
            this.currentCategory = currentCategory;
        }

        /**
         * Get the list of articles from the current category of the current site
         * @return list of articles
         */
        @Override
        public ArrayList<Article> call() {
            ArrayList<Article> articleList = new ArrayList<>();
            try {
                articleList = newsSite.fetchOneType(currentCategory);
            } catch (Exception e) {
                System.out.println("Error when fetching article from specific site: "
                        + newsSite.getWebsiteURL());
            }
            return articleList;
        }
    }
}