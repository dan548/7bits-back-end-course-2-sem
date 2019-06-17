package it.sevenbits.spring_homework.core.model;

import java.util.Objects;

/**
 * Meta class for task pagination.
 */
public class TasksPaginationMeta {

    private int total;
    private int page;
    private int size;
    private String next;
    private String prev;
    private String first;
    private String last;

    /**
     * Constructs a meta.
     * @param total number of tasks
     * @param page current page
     * @param size size of a page
     * @param next next page link (if present)
     * @param prev previous page link (if present)
     * @param first first page link
     * @param last last page link
     */
    public TasksPaginationMeta(final int total, final int page, final int size,
                               final String next, final String prev, final String first, final String last) {
        this.total = total;
        this.page = page;
        this.size = size;
        this.next = next;
        this.prev = prev;
        this.first = first;
        this.last = last;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(final int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(final int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    public String getNext() {
        return next;
    }

    public void setNext(final String next) {
        this.next = next;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(final String prev) {
        this.prev = prev;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(final String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(final String last) {
        this.last = last;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TasksPaginationMeta that = (TasksPaginationMeta) o;
        return total == that.total &&
                page == that.page &&
                size == that.size &&
                Objects.equals(next, that.next) &&
                Objects.equals(prev, that.prev) &&
                Objects.equals(first, that.first) &&
                Objects.equals(last, that.last);
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, page, size, next, prev, first, last);
    }
}
