// paquete de la practica
package es.ubu.gii.edat.P3;

// imports necesarios para la implementacion
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Mapa que guarda parejas clave-valor usando varias cubetas.
 * 
 * @author <a href="arc1049@alu.ubu.es">Alvaro Rodríguez Corral</a>
 * @author <a href="aaa1049@alu.ubu.es">Alejandro Abad Arroyo</a>
 * 
 * @since 2026-04-18
 * 
 * @param <K> tipo de las claves
 * @param <V> tipo de los valores
 */
public class MapaDispersionAbierta<K, V> extends AbstractMap<K, V> {

	/** Tamaño máximo de cada cubeta. */
	private int tamanoCubeta;
	/** Número actual de cubetas del mapa. */
	private int numeroCubetas;
	/** Número total de parejas guardadas. */
	private int numParesElementos;

	/** Estructura principal donde se guardan las cubetas. */
	private List<EntradaMultiple> tabla;

	/**
	 * Crea un mapa vacío con el tamaño de cubeta y el número de cubetas indicados.
	 *
	 * @param tamanoCubeta cantidad máxima de elementos por cubeta
	 * @param numeroCubetas cantidad inicial de cubetas
	 */
	public MapaDispersionAbierta(int tamanoCubeta, int numeroCubetas) {
		// Guardamos el tamano maximo que tendra cada cubeta.
		this.tamanoCubeta = tamanoCubeta;
		// Guardamos cuantas cubetas se crean al principio.
		this.numeroCubetas = numeroCubetas;
		// Al empezar, el mapa no tiene parejas guardadas.
		this.numParesElementos = 0;

		// Creamos la lista principal con espacio para todas las cubetas.
		this.tabla = new ArrayList<>(this.numeroCubetas);
		// Rellenamos la tabla con cubetas vacias.
		for (int i = 0; i < numeroCubetas; i++) {
			// Aniadimos una nueva cubeta en cada posicion.
			this.tabla.add(new EntradaMultiple());
		}
	}

	/**
	 * Devuelve cuántas cubetas tiene ahora mismo el mapa.
	 *
	 * @return número de cubetas
	 */
	public int getNumeroCubetas() {
		return numeroCubetas;
	}

	/**
	 * Devuelve todas las entradas del mapa en forma de conjunto.
	 *
	 * @return conjunto con todas las parejas clave-valor
	 */
	@Override
	public Set<Entry<K, V>> entrySet() {

		// Conjunto donde se iran metiendo todas las entradas del mapa.
		Set<Entry<K, V>> conjuntoFinal = new java.util.HashSet<>();

		// Recorremos cubeta por cubeta.
		for (EntradaMultiple cubeta : tabla) {

			// Recorremos cada posicion de la cubeta actual.
			for (int i = 0; i < cubeta.claves.size(); i++) {

				// Cogemos la clave guardada en esa posicion.
				K clave = cubeta.claves.get(i);
				// Cogemos el valor que va con esa clave.
				V valor = cubeta.valores.get(i);

				// Construimos la entrada clave-valor para el conjunto final.
				SimpleEntry parDeValores = new SimpleEntry(clave, valor);

				// Aniadimos la entrada al conjunto de salida.
				conjuntoFinal.add(parDeValores);
			}
		}

		// Devolvemos todas las entradas reunidas.
		return conjuntoFinal;
	}

	/**
	 * Añade una pareja clave-valor o actualiza el valor si la clave ya existe.
	 *
	 * @param key clave a guardar
	 * @param value valor asociado a la clave
	 * @return valor anterior si la clave ya estaba, o null si es nueva
	 */
	public V put(K key, V value) {

		// Calculamos en que cubeta cae la clave.
		int indice = Math.abs(key.hashCode()) % this.numeroCubetas;
		// Recuperamos la cubeta correspondiente.
		EntradaMultiple cubeta = this.tabla.get(indice);
		// Buscamos si la clave ya estaba guardada en esa cubeta.
		int posicionEnLista = cubeta.claves.indexOf(key);

		// Si la clave ya existia...
		if (posicionEnLista != -1) {

			// Si la clave ya existe, solo se sustituye su valor.
			V valorAntiguo = cubeta.valores.get(posicionEnLista);

			// Guardamos el nuevo valor en la misma posicion.
			cubeta.valores.set(posicionEnLista, value);

			// Devolvemos el valor que tenia antes.
			return valorAntiguo;

		} else {

			// Si la clave no estaba, comprobamos si la cubeta esta llena.
			if (cubeta.claves.size() == this.tamanoCubeta) {

				// Si la cubeta se llena, se amplía la tabla y se recalcula el índice.
				redimensionarTabla();
				// Recalculamos el indice con el nuevo numero de cubetas.
				indice = key.hashCode() % this.numeroCubetas;
				// Recuperamos la nueva cubeta de destino.
				cubeta = this.tabla.get(indice);

			}

			// Anadimos la clave al final de su cubeta.
			cubeta.claves.add(key);
			// Anadimos el valor en la misma posicion que la clave.
			cubeta.valores.add(value);
			// Aumentamos el contador de parejas del mapa.
			numParesElementos++;

			// Como era una clave nueva, no habia valor anterior.
			return null;
		}

	}

