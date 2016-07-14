package com.example.domen.mymanga.Models;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.domen.mymanga.Activity.AllMangaActivity;
import com.example.domen.mymanga.R;

import org.w3c.dom.Text;

/**
 * Created by domen on 14/07/2016.
 */
public class AllMangaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Cursor cursor;

    public AllMangaAdapter(Cursor cursor){

        this.cursor = cursor;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_manga_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        cursor.moveToPosition(position);
        final String title = cursor.getString(cursor.getColumnIndex(Contract.Manga.COLUMN_TITLE));
        final String image = cursor.getString(cursor.getColumnIndex(Contract.Manga.COLUMN_IMG));

        buildViewHolder((MyViewHolder) holder, title, image);
    }

    void buildViewHolder(MyViewHolder holder, String title, String imageUrl){

        holder.mangaTitle.setText(title);
        try {
            Glide.with(holder.mangaImage.getContext())
                    .load(imageUrl).fitCenter()
                    .error(R.mipmap.ic_launcher)
                    .into(holder.mangaImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView mangaImage;
        TextView mangaTitle;

        public MyViewHolder(View itemView) {
            super(itemView);

            mangaImage = (ImageView) itemView.findViewById(R.id.manga_cover);
            mangaTitle = (TextView) itemView.findViewById(R.id.manga_title);

        }
    }
}
