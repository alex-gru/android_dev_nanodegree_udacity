package nanodegree.p1.data;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import nanodegree.p1.MainActivity;

/**
 * Created by alexgru on 22-Mar-16.
 * Android Developer Nanodegree
 * UDACITY
 */
public class MovieDBAsyncTask extends AsyncTask<Void, Integer, Integer> {

    // fill in your API key here
    final static String THE_MOVIE_DB_API_KEY = "";
    final static String MOST_POPULAR_URL = "http://api.themoviedb.org/3/movie/popular?api_key=" + THE_MOVIE_DB_API_KEY;
    String result = "";

    @Override
    protected Integer doInBackground(Void... params) {

        if (android.os.Debug.isDebuggerConnected()) {
            android.os.Debug.waitForDebugger();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder sb = null;
        try {
            URL url = new URL(MOST_POPULAR_URL);

            urlConnection = (HttpURLConnection) url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            sb = new StringBuilder();
            String currLine;

            while ((currLine = in.readLine()) != null) {
                sb.append(currLine).append("\n");
            }

            in.close();
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "Exception occured while fetching movie information from The Movie DB.", e);
            e.printStackTrace();
            return -1;
        } finally {
            urlConnection.disconnect();
        }

        result = sb.toString();
        return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {

        try {
            ObjectMapper mapper = new ObjectMapper();

            JsonNode json = mapper.readValue(result, JsonNode.class);

            JsonNode movies = json.get("results");

            for (int idx = 0; idx < movies.size(); idx++) {
                JsonNode movieJson = movies.get(idx);
                Movie movie = mapper.treeToValue(movieJson, Movie.class);

                Log.d(MainActivity.TAG, "Movie: " + movie.original_title);
                //TODO: go on here and save list of Movie objects, which are loaded into gridview.
            }

        } catch (Exception e) {
            Log.e(MainActivity.TAG, "Exception occured while parsing JSON data.", e);
        }
    }
}
