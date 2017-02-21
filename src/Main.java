import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by kevin on 11/12/16.
 */
public class Main extends Application{

    ArrayList<DesktopEntry> desktopEntries;
    DesktopEntry selectedEntry = null;

    ListView<DesktopEntry> list;
    ObservableList<DesktopEntry> items;

    public static void main (String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (System.getProperty("user.name").toLowerCase().equals("root") && System.getProperty("os.name").toLowerCase().equals("linux"))
        {
            File mainDir = new File("/usr/share/applications");
            if (mainDir.exists() && mainDir.isDirectory()) {
                //get every file in /usr/share/applications
                List<File> desktopEntriesTemp = Arrays.asList(mainDir.listFiles());
                ArrayList<File> desktopEntryFiles = new ArrayList<File>();
                //check every file in /usr/share/applications to see if its's a .desktop file, and keep all .desktops in another ArrayList
                for (int i = 0; i < desktopEntriesTemp.size(); i++)
                    if (desktopEntriesTemp.get(i).toString().endsWith(".desktop")) {
                        desktopEntryFiles.add(desktopEntriesTemp.get(i));
                        System.out.println(desktopEntriesTemp.get(i));
                    }
                System.out.println(desktopEntryFiles.size());
                //create some DesktopEntries from all of the .desktop files
                desktopEntries = new ArrayList<DesktopEntry>();
                for (int i = 0; i < desktopEntryFiles.size(); i++) {
                    desktopEntries.add(new DesktopEntry(desktopEntryFiles.get(i)));
                    System.out.println(desktopEntries.get(i).printDebug());
                }


                primaryStage.setTitle("Manage Desktop Entries");
                GridPane mainGrid = new GridPane();

                VBox left = new VBox();

                list = new ListView<DesktopEntry>();

                items = FXCollections.observableArrayList();
                for (DesktopEntry i : desktopEntries) {
                    items.add(i);
                }
                list.setItems(items);

                TabPane tabPane = new TabPane();
                Tab tab = new Tab();
                tab.setText("System Wide");
                tab.setContent(list);
                tab.setClosable(false);
                tabPane.getTabs().add(tab);

                Button userEntries = new Button("Edit a User's Desktop Entries...");
                //userEntries.setOnAction(e -> );

                left.getChildren().addAll(userEntries, tabPane);
                left.setMargin(userEntries, new Insets(10));

                VBox right = new VBox(20);

                HBox nameRow = new HBox(10);
                Label nameLabel = new Label("Name:");
                TextField nameBox = new TextField();
                nameBox.setDisable(true);
                nameRow.getChildren().addAll(nameLabel, nameBox);

                HBox noDisplayRow = new HBox(10);
                Label noDisplayLabel = new Label("Hide Desktop Entry:");
                CheckBox noDisplayBox = new CheckBox();
                noDisplayBox.setDisable(true);
                noDisplayRow.getChildren().addAll(noDisplayLabel, noDisplayBox);

                HBox categoryRow = new HBox(10);
                Label categoryLabel = new Label("Categories:");
                ListView<String> categoryList = new ListView<String>();
                ObservableList<String> categoryItems = FXCollections.observableArrayList();
                categoryList.setItems(categoryItems);
                categoryList.setDisable(true);
                VBox addRemoveCategory = new VBox(10);
                Button addCategory = new Button("Add +");
                addCategory.setOnAction(e -> add(categoryItems));
                addCategory.setDisable(true);
                Button removeCategory = new Button("Remove -");
                removeCategory.setOnAction(e -> categoryItems.remove(categoryList.getSelectionModel().getSelectedItem()));
                removeCategory.setDisable(true);
                addRemoveCategory.getChildren().addAll(addCategory, removeCategory);
                categoryRow.getChildren().addAll(categoryLabel, categoryList, addRemoveCategory);

                HBox iconRow = new HBox(10);
                Label iconLabel = new Label("Icon:");
                TextField iconBox = new TextField();
                iconBox.setDisable(true);
                iconRow.getChildren().addAll(iconLabel, iconBox);

                Button button = new Button("Print Debug");
                button.setOnAction(e -> System.out.println("\n" + list.getSelectionModel().getSelectedItem().printDebug()));

                Button applyChanges = new Button("Apply Changes");
                applyChanges.setDisable(true);

                right.getChildren().addAll(nameRow, noDisplayRow, categoryRow, iconRow, applyChanges);
                mainGrid.add(left, 0, 0);
                mainGrid.add(right, 1, 0);
                mainGrid.setMargin(right, new Insets(10));
                Scene scene = new Scene(mainGrid, 1000, 500);
                primaryStage.setScene(scene);

                //Make sure columns 0 and 1 both take up 50% of the window space
                ColumnConstraints column0 = new ColumnConstraints();
                column0.setPercentWidth(50);
                ColumnConstraints column1 = new ColumnConstraints();
                column1.setPercentWidth(50);
                mainGrid.getColumnConstraints().addAll(column0, column1);

                //Make sure row 0 takes up 100% of the window space
                RowConstraints row0 = new RowConstraints();
                row0.setPercentHeight(100);
                mainGrid.getRowConstraints().add(row0);

                primaryStage.show();
                list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DesktopEntry>() {
                    @Override
                    public void changed(ObservableValue<? extends DesktopEntry> observable, DesktopEntry oldValue, DesktopEntry newValue) {
                        //enable all editing contronls since something is now selected
                        nameBox.setDisable(false);
                        noDisplayBox.setDisable(false);
                        categoryList.setDisable(false);
                        addCategory.setDisable(false);
                        removeCategory.setDisable(false);
                        iconBox.setDisable(false);
                        applyChanges.setDisable(false);

                        //load all values into editing controls
                        nameBox.setText(newValue.getName());
                        noDisplayBox.setSelected(newValue.getNoDisplay());
                        int size = categoryItems.size();
                        for (int i = 0; i < size; i++)
                            categoryItems.remove(categoryItems.get(0));
                        for (String i : newValue.getCategories())
                            categoryItems.add(i);
                        iconBox.setText(newValue.getIcon());
                        selectedEntry = newValue;
                    }
                });
                applyChanges.setOnAction(e -> apply(selectedEntry, nameBox.getText(), noDisplayBox.isSelected(), categoryItems, iconBox.getText()));
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "ERROR: Directory " + mainDir.getAbsolutePath() + " does not exist!", ButtonType.OK);
                alert.show();
            }
        }
        else
        {
            String alertText = "Make sure the program is ";
            if (System.getProperty("os.name").toLowerCase().equals("linux") == false)
                alertText = alertText + "run on a Linux system!";
            else
                alertText = alertText + "run as root!";
            Alert alert = new Alert(Alert.AlertType.WARNING, alertText, ButtonType.OK);
            alert.show();
        }
    }

    void apply(DesktopEntry entry, String name, boolean noDisplay, ObservableList<String> categoriesObservable, String icon) {
        //Create a dialog that asks if the user wants to apply changes
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Are you sure?");
        confirm.setContentText("Are you sure you want to apply changes? THIS CANNOT BE UNDONE!");
        confirm.setHeaderText(null);
        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");
        confirm.getButtonTypes().setAll(yes, no);
        Optional<ButtonType> result = confirm.showAndWait();

        //Apply changes if user clicks yes
        if (result.get() == yes) {
            System.out.println("\n APPLYING");
            boolean changeCategories = false;
            if (categoriesObservable.size() != entry.getCategories().size())
                changeCategories = true;
            else {
                for (int i = 0; i < categoriesObservable.size(); i++)
                    if (entry.getCategories().get(i).equals(categoriesObservable.get(i)) == false)
                        changeCategories = true;
            }
            if (entry.getName().equals(name) == false) {
                System.out.println("\n SETTING NAME TO: " + name);
                entry.setName(name);
                items.set(list.getSelectionModel().getSelectedIndex(), entry);
            }
            if (entry.getNoDisplay() != noDisplay) {
                System.out.println("\n SETTING NODISPLAY TO: " + noDisplay);
                entry.setNoDisplay(noDisplay);
            }
            if (changeCategories) {
                ArrayList<String> categories = new ArrayList<String>();
                for (String i : categoriesObservable)
                    categories.add(i);
                System.out.println("\n SETTING CATEGORIES TO: " + categories);
                entry.setCategories(categories);
            }
            if (entry.getIcon().equals(icon) == false) {
                System.out.println("\n SETTING ICON TO: " + icon);
                entry.setIcon(icon);
            }
            Alert success = new Alert(Alert.AlertType.INFORMATION, "Successfully applied changes. You may have to restart X11 or your desktop environment.", ButtonType.OK);
            success.setHeaderText(null);
            success.show();
        }
    }

    void add(ObservableList<String> list)
    {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Needed");
        dialog.setContentText("Please enter the category name:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent())
            list.add(result.toString().substring(9, result.toString().length() - 1));
        System.out.println(list);
    }
}
