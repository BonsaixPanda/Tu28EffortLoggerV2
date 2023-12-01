package Tu28EffortLoggerProject;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.LocalTime;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;


import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Vector; 
import java.util.*;


public class Tu28EffortLoggerProject extends Application {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/db_effortlogs";
    private static final String DATABASE_USERNAME = "----enter username----";
    private static final String DATABASE_PASSWORD = "----enter password----!";
    private static final String INSERT_EFFORT = "INSERT INTO logs (project_name, log_date, start_time, stop_time, delta_time, life_cycle_step, effort_category, misc) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	public static LocalTime start;
    public static String startTime; 
    
    public static void main(String[] args) throws SQLException {
    	// String project_name, String log_date, String start_time, String stop_time, String delta_time, String life_cycle_step, String effort_category, String misc);
        launch(args);
    }
    
    public void start(Stage primaryStage) throws SQLException {
    	ObservableList<String> nothing = FXCollections.observableArrayList("");
        primaryStage.setTitle("Effort Logger Console");
//============CHRIS PEKIN============//       
        // ================ SET TITLE ================ //
        
        //create title label
        Label titleLabel = new Label("Effort Logger Console");
        titleLabel.setFont(Font.font("Calibri", 20));
        titleLabel.setStyle("-fx-font-weight: bold");
        
        //create overall root pane
        BorderPane bp = new BorderPane();
        
        //create the top section area and give it a title
        HBox top = new HBox();
        top.setAlignment(Pos.CENTER);
        top.getChildren().add(titleLabel);
        top.setPadding(new Insets(5, 5, 0, 5)); // TOP / RIGHT / BOTTOM / LEFT
        bp.setTop(top);
        
        // ================ SET MAIN PAGE ================ //
        
        VBox index = new VBox();
        index.setPadding(new Insets(5, 10, 10, 10));
        
        // ================ SET CLOCK ================ //
        
        HBox clock = new HBox();
        Label clockLabel = new Label("Clock is stopped");
        clockLabel.setFont(Font.font("Calibri", 18));
        clockLabel.setStyle("-fx-font-weight: bold");
        clockLabel.setTextFill(Color.WHITE);
        
        Tooltip t = new Tooltip("The clock indicates whether or not the tool is logging time to an activity.");
        t.setFont(Font.font("Calibri", FontPosture.REGULAR, 12)); 
        clockLabel.setTooltip(t);

        clock.getChildren().add(clockLabel);
        clock.setPadding(new Insets(2, 2, 2, 2));
        clock.setAlignment(Pos.CENTER);
        clock.setStyle("-fx-background-color: #FF0000;");
        
        index.getChildren().add(clock);
        
        // ================ SET ACTIVITY START ================ //
        
        Label q1 = new Label("1. When you start a new activity, press the \"Start an Activity\" button.");
        index.getChildren().add(q1);
        q1.setPadding(new Insets(10, 0, 7, 0));
        q1.setStyle("-fx-font-weight: bold");
        
        Button b1 = new Button("Start an Activity");
        index.getChildren().add(b1);
        
        Tooltip t1 = new Tooltip("Start an activity with the below characteristics. This will start the time.");
        t.setFont(Font.font("Calibri", FontPosture.REGULAR, 12)); 
        b1.setTooltip(t1);
        q1.setTooltip(t1);

        
        b1.setOnAction(new EventHandler<ActionEvent>()
        {
        	@Override public void handle(ActionEvent e) {
        		LocalTime now = LocalTime.now();
        		start = now;
        		startTime = now.toString().substring(0,8);
        		if (clockLabel.getText().equals("Clock is stopped")) {
	        		clock.setStyle("-fx-background-color: green;");
	        		clockLabel.setText("Clock is running");
        		}
            }
        });
        
        Label q2 = new Label("2. Select the project, life cycle step, effort category, and deliverable from the following lists:");
        index.getChildren().add(q2);
        q2.setPadding(new Insets(7, 0, 5, 0));
        q2.setStyle("-fx-font-weight: bold");
        
        
        // ================ TOP ROW OF SELECTION ================ //

        HBox q21 = new HBox();
        q21.setPadding(new Insets(0, 0, 0, 25));
        
        VBox projectSection = new VBox();
        
        Label proj = new Label("Project:");
        proj.setPadding(new Insets(5, 0, 3, 0));
        proj.setStyle("-fx-font-weight: bold");
        projectSection.getChildren().add(proj);
        
        ComboBox<String> projectBox = new ComboBox<String>();
        projectBox.getItems().addAll("Development Project", "Business Project");
        projectSection.getChildren().add(projectBox);
        
        q21.getChildren().add(projectSection);
  
        VBox lifeCycleSection = new VBox();
        lifeCycleSection.setPadding(new Insets(0, 0, 0, 100));
        
        Label lifeCycle = new Label("Life Cycle Step:");
        lifeCycle.setPadding(new Insets(5, 0, 3, 0));
        lifeCycle.setStyle("-fx-font-weight: bold");
        lifeCycleSection.getChildren().add(lifeCycle);

        ComboBox<String> lifeCycleBox = new ComboBox<>();
        lifeCycleBox.getItems().addAll("Problem Understanding", "Conceptual Design Plan", "Requirements", "Conceptual Design", "Conceptual Design Review", "Detailed Design Plan", "Detailed Design/Prototype", "Detailed Design Review", "Implementation Plan", "Test Case Generation", "Solution Specification", "Solution Review", "Solution Implementation", "Unit/System Test", "Reflection", "Repository Update", "Planning", "Information Gathering");
        lifeCycleSection.getChildren().add(lifeCycleBox );
        
        q21.getChildren().add(lifeCycleSection);
        
        index.getChildren().add(q21);
        
        HBox q22 = new HBox();
        q22.setPadding(new Insets(0, 0, 0, 25));
        
        VBox effortSection = new VBox();
        
        Label effort = new Label("Effort Category:");
        effort.setPadding(new Insets(5, 0, 3, 0));
        effort.setStyle("-fx-font-weight: bold");
        effortSection.getChildren().add(effort);

        ComboBox<String> effortBox = new ComboBox<String>();
        effortBox.getItems().addAll("Plans", "Deliverables", "Interruptions", "Defects", "Other");
        effortSection.getChildren().add(effortBox );
        
        q22.getChildren().add(effortSection);
        
        VBox effortSection2 = new VBox();
        effortSection2.setPadding(new Insets(0, 0, 0, 100));
        
        Label placeHolder = new Label("");
        placeHolder.setPadding(new Insets(5, 0, 3, 0));
        effortSection2.getChildren().add(placeHolder);
        
        ComboBox<String> effortBox2 = new ComboBox<String>();
        ObservableList<String> plansBox = FXCollections.observableArrayList("text1", "text2", "text3");
        ObservableList<String> deliverablesBox = FXCollections.observableArrayList("text1", "text2", "text3");
        ObservableList<String> interruptionsBox = FXCollections.observableArrayList("text1", "text2", "text3");
        ObservableList<String> defectsBox = FXCollections.observableArrayList("text1", "text2", "text3");

        effortSection2.getChildren().add(effortBox2);
        
        q22.getChildren().add(effortSection2);
        
        index.getChildren().add(q22);

        bp.setCenter(index);
        
        Label q3 = new Label("3. Press the \"Stop this Activity\" to generate an effort log entry using the attributes above.");
        q3.setPadding(new Insets(13, 0, 7, 0));
        q3.setStyle("-fx-font-weight: bold");
        
        index.getChildren().add(q3);
        
        Button b2 = new Button("Stop this Activity");
        index.getChildren().add(b2);
        
        Tooltip t2 = new Tooltip("Stop the activity you previously started. This will stop the time.");
        t.setFont(Font.font("Calibri", FontPosture.REGULAR, 12)); 
        b2.setTooltip(t2);
        q3.setTooltip(t2);
        
        // ======= JEFFREY HSU ========= //
        
        Popup popup = new Popup(); //pop up for if the clock out was after hours
		Label outOfTime = new Label("Clock out after hours, report sent.");
		popup.getContent().add(outOfTime);
		popup.setAutoHide(true);
		outOfTime.setTextFill(Color.RED);
		outOfTime.setMinWidth(400); 
		outOfTime.setMinHeight(20);
        
        b2.setOnAction(new EventHandler<ActionEvent>() //handle the event for when a clock is stopped
        {
        	@Override public void handle(ActionEvent e) {
        		LocalTime now = LocalTime.now();
        		if (clockLabel.getText().equals("Clock is running")) {
        			clock.setStyle("-fx-background-color: red;");
        			clockLabel.setText("Clock is stopped");
        			LocalTime refTime = LocalTime.of(10, 0); // if the local time is after a specific time (example of 10am for video)
            		if (now.isAfter(refTime)) { // show the pop up if there is an error presented
            			popup.show(primaryStage);
            		}
        		}
        		String endTime = (now.toString().substring(0, 8));
        		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        		Date date = new Date();
        		long delta = start.until(now, ChronoUnit.HOURS);
        	
        		createNewEffortLog("" + projectBox.getValue(), formatter.format(date), startTime, endTime, "" + delta, "" + lifeCycleBox.getValue(), "" + effortBox.getValue(), "" + effortBox2.getValue());
            }
        });
        
        // ======= END OF JEFFREY HSU ======= //
        
        Label q4 = new Label("4. Unless you are done for the day, it is best to perform steps 1 and 2 above before resuming work.");
        q4.setPadding(new Insets(7, 0, 0, 0));
        q4.setStyle("-fx-font-weight: bold");
        
        index.getChildren().add(q4);
        
        HBox buttonBox = new HBox(15);
        buttonBox.setPadding(new Insets(7, 0, 0, 0));
        
        Button editorButton = new Button("Effort Log Editor");
        Button defectButton = new Button("Defect Log Console");
        Button definitionButton = new Button("Definitions");
        Button logButton = new Button("Effort and Defect Logs");
        Button PlanningPokerButton = new Button("Planning Poker");

        
        buttonBox.getChildren().addAll(editorButton, defectButton, definitionButton, logButton, PlanningPokerButton);
        buttonBox.setPadding(new Insets(0, 0, 20, 0));
        
        index.getChildren().add(buttonBox);
        
        // =============== LOGIN PAGE INFORMATION =================== //
        
        Button login = new Button("Login");
        index.getChildren().add(login);
        
        Scene scene = new Scene(bp, 800, 425);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        BorderPane logPane = new BorderPane();
        Scene logScene = new Scene(logPane, 800, 425);

        login.setOnAction(new EventHandler<ActionEvent>()
        {
        	@Override public void handle(ActionEvent e) {
                primaryStage.setScene(logScene);
        		primaryStage.show();
        	}
        });

        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER);

