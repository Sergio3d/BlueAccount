package com.cumn.blueaccount.models;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.cumn.blueaccount.Seleccionado;

import java.util.Date;

@Entity(tableName = TransacEntity.TABLA)
public class TransacEntity {
    static public final String TABLA = "usuarios";

    private String DESCRIP;
    private String CANTIDAD;
    private float aux;
    private String FECHA;
    private String USER;
    private String ID;

    public TransacEntity(String ID, String DESCRIP, String CANTIDAD, String FECHA, String USER) {
        this.ID = ID;
        this.DESCRIP = DESCRIP;
        this.CANTIDAD = CANTIDAD;
        this.FECHA = FECHA;
        this.USER = USER;
        this.aux = calculateAux();
    }

    public TransacEntity(String DESCRIP, String CANTIDAD, String FECHA, String USER) {
        this.DESCRIP = DESCRIP;
        this.CANTIDAD = CANTIDAD;
        this.FECHA = FECHA;
        this.USER = USER;
        this.aux = calculateAux();
    }

    private float calculateAux() {
        if (CANTIDAD != null) {
            return Float.parseFloat(CANTIDAD) * Seleccionado.GlobalVariables.getValor();
        } else {
            return 0;
        }
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUSER() {
        return USER;
    }

    public void setUSER(String USER) {
        this.USER = USER;
    }

    public String getDESCRIP() {
        return DESCRIP;
    }

    public void setDESCRIP(String DESCRIP) {
        this.DESCRIP = DESCRIP;
    }

    public String getCANTIDAD() {
        return Float.toString(aux);
    }

    public void setCANTIDAD(String CANTIDAD) {
        this.CANTIDAD = CANTIDAD;
        this.aux = calculateAux();
    }

    public String getFECHA() {
        return FECHA;
    }

    public void setFECHA(String FECHA) {
        this.FECHA = FECHA;
    }
}
