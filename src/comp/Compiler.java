/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package comp;

import java.io.PrintWriter;
import java.util.ArrayList;

import ast.*;
import lexer.*;

public class Compiler {

	public Compiler() { }

	// compile must receive an input with an character less than
	// p_input.lenght
	public Program compile(char[] input, PrintWriter outError) {

		ArrayList<CompilationError> compilationErrorList = new ArrayList<>();
		signalError = new ErrorSignaller(outError, compilationErrorList);
		symbolTable = new SymbolTable();
		lexer = new Lexer(input, signalError);
		signalError.setLexer(lexer);

		Program program = null;
		lexer.nextToken();
		program = program(compilationErrorList);
		return program;
	}
	// program := {annot} classDec { { annot } classDec }
	private Program program(ArrayList<CompilationError> compilationErrorList) {
		ArrayList<MetaobjectAnnotation> metaobjectCallList = new ArrayList<>();
		ArrayList<TypeCianetoClass> CianetoClassList = new ArrayList<>();
		Program program;
		boolean thereWasAnError = false;

		while ( lexer.token == Token.CLASS ||
				(lexer.token == Token.ID && lexer.getStringValue().equals("open") ) ||
				lexer.token == Token.ANNOT ) {
			try {
				while ( lexer.token == Token.ANNOT ) {
					metaobjectAnnotation(metaobjectCallList);
				}

				CianetoClassList.add(classDec());
			}
			/*catch( CompilerError e) {
				// if there was an exception, there is a compilation error
				thereWasAnError = true;
				while ( lexer.token != Token.CLASS && lexer.token != Token.EOF ) {
					try {
						next();
					}
					catch ( RuntimeException ee ) {
						e.printStackTrace();
						return program;
					}
				}
			}*/

			catch( CompilerError e) {
		      // if there was an exception, there is a compilation error
		      thereWasAnError = true;
			}

			catch ( RuntimeException e ) {
				e.printStackTrace();
				thereWasAnError = true;
			}

		}
		
		// Verifica se existe uma funcao Program
		try {
			if (symbolTable.returnClass("Program") == null) {
				lexer.lineNumber--;
				error("Source code without a class 'Program'");
			}
		} catch (CompilerError e) {
			thereWasAnError = true;
		}
		
		
		if ( !thereWasAnError && lexer.token != Token.EOF ) {
			try {
				error("End of file expected");
			}
			catch( CompilerError e) {
			}
		}
		program = new Program(CianetoClassList, metaobjectCallList, compilationErrorList);
		return program;
	}

	private void error(String msg) {
		this.signalError.showError(msg);
	}

	private void next() {
		lexer.nextToken();
	}

	private void check(Token shouldBe, String msg) {
		if ( lexer.token != shouldBe ) {
			error(msg);
		}
	}

	/**  parses a metaobject annotation as <code>{@literal @}cep(...)</code> in <br>
     * <code>
     * {@literal @}cep(5, "'class' expected") <br>
     * class Program <br>
     *     func run { } <br>
     * end <br>
     * </code>
     *

	 */
	@SuppressWarnings("incomplete-switch")
	// annot := "@" id [ "(" { annotParam } ")" ]
	private void metaobjectAnnotation(ArrayList<MetaobjectAnnotation> metaobjectAnnotationList) {
		// já leu "@" no program
		// name é o id
		String name = lexer.getMetaobjectName();
		int lineNumber = lexer.getLineNumber();
		lexer.nextToken();
		ArrayList<Object> metaobjectParamList = new ArrayList<>();
		boolean getNextToken = false;
		if ( lexer.token == Token.LEFTPAR ) {
			// metaobject call with parameters
			lexer.nextToken();
			// { annotParam := IntV 
			while ( lexer.token == Token.LITERALINT || lexer.token == Token.LITERALSTRING ||
					lexer.token == Token.ID ) {
				switch ( lexer.token ) {
				case LITERALINT:
					metaobjectParamList.add(lexer.getNumberValue());
					break;
				case LITERALSTRING:
					metaobjectParamList.add(lexer.getLiteralStringValue());
					break;
				case ID:
					metaobjectParamList.add(lexer.getStringValue());
				}
				next();
				if ( lexer.token == Token.COMMA ) {
					next();
				}
				else {
					break;
				}
			}
			if ( lexer.token != Token.RIGHTPAR )
			{
				error("')' expected after annotation with parameters");
			}
			else {
				getNextToken = true;
			}
		}
		switch ( name ) {
		case "nce":
			if ( metaobjectParamList.size() != 0 )
				error("Annotation 'nce' does not take parameters");
			break;
		case "cep":
			if ( metaobjectParamList.size() != 3 && metaobjectParamList.size() != 4 )
				error("Annotation 'cep' takes three or four parameters");
			if ( !( metaobjectParamList.get(0) instanceof Integer)  ) {
				error("The first parameter of annotation 'cep' should be an integer number");
			}
			else {
				int ln = (Integer ) metaobjectParamList.get(0);
				metaobjectParamList.set(0, ln + lineNumber);
			}
			if ( !( metaobjectParamList.get(1) instanceof String) ||  !( metaobjectParamList.get(2) instanceof String) )
				error("The second and third parameters of annotation 'cep' should be literal strings");
			if ( metaobjectParamList.size() >= 4 && !( metaobjectParamList.get(3) instanceof String) )
				error("The fourth parameter of annotation 'cep' should be a literal string");
			break;
		case "annot":
			if ( metaobjectParamList.size() < 2  ) {
				error("Annotation 'annot' takes at least two parameters");
			}
			for ( Object p : metaobjectParamList ) {
				if ( !(p instanceof String) ) {
					error("Annotation 'annot' takes only String parameters");
				}
			}
			if ( ! ((String ) metaobjectParamList.get(0)).equalsIgnoreCase("check") )  {
				error("Annotation 'annot' should have \"check\" as its first parameter");
			}
			break;
		default:
			error("Annotation '" + name + "' is illegal");
		}
		metaobjectAnnotationList.add(new MetaobjectAnnotation(name, metaobjectParamList));
		if ( getNextToken ) lexer.nextToken();
	}

