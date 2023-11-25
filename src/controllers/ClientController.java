package controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.DBConnect;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import models.Song;
import models.SongModel;


public class ClientController {

	@FXML
	private Pane playlist_panel;
	
	@FXML
	private Pane panel1;
	
	@FXML
	private Pane allsongs_panel;
	
	@FXML
	private Pane playlists_panel;
	
	@FXML
	private TextField playlistName_text;
	
	@FXML
	private TextField songName_text;
	
	// Declare DB objects
		DBConnect conn = null;
		Statement stmt = null;
		PreparedStatement prepstmt =null;
		ResultSet myRs = null;
	
	public ClientController() {
		conn = new DBConnect();
	}
	
	@FXML
	public void displaySongs() {
		playlist_panel.setVisible(false);
		panel1.setVisible(false);
		allsongs_panel.setVisible(true);
		playlists_panel.setVisible(false);
	}
	
	@FXML
	public void createPlaylist_click() {
		playlist_panel.setVisible(true);
		panel1.setVisible(false);
		allsongs_panel.setVisible(false);
		playlists_panel.setVisible(false);
	}
	
	@FXML
	public void displayPlaylist_panel() {
		playlist_panel.setVisible(false);
		panel1.setVisible(false);
		allsongs_panel.setVisible(false);
		playlists_panel.setVisible(true);
	}
	
	@FXML
	public void createPlaylist() {
		// INSERT INTO Playlist TABLE
				try {
				// Execute a query
					System.out.println("Inserting data into the playlist table...");
				stmt = conn.getConnection().createStatement();
				String sql = null;
				String songName = songName_text.getText();
				String playlistName = playlistName_text.getText();
				
				sql= "SELECT song_id FROM beatmusic_songs WHERE title = ? ";
				prepstmt = conn.getConnection().prepareStatement(sql);
				prepstmt.setString(1, songName);
				myRs = prepstmt.executeQuery();
				
				int song_id = 0;
				while(myRs.next()) {
					 song_id= myRs.getInt("song_id");
				}
				
				int user_id =1;
				// Include all object data to the database table
				sql = "INSERT INTO beatmusic_playlist(playlist_name,user_id,song_id) values (?,?,?)";
				prepstmt = conn.getConnection().prepareStatement(sql);
				prepstmt.setString(1, playlistName);
				prepstmt.setInt(2,user_id);
				prepstmt.setInt(3,song_id);
				prepstmt.executeUpdate();
				System.out.println("Playlist created");
		 
				conn.getConnection().close();
				} catch (SQLException se) {
				se.printStackTrace();
				}
	}

	
	/*
	 * Code for displaying all songs
	 * Creating a table view to display the list of sons and details
	 */
	
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
    public void initialize() {
    	title_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
    	artist_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArtist()));
    	album_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlbum()));
    	duration_column.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDuration()).asObject());


        // Connect to the database and retrieve data
        List<SongModel> songs = fetchDataFromDatabase();

        // Populate the TableView with data
        songs_tableview.getItems().addAll(songs);
        System.out.println("Displaying all songs");
    }

    private List<SongModel> fetchDataFromDatabase() {
        List<SongModel> songs = new ArrayList<>();

        // TODO: Connect to your database
        try { 
        	
        	System.out.println("Fetching all songs from songs table in database");
        	stmt = conn.getConnection().createStatement();
			String sql = null;
			sql= "SELECT * FROM beatmusic_songs";
			myRs = stmt.executeQuery(sql);

            while (myRs.next()) {
                String title = myRs.getString("title");
                String artist = myRs.getString("artist");
                String album = myRs.getString("album");
                int duration = myRs.getInt("duration");

                songs.add(new SongModel(title, artist, album,duration));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return songs;
    }
    
    /*
	 * Code for displaying all Playlists.
	 * Creating a List view to display the list of all playlists and details.
	 */
	
    public class PlaylistController {

        @FXML
        private ListView<String> playlist_listview;

        @FXML
        private Button playlistView_btn;

        @FXML
        private Label playlist_detailsLabel;

        // Add methods and event handlers here

        public void initialize() {
            // This method is automatically called when the FXML file is loaded
            // You can initialize your components or set event handlers here
        }
        
        @FXML
        private void viewPlaylistDetails() {
            String selectedPlaylist = playlist_listview.getSelectionModel().getSelectedItem();
            if (selectedPlaylist != null) {
                // Fetch playlist details and update detailsLabel
                String playlistDetails = "Details for " + selectedPlaylist + ":\n"
                        + "Song 1 - Artist 1 - Album 1\n"
                        + "Song 2 - Artist 2 - Album 2\n"
                        + "Song 3 - Artist 3 - Album 3";

                playlist_detailsLabel.setText(playlistDetails);
            } else {
            	playlist_detailsLabel.setText("Please select a playlist to view details.");
            }
        }
    }

	
}