        // === CHRIS PEKIN === //
        
        Label usernameLabel = new Label("Username:"); // username Label
        usernameLabel.setStyle("-fx-font-weight: bold");
        TextField usernameField = new TextField(); // username Field
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(200);
        
        Label passwordLabel = new Label("Password:"); //password label
        passwordLabel.setStyle("-fx-font-weight: bold");
        PasswordField passwordField = new PasswordField(); //password field
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(200);

        Button loginButton = new Button("Login"); // login button

        Label statusLabel = new Label();

        // Add the UI components to the center VBox
        centerBox.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton, statusLabel);

        // Set the center content of the BorderPane
        logPane.setCenter(centerBox);
        
      /// ============ JUSTIN BOYD =========== //

        BorderPane badbadSoapMan = new BorderPane();
        Scene badPeople = new Scene(badbadSoapMan, 800, 425);
        
       
        // Handle the login button click
        loginButton.setOnAction(event -> { //on Click Action Event
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (isValidInput(username) && isValidInput(password)) { // passes through a validation method (to check for SQL Injection)
                if (authenticate(username, password)) { // checks if the password and username is a valid combo
                	primaryStage.setScene(scene); // if the input valid, then change the screen to the actual effort logger
                } 
                else {
                    statusLabel.setText("Login failed. Please try again."); // if not display an error message
                }
            } 
            else { // if there is a SQL injection attempt it displays and prevents entry
                statusLabel.setText("Invalid input. Please use alphanumeric characters.");
                primaryStage.setScene(badPeople); // it displays a dead page if attempted
            }
        });
        
        // === Effort Log Editor === // Krishna Ramani
        
        BorderPane editPane = new BorderPane();
        Scene editorScene = new Scene(editPane, 800, 425);
                
        Label titleLabel2 = new Label("Effort Logger Editor");
        titleLabel2.setFont(Font.font("Calibri", 20));
        titleLabel2.setStyle("-fx-font-weight: bold");
        
        //create the top section area and give it a title
        HBox editTop = new HBox();
        editTop.setAlignment(Pos.CENTER);
        editTop.getChildren().add(titleLabel2);
        editTop.setPadding(new Insets(5, 5, 0, 5)); // TOP / RIGHT / BOTTOM / LEFT
        editPane.setTop(editTop);
        
        editorButton.setOnAction(event -> {
        	primaryStage.setScene(editorScene);
    		primaryStage.show();
        });
        
        VBox editCenter = new VBox();
        
        // ===== add q1 label and box ===== // 
        
        HBox editOne = new HBox();
        VBox editQ1Box = new VBox();
        
        Label editQ1 = new Label("1. Select the Project");
        editQ1.setStyle("-fx-font-weight: bold");
        
        editQ1Box.getChildren().addAll(editQ1);
        
        ComboBox<String> projectBox2 = new ComboBox<String>();
        projectBox2.getItems().addAll("Development Project", "Business Project");
        editQ1Box.getChildren().addAll(projectBox2);
        
        editCenter.setPadding(new Insets(25, 25, 25, 25));
        editOne.getChildren().addAll(editQ1Box);
        
        // ===== add q2.a label and box ===== // 
        
        VBox editQ2aBox = new VBox();
        editQ2aBox.setPadding(new Insets(0, 0, 0, 100));
        
        Label editq2a = new Label("2.a. Clear this Project's Effort Log");
        editq2a.setStyle("-fx-font-weight: bold");
        
        Button clearEffortButton = new Button("Clear This Effort Log");
        editQ2aBox.getChildren().addAll(editq2a, clearEffortButton);
        
        editOne.getChildren().add(editQ2aBox);
        
        editCenter.getChildren().addAll(editOne);
        editPane.setCenter(editCenter);
                
        // === q2.b. === //
        
        Label editq2b = new Label("2.b. Select the Effort Log Entry to be modified and make it the Current Effort Log Entry");
        editq2b.setStyle("-fx-font-weight: bold");
        editq2b.setPadding(new Insets(15, 0, 0, 0));
        
        ComboBox<String> q2bBox = new ComboBox<>();
        editCenter.getChildren().addAll(editq2b, q2bBox);
        q2bBox.setMinWidth(400);
        
        projectBox2.valueProperty().addListener((observable, oldValue, newValue) -> {
        	if (newValue == null) {
        		projectBox2.setItems(nothing);
        	}
        	else {
            	ObservableList<String> newList = FXCollections.observableArrayList();
        		Connection con = null;
        		ResultSet resultSet = null;
				try {
					con = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
					String project_name = projectBox2.getValue() + "";
					String query = "SELECT * FROM logs WHERE project_name = '" + project_name + "'";
					resultSet = con.createStatement().executeQuery(query);
					while(resultSet.next()) {
						newList.add(resultSet.getString("id") +  " ; " + resultSet.getString("log_date") +  " ; " + resultSet.getString("start_time") +  " ; " + resultSet.getString("life_cycle_step") +  " ; " + resultSet.getString("effort_category"));
					}
					con.close();
				} catch (SQLException e1) {
					System.out.println("we broke it");
					e1.printStackTrace();
				}
				q2bBox.setItems(newList);
        	}
        });
        
        // ===== q3 ===== //
        
        Label editq3 = new Label("Modify the Current Effort Log Entry's attributes and press \"Update This Entry\" when finished");
        editq3.setStyle("-fx-font-weight: bold");
        editq3.setPadding(new Insets(15, 0, 0, 0));
        
        TextField editDate = new TextField();
        TextField startTimePicker = new TextField();
        TextField stopTimePicker = new TextField();
        
        Label editDateLabel = new Label("Date: ");
        editDateLabel.setStyle("-fx-font-weight: bold");
        Label startTimePickerLabel = new Label("Start Time: ");
        startTimePickerLabel.setPadding(new Insets(0, 0, 0, 10));
        startTimePickerLabel.setStyle("-fx-font-weight: bold");
        Label stopTimePickerLabel = new Label("Stop Time: ");
        stopTimePickerLabel.setPadding(new Insets(0, 0, 0, 10));
        stopTimePickerLabel.setStyle("-fx-font-weight: bold");
        
        HBox q3Row1 = new HBox();
        q3Row1.getChildren().addAll(editDateLabel, editDate, startTimePickerLabel, startTimePicker, stopTimePickerLabel, stopTimePicker);
        q3Row1.setPadding(new Insets(15, 0, 15, 0));
        
        editCenter.getChildren().add(q3Row1);
        
        Label lifeCycleLabel = new Label("Life Cycle Step:");
        lifeCycleLabel.setStyle("-fx-font-weight: bold");
        ComboBox<String> lifeCycleComboBox = new ComboBox<>();
        lifeCycleComboBox.getItems().addAll("Problem Understanding", "Conceptual Design Plan", "Requirements", "Conceptual Design", "Conceptual Design Review", "Detailed Design Plan", "Detailed Design/Prototype", "Detailed Design Review", "Implementation Plan", "Test Case Generation", "Solution Specification", "Solution Review", "Solution Implementation", "Unit/System Test", "Reflection", "Repository Update", "Planning", "Information Gathering");
        
        editCenter.getChildren().addAll(lifeCycleLabel, lifeCycleComboBox);
        
        HBox edit3a = new HBox();
        edit3a.setPadding(new Insets(15, 0, 15, 0));
        Label editEffortCategory = new Label("Effort Category");
        editEffortCategory.setStyle("-fx-font-weight: bold");
        ComboBox<String> editEffortBox = new ComboBox<>();
        editEffortBox.getItems().addAll("Plans", "Deliverables", "Interruptions", "Defects", "Other");
        
        VBox edit3aP1 = new VBox();
        edit3aP1.getChildren().addAll(editEffortCategory, editEffortBox);
        edit3a.getChildren().addAll(edit3aP1);
        
        Label editPlan = new Label("Plan:");
        editPlan.setStyle("-fx-font-weight: bold");
        
        ComboBox<String> editPlanBox = new ComboBox<>();
        editPlanBox.setMinWidth(400);
        
        VBox edit3aP2 = new VBox();
        edit3aP2.setPadding(new Insets(0, 0, 0, 30));
        edit3aP2.getChildren().addAll(editPlan, editPlanBox);
        edit3a.getChildren().addAll(edit3aP2);
        
        editCenter.getChildren().addAll(edit3a);
        
        Button updateButton = new Button("Update Record");
        editCenter.getChildren().addAll(updateButton);

        q2bBox.valueProperty().addListener((observable, oldValue, newValue) -> {
        	String log = q2bBox.getValue() + "";
        	int id = Character.getNumericValue(log.charAt(0));
        	Connection con = null;
    		ResultSet resultSet = null;
			try {
				con = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
				String query = "SELECT * FROM logs WHERE id = '" + id + "'";
				resultSet = con.createStatement().executeQuery(query);
				
				while(resultSet.next())
				{
					editDate.setText(resultSet.getString("log_date"));
					startTimePicker.setText(resultSet.getString("start_time"));
					stopTimePicker.setText(resultSet.getString("stop_time"));
					lifeCycleComboBox.setValue(resultSet.getString("life_cycle_step"));
				}
				
				con.close();
			} catch (SQLException e1) {
				System.out.println("we broke it 2");
				e1.printStackTrace();
			}
        });
        
     // === Defect Console === //
        
        BorderPane defectPane = new BorderPane();
        Scene defectScene = new Scene(defectPane, 800, 425);
        
        Label titleLabel3 = new Label("Defect Console");
        titleLabel3.setFont(Font.font("Calibri", 20));
        titleLabel3.setStyle("-fx-font-weight: bold");
                
        //create the top section area and give it a title
        HBox defectTop = new HBox();
        defectTop.setAlignment(Pos.CENTER);
        defectTop.getChildren().add(titleLabel3);
        defectTop.setPadding(new Insets(5, 5, 0, 5)); // TOP / RIGHT / BOTTOM / LEFT
        defectPane.setTop(defectTop);
        
        defectButton.setOnAction(event -> {
        	primaryStage.setScene(defectScene);
    		primaryStage.show();
        });
        
        VBox defectCenter = new VBox();
        
        HBox defectOne = new HBox();
        VBox defectQ1Box = new VBox();

        Label defectQ1 = new Label("1. Select the Project");
        defectQ1.setStyle("-fx-font-weight: bold");

        defectQ1Box.getChildren().addAll(defectQ1);

        ComboBox<String> projectBox3 = new ComboBox<String>();
        projectBox3.getItems().addAll("Development Project", "Business Project");
        defectQ1Box.getChildren().addAll(projectBox3);

        defectCenter.setPadding(new Insets(25, 25, 25, 25));
        defectOne.getChildren().addAll(defectQ1Box);

        // ===== add q2.a label and box ===== // 

        VBox defectQ2aBox = new VBox();
        defectQ2aBox.setPadding(new Insets(0, 0, 0, 100));

        Label defectQ2a = new Label("2.a. Clear this Project's Defect Log");
        defectQ2a.setStyle("-fx-font-weight: bold");

        Button clearDefectButton = new Button("Clear This Defect Log");
        defectQ2aBox.getChildren().addAll(defectQ2a, clearDefectButton);

        defectOne.getChildren().add(defectQ2aBox);

        defectCenter.getChildren().addAll(defectOne);
        defectPane.setCenter(defectCenter);
        
        // === q2 === //
        
        Label defectQ2bLabel = new Label("2.b. Select one of the following defects to make it the Current Defect or press \"Create a New Deect\"");
        defectQ2bLabel.setPadding(new Insets(15, 0, 0, 0));
        defectQ2bLabel.setStyle("-fx-font-weight: bold");
        
        defectCenter.getChildren().addAll(defectQ2bLabel);
        
        ComboBox<String> defectQ2bBox = new ComboBox<>();
        defectQ2bBox.setMinWidth(400);
        
        projectBox3.valueProperty().addListener((observable, oldValue, newValue) -> {
        	if (newValue == null) {
        		projectBox2.setItems(nothing);
        	}
        	else {
            	ObservableList<String> newList = FXCollections.observableArrayList();
        		Connection con = null;
        		ResultSet resultSet = null;
				try {
					con = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
					String project_name = projectBox3.getValue() + "";
					String query = "SELECT * FROM logs WHERE project_name = '" + project_name + "'";
					resultSet = con.createStatement().executeQuery(query);
					while(resultSet.next()) {
						newList.add(resultSet.getString("id") +  " ; " + resultSet.getString("log_date") +  " ; " + resultSet.getString("start_time") +  " ; " + resultSet.getString("life_cycle_step") +  " ; " + resultSet.getString("effort_category"));
					}
					con.close();
				} catch (SQLException e1) {
					System.out.println("we broke it");
					e1.printStackTrace();
				}
				defectQ2bBox.setItems(newList);
        	}
        });
        
        Button createNewDefectButton = new Button("Create a New Defect");
        
        HBox defect2b = new HBox();
        defect2b.setPadding(new Insets(0, 0, 15, 0));
        defect2b.getChildren().addAll(defectQ2bBox, createNewDefectButton);
        defect2b.setSpacing(50);
        
        defectCenter.getChildren().addAll(defect2b);
        
        HBox defectButtonBox = new HBox();
        Button updateDefect = new Button("Update Current Defect");
        Button deleteDefect = new Button("Delete the Current Defect");
        Button defectToEffort = new Button("Proceed to the Effort Log Console");
        
        defectButtonBox.getChildren().addAll(updateDefect, deleteDefect, defectToEffort);
        defectButtonBox.setSpacing(15);
        defectCenter.getChildren().add(defectButtonBox);
        
