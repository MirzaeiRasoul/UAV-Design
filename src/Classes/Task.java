package Classes;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Task {

    private double x;
    private double y;
    private double radius;
    private Color color;

    private Circle task;

    public Task(double x, double y) {
        this.x = x;
        this.y = y;
        initDefaultValues();
        initTask();
    }

    private void initDefaultValues() {
        radius = 5;
        color = Color.rgb(255,0,255);
    }

    private void initTask() {
        task = new Circle(x, y, radius, color);
    }

    public Circle getTask() {
        return task;
    }
}