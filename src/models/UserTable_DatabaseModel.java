package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dao.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserTable_DatabaseModel {

	public static ObservableList<UserModel> fetchUsersFromDatabase() {
		ObservableList<UserModel> users = FXCollections.observableArrayList();

		try {
			PreparedStatement stmt = DBConnect.getConnection().prepareStatement("SELECT * FROM beatmusic_users");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String userName = rs.getString("username");
				String password = rs.getString("password");
				int is_Admin = rs.getInt("is_admin");
				int user_id = rs.getInt("user_id");

				boolean isAdmin = true;
				if (is_Admin == 0) {
					isAdmin = false;
				}
				users.add(new UserModel(user_id, userName, password, isAdmin));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return users;
	}

	public static void insertUserInToDatabase(UserModel user) {
		try {

			boolean is_admin = user.isAdmin();
			int isAdmin = 0;
			if (is_admin) {
				isAdmin = 1;
			}
			String sql = "INSERT INTO beatmusic_users (userName, password, is_admin) VALUES (?,?,?)";

			PreparedStatement stmt = DBConnect.getConnection().prepareStatement(sql);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			stmt.setInt(3, isAdmin);
			stmt.executeUpdate();
			stmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteUserFromDatabase(UserModel user) {
		try {

			String sql = "DELETE FROM beatmusic_users WHERE username = ?";

			PreparedStatement stmt = DBConnect.getConnection().prepareStatement(sql);
			stmt.setString(1, user.getUsername());
			stmt.executeUpdate();
			stmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setUserAsAdmin(UserModel user) {
		try {

			String sql = "UPDATE beatmusic_users SET is_admin = ? WHERE username = ?";

			PreparedStatement stmt = DBConnect.getConnection().prepareStatement(sql);
			stmt.setInt(1, 1);
			stmt.setString(2, user.getUsername());
			stmt.executeUpdate();
			stmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void removeUserAsAdmin(UserModel user) {
		try {

			String sql = "UPDATE beatmusic_users SET is_admin = ? WHERE username = ?";

			PreparedStatement stmt = DBConnect.getConnection().prepareStatement(sql);
			stmt.setInt(1, 0);
			stmt.setString(2, user.getUsername());
			stmt.executeUpdate();
			stmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
