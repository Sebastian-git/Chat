package client;

import java.util.ArrayList;

/*
 * Create a GUI for each client that displays all
 * messages received by the server.
 */

import javax.swing.JOptionPane;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GUI extends Application implements EventHandler<ActionEvent> {

	// Client Object
	private Client client;

	// Pop up window
	private String username;

	// Main window
	private Button exit;
	private TextField input;
	private TextArea display;
	private Stage window;
	private Scene scene1;
	
	// Online display
	public TextArea onlineDisplay;
	public ArrayList<String> allUsers = new ArrayList<String>();

	public static void main(String args[]) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		window = primaryStage;
		window.setTitle("Chat");
		
		// Text Area chat display
		display = new TextArea();
		display.setPrefColumnCount(40);
		display.setPrefRowCount(30);
		display.setEditable(false);
		display.setWrapText(true);
		display.textProperty().addListener(new ChangeListener<Object>() {
		    @Override
		    public void changed(ObservableValue<?> observable, Object oldValue,
		            Object newValue) {
		        onlineDisplay.setScrollTop(Double.MAX_VALUE); 
		    }
		});
		
		// Text Area online users
		onlineDisplay = new TextArea();
		onlineDisplay.setText("Online:");
		onlineDisplay.setPrefColumnCount(10);
		onlineDisplay.setPrefRowCount(10);
		onlineDisplay.setEditable(false);
		onlineDisplay.setWrapText(true);

		// Text Field
		input = new TextField();
		input.setPromptText("Type here...");
		input.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					String fullData = username + "> " + input.getText();
					display.appendText(fullData + "\n");
					client.send(input.getText());
					input.clear();
				}
			}
		});

		// Buttons
		exit = new Button("Exit");
		exit.setOnAction(this);
		exit.setMaxWidth(500);

		// GridPane configuration
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setHgap(3);
		grid.setVgap(3);
		grid.getChildren().addAll(onlineDisplay, exit, input, display);

		// GridPane constraints
		GridPane.setConstraints(display, 0, 0);
		GridPane.setConstraints(input, 0, 1);
		GridPane.setConstraints(exit, 1, 1);
		GridPane.setConstraints(onlineDisplay, 1, 0);

		// Scenes
		scene1 = new Scene(grid, 700, 500);
		grid.setStyle("-fx-background: black;");
		window.setScene(scene1);
		window.setResizable(false);
		window.show();
		window.setOnCloseRequest((WindowEvent event1) -> {
			System.exit(0);
		});

		// Background colors
		Region region = (Region) display.lookup(".content");
		Region region2 = (Region) onlineDisplay.lookup(".content");
		
		region.setStyle("-fx-background-color: black;");
		region2.setStyle("-fx-background-color: black;");
		
		
		exit.setStyle("-fx-text-fill: #00FF00; -fx-background-color: black; -fx-border-color: #00FF00;");
		display.setStyle("-fx-text-inner-color: #00FF00; -fx-background-color: #00FF00;");
		onlineDisplay.setStyle("-fx-text-inner-color: #00FF00; -fx-background-color: #00FF00;");
		input.setStyle("-fx-control-inner-background: black; -fx-text-inner-color: #00FF00; -fx-prompt-text-fill: #00FF00;");
		
		// Client object
		client = new Client(this);
		client.start();

		// Setting focus to text field
		input.requestFocus();

	}

	public void promptUsername() {
		do {
			username = JOptionPane.showInputDialog("Username: ");
			if (username == null) {
				System.exit(0);
			}
		} while (username.trim().equals(""));

	}

	// Updates Text Field screen
	public void displayMessage(String message) {
		display.appendText(message + "\n");
	}

	public String getUsername() {
		return username;
	}
	
	public void updateUsers() {
		onlineDisplay.setText("Online:\n");
		for (int i = 0; i < allUsers.size(); i++) {
			onlineDisplay.appendText(allUsers.get(i) + "\n");
		}
	}
	
	// Events
	public void handle(ActionEvent event) {
		if (event.getSource() == exit) {
			System.exit(0);
		}
	}

}
