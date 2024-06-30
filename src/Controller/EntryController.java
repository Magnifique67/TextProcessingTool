package Controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import Util.TextProcessingUtil;
import Module.Entry;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntryController {

    private static final String LIST = "List";
    private static final String SET = "Set";
    private static final String MAP = "Map";

    @FXML
    private ComboBox<String> collectionTypeComboBox;

    @FXML
    private ComboBox<String> currentCollectionComboBox;

    @FXML
    private TextArea collectionDisplayArea;

    @FXML
    private TextField itemTextField;

    @FXML
    private TextArea inputTextArea;

    @FXML
    private TextField patternTextField;

    @FXML
    private TextField replacementTextField;

    @FXML
    private TextField keyTextField;

    @FXML
    private TextField valueTextField;

    @FXML
    private TextArea resultTextArea;

    @FXML
    private TextFlow resultTextFlow;

    private TextProcessingUtil util;
    private String currentCollectionType;

    @FXML
    public void initialize() {
        util = new TextProcessingUtil();
        collectionTypeComboBox.setItems(FXCollections.observableArrayList(LIST, SET, MAP));
        collectionTypeComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                currentCollectionType = newValue;
                updateCurrentCollectionComboBox();
                updateCollectionDisplayArea();
            }
        });

        inputTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            updateRegexHighlights();
        });

        patternTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateRegexHighlights();
        });

        // Allow editing and deleting items from collection display area
        collectionDisplayArea.setOnMouseClicked(event -> {
            if (currentCollectionType.equals(LIST) || currentCollectionType.equals(SET)) {
                String selectedItem = collectionDisplayArea.getSelectedText();
                if (selectedItem != null && !selectedItem.isEmpty()) {
                    valueTextField.setText(selectedItem);
                }
            } else if (currentCollectionType.equals(MAP)) {
                String selectedItem = collectionDisplayArea.getSelectedText();
                if (selectedItem != null && !selectedItem.isEmpty()) {
                    String[] parts = selectedItem.split(" -> ");
                    if (parts.length == 2) {
                        keyTextField.setText(parts[0]);
                        valueTextField.setText(parts[1]);
                    }
                }
            }
        });
    }

    private void updateCurrentCollectionComboBox() {
        currentCollectionComboBox.setItems(FXCollections.observableArrayList(currentCollectionType));
        currentCollectionComboBox.getSelectionModel().selectFirst();
    }

    private void updateCollectionDisplayArea() {
        StringBuilder displayText = new StringBuilder();

        switch (currentCollectionType) {
            case LIST:
                displayText.append("Entries in List:\n");
                for (Entry entry : util.getEntriesList()) {
                    displayText.append(entry.getValue()).append("\n");
                }
                break;
            case SET:
                displayText.append("Entries in Set:\n");
                for (Entry entry : util.getEntriesSet()) {
                    displayText.append(entry.getValue()).append("\n");
                }
                break;
            case MAP:
                displayText.append("Entries in Map:\n");
                for (String key : util.getEntriesMap().keySet()) {
                    displayText.append(key).append(" -> ").append(util.getEntriesMap().get(key).getValue()).append("\n");
                }
                break;
        }

        collectionDisplayArea.setText(displayText.toString());
    }

    @FXML
    private void handleSearch() {
        String inputText = inputTextArea.getText();
        String patternText = patternTextField.getText();

        ArrayList<String> matches = util.search(inputText, patternText);

        resultTextFlow.getChildren().clear();
        Text resultText = new Text("Matches found: " + matches.toString());
        resultTextFlow.getChildren().add(resultText);

    }

    @FXML
    private void handleMatch() {
        String inputText = inputTextArea.getText();
        String patternText = patternTextField.getText();

        boolean isMatch = util.match(inputText, patternText);

        resultTextFlow.getChildren().clear();
        Text resultText = new Text(isMatch ? "The entire input text matches the pattern." : "The entire input text does not match the pattern.");
        resultTextFlow.getChildren().add(resultText);

    }

    @FXML
    private void handleReplace() {
        String inputText = inputTextArea.getText();
        String patternText = patternTextField.getText();
        String replacementText = replacementTextField.getText();

        String replacedText = util.replace(inputText, patternText, replacementText);

        resultTextFlow.getChildren().clear();
        Text resultText = new Text("Replaced text: " + replacedText);
        resultTextFlow.getChildren().add(resultText);

    }

    @FXML
    private void handleAdd() {
        String key = keyTextField.getText().trim();
        String value = valueTextField.getText().trim();
        if (collectionTypeComboBox.getValue() == null) {
            showAlert(AlertType.ERROR, "Error", "Please select a collection type.");
            return;
        }

        if (value.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Value cannot be empty.");
            return;
        }

        if (currentCollectionType.equals(MAP) && key.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Key cannot be empty for Map.");
            return;
        }

        switch (currentCollectionType) {
            case LIST:
                util.addEntryToList(new Entry("", value));
                break;
            case SET:
                util.addEntryToSet(new Entry("", value));
                break;
            case MAP:
                util.addEntryToMap(key, value);
                break;
            default:
                showAlert(AlertType.ERROR, "Error", "Unsupported collection type.");
                return;
        }

        updateCollectionDisplayArea();
    }

    @FXML
    private void handleEdit() {
        String oldValue = collectionDisplayArea.getSelectedText().trim();
        String newValue = valueTextField.getText().trim();

        if (newValue.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Item cannot be empty.");
            return;
        }

        switch (currentCollectionType) {
            case LIST:
                for (Entry entry : util.getEntriesList()) {
                    if (entry.getValue().equals(oldValue)) {
                        entry.setValue(newValue);
                        updateCollectionDisplayArea();
                        showAlert(AlertType.INFORMATION, "Edit Operation", "Item edited successfully.");
                        return;
                    }
                }
                break;
            case SET:
                if (util.getEntriesSet().removeIf(entry -> entry.getValue().equals(oldValue))) {
                    util.addEntryToSet(new Entry("", newValue));
                    updateCollectionDisplayArea();
                    showAlert(AlertType.INFORMATION, "Edit Operation", "Item edited successfully.");
                    return;
                }
                break;
            case MAP:
                String key = keyTextField.getText().trim();
                if (key.isEmpty()) {
                    showAlert(AlertType.ERROR, "Error", "Key cannot be empty for Map.");
                    return;
                }
                if (util.getEntriesMap().containsKey(key)) {
                    util.addEntryToMap(key, newValue);
                    updateCollectionDisplayArea();
                    showAlert(AlertType.INFORMATION, "Edit Operation", "Item edited successfully.");
                    return;
                }
                break;
            default:
                showAlert(AlertType.ERROR, "Error", "Unsupported collection type.");
        }

        showAlert(AlertType.ERROR, "Error", "Item not found for editing.");
    }


    @FXML
    private void handleDelete() {
        String item = valueTextField.getText().trim();
        if (item.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Item cannot be empty.");
            return;
        }

        switch (currentCollectionType) {
            case LIST:
                boolean deleted = false;
                for (Entry entry : util.getEntriesList()) {
                    if (entry.getValue().equals(item)) {
                        util.deleteEntryFromList(entry);
                        deleted = true;
                        break;
                    }
                }

                if (deleted) {
                    updateCollectionDisplayArea();
                    showAlert(AlertType.INFORMATION, "Delete Operation", "Item deleted successfully.");
                } else {
                    showAlert(AlertType.ERROR, "Error", "Item not found for deletion.");
                }
                break;
            case SET:
                boolean setDeleted = util.getEntriesSet().removeIf(entry -> entry.getValue().equals(item));
                if (setDeleted) {
                    updateCollectionDisplayArea();
                    showAlert(AlertType.INFORMATION, "Delete Operation", "Item deleted successfully.");
                } else {
                    showAlert(AlertType.ERROR, "Error", "Item not found for deletion.");
                }
                break;
            case MAP:
                Entry mapDeleted = util.getEntriesMap().remove(item);
                if (mapDeleted != null) {
                    updateCollectionDisplayArea();
                    showAlert(AlertType.INFORMATION, "Delete Operation", "Item deleted successfully.");
                } else {
                    showAlert(AlertType.ERROR, "Error", "Item not found for deletion.");
                }
                break;
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateRegexHighlights() {
        String inputText = inputTextArea.getText();
        String patternText = patternTextField.getText();

        resultTextFlow.getChildren().clear();
        if (patternText.isEmpty() || inputText.isEmpty()) return;

        try {
            Pattern pattern = Pattern.compile(patternText);
            Matcher matcher = pattern.matcher(inputText);

            int lastEnd = 0;
            while (matcher.find()) {
                Text textBeforeMatch = new Text(inputText.substring(lastEnd, matcher.start()));
                Text matchedText = new Text(matcher.group());
                matchedText.setFill(Color.RED); // Highlight color
                resultTextFlow.getChildren().addAll(textBeforeMatch, matchedText);
                lastEnd = matcher.end();
            }
            Text textAfterMatch = new Text(inputText.substring(lastEnd));
            resultTextFlow.getChildren().add(textAfterMatch);
        } catch (Exception e) {
            // Handle invalid regex pattern
            resultTextFlow.getChildren().add(new Text("Invalid regex pattern."));
        }
    }
        @FXML
        private void handleClearCollection() {
            switch (currentCollectionType) {
                case LIST:
                    util.getEntriesList().clear();
                    break;
                case SET:
                    util.getEntriesSet().clear();
                    break;
                case MAP:
                    util.getEntriesMap().clear();
                    break;
                default:
                    showAlert(AlertType.ERROR, "Error", "Unsupported collection type.");
                    return;
            }
            updateCollectionDisplayArea();
            showAlert(AlertType.INFORMATION, "Clear Operation", "Collection cleared successfully.");
        }
    @FXML
    private void handleFindItem() {
        String searchValue = valueTextField.getText().trim();
        if (searchValue.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Search value cannot be empty.");
            return;
        }

        StringBuilder foundLocations = new StringBuilder();
        boolean found = false;

        switch (currentCollectionType) {
            case LIST:
                for (int i = 0; i < util.getEntriesList().size(); i++) {
                    if (util.getEntriesList().get(i).getValue().equals(searchValue)) {
                        found = true;
                        foundLocations.append("List index: ").append(i).append("\n");
                    }
                }
                break;
            case SET:
                int index = 0;
                for (Entry entry : util.getEntriesSet()) {
                    if (entry.getValue().equals(searchValue)) {
                        found = true;
                        foundLocations.append("Set index: ").append(index).append("\n");
                    }
                    index++;
                }
                break;
            case MAP:
                for (String key : util.getEntriesMap().keySet()) {
                    if (util.getEntriesMap().get(key).getValue().equals(searchValue)) {
                        found = true;
                        foundLocations.append("Map key: ").append(key).append("\n");
                    }
                }
                break;
        }

        if (found) {
            showAlert(AlertType.INFORMATION, "Find Operation", "Item found at:\n" + foundLocations.toString());
        } else {
            showAlert(AlertType.ERROR, "Error", "Item not found.");
        }
    }

}

