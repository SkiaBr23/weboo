package unb.com.br.booksblank.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import unb.com.br.booksblank.R;
import unb.com.br.booksblank.activities.fragments.AdicionarLivroBuscaFragment;
import unb.com.br.booksblank.activities.fragments.AdicionarLivroFragment;
import unb.com.br.booksblank.activities.fragments.ChatFragment;
import unb.com.br.booksblank.activities.fragments.FavoritosFragment;
import unb.com.br.booksblank.activities.fragments.HomeFragment;
import unb.com.br.booksblank.activities.fragments.LivroDetalheFragment;
import unb.com.br.booksblank.activities.fragments.PerfilFragment;
import unb.com.br.booksblank.activities.fragments.PesquisaAutoresFragment;
import unb.com.br.booksblank.activities.fragments.PesquisaFragment;
import unb.com.br.booksblank.activities.fragments.PesquisaLivrosFragment;
import unb.com.br.booksblank.activities.fragments.PesquisaUsuariosFragment;
import unb.com.br.booksblank.activities.fragments.UsuarioBuscadoFragment;
import unb.com.br.booksblank.model.BookInfo;
import unb.com.br.booksblank.model.Shelf;
import unb.com.br.booksblank.model.TransactionInfo;
import unb.com.br.booksblank.model.User;
import unb.com.br.booksblank.model.UserInfo;

