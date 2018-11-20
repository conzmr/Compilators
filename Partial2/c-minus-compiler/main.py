import sys
import fileinput
import time
import string
from tkinter import *
from tkinter import ttk
from tkinter import filedialog
from fileinput import FileInput
import subprocess as sub
from pprint import pprint
from decimal import getcontext, Decimal
from typing import Tuple, Dict, List, Dict, Iterable, TypeVar
from enum import Enum

class Aplication():

    def __init__(self):
        self.raiz = Tk()
        self.raiz.geometry('500x700')
        self.raiz.resizable(width=True,height=True)
        self.raiz.title(' ')

        self.ttitle = Label(self.raiz, text="C MINUS", font=("HELVETICA", 40))
        self.tname1 = Label(self.raiz, text="Constanza Madrigal y Fernanda López", font=("HELVETICA", 15))

        self.ttitle.pack(side=TOP)
        self.tname1.pack(side=TOP)

        self.tinfo = Text(self.raiz, width=100, height=10, font=("Helvetica",14))
        self.tinfo.pack(side=TOP)

        self.output = Text(self.raiz, width=100, height=15, background='black', fg='white')
        self.output.pack(side=TOP)

        self.binfo = ttk.Button(self.raiz, text='Load file',
                                command=self.openFilename)
        self.binfo.pack(side=LEFT)

        self.bwrite = ttk.Button(self.raiz, text='Show result',
                                command=self.printOutput)
        self.bwrite.pack(side=LEFT)

        self.bsalir = ttk.Button(self.raiz, text='Close',
                                 command=self.raiz.destroy)
        self.bsalir.pack(side=RIGHT)

        self.binfo.focus_set()
        self.raiz.mainloop()


    def printOutput(self):
        content = self.parser.get_code()
        self.output.insert(END,content)


    def openFilename(self):
        file = filedialog.askopenfilename(filetypes = (("C minus files","*.c-"),("all files","*.*")))
        source = fileinput.input(files=(file))
        self.tinfo.delete("1.0", END)
        start = time.time()
        lexer = Lexer(source)
        tkns = lexer.get_tokens()
        self.parser = Parser(tkns)
        fileinput.close()
        time.process_time()
        elapsed = str(round(time.time() - start, 3))
        content = "\nExecution done in\n"+elapsed+" secs"
        self.tinfo.insert("1.0", content)


class Parser(object):
    def __init__(self, tokens: list):
        self.tokens = tokens
        self.text = archivo(tokens)

    def get_code(self):
        return self.text

class Lexer(object):
    def __init__(self, source: FileInput):
        self.source = source
        self.firstLine = source.readline()
        self.table = SymbolTable(source.filename())

    def get_tokens(self):
        firstLineTokens = tokenize(self.firstLine, 1, self.table)
        restTokens = [tkn for line in self.source for tkn in tokenize(line, self.source.lineno(), self.table)]
        return firstLineTokens + restTokens

    def get_symtable(self):
        return self.tables


class Symbols(Enum):
    MAIN = 0
    RETURN = 1
    IF= 2
    WHILE = 3
    EMPTY= 36
    INT = 4
    FLOAT = 6
    TRUE = 7
    FALSE = 8
    SUM = 9
    SUBTRACT = 10
    MULTIPLY = 11
    DIVISION = 12
    POWER = 13
    GT = 14
    LT = 15
    EQ = 16
    AND = 17
    OR = 18
    NOT = 19
    ASS = 20
    COMMA = 21
    SEMMI = 22
    LEFT_PAR = 23
    RIGHT_PAR = 24
    LEFT_CURL = 25
    RIGHT_CURL = 26
    ID = 27
    NUM = 28
    LEFT_BRACKET = 29
    RIGHT_BRACKET = 30
    NEQ = 31
    LTE = 32
    GTE = 33
    VOID = 34
    ELSE = 35

DATA_TYPES = {4, 6, 34 }
COMPARISON_OPERATORS = { 14, 15, 16, 31, 32, 33}
STATEMENTS = { 3, 2, 27, 35, 1 }

