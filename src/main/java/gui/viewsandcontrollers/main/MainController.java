package gui.viewsandcontrollers.main;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import XML.ClaseXML;
import gui.Notifications;
import gui.viewsandcontrollers.f.FController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import negocio.BibliotecaService;
import negocio.impl.BibliotecaImpl;
import negocio.modal.Genero;
import negocio.modal.Libro;

public class MainController implements Initializable {

	@FXML
	private TableView<Libro> tableView;

	@FXML
	private TableColumn<Libro, String> titulo;

	@FXML
	private TableColumn<Libro, String> isbn;

	@FXML
	private TableColumn<Libro, Genero> genero;

	@FXML
	private TableColumn<Libro, String> autor;

	@FXML
	private TableColumn<Libro, Integer> paginas;

	@FXML
	private Button buttonNuevo;

	@FXML
	private Button buttonEditar;

	@FXML
	private Button buttonBorrar;

	@FXML
	private Button buttonGuardar;

	@FXML
	private Button buttonCargar;

	private StringProperty texto = new SimpleStringProperty();

	private static BibliotecaService programa = BibliotecaImpl.getInstance();

	private static ObservableList<Libro> lista = FXCollections.observableArrayList(programa.getCatalogo());

	// Metodo que arranca el controlador

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		titulo.setCellValueFactory(new PropertyValueFactory<Libro, String>("titulo"));
		isbn.setCellValueFactory(new PropertyValueFactory<Libro, String>("isbn"));
		genero.setCellValueFactory(new PropertyValueFactory<Libro, Genero>("genero"));
		autor.setCellValueFactory(new PropertyValueFactory<Libro, String>("autor"));
		paginas.setCellValueFactory(new PropertyValueFactory<Libro, Integer>("paginas"));

		buttonEditar.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
		buttonBorrar.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());

		Notifications.subscribe(Notifications.CATALOGO_UPDATED, this, this::update);

		tableView.setItems(lista);
	}

	/**
	 * Llama al formulario para la creacion de un libro nuevo
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void buttonNuevo(ActionEvent event) throws IOException {

		formAction(event, null);

	}

	/**
	 * Momento en el que se abre el formulario
	 * 
	 * @param event
	 * @param libro
	 * @throws IOException
	 */
	private void formAction(ActionEvent event, Libro libro) throws IOException {

		Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../f/Formulario.fxml"));

		FController fcontroller;

		if (libro == null) {
			fcontroller = new FController();
		} else {
			fcontroller = new FController(libro);
		}

		fxmlLoader.setController(fcontroller);
		Parent root1 = fxmlLoader.load();
		stage.setScene(new Scene(root1));
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(((Node) event.getSource()).getScene().getWindow());
		stage.showAndWait();
	}

	/**
	 * Consiste en editar un libro ya creado anteriormente
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void buttonEditar(ActionEvent event) throws IOException {
		Libro libroEditar = tableView.getSelectionModel().getSelectedItem();
		formAction(event, libroEditar);

//		formAction(event, null);
	}

	@FXML
	private void buttonBorrar(ActionEvent event) throws IOException {

		Alert warning = new Alert(Alert.AlertType.CONFIRMATION);
		warning.setTitle("Borrar los datos de forma permanente");
		warning.setContentText("¿Quiere eliminar los datos?");

		Optional<ButtonType> botonRes = warning.showAndWait();

		if (botonRes.get() == ButtonType.OK) {
			programa.eliminar(tableView.getSelectionModel().getSelectedItem());
			Notifications.publish(Notifications.CATALOGO_UPDATED);
		}

	}

	@FXML
	private void buttonGuardar(ActionEvent event) throws IOException {

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Fichero XML");
		dialog.setContentText("Escribe el nombre del archivo:");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			programa.salvar(result.get());
		}

	}

	@FXML
	private void buttonCargar(ActionEvent event) throws IOException {

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Fichero XML");
		dialog.setContentText("Escribe el nombre del archivo:");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			programa.cargar(result.get());

			Notifications.publish(Notifications.CATALOGO_UPDATED);
		}

	}

	/**
	 * Actualiza el catálogo con lo nuevo añadido
	 * 
	 * @param event
	 */
	private void update(String event) {
		lista = FXCollections.observableArrayList(programa.getCatalogo());
		tableView.setItems(lista);
		tableView.refresh();
	}

	public static ObservableList<Libro> getCatalogo() {
		return lista;
	}

}