public class MainActivity extends AppCompatActivity implements LivroDetalheFragment.OnFragmentInteractionListener, PesquisaFragment.OnResultPesquisaListener,
FavoritosFragment.OnVolumesUpdateListener, PesquisaLivrosFragment.OnResultPesquisaListener, PerfilFragment.OnVolumesUpdateListener, HomeFragment.OnHomeUpdateListener,AdicionarLivroBuscaFragment.OnResultPesquisaListener,
AdicionarLivroFragment.OnAdicionarListener, PesquisaAutoresFragment.OnResultPesquisaListener, PesquisaUsuariosFragment.OnResultPesquisaListener, UsuarioBuscadoFragment.OnVolumesUpdateListener, ChatFragment.OnResultPesquisaListener{

    private HomeFragment homeFragment;
    private FavoritosFragment favoritosFragment;
    private PesquisaFragment pesquisaFragment;
    private ChatFragment chatFragment;
    private PerfilFragment perfilFragment;
    private AdicionarLivroBuscaFragment adicionarLivroBuscaFragment;
    private LivroDetalheFragment livroDetalheFragment;
    private ImageButton home, favoritos, pesquisa, chat, perfil;


    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;

    private static final String SHELF_WISHLIST = "2";
    private static final String SHELF_AVAILABLE = "1";
    private static final String SHELF_TRANSACTION = "3";

    private static final String TAG = "AdicionarLivroFragment";

    protected SharedPreferences sharedPreferences;
    protected static final String MY_PREFS = "MyPrefs";
    private final static String linkCapaFalha = "http://joerichard.net/wp-content/uploads/2015/08/stub.png";
    SharedPreferences.Editor editor;

    public List<Volume> volumeList;
    public List<Bitmap> thumbnails;

    public List<Volume> livrosresultadoPesquisa;
    public List<Bitmap> capasResuladoPesquisa;

    public List<Volume> livrosEstantePessoal;
    public List<Bitmap> capasEstantePessoal;

    public List<Volume> livrosFiccao;
    public List<Bitmap> capasFiccao;

    public List<Volume> livrosAcao;
    public List<Bitmap> capasAcao;

    public List<Volume> livrosTerror;
    public List<Bitmap> capasTerror;

    public List<Volume> livrosAventura;
    public List<Bitmap> capasAventura;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUIComponents();

    }

    public void setupUIComponents () {

        livrosresultadoPesquisa = new ArrayList<>();
        capasResuladoPesquisa = new ArrayList<>();
        volumeList = new ArrayList<>();
        thumbnails = new ArrayList<>();

        livrosEstantePessoal = new ArrayList<>();
        capasEstantePessoal = new ArrayList<>();

        livrosFiccao = new ArrayList<>();
        capasFiccao = new ArrayList<>();
        livrosAcao = new ArrayList<>();
        capasAcao = new ArrayList<>();
        livrosTerror = new ArrayList<>();
        capasTerror = new ArrayList<>();
        livrosAventura = new ArrayList<>();
        capasAventura = new ArrayList<>();

        sharedPreferences = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("FirstAccessFavoritos",true);
        editor.putBoolean("FirstAccessHome",true);
        editor.putBoolean("FirstAccessPerfil",true);
        editor.apply();

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();

        this.homeFragment = new HomeFragment();
        this.favoritosFragment = new FavoritosFragment();
        this.pesquisaFragment = new PesquisaFragment();
        this.chatFragment = new ChatFragment();
        this.perfilFragment = new PerfilFragment();
        this.adicionarLivroBuscaFragment = new AdicionarLivroBuscaFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, homeFragment,"homeFrag").addToBackStack("home").commit();
        //getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, chatFragment,"chatFrag").addToBackStack("chat").commit();

        home = (ImageButton)findViewById(R.id.iconFooterHome);
        favoritos = (ImageButton)findViewById(R.id.iconFooterFavoritos);
        pesquisa = (ImageButton)findViewById(R.id.iconFooterPesquisa);
        chat = (ImageButton)findViewById(R.id.iconFooterChat);
        perfil = (ImageButton)findViewById(R.id.iconFooterPerfil);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, homeFragment,"homeFrag").addToBackStack("home").commit();
                home.setBackgroundResource(R.drawable.ic_footer_home_selecionado);
                favoritos.setBackgroundResource(R.drawable.ic_footer_favoritos_nselecionado);
                pesquisa.setBackgroundResource(R.drawable.ic_footer_pesquisa_nselecionado);
                chat.setBackgroundResource(R.drawable.ic_footer_chat_nselecionado);
                perfil.setBackgroundResource(R.drawable.ic_footer_perfil_nselecionado);
            }
        });

        favoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, favoritosFragment,"favoritosFrag").addToBackStack("favoritos").commit();
                home.setBackgroundResource(R.drawable.ic_footer_home_nselecionado);
                favoritos.setBackgroundResource(R.drawable.ic_footer_favoritos_selecionado);
                pesquisa.setBackgroundResource(R.drawable.ic_footer_pesquisa_nselecionado);
                chat.setBackgroundResource(R.drawable.ic_footer_chat_nselecionado);
                perfil.setBackgroundResource(R.drawable.ic_footer_perfil_nselecionado);
            }
        });

        pesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, pesquisaFragment,"buscaGeralFrag").addToBackStack("buscageral").commit();
                home.setBackgroundResource(R.drawable.ic_footer_home_nselecionado);
                favoritos.setBackgroundResource(R.drawable.ic_footer_favoritos_nselecionado);
                pesquisa.setBackgroundResource(R.drawable.ic_footer_pesquisa_selecionado);
                chat.setBackgroundResource(R.drawable.ic_footer_chat_nselecionado);
                perfil.setBackgroundResource(R.drawable.ic_footer_perfil_nselecionado);
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, chatFragment,"chatFrag").addToBackStack("chat").commit();
                home.setBackgroundResource(R.drawable.ic_footer_home_nselecionado);
                favoritos.setBackgroundResource(R.drawable.ic_footer_favoritos_nselecionado);
                pesquisa.setBackgroundResource(R.drawable.ic_footer_pesquisa_nselecionado);
                chat.setBackgroundResource(R.drawable.ic_footer_chat_selecionado);
                perfil.setBackgroundResource(R.drawable.ic_footer_perfil_nselecionado);
            }
        });

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, perfilFragment, "perfilFrag").addToBackStack("perfil").commit();
                home.setBackgroundResource(R.drawable.ic_footer_home_nselecionado);
                favoritos.setBackgroundResource(R.drawable.ic_footer_favoritos_nselecionado);
                pesquisa.setBackgroundResource(R.drawable.ic_footer_pesquisa_nselecionado);
                chat.setBackgroundResource(R.drawable.ic_footer_chat_nselecionado);
                perfil.setBackgroundResource(R.drawable.ic_footer_perfil_selecionado);
            }
        });

    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.getBackStackEntryCount() > 0) {
            if (fragmentManager.getBackStackEntryCount() == 1) {
                finish();
            } else {
                fragmentManager.popBackStack();
                ajustaIconesFooter(fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount()-2).getName());
            }
        }
    }

    public void ajustaIconesFooter (String nomeFragment) {
        if (nomeFragment.equals("home")) {
            home.setBackgroundResource(R.drawable.ic_footer_home_selecionado);
            favoritos.setBackgroundResource(R.drawable.ic_footer_favoritos_nselecionado);
            pesquisa.setBackgroundResource(R.drawable.ic_footer_pesquisa_nselecionado);
            chat.setBackgroundResource(R.drawable.ic_footer_chat_nselecionado);
            perfil.setBackgroundResource(R.drawable.ic_footer_perfil_nselecionado);
        } else if (nomeFragment.equals("favoritos")) {
            home.setBackgroundResource(R.drawable.ic_footer_home_nselecionado);
            favoritos.setBackgroundResource(R.drawable.ic_footer_favoritos_selecionado);
            pesquisa.setBackgroundResource(R.drawable.ic_footer_pesquisa_nselecionado);
            chat.setBackgroundResource(R.drawable.ic_footer_chat_nselecionado);
            perfil.setBackgroundResource(R.drawable.ic_footer_perfil_nselecionado);
        } else if (nomeFragment.equals("buscageral")) {
            home.setBackgroundResource(R.drawable.ic_footer_home_nselecionado);
            favoritos.setBackgroundResource(R.drawable.ic_footer_favoritos_nselecionado);
            pesquisa.setBackgroundResource(R.drawable.ic_footer_pesquisa_selecionado);
            chat.setBackgroundResource(R.drawable.ic_footer_chat_nselecionado);
            perfil.setBackgroundResource(R.drawable.ic_footer_perfil_nselecionado);
        } else if (nomeFragment.equals("chat")) {
            home.setBackgroundResource(R.drawable.ic_footer_home_nselecionado);
            favoritos.setBackgroundResource(R.drawable.ic_footer_favoritos_nselecionado);
            pesquisa.setBackgroundResource(R.drawable.ic_footer_pesquisa_nselecionado);
            chat.setBackgroundResource(R.drawable.ic_footer_chat_selecionado);
            perfil.setBackgroundResource(R.drawable.ic_footer_perfil_nselecionado);
        } else if (nomeFragment.equals("perfil")) {
            home.setBackgroundResource(R.drawable.ic_footer_home_nselecionado);
            favoritos.setBackgroundResource(R.drawable.ic_footer_favoritos_nselecionado);
            pesquisa.setBackgroundResource(R.drawable.ic_footer_pesquisa_nselecionado);
            chat.setBackgroundResource(R.drawable.ic_footer_chat_nselecionado);
            perfil.setBackgroundResource(R.drawable.ic_footer_perfil_selecionado);
        } else if (nomeFragment.equals("livrodetalhe")) {
            home.setBackgroundResource(R.drawable.ic_footer_home_nselecionado);
            favoritos.setBackgroundResource(R.drawable.ic_footer_favoritos_nselecionado);
            pesquisa.setBackgroundResource(R.drawable.ic_footer_pesquisa_nselecionado);
            chat.setBackgroundResource(R.drawable.ic_footer_chat_nselecionado);
            perfil.setBackgroundResource(R.drawable.ic_footer_perfil_nselecionado);
        } else if (nomeFragment.equals("adicionarLivro")) {
            home.setBackgroundResource(R.drawable.ic_footer_home_nselecionado);
            favoritos.setBackgroundResource(R.drawable.ic_footer_favoritos_nselecionado);
            pesquisa.setBackgroundResource(R.drawable.ic_footer_pesquisa_nselecionado);
            chat.setBackgroundResource(R.drawable.ic_footer_chat_nselecionado);
            perfil.setBackgroundResource(R.drawable.ic_footer_perfil_nselecionado);
        }
    }

    /*@Override
    public void onLivroSelected(Bitmap capaLivro, String idLivro, Volume.VolumeInfo dadosLivro) {
        Toast.makeText(getApplicationContext(),"Livro: " + dadosLivro.getTitle(),Toast.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();
        String tituloLivro = dadosLivro.getTitle();
        String autorLivro = dadosLivro.getAuthors().get(0);
        String anoLivro = dadosLivro.getPublishedDate();

        bundle.putString("tituloLivro",tituloLivro);
        bundle.putString("autorLivro",autorLivro);
        bundle.putString("anoLivro",anoLivro);
        bundle.putString("idLivro",idLivro);
        bundle.putParcelable("capaLivro",capaLivro);
        LivroDetalheFragment livroDetalheFragment = new LivroDetalheFragment();
        livroDetalheFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, livroDetalheFragment, "livroDetalheFrag").addToBackStack("livrodetalhe").commit();
    }*/


    @Override
    public void volumesUpdate(List<Volume> volumes) {
        this.volumeList = volumes;
    }

    @Override
    public void thumbnailsUpdate(List<Bitmap> capas) {
        this.thumbnails = capas;
    }

    @Override
    public void onLivroSelectedFavoritos(Bitmap capaLivro, String idLivro, Volume.VolumeInfo dadosLivro) {
        Bundle bundle = new Bundle();
        String tituloLivro = dadosLivro.getTitle();
        String autorLivro = dadosLivro.getAuthors() != null ? dadosLivro.getAuthors().get(0) : "None";
        String anoLivro = dadosLivro.getPublishedDate() != null ? dadosLivro.getPublishedDate() : "None";
        String categoriaLivro = dadosLivro.getCategories() != null ? dadosLivro.getCategories().get(0) : "None";
        String categ[] = categoriaLivro.split("/");
        categoriaLivro = categ[0];
        String urlCapaLivro = getAvailableImageLink(dadosLivro);
        bundle.putString("urlCapaLivro",urlCapaLivro);
        bundle.putString("categoriaLivro",categoriaLivro);
        bundle.putString("tituloLivro",tituloLivro);
        bundle.putString("autorLivro",autorLivro);
        bundle.putString("anoLivro",anoLivro);
        bundle.putString("idLivro",idLivro);

        bundle.putParcelable("capaLivro",capaLivro);
        LivroDetalheFragment livroDetalheFragment = new LivroDetalheFragment();
        livroDetalheFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, livroDetalheFragment, "livroDetalheFrag").addToBackStack("livrodetalhe").commit();

        home.setBackgroundResource(R.drawable.ic_footer_home_nselecionado);
        favoritos.setBackgroundResource(R.drawable.ic_footer_favoritos_nselecionado);
        pesquisa.setBackgroundResource(R.drawable.ic_footer_pesquisa_nselecionado);
        chat.setBackgroundResource(R.drawable.ic_footer_chat_nselecionado);
        perfil.setBackgroundResource(R.drawable.ic_footer_perfil_nselecionado);
    }

    @Override
    public void onResultPesquisaLivros(Volumes volumes) {
        for (int i = 0; i < volumes.getItems().size(); i++) {
            this.livrosresultadoPesquisa.add(volumes.getItems().get(i));
        }
    }

    @Override
    public void onResultPesquisaCapas(List<Bitmap> capas) {
        for (int i = 0; i < capas.size(); i++) {
            this.capasResuladoPesquisa.add(capas.get(i));
        }
    }

    @Override
    public void onLivroSelected(Bitmap capaLivro, String idLivro, Volume.VolumeInfo dadosLivro) {
        Bundle bundle = new Bundle();
        String tituloLivro = dadosLivro.getTitle();
        if (dadosLivro.getCategories() != null) {
            for (String opa : dadosLivro.getCategories()) {
                System.out.println("Categoria: " + opa);
            }
        }
        if (dadosLivro.getMainCategory() != null) {
            System.out.println("CAT PRINCIPAL: " + dadosLivro.getMainCategory());
        }
        String autorLivro = dadosLivro.getAuthors() != null ? dadosLivro.getAuthors().get(0) : "None";
        String anoLivro = dadosLivro.getPublishedDate() != null ? dadosLivro.getPublishedDate() : "None";
        String categoriaLivro = dadosLivro.getCategories() != null ? dadosLivro.getCategories().get(0) : "None";
        String categ[] = categoriaLivro.split("/");
        categoriaLivro = categ[0];
        String urlCapaLivro = getAvailableImageLink(dadosLivro);
        bundle.putString("urlCapaLivro",urlCapaLivro);
        bundle.putString("categoriaLivro",categoriaLivro);
        bundle.putString("tituloLivro",tituloLivro);
        bundle.putString("autorLivro",autorLivro);
        bundle.putString("anoLivro",anoLivro);
        bundle.putString("idLivro",idLivro);
        bundle.putParcelable("capaLivro",capaLivro);
        LivroDetalheFragment livroDetalheFragment = new LivroDetalheFragment();
        livroDetalheFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, livroDetalheFragment, "livroDetalheFrag").addToBackStack("livrodetalhe").commit();

        home.setBackgroundResource(R.drawable.ic_footer_home_nselecionado);
        favoritos.setBackgroundResource(R.drawable.ic_footer_favoritos_nselecionado);
        pesquisa.setBackgroundResource(R.drawable.ic_footer_pesquisa_nselecionado);
        chat.setBackgroundResource(R.drawable.ic_footer_chat_nselecionado);
        perfil.setBackgroundResource(R.drawable.ic_footer_perfil_nselecionado);


    }

    @Override
    public void atualizaCapasWishList(List<Bitmap> imgs) {
        this.thumbnails = imgs;
    }

    @Override
    public void atualizaBooksWishlist(List<Volume> volumes) {
        this.volumeList = volumes;
    }

    private String getAvailableImageLink(Volume.VolumeInfo dadosLivro) {
        if (dadosLivro.getImageLinks() != null) {
            if(dadosLivro.getImageLinks().getThumbnail() != null){
                return dadosLivro.getImageLinks().getThumbnail();
            }else if (dadosLivro.getImageLinks().getSmallThumbnail() != null) {
                return dadosLivro.getImageLinks().getSmallThumbnail();
            } else if (dadosLivro.getImageLinks().getSmall() != null){
                return dadosLivro.getImageLinks().getSmall();
            } else if (dadosLivro.getImageLinks().getMedium() != null) {
                return dadosLivro.getImageLinks().getMedium();
            } else if (dadosLivro.getImageLinks().getLarge() != null){
                return dadosLivro.getImageLinks().getLarge();
            }
        }
        return linkCapaFalha;
    }

    @Override
    public void volumesPessoalUpdate(List<Volume> volumes) {
        this.livrosEstantePessoal = volumes;
    }

    @Override
    public void capasPessoalUpdate(List<Bitmap> capas) {
        this.capasEstantePessoal = capas;
    }


    @Override
    public void livrosCategoriasUpdate(List<Volume> ficcao, List<Volume> acao, List<Volume> terror, List<Volume> aventura) {
        this.livrosFiccao = ficcao;
        this.livrosAcao = acao;
        this.livrosTerror = terror;
        this.livrosAventura = aventura;
    }

    @Override
    public void capasCategoriasUpdate(List<Bitmap> ficcao, List<Bitmap> acao, List<Bitmap> terror, List<Bitmap> aventura) {
        this.capasFiccao = ficcao;
        this.capasAcao = acao;
        this.capasTerror = terror;
        this.capasAventura = aventura;
    }

    @Override
    public void adicionarLivro() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, adicionarLivroBuscaFragment,"adicionarLivroFrag").addToBackStack("adicionarLivro").commit();
        home.setBackgroundResource(R.drawable.ic_footer_home_nselecionado);
        favoritos.setBackgroundResource(R.drawable.ic_footer_favoritos_nselecionado);
        pesquisa.setBackgroundResource(R.drawable.ic_footer_pesquisa_nselecionado);
        chat.setBackgroundResource(R.drawable.ic_footer_chat_nselecionado);
        perfil.setBackgroundResource(R.drawable.ic_footer_perfil_nselecionado);
    }

    @Override
    public void onSolicitarLivro(User user, String bookCondition, String bookEdition, String idLivro, String bookTitle,String bookCoverUrl) {
        solicitarLivro(user,bookCondition,bookEdition,idLivro, bookTitle,bookCoverUrl);

    }


    public boolean solicitarLivro(final User user, final String bookCondition, final String bookEdition, final String idLivro, final String bookTitle, final String bookCoverUrl){

        final String userId = mAuth.getCurrentUser().getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User userRequest = dataSnapshot.getValue(User.class);
                        if (!userRequest.haveRequestTransaction(user.getUserId(), idLivro)) {
                            final String key = mDatabase.child("transactions").push().getKey();
                            final TransactionInfo transaction = new TransactionInfo(key,userId,user.getUserId(),idLivro, bookTitle, bookCoverUrl, bookCondition, bookEdition);
                            mDatabase.child("transactions").child(key).setValue(transaction);
                            userRequest.addTransaction(transaction);
                            mDatabase.child("users").child(userRequest.getUserId()).setValue(userRequest);
                            mDatabase.child("users").child(user.getUserId()).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            User userReceive = dataSnapshot.getValue(User.class);
                                            if (!userReceive.haveDeliverTransaction(userId, idLivro)) {
                                                userReceive.addTransaction(transaction);
                                                mDatabase.child("users").child(userReceive.getUserId()).setValue(userReceive);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Toast.makeText(getApplicationContext(), "Erro",
                                                    Toast.LENGTH_SHORT).show();
                                            Log.w(TAG, "addRemoveBookEstante:onCancelled", databaseError.toException());
                                        }
                                    });
                            Toast.makeText(getApplicationContext(), "Livro solicitado com sucesso!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Você já solicitou esse livro!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Erro",
                                Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "addRemoveBookEstante:onCancelled", databaseError.toException());
                    }
                });

        return false;
    }


    @Override
    public void onLivroSelectedAdicao(Bitmap capa, String id, Volume.VolumeInfo dadosLivro) {

        Bundle bundle = new Bundle();
        String tituloLivro = dadosLivro.getTitle();
        String autorLivro = dadosLivro.getAuthors().get(0);
        String anoLivro = dadosLivro.getPublishedDate();
        String editora = dadosLivro.getPublisher() != null ? dadosLivro.getPublisher() : "Info indisponivel";
        String categoriaLivro = dadosLivro.getCategories() != null ? dadosLivro.getCategories().get(0) : dadosLivro.getMainCategory();
        String urlCapaLivro = getAvailableImageLink(dadosLivro);
        bundle.putString("urlCapaLivro",urlCapaLivro);
        bundle.putString("categoriaLivro",categoriaLivro);
        bundle.putString("tituloLivro",tituloLivro);
        bundle.putString("autorLivro",autorLivro);
        bundle.putString("anoLivro",anoLivro);
        bundle.putString("idLivro",id);
        bundle.putParcelable("capaLivro",capa);
        bundle.putString("editora",editora);
        AdicionarLivroFragment adicionarLivroFragment = new AdicionarLivroFragment();
        adicionarLivroFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, adicionarLivroFragment, "adicionarLivroFinalFrag").addToBackStack("adicaoLivroOK").commit();

    }

    @Override
    public void adicionarLivroFinalizada(String idLivro, String conservacao, String edicao, String titulo, String autor, String ano, String categoria, String urlCapa) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
        fragmentManager.popBackStack();

        home.setBackgroundResource(R.drawable.ic_footer_home_selecionado);
        favoritos.setBackgroundResource(R.drawable.ic_footer_favoritos_nselecionado);
        pesquisa.setBackgroundResource(R.drawable.ic_footer_pesquisa_nselecionado);
        chat.setBackgroundResource(R.drawable.ic_footer_chat_nselecionado);
        perfil.setBackgroundResource(R.drawable.ic_footer_perfil_nselecionado);

        addOrRemoveBookEstante(idLivro,conservacao,edicao);
        registerBookShowcase(idLivro,conservacao,edicao, titulo, autor, ano, categoria, urlCapa);
    }

    private boolean addOrRemoveBookEstante(final String idLivro, final String condicao, final String edicao){
        final String userId = mAuth.getCurrentUser().getUid();
        mDatabase.child("shelves").child(userId).child(SHELF_AVAILABLE).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Shelf shelf = dataSnapshot.getValue(Shelf.class);
                        // Shelf nao existe ainda
                        if(shelf == null){
                            Toast.makeText(getApplicationContext(), "Shelf nao encontrada, criando...",
                                    Toast.LENGTH_SHORT).show();
                            // Cria nova shelf
                            shelf = new Shelf(userId,SHELF_AVAILABLE);
                        }
                            // Adiciona livro na lista
                            Toast.makeText(getApplicationContext(), "Adicionando livro da lista!",
                                    Toast.LENGTH_SHORT).show();
                            if(shelf.getBooks() == null){
                                shelf.initializeBooks();
                            }
                            shelf.getBooks().add(idLivro);
                            shelf.setBookCount(shelf.getBookCount()+1);
                        // Atualiza dados
                        mDatabase.child("shelves").child(userId).child(SHELF_AVAILABLE).setValue(shelf);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Erro",
                                Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "addRemoveBookEstante:onCancelled", databaseError.toException());
                    }
                });



        return false;
    }

    private boolean registerBookShowcase(final String idLivro, final String condicao, final String edicao, final String tituloLivro, final String autorLivro, final String anoLivro, final String categoriaLivro, final String urlCapaLivro){
        final String userId = mAuth.getCurrentUser().getUid();

        mDatabase.child("showcase").child(idLivro).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        BookInfo bookInfo = dataSnapshot.getValue(BookInfo.class);
                        // Shelf nao existe ainda
                        if(bookInfo == null){
                            // Cria nova shelf
                            bookInfo = new BookInfo(idLivro,tituloLivro,autorLivro,anoLivro,categoriaLivro,urlCapaLivro);
                        }

                            // Adiciona livro na lista
                            if(bookInfo.getUsers() == null){
                                bookInfo.initializeUsers();
                            }
                            bookInfo.addBook(userId,condicao,edicao);

                        // Atualiza dados
                        if(bookInfo.getBookCount() != 0) {
                            mDatabase.child("showcase").child(idLivro).setValue(bookInfo);
                        } else {
                            mDatabase.child("showcase").child(idLivro).removeValue();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Erro",
                                Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "addRemoveBookEstante:onCancelled", databaseError.toException());
                    }
                });
        return false;
    }

    @Override
    public void onUserSelected(User usuarioSelecionado) {
        System.out.println("Email: " + usuarioSelecionado.getEmail());
        Bundle bundle = new Bundle();
        bundle.putString("nome",usuarioSelecionado.getName());
        bundle.putString("nomeCompleto",usuarioSelecionado.getFullName());
        bundle.putString("email",usuarioSelecionado.getEmail());
        bundle.putString("userID",usuarioSelecionado.getUserId());
        bundle.putString("profilePic",usuarioSelecionado.getProfilePictureUrl());
        UsuarioBuscadoFragment usuarioBuscadoFragment = new UsuarioBuscadoFragment();
        usuarioBuscadoFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, usuarioBuscadoFragment, "usuarioBuscadoFragment").addToBackStack("usuarioBuscado").commit();
    }

    @Override
    public void onConfirmarTransacao(int tipoAcao, User u1, User u2, TransactionInfo tinfo) {
        System.out.println("Usuario 1: " + u1.getFullName());
        System.out.println("Usuario 2: " + u2.getFullName());
        System.out.println("Transacao: " + tinfo.getTransactionId());
        if (tipoAcao == 0) { //CONFIRMAR
           finalizaTransacao(u1,u2,tinfo);
        } else { //CANCELAMENTO
            cancelaTransacao(u1,u2,tinfo);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, chatFragment,"chatFrag").addToBackStack("chat").commit();
    }

    public void cancelaTransacao(User user1, User user2, final TransactionInfo transaction){
        final String userId = mAuth.getCurrentUser().getUid();

        mDatabase.child("transactions").child(transaction.getTransactionId()).removeValue();
        Toast.makeText(this.getApplicationContext(), "Cancelando transação...",
                Toast.LENGTH_SHORT).show();

        mDatabase.child("users").child(user1.getUserId()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User u1 = dataSnapshot.getValue(User.class);
                        // Shelf nao existe ainda

                        if (u1!= null){
                            u1.removeTransaction(transaction);

                            mDatabase.child("users").child(u1.getUserId()).setValue(u1);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Erro",
                                Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "addRemoveBookEstante:onCancelled", databaseError.toException());
                    }
                });

        mDatabase.child("users").child(user2.getUserId()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User u2 = dataSnapshot.getValue(User.class);
                        // Shelf nao existe ainda

                        if (u2!= null){
                             u2.removeTransaction(transaction);

                            mDatabase.child("users").child(u2.getUserId()).setValue(u2);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Erro",
                                Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "addRemoveBookEstante:onCancelled", databaseError.toException());
                    }
                });


    }

    public void finalizaTransacao(final User user1, final User user2, final TransactionInfo transaction){
        final String userId = mAuth.getCurrentUser().getUid();

        mDatabase.child("transactions").child(transaction.getTransactionId()).removeValue();
        Toast.makeText(this.getApplicationContext(), "Finalizando transação...",
                Toast.LENGTH_SHORT).show();

        mDatabase.child("showcase").child(transaction.getBookId()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        BookInfo book = dataSnapshot.getValue(BookInfo.class);

                        if (book!= null){
                            book.removeBook(user2.getUserId());
                            mDatabase.child("showcase").child(transaction.getBookId()).setValue(book);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Erro",
                                Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "addRemoveBookEstante:onCancelled", databaseError.toException());
                    }
                });

        mDatabase.child("shelves").child(user2.getUserId()).child(SHELF_AVAILABLE).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Shelf shelf = dataSnapshot.getValue(Shelf.class);

                        if (shelf!= null){
                            shelf.removeBook(transaction.getBookId());
                            mDatabase.child("shelves").child(user2.getUserId()).child(SHELF_AVAILABLE).setValue(shelf);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Erro",
                                Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "addRemoveBookEstante:onCancelled", databaseError.toException());
                    }
                });

        mDatabase.child("users").child(user1.getUserId()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User u1 = dataSnapshot.getValue(User.class);
                        // Shelf nao existe ainda

                        if (u1!= null){
                            u1.removeTransactionById(transaction.getTransactionId());
                            u1.getScore().setBooksTaken(u1.getScore().getBooksTaken()+1);
                            mDatabase.child("users").child(u1.getUserId()).setValue(u1);
                            }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Erro",
                                Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "addRemoveBookEstante:onCancelled", databaseError.toException());
                    }
                });

        mDatabase.child("users").child(user2.getUserId()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User u2 = dataSnapshot.getValue(User.class);
                        // Shelf nao existe ainda

                        if (u2!= null){
                            for (TransactionInfo t : u2.getTransactions()){
                                if(t.getTransactionId().equals(transaction.getTransactionId())){
                                    u2.removeTransaction(t);
                                }
                            }
                            u2.getScore().setBooksGiven(u2.getScore().getBooksGiven()+1);
                            mDatabase.child("users").child(u2.getUserId()).setValue(u2);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Erro",
                                Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "addRemoveBookEstante:onCancelled", databaseError.toException());
                    }
                });


    }
}
