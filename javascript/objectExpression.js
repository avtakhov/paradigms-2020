"use strict";


// Errors
function ParserError(message) {
    this.message = message;
}

ParserError.prototype = Object.create(Error.prototype);
ParserError.constructor = ParserError;
ParserError.prototype.name = 'ParserError';


function UnexpectedCharError(message) {
    this.message = message;
}

UnexpectedCharError.prototype = Object.create(ParserError.prototype);
UnexpectedCharError.constructor = UnexpectedCharError;
UnexpectedCharError.prototype.name = 'UnexpectedCharError';


function ParserArithmeticError(message) {
    this.message = message;
}

ParserArithmeticError.prototype = Object.create(ParserError.prototype);
ParserArithmeticError.constructor = ParserArithmeticError;
ParserArithmeticError.prototype.name = 'ParserArithmeticError';


function ExpressionError(message) {
    this.message = message;
}

ExpressionError.prototype = Object.create(Error.prototype);
ExpressionError.constructor = ExpressionError;
ExpressionError.prototype.name = 'ExpressionError';

function VariableError(message) {
    this.message = message;
}

VariableError.prototype = Object.create(ExpressionError.prototype);
VariableError.constructor = VariableError;
VariableError.prototype.name = 'VariableError';


function ArityError(message) {
    this.message = message;
}

ArityError.prototype = Object.create(ExpressionError.prototype);
ArityError.constructor = ArityError;
ArityError.prototype.name = 'ArityError';

function log(e) {
    // console.log("call " + e);
}

// Expression tree
function Operator(apply, strOperation, ...args) {
    log(1);
    this.args = args;
    this.apply = apply;
    this.strOperation = strOperation;
}

Operator.prototype.evaluate = function (x, y, z) {
    log(2);
    return this.apply(...this.args.map(i => i.evaluate(x, y, z)));
};

Operator.prototype.toString = function () {
    log(3);
    return this.args.reduce((ac, i) => ac + i.toString() + ' ', '') + this.strOperation;
};

Operator.prototype.prefix = function () {
    log(4);
    return "(" + this.strOperation + " " + this.args.map(i => i.prefix()).join(' ') + ")";
};

function Add() {
    log(5);
    if (arguments.length !== 2) {
        throw new ArityError("argument count error");
    }
    Operator.call(this, (a, b) => a + b, '+', ...arguments);
}

Add.prototype = Object.create(Operator.prototype);

function Subtract() {
    log(6);
    if (arguments.length !== 2) {
        throw new ArityError("argument count error");
    }
    Operator.call(this, (a, b) => a - b, '-', ...arguments);
}

Subtract.prototype = Object.create(Operator.prototype);

function Multiply() {
    log(7);
    if (arguments.length !== 2) {
        throw new ArityError("argument count error");
    }
    Operator.call(this, (a, b) => a * b, '*', ...arguments);
}

Multiply.prototype = Object.create(Operator.prototype);

function Divide() {
    log(8);
    if (arguments.length !== 2) {
        throw new ArityError("argument count error");
    }
    Operator.call(this, (a, b) => a / b, '/', ...arguments);
}

Divide.prototype = Object.create(Operator.prototype);

function Negate() {
    log(9);
    if (arguments.length !== 1) {
        throw new ArityError("argument count error");
    }
    Operator.call(this, x => -x, 'negate', ...arguments);
}

Negate.prototype = Object.create(Operator.prototype);

function calcSum(...args) {
    log(10);
    return args.reduce((ac, x) => ac + x, 0);
}

function Sum() {
    log(11);
    Operator.call(this, calcSum, "sum",...arguments);
}

Sum.prototype = Object.create(Operator.prototype);

function Avg() {
    log(12);
    Operator.call(this, (...args) => calcSum(...args) / Math.max(args.length, 1), "avg",...arguments);
}

Avg.prototype = Object.create(Operator.prototype);

function Const(c) {
    log(13);
    this.c = c;
}

Const.prototype.evaluate = function () {
    log(14);
    return this.c;
};

Const.prototype.toString = function () {
    log(15);
    return this.c.toString();
};

Const.prototype.prefix = Const.prototype.toString;

function Variable(str) {
    log(16);
    this.str = str;
    if ('x@y@z'.indexOf(str) === -1) {
        throw new VariableError("Invalid variable name: '" + str + "'");
    }
}

Variable.prototype.evaluate = function (x, y, z) {
    log(17);
    switch (this.str) {
        case 'x':
            return x;
        case 'y':
            return y;
        case 'z':
            return z;
    }
};

