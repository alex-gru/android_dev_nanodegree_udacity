package nanodegree.p1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import nanodegree.p1.data.Movie;
import nanodegree.p1.data.MovieDBAsyncTask;


public class MovieGridFragment extends Fragment implements AbsListView.OnScrollListener {
    public GridView gridview;
    private Menu menu;
    public static Movie[] movies_top_rated;
    public static Movie[] movies_most_popular;
    public static boolean sortModePopular = true;

    public MovieGridFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_moviegrid, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        toolbar.setTitle(getResources().getString(R.string.toolbar_title_moviedetail));
        toolbar.setDisplayHomeAsUpEnabled(false);

        gridview =(GridView) getActivity().findViewById(R.id.gridview);
        gridview.setAdapter(new MoviePosterAdapter(getActivity()));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                Bundle args = new Bundle();
                args.putInt("gridPosition", position);
                movieDetailFragment.setArguments(args);

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, movieDetailFragment)
                        .addToBackStack(null)
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .commit();
            }
        });
        if (movies_most_popular == null || movies_top_rated == null) {
            new MovieDBAsyncTask(gridview).execute();
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //nop
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        //TODO: handle further movie fetch here, if user scrolls till end of list
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_sort_popular).setVisible(false);
        menu.findItem(R.id.action_sort_rating).setVisible(true);
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
                return true;
            case R.id.action_sort_rating:
                menu.findItem(R.id.action_sort_popular).setVisible(true);
                menu.findItem(R.id.action_sort_rating).setVisible(false);

                MoviePosterAdapter.setSortModePopular(false);
                gridview.invalidateViews();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
