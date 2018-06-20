package unb.com.br.booksblank.activities.fragments;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import unb.com.br.booksblank.R;
import unb.com.br.booksblank.activities.BooksSample;
import unb.com.br.booksblank.activities.MainActivity;
import unb.com.br.booksblank.adapters.AdaptersCategoria.AcaoRecyclerAdapter;
import unb.com.br.booksblank.adapters.AdaptersCategoria.AventuraRecyclerAdapter;
import unb.com.br.booksblank.adapters.AdaptersCategoria.FiccaoRecyclerAdapter;
import unb.com.br.booksblank.adapters.AdaptersCategoria.TerrorRecyclerAdapter;
import unb.com.br.booksblank.adapters.BooksUserRecyclerAdapter;
import unb.com.br.booksblank.adapters.CategoriasHorizontRecyclerAdapter;
import unb.com.br.booksblank.model.Shelf;

public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public CoordinatorLayout coordinatorLayout;

    public LinearLayoutManager linearLayoutManagerBaseadoBusca;
    public RecyclerView rViewBaseadoBusca;

    public LinearLayoutManager linearLayoutManagerCategoriaFiccao;
    public RecyclerView rViewCategoriaFiccao;
    public FiccaoRecyclerAdapter ficcaoRecyclerAdapter;

    public LinearLayoutManager linearLayoutManagerCategoriaAcao;
    public RecyclerView rViewCategoriaAcao;
    public AcaoRecyclerAdapter acaoRecyclerAdapter;

    public LinearLayoutManager linearLayoutManagerCategoriaAventura;
    public RecyclerView rViewCategoriaAventura;
    public AventuraRecyclerAdapter aventuraRecyclerAdapter;

    public LinearLayoutManager linearLayoutManagerCategoriaTerror;
    public RecyclerView rViewCategoriaTerror;
    public TerrorRecyclerAdapter terrorRecyclerAdapter;

    public FloatingActionButton actionButtonAdicionar;

    public CategoriasHorizontRecyclerAdapter rcAdapter;

    public AppBarLayout appBarLayout;
    public CollapsingToolbarLayout collapsingToolbarLayout;
    public NestedScrollView nestedScrollView;

    private String TAG = "HomeFragment";

    public List<Volume> volumesGerais;
    public List<String> categoriasStrings;

    private ProgressDialog progressDialog;

    private List<String> bookIds = new ArrayList<>();

    protected SharedPreferences sharedPreferences;
    protected static final String MY_PREFS = "MyPrefs";
    SharedPreferences.Editor editor;
    private final static String linkCapaFalha = "http://joerichard.net/wp-content/uploads/2015/08/stub.png";

    public List<Volume> livrosFiccao;
    public List<Bitmap> capasFiccao;

    public List<Volume> livrosAcao;
    public List<Bitmap> capasAcao;

    public List<Volume> livrosTerror;
    public List<Bitmap> capasTerror;

    public List<Volume> livrosAventura;
    public List<Bitmap> capasAventura;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase = database.getReference();


    private OnHomeUpdateListener onHomeUpdateListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeUpdateListener) {
            onHomeUpdateListener = (OnHomeUpdateListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHomeUpdateListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onHomeUpdateListener = null;
    }

    public interface OnHomeUpdateListener {
        void livrosCategoriasUpdate (List<Volume> ficcao, List<Volume> acao, List<Volume> terror, List<Volume> aventura);
        void capasCategoriasUpdate (List<Bitmap> ficcao, List<Bitmap> acao, List<Bitmap> terror, List<Bitmap> aventura);
        void adicionarLivro();
        void onLivroSelected(Bitmap capa, String idLivro, Volume.VolumeInfo dadosLivro);
        void atualizaCapasWishList(List<Bitmap> imgs);
        void atualizaBooksWishlist(List<Volume> volumes);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        volumesGerais = new ArrayList<>();
        categoriasStrings = new ArrayList<>();
        categoriasStrings.add("fiction");
        categoriasStrings.add("action");
        categoriasStrings.add("horror");
        categoriasStrings.add("adventure");
        sharedPreferences = getActivity().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        TextView txtActionBar = (TextView) root.findViewById(R.id.txtActionBar);
        TextView txtHomeTitle = (TextView) root.findViewById(R.id.txtChamadaCapa);
        Typeface font = Typeface.createFromAsset(this.getActivity().getAssets(), "futura_medium.ttf");
        txtActionBar.setTypeface(font);
        txtHomeTitle.setTypeface(font);

        rViewBaseadoBusca = (RecyclerView)root.findViewById(R.id.recycler_view_baseado_busca);
        linearLayoutManagerBaseadoBusca = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rViewBaseadoBusca.setLayoutManager(linearLayoutManagerBaseadoBusca);
        rcAdapter = new CategoriasHorizontRecyclerAdapter(getActivity(), new CategoriasHorizontRecyclerAdapter.ListAdapterListener() {
            @Override
            public void onClickAtOKButton(Bitmap capa, String idLivro, Volume.VolumeInfo dadosLivro) {
                onHomeUpdateListener.onLivroSelected(capa, idLivro, dadosLivro);
            }
        });
        rViewBaseadoBusca.setAdapter(rcAdapter);
        rViewBaseadoBusca.setNestedScrollingEnabled(false);

        rViewBaseadoBusca.setClickable(false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();

        final String userId = mAuth.getCurrentUser().getUid();

        mDatabase.child("shelves").child(userId).child("2").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Shelf shelfWishlist = dataSnapshot.getValue(Shelf.class);
                        if(shelfWishlist == null) {
                            // Cria nova shelf
                            shelfWishlist = new Shelf(userId,"2");
                            shelfWishlist.initializeBooks();
                            bookIds = shelfWishlist.getBooks();
                            if (bookIds.size() > 0) {
                                atualizaRecyclerView(bookIds);
                            }
                        }else {
                            bookIds = shelfWishlist.getBooks();
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


        rViewCategoriaFiccao = (RecyclerView)root.findViewById(R.id.recycler_view_categoria_ficcao);
        linearLayoutManagerCategoriaFiccao = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rViewCategoriaFiccao.setLayoutManager(linearLayoutManagerCategoriaFiccao);
        ficcaoRecyclerAdapter = new FiccaoRecyclerAdapter(getActivity(), new FiccaoRecyclerAdapter.ListAdapterListener() {
            @Override
            public void onClickAtOKButton(Bitmap capa, String idLivro, Volume.VolumeInfo dadosLivro) {
                onHomeUpdateListener.onLivroSelected(capa,idLivro,dadosLivro);
            }
        });
        rViewCategoriaFiccao.setAdapter(ficcaoRecyclerAdapter);
        rViewCategoriaFiccao.setNestedScrollingEnabled(false);
        rViewCategoriaFiccao.setClickable(false);

        rViewCategoriaTerror = (RecyclerView)root.findViewById(R.id.recycler_view_categoria_terror);
        linearLayoutManagerCategoriaTerror = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rViewCategoriaTerror.setLayoutManager(linearLayoutManagerCategoriaTerror);
        terrorRecyclerAdapter = new TerrorRecyclerAdapter(getActivity(),new TerrorRecyclerAdapter.ListAdapterListener() {
            @Override
            public void onClickAtOKButton(Bitmap capa, String idLivro, Volume.VolumeInfo dadosLivro) {
                onHomeUpdateListener.onLivroSelected(capa,idLivro,dadosLivro);
            }
        });
        rViewCategoriaTerror.setAdapter(terrorRecyclerAdapter);
        rViewCategoriaTerror.setNestedScrollingEnabled(false);
        rViewCategoriaTerror.setClickable(false);

        rViewCategoriaAcao = (RecyclerView)root.findViewById(R.id.recycler_view_categoria_acao);
        linearLayoutManagerCategoriaAcao = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rViewCategoriaAcao.setLayoutManager(linearLayoutManagerCategoriaAcao);
        acaoRecyclerAdapter = new AcaoRecyclerAdapter(getActivity(), new AcaoRecyclerAdapter.ListAdapterListener() {
            @Override
            public void onClickAtOKButton(Bitmap capa, String idLivro, Volume.VolumeInfo dadosLivro) {
                onHomeUpdateListener.onLivroSelected(capa,idLivro,dadosLivro);
            }
        });
        rViewCategoriaAcao.setAdapter(acaoRecyclerAdapter);
        rViewCategoriaAcao.setNestedScrollingEnabled(false);
        rViewCategoriaAcao.setClickable(false);

        rViewCategoriaAventura = (RecyclerView)root.findViewById(R.id.recycler_view_categoria_aventura);
        linearLayoutManagerCategoriaAventura = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rViewCategoriaAventura.setLayoutManager(linearLayoutManagerCategoriaAventura);
        aventuraRecyclerAdapter = new AventuraRecyclerAdapter(getActivity(), new AventuraRecyclerAdapter.ListAdapterListener() {
            @Override
            public void onClickAtOKButton(Bitmap capa, String idLivro, Volume.VolumeInfo dadosLivro) {
                onHomeUpdateListener.onLivroSelected(capa,idLivro,dadosLivro);
            }
        });
        rViewCategoriaAventura.setAdapter(aventuraRecyclerAdapter);
        rViewCategoriaAventura.setNestedScrollingEnabled(false);
        rViewCategoriaAventura.setClickable(false);


        actionButtonAdicionar = (FloatingActionButton)root.findViewById(R.id.floatingActionButton);

        coordinatorLayout = (CoordinatorLayout)root.findViewById(R.id.fragmentHomeLayout);
        nestedScrollView = (NestedScrollView)root.findViewById(R.id.nestedScrollViewHome);

        actionButtonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionButtonAdicionar.animate();
                onHomeUpdateListener.adicionarLivro();
            }
        });


        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    actionButtonAdicionar.hide();
                } else {
                    actionButtonAdicionar.show();
                }
            }
        });

        collapsingToolbarLayout = (CollapsingToolbarLayout)root.findViewById(R.id.collapsingToolbarHome);
        appBarLayout = (AppBarLayout)root.findViewById(R.id.appBarLayoutHome);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Weboo");
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

        if (((MainActivity)getActivity()).livrosFiccao.size() > 0) {
            livrosFiccao = ((MainActivity)getActivity()).livrosFiccao;
            capasFiccao = ((MainActivity)getActivity()).capasFiccao;
            livrosAcao = ((MainActivity)getActivity()).livrosAcao;
            capasAcao = ((MainActivity)getActivity()).capasAcao;
            livrosTerror = ((MainActivity)getActivity()).livrosTerror;
            capasTerror = ((MainActivity)getActivity()).capasTerror;
            livrosAventura = ((MainActivity)getActivity()).livrosAventura;
            capasAventura = ((MainActivity)getActivity()).capasAventura;

            ficcaoRecyclerAdapter.setVolumes(livrosFiccao);
            ficcaoRecyclerAdapter.setThumbnails(capasFiccao);
            ficcaoRecyclerAdapter.notifyDataSetChanged();
            rViewCategoriaFiccao.setClickable(true);

            acaoRecyclerAdapter.setVolumes(livrosAcao);
            acaoRecyclerAdapter.setThumbnails(capasAcao);
            acaoRecyclerAdapter.notifyDataSetChanged();
            rViewCategoriaAcao.setClickable(true);

            terrorRecyclerAdapter.setVolumes(livrosTerror);
            terrorRecyclerAdapter.setThumbnails(capasTerror);
            terrorRecyclerAdapter.notifyDataSetChanged();
            rViewCategoriaTerror.setClickable(true);

            aventuraRecyclerAdapter.setVolumes(livrosAventura);
            aventuraRecyclerAdapter.setThumbnails(capasAventura);
            aventuraRecyclerAdapter.notifyDataSetChanged();
            rViewCategoriaAventura.setClickable(true);

        }

        obterLivrosCategorias();

        return root;
    }

    public void obterLivrosCategorias () {
        new BuscarLivrosCategorias().execute(categoriasStrings);
    }


    private class BuscarLivrosCategorias extends AsyncTask<List<String>, String, List<Volume>> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (sharedPreferences.getBoolean("FirstAccessHome",true)) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Buscando categorias...");
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
                String categoriaStr = params[0].get(i);
                Volumes volumes = booksSample.queryBooksBySubject(jsonFactory,categoriaStr);
                System.out.println("Tamanho do retorno: ("+i+"): "+volumes.getItems().size());
                if (volumes.getItems().size() > 0) {
                    for (int j = 0; j < volumes.getItems().size(); j++) {
                        retorno.add(volumes.getItems().get(j));
                    }
                }
            }
            System.out.println("Tamanho do retorno: " + retorno.size());
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
        if (sharedPreferences.getBoolean("FirstAccessHome",true)) {
            progressDialog.dismiss();
            editor = sharedPreferences.edit();
            editor.putBoolean("FirstAccessHome",false);
            editor.apply();
        }
        List<Volume> ficcaoAux = new ArrayList<>();
        List<Volume> acaoAux = new ArrayList<>();
        List<Volume> terrorAux = new ArrayList<>();
        List<Volume> aventuraAux = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == 0) {
                    ficcaoAux.add(volumes.get(j+(i*10)));
                } else if (i == 1) {
                    acaoAux.add(volumes.get(j+(i*10)));
                } else if (i == 2) {
                    terrorAux.add(volumes.get(j+(i*10)));
                } else if (i == 3) {
                    aventuraAux.add(volumes.get(j+(i*10)));
                }
            }
            if (i == 0) {
                ficcaoRecyclerAdapter.setVolumes(ficcaoAux);
                ficcaoRecyclerAdapter.notifyDataSetChanged();
            } else if (i == 1) {
                acaoRecyclerAdapter.setVolumes(acaoAux);
                acaoRecyclerAdapter.notifyDataSetChanged();
            } else if (i == 2) {
                terrorRecyclerAdapter.setVolumes(terrorAux);
                terrorRecyclerAdapter.notifyDataSetChanged();
            } else if (i == 3) {
                aventuraRecyclerAdapter.setVolumes(aventuraAux);
                aventuraRecyclerAdapter.notifyDataSetChanged();
            }
            //volumesAux.clear();
        }
        System.out.println("Tamanho ficcao: " + ficcaoRecyclerAdapter.getTotalVolumes());
        System.out.println("Tamanho acao: " + acaoRecyclerAdapter.getTotalVolumes());
        System.out.println("Tamanho aventura: " + aventuraRecyclerAdapter.getTotalVolumes());
        System.out.println("Tamanho terror: " + terrorRecyclerAdapter.getTotalVolumes());
        onHomeUpdateListener.livrosCategoriasUpdate(ficcaoAux,acaoAux,terrorAux,aventuraAux);
        //onVolumesUpdateListener.volumesPessoalUpdate(volumes);

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

    private class ThumbnailsDownloader extends AsyncTask<List<String>, Void, List<Bitmap>> {

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
            System.out.println("Quantidade de capas: " + imgs.size());
            /*if (sharedPreferences.getBoolean("FirstAccessHome",true)) {
                progressDialog.dismiss();
                editor = sharedPreferences.edit();
                if (imgs.size() > 0) {
                    editor.putBoolean("FirstAccessHome",false);
                }
                editor.apply();
            }*/
            super.onPostExecute(imgs);
            List<Bitmap> ficcaoAux = new ArrayList<>();
            List<Bitmap> acaoAux = new ArrayList<>();
            List<Bitmap> terrorAux = new ArrayList<>();
            List<Bitmap> aventuraAux = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 10; j++) {
                    if (i == 0) {
                        ficcaoAux.add(imgs.get(j+(i*10)));
                    } else if (i == 1) {
                        acaoAux.add(imgs.get(j+(i*10)));
                    } else if (i == 2) {
                        terrorAux.add(imgs.get(j+(i*10)));
                    } else if (i == 3) {
                        aventuraAux.add(imgs.get(j+(i*10)));
                    }
                }
                if (i == 0) {
                    ficcaoRecyclerAdapter.setThumbnails(ficcaoAux);
                } else if (i == 1) {
                    acaoRecyclerAdapter.setThumbnails(acaoAux);
                } else if (i == 2) {
                    terrorRecyclerAdapter.setThumbnails(terrorAux);
                } else if (i == 3) {
                    aventuraRecyclerAdapter.setThumbnails(aventuraAux);
                }
                //thumbnailsAux.clear();
            }
            //onVolumesUpdateListener.capasPessoalUpdate(imgs);
            onHomeUpdateListener.capasCategoriasUpdate(ficcaoAux,acaoAux,terrorAux,aventuraAux);
            ficcaoRecyclerAdapter.notifyDataSetChanged();
            acaoRecyclerAdapter.notifyDataSetChanged();
            terrorRecyclerAdapter.notifyDataSetChanged();
            aventuraRecyclerAdapter.notifyDataSetChanged();
            rViewCategoriaAventura.setClickable(true);
            rViewCategoriaAcao.setClickable(true);
            rViewCategoriaTerror.setClickable(true);
            rViewCategoriaFiccao.setClickable(true);
        }
    }

    public void atualizaRecyclerView (List<String> bookIds) {
        new BuscarLivros().execute(bookIds);
    }

    private class BuscarLivros extends AsyncTask<List<String>, String, List<Volume>> {

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
            rcAdapter.setVolumes(volumes);
            rcAdapter.notifyDataSetChanged();
            onHomeUpdateListener.atualizaBooksWishlist(volumes);

            List<String> links = new ArrayList<>();
            for (int i = 0; i < volumes.size(); i++) {
                Volume.VolumeInfo.ImageLinks imageLinksOp = volumes.get(i).getVolumeInfo().getImageLinks();
                if (imageLinksOp != null) {
                    String linkSaida;
                    linkSaida = getLinkValido(imageLinksOp);
                    if (linkSaida != null) {
                        links.add(linkSaida);
                    } else {
                        links.add(linkCapaFalha);
                    }
                } else {
                    links.add(linkCapaFalha);
                }
            }
            downloadImagensBaseada(links);
        }
    }

    public void downloadImagensBaseada (List<String> links) {
        new ThumbnailsDownloaderBaseada().execute(links);
    }

    private class ThumbnailsDownloaderBaseada extends AsyncTask<List<String>, Void, List<Bitmap>> {



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
            super.onPostExecute(imgs);
            rcAdapter.setThumbnails(imgs);
            onHomeUpdateListener.atualizaCapasWishList(imgs);
            rViewBaseadoBusca.setClickable(true);
            rcAdapter.notifyDataSetChanged();
        }
    }

}
