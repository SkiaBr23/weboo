package unb.com.br.booksblank.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;

import java.util.ArrayList;
import java.util.List;

import unb.com.br.booksblank.R;


public class BooksUserRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewBooksUser> {

    private List<Volume> volumes;
    private Context context;
    private List<Bitmap> thumbnails;


    private ListAdapterListener mListener;

    public interface ListAdapterListener { // create an interface
        void onClickAtOKButton(Bitmap capa, String id, Volume.VolumeInfo dadosLivro); // create callback function
    }

    public BooksUserRecyclerAdapter(Context context,  ListAdapterListener listAdapterListener) {
        this.context = context;
        this.mListener= listAdapterListener;

        this.volumes = new ArrayList<>();
        this.thumbnails = new ArrayList<>();
    }

    public void setLivrosUsuario (List<Volume> volumes) {
        this.volumes = volumes;
    }

    public void setCapasUsuario (List<Bitmap> capas) {
        this.thumbnails = capas;
    }

    @Override
    public RecyclerViewBooksUser onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, null);
        RecyclerViewBooksUser rcv = new RecyclerViewBooksUser(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewBooksUser holder, final int position) {
        if (position < thumbnails.size()) {
            holder.capaLivro.setVisibility(View.VISIBLE);
            holder.capaLivro.setImageBitmap(thumbnails.get(position));
            holder.progressBar.setVisibility(View.GONE);
           // holder.tituloLivro.setText(volumes.get(holder.getAdapterPosition()).getVolumeInfo().getTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickAtOKButton(thumbnails.get(position), volumes.get(holder.getAdapterPosition()).getId(), volumes.get(holder.getAdapterPosition()).getVolumeInfo());
                }
            });
        } else if (position < volumes.size()) {
            holder.capaLivro.setVisibility(View.GONE);
            //holder.tituloLivro.setText(volumes.get(holder.getAdapterPosition()).getVolumeInfo().getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return volumes.size();
    }
}