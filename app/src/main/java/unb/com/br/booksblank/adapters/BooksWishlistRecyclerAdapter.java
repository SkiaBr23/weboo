package unb.com.br.booksblank.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;

import java.util.ArrayList;
import java.util.List;

import unb.com.br.booksblank.R;


public class BooksWishlistRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewBooksWishlist> {

    private List<Volume> volumes;
    private Context context;
    private List<Bitmap> thumbnails;


    private ListAdapterListener mListener;

    public interface ListAdapterListener { // create an interface
        void onClickAtOKButton(Bitmap capa, String id, Volume.VolumeInfo dadosLivro); // create callback function
    }

    public BooksWishlistRecyclerAdapter(Context context, ListAdapterListener listAdapterListener) {
        this.context = context;
        this.volumes = new ArrayList<>();
        thumbnails = new ArrayList<>();

        this.mListener = listAdapterListener;
    }

    public List<Volume> getVolumes () {
        return this.volumes;
    }

    public void addVolume (Volume volume) {
        this.volumes.add(volume);
    }

    public List<Bitmap> getThumbnails () {
        return this.thumbnails;
    }

    public void addThumbnails(Bitmap thumbnail) {
        this.thumbnails.add(thumbnail);
    }

    public void setThumbnails (List<Bitmap> thumbnails) {
        this.thumbnails = thumbnails;
    }

    public void setVolumes (List<Volume> volumes) {
        this.volumes = volumes;
    }

    @Override
    public RecyclerViewBooksWishlist onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_wishlist, null);
        RecyclerViewBooksWishlist rcv = new RecyclerViewBooksWishlist(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewBooksWishlist holder, final int position) {

        if (position < thumbnails.size()) {
            holder.capaLivro.setVisibility(View.VISIBLE);
            holder.capaLivro.setImageBitmap(thumbnails.get(position));
            holder.progressBar.setVisibility(View.GONE);
            //holder.tituloLivro.setText(volumes.get(holder.getAdapterPosition()).getVolumeInfo().getTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickAtOKButton(thumbnails.get(position),volumes.get(position).getId(),volumes.get(holder.getAdapterPosition()).getVolumeInfo());
                }
            });
        } else if (position < volumes.size()) {
            holder.capaLivro.setVisibility(View.GONE);
            //holder.tituloLivro.setText(volumes.get(holder.getAdapterPosition()).getVolumeInfo().getTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickAtOKButton(thumbnails.get(position),volumes.get(position).getId(),volumes.get(holder.getAdapterPosition()).getVolumeInfo());
                }
            });
        }

        //holder.capaLivro.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.capa_livro_teste));
        //holder.tituloLivro.setText("Título do livro sendo exibido");
        //if (position % 2 == 0) {
            //holder.txtDisponibilidade.setText("     X");
            //holder.txtDisponibilidade.setTextColor(ContextCompat.getColor(context,R.color.livroIndisponivel));
        //}
    }

    @Override
    public int getItemCount() {
        return this.volumes.size();
    }
}