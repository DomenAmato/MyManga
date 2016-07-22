package com.example.domen.mymanga.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.domen.mymanga.Models.Contract;
import com.example.domen.mymanga.R;
import com.example.domen.mymanga.Utils.AutoSpanRecyclerView;

/**
 * Created by domen on 20/07/2016.
 */
public class MangaDetailFragment extends Fragment {

    public static MangaDetailFragment newInstance(String id){

        Bundle args = new Bundle();
        args.putString(Contract.Manga.COLUMN_MANGA_ID, id);

        MangaDetailFragment mangaDetailFragment = new MangaDetailFragment();
        mangaDetailFragment.setArguments(args);

        return mangaDetailFragment;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final String id = getArguments().getString(Contract.Manga.COLUMN_MANGA_ID);
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        if(id!=""){

            view.findViewById(R.id.not_selected_layer).setVisibility(View.GONE);
        }

        return view;
    }
}
