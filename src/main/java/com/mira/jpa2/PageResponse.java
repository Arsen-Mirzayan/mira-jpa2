package com.mira.jpa2;

import java.io.Serializable;
import java.util.List;

/**
 * Результат запроса на постраничное извлечение данных
 *
 * @param <T> класс объектов
 */
public final class PageResponse<T> implements Serializable {
    private final List<T> result;
    private final long page;
    private final long pageCount;
    private final long recordCount;

    public PageResponse(List<T> result, long page, long pageCount, long recordCount) {
        this.page = page;
        this.result = result;
        this.pageCount = pageCount;
        this.recordCount = recordCount;
    }

    public List<T> getResult() {
        return result;
    }

    public long getPage() {
        return page;
    }

    public long getPageCount() {
        return pageCount;
    }

    public long getRecordCount() {
        return recordCount;
    }
}
