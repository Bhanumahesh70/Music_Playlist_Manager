package controllers;

import java.util.List;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
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
	public void displayUsers() {
		
		homePane.setVisible(false);
		songsPane.setVisible(false);
		usersPane.setVisible(true);
		
		userNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
		isAdminColumn.setCellValueFactory(
				cellData -> new SimpleBooleanProperty(cellData.getValue().isAdmin()));
		
		// Connect to the database and retrieve data
				List<UserModel> users = null;
				try {
					users =UserTable_DatabaseModel.fetchUsersFromDatabase();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Populate the TableView with data
				usersTable.getItems().addAll(users);
				System.out.println("Displaying all users");
	}
	
	@FXML
	public void displayHome() {
		homePane.setVisible(true);
		songsPane.setVisible(false);
		usersPane.setVisible(false);
	}
	
	@FXML
	public void backInUsers() {
		
	}
  
	@FXML
	public void backInSongs() {
		
	}
}
