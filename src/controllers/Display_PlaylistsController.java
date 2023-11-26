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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import models.PlaylistModel;
import models.SongModel;

/*
 * Code for displaying all Playlists. Creating a List view to display the list
 * of all playlists and details.
 */
public class Display_PlaylistsController extends ClientController{


	// Declare DB objects
	DBConnect conn = null;
	Statement stmt = null;
	PreparedStatement prepstmt = null;
	ResultSet myRs = null;

	public Display_PlaylistsController() {
		conn = new DBConnect();
	}
	
	// Get user details
	int userId = ClientController.getUser();

	// Going back to home when back button is pressed
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
		private ListView<PlaylistModel> playlist_listview;

		@FXML
		private Button playlistView_btn;

		@FXML
		private Label playlist_detailsLabel;
		
		@FXML
		private TextArea songDeatils_textArea;
		
		@FXML
		private Pane displaySongs_Panel;
		
		@FXML
		private Pane playlists_panel;

		@FXML
		public void initialize() {
			
			//get playlist details from database 
			List<PlaylistModel> playlists = fetch_PlaylistFrom_Database();
			
			//Display the playlist titles trough the list view
			System.out.println("Displaying Playlists");
			playlist_listview.getItems().clear(); // Clear existing items
			playlist_listview.getItems().addAll( playlists);
			playlist_listview.getSelectionModel().selectedItemProperty()
	        .addListener((observable, oldPlaylist, newPlaylist) -> viewPlaylistDetails(newPlaylist));
		}

		List<PlaylistModel> fetch_PlaylistFrom_Database() {

			List<PlaylistModel> playlists = new ArrayList<>();

			// TODO: Connect to your database
			try {

				System.out.println("Fetching all playlists of the user from playlist table in database");
				stmt = conn.getConnection().createStatement();
				String sql = null;
				sql = "SELECT * FROM beatmusic_playlist WHERE user_id=" + userId;
				myRs = stmt.executeQuery(sql);
				
				List<Integer> songIds = new ArrayList<Integer>();
				while (myRs.next()) {
					String playlistName = myRs.getString("playlist_name");
					int userId = myRs.getInt("user_id");
					int songId = myRs.getInt("song_id");
					songIds.add(songId);
					playlists.add(new PlaylistModel(playlistName,userId,songIds));
					
				}
				stmt.close();
				myRs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("Fetching the playlists is complete");
			return playlists;
		}

		
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
		
		private void viewPlaylistDetails(PlaylistModel playlist) {
			
			displaySongs_Panel.setVisible(true);
			playlists_panel.setVisible(false);
			
			if (playlist != null) {
				title_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
				artist_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArtist()));
				album_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlbum()));
				duration_column.setCellValueFactory(
						cellData -> new SimpleIntegerProperty(cellData.getValue().getDuration()).asObject());
				
				List<Integer> songIds = playlist.getSongIds();
				
				// Connect to the database and retrieve data
				List<SongModel> songsFromPlaylist = new ArrayList<SongModel>();
				
				System.out.println("Fetching the songs from the playlist table in database");
				for (int i : songIds) {
					songsFromPlaylist.add(fetchSongsFromPlaylist(i));
				}
				// Populate the TableView with data
				songs_tableview.getItems().addAll(songsFromPlaylist);
				System.out.println("Displaying all songs");
			} else {
				playlist_detailsLabel.setText("Please select a playlist to view details.");
				songDeatils_textArea.setText("Select a playlist");
			}
		}

		SongModel fetchSongsFromPlaylist(int song_id) {
			SongModel songsFromPlaylist = null;

			// TODO: Connect to your database
			try {
				stmt = conn.getConnection().createStatement();
				String sql = null;
				sql = "SELECT * FROM beatmusic_songs WHERE song_id=" + song_id;
				myRs = stmt.executeQuery(sql);

				while (myRs.next()) {
					String title = myRs.getString("title");
					String artist = myRs.getString("artist");
					String album = myRs.getString("album");
					int duration = myRs.getInt("duration");

					songsFromPlaylist = new SongModel(title, artist, album, duration);
					
					//conn.getConnection().close();
				}
				stmt.close();
				myRs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return songsFromPlaylist;
		}

}
