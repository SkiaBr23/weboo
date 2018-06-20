package unb.com.br.booksblank.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import unb.com.br.booksblank.R;

public class BuscaGeral extends AppCompatActivity {

    private EditText editTxtPesquisa;
    private ImageButton clearPesquisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_geral);

        setupUIComponents();
    }

    public void setupUIComponents () {

        editTxtPesquisa = (EditText)findViewById(R.id.editTxtPesquisa);
        clearPesquisa = (ImageButton)findViewById(R.id.btnLimparPesquisar);

        editTxtPesquisa.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Toast.makeText(getApplication(),"Iniciar Pesquisa...",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        clearPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTxtPesquisa.setText("");
            }
        });

    }
}
