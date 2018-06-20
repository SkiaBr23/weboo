package unb.com.br.booksblank.activities.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import unb.com.br.booksblank.R;
import unb.com.br.booksblank.adapters.UsuariosRecyclerAdapter;
import unb.com.br.booksblank.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisaUsuariosFragment extends Fragment {

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView rViewListaUsuarios;
    private UsuariosRecyclerAdapter usuariosRecyclerAdapter;

    private OnResultPesquisaListener onResultPesquisaListener;

    public PesquisaUsuariosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_pesquisa_usuarios, container, false);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        rViewListaUsuarios = (RecyclerView)root.findViewById(R.id.recycler_view);
        rViewListaUsuarios.setItemAnimator(new FadeInAnimator());
        rViewListaUsuarios.getItemAnimator().setAddDuration(500);

        rViewListaUsuarios.setHasFixedSize(true);
        rViewListaUsuarios.setLayoutManager(linearLayoutManager);

        usuariosRecyclerAdapter = new UsuariosRecyclerAdapter(getActivity(), new UsuariosRecyclerAdapter.ListAdapterListener() {
            @Override
            public void onClickAtOKButton(User userSelecionado) {
                System.out.println("Email: " + userSelecionado.getEmail());

            }
        });

        rViewListaUsuarios.setAdapter(usuariosRecyclerAdapter);

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
        void onUserSelected(User usuarioSelecionado);
    }

    public void setListUsuariosResult (List<User> usuarios, List<Bitmap> profilePic) {

        usuariosRecyclerAdapter = new UsuariosRecyclerAdapter(getActivity(), new UsuariosRecyclerAdapter.ListAdapterListener() {
            @Override
            public void onClickAtOKButton(User userSelecionado) {
                onResultPesquisaListener.onUserSelected(userSelecionado);
            }
        });

        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(usuariosRecyclerAdapter);
        alphaAdapter.setDuration(1000);
        alphaAdapter.setFirstOnly(false);

        usuariosRecyclerAdapter.setUsuarios(usuarios);
        usuariosRecyclerAdapter.setProfilePic(profilePic);

        rViewListaUsuarios.setAdapter(alphaAdapter);
    }

}
