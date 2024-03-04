package festivales.modelo;

import festivales.modelo.Estilo;
import festivales.modelo.Festival;
import festivales.modelo.Mes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
/**
 * Esta clase guarda una agenda con los festivales programados
 * en una serie de meses
 *
 * La agenda guardalos festivales en una colecci�n map
 * La clave del map es el mes (un enumerado festivales.modelo.festivales.modelo.Mes)
 * Cada mes tiene asociados en una colecci�n ArrayList
 * los festivales  de ese mes
 *
 * Solo aparecen los meses que incluyen alg�n festival
 *
 * Las claves se recuperan en orden alfab�ico
 *
 */
public class AgendaFestivales {
    private TreeMap<Mes, ArrayList<Festival>> agenda;

    public AgendaFestivales() {
        this.agenda = new TreeMap<>();
    }
    /**
     * a�ade un nuevo festival a la agenda
     *
     * Si la clave (el mes en el que se celebra el festival)
     * no existe en la agenda se crear� una nueva entrada
     * con dicha clave y la colecci�n formada por ese �nico festival
     *
     * Si la clave (el mes) ya existe se a�ade el nuevo festival
     * a la lista de festivales que ya existe ese ms
     * insert�ndolo de forma que quede ordenado por nombre de festival.
     * Para este segundo caso usa el m�todo de ayuda
     * obtenerPosicionDeInsercion()
     *
     */
    public void addFestival(Festival festival) {
        Mes mes = festival.getMes();

        if (!agenda.containsKey(mes)) {
            // Si el mes no existe en la agenda, crea una nueva entrada con el festival.
            ArrayList<Festival> nuevosFestivales = new ArrayList<>();
            nuevosFestivales.add(festival);
            agenda.put(mes, nuevosFestivales);
        } else {
            // Si el mes ya existe, inserta el festival en la posici�n adecuada.
            ArrayList<Festival> festivalesEnMes = agenda.get(mes);
            int posicion = obtenerPosicionDeInsercion(festivalesEnMes, festival);
            festivalesEnMes.add(posicion, festival);
        }
    }
    /**
     *
     * @param festivales una lista de festivales
     * @param festival
     * @return la posici�n en la que deber�a ir el nuevo festival
     * de forma que la lista quedase ordenada por nombre
     */
    private int obtenerPosicionDeInsercion(ArrayList<Festival> festivales, Festival festival) {
        int posicion = 0;

        for (Festival fest : festivales) {
            if (festival.getNombre().compareTo(fest.getNombre()) < 0) {
                break;
            }
            posicion++;
        }

        return posicion;
    }
    /**
     * Representaci�n textual del festival
     * De forma eficiente
     *  Usa el conjunto de entradas para recorrer el map
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<Mes, ArrayList<Festival>> entry : agenda.entrySet()) {
            Mes mes = entry.getKey();
            ArrayList<Festival> festivalesEnMes = entry.getValue();

            result.append(mes).append(": ").append(festivalesEnMes.size()).append("\n");

            for (Festival festival : festivalesEnMes) {
                result.append(festival).append("\n");
            }

            result.append("------------------------------------------------------------\n");
        }

        return result.toString();
    }
    /**
     *
     * @param mes el mes a considerar
     * @return la cantidad de festivales que hay en ese mes
     * Si el mes no existe se devuelve -1
     */
    public int festivalesEnMes(Mes mes) {
        if (agenda.containsKey(mes)) {
            return agenda.get(mes).size();
        } else {
            return 0;
        }
    }
    /**
     * Se trata de agrupar todos los festivales de la agenda
     * por estilo.
     * Cada estilo que aparece en la agenda tiene asociada una colecci�n
     * que es el conjunto de nombres de festivales que pertenecen a ese estilo
     * Importa el orden de los nombres en el conjunto
     *
     * Identifica el tipo exacto del valor de retorno
     */
    public Map<Estilo, Set<String>> festivalesPorEstilo() {
        Map<Estilo, Set<String>> festivalesPorEstilo = new TreeMap<>();

        for (Mes mes : agenda.keySet()) {
            for (Festival festival : agenda.get(mes)) {
                for (Estilo estilo : festival.getEstilos()) {
                    festivalesPorEstilo.computeIfAbsent(estilo, k -> new HashSet<>()).add(festival.getNombre());
                }
            }
        }

        return festivalesPorEstilo;
    }
    /**
     * Se cancelan todos los festivales organizados en alguno de los
     * lugares que indica el conjunto en el mes indicado. Los festivales
     * concluidos o que no empezados no se tienen en cuenta
     * Hay que borrarlos de la agenda
     * Si el mes no existe se devuelve -1
     *
     * Si al borrar de un mes los festivales el mes queda con 0 festivales
     * se borra la entrada completa del map
     */
    public int cancelarFestivales(HashSet<String> lugares, Mes mes) {
        if (agenda.containsKey(mes)) {
            ArrayList<Festival> festivalesEnMes = agenda.get(mes);
            ArrayList<Festival> festivalesCancelados = new ArrayList<>();

            for (Festival festival : festivalesEnMes) {
                if (!festival.haConcluido() && lugares.contains(festival.getLugar())) {
                    festivalesCancelados.add(festival);
                }
            }

            festivalesEnMes.removeAll(festivalesCancelados);

            if (festivalesEnMes.isEmpty()) {
                agenda.remove(mes);
            }

            return festivalesCancelados.size();
        } else {
            return -1;
        }
    }
}