	// classDec := [ "open" ] "class" Id [ "extends" Id] memberList "end"
	private TypeCianetoClass classDec() {
		Boolean	flagOpen = false;
		
		String superClassName;

		if ( lexer.token == Token.ID && lexer.getStringValue().equals("open") ) {
			flagOpen = true;
			next();
		}

		check(Token.CLASS, "'class' expected");
		next();
		
		check(Token.ID, "Identifier expected");
		className = lexer.getStringValue();
		next();

		// Verifica se existe uma classe com o mesmo nome
		if(symbolTable.returnClass(className) != null)
			error("Error: Class " + lexer.getStringValue() + "has already been declared");
		else{
			classObj = new TypeCianetoClass(className);
			
			classObj.setOpen(flagOpen);
			
			if ( lexer.token == Token.EXTENDS ) {
				next();

				check(Token.ID, "Class expected");
				superClassName = lexer.getStringValue();
				
				TypeCianetoClass superClassObj = symbolTable.returnClass(superClassName);
				// verifica se super classe eh ela propria
				if(superClassName.equals(className))
					error("Class '" + className + "' is inheriting from itself");
				// verifica se super classe foi declarada
				else if(superClassObj == null)
					error("Class " + superClassName + " was not found");
				// verifica se super classe eh open
				else if(superClassObj.getOpen() == false)
					error("Class " + superClassName + " is not open");
				
				classObj.setSuperClass(superClassObj);
				
				//Insere a classe incompleta no symboltable 
				symbolTable.putClass(className, classObj);

				next();
			}

			//Insere a classe completa no symboltable
			symbolTable.removeClass(className);
			symbolTable.putClass(className, classObj);
			
			classObj.setMemberList(memberList());

		}
		check(Token.END, "'end' expected");
		next();
		
		if(className.equals("Program")) {
			if(symbolTable.returnMethod("run") == null) {
				lexer.lineNumber--;
				error("Method 'run' was not found in class 'Program'");
			}
		}

		//Limpa a hash das variaveis globais e funcoes
		symbolTable.resetAttibutes();
		//TIREI AQUI PQ PODE SER OVERRIDE
		symbolTable.resetMethods();

		return classObj;
	}

	// memberList := { [ Qualifier ] Member }
	private MemberList memberList() {
		MemberList memberList = new MemberList();
		Method method;
		
		while ( true ) {
			
			String qualifier = qualifier();
			Copiaqualifier = qualifier;
			
			// member := fieldDec | methodDec
			if ( lexer.token == Token.VAR ) {
				if(qualifier != null && qualifier.contains("public"))
					error("Attempt to declare public instance variable");
				memberList.addField(fieldDec());
			}
			else if ( lexer.token == Token.FUNC ) {
				method = methodDec();
				memberList.addMethod(method);
			}
			else {
				break;
			}
		}
		
		return memberList;
	}

	/* qualifier := "private" | "public" | "override" | "override" "public" |
	"final" | "final" "public" | "final" "override" |
	"final" "override" "public" | "shared" "private" | "shared" "public" */
	private String qualifier() {
		if ( lexer.token == Token.PRIVATE ) {
			next();
			return "private";
		}
		else if ( lexer.token == Token.PUBLIC ) {
			next();
			return "public";
		}
		else if ( lexer.token == Token.OVERRIDE ) {
			next();
			if ( lexer.token == Token.PUBLIC ) {
				next();
				return "override public";
			}
			return "override";
		}
		else if ( lexer.token == Token.FINAL ) {
			next();
			if ( lexer.token == Token.PUBLIC ) {
				next();
				return "final public";
			}
			else if ( lexer.token == Token.OVERRIDE ) {
				next();
				if ( lexer.token == Token.PUBLIC ) {
					next();
					return "final override public";
				}
				return "final override";
			}
			return "final";
		}
		else if(lexer.token == Token.SHARED) {
			if (lexer.token == Token.PUBLIC) {
				next();
				return "shared public";
			}
			else if (lexer.token == Token.PRIVATE) {
				next();
				return "shared private";
			}
		}
		else {
			if(lexer.getStringValue().toUpperCase().equals(lexer.getStringValue())) {
				error("'public', 'private' or '}' expected");
			}
		}
		return null;
	}