Variable.prototype.toString = function () {
    log(18);
    return this.str;
};
Variable.prototype.prefix = Variable.prototype.toString;

// Parser

const BaseParser = function () {
    let pos;
    let source;

    this.getPos = function () {
        return pos;
    };

    this.getSource = function () {
        return source;
    };

    this.setSource = function (value) {
        pos = 0;
        source = value;
    };

    this.next = function () {
        return source.charAt(pos++);
    };

    this.hasNext = function () {
        return pos < source.length;
    };
};

BaseParser.prototype.get = function () {
    return this.hasNext() ? this.getSource().charAt(this.getPos()) : '\0';
};

BaseParser.prototype.skipSpaces = function () {
    while (this.get() === ' ') {
        this.next();
    }
};

BaseParser.prototype.test = function (c) {
    this.skipSpaces();
    return this.get() === c;
};

BaseParser.prototype.expect = function (s) {
    for (let char of s) {
        if (this.get() !== char) {
            throw new UnexpectedCharError(this.getPos() + ": expected " + "'" + char + "', found " + (this.get() === '\0' ? 'EOF' : "'" + this.get() + "'"));
        }
        this.next();
    }
};

BaseParser.prototype.between = function (l, r) {
    return l <= this.get() && this.get() <= r;
}

const OPERATIONS = {
    '+': Add,
    '-': Subtract,
    '/': Divide,
    '*': Multiply,
    'negate': Negate,
    'sum' : Sum,
    'avg' : Avg
};

const Parser = function () {
    BaseParser.call(this);
};

Parser.prototype = Object.create(BaseParser.prototype);

Parser.prototype.parseExpression = function () {
    if (this.test('(')) {
        return this.parseHard();
    } else {
        return this.parseSimple();
    }
};

Parser.prototype.parseSimple = function () {
    if (this.between('0', '9') || this.test('-')) {
        return this.parseNumber();
    } else {
        return this.parseVariable();
    }
};

Parser.prototype.parseOperation = function () {
    this.skipSpaces();
    const sz = 6;
    for (let i = this.getPos() + sz; i > this.getPos(); i--) {
        let s = this.getSource().substring(this.getPos(), i);
        if (s in OPERATIONS) {
            this.expect(s);
            return OPERATIONS[s];
        }
    }
    throw new UnexpectedCharError(this.getPos() + ": Expected operator in (), found '" + this.get() + "'");
};

Parser.prototype.parseNumber = function () {
    let res = this.test('-') ? this.next() : '';
    while (this.between('0', '9')) {
        res += this.next();
    }
    if (res.length === 0 || res === '-') {
        throw new UnexpectedCharError(this.getPos() + ": " + "unknown symbol " + (this.test('\0') ? "EOF" : "'" + this.get() + "'"));
    }
    return new Const(+res);
};

Parser.prototype.parseHard = function () {
    this.expect('(');
    let op = this.parseOperation();

    if (op === undefined) {
        throw new UnexpectedCharError(this.getPos() + ": " + "unknown operator " + this.get());
    }

    if (this.get() !== '(' && this.get() !== ' ') {
        throw new UnexpectedCharError(this.getPos() + ": " + "missing ' ' after operation");
    }
    let a = [];
    while (this.hasNext() && !this.test(')')) {
        a.push(this.parseExpression());
    }
    this.expect(')');
    return new op(...a);
};

Parser.prototype.parseVariable = function () {
    let res = '';
    while (this.between('a', 'z') || this.between('A', 'Z') || this.get() === '_') {
        res += this.next();
    }
    if (res.length === 0) {
        throw new UnexpectedCharError(this.getPos() + ": " + "unknown symbol " + (this.test('\0') ? "EOF" : "'" + this.get() + "'"));
    }
    return new Variable(res);
};

Parser.prototype.parse = function (s) {
    this.setSource(s);
    let ans;
    try {
        ans = this.parseExpression();
    } catch (e) {

        if (e instanceof ExpressionError) {
            throw new ParserArithmeticError(this.getPos() + ": " + e.message);
        } else {
            throw e;
        }

    }


    this.skipSpaces();
    if (this.hasNext()) {
        throw new UnexpectedCharError(this.getPos() + ": Unexpected symbol " + "'" + this.get() + "'");
    }
    return ans;
};

let p = new Parser();

const parsePrefix = function (s) {
    return p.parse(s);
};