class Symbol(object):
    def __init__(self, kind: int, value: str):
        self.kind = kind
        self.value = value

    def __repr__(self):
        return repr(Symbols(self.kind))

class Token(object):
    def __init__(self, symbol: Symbol, line: int, col: int, dtype=None):
        self.symbol = symbol
        self.line = line
        self.col = col
        self.dtype = dtype

    def __repr__(self):
        return '<{}, {}, {}:{}:{}>'.format(
            repr(Symbols(self.symbol.kind)),
            self.dtype,
            self.symbol.value,
            self.line,
            self.col
        )

STATEMENTS_IDENTIFIERS = {
    'main': Symbol(0, 'main'),
    'return': Symbol(1, 'return'),
    'if': Symbol(2, 'if'),
    'else': Symbol(35, 'else'),
    'while': Symbol(3, 'while')
}
DATA_TYPES_IDENTIFIERS = {
    'int': Symbol(4, 'int'),
    'float': Symbol(6, 'float'),
    'void': Symbol(34, 'void')
}
BOOLEANS = {
    'true': Symbol(7, 'true'),
    'false': Symbol(8, 'false')
}
ARITHMETIC_OPERATORS = {
    '+': Symbol(9, '+'),
    '-': Symbol(10, '-'),
    '*': Symbol(11, '*'),
    '/': Symbol(12, '/')
}
RELATIONAL_OPERATORS = {
    '>': Symbol(14, '>'),
    '<': Symbol(15, '<'),
    '==': Symbol(16, '=='),
    '!=': Symbol(31, '!='),
    '>=': Symbol(33, '>='),
    '<=': Symbol(32, '<=')
}
BOOLEAN_OPERATORS = {
    '&': Symbol(17, '&'),
    '|': Symbol(18, '|'),
    '!': Symbol(19, '!')
}
ASSIGMENT = {
    '=': Symbol(20, '=')
}
PUNCTUATION_IDENTIFIERS = {
    ',': Symbol(21, ','),
    ';': Symbol(22, ';'),
    '(': Symbol(23, '('),
    ')': Symbol(24, ')'),
    '{': Symbol(25, '{'),
    '}': Symbol(26, '}'),
    '[': Symbol(29, '['),
    ']': Symbol(30, ']')
}

KeywordType = Dict[str, Symbol]
not_ids = STATEMENTS_IDENTIFIERS.copy()
not_ids.update(DATA_TYPES_IDENTIFIERS)
not_ids.update(BOOLEANS)
__all__ = not_ids.copy()
__all__.update(ARITHMETIC_OPERATORS)
__all__.update(RELATIONAL_OPERATORS)
__all__.update(BOOLEAN_OPERATORS)
__all__.update(ASSIGMENT)
__all__.update(PUNCTUATION_IDENTIFIERS)

class SymbolTable(object):
    def __init__(self, name: str):
        self.name = name
        self.identifiers = {}

    def lookup(self, value: str):
        tkn = self.identifiers.get(value)
        if tkn is not None:
            return tkn
        kwd = __all__.get(value)
        if kwd is not None:
            return kwd
        new_tkn = self.identifiers[value] = Symbol(ID, value)
        return new_tkn

    def contains(self, value: str):
        tkn = self.identifiers.get(value)
        return tkn is not None

    def get_identifiers(self):
        return self.identifiers.keys()

    def get_symbols(self):
        return self.identifiers.values()

    def print_table(self):
        print(self.identifiers)

    def __repr__(self):
        return self.name + ' symtable: ' + self.identifiers.__str__()


