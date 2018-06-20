package unb.com.br.booksblank.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;

import java.util.ArrayList;
import java.util.List;

import unb.com.br.booksblank.R;


public class BooksRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private List<Volume> volumes;
    private Context context;
    private List<Bitmap> thumbnails;


    private ListAdapterListener mListener;

    public interface ListAdapterListener { // create an interface
        void onClickAtOKButton(Bitmap capa, String id, Volume.VolumeInfo dadosLivro); // create callback function
    }

    public BooksRecyclerAdapter(Context context, List<Volume> volumes, ListAdapterListener listAdapterListener) {
        this.volumes = volumes;
        this.context = context;
        this.thumbnails = new ArrayList<>();
        for (int i = 0; i < volumes.size(); i++) {
            thumbnails.add(null);
        }
        this.mListener = listAdapterListener;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    public void setThumbnails (List<Bitmap> thumbnails) {
        this.thumbnails = thumbnails;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolders holder, final int position) {
        //holder.nomeLivro.setText(volumes.getItems().get(position).getVolumeInfo().getTitle());
        if (position < thumbnails.size()) {
            holder.progressBar.setVisibility(View.GONE);
            holder.capaLivro.setImageBitmap(thumbnails.get(position));
            //holder.tituloLivro.setText(volumes.get(holder.getAdapterPosition()).getVolumeInfo().getTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickAtOKButton(thumbnails.get(position),volumes.get(holder.getAdapterPosition()).getId(),volumes.get(holder.getAdapterPosition()).getVolumeInfo());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.volumes.size();
    }
}