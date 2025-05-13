import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;


public class JugadorGUI extends JFrame {
    private JTextField txtId, txtNombre, txtEdad, txtPuntaje;
    private JTextArea areaResultado;
    private static final String ARCHIVO = "jugadores.txt";

    public JugadorGUI() {
        setTitle("Gestión de Jugadores");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Campos
        JPanel panelCampos = new JPanel(new GridLayout(5, 2));
        panelCampos.add(new JLabel("ID (solo para buscar/editar/eliminar):"));
        txtId = new JTextField();
        panelCampos.add(txtId);
        panelCampos.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelCampos.add(txtNombre);
        panelCampos.add(new JLabel("Edad:"));
        txtEdad = new JTextField();
        panelCampos.add(txtEdad);
        panelCampos.add(new JLabel("Puntaje:"));
        txtPuntaje = new JTextField();
        panelCampos.add(txtPuntaje);

        // Botones
        JPanel panelBotones = new JPanel();
        String[] botones = {"Agregar", "Buscar", "Editar", "Eliminar", "Listar", "Limpiar"};
        for (String b : botones) {
            JButton btn = new JButton(b);
            btn.addActionListener(e -> manejarAccion(b));
            panelBotones.add(btn);
        }

        // Área de resultados
        areaResultado = new JTextArea(10, 40);
        areaResultado.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaResultado);

        add(panelCampos, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
    }

    private void manejarAccion(String accion) {
        switch (accion) {
            case "Agregar" -> agregarJugador();
            case "Buscar" -> buscarJugador();
            case "Editar" -> editarJugador();
            case "Eliminar" -> eliminarJugador();
            case "Listar" -> listarJugadores();
            case "Limpiar" -> limpiarCampos();
        }
    }

    private void agregarJugador() {
        try {
            String nombre = txtNombre.getText();
            int edad = Integer.parseInt(txtEdad.getText());
            int puntaje = Integer.parseInt(txtPuntaje.getText());
            int id = (int) (System.currentTimeMillis() % 10000);
            Jugador j = new Jugador(id, nombre, edad, puntaje);
            try (FileWriter fw = new FileWriter(ARCHIVO, true)) {
                fw.write(j.toTexto() + "\n");
                areaResultado.setText("Jugador agregado con ID: " + id + "\n");
            }
            limpiarCampos();
        } catch (Exception e) {
            areaResultado.setText("Error al agregar. Verifica los campos.");
        }
    }

    private void buscarJugador() {
        try {
            int id = Integer.parseInt(txtId.getText());
            for (Jugador j : cargarJugadores()) {
                if (j.id == id) {
                    txtNombre.setText(j.nombre);
                    txtEdad.setText(String.valueOf(j.edad));
                    txtPuntaje.setText(String.valueOf(j.puntaje));
                    areaResultado.setText("Jugador encontrado:\n" + j);
                    return;
                }
            }
            areaResultado.setText("Jugador no encontrado.");
        } catch (Exception e) {
            areaResultado.setText("Ingresa un ID válido.");
        }
    }

    private void editarJugador() {
        try {
            int id = Integer.parseInt(txtId.getText());
            List<Jugador> lista = cargarJugadores();
            boolean encontrado = false;
            for (Jugador j : lista) {
                if (j.id == id) {
                    j.nombre = txtNombre.getText();
                    j.edad = Integer.parseInt(txtEdad.getText());
                    j.puntaje = Integer.parseInt(txtPuntaje.getText());
                    encontrado = true;
                    break;
                }
            }
            if (encontrado) {
                guardarLista(lista);
                areaResultado.setText("Jugador actualizado.");
            } else {
                areaResultado.setText("Jugador no encontrado.");
            }
        } catch (Exception e) {
            areaResultado.setText("Error al editar. Revisa los campos.");
        }
    }

    private void eliminarJugador() {
        try {
            int id = Integer.parseInt(txtId.getText());
            List<Jugador> lista = cargarJugadores();
            boolean eliminado = lista.removeIf(j -> j.id == id);
            if (eliminado) {
                guardarLista(lista);
                areaResultado.setText("Jugador eliminado.");
                limpiarCampos();
            } else {
                areaResultado.setText("Jugador no encontrado.");
            }
        } catch (Exception e) {
            areaResultado.setText("Ingresa un ID válido.");
        }
    }

    private void listarJugadores() {
        StringBuilder sb = new StringBuilder("Lista de jugadores:\n");
        for (Jugador j : cargarJugadores()) {
            sb.append(j).append("\n");
        }
        areaResultado.setText(sb.toString());
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtEdad.setText("");
        txtPuntaje.setText("");
    }

    private List<Jugador> cargarJugadores() {
        List<Jugador> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lista.add(Jugador.desdeTexto(linea));
            }
        } catch (IOException ignored) {}
        return lista;
    }

    private void guardarLista(List<Jugador> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            for (Jugador j : lista) {
                pw.println(j.toTexto());
            }
        } catch (IOException e) {
            areaResultado.setText("Error al guardar.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JugadorGUI().setVisible(true));
    }
}