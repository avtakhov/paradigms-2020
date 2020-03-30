"use strict";

const cnst = value => () => value;

const variable = (s) => function () {
//     println(s);
//     println(arguments[0] + " " + arguments[1] + " " + arguments[2]);
    return arguments["xyz".indexOf(s)];
};

const binary = fun => (a, b) => (x, y, z) => fun(a(x, y, z), b(x, y ,z));

const add = binary((a, b) => a + b);
const subtract = binary((a, b) => a - b);
const multiply = binary((a, b) => a * b);
const divide = binary((a, b) => a / b);

const binaryOperations = {
    '+': add,
    '-': subtract,
    '/': divide,
    '*': multiply
};

const unary = fun => a => (x, y, z) => fun(a(x, y, z));

const sin = unary((a) => Math.sin(a));
const cos = unary((a) => Math.cos(a));
const negate = unary((a) => -a);
const pi = cnst(Math.PI);
const e = cnst(Math.E);

const unaryOperations = {
    'sin': sin,
    'cos': cos,
    'negate': negate
}

const constants = {
    'e': e,
    'pi': pi
};

function isDigit(s) {
    if (s.length === 0) {
        return false;
    }
    let start = s.charAt(0) === '-' ? 1 : 0;
    for (let i = start; i < s.length; i++) {
        if (!('0' <= s.charAt(i) && s.charAt(i) <= '9')) {
            return false;
        }
    }
    return true;
}

const parse = s => {
    let a = s.split(' ').filter(x => x !== '');
    let st = [];
    for (let i of a) {
        if (i in binaryOperations) {
            let second = st.pop();
            let first = st.pop();
            st.push(binaryOperations[i](first, second));
        } else if (i in unaryOperations) {
            st.push(unaryOperations[i](st.pop()));
        } else if (i in constants) {
            st.push(constants[i]);
        } else {
            st.push(isDigit(i) ? cnst(+i) : variable(i));
        }
    }
    return st[0];
};