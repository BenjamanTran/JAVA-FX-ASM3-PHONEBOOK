//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package controller;

import dao.ContactDAO;
import dao.GroupDAO;
import entity.Contact;
import entity.Group;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ContactController {
    @FXML
    private TextField search;
    @FXML
    private ComboBox<Group> cbGroup;
    @FXML
    private TableView tblContact;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnGroup;
    List<Contact> contacts;
    ContactDAO contactDAO = new ContactDAO();
    private final String GROUP = "data/group.txt";
    private final String CONTACT = "data/contact.txt";

    public ContactController() {
    }

    @FXML
    void initialize() {
        try {
            this.contacts = (new ContactDAO()).loadContact("data/contact.txt");
            TableColumn<String, Contact> fname = new TableColumn("First Name");
            fname.setCellValueFactory(new PropertyValueFactory("firstName"));
            this.tblContact.getColumns().add(fname);
            TableColumn<String, Contact> lname = new TableColumn("Last Name");
            lname.setCellValueFactory(new PropertyValueFactory("lastName"));
            this.tblContact.getColumns().add(lname);
            TableColumn<String, Contact> phone = new TableColumn("Phone");
            phone.setCellValueFactory(new PropertyValueFactory("phone"));
            this.tblContact.getColumns().add(phone);
            TableColumn<String, Contact> email = new TableColumn("Email");
            email.setCellValueFactory(new PropertyValueFactory("email"));
            this.tblContact.getColumns().add(email);
            TableColumn<String, Contact> dob = new TableColumn("Birth Date");
            dob.setCellValueFactory(new PropertyValueFactory("dob"));
            this.tblContact.getColumns().add(dob);
            TableColumn<String, Contact> group = new TableColumn("Group Name");
            group.setCellValueFactory(new PropertyValueFactory("group"));
            this.tblContact.getColumns().add(group);
            this.showGroup((new GroupDAO()).loadGroup("data/group.txt"));
            this.showContact(this.contacts);
            this.tblContact.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        } catch (Exception var7) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("" + var7);
        }

    }

    public void showContact(List<Contact> c) {
        this.tblContact.getItems().clear();
        String group = ((Group) this.cbGroup.getSelectionModel().getSelectedItem()).getName();
        Iterator var3;
        Contact x;
        if (group.equals("All")) {
            var3 = c.iterator();

            while (var3.hasNext()) {
                x = (Contact) var3.next();
                this.tblContact.getItems().add(x);
            }
        } else {
            var3 = c.iterator();

            while (var3.hasNext()) {
                x = (Contact) var3.next();
                if (x.getGroup().equalsIgnoreCase(group)) {
                    this.tblContact.getItems().add(x);
                }
            }
        }

    }

    public void showGroup(List<Group> g) {
        this.cbGroup.getItems().add(new Group("All"));
        Iterator var2 = g.iterator();

        while (var2.hasNext()) {
            Group x = (Group) var2.next();
            this.cbGroup.getItems().add(x);
        }

        this.cbGroup.getSelectionModel().select(0);
    }

    public void searchContact(ActionEvent evt) throws Exception {
        if (evt.getSource() == this.btnSearch) {
            String group = ((Group) this.cbGroup.getSelectionModel().getSelectedItem()).getName();
            List<Contact> c = this.contactDAO.search(this.contacts, group, this.search.getText());
            this.showContact(c);
        } else if (evt.getSource() == this.btnAdd) {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../ui/addContact.fxml"));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add a new Contact");
            stage.show();
            AddContactController c = (AddContactController) loader.getController();
            c.setContacts(this.contacts);
            c.setAddContactController(this);
        } else if (evt.getSource() == this.btnDelete) {
            this.deleteContact();
        } else if (evt.getSource() == this.btnUpdate) {
            this.updateContact();
        } else if (evt.getSource() == this.btnGroup) {
            this.groupPanel();
        }

    }

    public void groupPanel() throws Exception {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../ui/group.fxml"));
        Parent root = (Parent) loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Group a Management");
        stage.show();
        GroupController c = (GroupController) loader.getController();
        c.setContactController(this);
    }

    public void updateContact() throws Exception {
        int i = this.tblContact.getSelectionModel().getSelectedIndex();
        if (i < this.tblContact.getItems().size() && i >= 0) {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../ui/updateContact.fxml"));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Update a Contact");
            stage.show();
            UpdateContactController c = (UpdateContactController) loader.getController();
            c.setContacts(this.contacts);
            c.setContactController(this);
            c.setUpdatedContact((Contact) this.tblContact.getItems().get(i));
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Information");
            alert.setContentText("Select a Contact to update");
            alert.showAndWait();
        }

    }

    public void deleteContact() throws Exception {
        int i = this.tblContact.getSelectionModel().getSelectedIndex();
        Alert alert;
        if (i < this.tblContact.getItems().size() && i >= 0) {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Do you wanna delete selected contact?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                this.contacts.remove(i);
                this.showContact(this.contacts);
                this.contactDAO.saveToFile(this.contacts, "data/contact.txt");
            }
        } else {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Information");
            alert.setContentText("Select a Contact to delete");
            alert.showAndWait();
        }

    }
}
