package it.sevenbits.spring_homework.core.model;

public class TasksPaginationMeta {

    private int total;
    private int page;
    private int size;
    private String next;
    private String prev;
    private String first;
    private String last;

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
}
