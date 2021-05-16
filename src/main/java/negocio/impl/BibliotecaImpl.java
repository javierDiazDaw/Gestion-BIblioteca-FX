package negocio.impl;

import java.util.ArrayList;
import java.util.List;

import XML.ClaseXML;
import negocio.BibliotecaService;
import negocio.modal.Genero;
import negocio.modal.Libro;

public class BibliotecaImpl implements BibliotecaService {

	private static BibliotecaImpl program = null;
	private static List<Libro> catalogo;

	/**
	 * Contructor que no recibe parametros y solo es usada en esta clase
	 */
	private BibliotecaImpl() {
		catalogo = new ArrayList<Libro>();

		Libro libro1 = new Libro("IT", "9781231231234", Genero.NOVELA, "Stephen King", 1300);
		Libro libro2 = new Libro("La isla del tesoro", "9781869345867", Genero.FICCION, "Pedro", 200);
		Libro libro3 = new Libro("Platero y yo", "9781276849034", Genero.POESIA, "Juan Ramon Jimenez", 150);

		catalogo.add(libro1);
		catalogo.add(libro2);
		catalogo.add(libro3);
	}

	/**
	 * Metodo que devuelve la instancia
	 * @return program
	 */
	public static BibliotecaImpl getInstance() {
		if (program == null)
			program = new BibliotecaImpl();
		return program;

	}

	// Metodo que devuelve el catalogo para pasarlo al MainController
	public List<Libro> getCatalogo() {
		return catalogo;
	}

	public boolean nuevo(Libro libro) {

		return catalogo.add(libro);

	}

	public boolean editar(Libro libro) {

		
		boolean devolver = false;
		for (Libro l : catalogo) {
			int pos = catalogo.indexOf(l);
			catalogo.set(pos, libro);
			devolver = true;
			break;
		}
		return devolver;

	}

	public boolean eliminar(Libro libro) {

		return catalogo.remove(libro);

	}

	@Override
	public boolean salvar(String fichero) {
		boolean retorno = false;

		ClaseXML.guardarXML(fichero);

		return retorno;
	}

	@Override
	public boolean cargar(String fichero) {
		boolean retorno = false;

		ClaseXML.cargarXML(fichero);

		return retorno;
	}

}
