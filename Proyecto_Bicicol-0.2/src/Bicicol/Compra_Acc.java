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
public class Compra_Acc {
    int IdComAcc;
    int IdCompra;
    int RefAccesorio;
    int Cantidad;

    @Override
    public String toString() {
        return IdComAcc + "," + IdCompra + "," + RefAccesorio + "," + Cantidad;
    }

    public int getIdComAcc() {
        return IdComAcc;
    }

    public void setIdComAcc(int IdComAcc) {
        this.IdComAcc = IdComAcc;
    }

    public int getIdCompra() {
        return IdCompra;
    }

    public void setIdCompra(int IdCompra) {
        this.IdCompra = IdCompra;
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

    public Compra_Acc(int IdComAcc, int IdCompra, int RefAccesorio, int Cantidad) {
        this.IdComAcc = IdComAcc;
        this.IdCompra = IdCompra;
        this.RefAccesorio = RefAccesorio;
        this.Cantidad = Cantidad;
    }
}
