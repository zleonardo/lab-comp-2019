/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package comp;

import java.util.Hashtable;


public class SymbolTable {
	private Hashtable<String, Object> classesTable;
    private Hashtable<String, Object> functionTable;
    private Hashtable<String, Object> variables;

    public SymbolTable() {
        this.classesTable = new Hashtable<String, Object>();
        this.functionTable = new Hashtable<String, Object>();
        this.variables = new Hashtable<String, Object>();
    }

    // Limpa tabela de variáveis locais
    public void resetLocal() {
        this.variables.clear();
    }

    public Object returnClass(String chave) {
        return this.classesTable.get(chave);
    }

    public Object returnFunction(String chave) {
        return this.functionTable.get(chave);
    }
    
    public Object returnVariable(String chave) {
        return this.variables.get(chave);
    }

    public void putClass(String chave, Object valor) {
        this.classesTable.put(chave, valor);
    }

    public void putFunction(String chave, Object valor) {
        this.functionTable.put(chave, valor);
    }
    
    public void putVariable(String chave, Object valor) {
        this.variables.put(chave, valor);
    }
}
