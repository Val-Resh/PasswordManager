
import com.avaya.smgr.tm.util.PasswordController;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import java.sql.*;

public class DatabaseManager {
    private final String databaseLocation = "jdbc:sqlite:database.db";

    public DatabaseManager() throws NoSuchAlgorithmException {
    }

    // check whether a user already exists.
    // It returns true if there user in database. Returns false if user doesn't exist in the database.
    // Before any user is added to a database ensure that checkUserInDB returns false.
    public boolean checkUserInDB(String username){
        int counter = 0;
        try {
            Connection dbconnect = DriverManager.getConnection(databaseLocation);
            PreparedStatement query = dbconnect.prepareStatement("SELECT * FROM users WHERE username = ?;");
            query.setString(1, username);
            ResultSet result = query.executeQuery();
            while (result.next()) {
                counter++;
            }
            dbconnect.close();
            query.close();
            result.close();
        }
        catch (SQLException e){
            System.out.println("Unable to connect to database.");
            e.printStackTrace();
        }
        return counter != 0; // if counter is not zero then user exists in database. Should return true. If counter is zero, then it isn't in database, should return false.
    }

    // generate a random user ID and ensure that it doesn't already exist in the database.
    // All User ID have length size 6.
    public int generateUserID() {
        Random random = new Random();
        String stringID = "";
        int userID = 0;
        for (int i = 0; i < 6; i++){
            int temp = random.nextInt(10) + 48; // using ASCII values for easier conversion from int to string
            stringID += Character.toString((char) temp);
        }

        try{
            userID = Integer.parseInt(stringID);
        }
        catch (NumberFormatException e){
            e.printStackTrace();
        }
        if (!existsID(userID)) return userID; //returns the ID if there is no such ID already in database.
        else return -1; // error return
    }

    //helper function to check if ID already exists in database.
    //returns true if ID exists and false otherwise.
    private boolean existsID(int userID){
        int counter = 0;
        try {
            Connection dbconnect = DriverManager.getConnection(databaseLocation);
            PreparedStatement query = dbconnect.prepareStatement("SELECT id FROM users WHERE id = ?;");
            query.setInt(1, userID);
            ResultSet result = query.executeQuery();
            while (result.next()) {
                counter++;
            }
            dbconnect.close();
            query.close();
            result.close();
        }
        catch (SQLException e){
            System.out.println("Unable to connect to database.");
            e.printStackTrace();
        }
        return counter != 0; // if counter is not 0 then ID exists in database.
    }

    //retrieve the salt from database of a user via their ID. This salt can then be used to verify password during login.
    //this method assumes the user does exist in the database.
    public byte[] getUserSalt(int userID){
        byte[] salt = new byte[16];
        try {
            Connection dbconnect = DriverManager.getConnection(databaseLocation);
            PreparedStatement query = dbconnect.prepareStatement("SELECT salt FROM users WHERE id = ?;");
            query.setInt(1, userID);
            ResultSet result = query.executeQuery();
            while (result.next()) {
                salt = result.getBytes("salt");
            }
            dbconnect.close();
            query.close();
            result.close();
        }
        catch (SQLException e){
            System.out.println("Unable to connect to database.");
            e.printStackTrace();
        }
        return salt;
    }

    //get the ID of a registered user.
    //method assumes user exists in database.
    public int getUserID(String username){
        int userID = 0;
        try {
            Connection dbconnect = DriverManager.getConnection(databaseLocation);
            PreparedStatement query = dbconnect.prepareStatement("SELECT id FROM users WHERE username = ?;");
            query.setString(1, username);
            ResultSet getID = query.executeQuery();
            while (getID.next()) {
                userID = getID.getInt("id");
            }
            dbconnect.close();
            query.close();
            getID.close();
        }
        catch (SQLException e){
            System.out.println("Unable to connect to database.");
            e.printStackTrace();
        }
        return userID;
    }
    //method to register a new user into the database. It assumes that all the previous validity checks were already made.
    public void registerUser(String username, String hashedPassword, int userID, byte[] salt){
        try {
            Connection dbconnect = DriverManager.getConnection(databaseLocation);
            PreparedStatement insert = dbconnect.prepareStatement("INSERT INTO users (id, username, password, salt) VALUES(?, ?, ?, ?);");
            insert.setInt(1, userID);
            insert.setString(2, username);
            insert.setString(3, hashedPassword);
            insert.setBytes(4, salt);
            insert.execute();
            dbconnect.close();
            insert.close();
        }
        catch (SQLException e){
            System.out.println("Unable to connect to database.");
            e.printStackTrace();
        }
    }
    //return the hashed password from database using a user ID.
    //this is a helper function for verifying login.
    private String getUserPassword(int userID){
        String hashedPassword = null;
        try {
            Connection dbconnect = DriverManager.getConnection(databaseLocation);
            PreparedStatement query = dbconnect.prepareStatement("SELECT password FROM users WHERE id = ?;");
            query.setInt(1, userID);
            ResultSet getPassword = query.executeQuery();
            while (getPassword.next()) {
                hashedPassword = getPassword.getString("password");
            }
            dbconnect.close();
            query.close();
            getPassword.close();
        }
        catch (SQLException e){
            System.out.println("Unable to connect to database.");
            e.printStackTrace();
        }
        return hashedPassword;
    }
    //verify the login credentials. Main focus is verify the password.
    //returns true if credentials match. False otherwise.
    public boolean verifyLogin(int userID, String password) throws NoSuchAlgorithmException {
        PasswordController controller = new PasswordController();
        byte[] salt = getUserSalt(userID);
        String hashedPassword = controller.hashPassword(password, salt);
        String databasePassword = getUserPassword(userID);
        return hashedPassword.equals(databasePassword);
    }

