package com.ml.catalogg.ui;

import com.ml.catalogg.helper.MainScreenHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
@Component
public class MainScreenController implements Initializable {
    private static final String IDLE_BUTTON_STYLE = "-fx-background-color: transparent;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-background-color: #CCCCCC;";
    private static final String PRESSED_BUTTON_STYLE = "-fx-background-color: #BBBBBB;";
    private String buttonPressed = "";
    private final List<ToggleButton> buttonList = new ArrayList<ToggleButton>();
    @FXML
    private HBox barRoot;
    @FXML
    private BorderPane menuView;
    @FXML
    private BorderPane tableView;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ToggleButton addButton;
    @FXML
    private ToggleButton helpButton;
    @FXML
    private ToggleButton searchButton;
    @FXML
    private ToggleButton settingsButton;
    @FXML
    private ToggleButton statsButton;
    @FXML
    private ToggleButton manageButton;
    @FXML
    private ToggleButton viewButton;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set up look of buttons
        setUpButton(addButton, "addButton");
        setUpButton(manageButton, "manageButton");
        setUpButton(helpButton, "helpButton");
        setUpButton(searchButton, "searchButton");
        setUpButton(settingsButton, "settingsButton");
        setUpButton(viewButton, "viewButton");
        setUpButton(statsButton, "statsButton");

        //set up button handlers
        addButton.setOnAction(this::handleAddButton);
        manageButton.setOnAction(this::handleManageButton);
        helpButton.setOnAction(this::handleHelpButton);
        searchButton.setOnAction(this::handleSearchButton);
        settingsButton.setOnAction(this::handleSettingsButton);
        viewButton.setOnAction(this::handleViewButton);
        statsButton.setOnAction(this::handleStatsButton);

