package com.example.domen.mymanga.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.domen.mymanga.Models.Contract;
import com.example.domen.mymanga.R;

/**
 * Created by domen on 20/07/2016.
 */
public class MangaDetailFragment extends Fragment {

    View root;

    /*
    *   Creo una nuova istanza del fragment con un Bundle di dati come argomento
    */
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

        root = inflater.inflate(R.layout.fragment_detail, container, false);

        /*
        *   Estraggo le informazioni dal Bundle in input all'istanza
        */
        Bundle detailBundle = getArguments().getBundle("DetailBundle");
        String id = detailBundle.getString(Contract.Manga.COLUMN_MANGA_ID);
        String author = detailBundle.getString("author");
        String chapther = detailBundle.getString("chapter");
        String title = detailBundle.getString(Contract.Manga.COLUMN_TITLE);
        String img = detailBundle.getString(Contract.Manga.COLUMN_IMG);

        /*
        *   Se è stato richiesto il dettaglio di un manga
        */
        if (id != "") {

            /*
            *   Setto tutti i campi della view con i dati recuperati dal Bundle
            */
            TextView titleView = (TextView) root.findViewById(R.id.manga_detail_title);
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
            /*
            *   Mostro un Layer che indica che non ci sono manga selezionati (Per i Layout Tablet)
            */
            root.findViewById(R.id.not_selected_layer).setVisibility(View.VISIBLE);
        }

        return root;
    }
}
