package controllers;

import java.io.IOException;
import java.util.List;

import application.Main;
import javafx.beans.property.SimpleBooleanProperty;
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
import models.PlaylistModel;
import models.PlaylistTable_DatabaseModel;
import models.SongModel;
import models.SongTable_DatabaseModel;
import models.UserModel;
import models.UserTable_DatabaseModel;

public class AdminController {

	@FXML
	private Pane homePane;

	@FXML
	private Pane songsPane;

	@FXML
	private Pane usersPane;

	@FXML
	private Pane addSongsPane;

	@FXML
	private TextField songTitleText;

	@FXML
	private TextField albumText;

	@FXML
	private TextField artistText;

	@FXML
	private TextField durationText;

	@FXML
	private Label songAddedLabel;

	@FXML
	private Label userModifyLabel;
	
	@FXML
	private Label playSongLabel;

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
	private TableView<UserModel> usersTable;

	@FXML
	private TableColumn<UserModel, String> userNameColumn;

	@FXML
	private TableColumn<UserModel, Boolean> isAdminColumn;

	@FXML
	public void displaySongs() {

		homePane.setVisible(false);
		songsPane.setVisible(true);
		usersPane.setVisible(false);
		addSongsPane.setVisible(false);

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

		songs_tableview.getItems().clear();
		// Populate the TableView with data
		songs_tableview.getItems().addAll(songs);
		System.out.println("Displaying all songs");
		playSongLabel.setText("");
	}

