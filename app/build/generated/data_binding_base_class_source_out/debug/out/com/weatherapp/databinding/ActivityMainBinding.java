// Generated by view binder compiler. Do not edit!
package com.weatherapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.weatherapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button buttonCelsius;

  @NonNull
  public final Button buttonFahrenheit;

  @NonNull
  public final ImageView ivHumidity;

  @NonNull
  public final ImageView ivLocation;

  @NonNull
  public final ImageView ivMain;

  @NonNull
  public final ImageView ivMinMax;

  @NonNull
  public final ImageView ivSunrise;

  @NonNull
  public final ImageView ivSunset;

  @NonNull
  public final ImageView ivWind;

  @NonNull
  public final LinearLayout mainLayout;

  @NonNull
  public final TextView tvCountry;

  @NonNull
  public final TextView tvHumidity;

  @NonNull
  public final TextView tvMain;

  @NonNull
  public final TextView tvMainDescription;

  @NonNull
  public final TextView tvMax;

  @NonNull
  public final TextView tvMin;

  @NonNull
  public final TextView tvName;

  @NonNull
  public final TextView tvSpeed;

  @NonNull
  public final TextView tvSpeedUnit;

  @NonNull
  public final TextView tvSunriseTime;

  @NonNull
  public final TextView tvSunsetTime;

  @NonNull
  public final TextView tvTemp;

  private ActivityMainBinding(@NonNull LinearLayout rootView, @NonNull Button buttonCelsius,
      @NonNull Button buttonFahrenheit, @NonNull ImageView ivHumidity,
      @NonNull ImageView ivLocation, @NonNull ImageView ivMain, @NonNull ImageView ivMinMax,
      @NonNull ImageView ivSunrise, @NonNull ImageView ivSunset, @NonNull ImageView ivWind,
      @NonNull LinearLayout mainLayout, @NonNull TextView tvCountry, @NonNull TextView tvHumidity,
      @NonNull TextView tvMain, @NonNull TextView tvMainDescription, @NonNull TextView tvMax,
      @NonNull TextView tvMin, @NonNull TextView tvName, @NonNull TextView tvSpeed,
      @NonNull TextView tvSpeedUnit, @NonNull TextView tvSunriseTime,
      @NonNull TextView tvSunsetTime, @NonNull TextView tvTemp) {
    this.rootView = rootView;
    this.buttonCelsius = buttonCelsius;
    this.buttonFahrenheit = buttonFahrenheit;
    this.ivHumidity = ivHumidity;
    this.ivLocation = ivLocation;
    this.ivMain = ivMain;
    this.ivMinMax = ivMinMax;
    this.ivSunrise = ivSunrise;
    this.ivSunset = ivSunset;
    this.ivWind = ivWind;
    this.mainLayout = mainLayout;
    this.tvCountry = tvCountry;
    this.tvHumidity = tvHumidity;
    this.tvMain = tvMain;
    this.tvMainDescription = tvMainDescription;
    this.tvMax = tvMax;
    this.tvMin = tvMin;
    this.tvName = tvName;
    this.tvSpeed = tvSpeed;
    this.tvSpeedUnit = tvSpeedUnit;
    this.tvSunriseTime = tvSunriseTime;
    this.tvSunsetTime = tvSunsetTime;
    this.tvTemp = tvTemp;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.buttonCelsius;
      Button buttonCelsius = ViewBindings.findChildViewById(rootView, id);
      if (buttonCelsius == null) {
        break missingId;
      }

      id = R.id.buttonFahrenheit;
      Button buttonFahrenheit = ViewBindings.findChildViewById(rootView, id);
      if (buttonFahrenheit == null) {
        break missingId;
      }

      id = R.id.iv_humidity;
      ImageView ivHumidity = ViewBindings.findChildViewById(rootView, id);
      if (ivHumidity == null) {
        break missingId;
      }

      id = R.id.iv_location;
      ImageView ivLocation = ViewBindings.findChildViewById(rootView, id);
      if (ivLocation == null) {
        break missingId;
      }

      id = R.id.iv_main;
      ImageView ivMain = ViewBindings.findChildViewById(rootView, id);
      if (ivMain == null) {
        break missingId;
      }

      id = R.id.iv_min_max;
      ImageView ivMinMax = ViewBindings.findChildViewById(rootView, id);
      if (ivMinMax == null) {
        break missingId;
      }

      id = R.id.iv_sunrise;
      ImageView ivSunrise = ViewBindings.findChildViewById(rootView, id);
      if (ivSunrise == null) {
        break missingId;
      }

      id = R.id.iv_sunset;
      ImageView ivSunset = ViewBindings.findChildViewById(rootView, id);
      if (ivSunset == null) {
        break missingId;
      }

      id = R.id.iv_wind;
      ImageView ivWind = ViewBindings.findChildViewById(rootView, id);
      if (ivWind == null) {
        break missingId;
      }

      LinearLayout mainLayout = (LinearLayout) rootView;

      id = R.id.tv_country;
      TextView tvCountry = ViewBindings.findChildViewById(rootView, id);
      if (tvCountry == null) {
        break missingId;
      }

      id = R.id.tv_humidity;
      TextView tvHumidity = ViewBindings.findChildViewById(rootView, id);
      if (tvHumidity == null) {
        break missingId;
      }

      id = R.id.tv_main;
      TextView tvMain = ViewBindings.findChildViewById(rootView, id);
      if (tvMain == null) {
        break missingId;
      }

      id = R.id.tv_main_description;
      TextView tvMainDescription = ViewBindings.findChildViewById(rootView, id);
      if (tvMainDescription == null) {
        break missingId;
      }

      id = R.id.tv_max;
      TextView tvMax = ViewBindings.findChildViewById(rootView, id);
      if (tvMax == null) {
        break missingId;
      }

      id = R.id.tv_min;
      TextView tvMin = ViewBindings.findChildViewById(rootView, id);
      if (tvMin == null) {
        break missingId;
      }

      id = R.id.tv_name;
      TextView tvName = ViewBindings.findChildViewById(rootView, id);
      if (tvName == null) {
        break missingId;
      }

      id = R.id.tv_speed;
      TextView tvSpeed = ViewBindings.findChildViewById(rootView, id);
      if (tvSpeed == null) {
        break missingId;
      }

      id = R.id.tv_speed_unit;
      TextView tvSpeedUnit = ViewBindings.findChildViewById(rootView, id);
      if (tvSpeedUnit == null) {
        break missingId;
      }

      id = R.id.tv_sunrise_time;
      TextView tvSunriseTime = ViewBindings.findChildViewById(rootView, id);
      if (tvSunriseTime == null) {
        break missingId;
      }

      id = R.id.tv_sunset_time;
      TextView tvSunsetTime = ViewBindings.findChildViewById(rootView, id);
      if (tvSunsetTime == null) {
        break missingId;
      }

      id = R.id.tv_temp;
      TextView tvTemp = ViewBindings.findChildViewById(rootView, id);
      if (tvTemp == null) {
        break missingId;
      }

      return new ActivityMainBinding((LinearLayout) rootView, buttonCelsius, buttonFahrenheit,
          ivHumidity, ivLocation, ivMain, ivMinMax, ivSunrise, ivSunset, ivWind, mainLayout,
          tvCountry, tvHumidity, tvMain, tvMainDescription, tvMax, tvMin, tvName, tvSpeed,
          tvSpeedUnit, tvSunriseTime, tvSunsetTime, tvTemp);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
