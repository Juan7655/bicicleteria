/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bicicol;


public class Compra_Comp {
 
    int IdComComp;
    int IdCompra;
    int RefComponente;
    int Cantidad;

    @Override
    public String toString() {
        return IdComComp + "," + IdCompra + "," + RefComponente + "," + Cantidad;
    }

    public int getIdComComp() {
        return IdComComp;
    }

    public void setIdComComp(int IdComComp) {
        this.IdComComp = IdComComp;
    }

    public int getIdCompra() {
        return IdCompra;
    }

    public void setIdCompra(int IdCompra) {
        this.IdCompra = IdCompra;
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

    public Compra_Comp(int IdComComp, int IdCompra, int RefComponente, int Cantidad) {
        this.IdComComp = IdComComp;
        this.IdCompra = IdCompra;
        this.RefComponente = RefComponente;
        this.Cantidad = Cantidad;
    }
}
