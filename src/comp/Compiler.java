/*
	Nome: Vitor Pratali Camillo	 RA: 620181
	Nome: Leonardo Zaccarias	 RA: 620491
*/

package comp;

import java.io.PrintWriter;
import java.util.ArrayList;
import ast.*;
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

		// VERIFICAR SE TEM UMA CLASSE 'PROGRAM' QUE É OBRIGATÓRIA
			// VER ISSO NA PARTE SEMANTICA

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
	private TypeCianetoClass classDec() {
		MemberList memberList = null;

		if ( lexer.token == Token.ID && lexer.getStringValue().equals("open") ) {
			// DECLARA OPEN QUANDO UMA CLASSE É HERDADA DE OUTRA
			next();
		}

		check(Token.CLASS, "'class' expected");
		next();
		
		check(Token.ID, "Identifier expected");
		TypeCianetoClass classObj = new TypeCianetoClass(lexer.getStringValue());
		next();

		if ( lexer.token == Token.EXTENDS ) {
			next();

			check(Token.ID, "Identifier expected");
			String superclassName = lexer.getStringValue();
			// falta buscar o objeto da super class, e adiciona-lo a classe
			// classObj.setSuperClass(superClass);
			next();
		}

		memberList = memberList();
		classObj.setMemberList(memberList);
		
		check(Token.END, "'end' expected");
		next();

		return classObj;
	}

	// memberList := { [ Qualifier ] Member }
	private MemberList memberList() {
		MemberList memberList = new MemberList();
		Method method;
		
		while ( true ) {
			// tem que verificar se no qualifier volta algo ou não pq é opcional
			// e colocar na ast
			
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
		
		// le func
		next();

		if ( lexer.token == Token.ID ) {
			// unary method
			method = new Method(lexer.getStringValue());
			next();
		}
		else if ( lexer.token == Token.IDCOLON ) {
			method = new Method(lexer.getStringValue());
			next();

			// keyword method. It has parameters
			formalParamDec();
			//??
		}
		else {
			error("An identifier or identifer: was expected after 'func'");
		}
		if ( lexer.token == Token.MINUS_GT ) {
			// method declared a return type
			next();
			type();
		}
		
		check(Token.LEFTCURBRACKET, "'{' expected");
		next();

		statementList();
		check(Token.RIGHTCURBRACKET, "'}' expected");
		next();

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

		return new ParamDec(id, type);
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
			   lexer.token == Token.ASSERT){
			statement();
		}

		if (lexer.token != Token.RIGHTCURBRACKET){
			error("'}' was expected");
		}
		next();
	}

	// statementList := { Statement }
	private StatementList statementList() {
		StatementList statList = new StatementList();

		// NAO ENTENDI ESSE WHILE
		  // only '}' is necessary in this test
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
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
				break;
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
					expr();
				}
		}
		if ( checkSemiColon ) {
			check(Token.SEMICOLON, "';' expected");
			next();
			
		}
		return statement;
	}
	
	// assignExpr := Expr [ "=" Expr]
	private AssignExpr assignExpr() {
		AssignExpr assignExpr = new AssignExpr();

		assignExpr.setLeftExpr(expr());

		if(lexer.token == Token.ASSIGN){
			next();
			assignExpr.setRightExpr(expr());
		}

		return assignExpr;
	}

	// localDec := "var" Type IdList [ "=" Expr ]
	private LocalDecStat localDec() {
		// TERMINAR
		if(lexer.token != Token.VAR){
			error("'var' was expected");
		}

		next();
		LocalDecStat localDec = new LocalDecStat(type());

		localDec.setIdList(idList());
		
		if ( lexer.token == Token.ASSIGN ) {
			next();
			// check if there is just one variable
			expr();
		}

		return localDec;
	}
	
	// idList := Id { "," Id } 
	private IdList idList() {
		IdList idlist = new IdList();

		check(Token.ID, "A variable name was expected");
		idlist.add(lexer.getStringValue());
		next();

		while ( lexer.token == Token.COMMA ) {
			next();
			check(Token.ID, "A variable name was expected");
			idlist.add(lexer.getStringValue());
			next();
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
		check(Token.UNTIL, "missing keyword 'until'");
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
		
		check(Token.LEFTCURBRACKET, "'{' expected after the 'if' expression");
		next();
		
		// nao era pra ser statementlist?
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
		exprList();
	}
	
	// exprList := Expr { "," Expr }
	private void exprList() {

		expr();

		while(lexer.token == Token.COMMA){
			next();
			expr();
		}
	}

	// expr := SimpleExpression [ Relation SimpleExpr ]
	private Expr expr() {
		// CompositeExpr left = simpleExpr();
		CompositeExpr left = null;
		simpleExpr();

		switch(lexer.token){
			case EQ:
			case LT:
			case GT:
			case LE:
			case GE:
			case NEQ:
				String relation = relation();
				left = new CompositeExpr(left,  relation, simpleExpr());
		}
		
		return left;
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
			low = lowOperator();
		}
	}
	
	// term := signalFactor { highOperator signalFactor }
	private void term() {
		
		signalFactor();

		String high = highOperator();

		while (high != ""){
			
			next();
			
			signalFactor();
			
			high = highOperator();
		}
	}
	
	// signalFactor := [ Signal ] factor
	private void signalFactor() {
		if(lexer.token == Token.PLUS || lexer.token == Token.MINUS){
			Token signal = signal();
			next();
		
		// NAO SEI COMO FAZER AINDA
		factor();
	}
	
	// signal := "+" | "-"
	private Token signal() {
		Token signal = lexer.Token;
		next();
		return signal;
	}
	
	// factor := basicValue | "(" Expr ")" | "!" factor | "nil" | objectCreation | primaryExpr 
	private Expr factor() {
		// ta faltando o case pra objectCreation
		switch (lexer.token) {
            case LITERALINT:
            case LITERALSTRING:
            case TRUE:
            case FALSE:
                return basicValue();
            case LEFTPAR:
                next();
                Expr Expr = new ExprInParentheses(expr());
                check(Token.RIGHTPAR, "')' was expected");
                next();
				return factor;
            case NOT:
            	next();
				return new NegationFactor(factor());
            case NULL:
				return new NullExpr();
            default:
				return primaryExpr();
		}
	}
	
	// basicValue := "IntValue" | "BooleanValue" | "StringValue"
	private Value basicValue() {
		switch (lexer.token) {
            case LITERALINT:
				next();
				return new Value(lexer.getNumberValue);
            case LITERALSTRING:
				next();
				return new Value(lexer.getStringValue);
            case TRUE:
				next();
				return new Value(true);
            case FALSE:
				next();
				return new Value(false);
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
	
	/*  primaryExpr := "super" "." IdColon exprList | "super" "." Id |
						Id | Id "." Id | Id "." IdColon ExprList | "self" |
						"self" "." Id | "self" "." IdColon exprList | 
						"self" "." Id "." IdColon exprList | "self" "." Id "." Id
	*/	
	private void primaryExpr() {
		// PRECISA TERMINAR E AINDA NAO SEI COMO FAZER
		String id = "";
		if(lexer.token == Token.SUPER){
			next();
			check(Token.DOT, "'.' was expected after 'super'");
			next();
			if(lexer.token == Token.ID){
				id = lexer.getStringValue();
				next();
			}
			else if(lexer.token == Token.IDCOLON){
				id = lexer.getStringValue();
				next();
				exprList();
			}
			else{
				error("Id or IdColon was expected");
			}
		}
		else if(lexer.token == Token.ID){
			id = lexer.getStringValue();
			next();
			if(lexer.token == Token.DOT){
				next();
				if(lexer.token == Token.ID){
					next();
				}
				else if(lexer.token == Token.IDCOLON){
					next();
					exprList();
				}
				else if(lexer.token == Token.NEW){
					next();
					return new objectCreation(String id);
				}
				else{
					error("Id or IdColon was expected");
				}
			}
			else{
				// é só id entao
				return id;
			}
		}
		else if(lexer.token == Token.SELF){
			next();
			if(lexer.token == Token.DOT){
				next();
				if(lexer.token == Token.ID){
					id = lexer.getStringValue();
					if(lexer.token == Token.DOT){
						next();
						if(lexer.token == Token.ID){
							id = lexer.getStringValue();
						}
						else if(lexer.token == Token.IDCOLON){
							id = lexer.getStringValue();
							next();
							exprList();
						}
						else{
							error("Id or IdColon was expected");
						}
					}
					else{
						// só id mesmo
						return id;
					}
				}
				else if(lexer.token == Token.IDCOLON){
					id = lexer.getStringValue();
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
	
	// relation := "==" | "<" | ">" | "<=" | ">=" | "!=" 
	private Token relation() {
		Token relation = lexer.token;
		next();
		return relation;
	}
	
	// highOperator := "*" | "/" | "&&"
	private Token highOperator() {
		switch(lexer.token){
			case MULT:
				next();
				return Token.MULT;
			case DIV:
				next();
				return Token.DIV;
			case AND:
				next();
				return Token.AND;
			default:
				return null;
		}
	}
	
	// lowOperator := "+" | "-" | "||"
	private Token lowOperator() {
		switch(lexer.token){
			case PLUS:
				next();
				return Token.PLUS;
			case MINUS:
				next();
				return Token.MINUS;
			case OR:
				next();
				return Token.OR;
			default:
				return null;
		}
	}

	// fieldDec := "var" Type IdList [ ";" ]
	private Field fieldDec() {

		// le var
		next();

		type();
		
		Field field = new Field(idList());

		check(Token.SEMICOLON, "';' expected");
		next();

		return field;
	}

	// type := BasicType | Id
	private Type type() {
		// TERMINAR
		Type type = null;

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
				// se pa eh assim
				// return null;
				// acho q nulltype eh pra quando vc usa null no codigo em cianeto
				return Type.nullType;
			}
	}
	
	// // booleanValue := "true" | "false"
	// private void booleanValue() {
	// 	next();
	// }

	private Statement assertStat(){
		AssertStat assertStat = new AssertStat(expr());
		next();
		
		check(Token.COMMA,"',' expected after the expression of the 'assert' statement");
		next();

		check(Token.LITERALSTRING, "A literal string expected after the ',' of the 'assert' statement");
		assertStat.setString(lexer.getStringValue());
		next();

		return null;
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
	
	private static boolean startExpr(Token token) {
		return token == Token.FALSE || token == Token.TRUE
				|| token == Token.NOT || token == Token.SELF
				|| token == Token.LITERALINT || token == Token.SUPER
				|| token == Token.LEFTPAR || token == Token.NULL
				|| token == Token.ID || token == Token.LITERALSTRING;
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

	private SymbolTable		symbolTable;
	private Lexer			lexer;
	private ErrorSignaller	signalError;

}
