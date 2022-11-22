package edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts;

import android.icu.text.Transliterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentContactsBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentContactsCardBinding;

public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ContactsViewHolder> {

    private final List<Contacts> mContacts;

    public ContactsRecyclerViewAdapter(List<Contacts> mContacts) {
        this.mContacts = mContacts;
    }


    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactsViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contacts, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        holder.setContacts(mContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private FragmentContactsCardBinding binding;

        ContactsViewHolder(@NonNull View view) {
            super(view);
            this.mView = view;
            this.binding = FragmentContactsCardBinding.bind(view);
        }

        void setContacts(final Contacts contacts) {
            binding.imageContactIcon.setImageResource(R.drawable.ic_rainychat_launcher_foreground);
            binding.textContactEmail.setText(contacts.getEmail());
            binding.textContactName.setText(contacts.getName());

        }
    }
}
