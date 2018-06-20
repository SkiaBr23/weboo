package unb.com.br.booksblank.activities.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
 * Activities that contain this fragment must implement the
 * {@link PesquisaFragment.OnResultPesquisaListener} interface
 * to handle interaction events.
 * Use the {@link PesquisaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdicionarLivroBuscaFragment extends Fragment implements BooksRecyclerAdapter.ListAdapterListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageButton limparPesquisa;
    private EditText editTxtPesquisa;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView rView;
    private BooksRecyclerAdapter rcAdapter;
    private final static String linkCapaFalha = "http://joerichard.net/wp-content/uploads/2015/08/stub.png";
    private ProgressDialog progressDialog;

    private OnResultPesquisaListener onResultPesquisaListener;

    public AdicionarLivroBuscaFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_adicionar_livor_busca, container, false);
        editTxtPesquisa = (EditText)root.findViewById(R.id.editTxtPesquisa);
        limparPesquisa = (ImageButton)root.findViewById(R.id.btnLimparPesquisa);

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
                    Toast.makeText(getActivity(),"Iniciar Pesquisa...",Toast.LENGTH_SHORT).show();
                    new BuscarLivros().execute("intitle:"+editTxtPesquisa.getText().toString());
                    clearKeyboard();
                }
                return false;
            }
        });

        gridLayoutManager = new GridLayoutManager(getActivity(), 3);

        rView = (RecyclerView)root.findViewById(R.id.recycler_view);
        rView.setItemAnimator(new FadeInAnimator());
        rView.getItemAnimator().setAddDuration(500);

        rView.setHasFixedSize(true);
        rView.setLayoutManager(gridLayoutManager);

        return root;
    }

    public void clearKeyboard () {
        editTxtPesquisa.clearFocus();
        InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(editTxtPesquisa.getWindowToken(),0);
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
    public void onClickAtOKButton(Bitmap capa, String id, Volume.VolumeInfo dadosLivro) {

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
    public interface OnResultPesquisaListener {
        void onLivroSelectedAdicao(Bitmap capa, String id, Volume.VolumeInfo dadosLivro);
    }

    //----------------------------------AsyncTask----------------------------------//

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
            List<Volume> volumesLista = new ArrayList<>();
            for (int i = 0; i < volumes.getItems().size(); i++) {
                volumesLista.add(volumes.getItems().get(i));
            }
            rcAdapter = new BooksRecyclerAdapter(getActivity(), volumesLista, new BooksRecyclerAdapter.ListAdapterListener() {
                @Override
                public void onClickAtOKButton(Bitmap capa, String id, Volume.VolumeInfo dadosLivro) {
                    onResultPesquisaListener.onLivroSelectedAdicao(capa,id,dadosLivro);
                }
            });

            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(rcAdapter);
            alphaAdapter.setDuration(1000);
            alphaAdapter.setFirstOnly(false);
            rView.setAdapter(alphaAdapter);

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
            super.onPostExecute(imgs);
            progressDialog.dismiss();
            rcAdapter.setThumbnails(imgs);
            System.out.println("Thumbnails baixados");
            rcAdapter.notifyDataSetChanged();
        }
    }
}