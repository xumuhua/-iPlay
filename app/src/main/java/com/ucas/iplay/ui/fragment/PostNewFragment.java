package com.ucas.iplay.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.ucas.iplay.R;
import com.ucas.iplay.ui.base.BaseFragment;

import java.util.Calendar;

/**
 * Created by ivanchou on 1/19/2015.
 */
public class PostNewFragment extends BaseFragment implements CalendarDatePickerDialog.OnDateSetListener {
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";


    private TextView mStartDataTv;
    private TextView mStartTimeTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_postnew, container, false);

        mStartDataTv = (TextView) view.findViewById(R.id.tv_start_date);
        mStartTimeTv = (TextView) view.findViewById(R.id.tv_start_time);

        mStartDataTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return view;
    }

    @Override
    public void onDateSet(CalendarDatePickerDialog calendarDatePickerDialog, int i, int i2, int i3) {

    }
}
