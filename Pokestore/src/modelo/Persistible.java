package modelo;

/**
 * Contrato para los objetos que se pueden guardar en un archivo CSV.
 * Cada clase decide cómo se convierte en una línea de texto.
 */
public interface Persistible {
    String aLineaCSV();
}
