//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package controller;

import dao.GroupDAO;
import entity.Group;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class GroupController {
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnClose;
    @FXML
    private ListView<Group> tblGroup;
    @FXML
    private TextField search;
    @FXML
    private TextField groupName;
    private final String GROUP = "data/group.txt";
    GroupDAO groupDAO = new GroupDAO();
    List<Group> groups;
    ContactController contactController;

    public GroupController() {
    }

    public void setContactController(ContactController contactController) {
        this.contactController = contactController;
    }

    @FXML
    void initialize() {
        try {
            this.groups = this.groupDAO.loadGroup("data/group.txt");
            this.showGroup(this.groups);
            this.tblGroup.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            this.tblGroup.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Group>() {
                public void changed(ObservableValue<? extends Group> observable, Group oldValue, Group newValue) {
                    if (GroupController.this.tblGroup.getSelectionModel().getSelectedItem() != null) {
                        GroupController.this.groupName.setText(((Group) GroupController.this.tblGroup.getSelectionModel().getSelectedItem()).getName());
                    }

                }
            });
        } catch (Exception var3) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("" + var3);
        }

    }

    public void searchAction() {
        List<Group> g = this.groupDAO.search(this.groups, this.search.getText());
        this.showGroup(g);
    }

    public void addAction() throws Exception {
        String name = this.groupName.getText().trim();
        if (!name.isEmpty() && !name.equals("")) {
            Group g = new Group(name);
            System.out.println(g);
            int i = this.groupDAO.indexOf(this.groups, g);
            Alert alert;
            if (i != -1) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Group name exists already, choose another name");
                alert.showAndWait();
            } else {
                this.groupDAO.saveGroupToList(this.groups, g);
                this.groupDAO.saveGroupToFile(this.groups, "data/group.txt");
                this.showGroup(this.groups);
                this.contactController.showGroup(this.groups);
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setContentText("A new group has been added");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Group name cannot be empty");
            alert.showAndWait();
        }

    }

    public void updateAction() {
        int i = this.tblGroup.getSelectionModel().getSelectedIndex();
        if (i < this.tblGroup.getItems().size() && i >= 0) {
            Group updated = new Group(this.groupName.getText());
            Alert alert;
            if (!this.groupDAO.updateGroup(this.groups, updated)) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Information");
                alert.setContentText("Please select another name for group");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setContentText("A Group has been updated");
                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Information");
            alert.setContentText("Select a Contact to update");
            alert.showAndWait();
        }
    }

    public void deleteAction() throws Exception {
        int i = this.tblGroup.getSelectionModel().getSelectedIndex();
        if (i >= 0 && i < this.tblGroup.getItems().size()) {
            int size = ((Group) this.tblGroup.getItems().get(i)).contacts().size();
            Alert alert;
            if (size > 0) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Group has some contacts, cannot delete this one");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setContentText("Do you wanna delete selected group?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    this.groups.remove(i);
                    this.showGroup(this.groups);
                    this.groupDAO.saveGroupToFile(this.groups, "data/group.txt");
                }
            }

        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Select a group to delete");
            alert.showAndWait();
        }
    }

    public void groupAction(ActionEvent evt) throws Exception {
        if (evt.getSource() == this.btnSearch) {
            this.searchAction();
        } else if (evt.getSource() == this.btnAdd) {
            this.addAction();
        } else if (evt.getSource() == this.btnUpdate) {
            this.updateAction();
        } else if (evt.getSource() == this.btnDelete) {
            this.deleteAction();
        } else if (evt.getSource() == this.btnClose) {
            Node source = (Node) evt.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }

    }

    public void showGroup(List<Group> groups) {
        if (this.tblGroup.getItems() != null) {
            this.tblGroup.getItems().clear();
            Iterator var2 = groups.iterator();

            while (var2.hasNext()) {
                Group g = (Group) var2.next();
                this.tblGroup.getItems().add(g);
            }
        }

    }
}
