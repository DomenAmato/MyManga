package com.example.domen.mymanga.Models;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.domen.mymanga.R;

/**
 * Created by domen on 14/07/2016.
 */
public class AllMangaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Cursor cursor;
    private OnItemClickListener listener;

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
        final String id = cursor.getString(cursor.getColumnIndex(Contract.Manga.COLUMN_MANGA_ID));


        buildViewHolder((MyViewHolder) holder, id, title, image);
    }

    void buildViewHolder(MyViewHolder holder, final String id, String title, String imageUrl){

        holder.mangaTitle.setText(title);
        try {
            Glide.with(holder.mangaImage.getContext())
                    .load(imageUrl).fitCenter()
                    .error(R.mipmap.ic_launcher)
                    .into(holder.mangaImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null)
                    listener.onItemClick(id);
            }
        });


    }

    @Override
    public int getItemCount() {
        if (cursor != null)
            return cursor.getCount();
        else return -1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView mangaImage;
        TextView mangaTitle;
        LinearLayout rootLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            mangaImage = (ImageView) itemView.findViewById(R.id.manga_cover);
            mangaTitle = (TextView) itemView.findViewById(R.id.manga_title);
            rootLayout = (LinearLayout) itemView.findViewById(R.id.manga_item);
            //Log.v("MyMangaAdapeter", rootLayout.toString());
        }
    }

    public void swapCursor(Cursor cursor) {

        if (cursor == null) {
            this.cursor.close();
        } else if (cursor != this.cursor && this.cursor != null) {
            this.cursor.close();
            this.cursor = cursor;
            notifyDataSetChanged();
        } else {
            this.cursor = cursor;
            notifyDataSetChanged();

        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }

    public interface OnItemClickListener{
        void onItemClick(String id);
    }

}
