package gui.viewsandcontrollers.f;

import java.util.ArrayList;
import java.util.List;

import gui.Notifications;
import gui.viewsandcontrollers.form.viewmodel.LibroConverter;
import gui.viewsandcontrollers.form.viewmodel.LibroViewModel;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import negocio.modal.Genero;
import negocio.modal.Libro;

public class FController {

	//Negocio viewModel
	private LibroViewModel viewModel = new LibroViewModel();

	//Variables FXML
	@FXML
	private TextField txtTitulo;

	@FXML
	private TextField txtIsbn;
	
	@FXML
	private ChoiceBox<Pair<String, String>> choiceGenero = new ChoiceBox<>();

	@FXML
	private TextField txtAutor;

	@FXML
	private TextField txtPaginas;

	@FXML
	private Button buttonGuardar;

	@FXML
	private Button buttonCancelar;

	boolean action;
	

	/**
	 * Constructor sobreCargado
	 * @param libro
	 */
	public FController(Libro libro) {
		action = false;
		viewModel = LibroConverter.toLibroVM(libro);
	}

	/**
	 * Constructor por defecto
	 */
	public FController() {
		action = true;
	}

	public void initialize() {
		initChoice();
		binViewModel();
		if (action == false) {
			txtIsbn.setEditable(false);
		}

	}
	
	/**
	 * valor y clave (lo que muestra)
	 * 
	 * se recoge el key
	 */

	private void initChoice() {

		List<Pair<String, String>> opciones = new ArrayList<>();
		opciones.add(new Pair<String, String>(Genero.FICCION.toString(), Genero.FICCION.toString()));
		opciones.add(new Pair<String, String>(Genero.NOVELA.toString(), Genero.NOVELA.toString()));
		opciones.add(new Pair<String, String>(Genero.POESIA.toString(), Genero.POESIA.toString()));

		choiceGenero.setConverter(new StringConverter<Pair<String, String>>() {

			@Override
			public String toString(Pair<String, String> pair) {
				return pair.getKey();
			}

			@Override
			public Pair<String, String> fromString(String string) {
				return null;
			}
		});

		choiceGenero.getItems().addAll(opciones);

	}

	/**
	 * Introduce la informacion que rellena el usuario en las columnas
	 */
	private void binViewModel() {
		txtTitulo.textProperty().bindBidirectional(viewModel.tituloProperty());
		txtIsbn.textProperty().bindBidirectional(viewModel.isbnProperty());
		txtAutor.textProperty().bindBidirectional(viewModel.autorProperty());
		choiceGenero.valueProperty().bindBidirectional(viewModel.generoProperty());
		Bindings.bindBidirectional(txtPaginas.textProperty(), viewModel.paginasProperty(), new NumberStringConverter());

	}

	@FXML
	private void guardar(ActionEvent event) throws Exception {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();

		boolean exito = false;
		if (action) {
			exito = viewModel.create();
		} else {
			exito = viewModel.update();

		}
		stage.close();
		if (exito) {
			((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
			Notifications.publish(Notifications.CATALOGO_UPDATED);
		}

	}

	@FXML
	private void cancelar(ActionEvent event) throws Exception {

		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

}
