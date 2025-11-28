package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.SpinnerDateModel;
import controller.ControladorEmpleado;
import model.Empleado;
import java.util.Date;
import java.util.List;

/**
 * VentanaEmpleado: interfaz gráfica para CRUD de empleados.
 * - Formulario a la izquierda (ID, nombre, fechas, contrato, opciones).
 * - Tabla centrada a la derecha con los registros.
 * - Fondo rosado.
 */
public class VentanaEmpleado extends JFrame {

    // Componentes de la interfaz gráfica
    private JLabel lblId, lblNombre, lblFechaInicio, lblFechaTermino, lblContrato;
    private JTextField txtId, txtNombre;
    private JSpinner spinnerFechaInicio, spinnerFechaTermino;
    private JComboBox<String> cbContrato;
    private JCheckBox chkSalud, chkAfp;
    // Botones: Agregar, Modificar, Eliminar, Consultar (sin "Limpiar")
    private JButton btnAgregar, btnModificar, btnEliminar, btnConsultar;
    private JTable tablaEmpleados;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollPane;

    // Controlador que maneja la lógica de acceso a datos
    private ControladorEmpleado controlador;

    // Constructor: inicializa ventana y componentes
    public VentanaEmpleado() {

        controlador = new ControladorEmpleado();

        setSize(900, 550);
        setTitle("Sistema de Gestión de Pagos - MVP");
        // fondo rosado
        getContentPane().setBackground(Color.PINK);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Preparar componentes y layout
        configurarTablaDeDatos();         // crea modelo y tabla
        configurarCamposDeEntradas();     // crea etiquetas y campos (sin posicionar)
        configurarBotonesDeEventos();     // crea botones
        organizarLayout();                // organiza left/right con JSplitPane
        configurarEventos();              // asocia listeners a botones/tabla
        cargarDatosTabla();               // carga datos iniciales en la tabla
    }

    /**
     * organizarLayout: construye el panel izquierdo con formulario y botones
     * y el panel derecho con la tabla centrada.
     */
    private void organizarLayout() {
        JPanel left = new JPanel(new GridBagLayout());
        left.setBackground(Color.PINK);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;

        // fila 0: ID (label + campo)
        left.add(lblId, c);
        c.gridx = 1;
        left.add(txtId, c);

        // fila 1: Nombre
        c.gridy++;
        c.gridx = 0;
        left.add(lblNombre, c);
        c.gridx = 1;
        left.add(txtNombre, c);

        // fila 2: Fecha Inicio
        c.gridy++;
        c.gridx = 0;
        left.add(lblFechaInicio, c);
        c.gridx = 1;
        left.add(spinnerFechaInicio, c);

        // fila 3: Fecha Término
        c.gridy++;
        c.gridx = 0;
        left.add(lblFechaTermino, c);
        c.gridx = 1;
        left.add(spinnerFechaTermino, c);

        // fila 4: Tipo de Contrato
        c.gridy++;
        c.gridx = 0;
        left.add(lblContrato, c);
        c.gridx = 1;
        left.add(cbContrato, c);

        // fila 5: Checkboxes (ocupan dos columnas)
        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 2;
        JPanel chkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        chkPanel.setBackground(Color.PINK);
        chkPanel.add(chkSalud);
        chkPanel.add(chkAfp);
        left.add(chkPanel, c);
        c.gridwidth = 1;

        // fila 6: botones en grid 2x2 con separación 2px
        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 2;
        JPanel botonesPanel = new JPanel(new GridLayout(2, 2, 2, 2)); // 2x2, gaps 2px
        botonesPanel.setBackground(Color.PINK);
        // orden: dos arriba (Agregar, Modificar), dos abajo (Eliminar, Consultar)
        botonesPanel.add(btnAgregar);
        botonesPanel.add(btnModificar);
        botonesPanel.add(btnEliminar);
        botonesPanel.add(btnConsultar);
        left.add(botonesPanel, c);
        c.gridwidth = 1;

        // PANEL DERECHO: tabla centrada
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(Color.PINK);

        // Configuraciones visuales de la tabla: no editable por el usuario
        tablaEmpleados.setRowSelectionAllowed(false);
        tablaEmpleados.setColumnSelectionAllowed(false);
        tablaEmpleados.setCellSelectionEnabled(false);
        tablaEmpleados.setFocusable(false);
        tablaEmpleados.setDefaultEditor(Object.class, null); // no editar celdas
        tablaEmpleados.getTableHeader().setReorderingAllowed(false);
        tablaEmpleados.getTableHeader().setResizingAllowed(false);
        tablaEmpleados.setEnabled(false);

        GridBagConstraints rc = new GridBagConstraints();
        rc.gridx = 0;
        rc.gridy = 0;
        rc.weightx = 1.0;
        rc.weighty = 1.0;
        rc.anchor = GridBagConstraints.CENTER; // centro del panel derecho
        rc.fill = GridBagConstraints.NONE;

        // tamaño preferido del scrollPane para centrar visualmente
        scrollPane.setPreferredSize(new Dimension(480, 420));
        right.add(scrollPane, rc);

        // Split entre formulario (izquierda) y tabla (derecha)
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setDividerLocation(360);
        split.setResizeWeight(0);
        split.setOneTouchExpandable(true);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(split, BorderLayout.CENTER);

        // Ajustes de tamaño para componentes del formulario
        txtId.setPreferredSize(new Dimension(180, 24));
        txtNombre.setPreferredSize(new Dimension(180, 24));
        spinnerFechaInicio.setPreferredSize(new Dimension(160, 24));
        spinnerFechaTermino.setPreferredSize(new Dimension(160, 24));
        cbContrato.setPreferredSize(new Dimension(180, 24));
    }

