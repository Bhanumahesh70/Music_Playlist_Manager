package controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dao.DBConnect;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;


public class ClientController {

	@FXML
	private Pane playlist_panel;
	
	@FXML
	private Pane panel1;
	
	@FXML
	private TextField playlistName_text;
	
	@FXML
	private TextField songName_text;
	
	
	// Declare DB objects
		DBConnect conn = null;
		Statement stmt = null;
		PreparedStatement prepstmt =null;
		ResultSet myRs = null;
	
	public ClientController() {
		conn = new DBConnect();
	}
	
	public void createPlaylist_click() {
		playlist_panel.setVisible(true);
		panel1.setVisible(false);
	}
	
	public void createPlaylist() {
		// INSERT INTO Playlist TABLE
				try {
				// Execute a query
					System.out.println("Inserting data into the playlist table...");
				stmt = conn.getConnection().createStatement();
				String sql = null;
				String songName = songName_text.getText();
				String playlistName = playlistName_text.getText();
				
				sql= "SELECT song_id FROM beatmusic_songs WHERE title = ? ";
				prepstmt = conn.getConnection().prepareStatement(sql);
				prepstmt.setString(1, songName);
				myRs = prepstmt.executeQuery();
				
				int song_id = 0;
				while(myRs.next()) {
					 song_id= myRs.getInt("song_id");
				}
				
				int user_id =1;
				// Include all object data to the database table
				sql = "INSERT INTO beatmusic_playlist(playlist_name,user_id,song_id) values (?,?,?)";
				prepstmt = conn.getConnection().prepareStatement(sql);
				prepstmt.setString(1, playlistName);
				prepstmt.setInt(2,user_id);
				prepstmt.setInt(3,song_id);
				prepstmt.executeUpdate();
				System.out.println("Playlist created");
		 
				conn.getConnection().close();
				} catch (SQLException se) {
				se.printStackTrace();
				}
	}

}