        //set default view of the app to the view tab
        setInitialView();
        //setup object for getting a ref to this screen to set screens from other controllers
        sendController();
}
    private void setUpButton(ToggleButton button, String name) {
        button.setStyle(IDLE_BUTTON_STYLE);
        button.setOnMouseEntered(e -> {
            if (!buttonPressed.equals(name)) {
                button.setStyle(HOVERED_BUTTON_STYLE);
            }
        });
        button.setOnMouseExited(e -> {
            if (!buttonPressed.equals(name)) {
                button.setStyle(IDLE_BUTTON_STYLE);
            }
        });
        buttonList.add(button);
    }
    private void resetButtonStatesExceptSelected (ToggleButton button) {
        for (ToggleButton btn : buttonList) {
            if (btn == button) {
                continue;
            }
            else {
                btn.setStyle(IDLE_BUTTON_STYLE);
            }
        }
    }
    public void handleAddButton(ActionEvent event) {
        resetButtonStatesExceptSelected(addButton);
        addButton.setStyle(PRESSED_BUTTON_STYLE);
        this.buttonPressed = "addButton";
        tableView.getTop().setVisible(false);
        tableView.getTop().setManaged(false);
        Pane view = null;
        try {
            FXMLLoader loader = new FXMLLoader((getClass().getClassLoader().getResource("fxml/AddScreen.fxml")));
            loader.setControllerFactory(applicationContext::getBean);
            view = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scrollPane.setContent(view);
        scrollPane.setFitToHeight(false);
        scrollPane.setFitToWidth(true);
        view.autosize();
    }
    public void handleHelpButton(ActionEvent event) {
        resetButtonStatesExceptSelected(helpButton);
        helpButton.setStyle(PRESSED_BUTTON_STYLE);
        this.buttonPressed = "helpButton";
        tableView.getTop().setVisible(false);
        tableView.getTop().setManaged(false);
    }
    public void handleManageButton(ActionEvent event) {
        resetButtonStatesExceptSelected(manageButton);
        manageButton.setStyle(PRESSED_BUTTON_STYLE);
        this.buttonPressed = "manageButton";
        tableView.getTop().setVisible(true);
        tableView.getTop().setManaged(true);
        Pane upperView = null;
        try {
            FXMLLoader loader = new FXMLLoader((getClass().getClassLoader().getResource("fxml/ManageBar.fxml")));
            loader.setControllerFactory(applicationContext::getBean);
            upperView = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        menuView.setTop(upperView);
        Pane lowerView = null;
        if (!Objects.equals(scrollPane.getContent().getId(), "tableRoot")) {
            try {
                FXMLLoader loader = new FXMLLoader((getClass().getClassLoader().getResource("fxml/ToListenTable.fxml")));
                loader.setControllerFactory(applicationContext::getBean);
                lowerView = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            scrollPane.setContent(lowerView);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);
            lowerView.autosize();
        }
    }
    public void handleSearchButton(ActionEvent event){
        resetButtonStatesExceptSelected(searchButton);
        searchButton.setStyle(PRESSED_BUTTON_STYLE);
        this.buttonPressed = "searchButton";
        tableView.getTop().setVisible(true);
        tableView.getTop().setManaged(true);
        Pane upperView = null;
        try {
            FXMLLoader loader = new FXMLLoader((getClass().getClassLoader().getResource("fxml/SearchBar.fxml")));
            loader.setControllerFactory(applicationContext::getBean);
            upperView = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        menuView.setTop(upperView);
        if (!Objects.equals(scrollPane.getContent().getId(), "tableRoot")) {
            Pane lowerView = null;
            try {
                FXMLLoader loader = new FXMLLoader((getClass().getClassLoader().getResource("fxml/ToListenTable.fxml")));
                loader.setControllerFactory(applicationContext::getBean);
                lowerView = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            scrollPane.setContent(lowerView);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);
            lowerView.autosize();
        }
    }
    public void handleSettingsButton(ActionEvent event) {
        resetButtonStatesExceptSelected(settingsButton);
        settingsButton.setStyle(PRESSED_BUTTON_STYLE);
        this.buttonPressed = "settingsButton";
        tableView.getTop().setVisible(false);
        tableView.getTop().setManaged(false);
        Pane view = null;
        try {
            FXMLLoader loader = new FXMLLoader((getClass().getClassLoader().getResource("fxml/SettingScreen.fxml")));
            loader.setControllerFactory(applicationContext::getBean);
            view = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scrollPane.setContent(view);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        view.autosize();
    }
    public void handleStatsButton(ActionEvent event) {
        resetButtonStatesExceptSelected(statsButton);
        statsButton.setStyle(PRESSED_BUTTON_STYLE);
        this.buttonPressed = "statsButton";
        tableView.getTop().setVisible(false);
        tableView.getTop().setManaged(false);
        Pane view = null;
        try {
            FXMLLoader loader = new FXMLLoader((getClass().getClassLoader().getResource("fxml/StatsScreen.fxml")));
            loader.setControllerFactory(applicationContext::getBean);
            view = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scrollPane.setContent(view);
        scrollPane.setFitToHeight(false);
        scrollPane.setFitToWidth(true);
        view.autosize();
    }
    public void handleViewButton(ActionEvent event) {
        setInitialView();
    }
    private void setInitialView () {
        resetButtonStatesExceptSelected(viewButton);
        viewButton.setStyle(PRESSED_BUTTON_STYLE);
        this.buttonPressed = "viewButton";
        tableView.getTop().setVisible(false);
        tableView.getTop().setManaged(false);
        if (!Objects.equals(scrollPane.getContent().getId(), "tableRoot")) {
            Pane view = null;
            try {
                FXMLLoader loader = new FXMLLoader((getClass().getClassLoader().getResource("fxml/ToListenTable.fxml")));
                loader.setControllerFactory(applicationContext::getBean);
                view = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            scrollPane.setContent(view);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);
            view.autosize();
        }
    }

    public void setManageScreen(String screenName, long albumId) {
        tableView.getTop().setVisible(false);
        tableView.getTop().setManaged(false);
        Pane view = null;
        try {
            FXMLLoader loader = new FXMLLoader((getClass().getClassLoader().getResource("fxml/" + screenName + ".fxml")));
            loader.setControllerFactory(applicationContext::getBean);
            view = loader.load();
            if (screenName.equals("DetailsScreen")) {
                DetailsScreenController detailsController = loader.getController();
                detailsController.setData(albumId);
            } else if (screenName.equals("EditScreen")) {
                EditScreenController editController = loader.getController();
                editController.setData(albumId);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scrollPane.setContent(view);
        scrollPane.setFitToHeight(false);
        scrollPane.setFitToWidth(true);
        view.autosize();
    }
    private void sendController() {
        MainScreenHolder mainScreenHolder = MainScreenHolder.getInstance();
        mainScreenHolder.setMainScreenController(this);
    }
}
