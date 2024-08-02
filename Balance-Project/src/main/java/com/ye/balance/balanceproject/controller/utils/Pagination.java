package com.ye.balance.balanceproject.controller.utils;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Pagination {

    private int current;
    private int total;
    private boolean first;
    private boolean last ;
    private String url;
    private List<Integer> pages;
    private static Map<String , String > params ;


    public boolean isShow(){
        return pages.size() > 1 ;
    }

    public static Builder builder(String url){
        return new Builder(url);
    }

    public static class Builder {

        private String url;
        private int current;
        private int total;
        private boolean first;
        private boolean last;
        private Map<String, String> params;

        public Builder(String url){
            this.url = url;
        }

        public<T> Builder page(Page<T> page){
            this.current = page.getNumber();
            this.total = page.getTotalPages();
            this.first = page.isFirst();
            this.last = page.isLast();
            return this;
        }

        // builder design pattern

        public Builder current(int current) {
            this.current = current;
            return this;
        }

        public Builder total(int total) {
            this.total = total;
            return this;
        }

        public Builder first(boolean first) {
            this.first = first;
            return this;
        }

        public Builder last(boolean last) {
            this.last = last;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder params ( Map<String , String > params ){
            this.params = params;
            return this;
        }

        public Pagination build(){
            return new Pagination(current, total, first, last , url , params);
        }
    }

    private  Pagination(int current , int total , boolean first , boolean last , String url , Map<String , String> params){
        super();
        this.current = current;
        this.total = total;
        this.first = first;
        this.last = last;
        this.url = url;
        this.params = params;
        pages = new ArrayList<>();
        pages.add(current);



        while ( pages.size() < 3 && pages.get(0) > 0 ){
            pages.add(0 , pages.get(0) -1 );
        }

        while ( pages.size() > 5 && pages.get(pages.size() -1 ) < total ){
            pages.add( pages.get(pages.size() -1 ) + 1 );
        }

        while ( pages.size () < 5 && pages.get(0) > 0 ){
            pages.add(0 , pages.get(0) -1 );
        }
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Integer> getPages() {
        return pages;
    }

    public void setPages(List<Integer> pages) {
        this.pages = pages;
    }
}
