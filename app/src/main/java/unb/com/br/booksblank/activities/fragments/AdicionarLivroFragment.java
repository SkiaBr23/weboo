package unb.com.br.booksblank.activities.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import unb.com.br.booksblank.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdicionarLivroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdicionarLivroFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String tituloLivro;
    private String autorLivro;
    private String anoLivro;
    private String categoriaLivro;
    private String urlCapaLivro;
    private String editora;

    private String idLivro;
    private Bitmap capaLivro;
    private ImageView imgCapaLivro;
    private TextView txtTituloLivro;
    private TextView txtAutorLivro;
    private TextView txtEditoraLivro;
    private TextView txtAnoLivro;

    private OnAdicionarListener onAdicionarListener;

    RadioButton op1;
    RadioButton op2;
    RadioButton op3;

    private Button btnAdicionar;

    private EditText editTxtEdicao;
    private RadioGroup rgConservacao;


    public AdicionarLivroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdicionarLivro.
     */
    // TODO: Rename and change types and number of parameters
    public static AdicionarLivroFragment newInstance(String param1, String param2) {
        AdicionarLivroFragment fragment = new AdicionarLivroFragment();
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
            idLivro = getArguments().getString("idLivro");
            tituloLivro = getArguments().getString("tituloLivro");
            anoLivro = getArguments().getString("anoLivro");
            autorLivro = getArguments().getString("autorLivro");
            categoriaLivro = getArguments().getString("categoriaLivro");
            urlCapaLivro = getArguments().getString("urlCapaLivro");
            capaLivro = getArguments().getParcelable("capaLivro");
            editora = getArguments().getString("editora");
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
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_adicionar_livro, container, false);

        imgCapaLivro = (ImageView)root.findViewById(R.id.capaLivroAdicao);
        txtTituloLivro = (TextView)root.findViewById(R.id.tituloLivroAdicao);
        txtAutorLivro = (TextView)root.findViewById(R.id.autorAdicaoNome);
        txtEditoraLivro = (TextView)root.findViewById(R.id.editoraAdicaoNome);
        txtAnoLivro = (TextView)root.findViewById(R.id.anoAdicaoNome);

        txtTituloLivro.setText(tituloLivro);
        txtAutorLivro.setText(autorLivro);
        txtEditoraLivro.setText(editora);
        txtAnoLivro.setText(anoLivro);

        imgCapaLivro.setImageBitmap(capaLivro);

        btnAdicionar = (Button)root.findViewById(R.id.btnAdicionar);

        editTxtEdicao = (EditText)root.findViewById(R.id.editTxtEdicao);

        op1 = (RadioButton)root.findViewById(R.id.rgOp1);
        op2 = (RadioButton)root.findViewById(R.id.rgOp2);
        op3 = (RadioButton)root.findViewById(R.id.rgOp3);


        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTxtEdicao.getText() != null && !nenhumSelecionado()) {
                    RadioButton selected = getRBSelected();
                    String edicao = editTxtEdicao.getText().toString();
                    String conservacao = selected.getText().toString();
                    onAdicionarListener.adicionarLivroFinalizada(idLivro,conservacao,edicao, tituloLivro, autorLivro,anoLivro,categoriaLivro,urlCapaLivro);
                } else {
                    Toast.makeText(getActivity(),"Insira edição e conservação",Toast.LENGTH_LONG).show();
                }
            }
        });



        return root;
    }

    RadioButton getRBSelected () {
        if (op1.isChecked()) {
            return op1;
        } else if (op2.isChecked()) {
            return op2;
        } else {
            return op3;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAdicionarListener) {
            onAdicionarListener = (OnAdicionarListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAdicionarListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onAdicionarListener = null;
    }

    public interface OnAdicionarListener {
        void adicionarLivroFinalizada(String idLivro,String conservacao,String edicao, String tituloLivro, String autorLivro, String anoLivro, String categoriaLivro, String urlCapaLivro);
    }

    boolean nenhumSelecionado () {
        if (!op1.isChecked() && !op2.isChecked() && !op3.isChecked()) {
            return true;
        } else {
            return false;
        }
    }

}
