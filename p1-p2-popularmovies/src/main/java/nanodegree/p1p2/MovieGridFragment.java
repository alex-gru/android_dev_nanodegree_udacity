package nanodegree.p1p2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.LinkedList;
import java.util.List;

import nanodegree.p1p2.data.Movie;
import nanodegree.p1p2.data.MovieAsyncTask;


public class MovieGridFragment extends Fragment {
    public GridView gridview;
    private Menu menu;
    public static List<Movie> movies_top_rated;
    public static List<Movie> movies_most_popular;
    public static boolean sortModePopular = true;
    public static int lastPositionInGrid = -1;
    public static int page = 0;

    public MovieGridFragment() {

        if (movies_most_popular == null || movies_top_rated == null) {
            movies_most_popular = new LinkedList<Movie>();
            movies_top_rated = new LinkedList<Movie>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moviegrid, container, false);
        setHasOptionsMenu(true);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        toolbar.setTitle(getResources().getString(R.string.toolbar_title_moviegrid));
        toolbar.setDisplayHomeAsUpEnabled(false);

        gridview =(GridView) view.findViewById(R.id.gridview);
        gridview.setAdapter(new MoviePosterAdapter(getActivity()));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (MainActivity.isTablet)
                {
                    MovieDetailFragment detailFragment = (MovieDetailFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.detailfragment_container);
                    detailFragment.updateMovieDetailUI(position);
                } else {
                    MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                    Bundle args = new Bundle();
                    args.putInt("gridPosition", position);
                    movieDetailFragment.setArguments(args);

                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                            .replace(R.id.gridfragment_container, movieDetailFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
        gridview.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // nop
            }

            // approach as found here:  http://stackoverflow.com/a/16982317
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int _lastItem = firstVisibleItem + visibleItemCount;
                if (_lastItem > 0 && totalItemCount > 0)
                    if (_lastItem == MoviePosterAdapter.count && lastPositionInGrid < _lastItem) {
                        lastPositionInGrid = _lastItem;
                        // Last item is fully visible.
                        Log.d(MainActivity.TAG, "Now fetch next page from theMovieDB.");
                        new MovieAsyncTask((AppCompatActivity) getActivity(),gridview).execute();
                    }
            }
        });
        new MovieAsyncTask((AppCompatActivity) getActivity(),gridview).execute();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_sort_popular).setVisible(!sortModePopular);
        menu.findItem(R.id.action_sort_rating).setVisible(sortModePopular);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_sort_popular:
                menu.findItem(R.id.action_sort_rating).setVisible(true);
                menu.findItem(R.id.action_sort_popular).setVisible(false);

                MoviePosterAdapter.setSortModePopular(true);
                gridview.invalidateViews();
                gridview.smoothScrollToPosition(0);
                return true;
            case R.id.action_sort_rating:
                menu.findItem(R.id.action_sort_popular).setVisible(true);
                menu.findItem(R.id.action_sort_rating).setVisible(false);

                MoviePosterAdapter.setSortModePopular(false);
                gridview.invalidateViews();
                gridview.smoothScrollToPosition(0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
