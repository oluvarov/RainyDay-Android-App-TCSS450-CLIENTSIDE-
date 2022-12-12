package edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.incoming_request_list;

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
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentIncomingRequestCardBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.Contacts;


public class IncomingRequestListViewAdapter extends RecyclerView.Adapter<IncomingRequestListViewAdapter.ContactsViewHolder> {

    private Context context;
    private IncomingRequestApproveViewModel mIncomingRequestApproveModel;
    private UserInfoViewModel mUserInfoModel;
    private final List<Contacts> mContacts;

    public IncomingRequestListViewAdapter(Context context, List<Contacts> mContacts) {
        this.context = context;
        this.mContacts = mContacts;

        mIncomingRequestApproveModel = new ViewModelProvider((ViewModelStoreOwner) context).get(IncomingRequestApproveViewModel.class);
        mUserInfoModel = new ViewModelProvider((ViewModelStoreOwner) context).get(UserInfoViewModel.class);
    }


    @NonNull
    @Override
    public IncomingRequestListViewAdapter.ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IncomingRequestListViewAdapter.ContactsViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_incoming_request_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IncomingRequestListViewAdapter.ContactsViewHolder holder, int position) {
        holder.setContacts(mContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private @NonNull
        FragmentIncomingRequestCardBinding binding;

        ContactsViewHolder(@NonNull View view) {
            super(view);
            this.mView = view;
            this.binding = FragmentIncomingRequestCardBinding.bind(view);
        }

        void setContacts(final Contacts contacts) {
            binding.imageContactIcon.setImageResource(R.drawable.ic_rainychat_launcher_foreground);
            binding.textContactEmail.setText(contacts.getEmail());
            binding.textContactName.setText(contacts.getName());
            binding.textContactMemberID.setText(Integer.toString(contacts.getMemberID()));

            binding.contactIncomingApproveFab.setOnClickListener(button ->{
                mIncomingRequestApproveModel.connectApproveContact(contacts.getMemberID(),mUserInfoModel.getmJwt());
                binding.contactIncomingApproveFab.setVisibility(View.INVISIBLE);
            });
        }

    }
}
