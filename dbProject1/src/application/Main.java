package application;

import java.io.FileInputStream;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.PreparedStatement;

public class Main extends Application {

	Connection myConn = null;
	Statement Stmt = null;
	ResultSet Rs = null;
	BorderPane b1;
	BorderPane b2;
	BorderPane b3;
	BorderPane b4;
	Scene s3;
	Scene s4;
	Image img;
	ListView<String> list;
	ListView<String> list2;
	Button backButton = new Button("Back");
	Button backButton1 = new Button("Back");
	Button displayCustomers; // Make customers list
	Button delete = new Button("Delete from database");
	Scene s2;
	Button displayEmployee;

	public static void main(String[] args) throws Exception {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		list = new ListView<String>();
		list2 = new ListView<String>();

		primaryStage.setTitle("Bank Of Conor Database");

		// Login screen

		img = new Image(new FileInputStream("images/image.jpg"));
		ImageView ImageView = new ImageView(img);
		ImageView.setFitHeight(120);
		ImageView.setFitWidth(320);

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 45, 55, 25));

		Label userName = new Label("User Name:");
		grid.add(userName, 0, 1);

		TextField TextField = new TextField();
		grid.add(TextField, 1, 1);

		Label password = new Label("Password:");
		grid.add(password, 0, 2);

		PasswordField pwBox = new PasswordField();
		grid.add(pwBox, 1, 2);

		Button button = new Button("Sign in");
		HBox hbox = new HBox(10);
		hbox.setAlignment(Pos.BOTTOM_RIGHT);
		hbox.getChildren().add(button);
		grid.add(hbox, 1, 4);

		// Scene 2 Bank Database
		b1 = new BorderPane();
		// b1.setTop(ImageView);
		b1.setPadding(new Insets(20, 20, 20, 20));

		// Click button to connect to the database
		Button connect = new Button("Connect to DB");
		connect.setOnAction(e -> {
			dbConnector();
		});

		b2 = new BorderPane();
		b2.setPadding(new Insets(20, 20, 20, 20));

		b3 = new BorderPane();
		b3.setPadding(new Insets(20, 20, 20, 20));

		// Scenes
		Scene scene = new Scene(grid, 300, 300);
		s2 = new Scene(b1, 400, 400);
		s3 = new Scene(b2, 300, 300);
		s4 = new Scene(b3, 300, 300);

		// This will display all customers in the database
		displayCustomers = new Button("List of customers");
		displayCustomers.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				try {
					primaryStage.setScene(s3);
					backButton1 = new Button("Back to Main Menu");
					String returnSQL = get();

					list.getItems().add(returnSQL);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});

		// This will display all employees in the database
		displayEmployee = new Button("List of employees");
		displayEmployee.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				try {
					primaryStage.setScene(s4);
					backButton = new Button("Back to Main Menu");
					String returnSQL = getEmployee();

					list2.getItems().add(returnSQL);
				} catch (Exception e4) {
					// TODO Auto-generated catch block
					e4.printStackTrace();
				}
			}

		});

		// Create a table
		Button createTable = new Button("Create New Table"); // Update or create
		createTable.setOnAction(e -> {
			try {
				createTable();
			} catch (Exception e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
		});

		
		// Insert Into Table
		Button insertButton = new Button("Insert into the database");
		insertButton.setOnAction(e -> {
			try {
			 insert();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		});
		
		
		delete = new Button("Delete from the database");
		delete.setOnAction(e -> {
			try {
			deleteRow();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		});
		
		
		

		/*
		 * //Delete Row Button deleteButton = new Button("Delete table from database");
		 * deleteButton.setOnAction(e -> { try { DeleteRow(); } catch (Exception e2) {
		 * e2.printStackTrace(); } });
		 */

		// First VBox
		VBox box1 = new VBox(10);
		box1.getChildren().addAll(ImageView, connect, displayCustomers, displayEmployee, createTable, insertButton, delete); 
																													
																												
																														
		box1.setPadding(new Insets(20, 20, 20, 20));
		box1.setAlignment(Pos.CENTER);
		b1.setCenter(box1);

		// Second VBox
		VBox vb2 = new VBox(10);
		vb2.getChildren().addAll(list, backButton1);
		b2.setCenter(vb2);

		VBox vb3 = new VBox(10);
		vb3.getChildren().addAll(list2, backButton);
		b3.setCenter(vb3);

		// Button for second scene
		button.setOnAction(e -> primaryStage.setScene(s2));

		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		backButton.setOnAction(e -> primaryStage.setScene(s2));
		backButton1.setOnAction(e -> primaryStage.setScene(s2));

	}

	// Query and test connection
	public static Connection dbConnector() {

		try {

			// 1. Get a connection to database
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/bank";

			String username = "root";
			String password = "Katelyn2008";

			Class.forName(driver);

			Connection myConn = DriverManager.getConnection(url, username, password);
			System.out.println("Connected");
			return myConn;

		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	// How to select from a table
	public String get() throws Exception {
		String message = "";
		try {
			Connection myConn = dbConnector();
			PreparedStatement statement = myConn.prepareStatement("SELECT * from customer");

			ResultSet Rs = statement.executeQuery();

			System.out.println("List of customers: ");
			while (Rs.next()) {
				System.out.println(Rs.getString("full_name"));
				message = message + Rs.getString("full_name") + "\n";
			}

			System.out.println("\nRecords are shown");

		} catch (Exception e) {
			System.out.println(e);
		}
		return message;

	}

	public String getEmployee() throws Exception {
		String message1 = "";
		try {
			Connection myConn = dbConnector();
			PreparedStatement statement = myConn.prepareStatement("SELECT * from  employee");

			ResultSet Rs = statement.executeQuery();

			System.out.println("List of employees: ");
			while (Rs.next()) {
				System.out.println(Rs.getString("surname"));
				message1 = message1 + Rs.getString("surname") + "     " + Rs.getString("branch_name") + "\n";
			}

			System.out.println("\nRecords are shown");

		} catch (Exception e) {
			System.out.println(e);
		}
		return message1;

	}

	// Create a non-existing table
	public static void createTable() throws Exception {
		try {
			Connection myConn = dbConnector();
			PreparedStatement create = myConn
					.prepareStatement("CREATE TABLE IF NOT EXISTS manager (first_name varchar(30), "
							+ "last_name varchar(30), id_number int, PRIMARY KEY(id_number))");

			create.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			System.out.println("Table Created");
		}
	}
	
	// Insert into table
	public static void insert() throws Exception {
		String firstName = "Michael";
		String surName = "Collins";
		String id_number = "1916";
		String branch_name = "Clonakilty";
		try {

			Connection myConn = dbConnector();
			PreparedStatement posted = myConn.prepareStatement(
					"INSERT INTO manager (first_name,last_name,id_number,branch_name) VALUE('" + firstName + "'    ,'"
							+ surName + "'     ,'" + id_number + "'  ,  '" + branch_name + "')");

			posted.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			System.out.println("Insert Completed");

		}
	}
	

	  public static void deleteRow() {
		  String var3 = "1916"; 
		  try {
			  Connection myConn = dbConnector();
			  PreparedStatement delete = myConn.prepareStatement("DELETE FROM manager WHERE id_number ='" + var3 +"';"); 
			  delete.executeUpdate();
	  
	  } catch (Exception e) { System.out.println(e); } finally {
	  System.out.println("Row has been deleted"); } }
	 

}
