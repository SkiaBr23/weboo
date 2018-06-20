package unb.com.br.booksblank.activities.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.services.books.model.Volume;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import unb.com.br.booksblank.R;
import unb.com.br.booksblank.adapters.BooksRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisaAutoresFragment extends Fragment implements BooksRecyclerAdapter.ListAdapterListener {

    private GridLayoutManager gridLayoutManager;
    private RecyclerView rView;
    private BooksRecyclerAdapter rcAdapter;


    private OnResultPesquisaListener onResultPesquisaListener;

    public PesquisaAutoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_pesquisa_autores, container, false);

        gridLayoutManager = new GridLayoutManager(getActivity(), 3);

        rView = (RecyclerView)root.findViewById(R.id.recycler_view);
        rView.setItemAnimator(new FadeInAnimator());
        rView.getItemAnimator().setAddDuration(500);

        rView.setHasFixedSize(true);
        rView.setLayoutManager(gridLayoutManager);

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnResultPesquisaListener) {
            onResultPesquisaListener = (OnResultPesquisaListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnResultPesquisaListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onResultPesquisaListener = null;
    }

    public interface OnResultPesquisaListener {
        void onLivroSelected(Bitmap capaLivro, String idLivro, Volume.VolumeInfo dadosLivro);
    }

    @Override
    public void onClickAtOKButton(Bitmap capa, String idLivro, Volume.VolumeInfo dadosLivro) {
    }

    public void setListLivrosResult (List<Volume> volumes, List<Bitmap> capas) {

        rcAdapter = new BooksRecyclerAdapter(getActivity(), volumes, new BooksRecyclerAdapter.ListAdapterListener() {
            @Override
            public void onClickAtOKButton(Bitmap capa, String idLivro, Volume.VolumeInfo dadosLivro) {
                onResultPesquisaListener.onLivroSelected(capa, idLivro, dadosLivro);
            }
        });

        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(rcAdapter);
        alphaAdapter.setDuration(1000);
        alphaAdapter.setFirstOnly(false);

        rcAdapter.setThumbnails(capas);

        rView.setAdapter(alphaAdapter);
    }

}
