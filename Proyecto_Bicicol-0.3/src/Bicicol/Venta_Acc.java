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
public class Venta_Acc {

    int IdVenAcc;
    int IdVenta;
    int RefAccesorio;
    int Cantidad;

    @Override
    public String toString() {
        return IdVenAcc + "," + RefAccesorio + ","+ IdVenta + "," + Cantidad;
    }

    public int getIdVenAcc() {
        return IdVenAcc;
    }

    public void setIdVenAcc(int IdVenAcc) {
        this.IdVenAcc = IdVenAcc;
    }

    public int getIdVenta() {
        return IdVenta;
    }

    public void setIdVenta(int IdVenta) {
        this.IdVenta = IdVenta;
    }

    public int getRefAccesorio() {
        return RefAccesorio;
    }

    public void setRefAccesorio(int RefAccesorio) {
        this.RefAccesorio = RefAccesorio;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int Cantidad) {
        this.Cantidad = Cantidad;
    }

    public Venta_Acc(int IdVenAcc, int IdVenta, int RefAccesorio, int Cantidad) {
        this.IdVenAcc = IdVenAcc;
        this.IdVenta = IdVenta;
        this.RefAccesorio = RefAccesorio;
        this.Cantidad = Cantidad;
    }
}