class SymbolTableMod(object):
    identifiers: Dict[str, Token] = None
    def __init__(self, name: str, dtype: int):
        self.name = name
        self.identifiers = {}
        self.dtype: int = dtype

    def put(self, token: Token):
        retrieved = self.identifiers.get(token.symbol.value)
        self.identifiers[token.symbol.value] = token

    def get_token(self, value) -> Token:
        return self.identifiers.get(value)

    def contains(self, value: str) -> bool:
        tkn = self.identifiers.get(value)
        return tkn is not None

    def get_identifiers(self) -> Iterable[str]:
        return self.identifiers.keys()

    def get_tokens(self) -> Iterable[Token]:
        return self.identifiers.values()

    def print_table(self):
        print(self.identifiers)

    def __repr__(self):
        return self.name + ' symtable: ' + self.identifiers.__str__()


WHITE = '\n\t\r '
DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}
DOT = {'.'}
class Node(object):
    def __init__(self, transitions=None, is_final=False):
        self.transitions = transitions
        self.is_final = is_final

    def move(self, c: str):
        for tran in self.transitions:
            if c in tran[0]:
                return tran[1]
        return None

terminalNode = Node([], is_final=True)
assignmentNode = Node([({ '=' }, terminalNode)], is_final=True)
relationalNode = Node([({ '=' }, terminalNode)], is_final=True)
booleanNode = Node([({ '=' }, terminalNode)], is_final=True)
floatNode = Node(is_final=True)
floatNode.transitions = [(DIGITS, floatNode)]
dotNode = Node([(DIGITS, floatNode)])
intNode = Node(is_final=True)
intNode.transitions = [(DIGITS, intNode), (DOT, dotNode)]
idNode = Node(is_final=True)
idNode.transitions = [(set(string.ascii_letters).union(DIGITS), idNode)]
initialNode = Node([
    (set(ARITHMETIC_OPERATORS.keys()), terminalNode),
    (set(RELATIONAL_OPERATORS.keys()), relationalNode),
    (set(BOOLEAN_OPERATORS.keys()), booleanNode),
    (set(ASSIGMENT.keys()), assignmentNode),
    (set(PUNCTUATION_IDENTIFIERS.keys()), terminalNode),
    (set(string.ascii_lowercase), idNode),
    (DIGITS, intNode)
])

def lexException(line: str, lineno: int, col: int):
    import re
    msg = "Unexpected token {}, column {}:\n{}{}" \
        .format(lineno, col, line, re.sub(r'[^\s]', ' ', line[:col]) + '^')
    print(msg)
    exit()

def lstrip(string):
    i = 0
    stripped = { '\t': 0, ' ': 0, '\r': 0, '\n': 0 }
    while len(string) < i and string[i] in WHITE:
        stripped[string[i]] += 1
        i += 1
    return string[i:], stripped, i

def tokenize(line: str, lineno: int, symtable: SymbolTable):
    current = initialNode
    tokens = []
    value = ''
    for col, char in enumerate(line):
        if current is None:
            lexException(line, lineno, col-1)
        if current is initialNode and char in WHITE:
            continue
        nxt = current.move(char)
        if nxt is not None:
            value += char
            current = nxt
        elif current.is_final:
            if current is intNode or current is floatNode:
                tokens.append(Token(Symbol(NUM, value), lineno, col))
            else:
                symbol = symtable.lookup(value)
                tokens.append(Token(symbol, lineno, col))
            current = initialNode if char in WHITE else initialNode.move(char)
            value = '' if char in WHITE else char
        else:
            lexException(line, lineno, col)
    if current.is_final:
        if current is intNode or current is floatNode:
            tokens.append(Token(Symbol(NUM, value), lineno, col))
        else:
            symbol = symtable.lookup(value)
            tokens.append(Token(symbol, lineno, col))
    return tokens


# SYNTAX TREE
class IdNode(object):
    dtype: int = None
    value: str = None
    def __init__(self, dtype: int, value: str):
        self.dtype = dtype
        self.value = value

class ParametersNode(object):
    parameters: List[IdNode] = None
    def __init__(self, parameters: List[IdNode]):
        self.parameters = parameters

class FuncCallNode(object):
    value: str = None
    parameters: ParametersNode = None
    def __init__(self, value: str, parameters: ParametersNode):
        self.value = value
        self.parameters = parameters

