package edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.outgoing_request_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentOutgoingRequestCardBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.Contacts;


public class OutgoingRequestListViewAdapter extends RecyclerView.Adapter<OutgoingRequestListViewAdapter.ContactsViewHolder> {

    private Context mContext;
    private OutgoingRequestDeleteViewModel mOutgoingDeleteModel;
    private UserInfoViewModel mUserInfoModel;
    private final List<Contacts> mContacts;


    public OutgoingRequestListViewAdapter(Context context, List<Contacts> mContacts) {
        this.mContext = context;
        this.mContacts = mContacts;

        mOutgoingDeleteModel = new ViewModelProvider((ViewModelStoreOwner) context)
                .get(OutgoingRequestDeleteViewModel.class);
        mUserInfoModel = new ViewModelProvider((ViewModelStoreOwner) context)
                .get(UserInfoViewModel.class);
    }

    @NonNull
    @Override
    public OutgoingRequestListViewAdapter.ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OutgoingRequestListViewAdapter.ContactsViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_outgoing_request_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OutgoingRequestListViewAdapter.ContactsViewHolder holder, int position) {
        holder.setContacts(mContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private @NonNull
        FragmentOutgoingRequestCardBinding binding;


        ContactsViewHolder(@NonNull View view) {
            super(view);
            this.mView = view;
            this.binding = FragmentOutgoingRequestCardBinding.bind(view);
        }

        void setContacts(final Contacts contacts) {
            binding.imageContactIcon.setImageResource(R.drawable.ic_rainychat_launcher_foreground);
            binding.textContactEmail.setText(contacts.getEmail());
            binding.textContactName.setText(contacts.getName());
            binding.textContactoutgoingMemberID.setText(Integer.toString(contacts.getMemberID()));


            binding.contactoutgoingDeleteFab.setOnClickListener(view -> {
                mOutgoingDeleteModel.connectDeleteContacts(contacts.getMemberID(),
                        mUserInfoModel.getmJwt());
                binding.contactoutgoingDeleteFab.setVisibility(View.INVISIBLE);
            });


        }
    }
}
