package unb.com.br.booksblank.activities.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import unb.com.br.booksblank.R;
import unb.com.br.booksblank.activities.BooksSample;
import unb.com.br.booksblank.adapters.BooksRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisaLivrosFragment extends Fragment implements BooksRecyclerAdapter.ListAdapterListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private GridLayoutManager gridLayoutManager;
    private RecyclerView rView;
    private BooksRecyclerAdapter rcAdapter;


    private OnResultPesquisaListener onResultPesquisaListener;


    public PesquisaLivrosFragment() {
        // Required empty public constructor
    }

    public static PesquisaLivrosFragment newInstance(String param1, String param2) {
        PesquisaLivrosFragment fragment = new PesquisaLivrosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_pesquisa_livros, container, false);

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
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onResultPesquisaListener = null;
    }

    @Override
    public void onClickAtOKButton(Bitmap capa, String idLivro, Volume.VolumeInfo dadosLivro) {
    }

    public interface OnResultPesquisaListener {
        void onLivroSelected(Bitmap capaLivro, String idLivro, Volume.VolumeInfo dadosLivro);
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