class LiteralNode(object):
    dtype: int = None
    value: str = None
    def __init__(self, dtype: int, value: str):
        self.dtype = dtype
        self.value = value

class BinaryExpressionNode(object):
    dtype: int = None
    operator: int = None
    left = None
    right = None
    def __init__(self, dtype: int, operator: int, left, right):
        self.dtype = dtype
        self.operator = operator
        self.left = left
        self.right = right

class UnaryExpressionNode(object):
    dtype: int = None
    operator: int = None
    id_node: IdNode
    def __init__(self, dtype: int, operator: int, id_node: IdNode):
        self.dtype = dtype
        self.operator = operator
        self.id_node = id_node

class DeclarationNode(object):
    dtype: int = None
    value: str = None
    def __init__(self, dtype: int, value: str):
        self.dtype = dtype
        self.value = value

class DeclarationsNode(object):
    declarations: List[DeclarationNode] = None
    def __init__(self, declarations: List[DeclarationNode]):
        self.declarations = declarations

Value = TypeVar('Value', IdNode, LiteralNode, FuncCallNode)
ExpressionNode = TypeVar('Expression', Value, BinaryExpressionNode, UnaryExpressionNode)
class AssNode(object):
    left: IdNode = None
    right: ExpressionNode
    def __init__(self, left: IdNode, right: ExpressionNode):
        self.left = left
        self.right = right

class WhileNode(object):
    boolean: IdNode = None
    body = None
    def __init__(self, boolean: IdNode, body):
        self.boolean = boolean
        self.body = body

class IfNode(object):
    boolean: IdNode = None
    body = None
    def __init__(self, boolean: IdNode, body):
        self.boolean = boolean
        self.body = body

StatementNode = TypeVar('StatementNode', IfNode, WhileNode, AssNode)
class BodyNode(object):
    STATEMENTS_IDENTIFIERS = None
    def __init__(self, STATEMENTS_IDENTIFIERS):
        self.STATEMENTS_IDENTIFIERS = STATEMENTS_IDENTIFIERS

class ReturnNode(object):
    dtype: int = None
    value: str = None
    def __init__(self, dtype: int, value: str):
        self.dtype = dtype
        self.value = value

class FuncNode(object):
    arguments: DeclarationsNode = None
    declarations: DeclarationsNode = None
    body: BodyNode = None
    return_statement: ReturnNode = None
    def __init__(self, arguments: DeclarationsNode, declarations: DeclarationsNode, body: BodyNode, return_statement: ReturnNode):
        self.arguments = arguments
        self.body = body
        self.return_statement = return_statement

class MainNode(object):
    declarations: DeclarationsNode = None
    body: BodyNode = None
    def __init__(self, declarations: DeclarationsNode, body: BodyNode):
        self.declarations = declarations
        self.body = body

class FileNode(object):
    functions: List[FuncNode] = None
    def __init__(self, functions: List[FuncNode], main: MainNode):
        self.functions = functions
        self.main = main



def varNameGenerator():
    i = 1
    while True:
        yield 't' + str(i)
        i += 1
getVarName = varNameGenerator()

def labelNameGenerator():
    i = 1
    while True:
        yield 'L' + str(i)
        i += 1
getLabelName = labelNameGenerator()

def throwSyntaxError(msg):
    raise Exception(msg)

def check_id(token: Token, dtype: int):
    if token.symbol.kind is not ID:
        value = token.symbol.value
        throwSyntaxError('Identifier expected, found: {} in line {}, column {} '.format(value, token.line, token.col))
    else:
        token.dtype = dtype

def check_token_type(token: Token, keyword_type):
    if token.symbol.value not in keyword_type:
        value = token.symbol.value
        throwSyntaxError('Data type expected, found: {} in line {}, column {}'.format(value, token.line, token.col))
    return token.symbol.kind

def check_token(token: Token, kind: int):
    if token.symbol.kind is not kind:
        throwSyntaxError('Expected {}, found: {} in line {}, column {}'.format(Symbols(kind).name, token.symbol.value, token.line, token.col))

