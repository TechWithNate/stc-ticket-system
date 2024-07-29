package com.nate.stctickets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.nate.stctickets.R;
import com.nate.stctickets.activities.AddBalance;

public class WalletFragment extends Fragment {

    private View view;
    private TextView balance;
    private MaterialButton addBalance;
    private ImageView backBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.wallet_fragment, container, false);
        initViews();

        addBalance.setOnClickListener(v -> navigateBalance());
        backBtn.setOnClickListener(v -> getActivity().finish());

        return view;
    }

    private void initViews() {
        balance = view.findViewById(R.id.balance);
        addBalance = view.findViewById(R.id.add_balance);
        backBtn = view.findViewById(R.id.back_btn);
    }

    private void navigateBalance(){
        startActivity(new Intent(getActivity(), AddBalance.class));
    }
}
