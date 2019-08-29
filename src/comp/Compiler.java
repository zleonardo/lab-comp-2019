
package comp;

import java.io.PrintWriter;
import java.util.ArrayList;
import ast.LiteralInt;
import ast.MetaobjectAnnotation;
import ast.Program;
import ast.Statement;
import ast.TypeCianetoClass;
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
		Program program = new Program(CianetoClassList, metaobjectCallList, compilationErrorList);
		boolean thereWasAnError = false;
		while ( lexer.token == Token.CLASS ||
				(lexer.token == Token.ID && lexer.getStringValue().equals("open") ) ||
				lexer.token == Token.ANNOT ) {
			try {
				while ( lexer.token == Token.ANNOT ) {
					metaobjectAnnotation(metaobjectCallList);
				}
				classDec();
			}
			catch( CompilerError e) {
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
			}
			catch ( RuntimeException e ) {
				e.printStackTrace();
				thereWasAnError = true;
			}

		}
		if ( !thereWasAnError && lexer.token != Token.EOF ) {
			try {
				error("End of file expected");
			}
			catch( CompilerError e) {
			}
		}
		return program;
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
				lexer.nextToken();
				if ( lexer.token == Token.COMMA )
					lexer.nextToken();
				else
					break;
			}
			if ( lexer.token != Token.RIGHTPAR )
				error("')' expected after annotation with parameters");
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
	private void classDec() {
		if ( lexer.token == Token.ID && lexer.getStringValue().equals("open") ) {
			// open??
			next();
		}
		if ( lexer.token != Token.CLASS ) error("'class' expected");
		lexer.nextToken();
		if ( lexer.token != Token.ID )
			error("Identifier expected");
		String className = lexer.getStringValue();
		lexer.nextToken();
		if ( lexer.token == Token.EXTENDS ) {
			lexer.nextToken();
			if ( lexer.token != Token.ID )
				error("Identifier expected");
			String superclassName = lexer.getStringValue();

			lexer.nextToken();
		}

		memberList();
		if ( lexer.token != Token.END)
			error("'end' expected");
		lexer.nextToken();

	}

	// memberList := { [ Qualifier ] Member }
	private void memberList() {
		while ( true ) {
			// tem que verificar se no qualifier volta algo ou não pq é opcional
			qualifier();
			// member := fieldDec | methodDec
			if ( lexer.token == Token.VAR ) {
				fieldDec();
			}
			else if ( lexer.token == Token.FUNC ) {
				methodDec();
			}
			else {
				break;
			}
		}
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

	// methodDec := "func" IdColon FormalParamDec [ "->" Type ] "{" StatementList"}" | 
	//				"func" Id [ "->" Type ] "{" StatementList"}"
	private void methodDec() {
		// ja leu "func" no metodo memberList
		lexer.nextToken();
		if ( lexer.token == Token.ID ) {
			// unary method
			lexer.nextToken();
		}
		else if ( lexer.token == Token.IDCOLON ) {
			// keyword method. It has parameters

		}
		else {
			error("An identifier or identifer: was expected after 'func'");
		}
		if ( lexer.token == Token.MINUS_GT ) {
			// method declared a return type
			lexer.nextToken();
			type();
		}
		if ( lexer.token != Token.LEFTCURBRACKET ) {
			error("'{' expected");
		}
		next();
		statementList();
		if ( lexer.token != Token.RIGHTCURBRACKET ) {
			error("'{' expected");
		}
		next();

	}
	
	// formalParamDec := ParamDec {"," ParamDec }
	private void formalParamDec() {
		paramDec();

		while(lexer.token == Token.COMMA){
			// lê a ","
			next();
			paramDec();
		}
	}
	
	// paramDec := Type Id
	private void paramDec() {
		Type();

		if(lexer.token != Token.ID){
			error("Id was expected");
		}
	}
	
	// ESSA GRAMATICA NAO É CHAMADA POR NENHUMA OUTRA??
	// compStatement := "{" { Statement } "}"
	private void compStatement() {

		if (lexer.token != Token.LEFTCURBRACKET){
			error("'{' was expected");
		}
		next();

		// NAO SEI SE TA CERTO PQ TE O DEFAULT NO STATEMENT QUE PODE RECEBER OUTRAS COISAS
		while (lexer.token == Token.IF || lexer.token == Token.WHILE || lexer.token == Token.RETURN || 
			   lexer.token == Token.BREAK || lexer.token == Token.REPEAT || lexer.token == Token.VAR ||
			   lexer.token == Token.ASSERT ||){
			Statement();
		}

		if (lexer.token != Token.RIGHTCURBRACKET){
			error("'}' was expected");
		}
		next();
	}

	// statementList := { Statement }
	private void statementList() {
		  // only '}' is necessary in this test
		// Nao entendi isso que o zé colocou aqui
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
			statement();
		}
	}

	
