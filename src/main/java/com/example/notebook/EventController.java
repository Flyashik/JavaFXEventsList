package com.example.notebook;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EventController {
    @FXML
    private TextField eventNameTF;
    @FXML
    private TextField dateTF;
    @FXML
    private TextField placeTF;
    @FXML
    private TextField descriptionTF;
    @FXML
    private TextField partCondTF;
    @FXML
    private CheckBox participationCB;
    @FXML
    private TextField commentTF;

    @FXML
    private TableView<Event> tableV;
    @FXML
    private TableColumn eventNameCol;
    @FXML
    private TableColumn dateCol;
    @FXML
    private TableColumn placeCol;
    @FXML
    private TableColumn descriptionCol;
    @FXML
    private TableColumn partCondCol;
    @FXML
    private TableColumn participationCol;
    @FXML
    private TableColumn commentCol;

    Integer lastId;

    ArrayList<Event> eventList;
    ObservableList<Event> filteredList;

    @FXML
    public void initialize() throws FileNotFoundException {
        eventList = new ArrayList<Event>();

        eventNameCol.setCellValueFactory(new PropertyValueFactory<>("EventName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));
        placeCol.setCellValueFactory(new PropertyValueFactory<>("Place"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        partCondCol.setCellValueFactory(new PropertyValueFactory<>("PartCondition"));
        participationCol.setCellValueFactory(new PropertyValueFactory<>("Participation"));
        commentCol.setCellValueFactory(new PropertyValueFactory<>("Comment"));


        eventList = readCSV("data.csv");
        filteredList = FXCollections.observableArrayList(eventList);
        tableV.setItems(filteredList);
    }
    public static boolean isValidDate(String date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    @FXML
    protected void onClickAdd()
    {
        if (eventList.isEmpty())
        {
            lastId = 1;
        }
        else
        {
            lastId = eventList.get(eventList.size() - 1).getId() + 1;
        }

        Event event = new Event( 0,
                eventNameTF.getText(),
                dateTF.getText(),
                placeTF.getText(),
                descriptionTF.getText(),
                partCondTF.getText(),
                participationCB.isSelected(),
                "-");

        if (eventNameTF.getText().isEmpty() || dateTF.getText().isEmpty() || placeTF.getText().isEmpty() || partCondTF.getText().isEmpty())
        {
            showAlert(Alert.AlertType.WARNING, "Предупреждение", "Пустое поле!", "Не заполнены обязательные поля");
            return;
        }

        if (!isValidDate(dateTF.getText()))
        {
            showAlert(Alert.AlertType.WARNING, "Предупреждение", "Неверный формат даты!", "Пожалуйста, введите дату в формате dd.MM.yyyy");
            return;
        }

        eventList.add(event);

        filteredList = FXCollections.observableArrayList(eventList);
        tableV.setItems(filteredList);

        eventNameTF.clear();
        dateTF.clear();
        placeTF.clear();
        descriptionTF.clear();
        partCondTF.clear();

        exportToCSV(eventList, "data.csv");
    }

    @FXML
    protected void onClickDelete() {
        Event selectedItem = tableV.getSelectionModel().getSelectedItem();
        eventList.remove(selectedItem);

        filteredList = FXCollections.observableArrayList(eventList);
        tableV.setItems(filteredList);

        exportToCSV(eventList, "data.csv");
    }

    @FXML
    protected void onClickComment() {
        Event selectedItem = tableV.getSelectionModel().getSelectedItem();

        if (selectedItem == null)
            return;

        var i = 0;
        for (i = 0; i < eventList.size(); i++)
        {
            if (selectedItem.getId() == eventList.get(i).getId())
            {
                break;
            }
        }

        if (i == eventList.size())
            return;

        Event curItem = eventList.get(i);

        if (commentTF.getText().isEmpty())
        {
            showAlert(Alert.AlertType.WARNING, "Предупреждение", "Пустое поле!", "Поле Комментарий пусто");
            return;
        }
        
        Event newItem = new Event(curItem.getId(),
                curItem.getEventName(),
                curItem.getDate(),
                curItem.getPlace(),
                curItem.getDescription(),
                curItem.getPartCondition(),
                curItem.getParticipation(),
                commentTF.getText());

        eventList.set(i, newItem);

        filteredList = FXCollections.observableArrayList(eventList);
        tableV.setItems(filteredList);

        commentTF.clear();

        exportToCSV(eventList, "data.csv");
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content)
    {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void exportToCSV(ArrayList<Event> dataList, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

            writer.write("EventName,Date,Place,Description,PartCond,Participation,Comment");
            writer.newLine();

            for (Event data : dataList) {
                writer.write(data.getEventName() + "," +
                        data.getDate() + "," +
                        data.getPlace() + "," +
                        data.getDescription() + "," +
                        data.getPartCondition() + "," +
                        data.getParticipation() + "," +
                        data.getComment());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Event> readCSV(String fileName) {
        ArrayList<Event> dataList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] fields = line.split(",");
                var i = 1;
                if (fields.length == 7) {
                    String name = fields[0].trim();
                    String date = fields[1].trim();
                    String place = fields[2].trim();
                    String desc = fields[3].trim();
                    String partCond = fields[4].trim();
                    Boolean partition = Boolean.parseBoolean(fields[5]);
                    String comment = fields[6].trim();

                    Event taskData = new Event(i, name, date, place, desc, partCond, partition, comment);
                    i++;
                    dataList.add(taskData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataList;
    }
}