    /**
     * configurarCamposDeEntradas: instancia labels, campos y spinners.
     * - spinnerFechaTermino inicia deshabilitado; se habilita según contrato.
     */
    private void configurarCamposDeEntradas() {

        lblId = new JLabel("ID Empleado:");
        txtId = new JTextField();

        lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField();

        lblFechaInicio = new JLabel("F. Inicio (yyyy-MM-dd):");
        spinnerFechaInicio = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
        spinnerFechaInicio.setEditor(new JSpinner.DateEditor(spinnerFechaInicio, "yyyy-MM-dd"));

        lblFechaTermino = new JLabel("F. Término (yyyy-MM-dd):");
        spinnerFechaTermino = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
        spinnerFechaTermino.setEditor(new JSpinner.DateEditor(spinnerFechaTermino, "yyyy-MM-dd"));
        spinnerFechaTermino.setEnabled(false); // por defecto desactivado

        lblContrato = new JLabel("Tipo Contrato:");
        cbContrato = new JComboBox<>(new String[] { "Indefinido", "Plazo Fijo", "Honorarios" });

        chkSalud = new JCheckBox("Plan de Salud");
        chkSalud.setBackground(Color.PINK);

        chkAfp = new JCheckBox("AFP");
        chkAfp.setBackground(Color.PINK);

        // Al cambiar tipo de contrato se habilita/deshabilita fecha de término
        cbContrato.addActionListener(e -> {
            String tipo = cbContrato.getSelectedItem().toString();
            spinnerFechaTermino.setEnabled(!tipo.equals("Indefinido"));
        });
    }

