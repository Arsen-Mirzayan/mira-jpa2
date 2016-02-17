package com.mira.jpa2.hibernate;

import org.hibernate.dialect.SQLServer2012Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import java.util.List;

public class ExtendedSQLServer2012Dialect extends SQLServer2012Dialect {

    public ExtendedSQLServer2012Dialect() {
        registerFunction("contains", new StandardSQLFunction("contains", StandardBasicTypes.BOOLEAN){
            @Override
            public String render(Type firstArgumentType, List arguments, SessionFactoryImplementor sessionFactory) {
                return super.render(firstArgumentType, arguments, sessionFactory)+" and 1";
            }
        });
    }
}
