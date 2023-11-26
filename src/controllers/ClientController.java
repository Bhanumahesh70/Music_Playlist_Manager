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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import models.PlaylistModel;
import models.SongModel;

public class ClientController {

	@FXML
	private Pane playlist_panel;

	@FXML
	private Pane panel1;

	@FXML
	private Pane allsongs_panel;
	
	@FXML
	private TextField playlistName_text;

	@FXML
	private TextField songName_text;
	
	@FXML
	private Pane displaySongs_Panel;
	
	@FXML
	private Pane playlists_panel;

	// Declare DB objects
	DBConnect conn = null;
	Statement stmt = null;
	PreparedStatement prepstmt = null;
	ResultSet myRs = null;

	public ClientController() {
		conn = new DBConnect();
	}

	// User details
	protected static int userId;

	public static void setUser(int user_id) {
		userId = user_id;
	}
	
	public static int getUser() {
		return userId;
	}

	@FXML
	public void displaySongs() {
		playlist_panel.setVisible(false);
		panel1.setVisible(false);
		allsongs_panel.setVisible(true);
		playlists_panel.setVisible(false);
		displaySongs_Panel.setVisible(false);
		try {
			AnchorPane root;
			root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/Display_AllSongsView.fxml"));
			Main.stage.setTitle("All Songs");
			Scene scene = new Scene(root);
			Main.stage.setScene(scene);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@FXML
	public void createPlaylist_click() {
		playlist_panel.setVisible(true);
		panel1.setVisible(false);
		allsongs_panel.setVisible(false);
		playlists_panel.setVisible(false);
		displaySongs_Panel.setVisible(false);
	}

	@FXML
	public void displayPlaylist_panel() {
		playlist_panel.setVisible(false);
		panel1.setVisible(false);
		allsongs_panel.setVisible(false);
		playlists_panel.setVisible(true);
		displaySongs_Panel.setVisible(false);
	   // initialize2();
	    
	    try {
			AnchorPane root;
			root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/Display_PlaylistsView.fxml"));
			Main.stage.setTitle("All Songs");
			Scene scene = new Scene(root);
			Main.stage.setScene(scene);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void createPlaylist() {
		// INSERT INTO Playlist TABLE
		try {
			// Execute a query
			System.out.println("Inserting data into the playlist table...");
			String sql = null;
			String songName = songName_text.getText();
			String playlistName = playlistName_text.getText();

			sql = "SELECT song_id FROM beatmusic_songs WHERE title = ? ";
			prepstmt = conn.getConnection().prepareStatement(sql);
			prepstmt.setString(1, songName);
			myRs = prepstmt.executeQuery();

			int song_id = 0;
			while (myRs.next()) {
				song_id = myRs.getInt("song_id");
			}

			int user_id = 1;
			// Include all object data to the database table
			sql = "INSERT INTO beatmusic_playlist(playlist_name,user_id,song_id) values (?,?,?)";
			prepstmt = conn.getConnection().prepareStatement(sql);
			prepstmt.setString(1, playlistName);
			prepstmt.setInt(2, user_id);
			prepstmt.setInt(3, song_id);
			prepstmt.executeUpdate();
			System.out.println("Playlist created");
			prepstmt.close();
			myRs.close();
			conn.getConnection().close();
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	/*
	 * Code for displaying all songs Creating a table view to display the list of
	 * sons and details
	 */

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
	
	@FXML
	public void initialize() {
		//initialize1();
		//initialize2();
		
	}
	
	@FXML
	public void initialize1() {
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
