package unb.com.br.booksblank.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import unb.com.br.booksblank.R;

/**
 * Created by maximillianfx on 05/05/17.
 */

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView tituloLivro;
    public ImageView capaLivro;
    public LinearLayout progressBar;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        tituloLivro = (TextView)itemView.findViewById(R.id.txtTituloLivro);
        capaLivro = (ImageView)itemView.findViewById(R.id.capaLivro);
        progressBar = (LinearLayout)itemView.findViewById(R.id.progressBarCard);
    }

    @Override
    public void onClick(View view) {
        //Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}