package com.example.trabalhomobile_;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
                                // Redireciona para inserir_placa ou Admin
                                Intent intent = new Intent(context, inserir_placa.class);
                                context.startActivity(intent);

                                return;
                            }
                        } else {
                            Toast.makeText(context, "E-mail ou senha incorretos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void carregarVeiculos(RecyclerView recyclerView, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("veiculos")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(context, "Erro: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        List<Veiculo> lista = new ArrayList<>();

                        for (QueryDocumentSnapshot doc : value) {
                            String placa = doc.getString("placa");
                            String entrada = doc.getString("entrada");
                            String saida = doc.getString("saida");

                            Veiculo v = new Veiculo(placa, entrada, saida);
                            lista.add(v);
                        }

                        AdapterVeiculo adapter = new AdapterVeiculo(lista);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }

    public void apagarRegistrosMesAnteriorPorSaida(Context context) {
        db.collection("veiculos").get().addOnSuccessListener(queryDocumentSnapshots -> {
            int[] apagados = {0};

            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                String saida = doc.getString("saida");

                if (saida != null && !saida.isEmpty()) {
                    try {
                        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                        Date dataSaida = formato.parse(saida);

                        Calendar agora = Calendar.getInstance();
                        Calendar mesAnterior = (Calendar) agora.clone();
                        mesAnterior.add(Calendar.MONTH, -1);

                        Calendar dataRegistro = Calendar.getInstance();
                        dataRegistro.setTime(dataSaida);

                        if (dataRegistro.get(Calendar.MONTH) == mesAnterior.get(Calendar.MONTH) &&
                                dataRegistro.get(Calendar.YEAR) == mesAnterior.get(Calendar.YEAR)) {

                            doc.getReference().delete();
                            apagados[0]++;
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            Toast.makeText(context,
                    apagados[0] > 0 ?
                            apagados[0] + " registros do mês anterior foram apagados." :
                            "Nenhum registro do mês anterior encontrado",
                    Toast.LENGTH_LONG
            ).show();
        });
    }

    public void deletarVeiculo(String placa, OnCompleteListener<Void> listener) {
        FirebaseFirestore.getInstance()
                .collection("veiculos")
                .document(placa)
                .delete()
                .addOnCompleteListener(listener);
    }





}
