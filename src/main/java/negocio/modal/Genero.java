package negocio.modal;



public enum Genero {
	NOVELA("NOVELA"), POESIA ("POESIA"), FICCION ("FICCION");

	public static Genero getGenero(String genero) {
		Genero seleccionado = null;

		switch (genero.toUpperCase()) {
		case "NOVELA":
			seleccionado = Genero.NOVELA;
			break;
		case "FICCION":
			seleccionado = Genero.FICCION;
			break;
		case "POESIA":
			seleccionado = Genero.POESIA;
			break;
		}

		return seleccionado;
	}
	
	private String value; 
	
	private  Genero(String value) {
		this.value = value;
	}
	
	public String toString() {
		return this.getValue();
	}
	
	public String getValue() {
		return value;
	}
	
	

}
