package com.example.gymlogpractive.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.gymlogpractive.database.GymLogDatabase;

import java.util.Objects;

@Entity(tableName = GymLogDatabase.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String password;
    private boolean isAdmin;

    /**
     * Constructor for creating a new user with a username and password.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isAdmin = false;
    }

    /**
     * Checks equality based on id, username, password, and admin status.
     * Returns true if the given object matches this user.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && isAdmin == user.isAdmin && Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, isAdmin);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
