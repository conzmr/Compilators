# -*- coding: utf-8 -*-
import sys
sys.setrecursionlimit(200)

class Parser:
	def __init__(self, tokens, symbol_table, errors):
		self.tokens = []
		self.symbol_table = []
		self.errors = []
		self.current = None

	def parse(self, tokens, symbol_table, errors):
		self.tokens = tokens
		self.symbol_table = symbol_table
		self.errors = errors
		self.tree = {}
        symbol_table = {}
        current_err = { "depth": 0 }

def parser(tokens, symbol_table):
    new_symbol_table = {}
    current_err = { "level": 0 }

    def handle_err(message, token, level, can_have_error = False):
        err = {"SyntaticError: ": message, "Token": token, "Level": level}
        if level >= current_err["level"] and not can_have_error:
            current_err.update(err)
        return err
    def check_token(key, expected_value, error_message, level, c, declaration = False, scope = None, can_have_error = False):
        if tokens[c][key] == expected_value:
            if expected_value == "id" and declaration:
                idType = tokens[c - 1]["valor"]
                functionVariable = "Function" if tokens[c + 1]["valor"] == "(" else "Variable"
                symbol = {
                    'IDENTIFIER_TYPE': functionVariable,
                    'DATA_TYPE': idType,
                    'TYPE': tokens[c]["tipo"],
                    'VALUE': tokens[c]["valor"],
                    'LINE': tokens[c]["linea"],
                    'CHARACTER': tokens[c]["caracter"]
                }
                if new_symbol_table.get(scope):
                    new_symbol_table[scope][tokens[c]["valor"]] = symbol
                else:
                    new_symbol_table[scope] = {}
                    new_symbol_table[scope][tokens[c]["valor"]] = symbol
            return False, c + 1
        else:
            return handle_err(error_message, tokens[c], level, can_have_error), c

    def inicio(level, counter):
        func_err, c = bloque_de_funciones(level + 1, counter)
        err, c = principal(level + 1, c, "principal")
        if err:
            if func_err and func_err["level"] > err["level"]:
                return func_err, False
            return err, False
        return False, True
    def bloque_de_funciones(level, counter):
        err = False
        c = counter
        while not err:
            err, c = funcion(level + 1, c)
        if c > counter:
            return False, c
        else:
            return err, counter
    def funcion(level, counter):
        err, c = tipo_de_dato(level + 1, counter)
        if err:
            return err, counter
        scope = tokens[c]["valor"]
        err, c = check_token("tipo", "id", "id esperado", level, c, True, scope)
        if err:
            return err, counter
        err, c = check_token("valor", "(", "'(' esperado", level, c)
        if err:
            return err, counter
        err, c = argumentos(level + 1, c, scope)
        err, c = check_token("valor", ")", "')' esperado", level, c)
        if err:
            return err, counter

        err, c = check_token("valor", "{", "'{' esperado", level, c)
        if err:
            return err, counter
        err, c = bloque_de_codigo(level + 1, c, scope)
        if err:
            return err, counter
        err, c = check_token("valor", "regresa", "'regresa' esperado", level, c)
        if err:
            return err, counter
        err, c = check_token("tipo", "id", "id esperado", level, c)
        if err:
            return err, counter
        err, c = check_token("valor", ";", "';' esperado", level, c)
        if err:
            return err, counter
        err, c = check_token("valor", "}", "'}' esperado", level, c)
        if err:
            return err, counter
        return False, c
    def tipo_de_dato(level, counter):
        if tokens[counter]["valor"] in ["real", "entero", "logico"]:
            return False, counter + 1
        return handle_err("'tipo de dato' esperado", tokens[counter], level), counter
    def argumentos(level, counter, scope = None):
        err = False
        coma = True
        c = counter
        while not err:
            err, c = argumento(level + 1, c, scope)
            if not err:
                err, c = check_token("valor", ",", "',' esperada", level, c)
                if err:
                    coma = False
                    break
                coma = True
        if c > counter and not coma:
            return False, c
        else:
            return False, counter
    def argumento(level, counter, scope = None):
        err, c = tipo_de_dato(level + 1, counter)
        if err:
            return err, counter
        err, c = check_token("tipo", "id", "id esperado", level, c, True, scope)
        if err:
            return err, counter
        return False, c
    def bloque_de_codigo(level, counter, scope = None):
        err, c = declaraciones(level + 1, counter, scope)
        err, c = sentencias(level + 1, c)
        if err:
            return err, counter
        return False, c
    def declaraciones(level, counter, scope = None):
        err = False
        c = counter
        while not err:
            err, c = declaracion(level + 1, c, scope)
        if c > counter:
            return False, c
        else:
            return False, counter
    def declaracion(level, counter, scope = None):
        err, c = tipo_de_dato(level + 1, counter)
        if err:
            return err, counter
        err, c = check_token("tipo", "id", "id esperado", level, c, True, scope)
        if err:
            return err, counter
        err, c = check_token("tipo", ";", "';' esperado", level, c)
        if err:
            return err, counter
        return False, c
    def sentencias(level, counter):
        err = False
        c = counter
        while not err:
            err, c = sentencia(level + 1, c)
        if c > counter:
            return False, c
        else:
            return False, counter
    def sentencia(level, counter):
        err, c = si(level + 1, counter)
        if not err:
            return False, c
        err, c = mientras(level + 1, counter)
        if not err:
            return False, c
        err, c = asignacion(level + 1, counter)
        if not err:
            return False, c
        return err, counter
    def si(level, counter):
        err, c = check_token("valor", "si", "si esperado", level, counter)
        if err:
            return err, counter
        err, c = check_token("tipo", "(", "'(' esperado", level, c)
        if err:
            return err, counter
        err, c = check_token("tipo", "id", "id esperado", level, c)
        if err:
            return err, counter
        err, c = check_token("tipo", ")", "')' esperado", level, c)
        if err:
            return err, counter
        err, c = check_token("tipo", "{", "'{' esperado", level, c)
        if err:
            return err, counter
        err, c = sentencias(level + 1, c)
        err, c = check_token("tipo", "}", "'}' esperado", level, c)
        if err:
            return err, counter
        return False, c
    def mientras(level, counter):
        err, c = check_token("valor", "mientras", "si esperado", level, counter)
        if err:
            return err, counter
        err, c = check_token("tipo", "(", "'(' esperado", level, c)
        if err:
            return err, counter
        err, c = check_token("tipo", "id", "id esperado", level, c)
        if err:
            return err, counter
        err, c = check_token("tipo", ")", "')' esperado", level, c)
        if err:
            return err, counter
        err, c = check_token("tipo", "{", "'{' esperado", level, c)
        if err:
            return err, counter
        err, c = sentencias(level + 1, c)
        err, c = check_token("tipo", "}", "'}' esperado", level, c)
        if err:
            return err, counter
        return False, c
    def asignacion(level, counter):
        err, c = check_token("tipo", "id", "id esperado", level, counter)
        if err:
            return err, counter
        err, c = check_token("valor", "=", "'=' esperado", level, c)
        if err:
            return err, counter
        err, c = expresion(level + 1, c)
        if err:
            return err, counter
        return False, c
    def expresion(level, counter):
        err, c = aritmetica(level + 1, counter)
        if not err:
            err, c = check_token("valor", ";", "';' esperado", level, c)
            if not err:
                return False, c
        err, c = logica(level + 1, counter)
        if not err:
            err, c = check_token("valor", ";", "';' esperado", level, c)
            if not err:
                return False, c
        err, c = relacional(level + 1, counter)
        if not err:
            err, c = check_token("valor", ";", "';' esperado", level, c)
            if not err:
                return False, c
        err, c = check_token("valor", "!", "'!' esperado", level, counter)
        if err:
            return err, counter
        err, c = check_token("tipo", "id", "id esperado", level, c)
        if err:
            return err, counter
        err, c = check_token("valor", ";", "';' esperado", level, c)
        if err:
            return err, counter
        return False, c

    def aritmetica_anidada(level, counter):
        err, c = check_token("valor", "(", "'(' esperado", level, counter)
        if err:
            return err, counter
        err, c = aritmetica(level + 1, c)
        if err:
            return err, counter
        err, c = check_token("valor", ")", "')' esperado", level, c)
        if err:
            return err, counter
        return False, c

    def aritmetica(level, counter):
        if tokens[counter]["valor"] == "(":
            err, c = aritmetica_anidada(level + 1, counter)
            if not err:
                err_op, c_op = operador_aritmetico(level, c)
                if not err_op:
                    err, c = aritmetica(level + 1, c_op)
                    if err:
                        return err, c_op
                    return False, c
                else:
                    return False, c
            else:
                return err, counter
        err, c = valor_aritmetico(level + 1, counter)
        if not err:
            err_op, c_op = operador_aritmetico(level, c)
            if not err_op:
                err, c = aritmetica(level + 1, c_op)
                if err:
                    return err, c_op
                return False, c
            else:
                err, c_new = operador_relacional(level, c)
                if not err:
                    return {'level': level, 'mensaje': "Es relacional", 'token': tokens[c]}, counter
                err, c_new = operador_logico(level, c)
                if not err:
                    return {'level': level, 'mensaje': "Es logico", 'token': tokens[c]}, counter
                return False, c
        return err, counter


    def operador_aritmetico(level, counter):
        if tokens[counter]["valor"] in ["*", "+", "/", "^", "-"]:
            return False, counter + 1
        return handle_err("'operador aritmetico' esperado", tokens[counter], level), counter
    def valor_aritmetico(level, counter):
        err, c = llamada_funcion(level + 1, counter)
        if not err:
            return False, c
        err, c = check_token('tipo', 'id', 'id esperado', level, counter)
        if not err:
            return False, c
        err, c = check_token('tipo', 'real', 'lógico esperado', level, counter)
        if not err:
            return False, c
        err, c = check_token('tipo', 'entero', 'lógico esperado', level, counter)
        if not err:
            return False, c
        return err, counter


    def logica_anidada(level, counter):
        err, c = check_token("valor", "(", "'(' esperado", level, counter)
        if err:
            return err, counter
        err, c = logica(level + 1, c)
        if err:
            return err, counter
        err, c = check_token("valor", ")", "')' esperado", level, c)
        if err:
            return err, counter
        return False, c

    def logica(level, counter):
        if tokens[counter]["valor"] == "(":
            err, c = logica_anidada(level + 1, counter)
            if not err:
                err_op, c_op = operador_logico(level, c)
                if not err_op:
                    err, c = logica(level + 1, c_op)
                    if err:
                        return err, c_op
                    return False, c
                else:
                    return False, c
            else:
                return err, counter
        err, c = valor_logico(level + 1, counter)
        if not err:
            err_op, c_op = operador_logico(level, c)
            if not err_op:
                err, c = logica(level + 1, c_op)
                if err:
                    return err, c_op
                return False, c
            else:
                err, c_new = operador_relacional(level, c)
                if not err:
                    return {'level': level, 'mensaje': "Es relacional", 'token': tokens[c]}, counter
                err, c_new = operador_aritmetico(level, c)
                if not err:
                    return {'level': level, 'mensaje': "Es aritmetico", 'token': tokens[c]}, counter
                return False, c
        return err, counter


    def operador_logico(level, counter):
        if tokens[counter]["valor"] in ["&", "|"]:
            return False, counter + 1
        return handle_err("'operador lógico' esperado", tokens[counter], level), counter
    def valor_logico(level, counter):
        err, c = llamada_funcion(level + 1, counter)
        if not err:
            return False, c
        err, c = check_token('tipo', 'id', 'id esperado', level, counter)
        if not err:
            return False, c
        err, c = check_token('valor', 'verdadero', 'lógico esperado', level, counter)
        if not err:
            return False, c
        err, c = check_token('valor', 'falso', 'lógico esperado', level, counter)
        if not err:
            return False, c
        return err, counter
    def llamada_funcion(level, counter):
        err, c = check_token('tipo', 'id', 'id esperado', level, counter)
        if err:
            return err, counter
        err, c = check_token('valor', '(', "'(' esperado", level, c)
        if err:
            return err, counter
        err, c = argumentos_funcion(level + 1, c)
        err, c = check_token('valor', ')', "')' esperado", level, c)
        if err:
            return err, counter
        return False, c
    def argumentos_funcion(level, counter):
        err = False
        coma = True
        c = counter
        while not err:
            err, c = check_token("tipo", "id", "id esperado", level, c)
            if not err:
                err, c = check_token("valor", ",", "',' esperada", level, c)
                if err:
                    coma = False
                    break
                coma = True
        if c > counter and not coma:
            return False, c
        else:
            return False, counter
    def relacional(level, counter):
        err, c = valor_aritmetico(level + 1, counter)
        if err:
            return err, counter
        err, c = operador_relacional(level + 1, c)
        if err:
            return err, counter
        err, c = valor_aritmetico(level + 1, c)
        if err:
            return err, counter
        return False, c
    def operador_relacional(level, counter):
        if tokens[counter]["valor"] in ["<", ">", "=="]:
            return False, counter + 1
        return handle_err("'operador relacional' esperado", tokens[counter], level), counter
    def principal(level, counter, scope):
        err, c = check_token('valor', 'principal', "'principal' esperado", level, counter)
        if err:
            return err, counter
        err, c = check_token("tipo", "(", "'(' esperado", level, c)
        if err:
            return err, counter
        err, c = check_token("tipo", ")", "')' esperado", level, c)
        if err:
            return err, counter
        err, c = check_token("tipo", "{", "'{' esperado", level, c)
        if err:
            return err, counter
        err, c = bloque_de_codigo(level + 1, c, scope)
        if err:
            return err, counter
        err, c = check_token("tipo", "}", "'}' esperado", level, c)
        if err:
            return err, counter
        return False, c

    err, result = inicio(0, 0)
    if err:
        return err, False, None
    return False, True, new_symbol_table