	@FXML
	public void displayUsers() {

		homePane.setVisible(false);
		songsPane.setVisible(false);
		usersPane.setVisible(true);
		addSongsPane.setVisible(false);

		//userModifyLabel.setText("");

		userNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
		isAdminColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isAdmin()));

		// Connect to the database and retrieve data
		List<UserModel> users = null;
		try {
			users = UserTable_DatabaseModel.fetchUsersFromDatabase();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		usersTable.getItems().clear();
		// Populate the TableView with data
		usersTable.getItems().addAll(users);
		System.out.println("Displaying all users");
	}

	@FXML
	public void displayHome() {
		homePane.setVisible(true);
		songsPane.setVisible(false);
		usersPane.setVisible(false);
		addSongsPane.setVisible(false);
	}

	@FXML
	public void backInUsers() {
		displayHome();
		userModifyLabel.setText("");
	}

	@FXML
	public void backInSongs() {
		displayHome();
	}

	@FXML
	public void backToSongs() {
		displaySongs();
	}

	@FXML
	public void addSongs_click() {
		homePane.setVisible(false);
		songsPane.setVisible(false);
		usersPane.setVisible(false);
		addSongsPane.setVisible(true);
		songAddedLabel.setText("");
	}
	
	@FXML
	public void refreshEnterSongInputDetails() {
		// Clear text input fields
		songTitleText.clear();
		albumText.clear();
		artistText.clear();
		durationText.clear();
		songAddedLabel.setText("");
	}

	@FXML
	public void addSong() {

		String title = songTitleText.getText().trim();
		String album = albumText.getText().trim();
		String artist = artistText.getText().trim();
		String durationString = durationText.getText().trim();
		if (checkUserInputs(title, album, artist, durationString)) {
			int duration = Integer.parseInt(durationString);
			SongModel song = new SongModel(title, artist, album, duration, -1);
			
			if(!SongTable_DatabaseModel.isSongPresent(song)) {
			SongTable_DatabaseModel.insertSongInToDatabase(song);
			songAddedLabel.setText("Song '" + title + "' is added");
			}else {
				songAddedLabel.setText("Song title '" + title + "' is already present. Enter a new song title");
			}
		}
		
		}
	
	
	public boolean checkUserInputs(String title,String album,String artist,String durationString) {
		boolean status = true;
		if(title.equals("")){
			songAddedLabel.setText("Song title cannot be empty");
			status = false;
		}
		if(album.equals("")){
			songAddedLabel.setText("Album name cannot be empty");
			status = false;
		}
		if(artist.equals("")){
			songAddedLabel.setText("Artist name cannot be empty");
			status = false;
		}
		if(durationString.equals("")){
			songAddedLabel.setText("Song duration cannot be empty");
			status = false;
		}
		if(title.equals("")&&album.equals("")){
			songAddedLabel.setText("Song title and Album name cannot be empty");
			status = false;
		}
		if(title.equals("")&&artist.equals("")){
			songAddedLabel.setText("Song title and Artist name cannot be empty");
			status = false;
		}
		if(title.equals("")&&durationString.equals("")){
			songAddedLabel.setText("Song title and duration cannot be empty");
			status = false;
		}
		if(album.equals("")&&artist.equals("")){
			songAddedLabel.setText("Album and Artist name cannot be empty");
			status = false;
		}
		if(album.equals("")&&durationString.equals("")){
			songAddedLabel.setText("Album name and song duration cannot be empty");
			status = false;
		}
		if(artist.equals("")&&durationString.equals("")){
			songAddedLabel.setText("Artist name and song duration cannot be empty");
			status = false;
		}
		if(title.equals("")&&album.equals("")&&artist.equals("")){
			songAddedLabel.setText("Song title, Album, Artist name cannot be empty");
			status = false;
		}
		if(title.equals("")&&album.equals("")&&durationString.equals("")){
			songAddedLabel.setText("Song title, Album name and song duration cannot be empty");
			status = false;
		}
		if(title.equals("")&&artist.equals("")&&durationString.equals("")){
			songAddedLabel.setText("Song title, Artist name and song duration cannot be empty");
			status = false;
		}
		if(durationString.equals("")&&album.equals("")&&artist.equals("")){
			songAddedLabel.setText("Album, Artist name and song duration cannot be empty");
			status = false;
		}
		if(title.equals("")&&durationString.equals("")&&album.equals("")&&artist.equals("")){
			songAddedLabel.setText("Song title, Album, Artist and song duration cannot be empty");
			status = false;
		}
		if(!title.equals("")&&!album.equals("")&&!artist.equals("")&&!durationString.equals("")) {
			
			//Check if the duration given by user is valid
			if(!isValidDurationString(durationString)) {
				status = false;
			}
			
		}	
		return status;
	}
	
	////Check if the duration given by user is valid
	public boolean isValidDurationString(String durationString) {
	    try {
	        // Attempt to parse the string into an integer
	        int duration = Integer.parseInt(durationString);

	        // Check if the parsed duration is non-negative
	        if (duration < 0) {
	            // If it's negative, show an error message
	            //System.out.println("Duration cannot be negative");
	            songAddedLabel.setText("Duration cannot be negative");
	            return false;
	        }

	        // The string is a valid integer
	        return true;
	    } catch (NumberFormatException e) {
	        // Handle the case where the string is not a valid integer
	        System.out.println("Invalid duration format. Please enter a valid integer.");
	        songAddedLabel.setText("Invalid duration format. Please enter duration in seconds");
	        return false;
	    }
	}

	@FXML
	public void deleteUser() {
		// Get the selected user from the TableView
		UserModel selectedUser = usersTable.getSelectionModel().getSelectedItem();

		// Update the label with the selected user information
		if (selectedUser != null) {

			UserTable_DatabaseModel.deleteUserFromDatabase(selectedUser);
			userModifyLabel.setText("The user '" + selectedUser.getUsername() + "'is removed");
		} else {
			userModifyLabel.setText("No user is selected");
		}

		displayUsers();
	}

	@FXML
	public void changeAdminStatus(ActionEvent event) {
		// Get the selected user from the TableView
		UserModel selectedUser = usersTable.getSelectionModel().getSelectedItem();

		// Update the label with the selected user information
		if (selectedUser != null) {

			if (selectedUser.isAdmin()) {
				UserTable_DatabaseModel.removeUserAsAdmin(selectedUser);
				userModifyLabel.setText("The user '" + selectedUser.getUsername() + "' is no longer an admin");
			} else {
				UserTable_DatabaseModel.setUserAsAdmin(selectedUser);
				userModifyLabel.setText("The user '" + selectedUser.getUsername() + "' is now an admin");
			}
		} else {
			userModifyLabel.setText("No user is selected");
		}

		displayUsers();
	}
	
	
	@FXML
	public void playSong() {
		// Get the selected song from the TableView
        SongModel selectedSong = songs_tableview.getSelectionModel().getSelectedItem();

        // Update the label with the selected song information
        if (selectedSong != null) {
        	playSongLabel.setText("Now Playing: " + selectedSong.getTitle());
        } else {
        	playSongLabel.setText("No song is selected");
        }
	}
	@FXML
	public void signOut() {
		try {
			AnchorPane root;
			root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/LoginView.fxml"));
			Main.stage.setTitle("Login View");
			Scene scene = new Scene(root);
			Main.stage.setScene(scene);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	public void deleteSongClicked() {
		// Get the selected song from the TableView
        SongModel selectedSong = songs_tableview.getSelectionModel().getSelectedItem();
        
        // Update the label with the selected song information
        if (selectedSong != null) {
        	//Delete the selected song from dataBase
            SongTable_DatabaseModel.deleteSong(selectedSong);
        	displaySongs();
        	playSongLabel.setText("The song: '" + selectedSong.getTitle()+"' is deleted");
        } else {
        	playSongLabel.setText("No song is selected");
        }
        
	}
}
