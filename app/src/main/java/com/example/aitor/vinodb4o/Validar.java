package com.example.aitor.vinodb4o;

import android.os.Environment;

/**
 * Created by aitor on 22/11/2014.
 */
public class Validar {



    //valida los datos introducidos por el usuario
    public static boolean validarDatos(String nombre,String descri,String precio,String informacion){

        nombre=nombre.trim();
        descri=descri.trim();
        precio=precio.trim();
        informacion=informacion.trim();
        if(nombre.length()>0 && descri.length()>0 && precio.length()>0 && informacion.length()>0)
            return true;
        else
            return false;

    }


    //Comprueba si tenemos permisos para crear el archivo
    public static boolean isModificable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
