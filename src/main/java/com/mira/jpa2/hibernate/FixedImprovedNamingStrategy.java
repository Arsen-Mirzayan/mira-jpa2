package com.mira.jpa2.hibernate;

import org.hibernate.AssertionFailure;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.internal.util.StringHelper;

import static org.hibernate.internal.util.StringHelper.isNotEmpty;

/**
 * Подправленная стратегия именования объектов базы данных
 */
public class FixedImprovedNamingStrategy extends ImprovedNamingStrategy {
    /**
     * Проверяет является ли второй аргумент множественным числом первого аргумента
     *
     * @param single слово в единственном числе
     * @param plural слово во множественном
     * @return {@code true} если является
     */
    private boolean isPlural(String single, String plural) {
        return isNotEmpty(single) && isNotEmpty(plural) && plural.equalsIgnoreCase(single + "s");
    }

    @Override
    public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName) {
        String header = (propertyName != null && !isPlural(propertyTableName, propertyName)
                ? StringHelper.unqualify(propertyName)
                : propertyTableName) + "_" + referencedColumnName;
        if (header == null) throw new AssertionFailure("FixedImprovedNamingStrategy not properly filled");
        return columnName(header);
    }

    @Override
    public String logicalCollectionTableName(String tableName, String ownerEntityTable, String associatedEntityTable, String propertyName) {
        if (isPlural(associatedEntityTable, propertyName)) {
            propertyName = associatedEntityTable;
        }
        return super.logicalCollectionTableName(tableName, ownerEntityTable, associatedEntityTable, propertyName);
    }

    @Override
    public String collectionTableName(String ownerEntity, String ownerEntityTable, String associatedEntity, String associatedEntityTable, String propertyName) {
        if (isPlural(associatedEntityTable, propertyName)) {
            propertyName = associatedEntityTable;
        }
        return super.collectionTableName(ownerEntity, ownerEntityTable, associatedEntity, associatedEntityTable, propertyName);
    }


}
