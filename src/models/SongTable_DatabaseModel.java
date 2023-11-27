package models;

import dao.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SongTable_DatabaseModel {

    public static ObservableList<SongModel> fetchSongsFromDatabase() throws SQLException {
        ObservableList<SongModel> songs = FXCollections.observableArrayList();

        try (PreparedStatement stmt = DBConnect.getConnection().prepareStatement("SELECT * FROM beatmusic_songs");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                String album = rs.getString("album");
                int duration = rs.getInt("duration");

                songs.add(new SongModel(title, artist, album, duration));
            }
        }

        return songs;
    }
    
    public static SongModel fetchSongsFromPlaylist(int song_id) {
		SongModel songsFromPlaylist = null;

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
