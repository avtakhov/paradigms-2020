"use strict";

function Binary(first, second, apply, strOperation) {
    this.first = first;
    this.second = second;
    this.apply = apply;
    this.strOperation = strOperation;
}

Binary.prototype.evaluate = function (x, y, z) {
    return this.apply(this.first.evaluate(x, y, z), this.second.evaluate(x, y, z));
};

Binary.prototype.toString = function () {
    return this.first.toString() + " " + this.second.toString() + " " + this.strOperation;
};

function Add(first, second) {
    Binary.call(this, first, second, (a, b) => a + b, '+');
    // return new Binary(first, second, (a, b) => a + b, '+');
}
Add.prototype = Object.create(Binary.prototype);

function Subtract(first, second) {
    Binary.call(this, first, second, (a, b) => a - b, '-');
}
Subtract.prototype = Object.create(Binary.prototype);

function Multiply(first, second) {
    Binary.call(this, first, second, (a, b) => a * b, '*');
}
Multiply.prototype = Object.create(Binary.prototype);

function Divide(first, second) {
    Binary.call(this, first, second, (a, b) => a / b, '/');
}
Divide.prototype = Object.create(Binary.prototype);

function Unary(first, apply, strOperation) {
    this.first = first;
    this.apply = apply;
    this.strOperation = strOperation;
}

Unary.prototype.evaluate = function (x, y, z) {
    return this.apply(this.first.evaluate(x, y, z));
};

Unary.prototype.toString = function () {
    return this.first.toString() + " " + this.strOperation;
};

function Negate(first) {
    Unary.call(this, first, x => -x, 'negate');
    // return new Unary(first, x => -x, 'negate');
}
Negate.prototype = Object.create(Unary.prototype);

function Const(c) {
    this.c = c;
}

Const.prototype.evaluate = function (x, y, z) {
    // println(this.c);
    return this.c;
};

Const.prototype.toString = function () {
    return this.c.toString();
};

function Variable(str) {
    this.str = str;
}

Variable.prototype.evaluate = function () {
    return arguments['xyz'.indexOf(this.str)];
};

Variable.prototype.toString = function () {
    return this.str;
};