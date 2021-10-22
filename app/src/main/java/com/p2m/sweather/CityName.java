package com.p2m.sweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.p2m.sweather.databinding.ActivityCityNameBinding;

public class CityName extends AppCompatActivity {
    ActivityCityNameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityCityNameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.showdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.citytv.getText().toString().isEmpty()){
                    binding.citytv.setError("Please enter your city!");
                }
                else if (binding.countrytv.getText().toString().isEmpty()){
                    binding.countrytv.setError("Please enter your country!");
                }else {
                    startActivity(new Intent(CityName.this,MainActivity.class)
                            .putExtra("city",binding.citytv.getText().toString())
                            .putExtra("country",binding.countrytv.getText().toString()));
                    finish();
                }
            }
        });
    }
}