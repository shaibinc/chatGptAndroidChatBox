package com.cozy.apps.chatgpt;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> messages;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }
    public MessageAdapter() {

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView messageText;
        private TextView messageTime;
        private TextView messageId;

        public ViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message);
            messageTime = itemView.findViewById(R.id.text_time);
            messageId = itemView.findViewById(R.id.text_id);
        }

        public void bind(Message message) {
            messageText.setText(message.getContent());
            messageId.setText(message.getSender());
            messageTime.setText(message.getTimestamp());
        }
    }
}