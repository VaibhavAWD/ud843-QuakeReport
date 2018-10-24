package com.example.android.quakereport.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.quakereport.R;
import com.example.android.quakereport.model.Earthquake;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private static final String LOCATION_SEPARATOR = " of ";

    public EarthquakeAdapter(@NonNull Context context, @NonNull List<Earthquake> earthquakeList) {
        super(context, 0, earthquakeList);
    }

    class EarthquakeViewHolder {
        @BindView(R.id.tv_magnitude)
        TextView mMagnitude;

        @BindView(R.id.tv_offset)
        TextView mOffset;

        @BindView(R.id.tv_place)
        TextView mPlace;

        @BindView(R.id.tv_date)
        TextView mDate;

        @BindView(R.id.tv_time)
        TextView mTime;

        EarthquakeViewHolder(View earthquakeView) {
            ButterKnife.bind(this, earthquakeView);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View earthquakeView, @NonNull ViewGroup parent) {
        EarthquakeViewHolder earthquakeViewHolder;

        if (earthquakeView == null) {
            earthquakeView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_earthquake, parent, false);

            earthquakeViewHolder = new EarthquakeViewHolder(earthquakeView);

            earthquakeView.setTag(earthquakeViewHolder);
        } else {
            earthquakeViewHolder = (EarthquakeViewHolder) earthquakeView.getTag();
        }

        Earthquake earthquake = getItem(position);

        GradientDrawable magnitudeCircle = (GradientDrawable) earthquakeViewHolder.mMagnitude.getBackground();
        int magnitudeColor = getMagnitudeColor(earthquake.getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        String magnitude = getFormattedMagnitude(earthquake.getMagnitude());
        earthquakeViewHolder.mMagnitude.setText(magnitude);

        String originalPlace = earthquake.getPlace();
        String offset;
        String place;
        if (originalPlace.contains(LOCATION_SEPARATOR)) {
            String[] placeParts = originalPlace.split(LOCATION_SEPARATOR);
            offset = placeParts[0] + LOCATION_SEPARATOR;
            place = placeParts[1];
        } else {
            offset = getContext().getString(R.string.near_the);
            place = originalPlace;
        }

        earthquakeViewHolder.mOffset.setText(offset);

        earthquakeViewHolder.mPlace.setText(place);

        String date = getFormattedDate(earthquake.getTime());
        earthquakeViewHolder.mDate.setText(date);

        String time = getFormattedTime(earthquake.getTime());
        earthquakeViewHolder.mTime.setText(time);

        return earthquakeView;
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColor;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColor = R.color.magnitude1;
                break;
            case 2:
                magnitudeColor = R.color.magnitude2;
                break;
            case 3:
                magnitudeColor = R.color.magnitude3;
                break;
            case 4:
                magnitudeColor = R.color.magnitude4;
                break;
            case 5:
                magnitudeColor = R.color.magnitude5;
                break;
            case 6:
                magnitudeColor = R.color.magnitude6;
                break;
            case 7:
                magnitudeColor = R.color.magnitude7;
                break;
            case 8:
                magnitudeColor = R.color.magnitude8;
                break;
            case 9:
                magnitudeColor = R.color.magnitude9;
                break;
            default:
                magnitudeColor = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColor);
    }

    private String getFormattedMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private String getFormattedDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }

    private String getFormattedTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return timeFormat.format(date);
    }
}
