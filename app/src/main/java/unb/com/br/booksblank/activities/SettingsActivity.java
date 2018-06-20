package unb.com.br.booksblank.activities;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import unb.com.br.booksblank.R;
import unb.com.br.booksblank.model.User;

public class SettingsActivity extends com.fnp.materialpreferences.PreferenceActivity {

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        /**
         * We load a PreferenceFragment which is the recommended way by Android
         * see @http://developer.android.com/guide/topics/ui/settings.html#Fragment
         * @TargetApi(11)
         */
        setPreferenceFragment(new MyPreferenceFragment());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String telefone = PreferenceManager.getDefaultSharedPreferences(this).getString("telefone","999999999");
        atualizaNumero(telefone);
        System.out.println("Numero de telefone: " + telefone);
        System.out.println("Botao de voltar pressionado");
    }

    void atualizaNumero(final String telefone){
        final String userId = mAuth.getCurrentUser().getUid();

        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            user.setPhoneNumber(telefone);
                            mDatabase.child("users").child(userId).setValue(user);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("ChatFragment", "getTransactions:onCancelled", databaseError.toException());
                    }
                });

    }
    public static class MyPreferenceFragment extends com.fnp.materialpreferences.PreferenceFragment {
        @Override
        public int addPreferencesFromResource() {
            return R.layout.activity_preferences; // Your preference file
        }
    }
}