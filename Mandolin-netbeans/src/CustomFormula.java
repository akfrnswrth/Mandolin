/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.util.Scanner;
import java.util.Stack;

/**
 * Custom formula generated from string
 * (An expression tree)
 * @author ali.kocaturk.1
 */
public class CustomFormula implements Formula {

    private FormulaNode exTree;
    private VariableNode z; // NOTE: VariableNodes are NOT THREAD-SAFE
    private VariableNode c; // Again, NOT THREAD-SAFE

    /**
     * Constructs a CustomFormula according to the given postfix expression
     * @param sex
     */
    public CustomFormula(String sex) {
        z = new VariableNode();
        c = new VariableNode();
        exTree = buildExpression(sex);
        exTree.simplify();
    }

    /**
     * Sets the expression represented by this CustomFormula according to the
     * given postfix expression
     * @param sex
     * @throws IllegalArgumentException if an operand is missing or an operator
     * is unrecognized
     */
    public void setExpression(String sex) throws
            IllegalArgumentException {
        exTree = buildExpression(sex);
        exTree.simplify();
    }

    /**
     * Builds a FormulaNode according to the given postfix expression
     * @param sex
     * @return a FormulaNode representing the root of the expression tree
     * @throws IllegalArgumentException if an operand is missing or an operator
     * is unrecognized
     */
    private FormulaNode buildExpression(String sex)
            throws IllegalArgumentException {
        Scanner sexscn = new Scanner(sex);
        Stack<String> sexstk = new Stack<String>();

        while (sexscn.hasNext()) {
            sexstk.push(sexscn.next());
        }

        return buildExpression(sexstk);
    }

    /**
     * Builds a FormulaNode from the given expression stack.  NOTE: will empty
     * the stack.
     * @param sexstk
     * @return a FormulaNode representing the root of the new expression tree
     * @throws IllegalArgumentException if an operand is missing (stack
     * unexpectedly empty) or an operator is unrecognized
     */
    private FormulaNode buildExpression(Stack<String> sexstk)
            throws IllegalArgumentException {
        if (sexstk.isEmpty()) {
            return null;
        }
        String sop = sexstk.pop();
        if (sop.equals("z")) {
            return z;
        } else if (sop.equals("c")) {
            return c;
        } else if (sop.equals("i")) {
            return new ExpressionNode(new Complex(0, 1));
        } else if (sop.equals("*") || sop.equals("+") || sop.equals("/")
                || sop.equals("-") || sop.equals("^")) { // binary operators
            FormulaNode ren = buildExpression(sexstk);
            FormulaNode len = buildExpression(sexstk);
            if (len == null || ren == null) {
                throw new IllegalArgumentException("Malformed expression");
            }
            return new ExpressionNode(sop, len, ren);
        } else if (sop.equals("tan") || sop.equals("sin") || sop.equals("cos")
                || sop.equals("atan") || sop.equals("asin") || sop.equals("acos")
                || sop.equals("tanh") || sop.equals("sinh") || sop.equals("cosh")
                || sop.equals("atanh") || sop.equals("asinh") || sop.equals("acosh")
                || sop.equals("log") || sop.equals("ln") || sop.equals("abs")
                || sop.equals("mod") || sop.equals("abs2") || sop.equals("re")
                || sop.equals("im") || sop.equals("re") || sop.equals("arg")
                || sop.equals("conj") || sop.equals("exp")) {  // unary operators
            FormulaNode len = buildExpression(sexstk);
            if (len == null) {
                throw new IllegalArgumentException("Malformed expression");
            }
            return new ExpressionNode(sop, len, null);
        } else {
            try {
                return new ExpressionNode(new Complex(Double.parseDouble(sop), 0));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Unrecognized operator: " + sop);
            }
        }
    }

    /**
     * Evaluates the formula for the given values of z and c.  NOTE: This
     * method is not thread safe; if calc() is called while a previous calc() on
     * the same object is not finished, the z and c values may have changed
     * partway through the calculation.
     * @param z
     * @param c
     * @return
     */
    public Complex calc(Complex z, Complex c) {
        this.z.setValue(z);
        this.c.setValue(c);
        return exTree.evaluate();
    }

    /**
     * An interface specifying methods for an evaluable node of the expression
     * tree
     */
    private interface FormulaNode extends Serializable {

        /**
         * Evaluates this node.
         * @return the result of evaluation
         */
        public Complex evaluate();

        /**
         * Determines whether the value of this node will change if any
         * VariableNodes in the expressionTree are modified.  A constant node
         * will always return true and a VariableNode will always return false.
         * An ExpressionNode will return false unless all of its children return
         * true.
         * @return true if evaluate() will always return the same value,
         * otherwise false
         */
        public boolean isConstant();

        /**
         * Simplifies this node and its children to reduce operations that
         * always return the same value.
         */
        public void simplify();
    }

    /**
     * A node that is either an operation (with children) or a numeric value.
     */
    private class ExpressionNode implements FormulaNode {

        private Complex numeric;
        private String operator;
        private FormulaNode left;
        private FormulaNode right;

