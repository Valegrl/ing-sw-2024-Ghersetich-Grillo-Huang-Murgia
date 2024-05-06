package it.polimi.ingsw.utils;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents an account with a username and password.
 */
public class Account implements Serializable {

    /**
     * The username of the account.
     */
    private final String username;

    /**
     * The password of the account.
     */
    private final String password;

    /**
     * Constructor for the Account class with a username and password.
     * It initializes the username and password with the provided values.
     *
     * @param username The username of the account.
     * @param password The password of the account.
     */
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Default constructor for the Account class.
     * It initializes the username and password with null.
     */
    public Account() {
        this.username = null;
        this.password = null;
    }

    /**
     * @return The username of the account.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return The password of the account.
     */
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    /**
     * Checks if this Account is equal to another object.
     * An object is equal to this Account if it is also an Account and both have the same username and password.
     *
     * @param o The object to compare this Account to.
     * @return true if the other object is equal to this Account, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(username, account.username) &&
                Objects.equals(password, account.password);
    }

    /**
     * Generates a hash code for this Account.
     * The hash code is generated based on the username and password of this Account.
     *
     * @return The generated hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