def arguments(start: int, tokens: List[Token]):
    i = start
    eval_text = ''
    params_text = 'begin params\n'
    param_count = 0
    while tokens[i].symbol.kind is not RIGHT_PAR:
        param_count += 1
        i, expression_body, last_operation_text = expression(i, tokens)
        var_par = next(getVarName)
        eval_text += expression_body + var_par + ' = ' + last_operation_text + '\n'
        params_text += 'param ' + var_par + '\n'
        if tokens[i].symbol.kind is not COMMA:
            check_token(tokens[i], RIGHT_PAR)
            break
        check_token(tokens[i], COMMA)
        i += 1
    return i, eval_text + params_text, param_count

def call(start, tokens):
    i = start + 2
    text = ''
    n_args = 0
    check_token(tokens[start], ID)
    check_token(tokens[start + 1], LEFT_PAR)
    if not tokens[start + 2].symbol.kind is RIGHT_PAR:
        i, text, n_args = arguments(start + 2, tokens)
    check_token(tokens[i], RIGHT_PAR)
    var_return = next(getVarName)
    text += var_return + ' = ' + 'call ' + tokens[start].symbol.value + ', ' + str(n_args) + '\n'
    return i + 1, text, var_return

def factor(start, tokens):
    if tokens[start].symbol.kind is ID:
        if tokens[start + 1].symbol.kind is LEFT_PAR:
            return call(start, tokens)
        elif tokens[start + 1].symbol.kind is LEFT_BRACKET:
            i, body, var_pos = var_array(start, tokens)

            return i, body, '*' + var_pos
        else:
            return start + 1, '', tokens[start].symbol.value
    elif tokens[start].symbol.kind is NUM:
        return start + 1, '', tokens[start].symbol.value
    else:
        check_token(tokens[start], LEFT_PAR)
        i, expr_body, last_operation_text = expression(start + 1, tokens)
        check_token(tokens[i], RIGHT_PAR)
        expr_var = next(getVarName)
        expr_body += expr_var + ' = ' + last_operation_text + '\n'
        return i + 1, expr_body, expr_var

def additive_expression(start, tokens):
    term_end, term_body, term_result = term(start, tokens)
    if tokens[term_end].symbol.kind in [SUM, SUBTRACT]:
        term2_end, term2_body, term2_result = additive_expression(term_end + 1, tokens)
        term_body += term2_body

        term_var = next(getVarName)
        term_body += term_var + ' = ' + term_result.replace('\n', '') + ' ' + tokens[term_end].symbol.value + ' ' + term2_result + '\n'
        term_end = term2_end
        term_result = term_var
    return term_end, term_body, term_result

def term(start, tokens):
    term_end, term_body, term_result = factor(start, tokens)
    if tokens[term_end].symbol.kind in [MULTIPLY, DIVISION]:
        term2_end, term2_body, term2_result = term(term_end + 1, tokens)
        term_body += term2_body

        term_var = next(getVarName)
        term_body += term_var + ' = ' + term_result + ' ' + tokens[term_end].symbol.value + ' ' + term2_result + '\n'
        term_end = term2_end
        term_result = term_var
    return term_end, term_body, term_result

def simple_expression(start, tokens):
    term_end, term_body, term_result = additive_expression(start, tokens)
    if tokens[term_end].symbol.kind in COMPARISON_OPERATORS:
        term2_end, term2_body, term2_result = additive_expression(term_end + 1, tokens)
        term_body += term2_body
        term_var = next(getVarName)
        term_body += term_var + ' = ' + term_result + ' ' + tokens[term_end].symbol.value + ' ' + term2_result + '\n'
        term_end = term2_end
        term_result = term_var
    return term_end, term_body, term_result + '\n'

