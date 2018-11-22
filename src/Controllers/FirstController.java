package Controllers;

import Classes.*;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class FirstController implements Initializable {
    private double xOffset = 0;
    private double yOffset = 0;
    private OnlineTimer onlineTimer;
    private Operator operator;

    @FXML
    private Node header;
    @FXML
    private Label timeLabel;
    @FXML
    private Label planesLabel;
    @FXML
    private Label totalTasksLabel;
    @FXML
    private Label coveredTasksLabel;
    @FXML
    private Label remainedTasksLabel;
    @FXML
    private Pane mainPanel;
    @FXML
    private JFXComboBox<String> backgroundCombobox;

    @FXML
    private void CloseWindow() {
        Platform.exit();
        System.exit(0);
    }
    @FXML
    private void MinimizeWindow() {
        Stage primaryStage = Main.getStage();
        primaryStage.setIconified(true);
    }

    @FXML
    private void PlayTimer() {
        onlineTimer.Play();
    }

    @FXML
    private void PauseTimer() {
        onlineTimer.Pause();
    }

    @FXML
    private void StopTimer() {
        onlineTimer.Stop();
    }

    // Add draggable property to stage
    private void InitDragWindow() {
        Stage primaryStage = Main.getStage();
        header.setOnMousePressed(event -> {
            primaryStage.setOpacity(0.8);
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        header.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
        header.setOnMouseReleased(event -> {
            primaryStage.setOpacity(1);
        });
    }

    public void SetBackground(String name) {
        String url = String.valueOf(getClass().getResource("../Resources/img/" + name + ".png"));
        mainPanel.setStyle("-fx-background-image: url(" + url + ")");
    }

    public void initializeBackgroundCombo() {
        File aDirectory = new File(getClass().getResource("../Resources/img").getPath());
        String[] filesInDir = aDirectory.list();
        for (int i = 0; i < filesInDir.length; i++) {
            filesInDir[i] = filesInDir[i].replace(".png", "");
        }
        ObservableList<String> options = FXCollections.observableArrayList(filesInDir);
        backgroundCombobox.setPromptText(filesInDir[0]);
        backgroundCombobox.setItems(options);

        backgroundCombobox.valueProperty().addListener((observable, oldValue, newValue) -> {
            SetBackground(newValue);
            backgroundCombobox.setStyle("-fx-padding: 0");
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InitDragWindow();
        SetBackground("Background_1");
        initializeBackgroundCombo();

        Platform.runLater(() -> {
            operator = new Operator(mainPanel, 120, mainPanel.getHeight() - 120, 100);
//            planesLabel.setText(String.format("%02d", planes.size()));
        });

        mainPanel.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                System.out.println("Left-Click");
                Task task = new Task(event.getX(), event.getY());
                mainPanel.getChildren().add(task.getTask());
                operator.addTask(task);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                System.out.println("Right-Click");
            }
        });

        // Bind values for details label
        planesLabel.textProperty().bindBidirectional(Details.getPlanesNumberProperty());
        totalTasksLabel.textProperty().bindBidirectional(Details.getTotalTasksNumberProperty());
        coveredTasksLabel.textProperty().bindBidirectional(Details.getCoveredTasksNumberProperty());
        remainedTasksLabel.textProperty().bindBidirectional(Details.getRemainedTasksNumberProperty());

        onlineTimer = new OnlineTimer(timeLabel);
        onlineTimer.run();
    }
}