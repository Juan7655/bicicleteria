/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bicicol;

/**
 *
 * @author Natalia
 */
public class Venta_Comp {
    int IdVenComp;
    int IdVenta;
    int RefComponente;
    int Cantidad;

    @Override
    public String toString() {
        return IdVenComp + "," + RefComponente + "," + IdVenta + "," + Cantidad;
    }

    public int getIdVenComp() {
        return IdVenComp;
    }

    public void setIdVenComp(int IdVenComp) {
        this.IdVenComp = IdVenComp;
    }

    public int getIdVenta() {
        return IdVenta;
    }

    public void setIdVenta(int IdVenta) {
        this.IdVenta = IdVenta;
    }

    public int getRefComponente() {
        return RefComponente;
    }

    public void setRefComponente(int RefComponente) {
        this.RefComponente = RefComponente;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int Cantidad) {
        this.Cantidad = Cantidad;
    }

    public Venta_Comp(int IdVenComp, int IdVenta, int RefComponente, int Cantidad) {
        this.IdVenComp = IdVenComp;
        this.IdVenta = IdVenta;
        this.RefComponente = RefComponente;
        this.Cantidad = Cantidad;
    }
    
}
