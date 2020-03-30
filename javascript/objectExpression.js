"use strict";

function Operator(apply, strOperation, ...args) {
    this.args = args;
    this.apply = apply;
    this.strOperation = strOperation;
}

Operator.prototype.evaluate = function (x, y, z) {
    return this.apply(...this.args.map(i => i.evaluate(x, y, z)));
};

Operator.prototype.toString = function () {
    return this.args.reduce((ac, i) => ac + i.toString() + ' ', '') + this.strOperation;
};

Operator.prototype.prefix = function () {
    return "(" + this.args.reduce((ac, i) => ac + ' ' + i.prefix(), this.strOperation) + ")";
};

function Add(first, second) {
    Operator.call(this, (a, b) => a + b, '+', first, second);
}

Add.prototype = Object.create(Operator.prototype);

function Subtract(first, second) {
    Operator.call(this, (a, b) => a - b, '-', first, second);
}

Subtract.prototype = Object.create(Operator.prototype);

function Multiply(first, second) {
    Operator.call(this, (a, b) => a * b, '*', first, second);
}

Multiply.prototype = Object.create(Operator.prototype);

function Divide(first, second) {
    Operator.call(this, (a, b) => a / b, '/', first, second);
}

Divide.prototype = Object.create(Operator.prototype);

function Negate(first) {
    Operator.call(this, x => -x, 'negate', first);
}

Negate.prototype = Object.create(Operator.prototype);

function Const(c) {
    this.c = c;
}

Const.prototype.evaluate = function () {
    return this.c;
};

Const.prototype.toString = function () {
    return this.c.toString();
};

Const.prototype.prefix = Const.prototype.toString;


function Variable(str) {
    this.str = str;
    if (!(str in this.names)) {
        throw new Error("Invalid variable name: '" + str + "'");
    }
}

Variable.prototype.names = {"x" : 1, "y" : 1, "z" : 1};

Variable.prototype.evaluate = function (x, y, z) {
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
    return this.str;
};
Variable.prototype.prefix = Variable.prototype.toString;

function isLetter(ch) {
    return 'a' <= ch && ch <= 'z';
}

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

    this.get = function () {
        return source.charAt(pos);
    };

    this.next = function () {
        return source.charAt(pos++);
    };

    this.skipSpaces = function () {
        while (this.get() === ' ') {
            this.next();
        }
    };

    this.expectString = function (s) {
        for (let char of s) {
            if (this.get() !== char) {
                throw new Error("expected " + char);
            }
            this.next();
        }
    };

    this.ended = function () {
        this.skipSpaces();
        return pos >= source.length;
    };
};

const Operation = function (operation, count) {
    this.operation = operation;
    this.count = count;
};

const OPERATIONS = {
    '+': new Operation(Add, 2),
    '-': new Operation(Subtract, 2),
    '/': new Operation(Divide, 2),
    '*': new Operation(Multiply, 2),
    "negate": new Operation(Negate, 1)
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

const Parser = function () {
    BaseParser.call(this);

    this.parseNumber = function () {
        let res = this.get() === '-' ? this.next() : '';
        while (isDigit(this.get())) {
            res += this.next();
        }
        return new Const(+res);
    };

    this.parse = function (s) {
        this.setSource(s);
        let ans = this.parseExpression();
        if (this.ended()) {
            return ans;
        } else {
            throw new Error("Unexpected symbol");
        }
    };

    this.parseVariable = function () {
        let res = '';
        while (isLetter(this.get()) || this.get() === '_') {
            res += this.next();
        }
        return new Variable(res);
    };

    this.parseSimple = function () {
        this.skipSpaces();
        if (isDigit(this.get() || this.get() === '-')) {
            return this.parseNumber();
        } else {
            return this.parseVariable();
        }
    };

    this.parseOperation = function () {
        this.skipSpaces();
        const sz = 10;
        for (let i = this.getPos() + sz; i > this.getPos(); i--) {
            let s = this.getSource().substring(this.getPos(), i);
            if (s in OPERATIONS) {
                this.expectString(s);
                return OPERATIONS[s];
            }
        }
        return undefined;
    };

    this.parseExpression = function () {
        this.skipSpaces();
        if (this.get() === '(') {
            return this.parseHard();
        } else {
            return this.parseSimple();
        }
    };

    this.parseHard = function () {
        this.skipSpaces();
        this.expectString('(');
        let op = this.parseOperation();
        if (this.get() !== '(' && this.get() !== ' ') {
            throw new Error("missing ' ' after operation");
        }
        let a = [];
        for (let i = 0; i < op.count; i++) {
            a.push(this.parseExpression());
        }
        this.skipSpaces();
        this.expectString(')');
        return new op.operation(...a);
    };
};

let p = new Parser();

const parsePrefix = function(s) {
    return p.parse(s);
};

let c = new Add(new Variable('x'), new Const(2));

console.log(c.toString());
