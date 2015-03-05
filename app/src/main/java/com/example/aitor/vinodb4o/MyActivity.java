package com.example.aitor.vinodb4o;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MyActivity extends Activity {
    private static final int MODIFICAR=1;
    private ArrayList<Vino> valores = new ArrayList<Vino>();
    private Adapter ad;
    private ListView lista;
    private int indicemod;
    private ObjectContainer bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bd= Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), getExternalFilesDir(null) + "/bd.db4o");
        inicializar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //leerArchivo();
    }


    @Override
    protected void onStop() {
        super.onPause();
        bd.close();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
      if (id == R.id.action_insertar) {


          //Vino vino=new Vino("nombre","descsri","precio","info","img",1);
          insertarItem();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextual, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();


        Object o = info.targetView.getTag();
        //Vino vh=(Vino)o;
        Adapter.ViewHolder vh = (Adapter.ViewHolder) o;


        if (id == R.id.action_modificar) {
            modificarItem(vh);

        } else if (id == R.id.action_borrar) {
           borrarItem(vh);
        }
        return super.onContextItemSelected(item);

    }


    /*--------------------------------------------mis metodos----------------------------------------------------*/


    //inicializamos el list view y lo mostramos
    public void inicializar() {
        Vino vino = new Vino(null,null,null,null,null,0);
        List<Vino> todos = bd.queryByExample(vino);
        for(Vino v: todos){
           valores.add(v);
        }
        Collections.sort(valores);
        Log.v("tamano", valores.size() + "");
        lista = (ListView) findViewById(R.id.lista);
        ad = new Adapter(this, R.layout.fila, valores);
        lista.setAdapter(ad);
        ad.notifyDataSetChanged();
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     mostrarInformacion(position);
                }
        });
        registerForContextMenu(lista);
    }


    //metodo para mostrar la informacion al hacer click en un item

    public void mostrarInformacion(int posicion){
        ImageView img;
        TextView tv1,tv2,info;
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        LayoutInflater inflater= LayoutInflater.from(this);
        final View vista=inflater.inflate(R.layout.mensaje,null);
        img=(ImageView)vista.findViewById(R.id.imagenmen);
        tv1=(TextView)vista.findViewById(R.id.nombremen);
        tv2=(TextView)vista.findViewById(R.id.preciomen);
        info=(TextView)vista.findViewById(R.id.infomen);
        if(valores.get(posicion).getImg().compareTo("tinto")==0) {
            img.setImageDrawable(getResources().getDrawable(R.drawable.tinto));
        }else if(valores.get(posicion).getImg().compareTo("rosado")==0){
            img.setImageDrawable(getResources().getDrawable(R.drawable.rosado));
        }else if(valores.get(posicion).getImg().compareTo("blanco")==0){
            img.setImageDrawable(getResources().getDrawable(R.drawable.blanco));
        }else{
            img.setImageDrawable(getResources().getDrawable(R.drawable.vacio));
        }
        tv1.setText(valores.get(posicion).getNombre());
        tv2.setText(valores.get(posicion).getPrecio());
        info.setText(valores.get(posicion).getInformacion());
        alert.setView(vista);

        alert.setNegativeButton("Cerrar",null);
        alert.show();
    }


    //metodo para insnertar valores en el listview
    public void insertarItem(){

        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        LayoutInflater inflater= LayoutInflater.from(this);
        final View view=inflater.inflate(R.layout.dialogoinsertar, null);
        final EditText etnombre=(EditText)view.findViewById(R.id.etnombre);
        final EditText etdescri=(EditText)view.findViewById(R.id.etdescri);
        final EditText etprecio=(EditText)view.findViewById(R.id.etprecio);
        final EditText etinformacion=(EditText)view.findViewById(R.id.etinformacion);
        final RadioGroup rb=(RadioGroup)view.findViewById(R.id.rb);
        alert.setView(view);
        alert.setCancelable(false);

        alert.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombre,descri,precio,informacion;
                //Drawable foto;
                Vino obj;
                nombre=etnombre.getText().toString();
                descri=etdescri.getText().toString();
                precio=etprecio.getText().toString()+"€";
                informacion=etinformacion.getText().toString();
                int idseleccionado = rb.getCheckedRadioButtonId();
               if(Validar.validarDatos(nombre,descri,precio,informacion)) {
                   if (idseleccionado == R.id.rb1) {
                       String foto="tinto";
                       obj = new Vino(nombre, descri, precio,informacion, foto,R.id.rb1);

                   } else if (idseleccionado == R.id.rb2) {
                       String foto="rosado";
                       obj = new Vino(nombre, descri, precio,informacion, foto,R.id.rb2);
                   } else if (idseleccionado == R.id.rb3) {
                       String foto="blanco";
                       obj = new Vino(nombre, descri, precio,informacion, foto,R.id.rb3);
                   } else {
                       String foto="vacio";
                       obj = new Vino(nombre, descri, precio,informacion, foto,idseleccionado);
                   }
                   if(!valores.contains(obj)){
                       bd.store(obj);
                       bd.commit();
                     valores.add(obj);
                     Collections.sort(valores);
                     ad.notifyDataSetChanged();
                     tostada("Datos insertados");
                   }else{
                     tostada("Ese vino ya existe");
                   }

               }else{
                    tostada("Datos no validos ");
               }

            }
        });
        alert.setNegativeButton("Cancelar",null);
        alert.show();

    }

     // borrar item del listview
    public void borrarItem(final Adapter.ViewHolder vh){
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        LayoutInflater inflater= LayoutInflater.from(this);
        final View view=inflater.inflate(R.layout.dialogoborrar,null);
        alert.setMessage("¿Borrar " + vh.tvnombre.getText() + " de la lista?");
        alert.setView(view);
        alert.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Vino v=valores.get(vh.posicion);
                ObjectSet objeto =bd.queryByExample(v);
                if(objeto.hasNext()) {
                    Vino vino = (Vino) objeto.next();
                    bd.delete(vino);
                    valores.remove(vh.posicion);
                    ad.notifyDataSetChanged();
                    tostada("Datos borrados");
                }else {
                    tostada("Datos no encontrados");
                }
            }
        });
        alert.setNegativeButton("Cancelar",null);
        alert.show();
    }


    public void modificarItem(Adapter.ViewHolder vh){

        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        LayoutInflater inflater= LayoutInflater.from(this);
        Vino v=valores.get(vh.posicion);
        ObjectSet objeto =bd.queryByExample(v);
        final Vino vino=(Vino)objeto.next();
        final View view=inflater.inflate(R.layout.dialogoinsertar, null);
        final EditText etnombre=(EditText)view.findViewById(R.id.etnombre);
        final EditText etdescri=(EditText)view.findViewById(R.id.etdescri);
        final EditText etprecio=(EditText)view.findViewById(R.id.etprecio);
        final EditText etinformacion=(EditText)view.findViewById(R.id.etinformacion);
        final RadioGroup rb=(RadioGroup)view.findViewById(R.id.rb);
        if (vino.getIdradiobuton() == R.id.rb1) {
            rb.check(R.id.rb1);
        } else if (vino.getIdradiobuton() == R.id.rb2) {
            rb.check(R.id.rb2);
        } else if (vino.getIdradiobuton() == R.id.rb3) {
            rb.check(R.id.rb3);
        }
        etnombre.setText(vino.getNombre());
        etdescri.setText(vino.getDescri());
        etprecio.setText(vino.getPrecio());
        etinformacion.setText(vino.getInformacion());
        etinformacion.setText(vino.getInformacion());
        alert.setView(view);
        alert.setCancelable(false);

        alert.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String foto="vacio";
                if (rb.getCheckedRadioButtonId() == R.id.rb1) {
                    foto="tinto";
                } else if (rb.getCheckedRadioButtonId() == R.id.rb2) {
                    foto="rosado";
                } else if (rb.getCheckedRadioButtonId()== R.id.rb3) {
                    foto="blanco";
                }
                vino.setNombre(etnombre.getText().toString());
                vino.setDescri(etdescri.getText().toString());
                vino.setPrecio(etprecio.getText().toString());
                vino.setInformacion(etinformacion.getText().toString());
                vino.setIdradiobuton(rb.getCheckedRadioButtonId());
                vino.setImg(foto);
                        bd.store(vino);
                        bd.commit();
                        valores.clear();
                        inicializar();
                        tostada("Datos insertados");
                }

        });
        alert.setNegativeButton("Cancelar",null);
        alert.show();

    }




    //metodo usado para sacar mensajes
    public void tostada(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
