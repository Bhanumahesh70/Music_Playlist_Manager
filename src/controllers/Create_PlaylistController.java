package controllers;

import java.io.IOException;
import java.sql.SQLException;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class Create_PlaylistController extends ClientController {
	
	@FXML
	private TextField playlistName_text;

	@FXML
	private TextField songName_text;
	
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

}
