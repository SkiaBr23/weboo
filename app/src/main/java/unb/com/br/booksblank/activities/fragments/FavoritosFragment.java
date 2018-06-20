package unb.com.br.booksblank.activities.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.model.Volume;
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
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import unb.com.br.booksblank.R;
import unb.com.br.booksblank.activities.BooksSample;
import unb.com.br.booksblank.activities.MainActivity;
import unb.com.br.booksblank.adapters.BooksWishlistRecyclerAdapter;
import unb.com.br.booksblank.model.Shelf;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritosFragment extends Fragment implements BooksWishlistRecyclerAdapter.ListAdapterListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "FavoritosFragment";
    private final static String linkCapaFalha = "http://joerichard.net/wp-content/uploads/2015/08/stub.png";
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase = database.getReference();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GridLayoutManager gridLayoutManager;
    private RecyclerView rView;
    private BooksWishlistRecyclerAdapter rcAdapter;
    private List<String> bookIds = new ArrayList<>();
    private ProgressDialog progressDialog;

    protected SharedPreferences sharedPreferences;
    protected static final String MY_PREFS = "MyPrefs";
    SharedPreferences.Editor editor;

    public List<Volume> volumesListFragment = new ArrayList<>();
    public List<Bitmap> thumbnailsListFragment = new ArrayList<>();

    private OnVolumesUpdateListener onVolumesUpdateListener;


    public FavoritosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoritosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritosFragment newInstance(String param1, String param2) {
        FavoritosFragment fragment = new FavoritosFragment();
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

    @Override
    public void onClickAtOKButton(Bitmap capa, String id, Volume.VolumeInfo dadosLivro) {

    }

    public interface OnVolumesUpdateListener {
        void volumesUpdate(List<Volume> volumes);
        void thumbnailsUpdate(List<Bitmap> capas);
        void onLivroSelectedFavoritos(Bitmap capaLivro, String id, Volume.VolumeInfo dadosLivro);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();

        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_favoritos, container, false);


        TextView txtActionBar = (TextView) root.findViewById(R.id.txtHeaderWishlist);
        Typeface font = Typeface.createFromAsset(this.getActivity().getAssets(), "futura_medium.ttf");
        txtActionBar.setTypeface(font);

        gridLayoutManager = new GridLayoutManager(getActivity(), 3);

        rView = (RecyclerView)root.findViewById(R.id.recycler_view_wishlist);
        rView.setItemAnimator(new FadeInAnimator());
        rView.getItemAnimator().setAddDuration(500);

        rView.setHasFixedSize(true);
        rView.setLayoutManager(gridLayoutManager);

        rcAdapter = new BooksWishlistRecyclerAdapter(getActivity(), new BooksWishlistRecyclerAdapter.ListAdapterListener () {

            @Override
            public void onClickAtOKButton(Bitmap capa, String id, Volume.VolumeInfo dadosLivro) {
                onVolumesUpdateListener.onLivroSelectedFavoritos(capa, id, dadosLivro);
            }
        });

        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(rcAdapter);
        alphaAdapter.setDuration(1000);
        alphaAdapter.setFirstOnly(false);
        rView.setAdapter(alphaAdapter);

        /*
            BUSCANDO LIVROS FAVORITADOS NO FIREBASE RTDB
        */

        if (((MainActivity)getActivity()).volumeList.size() > 0) {
            volumesListFragment = ((MainActivity)getActivity()).volumeList;
            thumbnailsListFragment = ((MainActivity)getActivity()).thumbnails;
            rcAdapter.setVolumes(volumesListFragment);
            rcAdapter.setThumbnails(thumbnailsListFragment);
            rView.setAdapter(rcAdapter);
        }

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

        return root;
    }

    public void atualizaRecyclerView (List<String> bookIds) {
        new BuscarLivros().execute(bookIds);
    }

    private class BuscarLivros extends AsyncTask<List<String>, String, List<Volume>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (sharedPreferences.getBoolean("FirstAccessFavoritos",true)) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Buscando lista de desejos...");
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
            rcAdapter.setVolumes(volumes);
            rcAdapter.notifyDataSetChanged();
            onVolumesUpdateListener.volumesUpdate(volumes);

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
        new FavoritosFragment.ThumbnailsDownloader().execute(links);
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

            if (sharedPreferences.getBoolean("FirstAccessFavoritos",true)) {
                progressDialog.dismiss();
                editor = sharedPreferences.edit();
                editor.putBoolean("FirstAccessFavoritos",false);
                editor.apply();
            }

            super.onPostExecute(imgs);
            rcAdapter.setThumbnails(imgs);
            onVolumesUpdateListener.thumbnailsUpdate(imgs);
            rcAdapter.notifyDataSetChanged();
        }
    }

}
