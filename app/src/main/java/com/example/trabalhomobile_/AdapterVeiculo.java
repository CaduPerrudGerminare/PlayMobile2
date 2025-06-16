package com.example.trabalhomobile_;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterVeiculo extends RecyclerView.Adapter<AdapterVeiculo.ViewHolder> {

    private List<Veiculo> lista;

    // Construtor padrão que cria dados fictícios para teste
    public AdapterVeiculo() {
        this.lista = new ArrayList<>();
    }

    // Construtor que recebe lista real
    public AdapterVeiculo(List<Veiculo> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public AdapterVeiculo.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_carro, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterVeiculo.ViewHolder holder, int position) {
        Veiculo veiculo = lista.get(position);

        holder.textPlaca.setText("Placa: " + veiculo.getPlaca());
        holder.textEntrada.setText("Entrada: " + veiculo.getEntrada());
        holder.textSaida.setText("Saída: " + veiculo.getSaida());

        holder.itemView.setOnClickListener(v -> {
            if (!veiculo.getSaida().isEmpty()) {
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_exclusao, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                Button btnSim = dialogView.findViewById(R.id.buttonYes);
                Button btnNao = dialogView.findViewById(R.id.negative);

                btnSim.setOnClickListener(view -> {
                    Database db = new Database();
                    db.deletarVeiculo(veiculo.getPlaca(), task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(v.getContext(), "Veículo excluído com sucesso", Toast.LENGTH_SHORT).show();
                            lista.remove(veiculo);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(v.getContext(), "Erro: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    dialog.dismiss();
                });

                btnNao.setOnClickListener(view -> dialog.dismiss());

                if (dialog.getWindow() != null)
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textPlaca, textEntrada, textSaida;
        ConstraintLayout fundo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textPlaca = itemView.findViewById(R.id.textPlaca);
            textEntrada = itemView.findViewById(R.id.textEntrada);
            textSaida = itemView.findViewById(R.id.textSaida);
            fundo = itemView.findViewById(R.id.fundo);
        }
    }
}
