package com.mul1sh.easyprint.productscategory;

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


import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.mul1sh.easyprint.Cart;
import com.mul1sh.easyprint.IndividualProduct;
import com.mul1sh.easyprint.NotificationActivity;
import com.mul1sh.easyprint.R;
import com.mul1sh.easyprint.models.GenericProductModel;
import com.mul1sh.easyprint.networksync.CheckInternetConnection;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class Keychains extends AppCompatActivity {


    private LottieAnimationView tv_no_item;
    private FirebaseRecyclerAdapter<GenericProductModel, Cards.MovieViewHolder> adapter;

    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        //Initializing our Recyclerview
        RecyclerView mRecyclerView = findViewById(R.id.my_recycler_view);
        tv_no_item = findViewById(R.id.tv_no_cards);


        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(mLayoutManager);
        }

        Query query = database
                .getReference()
                .child("Products")
                .child("Keychain")
                .getRef();

        FirebaseRecyclerOptions<GenericProductModel> options =
                new FirebaseRecyclerOptions.Builder<GenericProductModel>()
                        .setQuery(query, snapshot -> new GenericProductModel(
                                Integer.parseInt(Objects.requireNonNull(snapshot.child("cardid").getValue()).toString()),
                                Objects.requireNonNull(snapshot.child("cardname").getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("cardimage").getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("carddiscription").getValue()).toString(),
                                Float.parseFloat(Objects.requireNonNull(snapshot.child("cardprice").getValue()).toString()))
                        )
                        .build();

        //Say Hello to our new FirebaseUI android Element, i.e., FirebaseRecyclerAdapter
        adapter = new FirebaseRecyclerAdapter<GenericProductModel, Cards.MovieViewHolder>(options) {
            @NonNull
            @Override
            public Cards.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cards_cardview_layout, parent, false);

                return new Cards.MovieViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull Cards.MovieViewHolder viewHolder, int position, @NonNull GenericProductModel model) {
                if (tv_no_item.getVisibility() == View.VISIBLE) {
                    tv_no_item.setVisibility(View.GONE);
                }
                viewHolder.cardname.setText(model.getCardname());
                viewHolder.cardprice.setText(String.format("$ %s", model.getCardprice()));
                Picasso.with(Keychains.this).load(model.getCardimage()).into(viewHolder.cardimage);

                viewHolder.mView.setOnClickListener(v -> {
                    Intent intent = new Intent(Keychains.this, IndividualProduct.class);
                    intent.putExtra("product", getItem(position));
                    startActivity(intent);
                });
            }


        };


        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(adapter);
        }

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

    public void viewCart(View view) {
        startActivity(new Intent(Keychains.this, Cart.class));
        finish();
    }

    public void Notifications(View view) {
        startActivity(new Intent(Keychains.this, NotificationActivity.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
    }
}
