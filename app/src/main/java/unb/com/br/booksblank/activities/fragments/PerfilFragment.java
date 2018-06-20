package unb.com.br.booksblank.activities.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.model.Volume;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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
import unb.com.br.booksblank.activities.LoginActivity;
import unb.com.br.booksblank.activities.MainActivity;
import unb.com.br.booksblank.activities.SettingsActivity;
import unb.com.br.booksblank.adapters.BooksRecyclerAdapter;
import unb.com.br.booksblank.adapters.BooksUserRecyclerAdapter;
import unb.com.br.booksblank.adapters.CategoriasHorizontRecyclerAdapter;
import unb.com.br.booksblank.model.Shelf;
import unb.com.br.booksblank.model.User;
import unb.com.br.booksblank.model.UserScore;
import unb.com.br.booksblank.util.CircleTransform;


public class PerfilFragment extends Fragment implements BooksUserRecyclerAdapter.ListAdapterListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView txtUserName;
    private TextView txtLivrosDoados;
    private TextView txtLivrosRecebidos;
    private TextView txtLivrosEstante;
    private TextView txtLivrosTrocados;
    private ImageButton btnConfig;
    private ImageView imgProfile;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView rView;
    private BooksUserRecyclerAdapter rcAdapter;
    private static final String TAG = "PerfilFragment";
    private FloatingActionButton floatingActionButtonAdicionarLivro;
    private FirebaseAuth mAuth;
    private List<String> bookIds = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase = database.getReference();
    private ProgressDialog progressDialog;

    protected SharedPreferences sharedPreferences;
    protected static final String MY_PREFS = "MyPrefs";
    SharedPreferences.Editor editor;
    private final static String linkCapaFalha = "http://joerichard.net/wp-content/uploads/2015/08/stub.png";

    private OnVolumesUpdateListener onVolumesUpdateListener;

    public List<Volume> volumesEstantePessoal;
    public List<Bitmap> capasEstantePessoal;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVolumesUpdateListener) {
            onVolumesUpdateListener = (OnVolumesUpdateListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnVolumesUpdateListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onVolumesUpdateListener = null;
    }

    public interface OnVolumesUpdateListener {
        void onLivroSelected(Bitmap capaLivro, String id, Volume.VolumeInfo dadosLivro);
        void volumesPessoalUpdate (List<Volume> volumes);
        void capasPessoalUpdate (List<Bitmap> capas);
        void adicionarLivro();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sharedPreferences = getActivity().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        this.volumesEstantePessoal = new ArrayList<>();
        this.capasEstantePessoal = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_perfil, container, false);

        txtUserName = (TextView)root.findViewById(R.id.txtUserName);
        txtLivrosDoados = (TextView)root.findViewById(R.id.txtLivrosDoados);
        txtLivrosEstante = (TextView)root.findViewById(R.id.txtLivrosEstante);
        txtLivrosRecebidos = (TextView)root.findViewById(R.id.txtLivrosRecebidos);
        floatingActionButtonAdicionarLivro = (FloatingActionButton)root.findViewById(R.id.floatingActionButtonAdicionarLivro);
        txtLivrosTrocados = (TextView)root.findViewById(R.id.txtLivrosTrocados);
        btnConfig = (ImageButton) root.findViewById(R.id.iconConfiguracoes);
        Typeface font = Typeface.createFromAsset(this.getActivity().getAssets(), "futura_medium.ttf");
        txtUserName.setTypeface(font);


        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnConfig.animate();
                callSettingsActivity();
            }
        });

        imgProfile = (ImageView)root.findViewById(R.id.imgProfile);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user.getDisplayName() != null) {
                txtUserName.setText(user.getDisplayName());
            } else {
                txtUserName.setText(user.getEmail());
            }
            if (user.getPhotoUrl() != null) {
                Picasso.with(imgProfile.getContext()).load(user.getPhotoUrl()).transform(new CircleTransform()).into(imgProfile);
            }

        } else {
            txtUserName.setText("Unknown User");
        }

        /*
            BUSCANDO INFORMACOES DE SCORE DO USUARIO NO FIREBASE DB
        */

        final String userId = mAuth.getCurrentUser().getUid();

        mDatabase.child("users").child(userId).child("score").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserScore score = dataSnapshot.getValue(UserScore.class);
                        if(score != null){
                            txtLivrosDoados.setText(score.getBooksGiven().toString());
                            txtLivrosRecebidos.setText(score.getBooksTaken().toString());
                            // TODO: Esse livros trocados seria simplesmente livros doados+recebidos. Trocaremos pra 'coins'
                            txtLivrosTrocados.setText(score.getCoins().toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUserScore:onCancelled", databaseError.toException());
                    }
                });

        floatingActionButtonAdicionarLivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionButtonAdicionarLivro.animate();
                onVolumesUpdateListener.adicionarLivro();
            }
        });

        gridLayoutManager = new GridLayoutManager(getActivity(), 3);

        rView = (RecyclerView)root.findViewById(R.id.recycler_view_livros_usuario);
        rView.setItemAnimator(new FadeInAnimator());
        rView.getItemAnimator().setAddDuration(500);

        rView.setHasFixedSize(true);
        rView.setLayoutManager(gridLayoutManager);

        rcAdapter = new BooksUserRecyclerAdapter(getActivity(), new BooksUserRecyclerAdapter.ListAdapterListener() {
            @Override
            public void onClickAtOKButton(Bitmap capa, String id, Volume.VolumeInfo dadosLivro) {
                onVolumesUpdateListener.onLivroSelected(capa, id, dadosLivro);
            }
        });

        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(rcAdapter);
        alphaAdapter.setDuration(1000);
        alphaAdapter.setFirstOnly(false);
        rView.setAdapter(alphaAdapter);
        rView.setClickable(false);
        rView.setNestedScrollingEnabled(false);

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout)root.findViewById(R.id.fragmentPerfilLayout);
        final NestedScrollView nestedScrollView = (NestedScrollView)root.findViewById(R.id.nestedScrollViewPerfil);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)root.findViewById(R.id.collapsingToolbarPerfil);
        final AppBarLayout appBarLayout = (AppBarLayout)root.findViewById(R.id.appBarLayoutPerfil);


        if (((MainActivity)getActivity()).livrosEstantePessoal.size() > 0) {
            volumesEstantePessoal = ((MainActivity)getActivity()).livrosEstantePessoal;
            capasEstantePessoal = ((MainActivity)getActivity()).capasEstantePessoal;
            System.out.println("Size estante: " + volumesEstantePessoal.size());
            System.out.println("Size capas: " + capasEstantePessoal.size());
            rcAdapter = new BooksUserRecyclerAdapter(getActivity(), new BooksUserRecyclerAdapter.ListAdapterListener() {
                @Override
                public void onClickAtOKButton(Bitmap capa, String id, Volume.VolumeInfo dadosLivro) {
                    onVolumesUpdateListener.onLivroSelected(capa, id, dadosLivro);
                }
            });
            rcAdapter.setLivrosUsuario(volumesEstantePessoal);
            rcAdapter.setCapasUsuario(capasEstantePessoal);
            rView.setAdapter(rcAdapter);
            rView.setClickable(true);
            rcAdapter.notifyDataSetChanged();
        }

        final String userID = mAuth.getCurrentUser().getUid();

        mDatabase.child("shelves").child(userID).child("1").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Shelf shelfPessoal = dataSnapshot.getValue(Shelf.class);
                        if(shelfPessoal == null) {
                            // Cria nova shelf
                            shelfPessoal = new Shelf(userId,"1");
                            shelfPessoal.initializeBooks();
                            bookIds = shelfPessoal.getBooks();
                            if (bookIds.size() > 0) {
                                atualizaRecyclerView(bookIds);
                            }
                        }else {
                            bookIds = shelfPessoal.getBooks();
                            txtLivrosEstante.setText(shelfPessoal.getBookCount().toString());
                            if(bookIds == null){
                                atualizaRecyclerView(new ArrayList<String>(0));
                            } else {
                                atualizaRecyclerView(bookIds);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getWishList:onCancelled", databaseError.toException());
                    }
                });

        return root;
    }


    public void callSettingsActivity(){
        Intent mainIntent = new Intent(this.getActivity(),SettingsActivity.class);
        startActivity(mainIntent);
        this.getActivity().overridePendingTransition(R.anim.fade_in_one,R.anim.fade_out);
    }


    @Override
    public void onClickAtOKButton(Bitmap capa, String id, Volume.VolumeInfo dadosLivro) {

    }

    public void atualizaRecyclerView (List<String> bookIds) {
        new BuscarLivros().execute(bookIds);
    }

    private class BuscarLivros extends AsyncTask<List<String>, String, List<Volume>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (sharedPreferences.getBoolean("FirstAccessPerfil",true)) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Buscando livros pessoais disponíveis...");
                progressDialog.show();
                progressDialog.setCancelable(false);
            }


        }

        @Override
        protected List<Volume> doInBackground(List<String>... params) {
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            BooksSample booksSample = new BooksSample();
            List<Volume> retorno = new ArrayList<>();
            try {
                for (int i = 0; i < params[0].size(); i++) {
                    String bookID = params[0].get(i);
                    Volume volume = booksSample.getBookById(jsonFactory,bookID);
                    retorno.add(volume);
                }
                return retorno;
            } catch (IOException e) {
                System.err.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Volume> volumes) {
            if (sharedPreferences.getBoolean("FirstAccessPerfil",true)) {
                progressDialog.dismiss();
                editor = sharedPreferences.edit();
                if (volumes.size() > 0) {
                    editor.putBoolean("FirstAccessPerfil",false);
                }
                editor.apply();
            }
            rcAdapter.setLivrosUsuario(volumes);
            rcAdapter.notifyDataSetChanged();
            onVolumesUpdateListener.volumesPessoalUpdate(volumes);

            List<String> links = new ArrayList<>();
            for (int i = 0; i < volumes.size(); i++) {
                Volume.VolumeInfo.ImageLinks imageLinks = volumes.get(i).getVolumeInfo().getImageLinks();
                if (imageLinks != null) {
                    String linkSaida = getLinkValido(imageLinks);
                    if (linkSaida != null) {
                        links.add(linkSaida);
                    } else {
                        links.add(linkCapaFalha);
                    }
                } else {
                    links.add(linkCapaFalha);
                }
            }
            downloadImagens(links);
        }
    }

    public String getLinkValido (Volume.VolumeInfo.ImageLinks imageLinks) {

        if (imageLinks.getLarge() != null) {
            return imageLinks.getLarge();
        }

        if (imageLinks.getMedium() != null) {
            return imageLinks.getMedium();
        }

        if (imageLinks.getSmall() != null) {
            return  imageLinks.getSmall();
        }

        if (imageLinks.getThumbnail() != null) {
            return imageLinks.getThumbnail();
        }

        if (imageLinks.getSmallThumbnail() != null) {
            return imageLinks.getSmallThumbnail();
        }

        return null;

    }

    public void downloadImagens (List<String> links) {
        new ThumbnailsDownloader().execute(links);
    }

    public class ThumbnailsDownloader extends AsyncTask<List<String>, Void, List<Bitmap>> {

        @Override
        protected List<Bitmap> doInBackground(List<String>... link) {
            Bitmap downloaded;
            URL urlLogosNewsAPI;
            HttpURLConnection urlConnection = null;
            ArrayList<Bitmap> imgs = new ArrayList<>();
            try {
                for (String linkaux : link[0]) {
                    urlLogosNewsAPI = new URL(linkaux);
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
            if (sharedPreferences.getBoolean("FirstAccessPerfil",true)) {
                progressDialog.dismiss();
                editor = sharedPreferences.edit();
                if (imgs.size() > 0) {
                    editor.putBoolean("FirstAccessPerfil",false);
                }
                editor.apply();
            }
            super.onPostExecute(imgs);
            rcAdapter.setCapasUsuario(imgs);
            onVolumesUpdateListener.capasPessoalUpdate(imgs);
            rcAdapter.notifyDataSetChanged();
            rView.setClickable(true);
        }
    }
}
