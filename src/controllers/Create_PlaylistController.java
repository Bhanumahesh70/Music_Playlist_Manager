package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import application.Main;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import models.PlaylistTable_DatabaseModel;
import models.SongModel;
import models.SongTable_DatabaseModel;

public class Create_PlaylistController extends ClientController {
	
	@FXML
	private TextField playlistName_text;

	@FXML
	private TextField songName_text;
	
	@FXML
	private Pane enterPlaylistTile_pane;
	
	@FXML
	private Pane AddSongsToPlaylist_pane;
	
	@FXML
	private Label playListCreated_label;
	
	@FXML
	private Label playlistTitleError_label;
	
	@FXML
	private Label addSongLabel;
	
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
	
	// Get user details
		int userId = ClientController.getUser();
		
		
	@FXML
	public void createPlaylist() {
		// INSERT INTO Playlist TABLE
		String playlistName = playlistName_text.getText();
		playlistTitleError_label.setText(" ");
		if(playlistName.equals("")) {
			String s = "The playlist title cannot be empty\nEnter a playlist name";
			playlistTitleError_label.setText(s);
			return;
		}
		if(PlaylistTable_DatabaseModel.isPlaylistPresent(playlistName, userId)){
			String s = "The playlist title is already present\nEnter a new name a create a new playlist";
			playlistTitleError_label.setText(s);
		}
		else {
		PlaylistTable_DatabaseModel.createEmptyPlaylist(playlistName,userId);
		enterPlaylistTile_pane.setVisible(false);
		AddSongsToPlaylist_pane.setVisible(true);
		String s = "Playlist '"+playlistName+"' is created";
		playListCreated_label.setText(s);
		}	
	}
	
	@FXML
	public void initialize() {
		title_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
		artist_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArtist()));
		album_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlbum()));
		duration_column.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(cellData.getValue().getDuration()).asObject());

		// Connect to the database and retrieve data
		List<SongModel> songs = null;
		try {
			songs = SongTable_DatabaseModel.fetchSongsFromDatabase();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Populate the TableView with data
		songs_tableview.getItems().addAll(songs);
		System.out.println("Displaying all songs");
	}
	
	@FXML
    public void addSongButtonClicked(ActionEvent event) {
        // Get the selected song from the TableView
        SongModel selectedSong = songs_tableview.getSelectionModel().getSelectedItem();

        // Update the label with the selected song information
        if (selectedSong != null) {
        	String playlistName = playlistName_text.getText();
        	int songId = selectedSong.getId();
        	
        	if(PlaylistTable_DatabaseModel.isSongPresentinPlaylist(playlistName,userId,songId)) {
        		
        		addSongLabel.setText("The song '" + selectedSong.getTitle()+"'\nis already in the playlist");
        		
        	}else {
      
        		addSongLabel.setText("The song '" + selectedSong.getTitle()+"'\nis added to the playlist");
            	PlaylistTable_DatabaseModel.addSongToPlaylist(playlistName,userId,songId);
        	}
        	
        } else {
        	addSongLabel.setText("No song is selected");
        }
    }
	
	@FXML
	public void backToEnterPlaylistTitle() {
		enterPlaylistTile_pane.setVisible(true);
		AddSongsToPlaylist_pane.setVisible(false);
	}

	@FXML
	public void addSongsToPlaylist() {
		
	}
	
	
}
