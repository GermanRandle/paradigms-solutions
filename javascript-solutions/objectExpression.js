"use strict"

function initObjectExpr(constr, evaluate, toString, diff, prefix, postfix) {
    let objExpr = constr;
    objExpr.prototype.evaluate = evaluate;
    objExpr.prototype.toString = toString;
    objExpr.prototype.diff = diff;
    objExpr.prototype.prefix = prefix;
    objExpr.prototype.postfix = postfix;
    return objExpr;
}

function constConstr(value) { this.value = parseInt(value) }
function constEvaluate() { return this.value }
function constToString() { return '' + this.value }
function constPrefix() { return '' + this.value }
function constPostfix() { return '' + this.value }
const Const = initObjectExpr(constConstr, constEvaluate, constToString, () => ZERO, constPrefix, constPostfix);

const ZERO = new Const(0);
const ONE = new Const(1);
const TWO = new Const(2);

function variableConstr(name) { this.name = name }
function variableEvaluate(x, y, z) { return this.name === 'x' ? x : this.name === 'y' ? y : z }
function variableToString() { return this.name }
function variablePrefix() { return this.name }
function variablePostfix() { return this.name }
function variableDiff(difVar) { return difVar === this.name ? ONE : ZERO }
const Variable = initObjectExpr(variableConstr, variableEvaluate, variableToString, variableDiff, variablePrefix, variablePostfix);

function operationConstr(...args) { this.operands = args }
function operationEvaluate(x, y, z) {
    let calculated = [];
    for (let i = 0; i < this.operands.length; i++) {
        calculated.push(this.operands[i].evaluate(x, y, z));
    }
    return this.doOperation(...calculated);
}
function operationToString() {
    let result = '';
    for (let i = 0; i < this.operands.length; i++) {
        result += (i > 0 ? ' ' : '') + this.operands[i].toString();
    }
    return result + ' ' + this.sign;
}
function operationPrefix() {
    let result = '(' + this.sign;
    for (let i = 0; i < this.operands.length; i++) {
        result += ' ' + this.operands[i].prefix();
    }
    if (this.operands.length === 0) {
        result += ' ';
    }
    return result + ')';
}
function operationPostfix() {
    let result = '(';
    for (let i = 0; i < this.operands.length; i++) {
        result += this.operands[i].postfix() + ' ';
    }
    if (this.operands.length === 0) {
        result += ' ';
    }
    return result + this.sign + ')';
}
function operationDiff() {
    throw new Error("Can't differentiate abstract operation");
}
const Operation = initObjectExpr(operationConstr, operationEvaluate, operationToString, operationDiff, operationPrefix, operationPostfix);

function initOperation(func, sign, diff) {
    function NewOp(...args) { Operation.call(this, ...args) }
    NewOp.prototype = Object.create(Operation.prototype);
    NewOp.prototype.constructor = NewOp;
    NewOp.prototype.doOperation = func;
    NewOp.prototype.sign = sign;
    NewOp.prototype.diff = diff;
    NewOp.prototype.argsCount = func.length;
    return NewOp;
}

function addDiff(difVar) {
    return new Add(this.operands[0].diff(difVar), this.operands[1].diff(difVar));
}
const Add = initOperation((a, b) => a + b, "+", addDiff);

function subDiff(difVar) {
    return new Subtract(this.operands[0].diff(difVar), this.operands[1].diff(difVar));
}
const Subtract = initOperation((a, b) => a - b, "-", subDiff);

function mulDiff(difVar) {
    return new Add(new Multiply(this.operands[0].diff(difVar), this.operands[1]),
        new Multiply(this.operands[0], this.operands[1].diff(difVar)));
}
const Multiply = initOperation((a, b) => a * b, "*", mulDiff);

function divDiff(difVar) {
    return new Divide(new Subtract(new Multiply(this.operands[0].diff(difVar), this.operands[1]),
            new Multiply(this.operands[0], this.operands[1].diff(difVar))),
        new Multiply(this.operands[1], this.operands[1]));
}
const Divide = initOperation((a, b) => a / b, "/", divDiff);

function negDiff(difVar) {
    return new Negate(this.operands[0].diff(difVar));
}
const Negate = initOperation((a) => (-a), "negate", negDiff);

function expDiff(difVar) {
    return new Multiply(new Exp(this.operands[0]), this.operands[0].diff(difVar));
}
const Exp = initOperation((p) => Math.exp(p), "exp", expDiff);

function GaussDiff(difVar) {
    let innerExpr = new Subtract(this.operands[3], this.operands[1]);
    let gaussExpr = new Multiply(
        this.operands[0],
        new Exp(new Negate(new Divide(
            new Multiply(
                innerExpr,
                innerExpr
            ),
            new Multiply(
                TWO,
                new Multiply(this.operands[2], this.operands[2])
            )
        )))
    );
    return gaussExpr.diff(difVar);
}
const Gauss = initOperation((a, b, c, x) => a * Math.exp(-((x - b) * (x - b) / (2 * c * c))), 'gauss', GaussDiff);

function SumexpDiff(difVar) {
    let result = (new Exp(this.operands[0])).diff(difVar);
    for (let i = 1; i < this.operands.length; i++) {
        result = new Add(result, (new Exp(this.operands[i])).diff(difVar))
    }
    return result.diff(difVar);
}
const sumexpCalc = (...args) => args.reduce((prev, cur) => prev + Math.exp(cur), 0);
const Sumexp = initOperation(sumexpCalc, 'sumexp', SumexpDiff);

