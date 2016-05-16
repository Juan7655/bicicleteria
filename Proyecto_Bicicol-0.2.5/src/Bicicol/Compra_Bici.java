    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bicicol;

public class Compra_Bici {

    private int IdComBic;
    private int IdCompra;
    private int RefBicicleta;
    private int Cantidad;

    @Override
    public String toString() {
        return IdComBic + "," + IdCompra + "," + RefBicicleta + "," + Cantidad;
    }

    public int getIdComBic() {
        return IdComBic;
    }

    public void setIdComBic(int IdComBic) {
        this.IdComBic = IdComBic;
    }

    public int getIdCompra() {
        return IdCompra;
    }

    public void setIdCompra(int IdCompra) {
        this.IdCompra = IdCompra;
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

    public Compra_Bici(int IdComBic, int IdCompra, int RefBicicleta, int Cantidad) {
        this.IdComBic = IdComBic;
        this.IdCompra = IdCompra;
        this.RefBicicleta = RefBicicleta;
        this.Cantidad = Cantidad;
    }

}
