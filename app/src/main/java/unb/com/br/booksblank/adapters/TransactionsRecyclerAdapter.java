package unb.com.br.booksblank.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.services.books.model.Volume;

import java.util.ArrayList;
import java.util.List;

import unb.com.br.booksblank.R;
import unb.com.br.booksblank.model.TransactionInfo;
import unb.com.br.booksblank.model.User;


public class TransactionsRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewTransactions> {

    private Context context;
    private List<TransactionInfo> transactions;
    private List<Bitmap> bookCovers;
    private List<Bitmap> profilePicturesUsers1;
    private List<Bitmap> profilePicturesUsers2;
    private List<User> users1;
    private List <User> users2;
    private String currentUser;



    private ListAdapterListener mListener;

    public interface ListAdapterListener { // create an interface
        void onClickAtOKButton(int tipoAcao, User user1, User user2, TransactionInfo tinfo); // create callback function
    }

    public TransactionsRecyclerAdapter(Context context, ListAdapterListener listAdapterListener) {
        this.context = context;
        this.transactions = new ArrayList<>();
        this.bookCovers = new ArrayList<>();
        this.profilePicturesUsers1 = new ArrayList<>();
        this.profilePicturesUsers2 = new ArrayList<>();
        this.users1 = new ArrayList<>();
        this.users2 = new ArrayList<>();
        this.currentUser = "";
        this.mListener = listAdapterListener;
    }

    @Override
    public RecyclerViewTransactions onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_transaction, null);
        RecyclerViewTransactions rcv = new RecyclerViewTransactions(layoutView);
        return rcv;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public void setUsers1(List<User> users1) {
        this.users1 = users1;
    }

    public void setUsers2(List<User> users2) {
        this.users2 = users2;
    }

    public void setBookCovers(List<Bitmap> bookCovers) {
        this.bookCovers = bookCovers;
    }

    public void setTransactions(List<TransactionInfo> transactions) {
        this.transactions = transactions;
    }

    public void setProfilePicturesUsers1(List<Bitmap> profilePicturesUsers1) {
        this.profilePicturesUsers1 = profilePicturesUsers1;
    }

    public void setProfilePicturesUsers2(List<Bitmap> profilePicturesUsers2) {
        this.profilePicturesUsers2 = profilePicturesUsers2;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewTransactions holder, final int position) {
        System.out.println("bindview");
        if (position < bookCovers.size() && position < users2.size() && position < users1.size() && position < transactions.size() && position < profilePicturesUsers2.size() && position < profilePicturesUsers1.size()) {
            holder.capaLivroTransaction.setImageBitmap(bookCovers.get(holder.getAdapterPosition()));
            holder.conservacaoLivroTransaction.setText("Conservação: " + transactions.get(holder.getAdapterPosition()).getBookCondition());
            holder.edicaoLivroTransaction.setText("Edição: " + transactions.get(holder.getAdapterPosition()).getBookEdition() + "º");
            holder.fotoPerfilUsuarioSolicitado.setImageBitmap(profilePicturesUsers2.get(holder.getAdapterPosition()));
            holder.fotoPerfilUsuarioSolicitante.setImageBitmap(profilePicturesUsers1.get(holder.getAdapterPosition()));
            holder.tituloLivroTransaction.setText(transactions.get(holder.getAdapterPosition()).getBookTitle());
            if(currentUser.equals(users1.get(holder.getAdapterPosition()).getUserId())){
                holder.btnStatusOK.setClickable(false);
                holder.btnStatusOK.setText("Aguardando resposta");
            }

            holder.btnStatusOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentUser.equals(users2.get(holder.getAdapterPosition()).getUserId())) {
                        mListener.onClickAtOKButton(0, users1.get(holder.getAdapterPosition()), users2.get(holder.getAdapterPosition()), transactions.get(holder.getAdapterPosition()));
                    }
                }
            });

            holder.btnStatusFail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickAtOKButton(1,users1.get(holder.getAdapterPosition()),users2.get(holder.getAdapterPosition()),transactions.get(holder.getAdapterPosition()));
                }
            });

            if(users2.get(holder.getAdapterPosition()).getUserId().equals(currentUser)){
                holder.nomeUsuarioSolicitado.setText(users1.get(holder.getAdapterPosition()).getFullName());
                holder.txtNumeroUsuarioSolicitado.setText(users1.get(holder.getAdapterPosition()).getPhoneNumber());
            }else {
                holder.nomeUsuarioSolicitado.setText(users2.get(holder.getAdapterPosition()).getFullName());
                holder.txtNumeroUsuarioSolicitado.setText(users2.get(holder.getAdapterPosition()).getPhoneNumber());
            }

        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}