	// methodDec := "func" IdColon FormalParamDec [ "->" Type ] "{" StatementList"}" | 
	//				"func" Id [ "->" Type ] "{" StatementList"}"
	private Method methodDec() {
		Method method = null;
		Boolean flagReturn = false;
		verificaReturn = false;
		//metodoAtual = null;
		
		// le func
		next();
		
		//Verificar ou atributo com o mesmo nome
		if(symbolTable.returnMethod(lexer.getStringValue()) != null) 
			error("Method '" + lexer.getStringValue() + "' is being redeclared");
		else if(symbolTable.returnAttribute(lexer.getStringValue()) != null) 
			error("Method '" + lexer.getStringValue() + "' has name equal to an instance variable");
		else {
			// VERIFICAR ESSE PRINT AQUI!!! ARQUIVO GER21.ci para verificar func print
			if ( lexer.token == Token.ID || lexer.token == Token.PRINT) {
				// unary method
				method = new Method(lexer.getStringValue(), Copiaqualifier);
				
				if(className.equals("Program")) {
					if(method.getName().equals("run")) {
						if(Copiaqualifier != null && Copiaqualifier.equals("private")) {
							error("Method 'run' of class 'Program' cannot be private");
						}	
					}
				}
				
				symbolTable.putMethod(lexer.getStringValue(), method);
				next();
				
				
				metodoAtual = method;
				
			}
			else if ( lexer.token == Token.IDCOLON ) {
				method = new Method(lexer.getStringValue(), Copiaqualifier);
				symbolTable.putMethod(lexer.getStringValue(), method);
				next();
				
				metodoAtual = method;

				formalParamDec();
				
			}
			else {
				error("An identifier or identifer: was expected after 'func'");
			}
			
			
			if ( lexer.token == Token.MINUS_GT ) {
				// method declared a return type
				
				if(className.equals("Program")) {
					if(method.getName().equals("run")) {
						error("Method 'run' of class 'Program' with a return value type");
					}
				}
				
				next();
				Type s = type();
				method.setType(s);
				metodoAtual = method;
				flagReturn = true;
			}
			
			if(classObj.getSuperClass() != null) {
				TypeCianetoClass superClass = classObj.getSuperClass();
				MethodList methodList = superClass.getMemberList();
				if(methodList != null && method != null) {
					for(int i = 0; i < methodList.getMethodList().size(); i++) {
						if(method.getName().equals(methodList.getMethodList().get(i).getName())){
							if(methodList.getVetor(i).getParametro().size() != method.getParametro().size()) {
								error("Method of the subclass has a signature different from the same method of superclass ");		
							}
							else if(methodList.getVetor(i).getType() != method.getType()) {
								error("Methods have different type of returns");
							}
							for(int j = 0; j < methodList.getMethodList().get(i).getParametro().size(); j++) {
								if(method.getParametro().get(j).getType() != methodList.getMethodList().get(i).getParametro().get(j).getType()) {
									error("Method of the subclass has a signature different from the same method of superclass");
								}
							}
							//System.out.println(methodList.getVetor(i).getScope().contains("override"));
							if(methodList.getVetor(i).getScope() != null && !methodList.getVetor(i).getScope().contains("override")) {
								error("'override' expected before overridden method");
							}
						}
					}
				}
			}
			
			check(Token.LEFTCURBRACKET, "'{' expected");
			next();

			statementList();
			
			// Verificar se há um return quando tem um tipo de retorno declarado
			if(flagReturn == true && verificaReturn == false) {
				check(Token.RETURN, "Missing 'return' statement");
			}
			
			if(flagReturn == false && verificaReturn == true) {
				lexer.lineNumber--;
				check(Token.RETURN, "Illegal 'return' statement. Method returns 'void'");
			}
			
			check(Token.RIGHTCURBRACKET, "'}' expected");
			
		}

		next();
		
		metodoAtual = null;

		symbolTable.resetVariables();
		return method;
	}
	
	// formalParamDec := ParamDec {"," ParamDec }
	private ArrayList<ParamDec> formalParamDec() {
		ArrayList<ParamDec> paramDecList = new ArrayList<ParamDec>();

		ParamDec p = paramDec();
		paramDecList.add(p);
		metodoAtual.addParametro(p);

		while(lexer.token == Token.COMMA){
			// lê a ","
			next();
			p = paramDec();
			paramDecList.add(p);
			metodoAtual.addParametro(p);
		}

		return paramDecList;
	}
	
