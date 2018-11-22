package Classes;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scenario {

    private List<JSONObject> operatorsList;
    private List<JSONObject> planesList;
    private List<JSONObject> tasksList;

    public Scenario(String path) throws IOException, ParseException {
        FileReader scenarioJson = new FileReader(path);
        JSONParser parser = new JSONParser();
        JSONObject scenarioObject = (JSONObject) parser.parse(scenarioJson);

        JSONArray operatorsArray = (JSONArray) scenarioObject.get("planes");
        operatorsList = new ArrayList<>();
        for (Object operator : operatorsArray) {
            operatorsList.add((JSONObject) operator);
        }

        JSONArray planesArray = (JSONArray) scenarioObject.get("planes");
        planesList = new ArrayList<>();
        for (Object plane : planesArray) {
            planesList.add((JSONObject) plane);
        }

        JSONArray tasksArray = (JSONArray) scenarioObject.get("tasks");
        tasksList = new ArrayList<>();
        for (Object task : tasksArray) {
            tasksList.add((JSONObject) task);
        }
    }

    public List<JSONObject> getOperatorsList() {
        return operatorsList;
    }

    public List<JSONObject> getPlanesList() {
        return planesList;
    }

    public List<JSONObject> getTasksList() {
        return tasksList;
    }
}