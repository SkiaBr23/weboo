package unb.com.br.booksblank.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import unb.com.br.booksblank.R;

/**
 * Created by maximillianfx on 05/05/17.
 */

public class RecyclerViewCapaCategoria extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ImageView primeiraCapaCategoria;

    public RecyclerViewCapaCategoria(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        primeiraCapaCategoria = (ImageView)itemView.findViewById(R.id.primeiraCapaCategoria);
    }

    @Override
    public void onClick(View view) {
        //Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}