	// paramDec := Type Id
	private ParamDec paramDec() {
		String id = "";
		Type type = type();
		
		if(metodoAtual.getName().equals("run:") || metodoAtual.getName().equals("run")) {
				error("Method 'run:' of class 'Program' cannot take parameters");
		}

		if(lexer.token != Token.ID){
			error("Id was expected");
		}
		else
			id = lexer.getStringValue();
		next();
		
		Variable param = new Variable(id);
		param.setType(type);
		//Verificar o objeto
		// pra q isso?
		symbolTable.putVariable(id, param);

		return new ParamDec(id, type);
	}
	
	// statementList := { Statement }
	private StatementList statementList() {
		
		StatementList statList = new StatementList();

		while (lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.UNTIL){
			statList.addStat(statement());
		}
		
		metodoAtual.addStat(statList);

		return statList;
	}
	
/* statement := AssignExpr ";" | IfStat | WhileStat | ReturnStat ";" |
                PrintStat ";" | "break" ";" | ";" |
                RepeatStat ";" | LocalDec ";" |
                AssertStat ";" */
	private Statement statement() {
		Statement statement = null;
		boolean checkSemiColon = true;
		
		switch (lexer.token) {
			case ASSIGN:
				statement = assignExpr();
				break;
			case IF:
				statement = ifStat();
				checkSemiColon = false;
				break;
			case WHILE:
				statement = whileStat();
				checkSemiColon = false;
				break;
			case RETURN:
				statement = returnStat();
				break;
			case BREAK:
				statement = breakStat();
				break;
			case SEMICOLON:
				next();
				return new NullStat();
			case REPEAT:
				statement = repeatStat();
				break;
			case VAR:
				statement = localDec();
				break;
			case ASSERT:
				statement = assertStat();
				break;
			default:
				if ( lexer.token == Token.ID && lexer.getStringValue().equals("Out") ) {
					statement = printStat();
				}
				else {
					statement = assignExpr();
				}
		}
		if ( checkSemiColon ) {
			check(Token.SEMICOLON, "';' missing");
			next();
			
		}
		return statement;
	}
	
	// assignExpr := Expr [ "=" Expr]
	private AssignExpr assignExpr() {
		AssignExpr assignExpr = new AssignExpr();
		
		if (lexer.token == Token.ID || lexer.token == Token.SELF || lexer.token == Token.SUPER){
			if(symbolTable.returnAttribute((lexer.getStringValue())) != null && lexer.token != Token.SELF){
				error("");
			}
			if(symbolTable.returnVariable(lexer.getStringValue()) == null) {
				if(lexer.token != Token.SELF && lexer.token != Token.SUPER) {
					error("Variable '" + lexer.getStringValue() + "' was not declared");
				}
			}
			
			assignExpr.setLeftExpr(expr());
        } else {
            error("variable expected at the left-hand side of a assignment");
        }

		if(lexer.token == Token.ASSIGN){
			next();
			assignExpr.setRightExpr(expr());
			
			//TERMINAR!
			if(assignExpr.getLeft().getType() == Type.booleanType) {
				if(assignExpr.getRight().getType() == Type.intType) {
					error("'int' cannot be assigned to 'boolean'");
				}
			}else if(assignExpr.getLeft().getType() == Type.intType) {
				if(assignExpr.getRight().getType() == Type.booleanType) {
					error("Type error: value of the right-hand side is not subtype of the variable of the left-hand side.");
				}else if(assignExpr.getRight().getType() == Type.nullType) {
					error("Type error: 'nil' cannot be assigned to a variable of a basic type");
				}else if(assignExpr.getRight().getType() != Type.intType) {
					error("Type error: value of the right-hand side is not subtype of the variable of the left-hand side.");
				}
			}else if(assignExpr.getLeft().getType() == Type.stringType) {
				
			}//else if(!left.getType().equals(right.getType())) {
				// Classes
				
			//}
				//error("Type error: value of the right-hand side is not subtype of the variable of the left-hand side.");
			//}
		}
		
		return assignExpr;
	}

