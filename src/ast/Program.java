/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package ast;

import java.util.*;
import comp.CompilationError;

public class Program {

	public Program(ArrayList<TypeCianetoClass> classList, ArrayList<MetaobjectAnnotation> metaobjectCallList, 
			       ArrayList<CompilationError> compilationErrorList) {
		this.classList = classList;
		this.metaobjectCallList = metaobjectCallList;
		this.compilationErrorList = compilationErrorList;
	}

	/**
	the name of the main Java class when the
	code is generated to Java. This name is equal
	to the file name (without extension)
	*/
	private String mainJavaClassName;

	public void setMainJavaClassName(String mainJavaClassName) {
		this.mainJavaClassName = mainJavaClassName;
	}

	public void genJava(PW pw) {
		
		pw.println("import java.io.*;");
		pw.println("import java.util.Scanner;");
		pw.println("");
		
		pw.println("public class " + mainJavaClassName + " {");
		pw.printlnIdent("	public static void main(String [] args) {");
		pw.printlnIdent("		new Program().run();");
		pw.printlnIdent("	}");
		pw.println("}");
		
		for(TypeCianetoClass classe : this.classList ) {
			classe.genJava(pw);
			pw.println("");
		}
	}
	
	public ArrayList<TypeCianetoClass> getClassList() {
		return classList;
	}

	public ArrayList<MetaobjectAnnotation> getMetaobjectCallList() {
		return metaobjectCallList;
	}

	public boolean hasCompilationErrors() {
		return compilationErrorList != null && compilationErrorList.size() > 0 ;
	}

	public ArrayList<CompilationError> getCompilationErrorList() {
		return compilationErrorList;
	}
	
	private ArrayList<TypeCianetoClass> classList;
	private ArrayList<MetaobjectAnnotation> metaobjectCallList;
	ArrayList<CompilationError> compilationErrorList;
}