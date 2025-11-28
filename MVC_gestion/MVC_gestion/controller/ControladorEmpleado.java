package controller;

import model.Empleado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorEmpleado {

    // Método para agregar (CREATE)
    public boolean agregarRegistro(Empleado emp) {
        String sql = "INSERT INTO empleado (idEmpleado, nombreEmpleado, fechaInicio, fechaTermino, tipoContrato, planSalud, afp) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, emp.getIdEmpleado());
            ps.setString(2, emp.getNombreEmpleado());
            ps.setDate(3, emp.getFechaInicio());
            if (emp.getFechaTermino() != null) {
                ps.setDate(4, emp.getFechaTermino());
            } else {
                ps.setNull(4, Types.DATE);
            }
            ps.setString(5, emp.getTipoContrato());
            ps.setBoolean(6, emp.isPlanSalud());
            ps.setBoolean(7, emp.isAfp());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para listar (READ)
    public List<Empleado> cargarRegistros() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT * FROM empleado";
        try (Connection con = Conexion.getConexion(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Empleado emp = new Empleado(
                    rs.getInt("idEmpleado"),
                    rs.getString("nombreEmpleado"),
                    rs.getDate("fechaInicio"),
                    rs.getDate("fechaTermino"),
                    rs.getString("tipoContrato"),
                    rs.getBoolean("planSalud"),
                    rs.getBoolean("afp")
                );
                lista.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Método para modificar (UPDATE)
    public boolean modificarRegistro(Empleado emp) {
        String sql = "UPDATE empleado SET nombreEmpleado=?, fechaInicio=?, fechaTermino=?, tipoContrato=?, planSalud=?, afp=? WHERE idEmpleado=?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, emp.getNombreEmpleado());
            ps.setDate(2, emp.getFechaInicio());
            if (emp.getFechaTermino() != null) {
                ps.setDate(3, emp.getFechaTermino());
            } else {
                ps.setNull(3, Types.DATE);
            }
            ps.setString(4, emp.getTipoContrato());
            ps.setBoolean(5, emp.isPlanSalud());
            ps.setBoolean(6, emp.isAfp());
            ps.setInt(7, emp.getIdEmpleado());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para eliminar (DELETE)
    public boolean eliminarRegistro(int id) {
        String sql = "DELETE FROM empleado WHERE idEmpleado=?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
