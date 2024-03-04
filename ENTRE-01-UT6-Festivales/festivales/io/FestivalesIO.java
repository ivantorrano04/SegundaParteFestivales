package festivales.io;

import festivales.modelo.AgendaFestivales;
import festivales.modelo.Estilo;
import festivales.modelo.Festival;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * La clase contiene métodos estáticos que permiten
 * cargar la agenda de festivales leyendo los datos desde
 * un fichero
 */
public class FestivalesIO {

    public static void cargarFestivales(AgendaFestivales agenda) {
        Scanner sc = null;
        try {
            sc = new Scanner(FestivalesIO.class.getResourceAsStream("/festivales.csv"));
            while (sc.hasNextLine()) {
                String lineaFestival = sc.nextLine();
                Festival festival = parsearLinea(lineaFestival);
                agenda.addFestival(festival);
            }
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
    }

    /**
     * Se parsea la línea extrayendo sus datos y creando y
     * devolviendo un objeto festivales.modelo.Festival
     *
     * @param lineaFestival los datos de un festival
     * @return el festival creado
     */
    public static Festival parsearLinea(String lineaFestival) {

        lineaFestival = lineaFestival.trim();

        String[] partes = lineaFestival.split(":");

        String nombre = obtenerNombre(partes[0]);
        String lugar = partes[1].toUpperCase();
        LocalDate fechaInicio = obtenerFecha(partes[2]);
        int duracion = Integer.parseInt(partes[3].trim());
        Set<Estilo> estilos = obtenerEstilos(Arrays.copyOfRange(partes, 4, partes.length));

        return new Festival(nombre, lugar, fechaInicio, duracion, (HashSet<Estilo>) estilos);
    }

    private static String obtenerNombre(String nombre) {
        String[] palabras = nombre.split(" ");
        StringBuilder resultado = new StringBuilder();
        for (String palabra : palabras) {
            resultado.append(palabra.substring(0, 1).toUpperCase())
                    .append(palabra.substring(1).toLowerCase())
                    .append(" ");
        }
        return resultado.toString().trim();
    }

    private static LocalDate obtenerFecha(String fecha) {
        String[] partesFecha = fecha.split("-");
        int dia = Integer.parseInt(partesFecha[0].trim());
        int mes = Integer.parseInt(partesFecha[1].trim());
        int anio = Integer.parseInt(partesFecha[2].trim());
        return LocalDate.of(anio, mes, dia);
    }

    private static Set<Estilo> obtenerEstilos(String[] estilos) {
        Set<Estilo> conjuntoEstilos = new HashSet<>();
        for (String estilo : estilos) {
            conjuntoEstilos.add(Estilo.valueOf(estilo.trim().toUpperCase()));
        }
        return conjuntoEstilos;
    }
}