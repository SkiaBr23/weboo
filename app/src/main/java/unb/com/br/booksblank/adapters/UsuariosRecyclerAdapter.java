package unb.com.br.booksblank.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.services.books.model.Volume;

import java.util.ArrayList;
import java.util.List;

import unb.com.br.booksblank.R;
import unb.com.br.booksblank.model.User;


public class UsuariosRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewUsuarios> {

    private Context context;
    private List<User> usuarios;
    private List<Bitmap> profilePic;


    private ListAdapterListener mListener;

    public interface ListAdapterListener { // create an interface
        void onClickAtOKButton(User userSelecionado); // create callback function
    }

    public UsuariosRecyclerAdapter(Context context, ListAdapterListener listAdapterListener) {
        this.context = context;
        this.usuarios = new ArrayList<>();
        this.profilePic = new ArrayList<>();
        this.mListener = listAdapterListener;
    }

    public void setUsuarios (List<User> usuarios) {
        this.usuarios = usuarios;
    }

    public void setProfilePic (List<Bitmap> profilePic) {
        this.profilePic = profilePic;
    }

    @Override
    public RecyclerViewUsuarios onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_usuarios_pesquisa, null);
        RecyclerViewUsuarios rcv = new RecyclerViewUsuarios(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewUsuarios holder, final int position) {
        if (position < profilePic.size()) {
            holder.fotoPerfilUsuario.setImageBitmap(profilePic.get(holder.getAdapterPosition()));
            holder.txtNomeUsuario.setText(usuarios.get(holder.getAdapterPosition()).getFullName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickAtOKButton(usuarios.get(holder.getAdapterPosition()));
                }
            });
        }
        //holder.capaLivro.setImageBitmap(thumbnails.get(position));
        //holder.tituloLivro.setText(volumes.get(holder.getAdapterPosition()).getVolumeInfo().getTitle());
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickAtOKButton(thumbnails.get(position),volumes.get(holder.getAdapterPosition()).getId(),volumes.get(holder.getAdapterPosition()).getVolumeInfo());
                }
            });*/
    }

    @Override
    public int getItemCount() {
        return profilePic.size();
    }
}