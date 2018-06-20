package unb.com.br.booksblank.activities.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import unb.com.br.booksblank.R;
import unb.com.br.booksblank.adapters.RecyclerViewTransactions;
import unb.com.br.booksblank.adapters.TransactionsRecyclerAdapter;
import unb.com.br.booksblank.model.Shelf;
import unb.com.br.booksblank.model.TransactionInfo;
import unb.com.br.booksblank.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String DEFAULT_PIC = "https://success.salesforce.com/resource/1498435200000/sharedlayout/img/new-user-image-default.png";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private RecyclerView rViewTransactions;
    private TransactionsRecyclerAdapter transactionsRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String userId;
    private List<TransactionInfo> transactions;
    private List<Bitmap> bookCovers;
    private List<Bitmap> profilePicturesUsers1;
    private List<Bitmap> profilePicturesUsers2;
    private List<User> users1;
    private List <User> users2;
    private String currentUser;

    private OnResultPesquisaListener onResultPesquisaListener;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
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

        transactions = new ArrayList<>();
        bookCovers = new ArrayList<>();
        profilePicturesUsers1 = new ArrayList<>();
        profilePicturesUsers2 = new ArrayList<>();
        users1 = new ArrayList<>();
        users2 = new ArrayList<>();
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
        void onConfirmarTransacao(int tipoAcao, User u1, User u2, TransactionInfo tinfo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_chat, container, false);

        TextView txtActionBar = (TextView) root.findViewById(R.id.txtHeaderChat);
        Typeface font = Typeface.createFromAsset(this.getActivity().getAssets(), "futura_medium.ttf");
        txtActionBar.setTypeface(font);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        rViewTransactions = (RecyclerView)root.findViewById(R.id.recycler_view_transactions);
        rViewTransactions.setItemAnimator(new FadeInAnimator());
        rViewTransactions.getItemAnimator().setAddDuration(500);

        rViewTransactions.setHasFixedSize(true);
        rViewTransactions.setLayoutManager(linearLayoutManager);

        transactionsRecyclerAdapter = new TransactionsRecyclerAdapter(getActivity(), new TransactionsRecyclerAdapter.ListAdapterListener() {
            @Override
            public void onClickAtOKButton(int tipoAcao, User user1, User user2, TransactionInfo tinfo) {
                onResultPesquisaListener.onConfirmarTransacao(tipoAcao, user1,user2,tinfo);
            }
        });

        rViewTransactions.setAdapter(transactionsRecyclerAdapter);

        System.out.println("START FETCH");
        fetchTransactions();
/*
        TextView tituloLivro = (TextView)rViewTransactions.findViewById(R.id.tituloLivroTransaction);
        tituloLivro.setTypeface(font);

        TextView edicaoLivroTransaction = (TextView)rViewTransactions.findViewById(R.id.edicaoLivroTransaction);
        edicaoLivroTransaction.setTypeface(font);

        TextView conservacaoLivroTransaction = (TextView)rViewTransactions.findViewById(R.id.conservacaoLivroTransaction);
        conservacaoLivroTransaction.setTypeface(font);

        TextView nomeUsuarioSolicitado = (TextView)rViewTransactions.findViewById(R.id.nomeUsuarioSolicitado);
        nomeUsuarioSolicitado.setTypeface(font);*/
        return root;

    }

    private void loadTransactions(){
        users1 = new ArrayList<>();
        users2 = new ArrayList<>();
        for (TransactionInfo t : transactions) {
            mDatabase.child("users").child(t.getUser2Id()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user != null) {
                                users2.add(user);
                            }
                            System.out.println("acabo1");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("ChatFragment", "getTransactions:onCancelled", databaseError.toException());
                        }
                    });

            mDatabase.child("users").child(t.getUser1Id()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user != null) {
                                users1.add(user);
                            }
                            System.out.println("acabo2");
                            loadAssets();
                            System.out.println("LOADOU TRANSACTIONS");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("ChatFragment", "getTransactions:onCancelled", databaseError.toException());
                        }
                    });

        }

    }

    public void loadAssets(){
        List<User> allUsers = new ArrayList<>();
        for(User u : users1){
            allUsers.add(u);
        }
        for(User u : users2){
            allUsers.add(u);
        }
        downloadFotos(allUsers);
        List<String> bookUrls = new ArrayList<>();
        for(TransactionInfo t : transactions){
            bookUrls.add(t.getBookCoverUrl());
        }
        downloadImagens(bookUrls);

        transactionsRecyclerAdapter.setUsers1(users1);
        transactionsRecyclerAdapter.setUsers2(users2);
        transactionsRecyclerAdapter.setCurrentUser(userId);
        System.out.println("BAIXOU TUDO + " + users1.size() + " " + users2.size() + " " + transactions.size());

        transactionsRecyclerAdapter.notifyDataSetChanged();
    }


    private void fetchTransactions() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        userId = mAuth.getCurrentUser().getUid();
        transactions = new ArrayList<>();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        System.out.println("buscando user " + user.getFullName());
                        if (user != null && user.getTransactions() != null) {
                            transactions = user.getTransactions();
                        }
                        transactionsRecyclerAdapter.setTransactions(transactions);
                        loadTransactions();

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("ChatFragment", "getTransactions:onCancelled", databaseError.toException());
                    }
                });

        System.out.println("CARREGANDO TRANSACTIONS");
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

            List<Bitmap> u1 = new ArrayList<>();
            List<Bitmap> u2 = new ArrayList<>();
            for (Bitmap b : imgs){
                if(imgs.indexOf(b) < imgs.size()/2){
                    u1.add(b);
                }else{
                    u2.add(b);
                }
            }
            transactionsRecyclerAdapter.setProfilePicturesUsers1(u1);
            transactionsRecyclerAdapter.setProfilePicturesUsers2(u2);
            transactionsRecyclerAdapter.notifyDataSetChanged();
            System.out.println("BAIXOU CAPAS");
            System.out.println("BAIXOU CAPAS " + u1.size() + " " + u2.size());
            //transactionsRecyclerAdapter.notifyDataSetChanged();
        }
    }

    public void downloadImagens (List<String> links) {
        new ThumbnailsDownloader2().execute(links);
    }

    public class ThumbnailsDownloader2 extends AsyncTask<List<String>, Void, List<Bitmap>> {

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
            transactionsRecyclerAdapter.setBookCovers(imgs);
            transactionsRecyclerAdapter.notifyDataSetChanged();
            System.out.println("BAIXOU PROFILESPICS");
        }
    }
}