        /**
         * Constructs an ExpressionNode with the given numeric value
         * @param n 
         */
        public ExpressionNode(Complex n) {
            numeric = n;
            operator = null;
            left = right = null;
        }

        /**
         * Constructs an operator ExpressionNode
         * @param op the operation to be performed
         * @param l the left parameter of the operation
         * @param r the right parameter of the operation, if applicable
         */
        public ExpressionNode(String op, FormulaNode l, FormulaNode r) {
            operator = op;
            numeric = null;
            left = l;
            right = r;
        }

        /**
         * Evaluates this ExpressionNode and, if necessary, its children.
         * @return the result of evaluation
         */
        public Complex evaluate() {
            if (numeric != null) {
                return numeric;
            } else if (operator.equals("+")) {
                return left.evaluate().add(right.evaluate());
            } else if (operator.equals("*")) {
                return left.evaluate().mult(right.evaluate());
            } else if (operator.equals("-")) {
                return left.evaluate().sub(right.evaluate());
            } else if (operator.equals("/")) {
                return left.evaluate().div(right.evaluate());
            } else if (operator.equals("^")) {
                return left.evaluate().pow(right.evaluate());
            } else if (operator.equals("sin")) {
                return left.evaluate().sin();
            } else if (operator.equals("cos")) {
                return left.evaluate().cos();
            } else if (operator.equals("tan")) {
                return left.evaluate().tan();
            } else if (operator.equals("asin")) {
                return left.evaluate().asin();
            } else if (operator.equals("acos")) {
                return left.evaluate().acos();
            } else if (operator.equals("atan")) {
                return left.evaluate().atan();
            } else if (operator.equals("sinh")) {
                return left.evaluate().sinh();
            } else if (operator.equals("cosh")) {
                return left.evaluate().cosh();
            } else if (operator.equals("tanh")) {
                return left.evaluate().tanh();
            } else if (operator.equals("asinh")) {
                return left.evaluate().asinh();
            } else if (operator.equals("acosh")) {
                return left.evaluate().acosh();
            } else if (operator.equals("atanh")) {
                return left.evaluate().atanh();
            } else if (operator.equals("log") || operator.equals("ln")) {
                return left.evaluate().log();
            } else if (operator.equals("abs") || operator.equals("mod")) {
                return new Complex(left.evaluate().abs(), 0);
            } else if (operator.equals("abs2")) {
                return new Complex(left.evaluate().abs(), 0);
            } else if (operator.equals("re")) {
                return new Complex(left.evaluate().getReal(), 0);
            } else if (operator.equals("im")) {
                return new Complex(left.evaluate().getImag(), 0);
            } else if (operator.equals("sqrt")) {
                return left.evaluate().sqrt();
            } else if (operator.equals("arg")) {
                return new Complex(left.evaluate().arg(), 0);
            } else if (operator.equals("exp")) {
                return left.evaluate().exp();
            } else if (operator.equals("conj")) {
                return left.evaluate().conj();
            }
            throw new IllegalArgumentException("Function " + operator
                    + " not implemented");
        }

        /**
         * Determines whether this ExpressionNode will always evaluate to the
         * same value
         * @return true if the evaluation is constant, otherwise false
         */
        public boolean isConstant() {
            return numeric != null || left.isConstant() && (right == null || right.isConstant());
        }

        /**
         * If this ExpressionNode is constant, it will be simplified to a
         * numeric.  Otherwise, its children will be simplified.
         */
        public void simplify() {
            if (numeric != null) {
                return;
            } else if (left.isConstant() && (right == null || right.isConstant())) {
                numeric = evaluate();
            } else {
                left.simplify();
                if (right != null) {
                    right.simplify();
                }
            }
        }

        /**
         * NOTE: Never used
         * @return the left child
         */
        public FormulaNode getLeft() {
            return left;
        }

        /**
         * NOTE: Never used
         * @return the right child
         */
        public FormulaNode getRight() {
            return right;
        }

        /**
         * Sets the left child.  
         * NOTE: Never used
         * @param l the new left child
         */
        public void setLeft(FormulaNode l) {
            left = l;
        }

        /**
         * Sets the right child.  
         * Note: Never used
         * @param r the new right child
         */
        public void setRight(FormulaNode r) {
            right = r;
        }
    }

    /**
     * A FormulaNode containing an adjustable value
     */
    private class VariableNode implements FormulaNode {

        private Complex value;

        /**
         * Constructs a VariableNode with an initial value of 0 + 0i
         */
        public VariableNode() {
            value = new Complex();
        }

        /**
         * Sets the value of this VariableNode to the given value
         * @param v 
         */
        public void setValue(Complex v) {
            value = v;
        }

        /**
         * Gets the value of this VariableNode
         * @return 
         */
        public Complex evaluate() {
            return value;
        }

        /**
         * Returns false because a VariableNode is variable, not constant.
         * @return false
         */
        public boolean isConstant() {
            return false;
        }

        /**
         * Does nothing because a variable cannot be simplified to a constant
         * value.
         */
        public void simplify() {
        }
    }
}