    /**
     * configurarTablaDeDatos: crea modelo no editable y la tabla.
     */
    private void configurarTablaDeDatos() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // evitar edición directa en la tabla
            }
        };
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("F. Inicio");
        modeloTabla.addColumn("F. Término");
        modeloTabla.addColumn("Contrato");
        modeloTabla.addColumn("Salud");
        modeloTabla.addColumn("AFP");

        tablaEmpleados = new JTable(modeloTabla);
        // evitar reordenar y redimensionar columnas
        tablaEmpleados.getTableHeader().setReorderingAllowed(false);
        for (int i = 0; i < modeloTabla.getColumnCount(); i++) {
            tablaEmpleados.getColumnModel().getColumn(i).setResizable(false);
        }

        scrollPane = new JScrollPane(tablaEmpleados);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    /**
     * configurarBotonesDeEventos: crea instancias de los botones.
     * Nota: el botón "Limpiar" fue eliminado; "Refrescar" renombrado a "Consultar".
     */
    private void configurarBotonesDeEventos() {
        btnAgregar = new JButton("Agregar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnConsultar = new JButton("Consultar"); // nombre cambiado
    }

    /**
     * configurarEventos: listeners para botones y tabla.
     * - Agregar / Modificar / Eliminar usan controlador para persistencia.
     * - Consultar recarga la tabla.
     */
    private void configurarEventos() {

        // AGREGAR: valida campos, arma objeto Empleado y llama al controlador
        btnAgregar.addActionListener(e -> {
            if (validarCampos()) {
                try {
                    Empleado emp = new Empleado();
                    emp.setIdEmpleado(Integer.parseInt(txtId.getText()));
                    emp.setNombreEmpleado(txtNombre.getText());

                    Date inicioVal = (Date) spinnerFechaInicio.getValue();
                    emp.setFechaInicio(new java.sql.Date(inicioVal.getTime()));

                    // si fecha termino está habilitada, se asigna; si no, queda null
                    if (spinnerFechaTermino.isEnabled()) {
                        Date terminoVal = (Date) spinnerFechaTermino.getValue();
                        emp.setFechaTermino(new java.sql.Date(terminoVal.getTime()));
                    } else {
                        emp.setFechaTermino(null);
                    }

                    emp.setTipoContrato(cbContrato.getSelectedItem().toString());
                    emp.setPlanSalud(chkSalud.isSelected());
                    emp.setAfp(chkAfp.isSelected());

                    if (controlador.agregarRegistro(emp)) {
                        JOptionPane.showMessageDialog(null, "Empleado agregado correctamente.");
                        cargarDatosTabla();
                        limpiarCampos();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al agregar empleado.");
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error en los datos ingresados.");
                }
            }
        });

        // MODIFICAR: similar a agregar pero llama a modificarRegistro
        btnModificar.addActionListener(e -> {
            if (validarCampos()) {
                try {
                    Empleado emp = new Empleado();
                    emp.setIdEmpleado(Integer.parseInt(txtId.getText()));
                    emp.setNombreEmpleado(txtNombre.getText());

                    Date inicioVal = (Date) spinnerFechaInicio.getValue();
                    emp.setFechaInicio(new java.sql.Date(inicioVal.getTime()));

                    if (spinnerFechaTermino.isEnabled()) {
                        Date terminoVal = (Date) spinnerFechaTermino.getValue();
                        emp.setFechaTermino(new java.sql.Date(terminoVal.getTime()));
                    } else {
                        emp.setFechaTermino(null);
                    }

                    emp.setTipoContrato(cbContrato.getSelectedItem().toString());
                    emp.setPlanSalud(chkSalud.isSelected());
                    emp.setAfp(chkAfp.isSelected());

                    if (controlador.modificarRegistro(emp)) {
                        JOptionPane.showMessageDialog(null, "Empleado modificado correctamente.");
                        cargarDatosTabla();
                        limpiarCampos();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al modificar empleado.");
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error en los datos ingresados.");
                }
            }
        });

        // ELIMINAR: elimina por ID
        btnEliminar.addActionListener(e -> {
            if (!txtId.getText().isEmpty()) {
                try {
                    int id = Integer.parseInt(txtId.getText());

                    if (controlador.eliminarRegistro(id)) {
                        JOptionPane.showMessageDialog(null, "Empleado eliminado.");
                        cargarDatosTabla();
                        limpiarCampos();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al eliminar empleado.");
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "El ID debe ser numérico.");
                }
            }
        });

        // CONSULTAR: recarga los registros en la tabla
        btnConsultar.addActionListener(e -> cargarDatosTabla());

        // Selección en la tabla: carga los datos del registro seleccionado en el formulario
        tablaEmpleados.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int fila = tablaEmpleados.getSelectedRow();

                if (fila >= 0) {
                    txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());

                    // convertir java.sql.Date a java.util.Date para el spinner
                    Object inicioObj = modeloTabla.getValueAt(fila, 2);
                    if (inicioObj instanceof java.sql.Date) {
                        spinnerFechaInicio.setValue(new Date(((java.sql.Date) inicioObj).getTime()));
                    } else {
                        spinnerFechaInicio.setValue(new Date());
                    }

                    Object terminoObj = modeloTabla.getValueAt(fila, 3);
                    if (terminoObj instanceof java.sql.Date) {
                        spinnerFechaTermino.setValue(new Date(((java.sql.Date) terminoObj).getTime()));
                    } else {
                        spinnerFechaTermino.setValue(new Date());
                    }

                    cbContrato.setSelectedItem(modeloTabla.getValueAt(fila, 4));
                    spinnerFechaTermino.setEnabled(!cbContrato.getSelectedItem().equals("Indefinido"));

                    chkSalud.setSelected((boolean) modeloTabla.getValueAt(fila, 5));
                    chkAfp.setSelected((boolean) modeloTabla.getValueAt(fila, 6));

                    // bloquear edición del ID cuando se selecciona un registro existente
                    txtId.setEditable(false);
                }
            }
        });

        // Listener para cerrar la aplicación correctamente
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
    }

    /**
     * validarCampos: validaciones básicas del formulario:
     * - ID y nombre obligatorios.
     * - Si contrato no es Indefinido, fecha término >= fecha inicio.
     */
    private boolean validarCampos() {

        if (txtId.getText().trim().isEmpty() ||
            txtNombre.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                "Error: ID y nombre son obligatorios.",
                "Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String tipo = cbContrato.getSelectedItem().toString();

        if (!tipo.equals("Indefinido")) {
            Date inicio = (Date) spinnerFechaInicio.getValue();
            Date termino = (Date) spinnerFechaTermino.getValue();
            if (termino == null || termino.before(inicio)) {
                JOptionPane.showMessageDialog(this,
                    "La fecha de término debe ser posterior o igual a la fecha de inicio.",
                    "Validación", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    /**
     * limpiarCampos: resetea el formulario a valores por defecto.
     * (Aunque el botón 'Limpiar' fue eliminado, el método se usa internamente).
     */
    private void limpiarCampos() {
        txtId.setText("");
        txtId.setEditable(true);
        txtNombre.setText("");
        spinnerFechaInicio.setValue(new Date());
        spinnerFechaTermino.setValue(new Date());
        cbContrato.setSelectedIndex(0);
        chkSalud.setSelected(false);
        chkAfp.setSelected(false);
        spinnerFechaTermino.setEnabled(false);
        tablaEmpleados.clearSelection();
    }

    /**
     * cargarDatosTabla: obtiene registros desde el controlador y los muestra.
     */
    private void cargarDatosTabla() {
        modeloTabla.setRowCount(0);
        List<Empleado> lista = controlador.cargarRegistros();

        for (Empleado e : lista) {
            modeloTabla.addRow(new Object[]{
                e.getIdEmpleado(),
                e.getNombreEmpleado(),
                e.getFechaInicio(),
                e.getFechaTermino(),
                e.getTipoContrato(),
                e.isPlanSalud(),
                e.isAfp()
            });
        }
    }
}
