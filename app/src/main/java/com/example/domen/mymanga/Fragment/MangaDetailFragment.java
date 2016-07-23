package com.example.domen.mymanga.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.domen.mymanga.Models.Contract;
import com.example.domen.mymanga.R;
import com.example.domen.mymanga.Service.MangaDetailService;
import com.example.domen.mymanga.Utils.AutoSpanRecyclerView;

/**
 * Created by domen on 20/07/2016.
 */
public class MangaDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    View root;

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
        root = inflater.inflate(R.layout.fragment_detail, container, false);

        if(id!=""){

            root.findViewById(R.id.not_selected_layer).setVisibility(View.GONE);

            Bundle mangaBundle = new Bundle();
            mangaBundle.putString(Contract.Manga.COLUMN_MANGA_ID,id);


            if (getActivity().getSupportLoaderManager().getLoader(Contract.DETAIL_LOADER) != null){
                getActivity().getSupportLoaderManager().destroyLoader(Contract.DETAIL_LOADER);
                getActivity().getSupportLoaderManager().initLoader(Contract.DETAIL_LOADER, mangaBundle, this);
            }else
                getActivity().getSupportLoaderManager().initLoader(Contract.DETAIL_LOADER, mangaBundle, this);

            Intent intent = new Intent(Intent.ACTION_SYNC, null, getContext(), MangaDetailService.class);
            intent.putExtra(Contract.Manga.COLUMN_MANGA_ID, id);
            getContext().startService(intent);
        }

        return root;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                Uri.parse(Contract.BASE_CONTENT_URI+"/"+Contract.Manga.TABLE_NAME+"/id/"+args.get(Contract.Manga.COLUMN_MANGA_ID)),
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

        data.moveToPosition(0);
        TextView title = (TextView)root.findViewById(R.id.manga_detail_title);
        title.setText(data.getString(data.getColumnIndex(Contract.Manga.COLUMN_TITLE)));

        ImageView mangaImage = (ImageView) root.findViewById(R.id.manga_cover_detail);
        String imageUrl = data.getString(data.getColumnIndex(Contract.Manga.COLUMN_IMG));

        try {
            Glide.with(mangaImage.getContext())
                    .load(imageUrl).fitCenter()
                    .error(R.mipmap.ic_launcher)
                    .into(mangaImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
