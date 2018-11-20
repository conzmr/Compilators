# -*- coding: utf-8 -*-
import sys
import re
from parser import parser

class Error:
	def __init__(self, type, line_num, char_num, ch, line):
		self.type = type
		self.line_num = line_num
		self.char_num = char_num
		self.ch = ch
		self.line = line
	def __repr__(self):
		return '{}: Invalid {} in {} \nLine {}, character {}.'.format(self.type, repr(self.ch), repr(self.line), repr(self.line_num), repr(self.char_num))

class Token:
	def __init__(self, type, value, line, ch):
		self.value = value
		self.type = type
		self.ch = ch
		self.line = line
		self.isFunction = None
		self.data_type = None

	def __repr__(self):
		return '{}(Type {}, Value {}, Line {}, Character {})'.format(self.__class__.__name__, repr(self.type), repr(self.value), repr(self.line), repr(self.ch))

class Lexer:
	def __init__(self):
		self.tokens = []
		self.errors = []
		self.current = None
		self.keywords = {'principal', 'entero', 'real', 'logico', 'si', 'regresa', 'mientras', 'verdadero', 'falso'}
		self.symbol_table = {}

	def add_token(self, type, val, line, char):
		self.tokens.append(Token(type, val, line, char))
		self.current = None

	def add_current_token(self):
		if self.current.type == 'IDENTIFIER' and self.current.value in self.keywords:
			self.current.type = 'KEYWORD'
		else:
			self.symbol_table[self.current.value] = self.current
		self.tokens.append(self.current)
		self.current = None

	def get_tokens(self, source_code):
		self.tokens = []
		self.current = None
		self.errors = []
		line_num = 0
		for line in source_code:
			ch_num = 0
			line_num += 1
			for ch in line:
				ch_num += 1
				if not (self.current is None):
					if not ( ch and ch.strip() ):
						self.add_current_token()
					elif self.current.type == 'ASSIGNATION_OP':
						if ch in "=":
							self.add_token('RELATIONAL_OP', "==", self.current.line, self.current.ch)
							continue
						else:
							self.add_current_token()
					elif self.current.type == 'INTEGER':
						if ch in ".":
							self.current.type = 'REAL'
							self.current.value += ch
							continue
						elif re.match("[ 0-9 ]", ch):
							self.current.value += ch
							continue
						else:
							self.add_current_token()
					elif self.current.type == 'REAL':
						if re.match("[ 0-9 ]", ch):
							self.current.value += ch
							continue
						else:
							if re.match("[ 0-9 ]*.[ 0-9 ]*", self.current.value):
								self.add_current_token()
							else:
								self.errors.append(Error("LexicalError", line_num, ch_num, ch, line))
					elif self.current.type == 'IDENTIFIER':
						if re.match("[a-zA-Z0-9]", ch):
							self.current.value += ch
							continue
						else:
							self.add_current_token()
				if ch and ch.strip():
					if ch in "+-*/^":
						self.add_token('ARITHMETIC_OP', ch, line_num, ch_num)
					elif ch in "&|!":
						self.add_token('LOGICAL_OP', ch, line_num, ch_num)
					elif ch in "<>":
						self.add_token('RELATIONAL_OP', ch, line_num, ch_num)
					elif ch in "{":
						self.add_token('LEFT_KEY', ch, line_num, ch_num)
					elif ch in "}":
						self.add_token('RIGHT_KEY', ch, line_num, ch_num)
					elif ch in "(":
						self.add_token('LEFT_PARENTHESIS', ch, line_num, ch_num)
					elif ch in ")":
						self.add_token('RIGHT_PARENTHESIS', ch, line_num, ch_num)
					elif ch in ";":
						self.add_token('SEMICOLON', ch, line_num, ch_num)
					elif ch in ",":
						self.add_token('COLON', ch, line_num, ch_num)
					elif ch in "=":
						self.current = Token('ASSIGNATION_OP', ch, line_num, ch_num)
					elif re.match("[ a-z ]", ch):
						self.current = Token('IDENTIFIER', ch, line_num, ch_num)
					elif re.match("[ 0-9 ]", ch):
						self.current = Token('INTEGER', ch, line_num, ch_num)
					else:
						self.errors.append(Error("LexicalError", line_num, ch_num, ch, line))
		if not (self.current is None):
			self.add_current_token()
		print(self.tokens)
		print(self.errors)