	// localDec := "var" Type IdList [ "=" Expr ]
	private LocalDecStat localDec() {
		next();
		
		Type type = type();

		IdList idList = idList(type);
		
		LocalDecStat localDec = new LocalDecStat(type);

		// Semantica
		for(int i = 0; i < idList.size(); i++){
			Variable varObj = idList.getVariable(i);
			String varName = varObj.getName();
			
			// Verifica se ja foi declarado
			if(symbolTable.returnVariable(varName) != null) {
				error("Variable '" + varName + "' is being redeclared");
			}
			else
				symbolTable.putVariable(varName, varObj);
		}
		
		localDec.setIdList(idList);

		if ( lexer.token == Token.ASSIGN ) {
			next();
			// check if there is just one variable
			if(idList.size() != 1)
				error("Error: Assign only allowed to one variable");
			localDec.setExpr(expr());
		}

		return localDec;
	}

	
	// idList := Id { "," Id } 
	private IdList idList(Type type) {
		IdList idlist = new IdList();

		String varName = lexer.getStringValue();
		
		//Palavra reservada
		//Precisa verificar se é um numero
		for (Token t : Token.values()) {
	        if (varName.equals(t.name().toLowerCase()) || varName.equals("Boolean") || varName.equals("Int") || varName.equals("String")){
	        	error("Identifier expected");
	        }
	    }
		
		Variable varObj = new Variable(varName);
		varObj.setType(type);
		idlist.add(varObj);
		next();

		while(lexer.token == Token.COMMA){
			next();

			check(Token.ID, "Identifier expected");
			varName = lexer.getStringValue();
			
			//Palavra reservada
			for (Token t : Token.values()) {
		        if (varName.equals(t.name().toLowerCase()) || varName.equals("Boolean") || varName.equals("Int") || varName.equals("String")){
		        	error("Identifier expected");
		        }
		    }
			
			varObj = new Variable(varName);
			varObj.setType(type);
			idlist.add(varObj);
			next();
		}

		return idlist;
	}

	// repeatStat := "repeat" statementList "until" expr
	private RepeatStat repeatStat() {
		RepeatStat repeatStat = null;
		// ja leu "repeat" no statement
		next();

		if(outsideWhileRepeatUntil){
			outsideWhileRepeatUntil = false;
			repeatStat = new RepeatStat(statementList());
			outsideWhileRepeatUntil = true;
		}
		else
			repeatStat = new RepeatStat(statementList());
		
		check(Token.UNTIL, "'until' expected");
		next();

		Expr expr = expr();
		
		// semantica
		if(expr.getType() != Type.booleanType)
			error("boolean expression expected in a repeat-until statement");

		repeatStat.setCondition(expr);

		return repeatStat;
	}

	// breakStat := "break"
	private BreakStat breakStat() {
		// ja leu o "break" no statement
		next();

		// semantica
		if(outsideWhileRepeatUntil)
			error("'break' statement found outside a 'while' or 'repeat-until' statement");

		return new BreakStat();
	}

	// returnStat := "return" expr
	private ReturnStat returnStat() {
		// ja leu o "return" no statement
		verificaReturn = true;
		next();
		
		Expr e = expr();
		
		//System.out.println(e.getType().getName());
		
		return new ReturnStat(e);
	}

	// whileStat := "while" expr "{" StatementList "}"
	private WhileStat whileStat() {
		// ja leu "while" no metodo statement
		next();

		Expr expr = expr();
		WhileStat whileStat = new WhileStat(expr);

		// semantica
		if(expr.getType() != Type.booleanType)
			error("non-boolean expression in 'while' command");

		check(Token.LEFTCURBRACKET, "missing '{' after the 'while' expression");
		next();

		if(outsideWhileRepeatUntil){
			outsideWhileRepeatUntil = false;
			whileStat.setWhilePart(statementList());
			outsideWhileRepeatUntil = true;
		}
		else
			whileStat.setWhilePart(statementList());

		check(Token.RIGHTCURBRACKET, "missing '}' after 'while' body");
		next();

		return whileStat;
	}

	// ifStat := "if" expr "{" Statement "}" [ "else" "{" Statement "}" ]
	private IfStat ifStat(){
		next();
		
		Expr expr = expr();
		IfStat ifStat = new IfStat(expr);

		// semantica
		if(expr.getType() != Type.booleanType)
			error("non-boolean expression in 'if' command");
		
		check(Token.LEFTCURBRACKET, "'{' expected");
		next();
		
		ifStat.setIfPart(statementList());
		
		check(Token.RIGHTCURBRACKET, "'}' was expected");
		
		next();
		
		if ( lexer.token == Token.ELSE ) {
			
			next();
			check(Token.LEFTCURBRACKET, "'{' expected after 'else'");
			next();
			
			ifStat.setElsePart(statementList());

			check(Token.RIGHTCURBRACKET, "'}' was expected");
			next();
		}

		return ifStat;
	}

	// Printstat ::= "Out" "." ( "print:" | "println:" ) expr { "," expr }
	private Out printStat() {
		// lê "Out"
		next();

		check(Token.DOT, "a '.' was expected after 'Out'");
		next();

		check(Token.IDCOLON, "'print:' or 'println:' was expected after 'Out.'");
		// precisa criar a classe Out
		String printName = lexer.getStringValue();
		//System.out.println(printName);
		Out write = new Out(printName);
		next();
		
		ExprList expr = exprList();
		write.setExpr(expr);
		
		for(int i = 0; i < expr.getTamanho(); i++) {
			//System.out.println(expr.getVetor(i));
			if(expr.getVetor(i) == null) {
				error("Command ' Out.print' without arguments");
			}else if(expr.getVetor(i).getType() == Type.booleanType) {
				error("Attempt to print a boolean expression");
			// é classe
			}else if(expr.getVetor(i).getType() != Type.intType && expr.getVetor(i).getType() != Type.stringType && expr.getVetor(i).getType() != Type.nullType) {
				error("Command 'write' does not accept objects");
			}
		}
		
		return write;
	}
	
