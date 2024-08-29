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

import com.google.android.material.button.MaterialButton;
import com.nate.stctickets.R;
import com.nate.stctickets.activities.AddBalance;
import com.nate.stctickets.models.BalanceUpdater;

public class WalletFragment extends Fragment {

    private View view;
    private TextView tvBalance;
    private MaterialButton addBalance;
    private ImageView backBtn;
    private BalanceUpdater balanceUpdater;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.wallet_fragment, container, false);
        initViews();

        addBalance.setOnClickListener(v -> navigateBalance());
        backBtn.setOnClickListener(v -> getActivity().finish());
        getBalance();

        return view;
    }

    private void initViews() {
        tvBalance = view.findViewById(R.id.balance);
        addBalance = view.findViewById(R.id.add_balance);
        backBtn = view.findViewById(R.id.back_btn);
        balanceUpdater = new BalanceUpdater(getContext(), null);
    }

    private void getBalance() {


        // Fetch balance and handle the result
        balanceUpdater.fetchBalance(new BalanceUpdater.BalanceCallback() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onBalanceFetched(double balance) {
                // Handle the fetched balance here
                tvBalance.setText("GHC " + String.format("%.2f", balance));
                System.out.println("Current balance: " + balance);
                // You can update UI components with the fetched balance here
            }

            @Override
            public void onError(Exception e) {
                // Handle the error here
                Toast.makeText(getContext(), "Error fetching balance: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("Error fetching balance: " + e.getMessage());
            }
        });
    }

    private void navigateBalance(){
        startActivity(new Intent(getActivity(), AddBalance.class));
    }

}
