/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bicicol;

/**
 *
 * @author OscarLopez
 */
public class Venta_Comp {

    private int IdVenCom;
    private int RefComponente;
    private int IdVenta;

    public Venta_Comp(int IdVenCom, int RefComponente, int IdVenta, int Cantidad) {
        this.IdVenCom = IdVenCom;
        this.RefComponente = RefComponente;
        this.IdVenta = IdVenta;
        this.Cantidad = Cantidad;
    }

      @Override
    public String toString() {
        return IdVenCom + "," + RefComponente + "," + IdVenta + "," + Cantidad;
    }

    
    public int getIdVenCom() {
        return IdVenCom;
    }

    public void setIdVenCom(int IdVenCom) {
        this.IdVenCom = IdVenCom;
    }

    public int getRefComponente() {
        return RefComponente;
    }

    public void setRefComponente(int RefComponente) {
        this.RefComponente = RefComponente;
    }

    public int getIdVenta() {
        return IdVenta;
    }

    public void setIdVenta(int IdVenta) {
        this.IdVenta = IdVenta;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int Cantidad) {
        this.Cantidad = Cantidad;
    }
    private int Cantidad;

}
