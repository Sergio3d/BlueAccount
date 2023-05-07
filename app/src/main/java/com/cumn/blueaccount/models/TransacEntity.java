package com.cumn.blueaccount.models;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = TransacEntity.TABLA)
public class TransacEntity {
    static public final String TABLA = "usuarios";

    protected String DESCRIP;
    protected String CANTIDAD;
    protected String FECHA;
    protected String USER;

    public TransacEntity(String DESCRIP, String CANTIDAD, String FECHA, String USER) {
        this.DESCRIP = DESCRIP;
        this.CANTIDAD = CANTIDAD;
        this.FECHA = FECHA;
        this.USER = USER;
    }

    public String getUSER() { return USER; }

    public void setUSER(String USER) { this.USER = USER; }

    public String getDESCRIP() {
        return DESCRIP;
    }

    public void setDESCRIP(String DESCRIP) {
        this.DESCRIP = DESCRIP;
    }

    public String getCANTIDAD() {
        return CANTIDAD;
    }

    public void setCANTIDAD(String CANTIDAD) {
        this.CANTIDAD = CANTIDAD;
    }

    public String getFECHA() {
        return FECHA;
    }

    public void setFECHA(String FECHA) {
        this.FECHA = FECHA;
    }
}