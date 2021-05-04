package com.weaponlin.inf.tyrion.dsl.operand.control;

import com.weaponlin.inf.tyrion.dsl.operand.Operand;
import com.weaponlin.inf.tyrion.dsl.operand.expression.ExpressionOperand;

import java.util.List;

public abstract class ControlOperand extends Operand {
    private static final long serialVersionUID = 5542597921702882179L;

    ControlOperand(String name) {
        super(name);
    }

    public abstract List<ExpressionOperand> getResultExpression();
}
