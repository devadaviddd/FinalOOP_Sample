module com.application.inte2512gfp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;
    requires javafx.web;

    opens com.application.inte2512gfp to javafx.fxml;
    opens com.application.inte2512gfp.entities to javafx.fxml;
    exports com.application.inte2512gfp;
    exports com.application.inte2512gfp.controller;
    exports com.application.inte2512gfp.entities;
    opens com.application.inte2512gfp.controller to javafx.graphics, javafx.fxml;
    exports com.application.inte2512gfp.view;
    opens com.application.inte2512gfp.view to javafx.fxml;
}