def var_array(start, tokens):
    check_token(tokens[start + 1], LEFT_BRACKET)
    expression_end, expression_body, last_operation_text = expression(start + 2, tokens)
    check_token(tokens[expression_end], RIGHT_BRACKET)
    var_index = next(getVarName)
    var_offset = next(getVarName)
    text = expression_body + var_index + ' = ' + last_operation_text + '\n'
    text += var_offset + ' = ' + var_index + ' * elem_size(' + tokens[start].symbol.value + ')\n'
    var_pos = next(getVarName)
    text += var_pos + ' = &' + tokens[start].symbol.value + ' + ' + var_offset + '\n'
    return expression_end + 1, text, var_pos

def ass_expression(start, tokens):
    i = start
    check_token(tokens[start], ID)
    text, result_var = '', tokens[start].symbol.value
    if tokens[start+1].symbol.kind is LEFT_BRACKET:
         i, var_text, result_var = var_array(start, tokens)
         result_var = '*' + result_var
         text += var_text
    else:
        i += 1
    check_token(tokens[i], ASS)

    i, expr_body_text, last_operation_text = expression(i + 1, tokens)
    text += expr_body_text + result_var + ' = ' + last_operation_text + '\n'
    return i, text, result_var

def expression(start: int, tokens: List[Token]):
    try:
        return ass_expression(start, tokens)
    except:
        return simple_expression(start, tokens)

def expression_statement(start: int, tokens: List[Token]):
    expr_end, expr_body, _ = expression(start, tokens)
    check_token(tokens[expr_end], SEMMI)
    return expr_end + 1, expr_body

def while_statement(start: int, tokens: List[Token]):
    check_token(tokens[start], WHILE)
    check_token(tokens[start+1], LEFT_PAR)
    expression_end, expr_body_text, last_operation_text = expression(start + 2, tokens)
    while_var = next(getVarName)
    while_label_condition = next(getLabelName)
    while_label_end = next(getLabelName)
    while_text = 'Label ' + while_label_condition + '\n' + expr_body_text + while_var + ' = ' + last_operation_text + '\n'
    while_text += 'if false ' + while_var + ' goto ' + while_label_end  + '\n'

    check_token(tokens[expression_end], RIGHT_PAR)
    while_body_end = 0
    if tokens[expression_end + 1].symbol.kind is LEFT_CURL:
        while_body_end, while_body_text = func_body(expression_end + 2, tokens)
        while_text += while_body_text
        check_token(tokens[while_body_end], RIGHT_CURL)
        while_body_end += 1
    else:
        while_body_end, while_body_text = statement(expression_end + 1, tokens)
        while_text += while_body_text
    while_text += 'goto ' + while_label_condition + '\n' + 'Label ' + while_label_end
    return while_body_end, while_text


def if_statement(start: int, tokens: List[Token]):
    check_token(tokens[start], IF)
    check_token(tokens[start+1], LEFT_PAR)
    expression_end, expr_body_text, last_operation_text = expression(start + 2, tokens)
    if_var = next(getVarName)
    if_text = expr_body_text + if_var + ' = ' + last_operation_text + '\n'
    else_label = next(getLabelName)
    end_if_label = next(getLabelName)
    if_text += 'if false ' + if_var + ' goto ' + else_label  + '\n'

    check_token(tokens[expression_end], RIGHT_PAR)
    if_body_end = 0
    if tokens[expression_end + 1].symbol.kind is LEFT_CURL:
        if_body_end, if_body_text = func_body(expression_end + 2, tokens)
        if_text += if_body_text
        check_token(tokens[if_body_end], RIGHT_CURL)
        if_body_end += 1
    else:
        if_body_end, if_body_text = statement(expression_end + 1, tokens)
        if_text += if_body_text

    if_text += 'goto ' + end_if_label + '\n'
    if_text += 'Label ' + else_label + '\n'

    if tokens[if_body_end].symbol.kind is ELSE:
        if tokens[if_body_end + 1].symbol.kind is LEFT_CURL:
            if_body_end, else_body_text = func_body(if_body_end + 2, tokens)
            if_text += else_body_text
            check_token(tokens[if_body_end], RIGHT_CURL)
            if_body_end += 1
        else:
            if_body_end, else_body_text = statement(if_body_end + 1, tokens)
            if_text += else_body_text

    if_text += 'Label ' + end_if_label + '\n'

    return if_body_end, if_text

