package model;

import java.sql.Date;

public class Empleado {
    private int idEmpleado;
    private String nombreEmpleado;
    private Date fechaInicio;
    private Date fechaTermino;
    private String tipoContrato;
    private boolean planSalud;
    private boolean afp;

    // Constructor Vacío
    public Empleado() {
    }

    // Constructor con parámetros
    public Empleado(int idEmpleado, String nombreEmpleado, Date fechaInicio, Date fechaTermino, String tipoContrato, boolean planSalud, boolean afp) {
        this.idEmpleado = idEmpleado;
        this.nombreEmpleado = nombreEmpleado;
        this.fechaInicio = fechaInicio;
        this.fechaTermino = fechaTermino;
        this.tipoContrato = tipoContrato;
        this.planSalud = planSalud;
        this.afp = afp;
    }

    // Getters y Setters
    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaTermino() { return fechaTermino; }
    public void setFechaTermino(Date fechaTermino) { this.fechaTermino = fechaTermino; }

    public String getTipoContrato() { return tipoContrato; }
    public void setTipoContrato(String tipoContrato) { this.tipoContrato = tipoContrato; }

    public boolean isPlanSalud() { return planSalud; }
    public void setPlanSalud(boolean planSalud) { this.planSalud = planSalud; }

    public boolean isAfp() { return afp; }
    public void setAfp(boolean afp) { this.afp = afp; }
}
