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



public class ChatRoomRecyclerViewAdapter extends RecyclerView.Adapter<ChatRoomRecyclerViewAdapter.ChatRoomViewHolder> {
    private final List<ChatRoom> mChatRoom;

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

    class ChatRoomViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private FragmentChatroomCardBinding binding;

        public ChatRoomViewHolder(@NonNull View view) {
            super(view);
            mView = view;
            binding = FragmentChatroomCardBinding.bind(view);
        }

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
