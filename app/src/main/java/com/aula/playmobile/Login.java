package com.aula.playmobile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.aula.playmobile.databinding.ActivityLoginBinding;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Login extends AppCompatActivity {

    private Database db = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // View references
        Button login = binding.loginButton;
        ImageView car = binding.car;
        ImageView city = binding.imageView2;
        LinearLayout layout = binding.linearLayout;
        EditText email = binding.editTextEmail;
        EditText password = binding.editTextPassword;

        // Secure preferences
        SharedPreferences sharedPrefs = null;
        try {
            MasterKey masterKey = new MasterKey.Builder(this)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            sharedPrefs = EncryptedSharedPreferences.create(
                    this,
                    "login_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        if (sharedPrefs == null) {
            Log.e("Login", "Failed to initialize encrypted preferences.");
            finish();
            return;
        }

        String userId = sharedPrefs.getString("userId", null);
        Log.d("Login", "UserId: " + userId);

        // Animation setup
        Glide.with(this).load(R.drawable.car).into(car);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child.getId() != R.id.car) {
                child.setAlpha(0f);
            }
        }
        city.setAlpha(0f);

        binding.main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.main.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                float finalX = car.getX();
                float finalY = car.getY();

                int centerX = (binding.main.getWidth() - car.getWidth()) / 2;
                int centerY = (binding.main.getHeight() - car.getHeight()) / 2;
                car.setX(centerX);
                car.setY(centerY);
                car.setScaleX(1.5f);
                car.setScaleY(1.5f);

                new Handler().postDelayed(() -> {
                    if (userId == null) {
                        // Animate car back
                        ObjectAnimator animX = ObjectAnimator.ofFloat(car, "x", finalX);
                        ObjectAnimator animY = ObjectAnimator.ofFloat(car, "y", finalY);
                        ObjectAnimator scaleX = ObjectAnimator.ofFloat(car, "scaleX", 1f);
                        ObjectAnimator scaleY = ObjectAnimator.ofFloat(car, "scaleY", 1f);
                        AnimatorSet moveSet = new AnimatorSet();
                        moveSet.playTogether(animX, animY, scaleX, scaleY);
                        moveSet.setDuration(1000);

                        moveSet.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                // Fade in UI
                                for (int i = 0; i < layout.getChildCount(); i++) {
                                    View child = layout.getChildAt(i);
                                    if (child.getId() != R.id.car) {
                                        child.animate().alpha(1f).setDuration(800).start();
                                    }
                                }
                                city.animate().alpha(1f).setDuration(800).start();

                                // Animate nav bar color
                                ValueAnimator navBarColorAnim = ValueAnimator.ofObject(
                                        new ArgbEvaluator(),
                                        Color.parseColor("#F8F9FB"),
                                        Color.parseColor("#CCD5FF")
                                );
                                navBarColorAnim.setDuration(800);
                                navBarColorAnim.addUpdateListener(animator -> {
                                    getWindow().setNavigationBarColor((int) animator.getAnimatedValue());
                                });
                                navBarColorAnim.start();
                            }
                        });

                        moveSet.start();
                    } else {
                        // Skip login, go to Home
//                        startActivity(new Intent(Login.this, HomeActivity.class));
//                        finish();
                    }
                }, 3000);
            }
        });

        SharedPreferences finalSharedPrefs = sharedPrefs;
        login.setOnClickListener(v -> {
            String emailValue = email.getText().toString();
            String passwordValue = password.getText().toString();

            Log.d("Login", "Email: " + emailValue + ", Password: " + passwordValue);

            db.loginWithEmailAndPassword(emailValue, passwordValue, Login.this, finalSharedPrefs);
        });
    }
}
