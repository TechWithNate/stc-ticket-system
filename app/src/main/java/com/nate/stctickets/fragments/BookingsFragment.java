package com.nate.stctickets.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nate.stctickets.R;
import com.nate.stctickets.activities.TicketDetails;
import com.nate.stctickets.adapters.BookingAdapter;
import com.nate.stctickets.models.BookingModel;

import java.util.ArrayList;

public class BookingsFragment extends Fragment implements BookingAdapter.TicketClicked {

    private View view;
    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private ArrayList<BookingModel> bookingModels;
    private BookingAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bookings_fragment, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.ticket_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        bookingModels = new ArrayList<>();


        fetchBookingModel();

        adapter = new BookingAdapter(bookingModels, getContext(), this);
        recyclerView.setAdapter(adapter);

        return view;
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


}
