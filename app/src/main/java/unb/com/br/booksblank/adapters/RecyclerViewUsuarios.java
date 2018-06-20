package unb.com.br.booksblank.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import unb.com.br.booksblank.R;

/**
 * Created by maximillianfx on 05/05/17.
 */

public class RecyclerViewUsuarios extends RecyclerView.ViewHolder implements View.OnClickListener{

    public RoundedImageView fotoPerfilUsuario;
    public TextView txtNomeUsuario;

    public RecyclerViewUsuarios(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        fotoPerfilUsuario = (RoundedImageView) itemView.findViewById(R.id.fotoPerfilUsuario);
        txtNomeUsuario = (TextView)itemView.findViewById(R.id.txtNomeUsuario);
    }

    @Override
    public void onClick(View view) {
        //Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}