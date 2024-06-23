package com.lksnext.arivas.domain;

import android.annotation.SuppressLint;
import android.os.Parcel;

import androidx.annotation.NonNull;

import com.google.android.material.datepicker.CalendarConstraints;
import java.util.Calendar;

@SuppressLint("ParcelCreator")
public class FutureDateValidator implements CalendarConstraints.DateValidator {

    private long minDate;
    private long maxDate;

    public FutureDateValidator() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        minDate = calendar.getTimeInMillis();

        calendar.add(Calendar.DAY_OF_YEAR, 7);
        maxDate = calendar.getTimeInMillis();
    }

    @Override
    public boolean isValid(long date) {
        return date >= minDate && date <= maxDate;    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}
