package models;
import java.util.ArrayList;
import java.util.List;

public class PlaylistModel {
    private String playlistName;
    private UserModel owner;
    private List<SongModel> songs;

    public PlaylistModel(String playlistName, UserModel owner) {
        this.playlistName = playlistName;
        this.owner = owner;
        this.songs = new ArrayList<>();
    }
    
    public PlaylistModel(String playlistName) {
    	this.playlistName = playlistName;
    }
    // Getters and setters

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public UserModel getOwner() {
        return owner;
    }

    public void setOwner(UserModel owner) {
        this.owner = owner;
    }

    public List<SongModel> getSongs() {
        return songs;
    }

    public void setSongs(List<SongModel> songs) {
        this.songs = songs;
    }

    // Methods
    public void addSong(SongModel song) {
        songs.add(song);
    }

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
}
