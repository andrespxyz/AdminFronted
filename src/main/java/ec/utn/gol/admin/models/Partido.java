package ec.utn.gol.admin.models;

public class Partido {
    private int id;
    private int seleccionLocalId;
    private int seleccionVisitanteId;
    private int sedeId;
    private String fechaHora;
    private String fase;
    private String grupo;
    private String estado;
    private Integer golesLocal;
    private Integer golesVisitante;
    private Seleccion seleccionLocal;
    private Seleccion seleccionVisitante;
    private Sede sede;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getSeleccionLocalId() { return seleccionLocalId; }
    public void setSeleccionLocalId(int seleccionLocalId) { this.seleccionLocalId = seleccionLocalId; }
    public int getSeleccionVisitanteId() { return seleccionVisitanteId; }
    public void setSeleccionVisitanteId(int seleccionVisitanteId) { this.seleccionVisitanteId = seleccionVisitanteId; }
    public int getSedeId() { return sedeId; }
    public void setSedeId(int sedeId) { this.sedeId = sedeId; }
    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
    public String getFase() { return fase; }
    public void setFase(String fase) { this.fase = fase; }
    public String getGrupo() { return grupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Integer getGolesLocal() { return golesLocal; }
    public void setGolesLocal(Integer golesLocal) { this.golesLocal = golesLocal; }
    public Integer getGolesVisitante() { return golesVisitante; }
    public void setGolesVisitante(Integer golesVisitante) { this.golesVisitante = golesVisitante; }
    public Seleccion getSeleccionLocal() { return seleccionLocal; }
    public void setSeleccionLocal(Seleccion seleccionLocal) { this.seleccionLocal = seleccionLocal; }
    public Seleccion getSeleccionVisitante() { return seleccionVisitante; }
    public void setSeleccionVisitante(Seleccion seleccionVisitante) { this.seleccionVisitante = seleccionVisitante; }
    public Sede getSede() { return sede; }
    public void setSede(Sede sede) { this.sede = sede; }
}