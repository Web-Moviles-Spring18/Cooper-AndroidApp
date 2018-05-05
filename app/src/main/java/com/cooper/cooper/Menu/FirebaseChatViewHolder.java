package com.cooper.cooper.Menu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cooper.cooper.R;

/**
 * Created by Javi on 3/29/2018.
 */

public class FirebaseChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    Context mContext;

    public FirebaseChatViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindMessage(ChatMessage message) {
        TextView messageText = (TextView) mView.findViewById(R.id.message_text);
        TextView messageUser = (TextView) mView.findViewById(R.id.message_user);
        TextView messageTime = (TextView) mView.findViewById(R.id.message_time);

        // Set their text
        messageText.setText(message.getMessage());
        messageUser.setText(message.getAuthorName());

        // Format the date before showing it
        //messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                //message.getTime()));
        messageTime.setText(message.getTime());
    }

    @Override
    public void onClick(View view) {

    }
}