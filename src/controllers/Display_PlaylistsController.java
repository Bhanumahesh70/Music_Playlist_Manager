package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import application.Main;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import models.PlaylistModel;
import models.PlaylistTable_DatabaseModel;
import models.SongModel;

/*
 * Code for displaying all Playlists. Creating a List view to display the list
 * of all playlists and details.
 */
public class Display_PlaylistsController extends ClientController {

	@FXML
	private ListView<PlaylistModel> playlist_listview;

	@FXML
	private Button playlistView_btn;

	@FXML
	private Label playlist_detailsLabel;

	@FXML
	private TextArea songDeatils_textArea;

	@FXML
	private Pane displaySongs_Panel;

	@FXML
	private Pane playlists_panel;

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

	// Get user details
	int userId = ClientController.getUser();

	// Going back to home when Home button is pressed
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
	private void submitButtonClicked() {
		// Get the selected playlist from the ListView
		PlaylistModel selectedPlaylist = playlist_listview.getSelectionModel().getSelectedItem();

		// Perform the action based on the selected playlist
		if (selectedPlaylist != null) {
			// Call the method or perform actions based on the selected playlist
			viewPlaylistDetails(selectedPlaylist);
		} else {
			// Handle the case when no playlist is selected
			System.out.println("No playlist selected");
		}
	}

	@FXML
	public void back() {
		displaySongs_Panel.setVisible(false);
		playlists_panel.setVisible(true);
	}

	@FXML
	public void initialize() {

		// get playlist details from database
		List<PlaylistModel> playlists = null;
		playlists = PlaylistTable_DatabaseModel.fetchPlaylistsFromDatabase(userId);

		// Display the playlist titles trough the list view
		System.out.println("Displaying Playlists");
		playlist_listview.getItems().clear(); // Clear existing items
		playlist_listview.getItems().addAll(playlists);
	}

	private void viewPlaylistDetails(PlaylistModel playlist) {

		displaySongs_Panel.setVisible(true);
		playlists_panel.setVisible(false);

		if (playlist != null) {

			String playlistName = playlist.getPlaylistName();
			songs_tableview.getItems().clear();

			if (!PlaylistTable_DatabaseModel.isPlaylistEmpty(playlistName, userId)) {

				title_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
				artist_column
						.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArtist()));
				album_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlbum()));
				duration_column.setCellValueFactory(
						cellData -> new SimpleIntegerProperty(cellData.getValue().getDuration()).asObject());

				List<SongModel> songsFromPlaylist = PlaylistTable_DatabaseModel.fetchSongsFromPlaylist(playlist);
				// Populate the TableView with data
				songs_tableview.getItems().clear();
				songs_tableview.getItems().addAll(songsFromPlaylist);
				System.out.println("Displaying all songs");
			}
		} else {
			playlist_detailsLabel.setText("Please select a playlist to view details.");
			songDeatils_textArea.setText("Select a playlist");
		}
	}
}
