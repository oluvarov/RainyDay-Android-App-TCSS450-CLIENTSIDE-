package edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.friend_list;

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
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentContactsCardBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.Contacts;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.incoming_request_list.IncomingRequestViewModel;

public class FriendListRecyclerViewAdapter extends RecyclerView.Adapter<FriendListRecyclerViewAdapter.ContactsViewHolder> {

    private Context context;
    private UserInfoViewModel mUserInfoModel;
    private FriendListDeleteViewModel mFriendListDeleteModel;
    private final List<Contacts> mContacts;

    public FriendListRecyclerViewAdapter(Context context, List<Contacts> mContacts) {
        this.mContacts = mContacts;
        this.context = context;

        mFriendListDeleteModel =  new ViewModelProvider((ViewModelStoreOwner) context)
                .get(FriendListDeleteViewModel.class);
        mUserInfoModel = new ViewModelProvider((ViewModelStoreOwner) context)
                .get(UserInfoViewModel.class);
    }


    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactsViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contacts_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        holder.setContacts(mContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private @NonNull FragmentContactsCardBinding binding;

        ContactsViewHolder(@NonNull View view) {
            super(view);
            this.mView = view;
            this.binding = FragmentContactsCardBinding.bind(view);
        }

        void setContacts(final Contacts contacts) {
            binding.imageContactIcon.setImageResource(R.drawable.ic_rainychat_launcher_foreground);
            binding.textContactEmail.setText(contacts.getEmail());
            binding.textContactName.setText(contacts.getName());
            binding.textContactfriendMemberID.setText(Integer.toString(contacts.getMemberID()));

            binding.buttonContactFriendDelete.setOnClickListener(button ->{
                    mFriendListDeleteModel.connectDeleteContacts(contacts.getMemberID()
                            , mUserInfoModel.getmJwt());
                    binding.buttonContactFriendDelete.setVisibility(View.INVISIBLE);
            });
        }
    }
}
