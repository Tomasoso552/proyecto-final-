public class Jugador {
    int id;
    String nombre;
    int edad;
    int puntaje;

    public Jugador(int id, String nombre, int edad, int puntaje) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.puntaje = puntaje;
    }

    public String toTexto() {
        return id + "," + nombre + "," + edad + "," + puntaje;
    }

    public static Jugador desdeTexto(String linea) {
        String[] datos = linea.split(",");
        int id = Integer.parseInt(datos[0]);
        String nombre = datos[1];
        int edad = Integer.parseInt(datos[2]);
        int puntaje = Integer.parseInt(datos[3]);
        return new Jugador(id, nombre, edad, puntaje);
    }

    public String toString() {
        return "ID: " + id + ", Nombre: " + nombre + ", Edad: " + edad + ", Puntaje: " + puntaje;
    }
}