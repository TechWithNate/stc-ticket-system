package com.nate.stctickets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nate.stctickets.R;
import com.nate.stctickets.models.BookingModel;

import java.util.ArrayList;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private ArrayList<BookingModel> bookingList;
    private Context context;
    private TicketClicked ticketClicked;

    public interface TicketClicked{
        void onTicketClicked(int position);
    }

    public BookingAdapter(ArrayList<BookingModel> bookingList, Context context, TicketClicked ticketClicked) {
        this.bookingList = bookingList;
        this.context = context;
        this.ticketClicked = ticketClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingModel bookingModel = bookingList.get(position);
        holder.passengerName.setText(bookingModel.getPassengerName());
        holder.fromLocation.setText(bookingModel.getFrom());
        holder.toLocation.setText(bookingModel.getTo());
        holder.date.setText(bookingModel.getDate());
        holder.time.setText(bookingModel.getTime());
        holder.seatNo.setText(bookingModel.getSeatNumber());

        holder.itemView.setOnClickListener(v -> ticketClicked.onTicketClicked(position));

    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView passengerName;
        private TextView fromLocation;
        private TextView toLocation;
        private TextView date;
        private TextView time;
        private TextView seatNo;

        public ViewHolder(View itemView) {
            super(itemView);
            passengerName = itemView.findViewById(R.id.passenger_name);
            fromLocation = itemView.findViewById(R.id.from_location);
            toLocation = itemView.findViewById(R.id.to_location);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            seatNo = itemView.findViewById(R.id.seat_no);
        }
    }

}
