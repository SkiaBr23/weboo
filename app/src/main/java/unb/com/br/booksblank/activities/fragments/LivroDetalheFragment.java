package unb.com.br.booksblank.activities.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import unb.com.br.booksblank.R;
import unb.com.br.booksblank.activities.LoginActivity;
import unb.com.br.booksblank.activities.MainActivity;
import unb.com.br.booksblank.adapters.BooksRecyclerAdapter;
import unb.com.br.booksblank.adapters.LivrosUsuariosRecyclerAdapter;
import unb.com.br.booksblank.model.BookInfo;
import unb.com.br.booksblank.model.Shelf;
import unb.com.br.booksblank.model.User;
import unb.com.br.booksblank.model.UserInfo;
import unb.com.br.booksblank.model.UserScore;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LivroDetalheFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LivroDetalheFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LivroDetalheFragment extends Fragment implements LivrosUsuariosRecyclerAdapter.ListAdapterListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SHELF_WISHLIST = "2";
    private static final String SHELF_AVAILABLE = "1";
    private static final String SHELF_TRANSACTION = "3";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "LivroDetalheFragment";
    private static final String DEFAULT_PIC = "https://success.salesforce.com/resource/1498435200000/sharedlayout/img/new-user-image-default.png";

    private boolean isWishListed = false;
    // TODO: Rename and change types of parameters
    private String tituloLivro;
    private String autorLivro;
    private String anoLivro;
    private String categoriaLivro;
    private String urlCapaLivro;

    private String idLivro;
    private Bitmap capaLivro;
    private ImageView imgCapaLivro;
    private TextView txtTituloLivro;
    private TextView txtAutorLivro;
    private TextView txtAnoLivro;
    private TextView txtCategoria;
    private TextView txtDisponiveisValor;
    private List<String> userIds;

    private FloatingActionButton buttonWishlist;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView rViewListaUsuarios;
    private LivrosUsuariosRecyclerAdapter livrosUsuariosRecyclerAdapter;

    private Toolbar toolbarLivroDetalhado;
    private OnFragmentInteractionListener mListener;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    public LivroDetalheFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LivroDetalheFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LivroDetalheFragment newInstance(String param1, String param2) {
        LivroDetalheFragment fragment = new LivroDetalheFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idLivro = getArguments().getString("idLivro");
            tituloLivro = getArguments().getString("tituloLivro");
            if (tituloLivro.length() > 35) {
                tituloLivro = tituloLivro.substring(0,34)+"...";
            }
            anoLivro = getArguments().getString("anoLivro");
            autorLivro = getArguments().getString("autorLivro");
            categoriaLivro = getArguments().getString("categoriaLivro");
            urlCapaLivro = getArguments().getString("urlCapaLivro");
            capaLivro = getArguments().getParcelable("capaLivro");
            formatarAnoLivro();
        }
    }

    public void formatarAnoLivro () {
        String dataFormatada;
        if (anoLivro != null) {
            if (anoLivro.length() > 4) {
                if (anoLivro.length() == 7) {
                    dataFormatada = anoLivro.substring(5, 7) + "/" + anoLivro.substring(0, 4);
                    anoLivro = dataFormatada;
                } else {
                    dataFormatada = anoLivro.substring(8, 10) + "/" + anoLivro.substring(5, 7) + "/" + anoLivro.substring(0, 4);
                    anoLivro = dataFormatada;
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_livro_collapse_detalhe, container, false);

        toolbarLivroDetalhado = (Toolbar)root.findViewById(R.id.toolbarLivroDetalhado);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarLivroDetalhado);

        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        txtTituloLivro = (TextView)root.findViewById(R.id.txtTituloLivro);
        txtAutorLivro = (TextView)root.findViewById(R.id.txtAutorLivro);
        txtAnoLivro = (TextView)root.findViewById(R.id.txtAnoLivro);
        txtCategoria = (TextView)root.findViewById(R.id.txtCategoria);
        txtDisponiveisValor = (TextView)root.findViewById(R.id.txtDisponiveisValor);
        imgCapaLivro = (ImageView)root.findViewById(R.id.imgCapaLivro);
        buttonWishlist = (FloatingActionButton)root.findViewById(R.id.buttonWishlist);
        toolbarLivroDetalhado.setTitle("");

        txtTituloLivro.setText(tituloLivro);
        txtAutorLivro.setText(autorLivro);
        txtAnoLivro.setText(anoLivro);
        imgCapaLivro.setImageBitmap(capaLivro);
        txtDisponiveisValor.setText("-");
        txtCategoria.setText(categoriaLivro);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)root.findViewById(R.id.collapsingToolbarLivro);
        AppBarLayout appBarLayout = (AppBarLayout)root.findViewById(R.id.appBarLayoutLivroDetalhado);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(tituloLivro);
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

        linearLayoutManager = new LinearLayoutManager(getActivity());

        rViewListaUsuarios = (RecyclerView)root.findViewById(R.id.recycler_view_listaUsuarios);
        rViewListaUsuarios.setItemAnimator(new FadeInAnimator());
        rViewListaUsuarios.getItemAnimator().setAddDuration(500);

        rViewListaUsuarios.setHasFixedSize(true);
        rViewListaUsuarios.setLayoutManager(linearLayoutManager);

        livrosUsuariosRecyclerAdapter = new LivrosUsuariosRecyclerAdapter(getActivity(),new LivrosUsuariosRecyclerAdapter.ListAdapterListener () {
            @Override
            public void onClickAtSolicitarButton(User user, String bookCondition, String bookEdition) {
                mListener.onSolicitarLivro(user, bookCondition, bookEdition, idLivro, tituloLivro, urlCapaLivro);
            }
        });

        rViewListaUsuarios.setAdapter(livrosUsuariosRecyclerAdapter);


        buttonWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addOrRemoveBookWishlist(idLivro,tituloLivro);
            }
        });


        // Estado do botão wishlist

         /*
            BUSCANDO INFORMACOES DE SCORE DO USUARIO NO FIREBASE DB
        */

        final String userId = mAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();

        mDatabase.child("shelves").child(userId).child(SHELF_WISHLIST).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Shelf wishShelf = dataSnapshot.getValue(Shelf.class);
                        if(wishShelf != null && wishShelf.getBooks() != null && wishShelf.getBooks().contains(idLivro)){
                           buttonWishlist.setImageResource(R.drawable.ic_footer_favoritos_vermelho);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getWishShelf:onCancelled", databaseError.toException());
                    }
                });

        userIds = new ArrayList<>();
        mDatabase.child("showcase").child(idLivro).child("users").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<List<UserInfo>> genericTypeIndicator =new GenericTypeIndicator<List<UserInfo>>(){};
                        final List<UserInfo> usersInfos = dataSnapshot.getValue(genericTypeIndicator);
                        if(usersInfos != null && !usersInfos.isEmpty()){
                            mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    List<User> tradingUsers = new ArrayList<>();
                                    List<String> tradingConditions = new ArrayList<>();
                                    List<String> tradingEditions = new ArrayList<>();
                                    for (UserInfo userInfo : usersInfos) {
                                        if(userInfo.getBookStatus().equals("AVAILABLE")) {
                                            tradingUsers.add(dataSnapshot.child(userInfo.getUserId()).getValue(User.class));
                                            tradingConditions.add(userInfo.getBookCondition());
                                            tradingEditions.add(userInfo.getBookEdition());
                                        }
                                    }
                                    atualizaUserCards(tradingUsers, tradingConditions, tradingEditions);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            } else {
                            atualizaUserCards(new ArrayList<User>(0),new ArrayList<String>(0),new ArrayList<String>(0));
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getTradingUsers:onCancelled", databaseError.toException());
                    }
                });

        return root;
    }

    private void atualizaUserCards(List<User> tradingUsers, List<String> tradingConditions, List<String> tradingEditions) {
        if(tradingUsers.size() > 1) {
            txtDisponiveisValor.setText(tradingUsers.size() + " usuários");
        } else {
            txtDisponiveisValor.setText(tradingUsers.size() + " usuário");
        }
        downloadImagens(tradingUsers);
        livrosUsuariosRecyclerAdapter.setUsuariosLivro(tradingUsers);
        livrosUsuariosRecyclerAdapter.setConditions(tradingConditions);
        livrosUsuariosRecyclerAdapter.setEditions(tradingEditions);
        livrosUsuariosRecyclerAdapter.notifyDataSetChanged();
    }

    public void downloadImagens (List<User> users) {
        new ThumbnailsDownloader().execute(users);
    }

    private boolean addOrRemoveBookWishlist(final String idLivro, String tituloLivro){
        final String userId = mAuth.getCurrentUser().getUid();

        mDatabase.child("shelves").child(userId).child(SHELF_WISHLIST).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Shelf shelf = dataSnapshot.getValue(Shelf.class);
                        // Shelf nao existe ainda
                        if(shelf == null){
                            Toast.makeText(getActivity(), "Shelf nao encontrada, criando...",
                                    Toast.LENGTH_SHORT).show();
                            // Cria nova shelf
                            shelf = new Shelf(userId,SHELF_WISHLIST);
                        }
                        if(shelf.getBooks() != null && shelf.getBooks().contains(idLivro)){
                            // Remove livro da lista
                            Toast.makeText(getActivity(), "Removendo livro da lista!",
                                    Toast.LENGTH_SHORT).show();
                            shelf.getBooks().remove(idLivro);
                            shelf.setBookCount(shelf.getBookCount()-1);
                            buttonWishlist.setImageResource(R.drawable.ic_footer_favoritos_nselecionado);
                        } else {
                            // Adiciona livro na lista
                            Toast.makeText(getActivity(), "Adicionando livro da lista!",
                                    Toast.LENGTH_SHORT).show();
                            if(shelf.getBooks() == null){
                                shelf.initializeBooks();
                            }
                            shelf.getBooks().add(idLivro);
                            shelf.setBookCount(shelf.getBookCount()+1);
                            buttonWishlist.setImageResource(R.drawable.ic_footer_favoritos_vermelho);
                            }
                        // Atualiza dados
                        mDatabase.child("shelves").child(userId).child(SHELF_WISHLIST).setValue(shelf);
                        }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "Erro",
                                Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "addRemoveBookWishlist:onCancelled", databaseError.toException());
                    }
                });
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClickAtSolicitarButton(User user, String bookCondition, String bookEdition) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onSolicitarLivro(User user, String bookCondition, String bookEdition, String idLivro, String bookTitle, String bookCoverUrl);
    }



    public void downloadFotos (List<User> users) {
        new ThumbnailsDownloader().execute(users);
    }

    public class ThumbnailsDownloader extends AsyncTask<List<User>, Void, List<Bitmap>> {

        @Override
        protected List<Bitmap> doInBackground(List<User>... users) {
            Bitmap downloaded;
            URL urlLogosNewsAPI;
            HttpURLConnection urlConnection = null;
            ArrayList<Bitmap> imgs = new ArrayList<>();
            try {
                for (User linkaux : users[0]) {
                    String url = linkaux.getProfilePictureUrl() != null ? linkaux.getProfilePictureUrl() : DEFAULT_PIC;
                    urlLogosNewsAPI = new URL(url);
                    urlConnection = (HttpURLConnection) urlLogosNewsAPI.openConnection();
                    urlConnection.connect();
                    InputStream inputStream = urlConnection.getInputStream();
                    downloaded = BitmapFactory.decodeStream(inputStream);
                    imgs.add(downloaded);
                }
                return imgs;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bitmap> imgs) {
            super.onPostExecute(imgs);
            livrosUsuariosRecyclerAdapter.setUsuariosFotos(imgs);
            livrosUsuariosRecyclerAdapter.notifyDataSetChanged();
        }
    }
}
