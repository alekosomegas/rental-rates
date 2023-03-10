module com.ak.rentalrates {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires htmlunit;

    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.chrome_driver;
    requires org.seleniumhq.selenium.support;

    opens com.ak.rentalrates to javafx.fxml;
    exports com.ak.rentalrates;
    exports com.ak.rentalrates.WebSites;
    opens com.ak.rentalrates.WebSites to javafx.fxml;
}