/* statement := AssignExpr ";" | IfStat | WhileStat | ReturnStat ";" |
                WriteStat ";" | "break" ";" | ";" |
                RepeatStat ";" | LocalDec ";" |
                AssertStat ";" */
	private void statement() {
		boolean checkSemiColon = true;
		switch ( lexer.token ) {
		case ASSIGN:
			assignExpr();
			break;
		case IF:
			ifStat();
			checkSemiColon = false;
			break;
		case WHILE:
			whileStat();
			checkSemiColon = false;
			break;
		case RETURN:
			returnStat();
			break;
		case BREAK:
			breakStat();
			break;
		case SEMICOLON:
			next();
			break;
		case REPEAT:
			repeatStat();
			break;
		case VAR:
			localDec();
			break;
		case ASSERT:
			assertStat();
			break;
		default:
			if ( lexer.token == Token.ID && lexer.getStringValue().equals("Out") ) {
				writeStat();
			}
			else {
				expr();
			}

		}
		if ( checkSemiColon ) {
			check(Token.SEMICOLON, "';' expected");
		}
	}
	
	// assignExpr := Expr [ "=" Expr]
	private void assignExpr() {
		expr();

		if(lexer.token == Token.ASSIGN){
			next();
			expr();
		}
	}

	// localDec := "var" Type IdList [ "=" Expr ]
	private void localDec() {

		if(lexer.token != Token.VAR){
			error("'var' was expected");
		}

		next();
		type();
		idList();
		
		if ( lexer.token == Token.ASSIGN ) {
			next();
			// check if there is just one variable
			expr();
		}
	}
	
	// idList := Id { "," Id } 
	private void idList() {

		check(Token.ID, "A variable name was expected");

		while ( lexer.token == Token.ID ) {
			next();
			if ( lexer.token == Token.COMMA ) {
				next();
			}
			else {
				break;
			}
		}
	}

	// repeatStat := "repeat" statementList "until" expr
	private void repeatStat() {
		// ja leu "repeat" no statement
		next();
		// Esse while o zé colocou, mas nao sei ainda o porque
		while ( lexer.token != Token.UNTIL && lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
			statementList();
		}
		check(Token.UNTIL, "missing keyword 'until'");

		expr();
	}

	// breakStat := "break"
	private void breakStat() {
		// ja leu o "break" no statement
		next();

	}

	// returnStat := "return" expr
	private void returnStat() {
		// ja leu o "return" no statement
		next();
		expr();
	}

	// whileStat := "while" expr "{" StatementList "}"
	private void whileStat() {
		// ja leu "while" no metodo statement
		next();
		expr();
		check(Token.LEFTCURBRACKET, "missing '{' after the 'while' expression");
		next();
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
			statementList();
		}
		check(Token.RIGHTCURBRACKET, "missing '}' after 'while' body");

		next();
	}

	// ifStat := "if" expr "{" Statement "}" [ "else" "{" Statement "}" ]
	private void ifStat() {
		// ja leu o "if" no statement
		next();
		expr();
		check(Token.LEFTCURBRACKET, "'{' expected after the 'if' expression");
		next();
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END && lexer.token != Token.ELSE ) {
			statement();
		}
		check(Token.RIGHTCURBRACKET, "'}' was expected");
		if ( lexer.token == Token.ELSE ) {
			next();
			check(Token.LEFTCURBRACKET, "'{' expected after 'else'");
			next();
			while ( lexer.token != Token.RIGHTCURBRACKET ) {
				statement();
			}
			check(Token.RIGHTCURBRACKET, "'}' was expected");
		}
		next();
	}

	// Writestat := "Out" "." ["print:" | "println:" ] expr
	private void writeStat() {
		// ja leu "Out" no statement
		next();
		check(Token.DOT, "a '.' was expected after 'Out'");
		next();
		// o zé colocou esse check, mas pela gramatica é opcional?. Então teria que ser:
		/* if(lexer.token == Token.IDCOLON){
			String printName = lexer.getStringValue();
			} */
		check(Token.IDCOLON, "'print:' or 'println:' was expected after 'Out.'");
		String printName = lexer.getStringValue();
		expr();
	}
	
	// exprList := Expr { "," Expr }
	private void exprList() {

		int flag;

		do{
			expr();

			if(lexer.token == Token.COMMA){
				next();
				flag = 1;
			}
			else{
				flag = 0
			}
		}while(flag);
	}

	// expr := SimpleExpression [ Relation SimpleExpr ]
	private void expr() {
		simpleExpr();

		String relation = relation();

		if(relation != "") {
			relation();
			simpleExpr();
		}
	}
	
	// simpleExpr := SumSubExpr { "++" SumSubExpr }
	private void simpleExpr() {
		SumSubExpr();

		while(lexer.token == Token.PLUSPLUS){
			next();
			SumSubExpr();
		}
	}
	
	// SumSubExpr := Term { lowOperator Term }
	private void SumSubExpr() {
		term();

		String low = lowOperator();

		while (low != ""){
			next();
			term();
		}
	}
	
	// term := signalFactor { highOperator signalFactor }
	private void term() {
		signalFactor();

		String high = highOperator();

		while (high != ""){
			next();
			signalFactor();
		}
	}
	
	// relation := "==" | "<" | ">" | "<=" | ">=" | "!=" 
	private void relation() {
		switch(lexer.token){
			case EQ:
				return "==";
				break;
			case LT:
				return "<";
				break;
			case GT:
				return ">";
				break;
			case LE:
				return "<=";
				break;
			case GE:
				return ">=";
				break;
			case NEQ:
				return ">";
				break;
			default:
				return "";
		}
	}
	
	// readExpr() := "In" "." [ "readInt" | "readString" ]
	private void readExpr() {
		if(lexer.token != Token.IN){
			error("'In' was expected");
		}
		check(Token.DOT, "'.' is missing after 'In'");

		if(lexer.token == Token.READINT){
			// voltar int
			next();
		}
		else if(lexer.token == Token.READSTRING){
			//voltar string
			next();
		}
	}
	
	// factor := basicValue | "(" Expr ")" | "!" factor | "nil | objectCreation | primaryExpr 
	private void factor() {
		switch (lexer.token) {
            case INT:
                basicValue();
                break;
            case STRING:
                basicValue();
                break;
            case TRUE:
                basicValue();
                break;
            case FALSE:
                basicValue();
                break;
            case LEFTPAR:
                next();
                expr();
                check(Token.RIGHTPAR, "')' was expected");
                next();
                break;
            case NOT:
                next();
                factor();
                break;
            case NULL:
                next();
                break;
            case ID:
            	objectCreation();
            default:
            	primaryExpr();
	}
	
	// objectCreaton := Id "." "new"
	private void objectCreation() {
			next();
			check(Token.DOT, "'.' was expected after id");
			next();
			check(Token.NEW, "'new' was expected after '.'");
			next();
	}
	
/*  primaryExpr := "super" "." IdColon exprList | "super" "." Id |
 					Id | Id "." Id | Id "." IdColon ExprList | "self" |
 					"self" "." Id | "self" "." IdColon exprList | 
 					"self" "." Id "." IdColon exprList | "self" "." Id "." Id
 */	
	private void primaryExpr() {
		if(lexer.token == Token.SUPER){
			next();
			check(Token.DOT, "'.' was expected after 'super'");
			next();
			if(lexer.token == Token.ID){

			}
			else if(lexer.token == Token.IDCOLON){
				next();
				exprList();
			}
			else{
				error("Id or IdColon was expected");
			}
		}
		else if(lexer.token == Token.ID){
			next();
			if(lexer.token == Token.DOT){
				if(lexer.token == Token.ID){
					
				}
				else if(lexer.token == Token.IDCOLON){
					next();
					exprList();
				}
				else{
					error("Id or IdColon was expected");
				}
			}
			else{
				// é só id entao
			}
		}
		else if(lexer.token == Token.SELF){
			next();
			if(lexer.token == Token.DOT){
				if(lexer.token == Token.ID){
					if(lexer.token == Token.DOT){
						if(lexer.token == Token.ID){
						}
						else if(lexer.token == Token.IDCOLON){
							next();
							exprList();
						}
						else{
							error("Id or IdColon was expected");
						}
					}
					else{
						// só id mesmo
					}
				}
				else if(lexer.token == Token.IDCOLON){
					next();
					exprList();
				}
				else{
					error("Id or IdColon was expected");
				}
			}
			else{
				// é só self entao
			}
		}
	}
	
	// highOperator := "*" | "/" | "&&"
	private String highOperator() {
		switch(lexer.token){
			case MULT:
				return "*";
				break;
			case DIV:
				return "/";
				break;
			case AND:
				return "&&";
				break;
			default:
				return "";
		}
	}
	
	// lowOperator := "+" | "-" | "||"
	private String lowOperator() {
		switch(lexer.token){
			case PLUS:
				return "+";
				break;
			case MINUS:
				return "-";
				break;
			case OR:
				return "||";
				break;
			default:
				return "";
		}
	}

	// fieldDec := "var" Type IdList [ ";" ]
	private void fieldDec() {
		lexer.nextToken();
		type();
		if ( lexer.token != Token.ID ) {
			this.error("A field name was expected");
		}
		else {
			while ( lexer.token == Token.ID  ) {
				lexer.nextToken();
				if ( lexer.token == Token.COMMA ) {
					lexer.nextToken();
				}
				else {
					break;
				}
			}
		}
		if(lexer.token == Token.SEMICOLON){
			next();
		}

	}

	// type := BasicType | Id
	private void type() {
		if ( lexer.token == Token.INT || lexer.token == Token.BOOLEAN || lexer.token == Token.STRING ) {
			basicType();
			next();
		}
		else if ( lexer.token == Token.ID ) {
			next();
		}
		else {
			this.error("A type was expected");
		}

	}
	
	// basicType := "Int" | "Boolean" | "String"
	private void basicType() {
		next();
	}
	
	// basicType := "IntValue" | "BooleanValue" | "StringValue"
	private void basicValue() {
		next();
	}
	
	// booleanValue := "true" | "false"
	private void booleanValue() {
		next();
	}

	/* qualifier := "private" | "public" | "override" | "override" "public" |
					"final" | "final" "public" | "final" "override" |
					"final" "override" "public" | "shared" "private" | "shared" "public" */
	private void qualifier() {
		if ( lexer.token == Token.PRIVATE ) {
			next();
		}
		else if ( lexer.token == Token.PUBLIC ) {
			next();
		}
		else if ( lexer.token == Token.OVERRIDE ) {
			next();
			if ( lexer.token == Token.PUBLIC ) {
				next();
			}
		}
		else if ( lexer.token == Token.FINAL ) {
			next();
			if ( lexer.token == Token.PUBLIC ) {
				next();
			}
			else if ( lexer.token == Token.OVERRIDE ) {
				next();
				if ( lexer.token == Token.PUBLIC ) {
					next();
				}
			}
		}
		else if(lexer.token == Token.SHARED) {
			if (lexer.token == Token.PUBLIC) {
				next();
			}
			else if (lexer.token == Token.PRIVATE) {
				next();
			}
		}
	}
	/**
	 * change this method to 'private'.
	 * uncomment it
	 * implement the methods it calls
	 */
	// "assertStat" :=  expr "," StringValue
	public Statement assertStat() {

		lexer.nextToken();
		int lineNumber = lexer.getLineNumber();
		expr();
		if ( lexer.token != Token.COMMA ) {
			this.error("',' expected after the expression of the 'assert' statement");
		}
		lexer.nextToken();
		if ( lexer.token != Token.LITERALSTRING ) {
			this.error("A literal string expected after the ',' of the 'assert' statement");
		}
		String message = lexer.getLiteralStringValue();
		lexer.nextToken();
		/*if ( lexer.token == Token.SEMICOLON )
			lexer.nextToken();*/

		return null;
	}

	// digit := 0 | ... | 9
	private void digit() {
		//??
	}

	// intValue := digit { digit }
	private void intValue() {
		//??
	}
	
	private LiteralInt literalInt() {

		LiteralInt e = null;

		// the number value is stored in lexer.getToken().value as an object of
		// Integer.
		// Method intValue returns that value as an value of type int.
		int value = lexer.getNumberValue();
		lexer.nextToken();
		return new LiteralInt(value);
	}
	
	// signalFactor := [ Signal ] factor
	private void signalFactor() {
		String signal = signal();
		next();
		factor();
	}
	
	// signal := "+" | "-"
	private String signal() {
		if(lexer.token == Token.PLUS){
			return "+";
		}
		else if(lexer.token == Token.MINUS){
			return "-";
		}
		else{
			return "";
		}
	}
	

	private static boolean startExpr(Token token) {

		return token == Token.FALSE || token == Token.TRUE
				|| token == Token.NOT || token == Token.SELF
				|| token == Token.LITERALINT || token == Token.SUPER
				|| token == Token.LEFTPAR || token == Token.NULL
				|| token == Token.ID || token == Token.LITERALSTRING;

	}

	private SymbolTable		symbolTable;
	private Lexer			lexer;
	private ErrorSignaller	signalError;

}
