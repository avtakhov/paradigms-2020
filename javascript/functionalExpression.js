"use strict";

const cnst = value => x => value;
const variable = () => x => x;
const add = (a, b) => x => a(x) + b(x);
const subtract = (a, b) => x => a(x) - b(x);
const divide = (a, b) => x => a(x) / b(x);
const multiply = (a, b) => x => a(x) * b(x);
const parseSimple = input => input.trim() === "x" ? variable("x") : cnst(+input);

const operations = {
    '+': add,
    '-': subtract,
    '/': divide,
    '*': multiply
};

const parse = s => {
    let a = s.split(' ').filter(x => x !== '');
    let st = [];
    for (let i of a) {
        if (i in operations) {
            let second = st.pop();
            let first = st.pop();
            st.push(operations[i](first, second));
        } else {
            st.push(parseSimple(i));
        }
    }
    return st[0];
};