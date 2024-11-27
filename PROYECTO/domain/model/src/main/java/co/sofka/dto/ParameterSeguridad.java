package co.sofka.dto;

public class ParameterSeguridad {

    private  String llaveSimetrica;

    private  String vectorInicializacion;

    public ParameterSeguridad(String llaveSimetrica, String vectorInicializacion) {
        this.llaveSimetrica = llaveSimetrica;
        this.vectorInicializacion = vectorInicializacion;
    }

    public ParameterSeguridad() {
    }

    public String getLlaveSimetrica() {
        return llaveSimetrica;
    }

    public void setLlaveSimetrica(String llaveSimetrica) {
        this.llaveSimetrica = llaveSimetrica;
    }

    public String getVectorInicializacion() {
        return vectorInicializacion;
    }

    public void setVectorInicializacion(String vectorInicializacion) {
        this.vectorInicializacion = vectorInicializacion;
    }
}
