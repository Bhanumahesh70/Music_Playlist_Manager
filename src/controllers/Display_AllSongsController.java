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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import models.SongModel;

public class Display_AllSongsController {
	/*
	 * Code for displaying all songs Creating a table view to display the list of
	 * sons and details
	 */

	@FXML
	private Button backButton;
	
	@FXML
	private Label label;
	
	@FXML
	private TableView<SongModel> songs_tableview;

	@FXML
	private TableColumn<SongModel, String> title_column;

	@FXML
	private TableColumn<SongModel, String> artist_column;

	@FXML
	private TableColumn<SongModel, String> album_column;

	@FXML
	private TableColumn<SongModel, Integer> duration_column;
	
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
	public void initialize() {
		title_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
		artist_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArtist()));
		album_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlbum()));
		duration_column.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(cellData.getValue().getDuration()).asObject());

		// Connect to the database and retrieve data
		List<SongModel> songs = fetchDataFromDatabase();

		// Populate the TableView with data
		songs_tableview.getItems().addAll(songs);
		System.out.println("Displaying all songs");
	}

	private List<SongModel> fetchDataFromDatabase() {
		List<SongModel> songs = new ArrayList<>();

		// TODO: Connect to your database
		try {

			System.out.println("Fetching all songs from songs table in database");
			stmt = conn.getConnection().createStatement();
			String sql = null;
			sql = "SELECT * FROM beatmusic_songs";
			myRs = stmt.executeQuery(sql);

			while (myRs.next()) {
				String title = myRs.getString("title");
				String artist = myRs.getString("artist");
				String album = myRs.getString("album");
				int duration = myRs.getInt("duration");

				songs.add(new SongModel(title, artist, album, duration));
				
			}
			stmt.close();
			myRs.close();
			//conn.getConnection().close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return songs;
	}
}
