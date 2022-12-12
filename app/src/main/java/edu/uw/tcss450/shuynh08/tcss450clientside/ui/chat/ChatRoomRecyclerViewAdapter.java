package edu.uw.tcss450.shuynh08.tcss450clientside.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.shuynh08.tcss450clientside.R;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentChatroomBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentChatroomCardBinding;
import edu.uw.tcss450.shuynh08.tcss450clientside.databinding.FragmentWeatherCardBinding;

/**
 * A RecyclerViewAdapter used to help create chatroom cards and display them in a RecyclerView.
 */
public class ChatRoomRecyclerViewAdapter extends RecyclerView.Adapter<ChatRoomRecyclerViewAdapter.ChatRoomViewHolder> {
    private final List<ChatRoom> mChatRoom;

    /**
     * Constructor to initialize a list of ChatRoom objects.
     * @param ChatRoom the chatroom
     */
    public ChatRoomRecyclerViewAdapter(List<ChatRoom> ChatRoom) {
        this.mChatRoom = ChatRoom;
    }

    @NonNull
    @Override
    public ChatRoomRecyclerViewAdapter.ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatRoomRecyclerViewAdapter.ChatRoomViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chatroom_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomRecyclerViewAdapter.ChatRoomViewHolder holder, int position) {
        holder.setChatRoom(mChatRoom.get(position));
    }

    @Override
    public int getItemCount() {
        return mChatRoom.size();
    }

    /**
     * Inner class to set up and update ChatRoom cards.
     */
    class ChatRoomViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private FragmentChatroomCardBinding binding;

        /**
         * Constructor to set up the view binding.
         * @param view the View
         */
        public ChatRoomViewHolder(@NonNull View view) {
            super(view);
            mView = view;
            binding = FragmentChatroomCardBinding.bind(view);
        }

        /**
         * Set up an individual chatroom for appearance and interaction.
         * @param chatRoom the ChatRoom
         */
        void setChatRoom(final ChatRoom chatRoom) {
            binding.textRoomname.setText(chatRoom.getName());
            binding.imageRoomicon.setImageResource(R.drawable.ic_chat_personicon);
            binding.textChatID.setText(Integer.toString(chatRoom.getChatID()));

            binding.cardRoot.setOnClickListener(view -> {
                Navigation.findNavController(mView).navigate(
                    ChatRoomFragmentDirections
                            .actionChatRoomFragmentToNavigationChat(chatRoom.getChatID())
                );
            });
        }
    }
}