// === BAAZ JHAJ Building the Effort and Defect Log Page === //
        
        BorderPane tablePane = new BorderPane();
        Scene tableScene = new Scene(tablePane, 800, 425);

        logButton.setOnAction(event -> {
            primaryStage.setScene(tableScene);
            primaryStage.show();
        });

        Label titleLabel4 = new Label("Effort and Defect Logs");
        titleLabel4.setFont(Font.font("Calibri", 20));
        titleLabel4.setStyle("-fx-font-weight: bold");

        //create the top section area and give it a title
        HBox logTop = new HBox();
        logTop.setAlignment(Pos.CENTER);
        logTop.getChildren().add(titleLabel4);
        logTop.setPadding(new Insets(5, 5, 0, 5)); // TOP / RIGHT / BOTTOM / LEFT
        tablePane.setTop(logTop);

        VBox tableBox = new VBox();

        TableView effortTable = new TableView();
        TableColumn project_name = new TableColumn("Project Name");
        TableColumn log_date = new TableColumn("Log Date");
        TableColumn delta_time = new TableColumn("Delta Time");
        effortTable.getColumns().addAll(project_name, log_date, delta_time);

        tableBox.getChildren().add(effortTable);

        TableView defectTable = new TableView();
        TableColumn project_name1 = new TableColumn("Project Name");
        TableColumn log_date1 = new TableColumn("Log Date");
        TableColumn delta_time1 = new TableColumn("Delta Time");
        defectTable.getColumns().addAll(project_name1, log_date1, delta_time1);

        tableBox.getChildren().add(defectTable);

        tablePane.setCenter(tableBox);

        
        BorderPane pokerPane = new BorderPane();
        Scene pokerScene = new Scene(pokerPane, 800, 425);