def scanner(filename):
    tokens = []
    errors = []
    symbol_table = {}
    reserved = ['entero', 'real', 'logico', 'regresa', 'si', 'mientras', 'principal', 'verdadero', 'falso']
    unary_tokens = [
        '{', '}', '(', ')', '/', '*', '+', '^', '-', '<', '>', '!', '&', '|', ',', ';'
    ]
    whitespace = [
        '\n', ' ', '\t', '\r'
    ]
    def buffer_match(tipo, valor, linea, caracter):
        if not valor:
            return
        if tipo == 'error':
            errors.append({'tipo': tipo, 'valor': valor, 'linea': linea + 1, 'caracter': caracter + 1})
            return
        if tipo == 'id':
            if valor in reserved:
                tipo = "reservada"
            elif valor not in symbol_table:
                symbol_table[valor] = {'tipo': tipo, 'valor': valor, 'linea': linea + 1, 'caracter': caracter + 1}
        tokens.append({'tipo': tipo, 'valor': valor, 'linea': linea + 1, 'caracter': caracter + 1})

    with open(filename, 'r') as f:
        line = f.readline()
        line_no = 0
        c_buffer = ""
        possible_type = ""
        c_no = 0
        c_start = 0
        while line:
            c_no = 0
            line_length = len(line)
            while c_no < line_length:
                c = line[c_no]
                if c in unary_tokens:
                    buffer_match(possible_type, c_buffer, line_no, c_start)
                    buffer_match(c, c, line_no, c_no)
                    possible_type = ""
                    c_no += 1
                    c_buffer = ""
                    continue
                if c in whitespace:
                    buffer_match(possible_type, c_buffer, line_no, c_start)
                    possible_type = ""
                    c_no += 1
                    c_buffer = ""
                    continue
                if possible_type == "" and ord(c) >= ord('a') and ord(c) <= ord('z'):
                    c_start = c_no
                    c_buffer += c
                    c_no += 1
                    possible_type = "id"
                    continue
                if possible_type == "id" and (
                        (ord(c) >= ord('a') and ord(c) <= ord('z')) or
                        (ord(c) >= ord('0') and ord(c) <= ord('9')) or
                        (ord(c) >= ord('A') and ord(c) <= ord('Z'))
                    ):
                    c_buffer += c
                    c_no += 1
                    continue
                if possible_type == "" and ord(c) >= ord('0') and ord(c) <= ord('9'):
                    c_start = c_no
                    c_buffer += c
                    c_no += 1
                    possible_type = "entero"
                    continue
                if (possible_type == "entero" or possible_type == "real") and ord(c) >= ord('0') and ord(c) <= ord('9'):
                    c_buffer += c
                    c_no += 1
                    continue
                if possible_type == "entero" and c == '.':
                    c_buffer += c
                    c_no += 1
                    c = line[c_no]
                    if ord(c) >= ord('0') and ord(c) <= ord('9'):
                        c_buffer += c
                        c_no += 1
                        possible_type = "real"
                        continue
                    else:
                        c_buffer += c
                        c_no += 1
                        possible_type = "error"
                        continue
                if c == "=":
                    buffer_match(possible_type, c_buffer, line_no, c_no)
                    c_buffer = ""
                    possible_type = ""
                    if line[c_no + 1] == '=':
                        buffer_match('==', '==', line_no, c_no)
                        c_no += 2
                        continue
                    else:
                        buffer_match('=', '=', line_no, c_no)
                        c_no += 1
                        continue
                possible_type = "error"
                c_no += 1
                c_buffer += c
            line = f.readline()
            line_no += 1
        buffer_match(possible_type, c_buffer, line_no - 1, c_start)
        return tokens, symbol_table, errors
    return [], {}, []

def main():
	file = sys.argv[1]
	tokens, symbols, errors = scanner(file)
	with open(file,'r') as source_code:
		lexer = Lexer()
		lexer.get_tokens(source_code)
		print("-------LEXER RESULT-------\n")
        	print("TOKENS:")
        	for token in tokens:
            		print(token)
        	print("\nSYMBOL TABLE:")
        	for key in lexer.symbol_table:
            		print("\"" + key +"\":", lexer.symbol_table[key])
        	print("\nERRORS:")
        	print(lexer.errors)

		if len(lexer.tokens) > 0 and len(lexer.errors) == 0:
             		err, result, syntax_symbols = parser(tokens, symbols)
	     		print("\n-------PARSER RESULT-------\n")
	     		if result:
		 		print("SYNTAX ACCEPTED")
	     		else:
		 		print("SYNTAX NOT ACCEPTED")
             		if err:
                 		print("Error: ", err)
            		else:
                	 	for scope in syntax_symbols:
					print(scope + ":")
             		 		for symbol in syntax_symbols[scope]:
                         			print("\"" + symbol +"\":", syntax_symbols[scope][symbol])


if __name__ == "__main__":
    main()