	// exprList := Expr { "," Expr }
	private ExprList exprList() {
		ExprList exprList = new ExprList();

		exprList.addExpr(expr());

		while(lexer.token == Token.COMMA){
			next();
			
			exprList.addExpr(expr());
		}

		return exprList;
	}

	// expr := SimpleExpression [ Relation SimpleExpr ]
	private Expr expr() {
		// CompositeExpr left = simpleExpr();
		Expr left = simpleExpr();

		switch(lexer.token){
			// relation := "==" | "<" | ">" | "<=" | ">=" | "!=" 
			case EQ:
			case LT:
			case GT:
			case LE:
			case GE:
			case NEQ:
				Token relation = lexer.token;
				next();
				Expr right = simpleExpr();
				
				//System.out.println(left.getType().getName());
				//TypeCianetoClass classe1 = left.getType().getName();
				//System.out.println(right.getType().getName());
				
				//NA HORA DE COMPARAR AS CLASSES TEM QUE VER SE UMA É HERDADA DA OUTRA. TRETA!!!
				
				/*if(relation == Token.EQ || relation == Token.NEQ) {
					if (left.getType().getName() != right.getType().getName()) {
						error("Incompatible types cannot be compared with '" + relation.toString() + "' because the result will always be 'false'");
					}
				}*/
				
				CompositeExpr ce = new CompositeExpr(left,  relation, right);
				ce.setType(Type.booleanType);
				left = ce;

				// semantica
			break;
			default:
				//break;
		}
		
		return left;
	}
	
	// simpleExpr := SumSubExpr { "++" SumSubExpr }
	private Expr simpleExpr() {
		Expr left = sumSubExpr();

		while(lexer.token == Token.PLUSPLUS){
			next();
			Expr right = sumSubExpr();
			CompositeExpr ce = new CompositeExpr(left, Token.PLUSPLUS, right);
			ce.setType(Type.stringType);
			
			// semantica
			if(left.getType() != Type.intType && left.getType() != Type.stringType)
				error("Illegal types with ++, only Int and String are allowed");

			if(right.getType() != Type.intType && right.getType() != Type.stringType)
				error("Illegal types with ++, only Int and String are allowed");

			left = ce;
		}

		return left;
	}
	
	// SumSubExpr := Term { lowOperator Term }
	private Expr sumSubExpr() {
		Expr left = term();

		// lowOperator := "+" | "-" | "||"
		while(lexer.token == Token.PLUS || lexer.token == Token.MINUS || lexer.token == Token.OR){
			Token oper = lexer.token;
			next();

			Expr right = term();

			CompositeExpr ce = new CompositeExpr(left, oper, right);
			
			if(left.getType() != right.getType())
				error("operator '" + oper.toString() + "' of '" + left.getType().getName() + "' expects an '" + right.getType().getName() + "' value");
			else
				ce.setType(left.getType());

			if(left.getType() == Type.booleanType && oper != Token.OR)
				error("type boolean does not support operation '" + oper.toString() + "'");

			if(left.getType() != Type.booleanType && oper == Token.OR)
				error("type" + left.getType().getName() +  " does not support operation '||'");
			if(right.getType() != Type.booleanType && oper == Token.OR)
				error("type" + right.getType().getName() +  " does not support operation '||'");

			left = ce;
		}

		return left;
	}
	
	// term := signalFactor { highOperator signalFactor }
	private Expr term() {
		Expr left = signalFactor();

		// highOperator := "*" | "/" | "&&"
		while(lexer.token == Token.MULT || lexer.token == Token.DIV || lexer.token == Token.AND){
			Token oper = lexer.token;
			next();
			
			Expr right = signalFactor();

			CompositeExpr ce = new CompositeExpr(left,  oper, right);
			
			// semantica
			
			if(left.getType() != right.getType())
				error("operator '" + oper.toString() + "' of '" + left.getType().getName() + "' expects an '" + left.getType().getName() + "' value");
			else
				ce.setType(left.getType());

			if(left.getType() == Type.booleanType && oper != Token.AND)
				error("type boolean does not support operation '" + oper.toString() + "'");
				
			if(left.getType() != Type.booleanType && oper == Token.AND)
				error("type" + left.getType().getName() +  " does not support operation '&&'");
			if(right.getType() != Type.booleanType && oper == Token.AND)
				error("type" + right.getType().getName() +  " does not support operation '&&'");

			left = ce;
		}

		return left;
	}
	
