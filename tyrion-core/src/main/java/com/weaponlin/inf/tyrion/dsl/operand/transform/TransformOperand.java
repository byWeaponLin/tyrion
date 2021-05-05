package com.weaponlin.inf.tyrion.dsl.operand.transform;

import com.weaponlin.inf.tyrion.dsl.operand.Operand;
import com.weaponlin.inf.tyrion.dsl.operand.expression.BetweenExpressionOperand;
import com.weaponlin.inf.tyrion.dsl.operand.expression.CompareExpressionOperand;
import com.weaponlin.inf.tyrion.dsl.operand.expression.ExpressionOperand;
import com.weaponlin.inf.tyrion.dsl.operand.expression.InExpressionOperand;
import com.weaponlin.inf.tyrion.dsl.operand.expression.LikeExpressionOperand;
import com.weaponlin.inf.tyrion.enums.ArithmeticOperator;
import com.weaponlin.inf.tyrion.enums.CompareOperator;
import com.weaponlin.inf.tyrion.enums.LikeOption;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class TransformOperand extends Operand {
    private static final long serialVersionUID = 8510012843009307113L;

    protected TransformOperand(String name) {
        super(name);
    }

    public CompareExpressionOperand ge(TransformOperand operand) {
        return new CompareExpressionOperand(this, CompareOperator.GE, operand);
    }

    public CompareExpressionOperand ge(Object object) {
        return ge(PlaceholderOperand.value(object));
    }

    public CompareExpressionOperand gt(TransformOperand operand) {
        return new CompareExpressionOperand(this, CompareOperator.GT, operand);
    }

    public CompareExpressionOperand gt(Object object) {
        return gt(PlaceholderOperand.value(object));
    }

    public CompareExpressionOperand eq(TransformOperand operand) {
        return new CompareExpressionOperand(this, CompareOperator.EQ, operand);
    }

    public CompareExpressionOperand eq(Object object) {
        return eq(PlaceholderOperand.value(object));
    }

    public CompareExpressionOperand neq(TransformOperand operand) {
        return new CompareExpressionOperand(this, CompareOperator.NEQ, operand);
    }

    public CompareExpressionOperand neq(Object object) {
        return neq(PlaceholderOperand.value(object));
    }

    public CompareExpressionOperand le(TransformOperand operand) {
        return new CompareExpressionOperand(this, CompareOperator.LE, operand);
    }

    public CompareExpressionOperand le(Object object) {
        return le(PlaceholderOperand.value(object));
    }

    public CompareExpressionOperand lt(TransformOperand operand) {
        return new CompareExpressionOperand(this, CompareOperator.LT, operand);
    }

    public CompareExpressionOperand lt(Object object) {
        return lt(PlaceholderOperand.value(object));
    }

    /**
     * {column} like '{parameter}'
     *
     * @param operand
     * @return
     */
    public LikeExpressionOperand like(VariableOperand operand) {
        decorateParameter(operand, CompareOperator.LIKE, LikeOption.NONE);
        return new LikeExpressionOperand(this, CompareOperator.LIKE, operand);
    }

    public LikeExpressionOperand like(Object object) {
        return like(PlaceholderOperand.value(object));
    }

    /**
     * {column} like '%{parameter}'
     *
     * @param operand
     * @return
     */
    public LikeExpressionOperand likeBefore(VariableOperand operand) {
        decorateParameter(operand, CompareOperator.LIKE, LikeOption.LEFT);
        return new LikeExpressionOperand(this, CompareOperator.LIKE, operand);
    }

    public LikeExpressionOperand likeBefore(Object object) {
        return likeBefore(PlaceholderOperand.value(object));
    }

    /**
     * {column} like '{parameter}%'
     *
     * @param operand
     * @return
     */
    public LikeExpressionOperand likeAfter(VariableOperand operand) {
        decorateParameter(operand, CompareOperator.LIKE, LikeOption.RIGHT);
        return new LikeExpressionOperand(this, CompareOperator.LIKE, operand);
    }

    public LikeExpressionOperand likeAfter(Object object) {
        return likeAfter(PlaceholderOperand.value(object));
    }


    /**
     * {column} like '%{parameter}%'
     *
     * @param operand
     * @return
     */
    public LikeExpressionOperand likeMiddle(VariableOperand operand) {
        decorateParameter(operand, CompareOperator.LIKE, LikeOption.ALL);
        return new LikeExpressionOperand(this, CompareOperator.LIKE, operand);
    }

    public LikeExpressionOperand likeMiddle(Object object) {
        return likeMiddle(PlaceholderOperand.value(object));
    }

    /**
     * {column} not like '{parameter}'
     *
     * @param operand
     * @return
     */
    public LikeExpressionOperand notLike(VariableOperand operand) {
        decorateParameter(operand, CompareOperator.NOT_LIKE, LikeOption.NONE);
        return new LikeExpressionOperand(this, CompareOperator.NOT_LIKE, operand);
    }

    public LikeExpressionOperand notLike(Object object) {
        return notLike(PlaceholderOperand.value(object));
    }

    /**
     * {column} not like '%{parameter}'
     *
     * @param operand
     * @return
     */
    public ExpressionOperand notLikeBefore(VariableOperand operand) {
        decorateParameter(operand, CompareOperator.NOT_LIKE, LikeOption.LEFT);
        return new LikeExpressionOperand(this, CompareOperator.NOT_LIKE, operand);
    }

    public ExpressionOperand notLikeBefore(Object object) {
        return notLikeBefore(PlaceholderOperand.value(object));
    }

    /**
     * {column} not like '{parameter}%'
     *
     * @param operand
     * @return
     */
    public LikeExpressionOperand notLikeAfter(VariableOperand operand) {
        decorateParameter(operand, CompareOperator.NOT_LIKE, LikeOption.RIGHT);
        return new LikeExpressionOperand(this, CompareOperator.NOT_LIKE, operand);
    }

    public LikeExpressionOperand notLikeAfter(Object object) {
        return notLikeAfter(PlaceholderOperand.value(object));
    }

    /**
     * {column} not like '%{parameter}%'
     *
     * @param operand
     * @return
     */
    public LikeExpressionOperand notLikeMiddle(VariableOperand operand) {
        decorateParameter(operand, CompareOperator.NOT_LIKE, LikeOption.ALL);
        return new LikeExpressionOperand(this, CompareOperator.NOT_LIKE, operand);
    }

    public LikeExpressionOperand notLikeMiddle(Object object) {
        return notLikeMiddle(PlaceholderOperand.value(object));
    }

    public CompareExpressionOperand isNull() {
        return new CompareExpressionOperand(this, CompareOperator.IS_NULL, null);
    }

    public CompareExpressionOperand isNotNull() {
        return new CompareExpressionOperand(this, CompareOperator.IS_NOT_NULL, null);
    }

    public InExpressionOperand in(TransformOperand operand) {
        return new InExpressionOperand(this, CompareOperator.IN, operand);
    }

    public InExpressionOperand in(Object... objects) {
        checkArgument(objects != null && objects.length > 0, "target values cant be null and length must great than 1");
        return in(PlaceholderOperand.values(objects));
    }

    public InExpressionOperand in(List<Object> objects) {
        checkArgument(objects != null && objects.size() > 0, "target values cant be null and length must great than 1");
        return in(PlaceholderOperand.values(objects));
    }

    public InExpressionOperand notIn(TransformOperand operand) {
        return new InExpressionOperand(this, CompareOperator.NOT_IN, operand);
    }

    public InExpressionOperand notIn(Object... objects) {
        checkArgument(objects != null && objects.length > 0, "target values cant be null and length must great than 1");
        return notIn(PlaceholderOperand.values(objects));
    }

    public InExpressionOperand notIn(List<Object> objects) {
        checkArgument(objects != null && objects.size() > 0, "target values cant be null and length must great than 1");
        return notIn(PlaceholderOperand.values(objects));
    }

    public BetweenExpressionOperand betweenAnd(TransformOperand value1, TransformOperand value2) {
        return new BetweenExpressionOperand(this, value1, value2);
    }

    public BetweenExpressionOperand betweenAnd(Object value1, Object value2) {
        return betweenAnd(PlaceholderOperand.value(value1), PlaceholderOperand.value(value2));
    }

    public ArithmeticOperand add(TransformOperand operand) {
        return new ArithmeticOperand(this, ArithmeticOperator.ADD, operand);
    }

    public ArithmeticOperand add(Object value) {
        return add(PlaceholderOperand.value(value));
    }

    public ArithmeticOperand minus(TransformOperand operand) {
        return new ArithmeticOperand(this, ArithmeticOperator.MINUS, operand);
    }

    public ArithmeticOperand minus(Object value) {
        return minus(PlaceholderOperand.value(value));
    }

    public ArithmeticOperand multiply(TransformOperand operand) {
        return new ArithmeticOperand(this, ArithmeticOperator.MULTIPLY, operand);
    }

    public ArithmeticOperand multiply(Object value) {
        return multiply(PlaceholderOperand.value(value));
    }

    public ArithmeticOperand divide(TransformOperand operand) {
        return new ArithmeticOperand(this, ArithmeticOperator.DIVIDE, operand);
    }

    public ArithmeticOperand divide(Object object) {
        return divide(PlaceholderOperand.value(object));
    }

    public ArithmeticOperand and(TransformOperand operand) {
        return new ArithmeticOperand(this, ArithmeticOperator.AND, operand);
    }

    public ArithmeticOperand and(Object value) {
        return and(PlaceholderOperand.value(value));
    }

    public ArithmeticOperand or(TransformOperand operand) {
        return new ArithmeticOperand(this, ArithmeticOperator.OR, operand);
    }

    public ArithmeticOperand or(Object value) {
        return or(PlaceholderOperand.value(value));
    }

    public abstract ExpressionOperand toExpression();

    @Override
    public TransformOperand as(String alias) {
        checkArgument(isNotBlank(alias), "alias can not be empty.");
        checkArgument(!alias.contains(" "), "alias can not exists space");
        this.alias = alias;
        return this;
    }

    @Override
    protected String decoratedAlias(boolean hasAlias) {
        return (hasAlias && isNotBlank(alias) ? " AS " + alias : "");
    }

    /**
     * only for like operation
     *
     * @param operand
     */
    private void decorateParameter(VariableOperand operand, CompareOperator operator, LikeOption likeOption) {
        checkArgument(operator == CompareOperator.LIKE || operator == CompareOperator.NOT_LIKE,
                "decorateParameter only support LIKE and NOT LIKE");
        checkNotNull(operand, "VariableOperand can not be null");
        List<Object> parameters = operand.getRealParameters();
        checkNotNull(parameters, "VariableOperand's parameters can not be null");
        String str = likeOption.format(parameters.get(0));
        if (operand instanceof ValueOperand) {
            str = "'" + str + "'";
        }
        parameters.set(0, str);
    }
}
