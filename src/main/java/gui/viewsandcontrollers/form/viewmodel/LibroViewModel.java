package gui.viewsandcontrollers.form.viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Pair;
import negocio.BibliotecaService;
import negocio.impl.BibliotecaImpl;
import negocio.modal.Genero;

/**
 * Clase compatible con JavaFX y ObservableList
 * 
 * @author javiq
 *
 */
public class LibroViewModel {

	//Negocio
	private BibliotecaService negocio = BibliotecaImpl.getInstance();

	private StringProperty titulo = new SimpleStringProperty();
	private StringProperty isbn = new SimpleStringProperty();
	private StringProperty autor = new SimpleStringProperty();
	private ObjectProperty<Pair<String, String>> genero = new SimpleObjectProperty();
	private IntegerProperty paginas = new SimpleIntegerProperty();

	//Constructor LibroViewModel
	public LibroViewModel() {
		genero.setValue(new Pair<String, String>(Genero.NOVELA.toString(), Genero.NOVELA.toString()));
	}

	//Constructor sobrecargado LibroViewModel
	public LibroViewModel(String titulo, String isbn, String genero, String autor, int paginas) {
		setTitulo(titulo);
		setIsbn(isbn);
		setGenero(new Pair<String, String>(genero, genero));
		setAutor(autor);
		setPaginas(paginas);
	}

	public String getTitulo() {
		return titulo.get();
	}

	public String getIsbn() {
		return isbn.get();
	}

	public Pair<String, String> getGenero() {
		return genero.get();
	}

	public String getAutor() {
		return autor.get();
	}

	public Integer getPaginas() {
		return paginas.get();
	}

	public void setTitulo(String titulo) {
		this.titulo.set(titulo);
	}

	public void setIsbn(String isbn) {
		this.isbn.set(isbn);
	}

	public void setAutor(String autor) {
		this.autor.set(autor);
	}

	public void setGenero(Pair<String, String> genero) {
		this.genero.set(genero);
	}

	public void setPaginas(Integer paginas) {
		this.paginas.set(paginas);
	}

	public StringProperty isbnProperty() {
		return isbn;
	}

	public ObjectProperty<Pair<String, String>> generoProperty() {
		return genero;
	}

	public StringProperty autorProperty() {
		return autor;
	}

	public IntegerProperty paginasProperty() {
		return paginas;
	}

	public StringProperty tituloProperty() {
		return titulo;
	}

	public boolean create() {

		return negocio.nuevo(LibroConverter.toLibro(this));
	}

	public boolean update() {

		return negocio.editar(LibroConverter.toLibro(this));
	}

}
