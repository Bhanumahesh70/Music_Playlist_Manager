package models;

import dao.DBConnect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PlaylistTable_DatabaseModel {

	public static List<PlaylistModel> fetchPlaylistsFromDatabase(int userId) {
		List<PlaylistModel> playlists = new ArrayList<>();

		try {

			PreparedStatement stmt = DBConnect.getConnection()
					.prepareStatement("SELECT * FROM beatmusic_playlist WHERE user_id = ?");

			stmt.setInt(1, userId);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String playlistName = rs.getString("playlist_name");
				int songId = rs.getInt("song_id");

				boolean playlistPresent_inList = false;
				for (PlaylistModel playlist : playlists) {
					if (playlist.getPlaylistName().equals(playlistName)) {
						playlist.addSongId(songId);
						playlistPresent_inList = true;
						break;
					}
				}
				if (!playlistPresent_inList) {
					List<Integer> songIds = new ArrayList<>();
					songIds.add(songId);
					playlists.add(new PlaylistModel(playlistName, userId, songIds));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return playlists;
	}

	/*
	public static List<SongModel> fetchSongsFromPlaylist(PlaylistModel playlist) {
		List<SongModel> songsFromPlaylist = new ArrayList<SongModel>();
		List<Integer> songIds = playlist.getSongIds();
		for (int i : songIds) {
			songsFromPlaylist.add(SongTable_DatabaseModel.fetchSongFromDatabase(i));
		}

		return songsFromPlaylist;
	}
	*/
	
	public static List<SongModel> fetchSongsFromPlaylist(PlaylistModel playlist, int userId) {
		List<SongModel> songsFromPlaylist = new ArrayList<SongModel>();
		try {

			PreparedStatement stmt = DBConnect.getConnection()
					.prepareStatement("SELECT * FROM beatmusic_playlist WHERE playlist_name =? AND user_id = ?");
			
			String playlistName = playlist.getPlaylistName();
			stmt.setString(1, playlistName);
			stmt.setInt(2, userId);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int songId = rs.getInt("song_id");
				songsFromPlaylist.add(SongTable_DatabaseModel.fetchSongFromDatabase(songId));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return songsFromPlaylist;
	}
	
	
	
	
	
	
	public static void insertIntoPlaylistDatabase(String playlistName, int user_id, int song_id) {
		try {
			
			String sql = "INSERT INTO beatmusic_playlist(playlist_name,user_id,song_id) values (?,?,?)";
			PreparedStatement stmt = DBConnect.getConnection().prepareStatement(sql);
			stmt.setString(1, playlistName);
			stmt.setInt(2, user_id);
			stmt.setInt(3, song_id);
			stmt.executeUpdate();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addSongToPlaylist(String playlistName, int songId, int userId) {
		try {
			
			if(isPlaylistEmpty(playlistName,userId)) {
				String sql = "UPDATE beatmusic_playlist SET song_id = ? WHERE playlist_name=? AND user_id =?";
				PreparedStatement stmt = DBConnect.getConnection().prepareStatement(sql);
				stmt.setInt(1, songId);
				stmt.setString(2, playlistName);
				stmt.setInt(3, userId);
				stmt.executeUpdate();
				stmt.close();
			}else {
				
				insertIntoPlaylistDatabase(playlistName,userId,songId);
			}
			
			System.out.println("The song with songId '"+songId+"' is added to the playlist '"+playlistName+"'" );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void createEmptyPlaylist(String playlistName, int user_id) {
		int song_id =-1;
		insertIntoPlaylistDatabase(playlistName,user_id,song_id);
		System.out.println("New Paylist '"+playlistName+"' is created and added to database");
	}
	
	public static boolean isPlaylistPresent(String playlistName, int user_id) {
		boolean isPlaylistPresent =false;
		try {
			String sql = "SELECT * FROM beatmusic_playlist";
			Statement stmt = DBConnect.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String playlist_name= rs.getString("playlist_name");
				if(playlist_name.equals(playlistName)){
					isPlaylistPresent =true;
					break;
				}
			}
			stmt.close();
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isPlaylistPresent;
		
	}
	public static boolean isPlaylistEmpty(String playlistName,int userId) {
		boolean isEmptyPlaylist = false;
		try {
			String sql = "SELECT * FROM beatmusic_playlist WHERE playlist_name=? AND user_id =?";
			PreparedStatement stmt = DBConnect.getConnection().prepareStatement(sql);
			stmt.setString(1, playlistName);
			stmt.setInt(2, userId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				int song_id = rs.getInt("song_id");
				if(song_id==-1)
				{
					isEmptyPlaylist = true;		
					System.out.println("The playlist is empty from db");
				}
				else {
					System.out.println("The playlist is not empty from db");
					break;
				}
			}
			stmt.close();
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isEmptyPlaylist;
	}
	
	public static boolean isSongPresentinPlaylist(String playlistName,int userId, int songId) {
		boolean isSongPresentinPlaylist = false;
		try {
			String sql = "SELECT * FROM beatmusic_playlist WHERE playlist_name=? AND user_id =?";
			PreparedStatement stmt = DBConnect.getConnection().prepareStatement(sql);
			stmt.setString(1, playlistName);
			stmt.setInt(2, userId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				int song_id = rs.getInt("song_id");
				if(song_id== songId)
				{
					isSongPresentinPlaylist = true;		
					System.out.println("The song is present in the playlist");
					break;
				}
			}
			stmt.close();
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSongPresentinPlaylist;
	}
	
	public static void deletePlaylist(String playlistName,int userId) {
		
		try {
			String sql = "DELETE FROM beatmusic_playlist WHERE playlist_name = ? AND user_id = ?";
			PreparedStatement stmt = DBConnect.getConnection().prepareStatement(sql);
			stmt.setString(1, playlistName);
			stmt.setInt(2, userId);
			stmt.executeUpdate();
			stmt.close();
			
			System.out.println("The Playlist '"+ playlistName+ "'is deleted from database");
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	
		
	}
	
	
	
	

}
