package com.mul1sh.easyprint.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

//This is our table name
@Table(name = "Pushnotifications")
public class Notification extends Model {

    @Column(name = "title")
    public String title;

    @Column(name = "body")
    public String body;

}
