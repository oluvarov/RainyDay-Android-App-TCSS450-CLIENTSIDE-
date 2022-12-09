package edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentContactsBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.model.UserInfoViewModel;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.add_friend.AddFriendFragment;
import edu.uw.tcss450.shuynh08.tcss450clientside.ui.contacts.friend_list.FriendListViewModel;

public class ContactsFragment extends Fragment {

    private FriendListViewModel mContactsModel;
    private ContactsGetInfoViewModel mContactsGetInfoModel;
    private FragmentContactsBinding binding;
    private RecyclerView recyclerView;
    private int mMemberID;
    private UserInfoViewModel mUserInfoModel;
    private ContactsViewPagerAdapter mContactsViewPagerAdapter;
    ExtendedFloatingActionButton mAddFriendFab;



    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactsModel = new ViewModelProvider(getActivity())
                .get(FriendListViewModel.class);
        mContactsGetInfoModel = new ViewModelProvider(getActivity())
                .get(ContactsGetInfoViewModel.class);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentContactsBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContactsViewPagerAdapter = new ContactsViewPagerAdapter(this);
        binding.viewPagerContacts.setAdapter(mContactsViewPagerAdapter);

        binding.tabContactsLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPagerContacts.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        binding.viewPagerContacts.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabContactsLayout.getTabAt(position).select();
            }
        });

        mAddFriendFab = binding.addFriendFab;
        mAddFriendFab.setOnClickListener(this::displayAddFriendFragment);
    }


    private void displayAddFriendFragment(View fab) {
        Navigation.findNavController(getView()).navigate(
                ContactsFragmentDirections.actionNavigationContactsToAddFriendFragment());
    }


}