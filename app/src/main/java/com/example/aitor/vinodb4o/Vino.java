package com.example.aitor.vinodb4o;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.Collator;
import java.util.Locale;

/**
 * Created by aitor on 06/10/2014.
 */
public class Vino<ArrayList>  implements Comparable<Vino>,Parcelable {

        private String nombre,descri, precio,informacion;
        private String img;
        private int idradiobuton;
    public Vino() {
        this.nombre="";
        this.descri="";
        this.precio="";
        this.img="";
        this.informacion="";
        this.idradiobuton=0;
    }
    public Vino(String nombre, String descri, String precio, String informacion, String img, int idradiobutton) {
        this.nombre=nombre;
        this.descri=descri;
        this.precio=precio;
        this.img=img;
        this.informacion=informacion;
        this.idradiobuton=idradiobutton;
    }

    public String getImg() {

        return img;
    }


    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIdradiobuton() {
        return idradiobuton;
    }

    public void setIdradiobuton(int idradiobuton) {
        this.idradiobuton = idradiobuton;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public int compareTo(Vino vino) {
            int ct1 = this.nombre.compareTo(vino.nombre);
            if(ct1!=0){
                Collator coll = Collator.getInstance(Locale.getDefault());
                ct1 = coll.compare(this.nombre, vino.nombre);
            }
            return ct1;
        }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.nombre);
        parcel.writeString(this.descri);
        parcel.writeString(this.precio);
        parcel.writeString(this.img);
        parcel.writeString(this.informacion);
        parcel.writeInt(this.idradiobuton);

    }

    public static final Parcelable.Creator<Vino> CREATOR =new Parcelable.Creator<Vino>(){
        @Override
        public Vino createFromParcel(Parcel p) {

            String nombre=p.readString();
            String descri=p.readString();
            String precio=p.readString();
            String img=p.readString();
            String informacion=p.readString();
            int idradiobutton=p.readInt();

            return new Vino(nombre,descri,precio,informacion,img,idradiobutton);
        }

        @Override
        public Vino[] newArray(int size) {
            return new Vino[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vino)) return false;

        Vino vino = (Vino) o;

        if (idradiobuton != vino.idradiobuton) return false;
        if (descri != null ? !descri.equals(vino.descri) : vino.descri != null) return false;
        if (img != null ? !img.equals(vino.img) : vino.img != null) return false;
        if (informacion != null ? !informacion.equals(vino.informacion) : vino.informacion != null)
            return false;
        if (nombre != null ? !nombre.equals(vino.nombre) : vino.nombre != null) return false;
        if (precio != null ? !precio.equals(vino.precio) : vino.precio != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nombre != null ? nombre.hashCode() : 0;
        result = 31 * result + (descri != null ? descri.hashCode() : 0);
        result = 31 * result + (precio != null ? precio.hashCode() : 0);
        result = 31 * result + (informacion != null ? informacion.hashCode() : 0);
        result = 31 * result + (img != null ? img.hashCode() : 0);
        result = 31 * result + idradiobuton;
        return result;
    }
}
