package com.cooper.cooper.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.cooper.cooper.R;
import com.cooper.cooper.Utils;
import com.cooper.cooper.http_requests.GetRequests;
import com.cooper.cooper.http_requests.HTTPRequestListener;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.json.JSONObject;

public class Chat_Act extends AppCompatActivity implements HTTPRequestListener {

    private static final int SIGN_IN_REQUEST_CODE = 666;
    private static final int TYPE_ME = 69;
    private static final int TYPE_THEM = 96;
    private FirebaseListAdapter<ChatMessage> adapter;

    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;

    private String userName;
    private String userEmail;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        GetRequests get_account = new GetRequests(this);
        get_account.execute(Utils.URL + "/account");

        /*if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in/sign up
            startActivityForResult(
                    AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(),
                    SIGN_IN_REQUEST_CODE
            );
        } else {
            // User already signed in
            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();

            displayChatMessages();
        }*/

        /*Toast.makeText(this,
                "Welcome " + userName,
                Toast.LENGTH_LONG)
                .show();*/

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference ref = FirebaseDatabase.getInstance()
                                            .getReference()
                                            .child(String.valueOf(getIntent().getLongExtra("pool", 0)));
                DatabaseReference push = ref.push();
                String pushId = push.getKey();
                push.setValue(new ChatMessage(userName,
                                                userEmail,
                                                pushId,
                                                input.getText().toString(),
                                                userId));

                // Clear the input
                input.setText("");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /*
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this,
                        "Successfully signed in. Welcome!",
                        Toast.LENGTH_LONG)
                        .show();
                displayChatMessages();
            } else {
                Toast.makeText(this,
                        "We couldn't sign you in. Please try again later.",
                        Toast.LENGTH_LONG)
                        .show();

                // Close app
                finish();
            }
        }
    }*/

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this)
                    //.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this,
                                    "You have been signed out.",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // Close activity
                            finish();
                        }
                    });
        }
        return true;
    }*/

    private void displayChatMessages() {
        final RecyclerView listOfMessages = (RecyclerView) findViewById(R.id.list_of_messages);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child(String.valueOf(getIntent().getLongExtra("pool", 0)));

        FirebaseRecyclerOptions<ChatMessage> options =
                new FirebaseRecyclerOptions.Builder<ChatMessage>()
                        .setQuery(query, ChatMessage.class)
                        .build();


        // Preferred RecyclerView implementation
        FirebaseRecyclerAdapter<ChatMessage, FirebaseChatViewHolder> adapter = new FirebaseRecyclerAdapter<ChatMessage, FirebaseChatViewHolder>(options) {

            @Override
            public int getItemViewType(int position) {
                ChatMessage model = getItem(position);
                if (model.getAuthorEmail().equals(userEmail)) {
                    return TYPE_ME;
                } else {
                    return TYPE_THEM;
                }
            }

            @Override
            public FirebaseChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                if (viewType == TYPE_ME) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.chat_my_message, parent, false);
                    return new FirebaseChatViewHolder(view);
                } else {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.chat_their_message, parent, false);
                    return new FirebaseChatViewHolder(view);
                }
            }

            @Override
            public void onDataChanged() {
                listOfMessages.smoothScrollToPosition(getItemCount());
            }

            @Override
            protected void onBindViewHolder(FirebaseChatViewHolder holder, int position, ChatMessage model) {
                // Bind the Chat object to the ChatHolder
                holder.bindMessage(model);
            }
        };

        adapter.startListening();
        listOfMessages.setAdapter(adapter);
        listOfMessages.smoothScrollToPosition(adapter.getItemCount());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        //layoutManager.setReverseLayout(true);

        // auto scroll
        /*adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = adapter.getItemCount();
                int lastVisiblePosition =
                        layoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added chat_my_message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    layoutManager.scrollToPosition(positionStart);
                }
            }
        });*/

        listOfMessages.setLayoutManager(layoutManager);
    }

    @Override
    public void requestDone(Object objectRes, int statusCode) {
        if(objectRes instanceof JSONObject) {
            JSONObject response = (JSONObject) objectRes;
            try {
                Log.d("json obtained: ", response.toString());
                JSONObject userInfo = new JSONObject(response.getString("response"));
                userName = userInfo.getString("name");
                userEmail = userInfo.getString("email");
                userId = userInfo.getInt("_id");

                Log.wtf("userName chat", userName);
                displayChatMessages();
            } catch (Exception e) {
                Log.d("failed to fetch json", e.toString());
            }
        }

    }
}