def return_statement(start: int, tokens: List[Token]):
    check_token(tokens[start], RETURN)
    if (tokens[start+1].symbol.kind is SEMMI):
        return start + 2, 'return\n'

    i, expr_body_text, last_operation_text = expression(start + 1, tokens)
    return_var = next(getVarName)
    text = expr_body_text + return_var  + ' = ' + last_operation_text + '\n' + 'return ' + return_var + '\n'
    check_token(tokens[i], SEMMI)
    return i + 1, text

def empty_statement(start: int, tokens: List[Token]):
    check_token(tokens[start], SEMMI)
    return start + 1, 'return\n'

def statement(start: int, tokens: List[Token]):
    kind = tokens[start].symbol.kind
    if kind is IF:
        return if_statement(start, tokens)
    if kind is WHILE:
        return while_statement(start, tokens)
    if kind is RETURN:
        return return_statement(start, tokens)
    if kind is SEMMI:
        return empty_statement(start, tokens)
    else:
        return expression_statement(start, tokens)

def func_body(start: int, tokens: List[Token]):
    i = start
    body_text = ''
    while tokens[i].symbol.kind in STATEMENTS:
        i, statement_text = statement(i, tokens)
        body_text += statement_text
    return i, body_text

def func_declarations(start: int, tokens: list):
    i = start
    while tokens[i].symbol.kind in DATA_TYPES:
        i = var_def(i, tokens, False)
    return i

def parameters(start: int, tokens: list):
    i = start
    if tokens[i].symbol.kind is VOID:
        check_token(tokens[i + 1], RIGHT_PAR)
        return i + 2
    if tokens[i].symbol.kind is RIGHT_PAR:
        return i + 1
    while tokens[i-1].symbol.kind is not RIGHT_PAR:
        i = var_def(i, tokens, True)
    return i

def func_def(start: int, tokens: list):
    text = 'entry ' + tokens[start + 1].symbol.value  + '\n'
    check_token(tokens[start+2], LEFT_PAR)
    end_args = parameters(start+3, tokens)

    check_token(tokens[end_args], LEFT_CURL)
    end_declarations = end_args + 1
    if tokens[end_declarations].symbol.kind in DATA_TYPES:
        end_declarations = func_declarations(end_declarations, tokens)

    end_body, body_text = func_body(end_declarations, tokens)
    text += body_text
    check_token(tokens[end_body], RIGHT_CURL)
    return end_body + 1, text

def var_def(i, tokens, isParam):
    if tokens[i + 2].symbol.kind is LEFT_BRACKET:
        return i + 6 - isParam
    else:
        return i + 3

def func_defs_var_def(tokens: list):
    i = 0
    text = ''
    while (i + 2) < len(tokens):
        if tokens[i + 2].symbol.kind in [SEMMI, LEFT_BRACKET]:
            i = var_def(i, tokens, False)
        else:
            i, _text = func_def(i, tokens)
            text += _text
    return i, text

def archivo(tokens: list):
    last, text = func_defs_var_def(tokens)
    return text


MAIN = 0
RETURN = 1
IF = 2
WHILE = 3
INT = 4
FLOAT = 6
TRUE = 7
FALSE = 8
SUM = 9
SUBTRACT = 10
MULTIPLY = 11
DIVISION = 12
POWER = 13
GT = 14
LT = 15
EQ = 16
AND = 17
OR = 18
NOT = 19
ASS = 20
COMMA = 21
SEMMI = 22
LEFT_PAR = 23
RIGHT_PAR = 24
LEFT_CURL = 25
RIGHT_CURL = 26
ID = 27
NUM = 28
LEFT_BRACKET = 29
RIGHT_BRACKET = 30
NEQ = 31
LTE = 32
GTE = 33
VOID = 34
ELSE = 35
EMPTY= 36
def main():
    mi_app = Aplication()
    return 0

if __name__ == '__main__':
    main()
