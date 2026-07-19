package ec.utn.gol.admin.models;

public class Seleccion {
    private int id;
    private String nombre;
    private String codigo;
    private String grupo;
    private String escudo;
    private int partidosJugados;
    private int partidosGanados;
    private int partidosEmpatados;
    private int partidosPerdidos;
    private int golesFavor;
    private int golesContra;
    private int puntos;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getGrupo() { return grupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }
    public String getEscudo() { return escudo; }
    public void setEscudo(String escudo) { this.escudo = escudo; }
    public int getPartidosJugados() { return partidosJugados; }
    public void setPartidosJugados(int partidosJugados) { this.partidosJugados = partidosJugados; }
    public int getPartidosGanados() { return partidosGanados; }
    public void setPartidosGanados(int partidosGanados) { this.partidosGanados = partidosGanados; }
    public int getPartidosEmpatados() { return partidosEmpatados; }
    public void setPartidosEmpatados(int partidosEmpatados) { this.partidosEmpatados = partidosEmpatados; }
    public int getPartidosPerdidos() { return partidosPerdidos; }
    public void setPartidosPerdidos(int partidosPerdidos) { this.partidosPerdidos = partidosPerdidos; }
    public int getGolesFavor() { return golesFavor; }
    public void setGolesFavor(int golesFavor) { this.golesFavor = golesFavor; }
    public int getGolesContra() { return golesContra; }
    public void setGolesContra(int golesContra) { this.golesContra = golesContra; }
    public int getPuntos() { return puntos; }
    public void setPuntos(int puntos) { this.puntos = puntos; }
}