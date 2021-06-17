package com.example.micovid.pantallaprincipal.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.micovid.R;
import com.example.micovid.databinding.FragmentComoJugarBinding;
import com.example.micovid.databinding.FragmentPrimerJugarBinding;
import com.example.micovid.databinding.FragmentSegundoJugarBinding;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentPrimerJugarBinding binding1;
    private FragmentSegundoJugarBinding binding2;


    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = null;
        switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
            case 1:
                binding1 = FragmentPrimerJugarBinding.inflate(inflater, container, false);
                root = binding1.getRoot();
                break;
            case 2:
                binding2 = FragmentSegundoJugarBinding.inflate(inflater, container, false);
                root = binding2.getRoot();
                break;
        }



        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding1 = null;
        binding2 = null;
    }
}