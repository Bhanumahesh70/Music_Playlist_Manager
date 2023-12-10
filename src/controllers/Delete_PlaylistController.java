package controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import application.Main;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import models.PlaylistModel;
import models.PlaylistTable_DatabaseModel;
import models.SongModel;
import models.SongTable_DatabaseModel;


public class Delete_PlaylistController {
	
	@FXML
	private TableView<PlaylistModel> playlists_tableview;

	@FXML
	private TableColumn<PlaylistModel, String> title_column;
	
	@FXML
	private Label playlistDelete_label;


	// Get user details
		int userId = ClientController.getUser();
	
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
	public void initialize() {
		title_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPlaylistName()));

		// Connect to the database and retrieve data
		List<PlaylistModel> playlists = null;
		try {
			playlists = PlaylistTable_DatabaseModel.fetchPlaylistsFromDatabase(userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Populate the TableView with data
		playlists_tableview.getItems().addAll(playlists);
		System.out.println("Displaying all Playlists");
	}
	@FXML
    public void deleteButtonClicked(ActionEvent event) {
		// Get the selected playlist from the TableView
	    PlaylistModel selectedPlaylist = playlists_tableview.getSelectionModel().getSelectedItem();

	    // Delete the selected playlist from the database
	    if (selectedPlaylist != null) {
	        // Create a confirmation dialog
	        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	        alert.setTitle("Confirmation");
	        alert.setHeaderText("Delete Playlist");
	        alert.setContentText("Are you sure you want to delete the playlist: '" + selectedPlaylist.getPlaylistName() + "'?");

	        // Show and wait for the user's response
	        Optional<ButtonType> result = alert.showAndWait();

	        // If the user clicks OK, delete the playlist
	        if (result.isPresent() && result.get() == ButtonType.OK) {
	            String playlistName = selectedPlaylist.getPlaylistName();
	            PlaylistTable_DatabaseModel.deletePlaylist(playlistName, userId);

	            // Remove the playlist from the TableView
	            playlists_tableview.getItems().remove(selectedPlaylist);
	            // Clear the selection
	            playlists_tableview.getSelectionModel().clearSelection();

	            String s = "The Playlist '" + playlistName + "' is deleted";
	            playlistDelete_label.setText(s);
	        } else {
	            playlistDelete_label.setText("Playlist deletion canceled");
	        }
	    } else {
	        playlistDelete_label.setText("No Playlist is selected");
	    }
        
        
    }
}
