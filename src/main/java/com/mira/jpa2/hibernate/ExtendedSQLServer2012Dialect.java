package com.mira.jpa2.hibernate;

import org.hibernate.dialect.SQLServer2012Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import java.sql.Types;
import java.util.List;

public class ExtendedSQLServer2012Dialect extends SQLServer2012Dialect {

  public ExtendedSQLServer2012Dialect() {
    registerColumnType(Types.CHAR, "nchar(1)");
    registerColumnType(Types.LONGVARCHAR, "nvarchar(max)");
    registerColumnType(Types.VARCHAR, 4000, "nvarchar($l)");
    registerColumnType(Types.VARCHAR, "nvarchar(max)");
    registerColumnType(Types.CLOB, "nvarchar(max)");

    registerColumnType(Types.NCHAR, "nchar(1)");
    registerColumnType(Types.LONGNVARCHAR, "nvarchar(max)");
    registerColumnType(Types.NVARCHAR, 4000, "nvarchar($l)");
    registerColumnType(Types.NVARCHAR, "nvarchar(max)");
    registerColumnType(Types.NCLOB, "nvarchar(max)");

    registerHibernateType(Types.NCHAR, StandardBasicTypes.CHARACTER.getName());
    registerHibernateType(Types.LONGNVARCHAR, StandardBasicTypes.TEXT.getName());
    registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
    registerHibernateType(Types.NCLOB, StandardBasicTypes.CLOB.getName());

    registerFunction("contains", new StandardSQLFunction("contains", StandardBasicTypes.BOOLEAN) {
      @Override
      public String render(Type firstArgumentType, List arguments, SessionFactoryImplementor sessionFactory) {
        return super.render(firstArgumentType, arguments, sessionFactory) + " and 1";
      }
    });
  }
}
