/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package comp;

import java.util.Hashtable;

import ast.TypeCianetoClass;
import ast.Variable;


public class SymbolTable {
	private Hashtable<String, TypeCianetoClass> classesTable;
    private Hashtable<String, Object> methodsTable;
    private Hashtable<String, Variable> attributesTable;
    private Hashtable<String, Variable> variablesTable;

    public SymbolTable() {
        this.classesTable = new Hashtable<String, TypeCianetoClass>();
        this.methodsTable = new Hashtable<String, Object>();
        this.attributesTable = new Hashtable<String, Variable>();
        this.variablesTable = new Hashtable<String, Variable>();
    }

    // Limpa tabela de variáveis locais

    public void resetAttibutes(){
        this.attributesTable.clear();
    }

    public void resetVariables(){
        this.variablesTable.clear();
    }

    public void resetMethods() {
        this.methodsTable.clear();
    }

    public TypeCianetoClass returnClass(String chave) {
        return this.classesTable.get(chave);
    }

    public Object returnMethod(String chave) {
        return this.methodsTable.get(chave);
    }
    
    public Variable returnAttribute(String chave) {
        return this.attributesTable.get(chave);
    }
    
    public Variable returnVariable(String chave) {
        return this.variablesTable.get(chave);
    }

    public void putClass(String chave, TypeCianetoClass valor) {
        this.classesTable.put(chave, valor);
    }

    public void removeClass(String chave) {
        this.classesTable.remove(chave);
    }

    public void putMethod(String chave, Object valor) {
        this.methodsTable.put(chave, valor);
    }
    
    public void putAttribute(String chave, Variable valor) {
        this.attributesTable.put(chave, valor);
    }
    
    public void putVariable(String chave, Variable valor) {
        this.variablesTable.put(chave, valor);
    }
}