function SoftmaxDiff(difVar) {
    return (new Divide(new Exp(this.operands[0]), new Sumexp(...this.operands))).diff(difVar);
}
let softmaxCalc = (...args) => Math.exp(args[0]) / sumexpCalc(...args);
const Softmax = initOperation(softmaxCalc, 'softmax', SoftmaxDiff);

let stack = [];

let apply = func => stack.push(new func(...stack.splice(-func.prototype.argsCount)));

const supportedVariables = new Map([['x', 0], ['y', 1], ['z', 2]]);
const supportedOperations = new Map([['+', Add], ['-', Subtract], ['*', Multiply],
    ['/', Divide], ['negate', Negate], ['gauss', Gauss], ['sumexp', Sumexp], ['softmax', Softmax]]);

function parse(expression) {
    let elements = expression.trim().split(/\s+/);
    elements.map( current => {
            supportedVariables.has(current) ? stack.push(new Variable(current)) : (
                supportedOperations.has(current) ? apply(supportedOperations.get(current)) : (
                    stack.push(new Const(current))
                )
            );
        }
    )
    return stack.pop();
}

class ParseError extends Error {
    constructor(message, pos) {
        super("Parsing error on position #" + pos + ": " + message);
    }
}

class UnexpectedSymbolError extends ParseError {
    constructor(message, pos) {
        super(message, pos);
    }
}

class OperationSignatureError extends ParseError {
    constructor(message, pos) {
        super(message, pos);
    }
}

class Parser {
    constructor(exp, mode) {
        this.exp = exp;
        this.boolMode = true;
        if (mode === "prefix") {
            this.pos = 0;
            this.boolMode = true;
        } else if (mode === "suffix") {
            this.pos = this.exp.length - 1;
            this.boolMode = false;
        } else {
            this.error(ParseError, "Unknown parsing mode");
        }
    }

    shift() {
        this.pos += (this.boolMode? 1 : -1);
    }

    isNotOutOfBound() {
        return this.pos >= 0 && this.pos < this.exp.length;
    }

    skipWhitespaces() {
        while (this.isNotOutOfBound() && this.exp[this.pos] === ' ') {
            this.shift();
        }
    }

    error(type, message) {
        throw new type(message, this.pos + 1);
    }

    checkSplit() {
        return !this.isNotOutOfBound() || this.exp[this.pos] === ' ' ||
            this.exp[this.pos] === '(' || this.exp[this.pos] === ')';
    }

    getToken(mode) {
        this.skipWhitespaces();
        if (this.isNotOutOfBound() && (this.exp[this.pos] === '(' || this.exp[this.pos] === ')')) {
            let curPos = this.pos;
            this.shift();
            return this.exp[curPos];
        }
        let token = '';
        while (!this.checkSplit()) {
            let curPos = this.pos;
            this.shift();
            token += this.exp[curPos];
        }
        if (!this.boolMode) {
            token = token.split("").reverse().join("");
        }
        return token;
    }

    isEndOfInput() {
        return this.pos === (this.boolMode? this.exp.length : -1);
    }

    parse() {
        let parsed = this.parseRecursive();
        this.skipWhitespaces();
        if (!this.isEndOfInput()) {
            this.error(UnexpectedSymbolError, "Expected end on input, but found \'" + this.exp[this.pos] + '\'', this.pos);
        }
        return parsed;
    }

    isOpening(token) {
        return token === (this.boolMode? "(" : ")");
    }

    isClosing(token) {
        return token === (this.boolMode? ")" : "(");
    }

    parseRecursive() {
        let token = this.getToken();
        if (this.isOpening(token)) {
            let inner = [];
            let func = this.parseRecursive();
            if (!supportedOperations.has(func)) {
                this.error(OperationSignatureError, "Expected operation's sign");
            }
            func = supportedOperations.get(func);
            const lastId = () => (this.boolMode? (inner.length - 1) : 0);
            while (inner.length === 0 || !this.isClosing(inner[lastId()])) {
                if (func.prototype.argsCount > 0 && inner.length > func.prototype.argsCount) {
                    this.error(OperationSignatureError, "Too many arguments for operation " + func.prototype.sign);
                }
                if (this.boolMode) {
                    inner.push(this.parseRecursive());
                } else {
                    inner.unshift(this.parseRecursive());
                }
                if (supportedOperations.has(inner[lastId()])) {
                    this.error(OperationSignatureError, "Expected argument");
                }
            }
            if (this.boolMode) {
                inner.pop();
            } else {
                inner.shift();
            }
            if (inner.length < func.prototype.argsCount) {
                this.error(OperationSignatureError, "Too few arguments for operation " + func.prototype.sign);
            }
            return new func(...inner);
        } else if (this.isClosing(token)) {
            return token;
        } else if (token === '') {
            this.error(UnexpectedSymbolError, "Unexpected eof");
        } else if (!isNaN(token)) {
            return new Const(token);
        } else if (supportedVariables.has(token)) {
            return new Variable(token);
        } else if (supportedOperations.has(token)) {
            return token;
        } else {
            this.error(UnexpectedSymbolError, "Unknown token: \'" + token + '\'');
        }
    }
}

let parseWithExceptions = (exp, mode) => (new Parser(exp, mode)).parse();
let parsePrefix = (exp) => parseWithExceptions(exp, "prefix");
let parsePostfix = (exp) => parseWithExceptions(exp, "suffix");
