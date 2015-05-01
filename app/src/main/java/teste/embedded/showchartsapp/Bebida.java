package teste.embedded.showchartsapp;

/**
 * Created by marcelo on 28/04/15.
 */
public class Bebida {

    private String _id;
    private String bebida;
    private Integer consumo;


    public String getBebida() {
        return bebida;
    }

    public void setBebida(String bebida) {
        this.bebida = bebida;
    }

    public Integer getConsumo() {
        return consumo;
    }

    public void setConsumo(Integer consumo) {
        this.consumo = consumo;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
