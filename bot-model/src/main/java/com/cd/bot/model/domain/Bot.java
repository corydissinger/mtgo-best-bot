package com.cd.bot.model.domain;

import javax.persistence.*;

/**
 * Created by Cory on 5/20/2017.
 */
@MappedSuperclass
public abstract class Bot {
    @Id
    @GeneratedValue
    protected Long id;

    @Column(nullable = false)
    protected String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
