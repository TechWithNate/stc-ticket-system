package com.nate.stctickets.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nate.stctickets.R;
import com.nate.stctickets.activities.BookTicket;
import com.nate.stctickets.activities.TicketDetails;
import com.nate.stctickets.adapters.BookingAdapter;
import com.nate.stctickets.models.BookingModel;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment implements BookingAdapter.TicketClicked{

    private View view;
    private MaterialButton bookBtn;
    private ImageSlider slider;
    private TextView username;
    private TextView seeAllTickets;
    private RecyclerView ticketRecycler;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ImageView profileImage;
    private ArrayList<BookingModel> bookingModels;
    private BookingAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        initViews();

        bookBtn.setOnClickListener(v -> navigateBook());
        ArrayList<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel("https://i0.wp.com/www.gbcghanaonline.com/wp-content/uploads/2023/12/stc-bus-scaled.webp", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://ocdn.eu/images/pulscms/Yzk7MDA_/161adef903a830f9c15d43af06fa6023.jpg", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTa8uxz-7ibdT85xxDGM_BFLpNGkp7gTvPxpw&s", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://i0.wp.com/africantraveltimes.com/wp-content/uploads/2020/11/STC-Ghana-Buses.png?resize=700%2C430&ssl=1", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://i.ytimg.com/vi/fy0dIuYQSGA/maxresdefault.jpg", ScaleTypes.FIT));

        slider.setImageList(imageList, ScaleTypes.FIT);
        loadUserData();

        seeAllTickets.setOnClickListener(v -> navigateBookings());

        ticketRecycler = view.findViewById(R.id.ticket_recycler);
        ticketRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        ticketRecycler.setHasFixedSize(true);
        bookingModels = new ArrayList<>();
        adapter = new BookingAdapter(bookingModels, getContext(), this);
        ticketRecycler.setAdapter(adapter);


        fetchBookingModel();

        return view;
    }

    private void navigateBookings() {
        openFragment(new BookingsFragment());
    }

    private void initViews() {
        bookBtn = view.findViewById(R.id.bookBtn);
        slider = view.findViewById(R.id.image_slider);
        username = view.findViewById(R.id.username);
        firebaseAuth = FirebaseAuth.getInstance();
        seeAllTickets = view.findViewById(R.id.see_all);
        ticketRecycler = view.findViewById(R.id.ticket_recycler);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        profileImage = view.findViewById(R.id.profile_img);
    }

    private void loadUserData() {
        if (firebaseAuth.getCurrentUser() != null) {
            databaseReference.child(Objects.requireNonNull(firebaseAuth.getUid())).get().addOnSuccessListener(snapshot -> {
                if (snapshot.exists()) {
                    String name = snapshot.child("username").getValue(String.class);
                    String imageUrl = snapshot.child("image").getValue(String.class);
                    username.setText("Hello " + name + ",");
                    Glide.with(getActivity()).load(imageUrl).into(profileImage);

                } else {
                    Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void openFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void fetchBookingModel() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getUid()).child("tickets");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    BookingModel model = snapshot.getValue(BookingModel.class);
                    if (model != null){
                        bookingModels.add(model);
                    }else {
                        Toast.makeText(getContext(), "Booking not found", Toast.LENGTH_SHORT).show();
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onTicketClicked(int position) {
        viewTicketDetails(bookingModels.get(position));
    }

    private void viewTicketDetails(BookingModel bookingModel) {
        Intent intent = new Intent(getContext(), TicketDetails.class);
        intent.putExtra("ticket", bookingModel);
        startActivity(intent);
    }


        private void navigateBook () {
            startActivity(new Intent(getActivity(), BookTicket.class));
        }

    }
