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
import models.SongTable_DatabaseModel;

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
	private Label myPlaylistLabel;
	
	@FXML
	private Label emptyPlaylistLabel;
	
	@FXML
	private Label songAddedLabel;

	@FXML
	private TextArea songDeatils_textArea;

	@FXML
	private Pane displaySongs_Panel;

	@FXML
	private Pane playlists_panel;
	
	@FXML
	private Pane addSongsPane;

	@FXML
	private TableView<SongModel> PlaylistSongs_tableview;

	@FXML
	private TableColumn<SongModel, String> PlaylistTitle_column;

	@FXML
	private TableColumn<SongModel, String> PlaylistArtist_column;

	@FXML
	private TableColumn<SongModel, String> PlaylistAlbum_column;

	@FXML
	private TableColumn<SongModel, Integer> PlaylistDuration_column;

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
	
	// declaring a variable to get which playlist the user has selected 
	PlaylistModel selected_Playlist;
	
	//getter and setter methods for user selected playlist
	private void setSelectedPlaylist(PlaylistModel playlist) {
		selected_Playlist = playlist;
	}
	private PlaylistModel getSelectedPlaylist() {
		return selected_Playlist;
	}
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
		// set the selected playlist variable with user selected playlist
					setSelectedPlaylist(selectedPlaylist);

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
		addSongsPane.setVisible(false);
		myPlaylistLabel.setText("My Playlists");
		PlaylistSongs_tableview.getItems().clear();
	}
	@FXML
	public void backToplaylist() {
		PlaylistModel playlist = getSelectedPlaylist();
		viewPlaylistDetails(playlist);
		
		
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
		addSongsPane.setVisible(false);

		if (playlist != null) {
			String playlistName = playlist.getPlaylistName();
			//PlaylistSongs_tableview.getItems().clear();
			myPlaylistLabel.setText(playlistName);
			//PlaylistSongs_tableview.getItems().clear();

			if (!PlaylistTable_DatabaseModel.isPlaylistEmpty(playlistName, userId)) {

				PlaylistTitle_column.setCellValueFactory(cellData -> {
	                if (cellData.getValue() != null && cellData.getValue().getTitle() != null) {
	                    return new SimpleStringProperty(cellData.getValue().getTitle());
	                } else {
	                    return new SimpleStringProperty("");
	                }
						
			});
				
				/*
				PlaylistArtist_column
						.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArtist()));
				PlaylistAlbum_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlbum()));
				PlaylistDuration_column.setCellValueFactory(
						cellData -> new SimpleIntegerProperty(cellData.getValue().getDuration()).asObject());
				*/
				PlaylistTitle_column.setCellValueFactory(cellData -> {
			        if (cellData.getValue() != null && cellData.getValue().getTitle() != null) {
			            return new SimpleStringProperty(cellData.getValue().getTitle());
			        } else {
			            return new SimpleStringProperty("");
			        }
			    });

			    PlaylistArtist_column.setCellValueFactory(cellData -> {
			        if (cellData.getValue() != null && cellData.getValue().getArtist() != null) {
			            return new SimpleStringProperty(cellData.getValue().getArtist());
			        } else {
			            return new SimpleStringProperty("");
			        }
			    });

			    PlaylistAlbum_column.setCellValueFactory(cellData -> {
			        if (cellData.getValue() != null && cellData.getValue().getAlbum() != null) {
			            return new SimpleStringProperty(cellData.getValue().getAlbum());
			        } else {
			            return new SimpleStringProperty("");
			        }
			    });

			    PlaylistDuration_column.setCellValueFactory(cellData -> {
			        if (cellData.getValue() != null) {
			            return new SimpleIntegerProperty(cellData.getValue().getDuration()).asObject();
			        } else {
			            return new SimpleIntegerProperty(0).asObject(); // Assuming a default value for duration
			        }
			    });

				List<SongModel> songsFromPlaylist = PlaylistTable_DatabaseModel.fetchSongsFromPlaylist(playlist,userId);
				// Populate the TableView with data
				PlaylistSongs_tableview.getItems().clear();
				PlaylistSongs_tableview.getItems().addAll(songsFromPlaylist);
				System.out.println("Displaying the songs from the playlist '"+playlistName+"'");
				emptyPlaylistLabel.setText(" ");
				
			}
			else {
				emptyPlaylistLabel.setText("The playlist is empty");
			}
		} else {
			playlist_detailsLabel.setText("Please select a playlist to view details.");
			songDeatils_textArea.setText("Select a playlist");
		}
	}
	
	@FXML
	public void addSongsClicked() {
		displaySongs_Panel.setVisible(false);
		playlists_panel.setVisible(false);
		addSongsPane.setVisible(true);
		displayAllSongs();
	}
	
	public void displayAllSongs() {
		
		songs_tableview.getItems().clear();
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
		songAddedLabel.setText("Select songs to add to the playlist");
		System.out.println("Displaying all songs to add to playlist");
		
	}
	
	@FXML
	public void addSongToPlaylistClick(ActionEvent event) {
        // Get the selected song from the TableView
        SongModel selectedSong = songs_tableview.getSelectionModel().getSelectedItem();

        // Update the label with the selected song information
        if (selectedSong != null) {
        	
        	//Get the user selected playlist
        	PlaylistModel playlist = getSelectedPlaylist();
        	String playlistName = playlist.getPlaylistName();
        	int songId = selectedSong.getId();
        	
        	if(PlaylistTable_DatabaseModel.isSongPresentinPlaylist(playlistName,userId,songId)) {
        		
        		songAddedLabel.setText("The song '" + selectedSong.getTitle()+"' is already in the playlist");
        		
        	}else {
      
        		songAddedLabel.setText("The song '" + selectedSong.getTitle()+"' is added to the playlist");
            	PlaylistTable_DatabaseModel.addSongToPlaylist(playlistName,songId,userId);
            	
            	// Refresh the TableView with the updated data
                //viewPlaylistDetails(playlist);
        	}
        	
        } else {
        	songAddedLabel.setText("No song is selected");
        }
    }
	
	@FXML
    public void playButtonClicked(ActionEvent event) {
        // Get the selected song from the TableView
        SongModel selectedSong = PlaylistSongs_tableview.getSelectionModel().getSelectedItem();

        // Update the label with the selected song information
        if (selectedSong != null) {
        	emptyPlaylistLabel.setText("Now Playing: " + selectedSong.getTitle());
        } else {
        	emptyPlaylistLabel.setText("No song is selected");
        }
    }
}
