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


public class CategoriasHorizontRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Volume> volumes;
    private Context context;
    private List<Bitmap> thumbnails;


    private ListAdapterListener mListener;

    public interface ListAdapterListener { // create an interface
        void onClickAtOKButton(Bitmap capa, String idLivro, Volume.VolumeInfo dadosLivro); // create callback function
    }

    public CategoriasHorizontRecyclerAdapter(Context context, ListAdapterListener listAdapterListener) {
        this.context = context;
        this.mListener = listAdapterListener;
        this.volumes = new ArrayList<>();
        this.thumbnails = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public void setVolumes (List<Volume> volumes) {
        this.volumes = volumes;
    }

    public void setThumbnails (List<Bitmap> thumbnails) {
        this.thumbnails = thumbnails;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView;
        RecyclerView.ViewHolder vHolder = null;
        switch (viewType) {
            case 0:
                layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, null);
                vHolder = new RecyclerViewCategoriasHorizont(layoutView);
                break;
            case 1:
                layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, null);
                vHolder = new RecyclerViewCategoriasHorizont(layoutView);
                break;
            default:
                break;
        }
        return vHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        switch (holder.getItemViewType()) {
            case 0:
                RecyclerViewCategoriasHorizont recyclerViewCapaCategoria = (RecyclerViewCategoriasHorizont)holder;
                configureViewHolderCategoriasHorizont(recyclerViewCapaCategoria,position);
                break;
            case 1:
                RecyclerViewCategoriasHorizont recyclerViewCategoriasHorizont = (RecyclerViewCategoriasHorizont)holder;
                configureViewHolderCategoriasHorizont(recyclerViewCategoriasHorizont,position);
                break;
            default:
                break;
        }
    }

    private void configureViewHolderCapa (RecyclerViewCapaCategoria rvcc, int position) {
        rvcc.primeiraCapaCategoria.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.capa_categoria_aventura));
    }

    private void configureViewHolderCategoriasHorizont (final RecyclerViewCategoriasHorizont rvch, int position) {
        if (position < thumbnails.size()) {
            rvch.capaLivro.setVisibility(View.VISIBLE);
            rvch.capaLivro.setImageBitmap(thumbnails.get(position));
            rvch.progressBar.setVisibility(View.GONE);
            rvch.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickAtOKButton(thumbnails.get(rvch.getAdapterPosition()),volumes.get(rvch.getAdapterPosition()).getId(),volumes.get(rvch.getAdapterPosition()).getVolumeInfo());
                }
            });
            //rvch.tituloLivro.setText(volumes.get(rvch.getAdapterPosition()).getVolumeInfo().getTitle());
        } else if (position < volumes.size()) {
            rvch.capaLivro.setVisibility(View.GONE);
            //rvch.tituloLivro.setText(volumes.get(rvch.getAdapterPosition()).getVolumeInfo().getTitle());
        }
    }

    public int getTotalVolumes () {
        return this.volumes.size();
    }

    @Override
    public int getItemCount() {
        return this.thumbnails.size();
    }
}