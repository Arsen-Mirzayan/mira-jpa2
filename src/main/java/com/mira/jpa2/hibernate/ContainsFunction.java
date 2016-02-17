package com.mira.jpa2.hibernate;

import org.hibernate.jpa.criteria.CriteriaBuilderImpl;
import org.hibernate.jpa.criteria.ParameterRegistry;
import org.hibernate.jpa.criteria.Renderable;
import org.hibernate.jpa.criteria.compile.RenderingContext;
import org.hibernate.jpa.criteria.expression.LiteralExpression;
import org.hibernate.jpa.criteria.expression.function.BasicFunctionExpression;

import javax.persistence.criteria.Expression;
import java.io.Serializable;

/**
 * Models a SQL <tt>CONTAINS</tt> expression.
 *
 * @author Steve Ebersole
 */
public class ContainsFunction extends BasicFunctionExpression<Boolean> implements Serializable {
    private final Expression<String> field;
    private final Expression<String> value;

    public ContainsFunction(CriteriaBuilderImpl criteriaBuilder, Expression<String> field, Expression<String> value) {
        super(criteriaBuilder, Boolean.class, "contains");
        this.field = field;
        this.value = value;
    }

    public ContainsFunction(
            CriteriaBuilderImpl criteriaBuilder,
            Expression<String> field,
            String value) {
        this(criteriaBuilder, field, new LiteralExpression<>(criteriaBuilder, value));
    }

    public Expression<String> getField() {
        return field;
    }

    public Expression<String> getValue() {
        return value;
    }

    public void registerParameters(ParameterRegistry registry) {
        Helper.possibleParameter(getField(), registry);
        Helper.possibleParameter(getValue(), registry);
    }


    @Override
    public String render(RenderingContext renderingContext) {
        return String.format("contains(%s,%s)=true"
                , ((Renderable) getField()).render(renderingContext)
                , ((Renderable) getValue()).render(renderingContext));
    }
}
