package unb.com.br.booksblank.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import unb.com.br.booksblank.R;

/**
 * Created by maximillianfx on 05/05/17.
 */

public class RVHLivrosUsuarios extends RecyclerView.ViewHolder implements View.OnClickListener{

    public RoundedImageView fotoPerfilUsuario;
    public Button btnSolicitar;
    public TextView txtNomeUsuario;
    public TextView txtValorEdicao;
    public TextView txtValorConservacao;

    public RVHLivrosUsuarios(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        fotoPerfilUsuario = (RoundedImageView) itemView.findViewById(R.id.fotoPerfilUsuario);
        btnSolicitar = (Button)itemView.findViewById(R.id.btnSolicitar);
        txtNomeUsuario = (TextView)itemView.findViewById(R.id.txtNomeUsuario);
        txtValorEdicao = (TextView)itemView.findViewById(R.id.valorEdicao);
        txtValorConservacao = (TextView)itemView.findViewById(R.id.valorConservacao);
    }

    @Override
    public void onClick(View view) {
        //Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}