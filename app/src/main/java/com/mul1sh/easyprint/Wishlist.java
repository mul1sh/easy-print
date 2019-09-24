package com.mul1sh.easyprint;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.mul1sh.easyprint.models.GenericProductModel;
import com.mul1sh.easyprint.models.SingleProductModel;
import com.mul1sh.easyprint.networksync.CheckInternetConnection;
import com.mul1sh.easyprint.usersession.UserSession;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class Wishlist extends AppCompatActivity {

    private FirebaseRecyclerAdapter<SingleProductModel,MovieViewHolder> adapter;

    //to get user session data
    private UserSession session;
    private String mobile;
    private RecyclerView mRecyclerView;

    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private LottieAnimationView tv_no_item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Wishlist");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        //retrieve session values and display on listviews
        getValues();

        //SharedPreference for Cart Value
        session = new UserSession(getApplicationContext());

        //validating session
        session.isLoggedIn();

        mRecyclerView = findViewById(R.id.recyclerview);
        tv_no_item = findViewById(R.id.tv_no_cards);
        FrameLayout activitycartlist = findViewById(R.id.frame_container);
        LottieAnimationView emptycart = findViewById(R.id.empty_cart);

        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if(session.getWishlistValue()>0) {
            populateRecyclerView();
        }else if(session.getWishlistValue() == 0)  {
            tv_no_item.setVisibility(View.GONE);
            activitycartlist.setVisibility(View.GONE);
            emptycart.setVisibility(View.VISIBLE);
        }
    }

    private void populateRecyclerView() {


        Query query = database
                .getReference()
                .child("wishlist")
                .child(mobile)
                .getRef();

        FirebaseRecyclerOptions<SingleProductModel> options =
                new FirebaseRecyclerOptions
                        .Builder<SingleProductModel>()
                        .setQuery(query, snapshot -> new SingleProductModel(
                                Integer.parseInt(Objects.requireNonNull(snapshot.child("prid").getValue()).toString()),
                                Integer.parseInt(Objects.requireNonNull(snapshot.child("no_of_items").getValue()).toString()),
                                Objects.requireNonNull(snapshot.child("useremail").getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("usermobile").getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("prname").getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("prprice").getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("primage").getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("prdesc").getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("message_header").getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("message_body").getValue()).toString()
                        ))
                        .build();


        //Say Hello to our new FirebaseUI android Element, i.e., FirebaseRecyclerAdapter
        final FirebaseRecyclerAdapter<SingleProductModel,MovieViewHolder> adapter
                = new FirebaseRecyclerAdapter<SingleProductModel, MovieViewHolder>(options) {
            @NonNull
            @Override
            public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cart_item_layout, parent, false);

                return new MovieViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MovieViewHolder viewHolder, int position, @NonNull SingleProductModel model) {
                if(tv_no_item.getVisibility()== View.VISIBLE){
                    tv_no_item.setVisibility(View.GONE);
                }
                viewHolder.cardname.setText(model.getPrname());
                viewHolder.cardprice.setText(String.format("$ %s", model.getPrprice()));
                viewHolder.cardcount.setText(String.format("Quantity : %d", model.getNo_of_items()));
                Picasso.with(Wishlist.this).load(model.getPrimage()).into(viewHolder.cardimage);

                viewHolder.carddelete.setOnClickListener(v -> {
                    Toast.makeText(Wishlist.this,getItem(position).getPrname(),Toast.LENGTH_SHORT).show();
                    getRef(position).removeValue();
                    session.decreaseWishlistValue();
                    startActivity(new Intent(Wishlist.this,Wishlist.class));
                    finish();
                });

                viewHolder.mView.setOnClickListener(v -> {
                    Intent intent = new Intent(Wishlist.this,IndividualProduct.class);
                    intent.putExtra("product",new GenericProductModel(model.getPrid(),model.getPrname(),model.getPrimage(),model.getPrdesc(),Float.parseFloat(model.getPrprice())));
                    startActivity(intent);
                });
            }
        };
        mRecyclerView.setAdapter(adapter);
    }

    //viewHolder for our Firebase UI
    public static class MovieViewHolder extends RecyclerView.ViewHolder{

        TextView cardname;
        ImageView cardimage;
        TextView cardprice;
        TextView cardcount;
        ImageView carddelete;

        View mView;
        MovieViewHolder(View v) {
            super(v);
            mView = v;
            cardname = v.findViewById(R.id.cart_prtitle);
            cardimage = v.findViewById(R.id.image_cartlist);
            cardprice = v.findViewById(R.id.cart_prprice);
            cardcount = v.findViewById(R.id.cart_prcount);
            carddelete = v.findViewById(R.id.deletecard);
        }
    }


    private void getValues() {

        //create new session object by passing application context
        session = new UserSession(getApplicationContext());

        //validating session
        session.isLoggedIn();

        //get User details if logged in
        HashMap<String, String> user = session.getUserDetails();
        mobile = user.get(UserSession.KEY_MOBiLE);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void viewProfile(View view) {
        startActivity(new Intent(Wishlist.this,Profile.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

    }

    public void Notifications(View view) {

        startActivity(new Intent(Wishlist.this,NotificationActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
