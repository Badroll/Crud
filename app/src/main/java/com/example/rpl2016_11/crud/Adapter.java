package com.example.rpl2016_11.crud;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rpl2016_11.crud.model.Siswa;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context context;
    private List<Siswa> siswa;
   // private ItemClick clickMee;


    public Adapter(Context context, List<Siswa> siswa/*, ItemClick clickMee*/) {
        this.context = context;
        this.siswa = siswa;
       // this.clickMee = clickMee;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id = itemView.findViewById(R.id.tvId);
        TextView nama = itemView.findViewById(R.id.tvNama);
        TextView jk = itemView.findViewById(R.id.tvJK);
        TextView jurusan = itemView.findViewById(R.id.tvjurusan);
        TextView pasword = itemView.findViewById(R.id.tvPas);



        public ViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickMee.onClick(itemView, getAdapterPosition());
                }
            }); */

        }

        @Override
        public void onClick(View view) {
            String Id = id.getText().toString();
            String Nama = nama.getText().toString();
            String Jk = jk.getText().toString();
            String Jurusan = jurusan.getText().toString();
            String Pasword = pasword.getText().toString();

            Intent i = new Intent(context,UpdateActivity.class);
            i.putExtra("id" ,Id);
            i.putExtra("nama" ,Nama);
            i.putExtra("jk" ,Jk);
            i.putExtra("jurusan" ,Jurusan);
            i.putExtra("pasword" ,Pasword);
            Log.e("id", "onClick: " + id );
            context.startActivity(i);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.blueprint, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Siswa ssw = siswa.get(position);
        holder.id.setText(ssw.getId());
        holder.nama.setText(ssw.getNama());
        holder.jurusan.setText(ssw.getJurusan());
        holder.jk.setText(ssw.getJk());
        holder.pasword.setText(ssw.getPasword());
    }

    @Override
    public int getItemCount() {
        return siswa.size();

    }
}