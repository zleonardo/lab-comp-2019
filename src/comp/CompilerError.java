/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package comp;

public class CompilerError extends RuntimeException {
	public CompilerError(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorMessage() { return errorMessage; }

	private String errorMessage;
	private static final long serialVersionUID = 1L;

}
