package com.aula.playmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable; // Importar para ColorDrawable
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class inserir_placa extends AppCompatActivity {

    private EditText edtPlaca;
    private Button botaoEntrada, botaoSaida;

    private String horaEntrada;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserir);

        edtPlaca = findViewById(R.id.editPlaca);
        botaoEntrada = findViewById(R.id.botaoEntrada);
        botaoSaida = findViewById(R.id.botaoSaida);

        db = FirebaseFirestore.getInstance();

        botaoEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placa = edtPlaca.getText().toString().trim();

                if (TextUtils.isEmpty(placa)) {
                    Toast.makeText(inserir_placa.this, "Por favor, insira a placa.", Toast.LENGTH_SHORT).show();
                    return;
                }

                horaEntrada = getDataHoraAtual();

                Map<String, Object> dados = new HashMap<>();
                dados.put("placa", placa);
                dados.put("entrada", horaEntrada);
                dados.put("saida", ""); // saída vazia no registro de entrada

                db.collection("veiculos")
                        .document(placa)
                        .set(dados)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(inserir_placa.this, "Entrada registrada: " + horaEntrada, Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(inserir_placa.this, "Erro ao registrar entrada: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
            }
        });

        botaoSaida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placa = edtPlaca.getText().toString().trim();

                if (TextUtils.isEmpty(placa)) {
                    Toast.makeText(inserir_placa.this, "Por favor, insira a placa.", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(inserir_placa.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_saida, null);

                // Find custom buttons from the inflated dialog view
                Button buttonYes = dialogView.findViewById(R.id.buttonYes);
                Button buttonNo = dialogView.findViewById(R.id.negative);

                builder.setView(dialogView);

                AlertDialog dialog = builder.create();

                // >>> ADIÇÃO: Torna o fundo da janela do diálogo transparente <<<
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }

                // Set click listeners for your custom buttons
                buttonYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        registrarSaida(placa);
                        dialog.dismiss(); // Dismiss the dialog after action
                    }
                });

                buttonNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss(); // Just dismiss the dialog
                    }
                });

                dialog.show();
            }
        });
    }

    private void registrarSaida(String placa) {
        String horaSaida = getDataHoraAtual();

        db.collection("veiculos")
                .document(placa)
                .update("saida", horaSaida)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(inserir_placa.this, "Saída registrada: " + horaSaida, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(inserir_placa.this, "Erro ao registrar saída: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private String getDataHoraAtual() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("pt", "BR"));
        return sdf.format(new Date());
    }
}