	// signalFactor := [ Signal ] factor
	private Expr signalFactor() {
		// signal := "+" | "-"
		if(lexer.token == Token.PLUS || lexer.token == Token.MINUS){
			Token signal = lexer.token;
			next();
			return new SignalFactor(signal, factor());
		}
		else
			return factor();
	}
	
	// factor := basicValue | "(" Expr ")" | "!" factor | "nil" | objectCreation | primaryExpr 
	private Expr factor() {
		switch (lexer.token) {
			// basicValue := "IntValue" | "BooleanValue" | "StringValue"
            case LITERALINT:
				return literalInt();
            case LITERALSTRING:
				return literalString();
            case TRUE:
				next();
				return LiteralBoolean.True;
            case FALSE:
				next();
				return LiteralBoolean.False;
        
		    case LEFTPAR:
                next();
                Expr expr = new ExprInParentheses(expr());
                check(Token.RIGHTPAR, "')' was expected");
                next();
				return expr;
        
		    case NOT:
				next();
				Expr factor = factor();
				if(factor.getType() != Type.booleanType)
					error("Operator '!' does not accepts '" + factor.getType().toString() + "' values");
					
				return new NegationFactor(factor);
        
		    case NULL:
				return new NullExpr();
            default:
				return primaryExpr();
		}
	}
	
	// objectCreation := Id "." "new"
	// REVISAR
	// joguei pra dentro do primary
	private Expr objectCreation(PrimaryExpr primaryExpr){
		// le new
		
		ObjectCreation obj = new ObjectCreation(primaryExpr.getFirstIdName());
		
		next();

		// semantica
		TypeCianetoClass classObj = symbolTable.returnClass(primaryExpr.getFirstIdName());
		if(classObj == null)
			error("Type " + primaryExpr.getFirstIdName() + "not found");
		else{
			primaryExpr.setFirstIdObj(classObj);
			primaryExpr.setType(classObj);
			primaryExpr.setObject(obj);
		}

		return primaryExpr;
	}
	
	/*  primaryExpr :=	"super" "." Id |
						"super" "." IdColon exprList |
						"self" |
						"self" "." Id |
						"self" "." Id "." Id |
						"self" "." Id "." IdColon exprList |
						"self" "." IdColon exprList |
						Id |
						Id "." Id |
						Id "." IdColon ExprList
	*/	
	private Expr primaryExpr() {
		PrimaryExpr primaryExpr = new PrimaryExpr();
		TypeCianetoClass classObj;
		Variable variableObj;
		Boolean finished = false;

		// escopo
		if(lexer.token == Token.SUPER){
			if(className.equals("Program")) {
				error("'super' used in class 'Program' that does not have a superclas");
			}
			
			primaryExpr.setScope(Token.SUPER);
			next();

			check(Token.DOT, "'.' was expected after 'super'");
			next();
		}
		else if(lexer.token == Token.SELF){
			primaryExpr.setScope(Token.SELF);
			next();
			if(lexer.token != Token.DOT)
				finished = true;
			else
				// le ponto
				next();
		}

		if(lexer.token == Token.IN)
			return readExpr();
		else if(!finished && lexer.token == Token.ID || lexer.token == Token.PRINT){
			primaryExpr.setFirstIdName(lexer.getStringValue());
			Variable ok = symbolTable.returnAttribute(lexer.getStringValue());
			Variable ok1 = symbolTable.returnVariable(lexer.getStringValue());
			if(ok != null) {
				primaryExpr.setType(ok.getType());
			}
			if(ok1 != null) {
				primaryExpr.setType(ok1.getType());
			}
			if(primaryExpr.getScope() == Token.SUPER) {
			}
			next();
			if(lexer.token == Token.DOT) {
				next();
				/*if(ok1 != null || ok != null) {
					error("Message send to a non-object receiver");
				}*/
				if(lexer.token == Token.NEW) {
					return objectCreation(primaryExpr);
				}else if(lexer.token == Token.ID || lexer.token == Token.PRINT) {
					next();
					primaryExpr.setSecondIdName(lexer.getStringValue());
				}else if(lexer.token == Token.IDCOLON) {
					primaryExpr.setSecondIdName(lexer.getStringValue());
					next();
					ExprList expr = exprList();
					primaryExpr.setExprList(expr);
					
					/*for(int i = 0; i < expr.getTamanho(); i++) {
						if(expr.getVetor(i) != null) {
							if(expr.getVetor(i).getType() != Type.booleanType || expr.getVetor(i).getType() != Type.stringType || expr.getVetor(i).getType() != Type.intType) {
								error("Variable not found");
							}
						}
					}*/
					
				}
			}
			finished = true;
		}
		else if(!finished && lexer.token == Token.IDCOLON){
			primaryExpr.setFirstIdName(lexer.getStringValue());
			next();
			primaryExpr.setExprList(exprList());
			
		}
		else{
			//return 
			//error("Expression expected");
			return primaryExpr;
		}

		// Semantica
		// Sem self e super
		if(primaryExpr.getScope() == null){
			// apenas id
			if(primaryExpr.getSecondIdName() == null){
				variableObj = symbolTable.returnVariable(primaryExpr.getFirstIdName());
				if(variableObj == null) {
					if(!primaryExpr.getFirstIdName().equals("nil")) {
						error("Variable " + primaryExpr.getFirstIdName() + " not declared");
					}
				}
				else{
					primaryExpr.setFirstIdObj(variableObj);
					primaryExpr.setType(variableObj.getType());
				}
			}
			// id.id
			else if(primaryExpr.getSecondIdName() != null && primaryExpr.getExprList() == null){
				classObj = symbolTable.returnClass(primaryExpr.getFirstIdName());
				if(classObj == null)
					error("Class " + primaryExpr.getFirstIdName() + " not declared");
				else
					primaryExpr.setFirstIdObj(classObj);
				
				variableObj = classObj.returnField(primaryExpr.getSecondIdName());
				if(variableObj == null) {
					
					if(primaryExpr.getSecondIdName() != "nil") {
					
						error("Variable " + primaryExpr.getSecondIdName() + " not declared");;
					}
				}
				else{
					primaryExpr.setSecondIdObj(variableObj);
					primaryExpr.setType(variableObj.getType());
				}
			}
		}

		return primaryExpr;
	}
	
