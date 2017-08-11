package com.chandler.charles.testerino;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private EditText zipCodeEditText;
    private Button getForecastButton;
    private TextView cityNameTextView;
    private TextView currentTempTextView;
    private TextView forecastDetailTextView;
    private ProgressBar progressBar;
    private SimpleDraweeView backgroundImageView;
    private FloatingActionButton fab;

    private Retrofit retrofit;
    private CurrentWeatherViewModel viewModel;
    private String currentZipCode;

    private static final String BASE_URL = "https://api.openweathermap.org/";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_main);

        zipCodeEditText = (EditText) findViewById(R.id.zipCode);
        zipCodeEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                getWeather(zipCodeEditText.getText().toString());
                hideEditText();
                return false;
            }
        });
        zipCodeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                    hideEditText();
                }
            }
        });

        getForecastButton = (Button) findViewById(R.id.getForecastButton);
        getForecastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ForecastActivity.build(MainActivity.this, currentZipCode);
                startActivity(intent);
            }
        });

        cityNameTextView = (TextView) findViewById(R.id.cityName);
        currentTempTextView = (TextView) findViewById(R.id.currentTemp);
        forecastDetailTextView = (TextView) findViewById(R.id.forecastDetail);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        backgroundImageView = (SimpleDraweeView) findViewById(R.id.backgroundImageView);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zipCodeEditText.setVisibility(View.VISIBLE);
                zipCodeEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                fab.setVisibility(View.INVISIBLE);
                getForecastButton.setVisibility(View.INVISIBLE);
            }
        });

        CurrentWeatherViewModel viewModel = new CurrentWeatherViewModel();
        render(viewModel);
    }

    public void render(CurrentWeatherViewModel viewModel) {
        this.viewModel = viewModel;
        cityNameTextView.setText(viewModel.getCityName());
        currentTempTextView.setText(viewModel.getCurrentTemp());
        forecastDetailTextView.setText(viewModel.getCondition());
        backgroundImageView.setController(viewModel.getBackgroundController());
        getForecastButton.setVisibility(viewModel.isDefault() ? View.GONE : View.VISIBLE);
        currentZipCode = zipCodeEditText.getText().toString();
        zipCodeEditText.setText("");
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void getWeather(String zipCode) {
        if (zipCode.isEmpty()) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        WeatherAPI apiService = retrofit.create(WeatherAPI.class);

        String param = zipCode + ",us";

        Call<CurrentWeather> call = apiService.getWeather(param, getResources().getString(R.string.api_key));
        call.enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(@NonNull Call<CurrentWeather> call, @NonNull Response<CurrentWeather> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);

                    CurrentWeather currentWeather = response.body();
                    CurrentWeatherViewModel viewModel = new CurrentWeatherViewModel(currentWeather);
                    render(viewModel);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Log.d("1234", "Failed externally");

                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(MainActivity.this, jsonObject.getString("message").toUpperCase(), Toast.LENGTH_SHORT).show();
                    } catch (Exception error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<CurrentWeather> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d("1234", "Failed internally", t);
                Toast.makeText(MainActivity.this, "Try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideEditText() {
        zipCodeEditText.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.VISIBLE);
    }

}
