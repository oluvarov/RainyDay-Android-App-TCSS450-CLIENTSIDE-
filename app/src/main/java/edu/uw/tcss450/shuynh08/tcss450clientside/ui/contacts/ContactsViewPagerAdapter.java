package edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.friend_list.FriendListFragment;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.incoming_request_list.IncomingRequestListFragment;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.outgoing_request_list.OutgoingRequestListFragment;

public class ContactsViewPagerAdapter extends FragmentStateAdapter {

    public ContactsViewPagerAdapter(@NonNull ContactsFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
            default:
                return new FriendListFragment();
            case 1:
                return new IncomingRequestListFragment();
            case 2:
                return new OutgoingRequestListFragment();
        }
    }


    @Override
    public int getItemCount() {
        return 3;
    }
}
