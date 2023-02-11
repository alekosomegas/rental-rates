module com.ak.rentalrates {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ak.rentalrates to javafx.fxml;
    exports com.ak.rentalrates;
}