package ar.edu.um.programacion2.simple.exception;

/**
 * @author Martin
 *
 */
public class MenuNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3095543865324828748L;

	public MenuNotFoundException(Integer id) {
		super("Cannot find Menu " + id);
	}

}
