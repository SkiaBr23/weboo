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


public class LivrosUsuariosRecyclerAdapter extends RecyclerView.Adapter<RVHLivrosUsuarios> {

    private Context context;
    private List<User> users;
    private List<String> conditions;
    private List<String> editions;
    private List<Bitmap> profilePictures;

    private ListAdapterListener mListener;

    public interface ListAdapterListener { // create an interface
        void onClickAtSolicitarButton(User user, String bookCondition, String bookEdition); // create callback function
    }

    public LivrosUsuariosRecyclerAdapter(Context context, ListAdapterListener listAdapterListener) {
        this.context = context;
        this.mListener = listAdapterListener;
        this.users = new ArrayList<>();
        this.conditions = new ArrayList<>();
        this.editions = new ArrayList<>();
        this.profilePictures = new ArrayList<>();
    }

    @Override
    public RVHLivrosUsuarios onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_livro_usuario, null);
        RVHLivrosUsuarios rcv = new RVHLivrosUsuarios(layoutView);
        return rcv;
    }

    public void setConditions(List<String> conditions) {
        this.conditions = conditions;
    }

    public void setEditions(List<String> editions) {
        this.editions = editions;
    }

    public void setUsuariosLivro (List<User> users) {
        this.users = users;
    }

    public void setUsuariosFotos (List<Bitmap> profilePictures) {
        this.profilePictures = profilePictures;
    }

    @Override
    public void onBindViewHolder(final RVHLivrosUsuarios holder, final int position) {
        if (position < profilePictures.size()) {
            holder.fotoPerfilUsuario.setImageBitmap(profilePictures.get(holder.getAdapterPosition()));
            holder.fotoPerfilUsuario.setVisibility(View.VISIBLE);
            holder.txtNomeUsuario.setText(users.get(holder.getAdapterPosition()).getFullName());
            holder.txtValorConservacao.setText(conditions.get(holder.getAdapterPosition()));
            holder.txtValorEdicao.setText(editions.get(holder.getAdapterPosition()));

            holder.btnSolicitar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickAtSolicitarButton(users.get(holder.getAdapterPosition()), conditions.get(holder.getAdapterPosition()), editions.get(holder.getAdapterPosition()));
                }
            });
        } else if (position < users.size()) {
            //holder.setVisibility(View.GONE);
            //holder.tituloLivro.setText(volumes.get(holder.getAdapterPosition()).getVolumeInfo().getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}