package com.example.domen.mymanga.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.domen.mymanga.BroadcastReceiver.DownloadResultReceiver;
import com.example.domen.mymanga.Models.Contract;
import com.example.domen.mymanga.R;
import com.example.domen.mymanga.Service.MangaDetailService;
import com.example.domen.mymanga.Utils.AutoSpanRecyclerView;

/**
 * Created by domen on 20/07/2016.
 */
public class MangaDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    View root;

    public static MangaDetailFragment newInstance(Bundle mangaBundle){

        Bundle args = new Bundle();
        args.putBundle("DetailBundle", mangaBundle);

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

        final Bundle detailBundle = getArguments().getBundle("DetailBundle");
        //final String id = getArguments().getString(Contract.Manga.COLUMN_MANGA_ID);
        root = inflater.inflate(R.layout.fragment_detail, container, false);
        String id = detailBundle.getString(Contract.Manga.COLUMN_MANGA_ID);
        String author = detailBundle.getString("author");
        String chapther = detailBundle.getString("chapter");
        String title = detailBundle.getString(Contract.Manga.COLUMN_TITLE);
        String img = detailBundle.getString(Contract.Manga.COLUMN_IMG);

        if (id != "") {

            TextView titleView = (TextView) root.findViewById(R.id.manga_detail_title2);
            titleView.setText(title);

            ImageView mangaImage = (ImageView) root.findViewById(R.id.manga_cover_detail);


            try {
                Glide.with(mangaImage.getContext())
                        .load(img).fitCenter()
                        .error(R.mipmap.ic_launcher)
                        .into(mangaImage);
            } catch (Exception e) {
                e.printStackTrace();
            }


            TextView authorView = (TextView) root.findViewById(R.id.manga_detail_author);
            authorView.setText(author);
            TextView chapterView = (TextView) root.findViewById(R.id.manga_detail_chapter);
            chapterView.setText("Numero Capitoli: " + chapther);

        }else{
            root.findViewById(R.id.not_selected_layer).setVisibility(View.VISIBLE);
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
        TextView title = (TextView) root.findViewById(R.id.manga_detail_title2);
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
