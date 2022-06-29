"use strict";

let eps = 1e-8;
let average = (...args) => args.reduce((a, b) => a + b, 0) / args.length;
let median = (...args) => args.sort((a, b) => a - b < 0 ? -1: (a - b === 0 ? 0 : 1))[Math.floor(args.length / 2 + eps)];

let cnst = x => () => x;
let variable = name => (...args) => args[supportedVariables.get(name)];
let operation = (func, ...operands) => (...args) => func(...operands.map((curr) => curr(...args)));
let subtract = (f1, f2) => operation((a, b) => a - b, f1, f2);
let add = (f1, f2) => operation((a, b) => a + b, f1, f2);
let multiply = (f1, f2) => operation((a, b) => a * b, f1, f2);
let divide = (f1, f2) => operation((a, b) => a / b, f1, f2);
let negate = f1 => operation(a => -a, f1);
let avg3 = (f1, f2, f3) => operation(average, f1, f2, f3);
let med5 = (f1, f2, f3, f4, f5) => operation(median, f1, f2, f3, f4, f5);
let pi = () => Math.PI;
let e = () => Math.E;

const supportedVariables = new Map([['x', 0], ['y', 1], ['z', 2]]);
const supportedFunctions = new Map([['+', add], ['-', subtract], ['*', multiply], ['/', divide],
    ['negate', negate], ['avg3', avg3], ['med5', med5]]);
const supportedConstants = new Map([['e', e], ['pi', pi]]);

let stack = [];

let apply = func => stack.push(func(...stack.splice(-func.length)));

let parse = function (expression) {
    let elements = expression.trim().split(/\s+/);
    elements.map( current => {
            supportedVariables.has(current) ? stack.push(variable(current)) : (
                supportedConstants.has(current) ? stack.push(supportedConstants.get(current)) : (
                    supportedFunctions.has(current) ? apply(supportedFunctions.get(current)) : (
                        stack.push(cnst(parseInt(current)))
                    )
                )
            );
        }
    )
    return stack.pop();
}

let sampleExpr = add(
    subtract(
        multiply(
            variable("x"),
            variable("x")
        ),
        multiply(
            cnst(2),
            variable("x")
        )
    ),
    cnst(1)
);

for (let i = 0; i <= 10; i++) {
    println(sampleExpr(i, 0, 0));
}
