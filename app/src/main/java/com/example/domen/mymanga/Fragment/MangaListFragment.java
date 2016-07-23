package com.example.domen.mymanga.Fragment;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.domen.mymanga.Activity.AllMangaActivity;
import com.example.domen.mymanga.Activity.MangaDetailActivity;
import com.example.domen.mymanga.Activity.SearchResultActivity;
import com.example.domen.mymanga.Models.AllMangaAdapter;
import com.example.domen.mymanga.Models.Contract;
import com.example.domen.mymanga.R;
import com.example.domen.mymanga.Utils.AutoSpanRecyclerView;

/**
 * Created by domen on 20/07/2016.
 */
public class MangaListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private Communication listener;

    private AutoSpanRecyclerView mangaList;
    private boolean isTablet = false;
    private AllMangaActivity MyActivity;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (Communication)context;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_manga_list, container, false);
        MyActivity = (AllMangaActivity) getActivity();


        mangaList = (AutoSpanRecyclerView) view.findViewById(R.id.all_manga_list);
        mangaList.setGridLayoutManager(RecyclerView.VERTICAL, R.layout.all_manga_item, 1);
        mangaList.setHasFixedSize(true);

        getActivity().getSupportLoaderManager().initLoader(Contract.LIST_LOADER, null, this);

        return view;
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                Uri.parse(Contract.BASE_CONTENT_URI+"/"+Contract.Manga.TABLE_NAME),
                new String[]{
                        Contract.Manga.COLUMN_MANGA_ID,
                        Contract.Manga.COLUMN_TITLE,
                        Contract.Manga.COLUMN_IMG
                },
                null, null, null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        AllMangaAdapter myAdapter = new AllMangaAdapter(data);

        myAdapter.setOnItemClickListener(new AllMangaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id) {
                if(listener!=null)
                    listener.onItemChoosed(id);

                //isTablet = MyActivity.isTablet();
                //if (!isTablet) {
                //    FragmentManager fm = getFragmentManager();
                //    MangaDetailFragment df = new MangaDetailFragment();
                //    fm.beginTransaction().replace(R.id.container, df).addToBackStack("MyManga").commit();

                //}
            }
        });

        mangaList.setAdapter(myAdapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public interface Communication{
        void onItemChoosed(String id);
    }
}
