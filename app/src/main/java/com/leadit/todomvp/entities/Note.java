package com.leadit.todomvp.entities;

import java.util.Date;

/**
 * Note entity
 *
 * @author Mohamed Essid on 02/02/2017.
 */

public class Note {

    private Long id = null;
    private String message;
    private Date creationDate;

    public Note() {
    }

    /**
     * creates a note from message with system date and wihtout id
     *
     * @param message note message
     */
    public Note(String message) {
        this.message = message;
        this.creationDate = new Date();
    }

    /**
     * note constructor
     *
     * @param message
     * @param creationDate
     * @param id
     */
    public Note(Long id, String message, Date creationDate) {
        this.message = message;
        this.creationDate = creationDate;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
