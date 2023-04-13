package com.cumn.blueaccount.models;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = TransacEntity.TABLA)
public class TransacEntity {
    static public final String TABLA = "usuarios";

    @PrimaryKey()
    protected Date IDFECHA;

    protected Boolean GAS_ING;
    protected String DESCRIP;
    protected String ETIQUETA;
    protected Float CANTIDAD;
    protected Date FECHA;

    public TransacEntity(String nombre, String password, float rol, String IDFECHA, Boolean GAS_ING, String DESCRIP, String ETIQUETA, Float CANTIDAD, Date FECHA) {
        this.IDFECHA = new Date();
        this.GAS_ING = GAS_ING;
        this.DESCRIP = DESCRIP;
        this.ETIQUETA = ETIQUETA;
        this.CANTIDAD = CANTIDAD;
        this.FECHA = FECHA;
    }

    public Date getIDFECHA() {
        return IDFECHA;
    }

    public Boolean getGAS_ING() {
        return GAS_ING;
    }

    public void setGAS_ING(Boolean GAS_ING) {
        this.GAS_ING = GAS_ING;
    }

    public String getDESCRIP() {
        return DESCRIP;
    }

    public void setDESCRIP(String DESCRIP) {
        this.DESCRIP = DESCRIP;
    }

    public String getETIQUETA() {
        return ETIQUETA;
    }

    public void setETIQUETA(String ETIQUETA) {
        this.ETIQUETA = ETIQUETA;
    }

    public Float getCANTIDAD() {
        return CANTIDAD;
    }

    public void setCANTIDAD(Float CANTIDAD) {
        this.CANTIDAD = CANTIDAD;
    }

    public Date getFECHA() {
        return FECHA;
    }

    public void setFECHA(Date FECHA) {
        this.FECHA = FECHA;
    }
}