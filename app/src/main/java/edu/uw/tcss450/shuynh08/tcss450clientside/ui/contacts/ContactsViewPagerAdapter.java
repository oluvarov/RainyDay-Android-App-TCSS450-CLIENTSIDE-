package edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.friend_list.FriendListFragment;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.incoming_request_list.IncomingRequestListFragment;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.outgoing_request_list.OutgoingRequestListFragment;

/**
 * A FragmentStateAdapter used to create tabs for the Contacts fragment and displays them.
 */
public class ContactsViewPagerAdapter extends FragmentStateAdapter {

    /**
     * Constructor for our FragmentStateAdapter, using super class's defaults.
     * @param fragmentActivity The current fragment
     */
    public ContactsViewPagerAdapter(@NonNull ContactsFragment fragmentActivity) {
        super(fragmentActivity);
    }

    /**
     * Constructs the various associated Contacts-related fragments based on the specified input.
     *      0 → FriendListFragment (Default)
     *      1 → IncomingRequestListFragment
     *      2 → OutgoingRequestListFragment
     * @param position An integer for one of the options
     * @return The selected fragment
     */
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

    /**
     * @return total number of Contacts-related tabs.
     */
    @Override
    public int getItemCount() {
        return 3;
    }
}
