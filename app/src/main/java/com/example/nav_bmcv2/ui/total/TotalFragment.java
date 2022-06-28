package com.example.nav_bmcv2.ui.total;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nav_bmcv2.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class TotalFragment extends Fragment {

    private TotalViewModel mViewModel;
    private AppCompatButton start_date, end_date;

    public static TotalFragment newInstance() {
        return new TotalFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.total_fragment, container, false);
        start_date = view.findViewById(R.id.create_start_date_button);
        end_date = view.findViewById(R.id.create_end_date_button);
//        View.OnClickListener datePickerDialog;
        start_date.setOnClickListener(datePickerDialog);
        end_date.setOnClickListener(datePickerDialog);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        start_date.setText(simpleDateFormat.format(new Date()));
        end_date.setText(simpleDateFormat.format(new Date()));

        return view;
    }

    private View.OnClickListener datePickerDialog = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MaterialDatePicker.Builder<Pair<Long, Long>> build = MaterialDatePicker.Builder.dateRangePicker();
            MaterialDatePicker picker = build.build();
            picker.show(getParentFragmentManager(), picker.toString());
            picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                @Override
                public void onPositiveButtonClick(Object selection) {
                    Pair s = (Pair) selection;
                    long F = (Long) s.first;
                    long S = (Long) s.second;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    start_date.setText(simpleDateFormat.format(new Date(F)));
                    end_date.setText(simpleDateFormat.format(new Date(S)));
                }
            });
        }
    };
}