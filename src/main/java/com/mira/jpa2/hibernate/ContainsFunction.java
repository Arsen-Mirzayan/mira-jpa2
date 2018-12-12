package com.mira.jpa2.hibernate;


import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.ParameterContainer;
import org.hibernate.query.criteria.internal.ParameterRegistry;
import org.hibernate.query.criteria.internal.Renderable;
import org.hibernate.query.criteria.internal.compile.RenderingContext;
import org.hibernate.query.criteria.internal.expression.LiteralExpression;
import org.hibernate.query.criteria.internal.expression.function.BasicFunctionExpression;

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
        ParameterContainer.Helper.possibleParameter(getField(), registry);
        ParameterContainer.Helper.possibleParameter(getValue(), registry);
    }


    @Override
    public String render(RenderingContext renderingContext) {
        return String.format("contains(%s,%s)=true"
                , ((Renderable) getField()).render(renderingContext)
                , ((Renderable) getValue()).render(renderingContext));
    }
}
