package ch.epfl.sweng.SDP.matchmaking;

import ch.epfl.sweng.SDP.auth.ConstantsWrapper;

import com.google.firebase.database.DatabaseReference;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Matchmaker implements MatchmakingInterface {

    private ConstantsWrapper constantsWrapper;
    private static Matchmaker singleInstance = null;
    // static method to create instance of Singleton class
    private DatabaseReference reference;

    /**
     * Create a singleton Instance.
     *
     * @return returns a singleton instance.
     */
    public static Matchmaker getInstance(ConstantsWrapper constantsWrapper) {
        if (singleInstance == null) {
            singleInstance = new Matchmaker(constantsWrapper);
        }

        return singleInstance;
    }


    /**
     * Matchmaker init.
     */
    private Matchmaker(ConstantsWrapper constantsWrapper) {
        this.constantsWrapper = constantsWrapper;
        this.reference = constantsWrapper.getReference("rooms");
    }

    /**
     * join a room.
     */
    public Boolean joinRoom() {

        Boolean successful = false;
        HttpURLConnection connection = null;

        try {
            //Create connection

            String userId = constantsWrapper.getFirebaseUserId();
            String urlParameters = "userId=" + URLEncoder.encode(userId, "UTF-8");
            URL url = new URL("https://us-central1-gyrodraw.cloudfunctions.net/joinGame?" + urlParameters);
            connection = createConnection(url);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // OK
                successful = true;
                // otherwise, if any other status code is returned, or no status
                // code is returned, do stuff in the else block
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return successful;
    }

    /**
     * Creates a connection.
     *
     * @return set up connection
     */
    private HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");

        connection.setRequestProperty("Content-Language", "en-US");

        connection.setUseCaches(false);
        connection.setDoOutput(true);
        return connection;
    }

    /**
     * leave a room.
     *
     * @param roomId the id of the room.
     */
    public Boolean leaveRoom(String roomId) {
        reference.child(roomId)
                .child("users")
                .child(constantsWrapper.getFirebaseUserId())
                .removeValue();
        return true;
    }
}
