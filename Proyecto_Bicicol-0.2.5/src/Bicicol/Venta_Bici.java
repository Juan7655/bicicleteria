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
public class Venta_Bici {

    private int IdVenBi;
    private int RefBici;
    private int IdVenta;
    private int Cantidad;

    @Override
    public String toString() {
        return IdVenBi + "," + RefBici + "," + IdVenta + "," + Cantidad;
    }

    public int getIdVenBi() {
        return IdVenBi;
    }

    public void setIdVenBi(int IdVenBi) {
        this.IdVenBi = IdVenBi;
    }

    public int getRefBici() {
        return RefBici;
    }

    public void setRefBici(int RefBici) {
        this.RefBici = RefBici;
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

    public Venta_Bici(int IdVenBi, int RefBici, int IdVenta, int Cantidad) {
        this.IdVenBi = IdVenBi;
        this.RefBici = RefBici;
        this.IdVenta = IdVenta;
        this.Cantidad = Cantidad;
    }

}
