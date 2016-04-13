package nanodegree.p1p2.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import nanodegree.p1p2.MainActivity;
import nanodegree.p1p2.MovieDetailFragment;
import nanodegree.p1p2.MovieGridFragment;
import nanodegree.p1p2.MoviePosterAdapter;
import nanodegree.p1p2.R;

/**
 * Created by alexgru on 22-Mar-16.
 * Android Developer Nanodegree
 * UDACITY
 */
public class MovieAsyncTask extends AsyncTask<Void, Integer, Integer> {

    private final AppCompatActivity activity;
    private String THE_MOVIE_DB_API_KEY = null;
    private String MOST_POPULAR_URL = "http://api.themoviedb.org/3/movie/popular";
    private String TOP_RATED_URL = "http://api.themoviedb.org/3/movie/top_rated";
    private final GridView gridView;
    String result_most_popular = "";
    String result_top_rated = "";

    public MovieAsyncTask(AppCompatActivity activity, GridView gridview) {
        this.gridView = gridview;
        this.activity = activity;
        try {
            THE_MOVIE_DB_API_KEY =  new BufferedReader(new InputStreamReader(gridview.getResources().openRawResource(R.raw.themoviedb))).readLine();
            MOST_POPULAR_URL +=  "?api_key=" + THE_MOVIE_DB_API_KEY;
            TOP_RATED_URL +=  "?api_key=" + THE_MOVIE_DB_API_KEY;

        } catch (IOException e) {
            Log.e(MainActivity.TAG, "Could not read API key. Check if 'themoviedb.txt' is present.", e);
        }
    }

    @Override
    protected Integer doInBackground(Void... params) {

        try {
            MovieGridFragment.page++;
            result_most_popular = getMovieDataFromURL(MOST_POPULAR_URL, MovieGridFragment.page);
            result_top_rated = getMovieDataFromURL(TOP_RATED_URL, MovieGridFragment.page);
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "Exception occured while fetching movie information from The Movie DB.", e);
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    private String getMovieDataFromURL(String urlString, int page) throws IOException {
        HttpURLConnection urlConnection = null;
        StringBuilder sb;
        try {
            sb = new StringBuilder();
            URL url = new URL(urlString + "&page=" + page);
            urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            String currLine;

            while ((currLine = in.readLine()) != null) {
                sb.append(currLine).append("\n");
            }
            in.close();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(Integer integer) {

        try {
            ObjectMapper mapper = new ObjectMapper();

            String json_most_popular = mapper.readValue(result_most_popular, JsonNode.class).get("results").toString();
            String json_top_rated = mapper.readValue(result_top_rated, JsonNode.class).get("results").toString();
            List<Movie> newMostPopularMovies = mapper.readValue(json_most_popular, TypeFactory.defaultInstance().constructCollectionType(List.class, Movie.class));
            List<Movie> newTopRatedMovies = mapper.readValue(json_top_rated, TypeFactory.defaultInstance().constructCollectionType(List.class, Movie.class));
            MovieGridFragment.movies_most_popular.addAll(newMostPopularMovies);
            MovieGridFragment.movies_top_rated.addAll(newTopRatedMovies);
            MoviePosterAdapter.setSortModePopular(MovieGridFragment.sortModePopular);
            gridView.invalidateViews();

            MovieDetailFragment detailFragment = (MovieDetailFragment)activity.getSupportFragmentManager().findFragmentByTag(MovieDetailFragment.TAG);
            if (detailFragment != null && detailFragment.movie == null) {
                detailFragment.updateMovieDetailUI();
            }

            /**
             * DEBUG - output DB contents
             */
             SQLiteDatabase database = MainActivity.movieDBHelper.getReadableDatabase();

            String[] projection = {
                    MovieContract.MovieEntry._ID,
                    MovieContract.MovieEntry. COLUMN_NAME_ID,
                    MovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH ,
                    MovieContract.MovieEntry.COLUMN_NAME_ADULT,
                    MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW ,
                    MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE ,
                    MovieContract.MovieEntry. COLUMN_NAME_GENRE_IDS ,
                    MovieContract.MovieEntry.COLUMN_NAME_ORIGINAL_TITLE ,
                    MovieContract.MovieEntry. COLUMN_NAME_ORIGINAL_LANGUAGE,
                    MovieContract.MovieEntry. COLUMN_NAME_TITLE,
                    MovieContract.MovieEntry. COLUMN_NAME_BACKDROP_PATH,
                    MovieContract.MovieEntry. COLUMN_NAME_POPULARITY ,
                    MovieContract.MovieEntry. COLUMN_NAME_VOTE_COUNT ,
                    MovieContract.MovieEntry.COLUMN_NAME_VIDEO,
                    MovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE
            };

            String sortOrder = MovieContract.MovieEntry._ID + " DESC";

            Cursor cursor = database.query(
                    MovieContract.MovieEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder
            );

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                Log.d(MainActivity.TAG,cursor.getString(0));
                Log.d(MainActivity.TAG,cursor.getString(1));
                Log.d(MainActivity.TAG,cursor.getString(2));
                Log.d(MainActivity.TAG,cursor.getString(3));
                Log.d(MainActivity.TAG,cursor.getString(4));
                cursor.moveToNext();
            }

        } catch (Exception e) {
            Log.e(MainActivity.TAG, "Exception occured while parsing JSON data.", e);
        }
    }
}
