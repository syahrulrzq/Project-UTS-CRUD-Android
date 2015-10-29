package com.example.revze.crudandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.revze.crudandroid.domain.Siswa;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Siswa siswa = new Siswa();
    DBAdapter dbAdapter = null;

    EditText txtNama, txtKelas;
    ListView listSiswa;
    Button btnSimpan;
    Siswa editSiswa;

    private static final String OPTION[] = {"Edit", "Delete"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbAdapter = new DBAdapter(getApplicationContext());

        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        txtNama = (EditText) findViewById(R.id.txtNama);
        txtKelas = (EditText) findViewById(R.id.txtKelas);
        listSiswa = (ListView) findViewById(R.id.listSiswa);

        listSiswa.setOnItemClickListener(new ListItemClick());
        listSiswa.setAdapter(new ListSiswaAdapter(dbAdapter
                .getAllSiswa()));
    }

    public class ListItemClick implements AdapterView
            .OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view,
                                int position, long id) {
            final Siswa siswa = (Siswa) listSiswa
                    .getItemAtPosition(position);
            showOptionDialog(siswa);
        }
    }

    public void showOptionDialog(Siswa siswa) {
        final Siswa mSiswa;
        mSiswa = siswa;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Test")
                .setItems(OPTION, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int post) {
                        switch (post) {
                            case 0:
                                editSiswa = mSiswa;
                                txtNama.setText(mSiswa.getNama());
                                txtKelas.setText(mSiswa.getKelas());
                                btnSimpan.setText("Edit");
                                break;
                            case 1:
                                dbAdapter.delete(mSiswa);
                                listSiswa.setAdapter
                                        (new ListSiswaAdapter(dbAdapter.getAllSiswa()));
                                break;
                            default:
                                break;
                        }
                    }
                });
        final Dialog d = builder.create();
        d.show();
    }

    public void save(View v) {
        if(txtNama.getText().length() == 0 ||
                txtKelas.getText().length() == 0) {
            txtNama.setError("Cannot Empty");
            txtKelas.setError("Cannot Empty");
        } else {
            if(btnSimpan.getText().equals("Edit")) {
                editSiswa.setNama(txtNama.getText().toString());
                editSiswa.setKelas(txtKelas.getText().toString());
                dbAdapter.updateSiswa(editSiswa);
                btnSimpan.setText("Simpan");
            } else {
                siswa.setNama(txtNama.getText().toString());
                siswa.setKelas(txtKelas.getText().toString());
                dbAdapter.save(siswa);
            }
            txtNama.setText("");
            txtKelas.setText("");
        }
        listSiswa.setAdapter(new ListSiswaAdapter(dbAdapter
                .getAllSiswa()));
    }

    public class ListSiswaAdapter extends BaseAdapter {
        private List<Siswa> listSiswa;

        public ListSiswaAdapter (List<Siswa> listSiswa) {
            this.listSiswa = listSiswa;
        }

        @Override
        public int getCount() {
            return this.listSiswa.size();
        }

        @Override
        public Siswa getItem(int position) {
            return this.listSiswa.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater
                        .from(getApplicationContext())
                        .inflate(R.layout.list_layout, parent, false);
            }
            final Siswa siswa = getItem(position);
            if(siswa != null) {
                TextView labelNama = (TextView) convertView
                        .findViewById(R.id.labelNama);
                labelNama.setText(siswa.getNama());
                TextView labelKelas = (TextView) convertView
                        .findViewById(R.id.labelKelas);
                labelKelas.setText(siswa.getKelas());
            }
            return convertView;
        }
    }
}
