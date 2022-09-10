package com.example.viaje;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText jetcodigo,jetciudad,jetcantidad,jetvalor;

    ClsOpenHelper admin=new ClsOpenHelper(this,"viaje.db",null,1);

    String codigo,ciudad,cantidad,valor;
    long resp;
    int sw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        jetcodigo=findViewById(R.id.etcodigo);
        jetciudad=findViewById(R.id.etciudadd);
        jetcantidad=findViewById(R.id.etcantidad);
        jetvalor=findViewById(R.id.etvalor);
    }

    public void adicionar(View view){
        codigo=jetcodigo.getText().toString();
        ciudad=jetciudad.getText().toString();
        cantidad=jetcantidad.getText().toString();
        valor=jetvalor.getText().toString();
        if (codigo.isEmpty() || ciudad.isEmpty() ||
                cantidad.isEmpty() || valor.isEmpty()){
            Toast.makeText(this,"todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();

    }
        else{
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues registro=new ContentValues();
            registro.put("codigo",Integer.parseInt(codigo));
            registro.put("ciudad",ciudad);
            registro.put("cantidad",Integer.parseInt(cantidad));
            registro.put("valor",Integer.parseInt(valor));
            if(sw==0)
                resp=db.insert("TblViaje",null,registro);
            else{
                resp=db.update("TblViaje",registro,"codigo'" +codigo + "'",null);
                sw=0;
            }
            if (resp > 0){
                Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }
            else
                Toast.makeText(this, "Error guardando registro", Toast.LENGTH_SHORT).show();
            db.close();
        }
    }
    public void Consultar(View view){
        codigo=jetcodigo.getText().toString();
        if (codigo.isEmpty()){
            Toast.makeText(this, "el codigo es requerida", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
        else{
            SQLiteDatabase db=admin.getReadableDatabase();
            Cursor fila=db.rawQuery("select * from TblViaje where codigo='" + codigo + "'",null);
            if (fila.moveToNext()){
                sw=1;
                jetciudad.setText(fila.getString(1));
                jetcantidad.setText(fila.getString(2));
                jetvalor.setText(fila.getString(3));

            }
            else
                Toast.makeText(this, "viaje no registrado", Toast.LENGTH_SHORT).show();
            db.close();
        }
    }

    public void Anular (View view){
        if(sw==0){
            Toast.makeText(this,"primero debe consultar",Toast.LENGTH_SHORT).show();
            jetciudad.requestFocus();
        }
        else{
            SQLiteDatabase db=admin.getReadableDatabase();
            ContentValues registro=new ContentValues();
            resp=db.update("TblViaje",registro,"codigo='" + codigo+"'",null);
            if(resp>0){
                Toast.makeText(this,"registro anulado",Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }
            else
                Toast.makeText(this,"Error anulado registrado",Toast.LENGTH_SHORT).show();
            db.close();
        }
    }
    private void Limpiar_campos(){
        jetcodigo.setText("");
        jetvalor.setText("");
        jetcantidad.setText("");
        jetciudad.setText("");

}
}