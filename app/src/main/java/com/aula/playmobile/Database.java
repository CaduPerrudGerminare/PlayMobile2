package com.aula.playmobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Database {
    private FirebaseFirestore db;

    public Database() {
        db = FirebaseFirestore.getInstance();
    }

    public void loginWithEmailAndPassword(String email, String password, Context context, SharedPreferences sharedPrefs) {
        if (!Utils.isValidEmail(email)) {
            Toast.makeText(context, "E-mail inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Utils.isValidPassword(password)) {
            Toast.makeText(context, "Senha inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String userId = document.getId();
                                String username = document.getString("username");

                                sharedPrefs.edit()
                                        .putString("userId", userId)
                                        .putString("username", username)
                                        .apply();

                                Toast.makeText(context, "Login realizado com sucesso", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else {
                            Toast.makeText(context, "E-mail ou senha incorretos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