//=====================planning poker - JefREY HSU==============//        
        Label PlanningPokerTitle = new Label("Planning Poker");
        PlanningPokerTitle.setFont(Font.font("Calibri", 20));
        PlanningPokerTitle.setStyle("-fx-font-weight: bold");
        
        //create the top section area and give it a title
        HBox PPTop = new HBox();
        PPTop.setAlignment(Pos.CENTER);
        PPTop.getChildren().add(PlanningPokerTitle);
        PPTop.setPadding(new Insets(5, 5, 0, 5)); // TOP / RIGHT / BOTTOM / LEFT
        pokerPane.setTop(PPTop);
        
        PlanningPokerButton.setOnAction(event -> {
        	primaryStage.setScene(pokerScene);
    		primaryStage.show();
        });
        
        VBox pokerCenter = new VBox();
        
        // ===== add q1 label and box ===== // 
        HBox pokerOne = new HBox();
        VBox pokerQ1Box = new VBox();
        pokerQ1Box.setSpacing(20);  // Set spacing between components

        Label pokerq1 = new Label("1. Setup: Create a Project");
        pokerq1.setStyle("-fx-font-weight: bold");

        pokerQ1Box.getChildren().addAll(pokerq1);

        TextField pokerProjectName = new TextField();
        HBox textInputRowC = new HBox(pokerProjectName);

        // 1b. User Story
        Label userStoryLabel = new Label("1a. User Story");
        userStoryLabel.setStyle("-fx-font-weight: bold");

        TextField userStoryTextField = new TextField();
        HBox userStoryRow = new HBox(userStoryTextField);

        // 1c. Keywords
        Label keywordsLabel = new Label("1b. Keywords");
        keywordsLabel.setStyle("-fx-font-weight: bold");

        TextField keywordsTextField = new TextField();
        HBox keywordsRow = new HBox(keywordsTextField);

        // Combine userStory and keywords in the same HBox
        HBox userStoryAndKeywordsBox = new HBox(userStoryLabel, userStoryRow, keywordsLabel, keywordsRow);

        pokerQ1Box.getChildren().addAll(textInputRowC, userStoryAndKeywordsBox);
        pokerOne.getChildren().addAll(pokerQ1Box);
        
        // ===== add q2.a label and box ===== // 
        
        pokerPane.setCenter(pokerCenter);
                
        // === 2. === //

     // === 2. First Round of Poker === //
     VBox pokerQ2bBox = new VBox();
     pokerQ2bBox.setSpacing(20);  // Set spacing between components
     pokerQ2bBox.setPadding(new Insets(20, 0, 0, 0));  // Set top padding to create space

     Label pokerq2b = new Label("2. First Round of Poker");
     pokerq2b.setStyle("-fx-font-weight: bold");

     // --- 2a.BAAZ JHAJ PROTOTYPE Historical Projects --- //
     Label historicalProjectsLabel = new Label("2a. Historical Projects");
     historicalProjectsLabel.setStyle("-fx-font-weight: bold");

     // Assuming you have a list of historical projects
     ObservableList<String> historicalProjects = FXCollections.observableArrayList("Business Project", "Development Project", "New Project");
     Vector<Integer> numericValues = new Vector<>();
     numericValues.add(10);
     numericValues.add(2);
     numericValues.add(5);

     
     ListView<String> historicalProjectsListView = new ListView<>(historicalProjects);
     historicalProjectsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

     HBox historicalProjectsRow = new HBox(historicalProjectsLabel, historicalProjectsListView);

     // --- 2b. Estimate --- //
     Label estimateLabel = new Label("    2b. Estimate:   ");
     estimateLabel.setStyle("-fx-font-weight: bold");
     

     // Create a label to display the calculated average
     Label averageLabel = new Label();
     averageLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
     HBox averageRow = new HBox(averageLabel);

     // Listen for changes in the selection model of historicalProjectsListView
     historicalProjectsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    	    // Calculate average based on selected items
    	    List<Integer> selectedIndices = historicalProjectsListView.getSelectionModel().getSelectedIndices();

    	    // Convert Vector to List for stream processing
    	    List<Integer> numericValuesList = new ArrayList<>(numericValues);

    	    double average = selectedIndices.stream().mapToDouble(i -> numericValuesList.get(i)).average().orElse(0.0);

    	    // Round down to the nearest integer using Math.floor
    	    int roundedAverage = (int) Math.floor(average);

    	    averageLabel.setText(String.valueOf(roundedAverage));  // Display the rounded average as an integer
    	});

     HBox estimateRow = new HBox(estimateLabel, averageRow);

     // --- 2c. Poker Card Weight --- //
     Label pokerCardWeightLabel = new Label("       2c. Poker Card Weight\t");
     pokerCardWeightLabel.setStyle("-fx-font-weight: bold");

     // Assuming you want a dropdown of numbers 1 through 10
     ObservableList<Integer> cardWeights = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
     ComboBox<Integer> cardWeightsComboBox = new ComboBox<>(cardWeights);


	HBox cardWeightsRow = new HBox(pokerCardWeightLabel, cardWeightsComboBox);
	
	HBox poker2Box = new HBox();
	
	Button generateCardButton = new Button("Generate Card");

	Map<String, Set<String>> projectKeywordsMap = new HashMap<>();
	

	// Sample keywords for existing projects
	Set<String> businessProjectKeywords = new HashSet<>(Arrays.asList("finance", "management", "strategy"));
	Set<String> developmentProjectKeywords = new HashSet<>(Arrays.asList("software", "programming", "coding"));

	// Adding sample keywords to the projectKeywordsMap for existing projects
	projectKeywordsMap.put("Business Project", businessProjectKeywords);
	projectKeywordsMap.put("Development Project", developmentProjectKeywords);
	

	// When the "Generate Card" button is clicked
	generateCardButton.setOnAction(event -> {
	    // Get values from the project name text field, keywords text field, and poker card weight dropdown
	    String projectName = pokerProjectName.getText();
	    Integer selectedCardWeight = cardWeightsComboBox.getValue();
	    String[] keywordsArray = keywordsTextField.getText().split("\\s+"); // Split keywords by spaces

	    // Store keywords for the project
	    Set<String> keywordsSet = new HashSet<>(Arrays.asList(keywordsArray));
	    projectKeywordsMap.put(projectName, keywordsSet);

	    // Update the list of historical projects and numeric values
	    historicalProjects.add(projectName);
	    numericValues.add(selectedCardWeight);

	    // Update the list view with the modified historical projects
	    historicalProjectsListView.setItems(historicalProjects);

	    // Clear the project name text field, keywords text field, user story text field, and card weight dropdown
	    pokerProjectName.clear();
	    keywordsTextField.clear();
	    userStoryTextField.clear();
	    cardWeightsComboBox.getSelectionModel().clearSelection();
	});

	// When keywords are entered in the text field
	keywordsTextField.textProperty().addListener((observable, oldValue, newValue) -> {
	    String[] enteredKeywords = newValue.toLowerCase().split("\\s+"); // Split entered keywords by spaces, convert to lowercase

	    // Filter historical projects based on entered keywords
	    List<String> filteredProjects;

	    if (newValue.isEmpty()) {
	        filteredProjects = new ArrayList<>(historicalProjects);
	    } else {
	        filteredProjects = projectKeywordsMap.entrySet().stream()
	                .filter(entry -> {
	                    Set<String> projectKeywords = entry.getValue();
	                    // Check if any entered keyword matches any project keyword
	                    return Arrays.stream(enteredKeywords).anyMatch(projectKeywords::contains);
	                })
	                .map(Map.Entry::getKey)
	                .collect(Collectors.toList());
	    }

	    // Update the list view to display filtered projects
	    historicalProjectsListView.setItems(FXCollections.observableArrayList(filteredProjects));
	});

     poker2Box.getChildren().addAll(historicalProjectsRow, estimateRow, cardWeightsRow, generateCardButton);
     
     pokerQ2bBox.getChildren().addAll(pokerq2b, poker2Box);

        
  // === 3. Subsequent Planning === //
     VBox pokerQ3Box = new VBox();
     pokerQ3Box.setSpacing(20);  // Set spacing between components
     pokerQ3Box.setPadding(new Insets(20, 0, 0, 0));  // Set top padding to create space

     Label pokerq3b = new Label("3. Subsequent Rounds");
     pokerq3b.setStyle("-fx-font-weight: bold");

     // --- 3a. Select Project --- //
     Label selectProjectLabel = new Label("3a. Select Project");
     selectProjectLabel.setStyle("-fx-font-weight: bold");

     ComboBox<String> projectComboBox = new ComboBox<>(historicalProjects);

     VBox selectProjectVBox = new VBox(selectProjectLabel, projectComboBox);

     // --- 3b. Effort Data --- //
     Label effortDataLabel = new Label("3b. Effort Data");
     effortDataLabel.setStyle("-fx-font-weight: bold");

     TextField effortDataTextField = new TextField();

     VBox effortDataVBox = new VBox(effortDataLabel, effortDataTextField);

     // --- 3c. Defect Data --- //
     Label defectDataLabel = new Label("3c. Defect Data");
     defectDataLabel.setStyle("-fx-font-weight: bold");

     TextField defectDataTextField = new TextField();

     VBox defectDataVBox = new VBox(defectDataLabel, defectDataTextField);

     // --- Finish Button --- //
     Label finishLabe = new Label("");
     Button finishButton = new Button("Finish");
     VBox finishBox = new VBox(finishLabe, finishButton);
     
     Vector<String> EffortData = new Vector<>();
     Vector<String> DefectData = new Vector<>();
     Map<String, ProjectData> projectDataMap = new HashMap<>();

     
     finishButton.setOnAction(event -> {
    	    String selectedProject = projectComboBox.getValue();
    	    String effortData = effortDataTextField.getText();
    	    String defectData = defectDataTextField.getText();

    	    // Update the project data for the selected project
    	    if (selectedProject != null && !selectedProject.isEmpty()) {
    	        ProjectData projectData = projectDataMap.getOrDefault(selectedProject, new ProjectData("", ""));
    	        projectData.setEffortData(effortData);
    	        projectData.setDefectData(defectData);
    	        projectDataMap.put(selectedProject, projectData);
    	    }

    	    // Clear the text fields and combo box selection
    	    effortDataTextField.clear();
    	    defectDataTextField.clear();
    	    projectComboBox.getSelectionModel().clearSelection();
    	});

    	// When a project is selected from the combo box
    	projectComboBox.setOnAction(event -> {
    	    String selectedProject = projectComboBox.getValue();

    	    // Display corresponding effort and defect data in the text fields
    	    if (selectedProject != null && !selectedProject.isEmpty()) {
    	        ProjectData projectData = projectDataMap.getOrDefault(selectedProject, new ProjectData("", ""));
    	        effortDataTextField.setText(projectData.getEffortData());
    	        defectDataTextField.setText(projectData.getDefectData());
    	    }
    	});

     // Combine VBox1, VBox2, VBox3, and the Finish button in an HBox
     HBox subsequenceRoundsHBox = new HBox(selectProjectVBox, effortDataVBox, defectDataVBox, finishBox);
     subsequenceRoundsHBox.setSpacing(20);

     pokerQ3Box.getChildren().addAll(pokerq3b, subsequenceRoundsHBox);

     pokerCenter.getChildren().addAll(pokerOne, pokerQ2bBox, pokerQ3Box);
