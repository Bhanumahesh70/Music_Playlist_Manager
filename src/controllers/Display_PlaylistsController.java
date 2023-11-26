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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import models.PlaylistModel;
import models.SongModel;

public class Display_PlaylistsController extends ClientController{
	
	/*
	 * Code for displaying all Playlists. Creating a List view to display the list
	 * of all playlists and details.
	 */

	// Declare DB objects
	DBConnect conn = null;
	Statement stmt = null;
	PreparedStatement prepstmt = null;
	ResultSet myRs = null;

	public Display_PlaylistsController() {
		conn = new DBConnect();
	}
	
	int userId = ClientController.getUser();

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
		private ListView<String> playlist_listview;

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

		// Add methods and event handlers here
		
		@FXML
		public void initialize() {
			List<PlaylistModel> playlists = fetchPlaylistFromDatabase();
			// Populate the ListView with fetched playlists
			List<String> playlist_titles = new ArrayList<String>();
			for(PlaylistModel playlist:playlists) {
				
				String playlistTitle = playlist.getPlaylistName();
				playlist_titles.add(playlistTitle);
			}
			System.out.println("Displaying Playlists");
			playlist_listview.getItems().clear(); // Clear existing items
			playlist_listview.getItems().addAll( playlist_titles);
			// Set an event listener for the ListView selection change
			playlist_listview.getSelectionModel().selectedItemProperty()
					.addListener((observable, oldValue, newValue) -> viewPlaylistDetails(newValue));
		}

		List<PlaylistModel> fetchPlaylistFromDatabase() {

			List<PlaylistModel> playlists = new ArrayList<>();

			// TODO: Connect to your database
			try {

				System.out.println("Fetching all playlists of the user from playlist table in database");
				stmt = conn.getConnection().createStatement();
				String sql = null;
				sql = "SELECT * FROM beatmusic_playlist WHERE user_id=" + userId;
				myRs = stmt.executeQuery(sql);

				while (myRs.next()) {
					String playlistName = myRs.getString("playlist_name");
					// int songId = myRs.getInt("song_id");
					playlists.add(new PlaylistModel(playlistName));
					
				}
				stmt.close();
				myRs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("Fetching the playlists is complete");
			return playlists;
		}

		
		private void viewPlaylistDetails(String playlistName) {
			
			displaySongs_Panel.setVisible(true);
			playlists_panel.setVisible(false);
			
			if (playlistName != null) {
				// Fetch playlist details from the database based on the selected playlist
				String playlistDetails = fetchDetailsFromDatabase(playlistName);
				//playlist_detailsLabel.setText(playlistDetails);
				songDeatils_textArea.setText("The song details are: "+playlistDetails);
			} else {
				playlist_detailsLabel.setText("Please select a playlist to view details.");
				songDeatils_textArea.setText("Select a playlist");
			}
		}

		@SuppressWarnings("null")
		private String fetchDetailsFromDatabase(String playlistName) {
			// Sample implementation; replace with your actual logic

			//String playlistName = selectedPlaylist.getPlaylistName();
			String songDetails = null;
			StringBuilder sc = new StringBuilder();

			try {
				stmt = conn.getConnection().createStatement();
				System.out.println("Fetching song details for playlist "+playlistName);
				String sql = "SELECT * FROM beatmusic_playlist WHERE playlist_name ='" + playlistName+"'";
				myRs = stmt.executeQuery(sql);
				List<Integer> songIds = new ArrayList<Integer>();
				// List<SongModel> songsFromPlaylist = fetchSongsFromPlaylist();
				while (myRs.next()) {
					songIds.add(myRs.getInt("song_id"));
				}
				List<SongModel> songsFromPlaylist = new ArrayList<SongModel>();
				
				System.out.println("Fetching the songs from the playlist table in database");
				for (int i : songIds) {
					songsFromPlaylist.add(fetchSongsFromPlaylist(i));
				}

				
				sc.append("Details for " +playlistName + ":\n");
				for (SongModel song : songsFromPlaylist) {

					String title = song.getTitle();
					String Album = song.getAlbum();
					String Artist = song.getArtist();

					String s = title + " - " + Artist + " - " + Album + "\n";
					sc.append(s);

				}
				songDetails = sc.toString();
				stmt.close();
				myRs.close();
				//conn.getConnection().close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			System.out.println("Son details are: "+ songDetails);
				return songDetails;
			

		}
		/*
		 * @FXML private void viewPlaylistDetails() { String selectedPlaylist =
		 * playlist_listview.getSelectionModel().getSelectedItem(); if (selectedPlaylist
		 * != null) { // Fetch playlist details and update detailsLabel String
		 * playlistDetails = "Details for " + selectedPlaylist + ":\n" +
		 * "Song 1 - Artist 1 - Album 1\n" + "Song 2 - Artist 2 - Album 2\n" +
		 * "Song 3 - Artist 3 - Album 3";
		 * 
		 * playlist_detailsLabel.setText(playlistDetails); } else {
		 * playlist_detailsLabel.setText("Please select a playlist to view details."); }
		 * }
		 */

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
	//}

}
