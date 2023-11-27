package models;

import dao.DBConnect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistTable_DatabaseModel {

    public static List<PlaylistModel> fetchPlaylistsFromDatabase(int userId) throws SQLException {
        List<PlaylistModel> playlists = new ArrayList<>();

        try (PreparedStatement stmt = DBConnect.getConnection().prepareStatement(
                "SELECT * FROM beatmusic_playlist WHERE user_id = ?")) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String playlistName = rs.getString("playlist_name");
                    int songId = rs.getInt("song_id");

                    boolean playlistPresent = false;
                    for (PlaylistModel playlist : playlists) {
                        if (playlist.getPlaylistName().equals(playlistName)) {
                            playlist.addSongId(songId);
                            playlistPresent = true;
                            break;
                        }
                    }
                    if (!playlistPresent) {
                        List<Integer> songIds = new ArrayList<>();
                        songIds.add(songId);
                        playlists.add(new PlaylistModel(playlistName, userId, songIds));
                    }
                }
            }
        }

        return playlists;
    }
    

	public static List<SongModel> fetchSongsFromPlaylist(PlaylistModel playlist){
		List<SongModel> songsFromPlaylist = new ArrayList<SongModel>();
		List<Integer> songIds = playlist.getSongIds();
		for (int i : songIds) {
			songsFromPlaylist.add(SongTable_DatabaseModel.fetchSongFromDatabase(i));
		}
		
		return songsFromPlaylist;
	}
}
