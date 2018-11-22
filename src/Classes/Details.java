package Classes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public final class Details {

    private static StringProperty planesNumber = new SimpleStringProperty("00");
    private static StringProperty totalTasksNumber = new SimpleStringProperty("00");
    private static StringProperty coveredTasksNumber = new SimpleStringProperty("00");
    private static StringProperty remainedTasksNumber = new SimpleStringProperty("00");

    private Details() {
        throw new AssertionError();
    }

    private static String getValueInFormat(int value) {
        return String.format("%02d", value);
    }

    public static StringProperty getPlanesNumberProperty() {
        return planesNumber;
    }

    public static void setPlanesNumber(int value) {
        planesNumber.set(getValueInFormat(value));
    }

    public static StringProperty getTotalTasksNumberProperty() {
        return totalTasksNumber;
    }

    public static void setTotalTasksNumber(int value) {
        totalTasksNumber.set(getValueInFormat(value));
    }

    public static StringProperty getCoveredTasksNumberProperty() {
        return coveredTasksNumber;
    }

    public static void setCoveredTasksNumber(int value) {
        coveredTasksNumber.set(getValueInFormat(value));
    }

    public static StringProperty getRemainedTasksNumberProperty() {
        return remainedTasksNumber;
    }

    public static void setRemainedTasksNumber(int value) {
        remainedTasksNumber.set(getValueInFormat(value));
    }
}