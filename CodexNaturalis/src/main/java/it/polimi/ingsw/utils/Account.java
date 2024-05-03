package it.polimi.ingsw.utils;

/**
 * This class represents an account with a username and password.
 */
public class Account {

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
}
