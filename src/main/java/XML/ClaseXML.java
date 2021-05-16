package XML;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import negocio.BibliotecaService;
import negocio.impl.BibliotecaImpl;
import negocio.modal.Genero;
import negocio.modal.Libro;

/**
 * Clase que guarda y carga un XML a la Gestion de Biblioteca
 * @author javiq
 *
 */
public class ClaseXML {

	private static BibliotecaService programa = BibliotecaImpl.getInstance();

	// creacion de una lista de tipo libro

	private static List<Libro> listaLibro;

	/**
	 * Guarda el archivo xml creado
	 * 
	 * @param nombre_archivo
	 */
	public static void guardarXML(String nombre_archivo) {

		listaLibro = programa.getCatalogo();

		ArrayList<String> titulo = new ArrayList<>();
		ArrayList<String> isbn = new ArrayList<>();
		ArrayList<String> genero = new ArrayList<>();
		ArrayList<String> autor = new ArrayList<>();
		ArrayList<String> paginas = new ArrayList<>();

		for (int i = 0; i < listaLibro.size(); i++) {
			titulo.add(listaLibro.get(i).getTitulo());
			isbn.add(listaLibro.get(i).getIsbn());
			genero.add(listaLibro.get(i).getGenero().toString());
			autor.add(listaLibro.get(i).getAutor());
			paginas.add(Integer.toString(listaLibro.get(i).getPaginas()));

		}
		try {
			generate(nombre_archivo, titulo, isbn, genero, autor, paginas);
		} catch (Exception e) {
		}

	}

	public static void generate(String name, ArrayList<String> titulo, ArrayList<String> isbn, ArrayList<String> genero,
			ArrayList<String> autor, ArrayList<String> paginas) throws Exception {

		if (name.isEmpty() || titulo.isEmpty() || isbn.isEmpty() || genero.size() != autor.size()) {
			System.out.println("ERROR ArrayList vacio");
			return;
		} else {

			/**
			 * Las clases DocumentBuilderFactory y DocumentBuilder generan un documento XML
			 * almacenándolo en memoria. Para ello se debe utilizar el método newDocument().
			 */
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation implementation = builder.getDOMImplementation();
			Document document = implementation.createDocument(null, name, null);
			document.setXmlVersion("1.0");

			//Metodo de la clase Document para obtener informacion
			Element raiz = document.getDocumentElement();
			
			// Dentro del bucle añadimos en el XML los valores de cada libro
			for (int i = 0; i < titulo.size(); i++) {
				
				//Crea el elemento libro
				Element itemLibro = document.createElement("libro");
				
				/**
				 * Crea los elementos de las caracteríaticas del libro 
				 * con los valores asignados
				 */
				Element tituloNode = document.createElement("titulo");
				Text nodeTituloValue = document.createTextNode(titulo.get(i));
				tituloNode.appendChild(nodeTituloValue);
				
				Element isbnNode = document.createElement("isbn");
				Text nodeIsbnValue = document.createTextNode(isbn.get(i));
				isbnNode.appendChild(nodeIsbnValue);
				
				Element generoNode = document.createElement("genero");
				Text nodeGeneroValue = document.createTextNode(genero.get(i));
				generoNode.appendChild(nodeGeneroValue);
				
				Element autorNode = document.createElement("autor");
				Text nodeAutorValue = document.createTextNode(autor.get(i));
				autorNode.appendChild(nodeAutorValue);
				
				Element paginasNode = document.createElement("paginas");
				Text nodePaginasValue = document.createTextNode(paginas.get(i));
				paginasNode.appendChild(nodePaginasValue);

				
				// inserta un nuevo nodo dentro del ItemNode
				itemLibro.appendChild(tituloNode);
				itemLibro.appendChild(isbnNode);
				itemLibro.appendChild(generoNode);
				itemLibro.appendChild(autorNode);
				itemLibro.appendChild(paginasNode);
				
				// agrega itemNode a la raiz "Documento"
				raiz.appendChild(itemLibro); 
			}
			
			/**
			 * Como fuente XML se usa la clase DOMSource, que se puede instanciar 
			 * pasándole nuestro Document
			 * 
			 * Como destino de la transformación, usaremos un StreamResult, 
			 * al que pasaremos un archivo con el nombre que le hayamos asignado.
			 */
			Source source = new DOMSource(document);
			Result result = new StreamResult(new java.io.File(name + ".xml")); 
			
			
			
			/**
			 * Las clases Transformer y TransformerFactory transforman una fuente
			 * XML (Document) en un fichero XML.
			 */
			
			// Obtención del TransfomerFactory y del Transformer a partir de él.
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			// // Se realiza la transformación, de Document a Fichero.
			transformer.transform(source, result);
		}

	}

	/**
	 * Introduce un archivo XML en el catalogo
	 * @param nombre_archivo
	 */
	public static void cargarXML(String nombre_archivo) {

		try {
			
			/**
			 * Las clases DocumentBuilderFactory y DocumentBuilder generan un documento XML
			 * almacenándolo en memoria todo el contenido. 
			 * Para ello se debe utilizar el método newDocument().
			 */
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Se obtiene el documento, a partir del XML
			Document documento = builder.parse(new File(nombre_archivo + ".xml"));

			// Se coge la etiqueta libro del documento
			NodeList cargaXML = documento.getElementsByTagName("libro");

			// Recorre la etiqueta
			for (int i = 0; i < cargaXML.getLength(); i++) {

				// Cojo el nodo actual
				Node nodo = cargaXML.item(i);

				// Compruebo si el nodo es un elemento

				if (nodo.getNodeType() == Node.ELEMENT_NODE) {

					// Se transforma a Element
					Element element = (Element) nodo;

					String titulo = element.getElementsByTagName("titulo").item(0).getTextContent();
					String isbn = element.getElementsByTagName("isbn").item(0).getTextContent();
					Genero genero = Genero.getGenero(element.getElementsByTagName("genero").item(0).getTextContent());
					String autor = element.getElementsByTagName("autor").item(0).getTextContent();
					int paginas = Integer.parseInt(element.getElementsByTagName("paginas").item(0).getTextContent());

					Libro libro = new Libro(titulo, isbn, genero, autor, paginas);
					programa.nuevo(libro);
				}

			}

		} catch (ParserConfigurationException | SAXException | IOException ex) {
			System.out.println(((Throwable) ex).getMessage());
		}
	}

}
