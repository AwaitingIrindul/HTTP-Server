/**
 * Created by Shauny on 31-May-17.
 */

import java.io.IOException;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller extends VBox {
    private Stage stage;
    @FXML public Button search;
    @FXML public TextField IP;
    @FXML public TextField Port;
    @FXML public TextField URL;

    public Controller() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


//    public String getText() {
//        return textProperty().get();
//    }
//
//    public void setText(String value) {
//        textProperty().set(value);
//    }

//    public StringProperty textProperty() {
//        return textField.textProperty();
//    }

    @FXML
    protected void search() {
        if(IP.getText().length()>0 && Port.getText().length()>0 && URL.getText().length()>0)
        {
            // APPELER LA FCT RUN()
        }
    }

}
