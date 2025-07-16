package org.example.Repository;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;

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
            "INSERT INTO " + TABLE_NAME + " (user_id, mobile_number, is_new_user) VALUES (?, ?, FALSE)";

    Tracer tracer = GlobalOpenTelemetry.getTracer("grpc-server");

    /**
     * Get mobile number by userId.
     */
    public String getMobileNumberByUserId(long userId) {
        Span dbSpan = tracer.spanBuilder("postgres.users").startSpan();
        dbSpan.setAttribute("db.user_id", userId);

        try (Connection connection = DBconnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(QUERY_GET_MOBILE_BY_USER_ID)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("USER FOUND-MOBILE NUMBER: ");
                    sb.append(rs.getString("mobile_number"));

                    dbSpan.setAttribute("db.result", "FOUND");
                    return sb.toString();
                } else {
                    dbSpan.setAttribute("db.result", "NOT_FOUND");
                    return "NOT FOUND: NO USER FROM THIS ID:" + userId;
                }
            }

        } catch (SQLException e) {
            dbSpan.recordException(e);
            System.err.println("[ERROR] Failed to get mobile number for userId=" + userId);
            e.printStackTrace();
        } finally {
            dbSpan.end();
        }

        return null;
    }

    /**
     * Check if the user is new.
     * If user does not exist, insert as new & return true.
     * If user exists, return value of is_new_user.
     */
    public boolean getOrCreateAndCheckIsNew(long userId, String mobileNumber) {
        Span dbSpan = tracer.spanBuilder("postgres.users").startSpan();
        dbSpan.setAttribute("db.user_id", userId);

        if (mobileNumber == null) mobileNumber = "";

        try (Connection connection = DBconnection.getInstance().getConnection()) {

            // Check if user already exists
            try (PreparedStatement psCheck = connection.prepareStatement(QUERY_GET_IS_NEW_USER)) {
                psCheck.setLong(1, userId);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        dbSpan.setAttribute("db.result", "EXISTS");
                        return false;
                    }
                }
            }

            // User does NOT exist → insert new user
            try (PreparedStatement psInsert = connection.prepareStatement(QUERY_INSERT_NEW_USER)) {
                psInsert.setLong(1, userId);
                psInsert.setString(2, mobileNumber);

                psInsert.executeUpdate();
                dbSpan.setAttribute("db.result", "INSERTED");
                System.out.println("[INFO] Inserted new user userId=" + userId);
                return true;
            }

        } catch (SQLException e) {
            dbSpan.recordException(e);
            System.err.println("[ERROR] Failed to check/insert user userId=" + userId);
            e.printStackTrace();
        } finally {
            dbSpan.end();
        }

        // On error, assume not new
        return false;
    }
}
