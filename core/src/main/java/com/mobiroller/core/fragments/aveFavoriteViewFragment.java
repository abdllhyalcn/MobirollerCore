package com.mobiroller.core.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.mobiroller.core.R2;
import com.mobiroller.core.adapters.FavoriteAdapter;
import com.mobiroller.core.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ealtaca on 6/30/18.
 */

public class aveFavoriteViewFragment extends BaseModuleFragment {

    @BindView(R2.id.favorite_recycler_view)
    RecyclerView recyclerView;
    @BindView(R2.id.favorite_main_layout)
    RelativeLayout mainLayout;
    @BindView(R2.id.favorite_overlay_layout)
    RelativeLayout overlayLayout;
    @BindView(R2.id.empty_view)
    RelativeLayout emptyView;
    @BindView(R2.id.favorite_empty_image)
    ImageView emptyImageView;
    @BindView(R2.id.favorite_empty_text)
    TextView emptyTextView;

    private FavoriteAdapter mFavoriteAdapter;

    private String lastSearchedQuery;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_favorite, container, false);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setFavoriteAdapter();
        if (mainLayout != null) {
            bannerHelper.addBannerAd(mainLayout, overlayLayout);
        }
    }

    private void setFavoriteAdapter() {
        mFavoriteAdapter = new FavoriteAdapter(getActivity());
        recyclerView.setAdapter(mFavoriteAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));

        if (mFavoriteAdapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            emptyImageView.setColorFilter(sharedPrefHelper.getActionBarColor(), PorterDuff.Mode.SRC_ATOP);
            emptyTextView.setTextColor(sharedPrefHelper.getActionBarColor());
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        if (lastSearchedQuery != null) {
            mFavoriteAdapter.getFilter().filter(lastSearchedQuery);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        ImageView iconSearch = searchView.findViewById(androidx.appcompat.R.id.search_button);
        iconSearch.setColorFilter(getResources().getColor(R.color.white));
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                lastSearchedQuery = query;
                mFavoriteAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                lastSearchedQuery = query;
                mFavoriteAdapter.getFilter().filter(query);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_search || super.onOptionsItemSelected(item);
    }
}