	// fieldDec := "var" Type IdList [ ";" ]
	private ArrayList<Variable> fieldDec() {
		ArrayList<Variable> fieldList = new ArrayList<Variable>();
		// le var
		next();

		Type type = type();
		
		IdList idList = idList(type);
		
		// Semantica
		for(int i = 0; i < idList.size(); i++){
			Variable varObj = idList.getVariable(i);
			String varName = varObj.getName();
			// Verifica se ja foi declarado
			if(symbolTable.returnAttribute(varName) != null)
				error("Variable '" + varName + " is being redeclared");
			else{
				symbolTable.putAttribute(varName, varObj);
				fieldList.add(varObj);
			}
		}

		if(lexer.token == Token.SEMICOLON) {
			next();
		}

		return fieldList;
	}

	// type := BasicType | Id
	private Type type() {
		Type type = null;
		if ( lexer.token == Token.INT || lexer.token == Token.BOOLEAN || lexer.token == Token.STRING ) {
			type = basicType();
			next();
		}
		else if ( lexer.token == Token.ID ) {
			type = symbolTable.returnClass(lexer.getStringValue());
			if(type == null) {
				error("Type '" + lexer.getStringValue() + "' was not found");
			}
			next();
		}

		if(type == null)
			this.error("Type '" + lexer.getStringValue() + "' was not found");
		
		return type;
	}
	
	// basicType := "Int" | "Boolean" | "String"
	private Type basicType() {
        switch (lexer.token) {
            case INT:
				return Type.intType;
            case BOOLEAN:
				return Type.booleanType;
            case STRING:
				return Type.stringType;
            default: 
				this.error("Error: Invalid type! token: " + lexer.token);
				return null;
		}
	}
	
	// asserStat := "assert" expr "," stringvalue
	private Statement assertStat(){
		
		next();
		
		AssertStat assertStat = new AssertStat(expr());
		
		check(Token.COMMA, "',' expected after the expression of the 'assert' statement");
		next();

		check(Token.LITERALSTRING, "A literal string expected after the ',' of the 'assert' statement");
		assertStat.setString(literalString());

		return assertStat;
	}
	
	private LiteralInt literalInt() {
		int value = lexer.getNumberValue();
		//System.out.println(value);
		next();
		return new LiteralInt(value);
	}
	
	private LiteralString literalString() {
		String value = lexer.getLiteralStringValue();
		next();
		return new LiteralString(value);
	}

	// readExpr() := "In" "." ( "readInt" | "readString" )
	private Expr readExpr() {
		// ja verificou in na chamada da funcao
		next();
		
		check(Token.DOT, "'.' is missing after 'In'");
		next();

		if(lexer.token == Token.READINT || lexer.token == Token.READSTRING){
			Token token = lexer.token;
			next();
			return new ReadExpr(token);
		}
		if(lexer.getStringValue() != "") {
			error("Unknown method '" + lexer.getStringValue() + "'");
		}
		else{
			error("Command 'In.' without arguments");
		}
		
		return null;
	}

	private boolean outsideWhileRepeatUntil = true;
	private SymbolTable		symbolTable;
	private Lexer			lexer;
	private ErrorSignaller	signalError;
	private Method metodoAtual;
	private Boolean verificaReturn = false;
	private String className;
	private String Copiaqualifier;
	private TypeCianetoClass classObj = null;

}
