package com.example.domen.mymanga.Models;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.domen.mymanga.R;

import org.w3c.dom.Text;

/**
 * Created by domen on 14/07/2016.
 */
public class AllMAngaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Cursor cursor;

    public AllMAngaAdapter(Cursor cursor){

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
