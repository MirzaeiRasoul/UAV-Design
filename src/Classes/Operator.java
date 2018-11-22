package Classes;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Operator {

    private double x;
    private double y;
    private double range;
    private Color color;
    private double centerPointRadius;
    private List<Plane> planes;
    private ObservableList<Task> tasks;

    private Pane mainPanel;
    private Circle centerPoint;
    private Circle operatorRange;
    private Group operator;

    public Operator(Pane mainPanel, double x, double y, double range) {
        this.mainPanel = mainPanel;
        this.x = x;
        this.y = y;
        this.range = range;
        initDefaultValues();
        initOperator();
        initPlanes();
        initPlanesIsInRangeListener();
        initTasksListener();
    }

    private void initDefaultValues() {
        color = Color.RED;
        centerPointRadius = 5;
    }

    private void initOperator() {
        System.out.println(x + " --- " + y);
        operator = new Group();
        operatorRange = new Circle(x, y, range);
        operatorRange.getStyleClass().add("operatorRange");
        centerPoint = new Circle(x, y, centerPointRadius, color);
        operator.getChildren().addAll(centerPoint, operatorRange);
        mainPanel.getChildren().add(operator);
    }

    private Plane getNearestPlane(Task task) {
        double minDistance = 100000;
        Plane nearestPlane = null;
        for (Plane plane : planes) {
            double distance = Utils.calculateDistance(task.getTask().getCenterX(), task.getTask().getCenterY(), plane.getInnerX(), plane.getInnerY());
            if (distance < minDistance && plane.isInRange().getValue()) {
                minDistance = distance;
                nearestPlane = plane;
            }
        }
        return nearestPlane;
    }

    public void initPlanes() {
        planes = new ArrayList<>();
        try {
            Scenario scenario = new Scenario(getClass().getResource("../Resources/test-scenario.json").getPath());
            List<JSONObject> planesList = scenario.getPlanesList();
            for (JSONObject planeObject : planesList) {
                Plane plane = new Plane(mainPanel, Integer.valueOf(planeObject.get("x").toString()),
                        Integer.valueOf(planeObject.get("y").toString()), planeObject.get("color").toString());
                planes.add(plane);
            }
            Details.setPlanesNumber(planes.size());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void initPlanesIsInRangeListener() {
        for (Plane plane : planes) {
            plane.isInRange().addListener((observable, oldValue, newValue) -> {
                if (!tasks.isEmpty()) {
                    for (Task task : tasks) {
                        plane.addTask(task);
                    }
                    if (!plane.isInMoving()) {
                        plane.sequentialMovement();
                    }
                    tasks.clear();
                }
            });
        }
    }

    private void initTasksListener() {
        tasks = FXCollections.observableArrayList();
        tasks.addListener((ListChangeListener<? super Task>) c -> {
            c.next();
            if (c.wasAdded()) {
                Details.setTotalTasksNumber(Integer.parseInt(Details.getTotalTasksNumberProperty().getValue()) + 1);
                Details.setRemainedTasksNumber(Integer.parseInt(Details.getTotalTasksNumberProperty().getValue()) -
                        Integer.parseInt(Details.getCoveredTasksNumberProperty().getValue()));
                Task task = c.getAddedSubList().get(0);
                Plane nearestPlane = getNearestPlane(task);
                if (nearestPlane != null) {
                    tasks.remove(task);
                    nearestPlane.addTask(task);
                    if (!nearestPlane.isInMoving()) {
                        nearestPlane.sequentialMovement();
                    }
                }
            } else {

            }
        });
    }

    public void addTask(Task task) {
        tasks.add(task);
    }
}