package controllers;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import application.Main;
import dao.DBConnect;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import models.SongModel;
import models.SongTable_DatabaseModel;

public class Display_AllSongsController {
	/*
	 * Code for displaying all songs Creating a table view to display the list of
	 * songs and details
	 */

	@FXML
	private Button backButton;
	
	@FXML
	private Label label;
	
	@FXML
	private Label songPlaying_Label;
	
	@FXML
	private static TableView<SongModel> songs_tableview;

	@FXML
	private static TableColumn<SongModel, String> title_column;

	@FXML
	private static TableColumn<SongModel, String> artist_column;

	@FXML
	private static TableColumn<SongModel, String> album_column;

	@FXML
	private static TableColumn<SongModel, Integer> duration_column;
	
	// Declare DB objects
		DBConnect conn = null;
		Statement stmt = null;
		PreparedStatement prepstmt = null;
		ResultSet myRs = null;

		public Display_AllSongsController() {
			conn = new DBConnect();
		}
	
		@FXML
		public void displayHome() {
			try {
				AnchorPane root;
				root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/ClientView.fxml"));
				Main.stage.setTitle("Client View");
				Scene scene = new Scene(root);
				Main.stage.setScene(scene);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@FXML
	    public void playButtonClicked(ActionEvent event) {
	        // Get the selected song from the TableView
	        SongModel selectedSong = songs_tableview.getSelectionModel().getSelectedItem();

	        // Update the label with the selected song information
	        if (selectedSong != null) {
	        	songPlaying_Label.setText("Now Playing: " + selectedSong.getTitle());
	        } else {
	        	songPlaying_Label.setText("No song is selected");
	        }
	    }
		
	@FXML
	public static void initialize() {
		title_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
		artist_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArtist()));
		album_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlbum()));
		duration_column.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(cellData.getValue().getDuration()).asObject());

		// Connect to the database and retrieve data
		List<SongModel> songs = null;
		try {
			songs = SongTable_DatabaseModel.fetchSongsFromDatabase();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Populate the TableView with data
		songs_tableview.getItems().addAll(songs);
		System.out.println("Displaying all songs");
	}

}
