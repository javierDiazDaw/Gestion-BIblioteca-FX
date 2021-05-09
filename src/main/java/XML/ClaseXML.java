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

import negocio.impl.BibliotecaImpl;
import negocio.modal.Genero;
import negocio.modal.Libro;

public class ClaseXML {

	private static BibliotecaImpl programa = BibliotecaImpl.getInstance();
	private static List<Libro> listaLibro;

	public static void getXML(String nombre_archivo) {

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

	public static void cargarXML(String nombre_archivo) {

		try {
			// Creo una instancia de DocumentBuilderFactory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			// Creo un documentBuilder
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Obtengo el documento, a partir del XML
			Document documento = builder.parse(new File(nombre_archivo + ".xml"));

			// Cojo todas las etiquetas coche del documento
			NodeList cargaXML = documento.getElementsByTagName("libro");

			// Recorro las etiquetas
			for (int i = 0; i < cargaXML.getLength(); i++) {
				// Cojo el nodo actual
				Node nodo = cargaXML.item(i);
				// Compruebo si el nodo es un elemento
				if (nodo.getNodeType() == Node.ELEMENT_NODE) {
					// Lo transformo a Element
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

	public static void generate(String name, ArrayList<String> titulo, ArrayList<String> isbn, ArrayList<String> genero,
			ArrayList<String> autor, ArrayList<String> paginas) throws Exception {

		if (name.isEmpty() || titulo.isEmpty() || isbn.isEmpty() || genero.size() != autor.size()) {
			System.out.println("ERROR empty ArrayList");
			return;
		} else {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation implementation = builder.getDOMImplementation();
			Document document = implementation.createDocument(null, name, null);
			document.setXmlVersion("1.0");

			// Main Node
			Element raiz = document.getDocumentElement();
			// Por cada key creamos un item que contendrá la key y el value
			for (int i = 0; i < titulo.size(); i++) {
				// Item Node
				Element itemLibro = document.createElement("libro");
				// Key Node
				Element tituloNode = document.createElement("titulo");
				Text nodeTituloValue = document.createTextNode(titulo.get(i));
				tituloNode.appendChild(nodeTituloValue);
				// Key Node
				Element isbnNode = document.createElement("isbn");
				Text nodeIsbnValue = document.createTextNode(isbn.get(i));
				isbnNode.appendChild(nodeIsbnValue);
				// Key Node
				Element generoNode = document.createElement("genero");
				Text nodeGeneroValue = document.createTextNode(genero.get(i));
				generoNode.appendChild(nodeGeneroValue);
				// Key Node
				Element autorNode = document.createElement("autor");
				Text nodeAutorValue = document.createTextNode(autor.get(i));
				autorNode.appendChild(nodeAutorValue);
				// Key Node
				Element paginasNode = document.createElement("paginas");
				Text nodePaginasValue = document.createTextNode(paginas.get(i));
				paginasNode.appendChild(nodePaginasValue);

				// append keyNode and valueNode to itemNode
				itemLibro.appendChild(tituloNode);
				itemLibro.appendChild(isbnNode);
				itemLibro.appendChild(generoNode);
				itemLibro.appendChild(autorNode);
				itemLibro.appendChild(paginasNode);
				// append itemNode to raiz
				raiz.appendChild(itemLibro); // pegamos el elemento a la raiz "Documento"
			}
			// Generate XML
			Source source = new DOMSource(document);
			// Indicamos donde lo queremos almacenar
			Result result = new StreamResult(new java.io.File(name + ".xml")); // nombre del archivo
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(source, result);
		}

	}

}