//=====END OF JEFFREY HSU====//
    } 
    
    private boolean isValidInput(String input) { //SQL INJECT method
        return input.matches("^[a-zA-Z0-9]+$");
    }
    
    private boolean authenticate(String username, String password) { // password authetnitcation
        if (username.equals("admin") && password.equals("password"))
        	return true;
        return false; 
    }
    
    // =========== END JUSTIN BOYD ==================== //
    
    // BAAZ JHAJ
    
    private void catchOutOfBoundsAddress(int ar[]) {
        try {
            for (int i = 0; i <= ar.length; i++)
                System.out.print(ar[i]+" ");
        }
        catch (Exception e) {
            System.out.println("\nException caught");
        }
    }
    
    private void createNewEffortLog(String project_name, String log_date, String start_time, String stop_time, String delta_time, String life_cycle_step, String effort_category, String misc)
    {
    	Connection con = null;
    	try {
    	      con = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

    	      System.out.println("Connected!");

    	      PreparedStatement preparedStatement = con.prepareStatement(INSERT_EFFORT);
    	      
		      preparedStatement.setString(1, project_name);	
		      preparedStatement.setString(2, log_date);
		      preparedStatement.setString(3, start_time);
		      preparedStatement.setString(4, stop_time);
		      preparedStatement.setString(5, delta_time);
		      preparedStatement.setString(6, life_cycle_step);
		      preparedStatement.setString(7, effort_category);
		      preparedStatement.setString(8, misc);
	
		      System.out.println(preparedStatement);
		      preparedStatement.executeUpdate();
		      
		      con.close();
    	      
    	    } catch (SQLException ex) {
    	        throw new Error("Error ", ex);
    	    } finally {
    	      try {
    	        if (con != null) {
    	            con.close();
    	        }
    	      } catch (SQLException ex) {
    	          System.out.println(ex.getMessage());
    	      }
    	    }
    }
    
    // ====== BAAZ JHAJ END ===== //
    
//    private void comboAction(ActionEvent event) {
//
//        System.out.println(effortBox.getValue());
//
//    }

}


class ProjectData {
    private String effortData;
    private String defectData;

    public ProjectData(String effortData, String defectData) {
        this.effortData = effortData;
        this.defectData = defectData;
    }

    public String getEffortData() {
        return effortData;
    }

    public String getDefectData() {
        return defectData;
    }

    public void setEffortData(String effortData) {
        this.effortData = effortData;
    }

    public void setDefectData(String defectData) {
        this.defectData = defectData;
    }
}