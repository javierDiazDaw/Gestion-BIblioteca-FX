package gui.viewsandcontrollers.form.viewmodel;

import negocio.modal.Genero;
import negocio.modal.Libro;

/**
 * Clase que convierte un libro normal en un libroViewModel, cambia el tipo del libro
 * @author javiq
 *
 */
public class LibroConverter {

	public static Libro toLibro(LibroViewModel viewModel) {
		return new Libro(
				viewModel.getTitulo(), 
				viewModel.getIsbn(), 
				Genero.getGenero(viewModel.getGenero().getValue()), 
				viewModel.getAutor(), 
				viewModel.getPaginas()
				);
	}

	public static LibroViewModel toLibroVM(Libro libro) { 
		return new LibroViewModel(
				libro.getTitulo(), 
				libro.getIsbn(), 
				libro.getGenero().toString(),
				libro.getAutor(),
				libro.getPaginas().intValue()
				);
	}
}
