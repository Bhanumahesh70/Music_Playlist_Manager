package models;

import dao.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SongTable_DatabaseModel {

	public static ObservableList<SongModel> fetchSongsFromDatabase() {
		ObservableList<SongModel> songs = FXCollections.observableArrayList();

		try {
			PreparedStatement stmt = DBConnect.getConnection().prepareStatement("SELECT * FROM beatmusic_songs");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String title = rs.getString("title");
				String artist = rs.getString("artist");
				String album = rs.getString("album");
				int duration = rs.getInt("duration");

				songs.add(new SongModel(title, artist, album, duration));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return songs;
	}

	public static SongModel fetchSongFromDatabase(int song_id) {
		SongModel song = null;

		// TODO: Connect to your database
		try {
			Statement stmt = DBConnect.getConnection().createStatement();
			String sql = null;
			sql = "SELECT * FROM beatmusic_songs WHERE song_id=" + song_id;
			ResultSet myRs = stmt.executeQuery(sql);

			while (myRs.next()) {
				String title = myRs.getString("title");
				String artist = myRs.getString("artist");
				String album = myRs.getString("album");
				int duration = myRs.getInt("duration");

				song = new SongModel(title, artist, album, duration);

				// conn.getConnection().close();
			}
			stmt.close();
			myRs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return song;
	}
	
	public static void insertSongInToDatabase(SongModel song) {
		try {
			
			String sql = "INSERT INTO beatmusic_songs (title, artist, album, duration) VALUES (?,?,?,?)";

			PreparedStatement stmt = DBConnect.getConnection().prepareStatement(sql);
			stmt.setString(1,song.getTitle());
			stmt.setString(2, song.getArtist());
			stmt.setString(3, song.getAlbum());
			stmt.setInt(4, song.getDuration());
			stmt.executeUpdate();
			stmt.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}