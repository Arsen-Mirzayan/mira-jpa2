package com.mira.jpa2;

import java.io.Serializable;

/**
 * Запрос на постраничное извлечение данных
 */
public final class PageRequest<T> implements Serializable {
    public static final long DEFAULT_PAGE_SIZE = 12;
    private long page;
    private long pageSize;
    private Orders<T> orders;

    /**
     * Создаёт запрос с указанием номер страницы. Размер страницы устанавливается по умолчанию.
     *
     * @param page   номер страницы
     * @param orders условие сортировки
     */
    public PageRequest(long page, Orders<T> orders) {
        this(page, DEFAULT_PAGE_SIZE, orders);
    }

    /**
     * Создаёт запрос с указанием номера страницы и размера страниц.
     *
     * @param page     номер страницы
     * @param pageSize размер страниц
     */
    public PageRequest(long page, long pageSize, Orders<T> orders) {
        this.page = page;
        this.pageSize = pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE;
        this.orders = orders;
    }

    /**
     * @return номер страницы
     */
    public long getPage() {
        return page;
    }

    /**
     * @return размер страниц
     */
    public long getPageSize() {
        return pageSize;
    }

    /**
     * @return параметры сортировки
     */
    public Orders<T> getOrders() {
        return orders;
    }

    /**
     * @return начальная позиция
     */
    public long getStartPosition() {
        return page * pageSize;
    }
}
