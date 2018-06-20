package unb.com.br.booksblank.adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import unb.com.br.booksblank.R;

/**
 * Created by maximillianfx on 05/05/17.
 */

public class RecyclerViewTransactions extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView tituloLivroTransaction;
    public ImageView capaLivroTransaction;
    public TextView edicaoLivroTransaction;
    public TextView conservacaoLivroTransaction;
    public RoundedImageView fotoPerfilUsuarioSolicitante;
    public RoundedImageView fotoPerfilUsuarioSolicitado;
    public TextView nomeUsuarioSolicitado;
    public TextView txtNumeroUsuarioSolicitado;
    public Button btnStatusOK;
    public Button btnStatusFail;

    public RecyclerViewTransactions(View itemView) {
        super(itemView);
        tituloLivroTransaction = (TextView)itemView.findViewById(R.id.tituloLivroTransaction);
        capaLivroTransaction = (ImageView)itemView.findViewById(R.id.capaLivroTransaction);
        edicaoLivroTransaction = (TextView) itemView.findViewById(R.id.edicaoLivroTransaction);
        conservacaoLivroTransaction = (TextView) itemView.findViewById(R.id.conservacaoLivroTransaction);
        fotoPerfilUsuarioSolicitante = (RoundedImageView) itemView.findViewById(R.id.fotoPerfilUsuarioSolicitante);
        fotoPerfilUsuarioSolicitado = (RoundedImageView) itemView.findViewById(R.id.fotoPerfilUsuarioSolicitado);
        nomeUsuarioSolicitado = (TextView) itemView.findViewById(R.id.nomeUsuarioSolicitado);
        txtNumeroUsuarioSolicitado = (TextView) itemView.findViewById(R.id.txtNumeroUsuarioSolicitado);
        btnStatusOK = (Button) itemView.findViewById(R.id.btnConfirmar);
        btnStatusFail = (Button) itemView.findViewById(R.id.btnStatusFail);

    }

    @Override
    public void onClick(View view) {
        //Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}