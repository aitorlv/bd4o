package com.example.aitor.vinodb4o;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aitor on 05/10/2014.
 */
public class Adapter extends ArrayAdapter<Vino> {

        private Context contexto;
        private ArrayList<Vino> lista;
        private int recurso;
        private static LayoutInflater inflater;

    public static class ViewHolder{
        public TextView tvnombre,tvdescri,tvprecio;
        public int posicion,idradiobutton;
        public ImageView img;
        public String informacion;
    }

    public Adapter(Context context, int resource, ArrayList<Vino> objects) {
        super(context, resource, objects);
        this.contexto=context;
        this.lista=objects;
        this.recurso=resource;
        this.inflater=(LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       //Log.v("LOG",lista.size()+"");
       ViewHolder vh=null;
        if (convertView==null){
            convertView=inflater.inflate(recurso,null);
            vh=new ViewHolder();
            vh.img=(ImageView)convertView.findViewById(R.id.Image);
            vh.tvnombre=(TextView) convertView.findViewById(R.id.Nombre);
            vh.tvdescri=(TextView) convertView.findViewById(R.id.descri);
            vh.tvprecio=(TextView) convertView.findViewById(R.id.precio);

            convertView.setTag(vh);
        }else{

           vh=(ViewHolder)convertView.getTag();
        }

        Vino obj= (Vino)getItem(position);
        vh.posicion=position;
        if(obj.getImg().compareTo("tinto")==0){
            vh.img.setImageDrawable(contexto.getResources().getDrawable(R.drawable.tinto));
        }else if(obj.getImg().compareTo("blanco")==0){
            vh.img.setImageDrawable(contexto.getResources().getDrawable(R.drawable.blanco));
        }else if(obj.getImg().compareTo("rosado")==0){
            vh.img.setImageDrawable(contexto.getResources().getDrawable(R.drawable.rosado));
        }else{
            vh.img.setImageDrawable(contexto.getResources().getDrawable(R.drawable.vacio));
        }
        vh.tvnombre.setText(obj.getNombre()+"");
        vh.tvdescri.setText(obj.getDescri()+"");
        vh.tvprecio.setText(obj.getPrecio()+"");
        vh.idradiobutton=obj.getIdradiobuton();
        vh.informacion=obj.getInformacion();
        return convertView;
    }
}