    //generates a AES 256 bit symmetric key for encryption and decryption of passwords on database for the password manager.
    //There are a few better options that I considered.
    //However as this is a personal proof of concept project I couldn't afford better options.
    //in a professional scenario I'd rather use asymmetric encryption with physical authentication device. Yet, for this project this will do :)
    public SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }
    //generate a initialisation vector for the encryption/decryption of the passwords in the manager.
    public IvParameterSpec generateIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    //encryption method. It uses the secret key, IV to encrypt the password of users in manager.
    //passwords in database should be encrypted.
    //Important: The master password should be HASHED. NOT ENCRYPTED.
    //Only encrypt passwords inside the manager that requires the verification of hashed password.
    public String encrypt(String input, SecretKey secretKey, IvParameterSpec initialisationVector) throws NoSuchPaddingException, NoSuchAlgorithmException
            , BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, initialisationVector);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }
    //the method of decryption. This is used to decrypt the encrypted passwords before showing them to the user.
    //It makes them readable for user.
    public String decrypt(String encryptedMessage, SecretKey secretKey, IvParameterSpec initialisationVector) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, initialisationVector);
        byte[] decryptedMessage = cipher.doFinal(Base64.getDecoder()
                .decode(encryptedMessage));
        return new String(decryptedMessage);
    }

    //allows the users to add their website/username/password to the database.
    //uses physical pen drive with secret key to encrypt the passwords stored in the database so only the user is able to read them.
    public void addToManager(int userID, String website, String username, String password, SecretKey secretKey, IvParameterSpec initialisationVector) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        String encryptedPassword = encrypt(password, secretKey, initialisationVector);
        try {
            Connection dbconnect = DriverManager.getConnection(databaseLocation);
            PreparedStatement add = dbconnect.prepareStatement("INSERT INTO manager (user_id, website, username, password) VALUES(?, ?, ?, ?)");
            add.setInt(1, userID);
            add.setString(2, website);
            add.setString(3, username);
            add.setString(4, encryptedPassword);
            add.execute();
            dbconnect.close();
            add.close();
        }
        catch (SQLException e){
            System.out.println("Unable to connect to database.");
            e.printStackTrace();
        }
    }
    //shows the user all of their stored website/username/password combinations.
    //requires the pen drive to use the secret key to decrypt the passwords.
    public void readAll(String username, SecretKey secretKey, IvParameterSpec initialisationVector){
        try {
            Connection dbconnect = DriverManager.getConnection(databaseLocation);
            PreparedStatement select = dbconnect.prepareStatement("SELECT * FROM manager WHERE user_id = ?;");
            select.setInt(1, getUserID(username));
            ResultSet results = select.executeQuery();
                while (results.next()){
                    System.out.println(
                            "------------------------------------------------------------------------------------------------------------\n"
                                    +"ID: " + results.getInt("id") + " | Website: " + results.getString("website") +
                                    " | Username: " + results.getString("username") +
                                    " | Password: " + decrypt(results.getString("password"), secretKey, initialisationVector) +"\n" +
                                    "------------------------------------------------------------------------------------------------------------\n");
                }
            dbconnect.close();
            select.close();
            results.close();
        }
        catch (SQLException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            System.out.println("Unable to connect to database.");
            e.printStackTrace();
        }
    }
    //shows all website/username/password combinations with username or website of user choice.
    //This is used to check for a username/password for a specific website or find the password for a specific username.
    //this is good if your database is large and showall shows too many resutls.
    public void readWithInput(int userID, String input, SecretKey secretKey, IvParameterSpec initialisationVector, String parameter){
        try {
            Connection dbconnect = DriverManager.getConnection(databaseLocation);
            PreparedStatement select;
            if (parameter.equals("username")) {
                select = dbconnect.prepareStatement("SELECT * FROM manager WHERE username = ? AND user_id = ?;");
            }
            else {
                select = dbconnect.prepareStatement("SELECT * FROM manager WHERE website = ? AND user_id = ?;");
            }
            select.setString(1, input);
            select.setInt(2, userID);
            ResultSet results = select.executeQuery();
            while (results.next()){
                System.out.println(
                        "------------------------------------------------------------------------------------------------------------\n"
                                +"ID: " + results.getInt("id") +
                                " | Website: " + results.getString("website") +
                                " | Username: " + results.getString("username") +
                                " | Password: " + decrypt(results.getString("password"), secretKey, initialisationVector) +"\n" +
                                "------------------------------------------------------------------------------------------------------------\n");
            }
            dbconnect.close();
            select.close();
            results.close();
        }
        catch (SQLException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            System.out.println("Unable to connect to database.");
            e.printStackTrace();
        }
    }
    //deletes an entry from the user's storage.
    //it varies the user_id to ensure that user isn't able to delete the entries of other users in the system.
    public void deleteEntry(int id, String username){
        try {
            Connection dbconnect = DriverManager.getConnection(databaseLocation);
            PreparedStatement delete = dbconnect.prepareStatement("DELETE FROM manager WHERE id = ? AND user_id = ?");
            delete.setInt(1, id);
            delete.setInt(2, getUserID(username));
            delete.execute();
            dbconnect.close();
            delete.close();
        }
        catch (SQLException e) {
            System.out.println("Unable to connect to database.");
            e.printStackTrace();
        }
    }
}
