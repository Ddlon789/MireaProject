package ru.mirea.malyushin.mireaproj.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.mirea.malyushin.mireaproj.R;
import ru.mirea.malyushin.mireaproj.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private FragmentProfileBinding binding;
    private final String NAME = "name";
    private final String SURNAME = "surname";
    private final String PATR = "patronymic";
    private final String TEL = "tel";

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("mirea_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(NAME, binding.name.getText().toString());
                editor.putString(SURNAME, binding.surname.getText().toString());
                editor.putString(PATR, binding.patronymic.getText().toString());
                editor.putString(TEL, binding.patronymic.getText().toString());
                editor.apply();

                binding.name.setText("");
                binding.surname.setText("");
                binding.patronymic.setText("");
                binding.tel.setText("");
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}