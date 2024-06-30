package Util;

import Module.Entry;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextProcessingUtil {

    private List<Entry> entriesList;
    private Set<Entry> entriesSet;
    private Map<String, Entry> entriesMap;

    public TextProcessingUtil() {
        entriesList = new ArrayList<>();
        entriesSet = new HashSet<>();
        entriesMap = new HashMap<>();
    }

    public List<Entry> getEntriesList() {
        return entriesList;
    }

    public Set<Entry> getEntriesSet() {
        return entriesSet;
    }

    public Map<String, Entry> getEntriesMap() {
        return entriesMap;
    }

    public void addEntryToList(Entry entry) {
        if (entriesList.stream().anyMatch(e -> e.getValue().equals(entry.getValue()))) {
            showAlert(AlertType.ERROR, "Error", "Entry already exists in the List.");
        } else {
            entriesList.add(entry);
        }
    }

    public void addEntryToSet(Entry entry) {
        if (entriesSet.stream().anyMatch(e -> e.getValue().equals(entry.getValue()))) {
            showAlert(AlertType.ERROR, "Error", "Entry already exists in the Set.");
        } else {
            entriesSet.add(entry);
        }
    }



    public void addEntryToMap(String key, String value) {
        if (entriesMap.containsKey(key)) {
            showAlert(AlertType.ERROR, "Error", "Key already exists in the Map.");
        } else {
            entriesMap.put(key, new Entry(key, value));
        }
    }

    public void updateEntryInList(int index, Entry newEntry) {
        entriesList.set(index, newEntry);
    }

    public void deleteEntryFromList(Entry entry) {
        entriesList.remove(entry);
    }

    public void deleteEntryFromSet(Entry entry) {
        entriesSet.remove(entry);
    }

    public void deleteEntryFromMap(String key) {
        entriesMap.remove(key);
    }

    public ArrayList<String> search(String inputText, String patternText) {
        ArrayList<String> matches = new ArrayList<>();
        try {
            Pattern pattern = Pattern.compile(patternText);
            Matcher matcher = pattern.matcher(inputText);
            while (matcher.find()) {
                matches.add(matcher.group());
            }
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "Invalid regex pattern.");
        }
        return matches;
    }

    public boolean match(String inputText, String patternText) {
        try {
            Pattern pattern = Pattern.compile(patternText);
            Matcher matcher = pattern.matcher(inputText);
            return matcher.matches();
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "Invalid regex pattern.");
            return false;
        }
    }

    public String replace(String inputText, String patternText, String replacementText) {
        try {
            Pattern pattern = Pattern.compile(patternText);
            Matcher matcher = pattern.matcher(inputText);
            return matcher.replaceAll(replacementText);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "Invalid regex pattern.");
            return inputText;
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
