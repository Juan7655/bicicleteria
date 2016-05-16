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
public class Venta_Bici {

    private int IdVenBic;
    private int IdVenta;
    private int RefBicicleta;
    private int Cantidad;

    @Override
    public String toString() {
        return IdVenBic + "," + IdVenta + "," + RefBicicleta + "," + Cantidad;
    }

    public int getIdComBic() {
        return IdVenBic;
    }

    public void setIdComBic(int IdComBic) {
        this.IdVenBic = IdComBic;
    }

    public int getIdCompra() {
        return IdVenta;
    }

    public void setIdCompra(int IdCompra) {
        this.IdVenta = IdCompra;
    }

    public int getRefBicicleta() {
        return RefBicicleta;
    }

    public void setRefBicicleta(int RefBicicleta) {
        this.RefBicicleta = RefBicicleta;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int Cantidad) {
        this.Cantidad = Cantidad;
    }

    public Venta_Bici(int IdVenBic, int IdVenta, int RefBicicleta, int Cantidad) {
        this.IdVenBic = IdVenBic;
        this.IdVenta = IdVenta;
        this.RefBicicleta = RefBicicleta;
        this.Cantidad = Cantidad;
    }

}
