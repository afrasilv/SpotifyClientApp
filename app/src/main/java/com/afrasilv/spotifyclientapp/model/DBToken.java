package com.afrasilv.spotifyclientapp.model;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "DBTOKEN".
 */
public class DBToken {

    private Long id;
    /** Not-null value. */
    private String tokenvalue;

    public DBToken() {
    }

    public DBToken(Long id) {
        this.id = id;
    }

    public DBToken(Long id, String tokenvalue) {
        this.id = id;
        this.tokenvalue = tokenvalue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getTokenvalue() {
        return tokenvalue;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTokenvalue(String tokenvalue) {
        this.tokenvalue = tokenvalue;
    }

}
