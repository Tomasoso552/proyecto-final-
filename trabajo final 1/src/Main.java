import java.io.*;
import java.util.*;

public class Main {
    static String archivo = "jugadores.txt";
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Menú ---");
            System.out.println("1. Agregar jugador");
            System.out.println("2. Buscar jugador");
            System.out.println("3. Editar jugador");
            System.out.println("4. Eliminar jugador");
            System.out.println("5. Listar jugadores");
            System.out.println("6. Salir");
            System.out.print("Opción: ");
            String entrada = sc.nextLine();

            switch (entrada) {
                case "1" -> agregarJugador();
                case "2" -> buscarJugador();
                case "3" -> editarJugador();
                case "4" -> eliminarJugador();
                case "5" -> listarJugadores();
                case "6" -> {
                    System.out.println("Adiós!");
                    return;
                }
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    static void agregarJugador() {
        try {
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Edad: ");
            int edad = Integer.parseInt(sc.nextLine());
            System.out.print("Puntaje: ");
            int puntaje = Integer.parseInt(sc.nextLine());
            int id = (int) (System.currentTimeMillis() % 10000);
            Jugador j = new Jugador(id, nombre, edad, puntaje);
            try (FileWriter fw = new FileWriter(archivo, true)) {
                fw.write(j.toTexto() + "\n");
                System.out.println("Jugador agregado con ID " + id);
            } catch (IOException e) {
                System.out.println("Error al guardar.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Edad y puntaje deben ser números.");
        }
    }

    static void buscarJugador() {
        System.out.print("ID del jugador a buscar: ");
        String entrada = sc.nextLine();
        try {
            int id = Integer.parseInt(entrada);
            for (Jugador j : cargarJugadores()) {
                if (j.id == id) {
                    System.out.println(j);
                    return;
                }
            }
            System.out.println("Jugador no encontrado.");
        } catch (NumberFormatException e) {
            System.out.println("Debes ingresar un número válido.");
        }
    }

    static void editarJugador() {
        System.out.print("ID del jugador a editar: ");
        String entrada = sc.nextLine();
        try {
            int id = Integer.parseInt(entrada);
            List<Jugador> lista = cargarJugadores();
            boolean encontrado = false;

            for (Jugador j : lista) {
                if (j.id == id) {
                    System.out.print("Nuevo nombre: ");
                    j.nombre = sc.nextLine();
                    System.out.print("Nueva edad: ");
                    j.edad = Integer.parseInt(sc.nextLine());
                    System.out.print("Nuevo puntaje: ");
                    j.puntaje = Integer.parseInt(sc.nextLine());
                    encontrado = true;
                    break;
                }
            }

            if (encontrado) {
                guardarLista(lista);
                System.out.println("Jugador editado.");
            } else {
                System.out.println("Jugador no encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Edad y puntaje deben ser números.");
        }
    }

    static void eliminarJugador() {
        System.out.print("ID del jugador a eliminar: ");
        String entrada = sc.nextLine();
        try {
            int id = Integer.parseInt(entrada);
            List<Jugador> lista = cargarJugadores();
            boolean eliminado = lista.removeIf(j -> j.id == id);

            if (eliminado) {
                guardarLista(lista);
                System.out.println("Jugador eliminado.");
            } else {
                System.out.println("Jugador no encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Debes ingresar un número válido.");
        }
    }

    static void listarJugadores() {
        List<Jugador> lista = cargarJugadores();
        if (lista.isEmpty()) {
            System.out.println("No hay jugadores registrados.");
        } else {
            for (Jugador j : lista) {
                System.out.println(j);
            }
        }
    }

    static List<Jugador> cargarJugadores() {
        List<Jugador> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lista.add(Jugador.desdeTexto(linea));
            }
        } catch (IOException ignored) {}
        return lista;
    }

    static void guardarLista(List<Jugador> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (Jugador j : lista) {
                pw.println(j.toTexto());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar.");
        }
    }
}