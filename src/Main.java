/**
 * Created by Shauny on 31-May-17.
 */
        import javafx.application.Application;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Scene;
        import javafx.scene.layout.BorderPane;
        import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("app.fxml"));

            BorderPane root = loader.load();

            Controller controller = loader.getController();
            controller.setStage(primaryStage);

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("HTTP Client");
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
