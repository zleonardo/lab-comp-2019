/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package comp;

import java.io.PrintWriter;
import java.util.ArrayList;

import ast.AssertStat;
import ast.AssignExpr;
import ast.BreakStat;
import ast.CompositeExpr;
import ast.Expr;
import ast.ExprInParentheses;
import ast.ExprList;
import ast.Field;
import ast.IdList;
import ast.IfStat;
import ast.LiteralBoolean;
import ast.LiteralInt;
import ast.LiteralString;
import ast.LocalDecStat;
import ast.MemberList;
import ast.MetaobjectAnnotation;
import ast.Method;
import ast.NegationFactor;
import ast.NullExpr;
import ast.NullStat;
import ast.ParamDec;
import ast.PrimaryExpr;
import ast.Program;
import ast.ReadExpr;
import ast.RepeatStat;
import ast.ReturnStat;
import ast.SignalFactor;
import ast.Statement;
import ast.StatementList;
import ast.Type;
import ast.TypeCianetoClass;
import ast.WhileStat;
import lexer.Lexer;
import lexer.Token;

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
		MemberList memberList = null;
		TypeCianetoClass classObj = null;
		TypeCianetoClass classExtend = null;

		if ( lexer.token == Token.ID && lexer.getStringValue().equals("open") ) {
			flagOpen = true;
			next();
		}

		check(Token.CLASS, "'class' expected");
		next();
		
		check(Token.ID, "Identifier expected");
		
		// Verifica se existe uma classe com o mesmo nome
		if(symbolTable.returnClass(lexer.getStringValue()) == null) {
			
			classObj = new TypeCianetoClass(lexer.getStringValue());
			classObj.setOpen(flagOpen);
			
			//Insere a classe no symboltable
			symbolTable.putClass(lexer.getStringValue(), classObj);
			
			next();
		
			if ( lexer.token == Token.EXTENDS ) {
				next();

				check(Token.ID, "Identifier expected");
				
				//TEM QUE VERIFICAR SE A CLASSE EXTENDIDA TEM A PALAVRA OPEN EM SUA DECLARACAO
				
				String superclassName = lexer.getStringValue();
				if(symbolTable.returnClass(superclassName) == null) {
					error("Class " + superclassName + " was not found");
				}
				// VERIFICAR ISSO AQUI!!!
				// falta buscar o objeto da super class, e adiciona-lo a classe
				//classObj.setSuperClass(superclassName);
				next();
			}

			memberList = memberList();
			classObj.setMemberList(memberList);
			
		}else {
			error("Error: Class " + lexer.getStringValue() + "has already been declared");
		}
		
		check(Token.END, "'end' expected");
		next();
		
		//Limpa a hash das variaveis locais
		//Sepa que tem que limpar as funcoes. VERIFICAR!
		symbolTable.resetLocal();

		return classObj;
	}

	// memberList := { [ Qualifier ] Member }
	private MemberList memberList() {
		MemberList memberList = new MemberList();
		Method method;
		
		while ( true ) {
			
			String qualifier = qualifier();
			
			// member := fieldDec | methodDec
			if ( lexer.token == Token.VAR ) {
				memberList.addField(fieldDec());
			}
			else if ( lexer.token == Token.FUNC ) {
				method = methodDec();
				// nao sei como tratar casos que nao tem private nem public
				if (qualifier != null && qualifier.contains("private"))
					memberList.addPrivateMethod(method);
				else 
					memberList.addPublicMethod(method);
			}
			else {
				break;
			}
		}
		
		//VERIFICAR SE NA CLASSE PROGRAM TEM A FUNCAO RUN. OBRIGATORIO!!!
		
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
		return null;
	}

	// methodDec := "func" IdColon FormalParamDec [ "->" Type ] "{" StatementList"}" | 
	//				"func" Id [ "->" Type ] "{" StatementList"}"
	private Method methodDec() {
		Method method = null;
		Boolean flagReturn = false;
		//metodoAtual = null;
		
		symbolTable.resetVariables();
		
		// le func
		next();
		
		//Verificar se existe ja uma funcao com o mesmo nome
		if(symbolTable.returnFunction(lexer.getStringValue()) == null) {
		
			// VERIFICAR ESSE PRINT AQUI!!! ARQUIVO GER21.ci para verificar func print
			if ( lexer.token == Token.ID || lexer.token == Token.PRINT) {
				// unary method
				method = new Method(lexer.getStringValue());
				symbolTable.putFunction(lexer.getStringValue(), method);
				next();
				
				metodoAtual = method;
				
			}
			else if ( lexer.token == Token.IDCOLON ) {
				method = new Method(lexer.getStringValue());
				symbolTable.putFunction(lexer.getStringValue(), method);
				next();
				
				metodoAtual = method;

				formalParamDec();
			}
			else {
				error("An identifier or identifer: was expected after 'func'");
			}
			
			if(metodoAtual.getName().equals("run:") || metodoAtual.getName().equals("run")) {
				if(metodoAtual.getParametro() > 0) {
					error("metodo 'run' nao deve tomar parametros");
				}
			}
			
			if ( lexer.token == Token.MINUS_GT ) {
				// method declared a return type
				next();
				Type s = type();
				//metodoAtual.setType(s.get);
			}
			
			
			check(Token.LEFTCURBRACKET, "'{' expected");
			next();

			statementList();
			check(Token.RIGHTCURBRACKET, "'}' expected");
			
			if(flagReturn) {
				
			}
			
			
		}
		else {
			error("Error: Function " + lexer.getStringValue() + " has already been declared");
		}

		symbolTable.resetVariables();

		next();
		
		metodoAtual = null;

		return method;
	}
	
	// formalParamDec := ParamDec {"," ParamDec }
	private ArrayList<ParamDec> formalParamDec() {
		ArrayList<ParamDec> paramDecList = new ArrayList<ParamDec>();

		paramDecList.add(paramDec());

		while(lexer.token == Token.COMMA){
			// lê a ","
			next();
			paramDecList.add(paramDec());
		}

		return paramDecList;
	}
	
	// paramDec := Type Id
	private ParamDec paramDec() {
		String id = "";
		Type type = type();
		

		if(lexer.token != Token.ID){
			error("Id was expected");
		}
		else
			id = lexer.getStringValue();
		next();
		
		metodoAtual.setParametro();
		
		//Verificar o objeto
		symbolTable.putVariable(id, type);

		return new ParamDec(id, type);
	}
	
	// statementList := { Statement }
	private StatementList statementList() {
		StatementList statList = new StatementList();

		while (lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.UNTIL){
			statList.addStat(statement());
		}

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
					printStat();
				}
				else {
					assignExpr();
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
			
			
			//System.out.println(assignExpr.getLeft());
			//System.out.println(assignExpr.getRight());
			
			//System.out.println(assignExpr.getLeft().getType());
			//System.out.println(assignExpr.getRight().getType());
			
			/*if(assignExpr.getLeft().getType() != assignExpr.getRight().getType()) {
				error("Types incompatibles");
			}*/

		}
		
		return assignExpr;
	}

	// localDec := "var" Type IdList [ "=" Expr ]
	private LocalDecStat localDec() {
		
		next();

		LocalDecStat localDec = new LocalDecStat(type());
		
		if(localDec.getType() != Type.stringType && localDec.getType() != Type.intType && localDec.getType() != Type.booleanType) {
			if(symbolTable.returnClass(localDec.getType().getName()) == null) {
				error("Type '" + localDec.getType().getName() + "' was not found");
			}
		}
		

		localDec.setIdList(idList());
		
		if ( lexer.token == Token.ASSIGN ) {
			next();
			// check if there is just one variable
			localDec.setExpr(expr());
		}

		return localDec;
	}
	
	// idList := Id { "," Id } 
	private IdList idList() {
		IdList idlist = new IdList();
		
		//Verifica se ja existe uma variavel
		if(symbolTable.returnVariable(lexer.getStringValue()) == null) {
			check(Token.ID, "Identifier expected");
			idlist.add(lexer.getStringValue());
			symbolTable.putVariable(lexer.getStringValue(), idlist);
			next();

			while ( lexer.token == Token.COMMA ) {
				next();
				if(symbolTable.returnVariable(lexer.getStringValue()) == null) {
					check(Token.ID, "Identifier expected");
					idlist.add(lexer.getStringValue());
					symbolTable.putVariable(lexer.getStringValue(), idlist);
					next();
				}
				else {
					error("Missing identifier");
				}
			}
		}
		else {
			error("Missing identifier");
		}

		return idlist;
	}

	// repeatStat := "repeat" statementList "until" expr
	private RepeatStat repeatStat() {
		// ja leu "repeat" no statement
		next();

		// Esse while o zé colocou, mas nao sei ainda o porque
		// while ( lexer.token != Token.UNTIL && lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
		RepeatStat repeatStat = new RepeatStat(statementList());
		// }
		check(Token.UNTIL, "'until' expected");
		next();

		repeatStat.setCondition(expr());

		return repeatStat;
	}

	// breakStat := "break"
	private BreakStat breakStat() {
		// ja leu o "break" no statement
		next();
		return new BreakStat();
	}

	// returnStat := "return" expr
	private ReturnStat returnStat() {
		// ja leu o "return" no statement
		next();
		return new ReturnStat(expr());
	}

	// whileStat := "while" expr "{" StatementList "}"
	private WhileStat whileStat() {
		// ja leu "while" no metodo statement
		next();

		WhileStat whileStat = new WhileStat(expr());

		check(Token.LEFTCURBRACKET, "missing '{' after the 'while' expression");
		next();

		whileStat.setWhilePart(statementList());

		check(Token.RIGHTCURBRACKET, "missing '}' after 'while' body");
		next();

		return whileStat;
	}

	// ifStat := "if" expr "{" Statement "}" [ "else" "{" Statement "}" ]
	private IfStat ifStat(){
		next();
		
		IfStat ifStat = new IfStat(expr());
		
		check(Token.LEFTCURBRACKET, "'{' expected");
		next();
		
		// nao era pra ser statementlist?
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END && lexer.token != Token.ELSE ) {
			statement();
		}
		
		check(Token.RIGHTCURBRACKET, "'}' was expected");
		
		next();
		
		if ( lexer.token == Token.ELSE ) {
			
			next();
			check(Token.LEFTCURBRACKET, "'{' expected after 'else'");
			next();
			while ( lexer.token != Token.RIGHTCURBRACKET ) {
				statement();
			}
			check(Token.RIGHTCURBRACKET, "'}' was expected");
			next();
		}
		

		return ifStat;
	}

	// Printstat ::= "Out" "." ( "print:" | "println:" ) expr { "," expr }
	private void printStat() {
		// lê "Out"
		next();

		check(Token.DOT, "a '.' was expected after 'Out'");
		next();

		check(Token.IDCOLON, "'print:' or 'println:' was expected after 'Out.'");
		// precisa criar a classe Out
		String printName = lexer.getStringValue();
		next();
		ExprList expr = exprList();
		
		for(int i = 0; i < expr.getTamanho(); i++) {
			//System.out.println(expr.getVetor(i).getType());
			if(expr.getVetor(i) == null) {
				error("Command ' Out.print' without arguments");
			}else if(expr.getVetor(i).getType().equals("boolean")) {
				error("Attempt to print a boolean expression");
			}
		}
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
		Expr ce = simpleExpr();

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
				ce = new CompositeExpr(ce,  relation, simpleExpr());
			break;
			default:
				//break;
		}
		
		return ce;
	}
	
	// simpleExpr := SumSubExpr { "++" SumSubExpr }
	private Expr simpleExpr() {
		Expr ce = sumSubExpr();

		while(lexer.token == Token.PLUSPLUS){
			next();
			
			ce = new CompositeExpr(ce, Token.PLUSPLUS, sumSubExpr());
		}

		return ce;
	}
	
	// SumSubExpr := Term { lowOperator Term }
	private Expr sumSubExpr() {
		Expr ce = term();

		// lowOperator := "+" | "-" | "||"
		while(lexer.token == Token.PLUS || lexer.token == Token.MINUS || lexer.token == Token.OR){
			Token oper = lexer.token;
			next();
			
			ce = new CompositeExpr(ce, oper, term());
		}

		return ce;
	}
	
	// term := signalFactor { highOperator signalFactor }
	private Expr term() {
		Expr ce = signalFactor();

		// highOperator := "*" | "/" | "&&"
		while(lexer.token == Token.MULT || lexer.token == Token.DIV || lexer.token == Token.AND){
			Token oper = lexer.token;
			next();
			
			ce = new CompositeExpr(ce, oper, signalFactor());
		}

		return ce;
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
            	next();
				return literalInt();
            case LITERALSTRING:
				next();
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
				return new NegationFactor(factor());
        
		    case NULL:
				return new NullExpr();
            default:
				return primaryExpr();
		}
	}
	
	// objectCreation := Id "." "new"
	// REVISAR
	// joguei pra dentro do primary
	private void objectCreation(){
		next();
		check(Token.DOT, "'.' was expected after id");
		next();
		check(Token.NEW, "'new' was expected after '.'");
		next();
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

		// escopo
		if(lexer.token == Token.SUPER){
			primaryExpr.setScope(Token.SUPER);
			next();

			check(Token.DOT, "'.' was expected after 'super'");
			next();
		}
		else if(lexer.token == Token.SELF){
			primaryExpr.setScope(Token.SELF);
			next();
			if(lexer.token != Token.DOT)
				return primaryExpr;
			else
				// le ponto
				next();
		}

		//primeiro id
		
		if(lexer.token == Token.IN) {
			return readExpr();
		}else if(lexer.token == Token.ID|| lexer.token == Token.PRINT){
			primaryExpr.setFirstId(lexer.getStringValue());
			next();
			if(lexer.token == Token.DOT) {
				next();
				if(lexer.token == Token.NEW) {
					next();
				}else if(lexer.token == Token.ID || lexer.token == Token.PRINT) {
					next();
					primaryExpr.setSecondId(lexer.getStringValue());	
				}else if(lexer.token == Token.IDCOLON) {
					primaryExpr.setSecondId(lexer.getStringValue());
					next();
					primaryExpr.setExprList(exprList());
				}
			}
			return primaryExpr;
		}
		else if(lexer.token == Token.IDCOLON){
			primaryExpr.setFirstId(lexer.getStringValue());
			next();
			primaryExpr.setExprList(exprList());
			return primaryExpr;
		}
		else{
			error("Expression expected");
			//return primaryExpr;
		}

		// segundo id
		/*
		if(lexer.token == Token.ID){
			primaryExpr.setSecondId(lexer.getStringValue());
			next();
			if(lexer.token == Token.DOT) {
				next();
				if(lexer.token == Token.NEW) {
					next();
				}
			}
			return primaryExpr;
		}
		else if(lexer.token == Token.IDCOLON){
			primaryExpr.setSecondId(lexer.getStringValue());
			next();
			primaryExpr.setExprList(exprList());
			return primaryExpr;
		}
		else{
			error("Id or IdColon was expected");
			//return primaryExpr;
		}
		*/
		
		return primaryExpr;
	}
	
	// fieldDec := "var" Type IdList [ ";" ]
	private Field fieldDec() {
		// le var
		next();

		Type type = type();
		
		
		IdList idList = idList();
		

		if(lexer.token == Token.SEMICOLON) {
			next();
		}
		

		return new Field(type, idList);
	}

	// type := BasicType | Id
	private Type type() {
		Type type;

		if ( lexer.token == Token.INT || lexer.token == Token.BOOLEAN || lexer.token == Token.STRING ) {
			type = basicType();
			next();
		}
		else if ( lexer.token == Token.ID ) {
			type = new TypeCianetoClass(lexer.getStringValue());
			next();
		}
		else {
			this.error("A type was expected");
			type = null;
			next();
		}
		
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
		next();

		return null;
	}
	
	private LiteralInt literalInt() {
		int value = lexer.getNumberValue();
		//next();
		return new LiteralInt(value);
	}
	
	private LiteralString literalString() {
		String value = lexer.getStringValue();
		//next();
		return new LiteralString(value);
	}

	// readExpr() := "In" "." ( "readInt" | "readString" )
	private Expr readExpr() {
		// ja verificou in na chamada da funcao
		next();
		
		check(Token.DOT, "'.' is missing after 'In'");
		next();

		if(lexer.token == Token.READINT || lexer.token == Token.READSTRING){
			next();
			return new ReadExpr(lexer.token);
		}
		else{
			error("Command 'In.' without arguments");
			return null;
		}
	}

	private SymbolTable		symbolTable;
	private Lexer			lexer;
	private ErrorSignaller	signalError;
	private Method metodoAtual;

}
