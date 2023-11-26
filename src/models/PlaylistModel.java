package models;
import java.util.ArrayList;
import java.util.List;

public class PlaylistModel {
    private String playlistName;
    private int userId;
    private List<Integer> songIds;
    //private int songId;
    
    
    //private List<SongModel> songs;

    public PlaylistModel(String playlistName, int userId, List<Integer> songIds) {
        this.playlistName = playlistName;
        this.userId = userId;
        this.songIds = songIds;
    }
    
    // Getters and setters

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int UserId) {
        this.userId = UserId;
    }

    public List<Integer> getSongIds() {
        return songIds;
    }

    public void setSongIds(List<Integer> songIds ) {
        this.songIds = songIds;
    }
    
    @Override
    public String toString() {
        return playlistName;
    }

    // Methods
   
    public void addSongId(int song) {
    	songIds.add(song);
    }

    /*
    public void removeSong(SongModel song) {
        songs.remove(song);
    }

    public int getPlaylistDuration() {
        int totalDuration = 0;
        for (SongModel song : songs) {
            totalDuration += song.getDuration();
        }
        return totalDuration;
    }
    */
}
