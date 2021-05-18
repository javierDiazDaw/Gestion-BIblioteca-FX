package gui.viewsandcontrollers.main;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import gui.Notifications;
import gui.viewsandcontrollers.f.FController;
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
	
	/**
	 * La capa de negocio expone la lógica necesaria a la capa de presentación
	 * para que el usuario a través de la interfaz interactúe con las funcionalidades
	 * de la aplicación.
	 */

	private static BibliotecaService programa = BibliotecaImpl.getInstance();

	/**
	 * Variables de la clase
	 */

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

	/**
	 * Realizar un seguimiento de los cambios
	 * Es una interfaz de lista de JavaFX, y se usa FXCollections
	 * para equipar una lista con la funcionalidad adicional.
	 */
	private static ObservableList<Libro> lista = FXCollections.observableArrayList(programa.getCatalogo());

	// Metodo que arranca el controlador

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		/**
		 * Cada variable llama a PropertyValueFactory que crea una nueva propiedad con el mismo nombre
		 */

		titulo.setCellValueFactory(new PropertyValueFactory<Libro, String>("titulo"));
		isbn.setCellValueFactory(new PropertyValueFactory<Libro, String>("isbn"));
		genero.setCellValueFactory(new PropertyValueFactory<Libro, Genero>("genero"));
		autor.setCellValueFactory(new PropertyValueFactory<Libro, String>("autor"));
		paginas.setCellValueFactory(new PropertyValueFactory<Libro, Integer>("paginas"));

		/**
		 * Desactiva los botones de guardar y editar a menos que esté seleccionado un libro con el cursor
		 */
		
		buttonEditar.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
		buttonBorrar.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());

		Notifications.subscribe(Notifications.CATALOGO_UPDATED, this, this::actualizarCatalogo);

		//Muestra los libros en la Gestion de Biblioteca
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

		
		/**
		 * Genera una nueva ventana
		 */
		fxmlLoader.setController(fcontroller);
		Parent root1 = fxmlLoader.load();
		stage.setScene(new Scene(root1));
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(((Node) event.getSource()).getScene().getWindow());
		stage.showAndWait();
	}

	/**
	 * Consiste en editar un libro ya creado anteriormente seleccionandolo
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void buttonEditar(ActionEvent event) throws IOException {
		Libro libroEditar = tableView.getSelectionModel().getSelectedItem();
		formAction(event, libroEditar);
	}

	/**
	 * Elimina el libro seleccionado por el usuario
	 * 
	 * @param event
	 * @throws IOException
	 */
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

	/**
	 * Guarda el catalogo en un fichero XML
	 * 
	 * @param event
	 * @throws IOException
	 */
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

	/**
	 * Introduce al catalogo un fichero XML creado anteriormente
	 * 
	 * @param event
	 * @throws IOException
	 */
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
	 * Actualiza el catalogo con lo nuevo añadido
	 * 
	 * @param event
	 */
	private void actualizarCatalogo(String event) {
		lista = FXCollections.observableArrayList(programa.getCatalogo());
		tableView.setItems(lista);
		tableView.refresh();
	}

	public static ObservableList<Libro> getCatalogo() {
		return lista;
	}

}
