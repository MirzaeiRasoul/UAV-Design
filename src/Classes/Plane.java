package Classes;

import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Plane {

    private double x;
    private double y;
    private double radius;
    private double speed;
    private String color;
    private int communicationRange;
    private double batteryCapacity;
    private double innerX;
    private double innerY;
    private BooleanProperty inRange;
    private boolean inMoving;
    private ObservableList<Task> tasks;
    private List<Line> paths;
    private TranslateTransition backTransition;

    private Pane mainPanel;
    private Circle plane;

    public Plane(Pane mainPanel, double x, double y, String color) {
        this.mainPanel = mainPanel;
        this.x = x;
        this.y = y;
        this.color = color;
        initDefaultValues();
        initPlane();
        initTasksListener();
    }

    private void initDefaultValues() {
        radius = 10;
        speed = 100;
        innerX = x;
        innerY = y;
        inRange = new SimpleBooleanProperty(true);
        inMoving = false;
    }

    private void initPlane() {
        plane = new Circle(x, y, radius);
        plane.setFill(Color.valueOf(color));
        mainPanel.getChildren().add(plane);
    }

    // Add listener to tasks list for routing
    private void initTasksListener() {
        paths = new ArrayList<>();
        tasks = FXCollections.observableArrayList();
        tasks.addListener((ListChangeListener<? super Task>) c -> {
            c.next();
            if (c.wasAdded()) {
                Line path;
                if (tasks.size() == 1) {
                    path = new Line(innerX, innerY, tasks.get(0).getTask().getCenterX(), tasks.get(0).getTask().getCenterY());
                } else {
                    Task task = c.getAddedSubList().get(0);
                    Task prevTask = tasks.get(tasks.indexOf(task) - 1);
                    path = new Line(prevTask.getTask().getCenterX(), prevTask.getTask().getCenterY(),
                            task.getTask().getCenterX(), task.getTask().getCenterY());
                }
                path.setStroke(Color.valueOf(color));
                mainPanel.getChildren().add(path);
                paths.add(path);
            } else {
                // Remove path of removed task
                Line removedPath = paths.get(0);
                mainPanel.getChildren().remove(removedPath);
                paths.remove(removedPath);
            }
        });
    }

    // Run plane in sequential mode
    public void sequentialMovement() {
        // Set inMoving flag for prevent duplicate call
        inMoving = true;
        // Get first task
        Task currentTask = tasks.get(0);
        // Calculate distance
        double distance = Utils.calculateDistance(currentTask.getTask().getCenterX(), currentTask.getTask().getCenterY(), innerX, innerY);
        // Resolve problem with very short distance
        distance = distance > 1 ? distance : 1;
        // Start plane transition to task
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(distance * 1000 / speed), plane);
        translateTransition.setToX(currentTask.getTask().getCenterX() - x);
        translateTransition.setToY(currentTask.getTask().getCenterY() - y);
        translateTransition.play();

        Line path = paths.get(0);
        // Add listener to plane translate value
        plane.translateXProperty().addListener((observable, oldValue, newValue) -> {
            // Update position of plane
            innerX = x + plane.translateXProperty().getValue();
            innerY = y + plane.translateYProperty().getValue();
            // Calculate online distance
            double onlineDistance = Utils.calculateDistance(120, 446, innerX, innerY);
            if (onlineDistance > 100 && inRange.getValue()) {
                inRange.setValue(false);
            }
            // Update path from plane position
            path.setStartX(innerX);
            path.setStartY(innerY);
        });

        translateTransition.setOnFinished(event -> {
            // System.out.println(innerX + "  --- " + innerY);
            mainPanel.getChildren().remove(currentTask.getTask());
            removeTask(currentTask);
            Details.setCoveredTasksNumber(Integer.parseInt(Details.getCoveredTasksNumberProperty().getValue()) + 1);
            Details.setRemainedTasksNumber(Integer.parseInt(Details.getTotalTasksNumberProperty().getValue()) -
                    Integer.parseInt(Details.getCoveredTasksNumberProperty().getValue()));
            // Check tasks list & position status for select next action
            if (tasks.isEmpty()) {
                if (!inRange.getValue()) {
                    backToOperator();
                } else {
                    inMoving = false;
                }
            } else {
                sequentialMovement();
            }
        });
    }

    // Back to operator range
    public void backToOperator() {
        // Calculate distance to center point of operator
        double distance = Utils.calculateDistance(120, 446, innerX, innerY);
        // Start plane transition to operator
        backTransition = new TranslateTransition(Duration.millis(distance * 1000 / speed), plane);
        backTransition.setToX(120 - x);
        backTransition.setToY(446 - y);
        backTransition.play();
        // Add listener for update plane position
        plane.translateXProperty().addListener((observable, oldValue, newValue) -> {
            // Calculate online distance
            double onlineDistance = Utils.calculateDistance(120, 446, innerX, innerY);
            if (onlineDistance < 100 && inRange.getValue() == false) {
                if (tasks.isEmpty()) {
                    inMoving = false;
                }
                backTransition.pause();
                inRange.setValue(true);
            }
        });
//        // Set inMoving flag for allow plane to move again
//        backTransition.setOnFinished(event -> {
//            inMoving = false;
//        });
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public Circle getPlane() {
        return plane;
    }

    public double getInnerX() {
        return innerX;
    }

    public double getInnerY() {
        return innerY;
    }

    public BooleanProperty isInRange() {
        return inRange;
    }

    public boolean isInMoving() {
        return inMoving;
    }
}