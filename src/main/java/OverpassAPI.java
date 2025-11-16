import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

public class OverpassAPI {

    public static JSONObject fetchJSON(String overpassQL) throws Exception {

        String apiUrl = "https://overpass-api.de/api/interpreter?data=";

        // Encode the Overpass query
        String encoded = URLEncoder.encode(overpassQL, "UTF-8");

        // Build URI (preferred over deprecated URL(String))
        URL url = new URI(apiUrl + encoded).toURL();

        // Open connection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Java-Overpass-Client");

        // Read response
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );

        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();

        return new JSONObject(response.toString());
    }
}