	/**
	 * Aumenta el número de cubetas y recoloca todos los elementos.
	 */
	private void redimensionarTabla() {

		// Calculamos el nuevo numero de cubetas (50% mas).
		int nuevoNumeroCubetas = (int) (numeroCubetas * 1.5);
		// Guardamos una referencia a la tabla vieja para no perder datos.
		List<EntradaMultiple> tablaAntigua = this.tabla;

		// Actualizamos el numero de cubetas a su nuevo valor.
		this.numeroCubetas = nuevoNumeroCubetas;
		// Creamos una tabla vacia con el nuevo tamano.
		this.tabla = new ArrayList<>(this.numeroCubetas);
		// Rellenamos la nueva tabla con cubetas vacias.
		for (int i = 0; i < numeroCubetas; i++) {
			// Anadimos una cubeta en cada posicion.
			this.tabla.add(new EntradaMultiple());
		}
		// Reiniciamos el contador porque se van a reinsertar todos los pares.
		this.numParesElementos = 0;

		// Recorremos las cubetas antiguas para recuperar sus datos.
		for (EntradaMultiple cubeta : tablaAntigua) {
			// Recorremos todas las posiciones usadas de cada cubeta.
			for (int i = 0; i < cubeta.claves.size(); i++) {
				// Reinsertar garantiza que cada clave vaya a su nueva cubeta correcta.
				this.put(cubeta.claves.get(i), cubeta.valores.get(i));
			}
		}

	}

	/**
	 * Elimina la pareja asociada a una clave.
	 *
	 * @param key clave que se quiere borrar
	 * @return valor eliminado, o null si la clave no estaba
	 */
	public V remove(Object key) {

		// Calculamos la cubeta donde deberia estar la clave.
		int indice = Math.abs(key.hashCode()) % numeroCubetas;
		// Variable para guardar el valor borrado (si existe).
		V valorEliminado = null;
		// Recuperamos la cubeta que toca revisar.
		EntradaMultiple cubeta = tabla.get(indice);

		// Recorremos las claves de la cubeta.
		for (int i = 0; i < cubeta.claves.size(); i++) {
			// Leemos la clave actual.
			K claveActual = cubeta.claves.get(i);

			// Si coincide con la clave buscada...
			if (claveActual.equals(key)) {

				// Guardamos el valor para devolverlo al final.
				valorEliminado = cubeta.valores.get(i);

				// Borramos la clave de esa posicion.
				cubeta.claves.remove(i);
				// Borramos tambien el valor emparejado.
				cubeta.valores.remove(i);

				// Reducimos el numero total de parejas guardadas.
				numParesElementos--;
			}
		}

		// Devolvemos el valor eliminado, o null si no habia clave.
		return valorEliminado;
	}

	/**
	 * Busca el valor guardado para una clave.
	 *
	 * @param key clave a consultar
	 * @return valor encontrado, o null si no existe
	 */
	public V get(Object key) {

		// Calculamos la cubeta donde deberia encontrarse la clave.
		int indice = Math.abs(key.hashCode()) % numeroCubetas;
		// Variable para el resultado (empieza en null).
		V valorObtenido = null;
		// Recuperamos la cubeta que corresponde al indice calculado.
		EntradaMultiple cubeta = tabla.get(indice);

		// Revisamos una a una las claves de esa cubeta.
		for (int i = 0; i < cubeta.claves.size(); i++) {
			// Se busca la clave y, si aparece, se devuelve su valor asociado.
			if (cubeta.claves.get(i).equals(key)) {
				// Si coincide, guardamos el valor encontrado.
				valorObtenido = cubeta.valores.get(i);
			}
		}

		// Devolvemos el valor encontrado o null si no estaba.
		return valorObtenido;

	}

	/**
	 * Cubeta interna que guarda varias claves y sus valores en paralelo.
	 */
	private class EntradaMultiple {
		/** Lista de claves de la cubeta. */
		// Se reserva capacidad inicial igual al tamano de cubeta.
		List<K> claves = new ArrayList<K>(tamanoCubeta);
		/** Lista de valores de la cubeta. */
		// Esta lista siempre va en paralelo con la lista de claves.
		List<V> valores = new ArrayList<V>(tamanoCubeta);
	}

	/**
	 * Entrada sencilla de clave y valor para construir el conjunto final.
	 */
	private class SimpleEntry extends AbstractMap.SimpleEntry<K, V> {

		/**
		 * Crea una entrada con su clave y su valor.
		 *
		 * @param key clave de la entrada
		 * @param value valor de la entrada
		 */
		public SimpleEntry(K key, V value) {
			// Delega en la clase base para guardar la pareja clave-valor.
			super(key, value);
		}
	}

}
