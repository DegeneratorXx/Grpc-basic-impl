package org.example.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDataHelper {
    public static final String TABLE_NAME = "users";

    private static final String QUERY_GET_MOBILE_BY_USER_ID =
            "SELECT mobile_number FROM " + TABLE_NAME + " WHERE user_id = ?";

    private static final String QUERY_GET_IS_NEW_USER =
            "SELECT is_new_user FROM " + TABLE_NAME + " WHERE user_id = ?";

    private static final String QUERY_INSERT_NEW_USER =
            "INSERT INTO " + TABLE_NAME + " (user_id, mobile_number, is_new_user) VALUES (?, ?, TRUE)";

    /**
     * Get mobile number by userId.
     */
    public String getMobileNumberByUserId(long userId) {
        try (Connection connection = DBconnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(QUERY_GET_MOBILE_BY_USER_ID)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("mobile_number");
                }
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to get mobile number for userId=" + userId);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Check if the user is new.
     * If user does not exist, insert as new & return true.
     * If user exists, return value of is_new_user.
     */
    public boolean getOrCreateAndCheckIsNew(long userId, String mobileNumber) {
        if (mobileNumber == null) mobileNumber = "";

        try (Connection connection = DBconnection.getInstance().getConnection()) {

            // First, check if user exists
            try (PreparedStatement psCheck = connection.prepareStatement(QUERY_GET_IS_NEW_USER)) {
                psCheck.setLong(1, userId);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        return rs.getBoolean("is_new_user");
                    }
                }
            }

            // User does NOT exist — insert as new
            try (PreparedStatement psInsert = connection.prepareStatement(QUERY_INSERT_NEW_USER)) {
                psInsert.setLong(1, userId);
                psInsert.setString(2, mobileNumber);
                psInsert.executeUpdate();
                System.out.println("[INFO] Inserted new user userId=" + userId);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to check/insert user userId=" + userId);
            e.printStackTrace();
        }

        return false;
    }
}
