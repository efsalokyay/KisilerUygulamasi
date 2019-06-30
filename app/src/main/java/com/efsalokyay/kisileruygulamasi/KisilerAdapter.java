package com.efsalokyay.kisileruygulamasi;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class KisilerAdapter extends RecyclerView.Adapter<KisilerAdapter.CardTasarimTutucu> {

    private Context mContext;
    private List<Kisiler> kisilerListe;

    private Veritabani vt;

    public KisilerAdapter(Context mContext, List<Kisiler> kisilerListe, Veritabani vt) {
        this.mContext = mContext;
        this.kisilerListe = kisilerListe;
        this.vt = vt;
    }

    @NonNull
    @Override
    public CardTasarimTutucu onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.kisi_card_tasarim, viewGroup, false);

        return new CardTasarimTutucu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardTasarimTutucu cardTasarimTutucu, int i) {

        final Kisiler kisi = kisilerListe.get(i);
        cardTasarimTutucu.kisi_bilgi_text.setText(kisi.getKisi_ad() + " - " + kisi.getKisi_tel());

        cardTasarimTutucu.nokta_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(mContext, cardTasarimTutucu.nokta_image_view);
                popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.action_sil:
                                Snackbar.make(cardTasarimTutucu.nokta_image_view, "Kişi silinsin mi ?", Snackbar.LENGTH_SHORT)
                                        .setAction("Evet", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                new KisilerDao().kisiSil(vt, kisi.getKisi_id());

                                                kisilerListe = new KisilerDao().tumKisiler(vt);
                                                notifyDataSetChanged();
                                            }
                                        })
                                        .show();
                                return true;
                            case R.id.action_duzenle:
                                alertGoster(kisi);
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return kisilerListe.size();
    }

    public void alertGoster(final Kisiler kisi) {

        LayoutInflater layout = LayoutInflater.from(mContext);
        View tasarim = layout.inflate(R.layout.alert_tasarim, null);

        final EditText ad_text = tasarim.findViewById(R.id.kisi_ad_text);
        final EditText tel_text = tasarim.findViewById(R.id.kisi_tel_text);

        ad_text.setText(kisi.getKisi_ad());
        tel_text.setText(kisi.getKisi_tel());

        AlertDialog.Builder ad = new AlertDialog.Builder(mContext);
        ad.setTitle("Kişi Düzenle");
        ad.setView(tasarim);
        ad.setPositiveButton("Düzenle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String kisi_ad = ad_text.getText().toString().trim();
                String kisi_tel = tel_text.getText().toString().trim();

                new KisilerDao().kisiGuncelle(vt, kisi.getKisi_id(), kisi_ad, kisi_tel);

                kisilerListe = new KisilerDao().tumKisiler(vt);

                notifyDataSetChanged();

            }
        });
        ad.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        ad.create().show();

    }

    public class CardTasarimTutucu extends RecyclerView.ViewHolder {

        private TextView kisi_bilgi_text;
        private ImageView nokta_image_view;

        public CardTasarimTutucu(@NonNull View itemView) {
            super(itemView);

            kisi_bilgi_text = itemView.findViewById(R.id.kisi_bilgi_text);
            nokta_image_view = itemView.findViewById(R.id.nokta_image_view);
        }
    }
}
