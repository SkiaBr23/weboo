package unb.com.br.booksblank.activities.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.squareup.picasso.Picasso;

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
import unb.com.br.booksblank.adapters.BooksRecyclerAdapter;
import unb.com.br.booksblank.model.Shelf;
import unb.com.br.booksblank.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PesquisaFragment.OnResultPesquisaListener} interface
 * to handle interaction events.
 * Use the {@link PesquisaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PesquisaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "PesquisaFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageButton limparPesquisa;
    private EditText editTxtPesquisa;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView rView;
    private BooksRecyclerAdapter rcAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    public List<User> usersBuscados = new ArrayList<>();
    public List<Bitmap> profilePicUsersBuscados = new ArrayList<>();
    private final static String userSemFoto = "http://enadcity.org/enadcity/wp-content/uploads/2017/02/profile-pictures.png";
    private final static String linkCapaFalha = "http://joerichard.net/wp-content/uploads/2015/08/stub.png";
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase = database.getReference();
    private List<User> users = new ArrayList<>();

    private ProgressDialog progressDialog;

    public List<Volume> volumesPesquisa = new ArrayList<>();
    public List<Bitmap> capas = new ArrayList<>();

    private OnResultPesquisaListener onResultPesquisaListener;

    public PesquisaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PesquisaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PesquisaFragment newInstance(String param1, String param2) {
        PesquisaFragment fragment = new PesquisaFragment();
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

    public interface OnResultPesquisaListener {
        void onResultPesquisaLivros(Volumes volumes);
        void onResultPesquisaCapas(List<Bitmap> capas);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_pesquisa, container, false);
        editTxtPesquisa = (EditText)root.findViewById(R.id.editTxtPesquisa);
        limparPesquisa = (ImageButton)root.findViewById(R.id.btnLimparPesquisa);
        mAuth = FirebaseAuth.getInstance();
        limparPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTxtPesquisa.setText("");
            }
        });

        editTxtPesquisa.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (viewPager.getCurrentItem() == 0) {
                        System.out.println(viewPager.getCurrentItem());
                        new BuscarLivros().execute("intitle:" + editTxtPesquisa.getText().toString());
                    } else if (viewPager.getCurrentItem() == 1) {
                        System.out.println(viewPager.getCurrentItem());
                        new BuscarLivros().execute("inauthor:" + editTxtPesquisa.getText().toString());
                    } else if (viewPager.getCurrentItem() == 2) {
                        BuscarUsuarios(editTxtPesquisa.getText().toString());
                    }
                    clearKeyboard();
                }
                return false;
            }
        });

        editTxtPesquisa.postDelayed(new Runnable() {
            @Override
            public void run() {
                editTxtPesquisa.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }
        },200);

        tabLayout = (TabLayout)root.findViewById(R.id.tab_layout_pesquisa);
        tabLayout.addTab(tabLayout.newTab().setText("Livros"));
        tabLayout.addTab(tabLayout.newTab().setText("Autores"));
        tabLayout.addTab(tabLayout.newTab().setText("Usuários"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager)root.findViewById(R.id.pagerFragments);
        adapter = new PagerAdapter
                (getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (((MainActivity)getActivity()).livrosresultadoPesquisa.size() > 0) {
            this.volumesPesquisa = ((MainActivity)getActivity()).livrosresultadoPesquisa;
            this.capas = ((MainActivity)getActivity()).capasResuladoPesquisa;
            //PesquisaLivrosFragment plf = (PesquisaLivrosFragment)adapter.instantiateItem(viewPager,viewPager.getCurrentItem());
            //plf.setListLivrosResult(this.volumesPesquisa,this.capas);
            System.out.println("Tamanho livros result: " + ((MainActivity)getActivity()).livrosresultadoPesquisa.size());
        }

        //viewPager.setOffscreenPageLimit(0);

        return root;
    }

    public void clearKeyboard () {
        editTxtPesquisa.clearFocus();
        InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(editTxtPesquisa.getWindowToken(),0);
    }


    public void BuscarUsuarios(final String query) {
        users = new ArrayList<>();
        final String userId = mAuth.getCurrentUser().getUid();
        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usersBuscados = new ArrayList<User>();
                for (DataSnapshot val : dataSnapshot.getChildren()) {
                    if (val.child("fullName").getValue(String.class).toLowerCase().contains(query.toLowerCase())) {
                        usersBuscados.add(val.getValue(User.class));

                    }
                }
                for (User user : usersBuscados) {
                    System.out.println("Usuário: " + user.getFullName());
                }
                new ProfilePicDownloader().execute(usersBuscados);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        }
    //----------------------------------AsyncTask----------------------------------//

    private class ProfilePicDownloader extends AsyncTask<List<User>, Void, ArrayList<Bitmap>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Buscando usuários...");
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected ArrayList<Bitmap> doInBackground(List<User>... users) {
            Bitmap downloaded;
            URL urlLogosNewsAPI;
            String linkFinal;
            HttpURLConnection urlConnection = null;
            ArrayList<Bitmap> imgs = new ArrayList<>();
            try {
                for (User userAux : users[0]) {
                    if (userAux.getProfilePictureUrl() != null) {
                        linkFinal = userAux.getProfilePictureUrl();
                    } else {
                        linkFinal = userSemFoto;
                    }
                    urlLogosNewsAPI = new URL(linkFinal);
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
        protected void onPostExecute(ArrayList<Bitmap> imgs) {
            profilePicUsersBuscados = imgs;
            progressDialog.dismiss();
            System.out.println("Size usuários: " + usersBuscados.size());
            System.out.println("Size profilePics: " + profilePicUsersBuscados.size());
            if (viewPager.getCurrentItem() == 2) {
                PesquisaUsuariosFragment puf = (PesquisaUsuariosFragment)adapter.instantiateItem(viewPager,viewPager.getCurrentItem());
                puf.setListUsuariosResult(usersBuscados,profilePicUsersBuscados);
            }

        }
    }

    private class BuscarLivros extends AsyncTask<String, String, Volumes> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Buscando livros...");
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected Volumes doInBackground(String... params) {
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            BooksSample booksSample = new BooksSample();
            try {
                String query = params[0];

                try {
                    Volumes volumes = booksSample.queryGoogleBooks(jsonFactory, query);
                    if (volumes != null) {
                        System.out.println("Busca retornou dados!");
                        return volumes;
                    } else {
                        System.out.println("Busca não retornou dados!");
                        return null;
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Volumes volumes) {

            /*rcAdapter = new BooksRecyclerAdapter(getActivity(), volumes, new BooksRecyclerAdapter.ListAdapterListener() {
                @Override
                public void onClickAtOKButton(Bitmap capa, String idLivro, Volume.VolumeInfo dadosLivro) {
                    onResultPesquisaListener.onLivroSelected(capa, idLivro, dadosLivro);
                }
            });

           ;*/
            onResultPesquisaListener.onResultPesquisaLivros(volumes);
            setLivros(volumes);
            List<String> links = new ArrayList<>();
            for (int i = 0; i < volumes.getItems().size(); i++) {
                Volume.VolumeInfo.ImageLinks imageLinks = volumes.getItems().get(i).getVolumeInfo().getImageLinks();
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

    public void setLivros (Volumes volumes) {
        this.volumesPesquisa.clear();
        for (int i = 0; i < volumes.getItems().size(); i++) {
            this.volumesPesquisa.add(volumes.getItems().get(i));
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

    private class ThumbnailsDownloader extends AsyncTask<List<String>, Void, ArrayList<Bitmap>> {

        @Override
        protected ArrayList<Bitmap> doInBackground(List<String>... link) {
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
        protected void onPostExecute(ArrayList<Bitmap> imgs) {
            progressDialog.dismiss();
            super.onPostExecute(imgs);
            System.out.println("Thumbnails baixados");
            onResultPesquisaListener.onResultPesquisaCapas(imgs);
            setCapas(imgs);
        }
    }

    public void setCapas (List<Bitmap> capas) {
        progressDialog.dismiss();
        this.capas = capas;
        System.out.println("Tamanho capas: " + capas.size());
        if (viewPager.getCurrentItem() == 0) {
            PesquisaLivrosFragment plf = (PesquisaLivrosFragment)adapter.instantiateItem(viewPager,viewPager.getCurrentItem());
            plf.setListLivrosResult(this.volumesPesquisa,this.capas);
        } else if (viewPager.getCurrentItem() == 1) {
            PesquisaAutoresFragment paf = (PesquisaAutoresFragment)adapter.instantiateItem(viewPager,viewPager.getCurrentItem());
            paf.setListLivrosResult(this.volumesPesquisa,this.capas);
        }
    }

}
