import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class KeyManagement {

    //create a place to store the encryption key of the user.
    //it creates a file in the E: DRIVE, it requires a pen drive.
    //this pen drive is then used for authentication purposes
    public void createStorageForKey() {
        try {
            File storage = new File("E:\\storage.db"); //create storage for key
            if (storage.createNewFile()) {
                System.out.println("Storage successful. Creating key."); //successful
            } else {
                System.out.println("Storage already exists."); //file already exists.
            }
        } catch (IOException e) {
            System.out.println("An error occurred."); //error catch
            e.printStackTrace();
        }
    }

    //generates a AES symmetric key that is then stored in the pen drive for physical authentication.
    //generates the initialisation vector that is used during encryption for decryption purposes.
    //requires that the storage inside pen drive is created first. Use createStoreForKey() this is handled prior during registration process.
    public void generateKeyIntoStorage() {
        try {
            SecretKey secretKey = new DatabaseManager().generateKey();
            byte[] secretKeyBytes = secretKey.getEncoded();
            IvParameterSpec initialisationVector = new DatabaseManager().generateIV();
            byte[] ivBytes = initialisationVector.getIV();
            Connection createTable = DriverManager.getConnection("jdbc:sqlite:E:\\storage.db");
            Connection insertKey = DriverManager.getConnection("jdbc:sqlite:E:\\storage.db");//connect to the storage.db It should've been stored at the pen drive. (I'm using E drive) it can be different, yet I won't focus on that as this project is proof of concept.
            Statement createKeyTable = createTable.createStatement();
            createKeyTable.execute("CREATE TABLE IF NOT EXISTS keystorage (secret_key VARBINARY NOT NULL, initialisation_vector VARBINARY NOT NULL);");
            PreparedStatement keyInsert = insertKey.prepareStatement("INSERT INTO keystorage (secret_key, initialisation_vector) VALUES (?, ?);");
            keyInsert.setBytes(1, secretKeyBytes);
            keyInsert.setBytes(2, ivBytes);
            keyInsert.execute();
            createTable.close();
            insertKey.close();
            createKeyTable.close();
            keyInsert.close();
        } catch (SQLException | NoSuchAlgorithmException e) {
            System.out.println("Error with connection/Key generator.");
            e.printStackTrace();
        }
    }

    //retrieves the initialisation vector to be used in the decryption of passwords in the database manager.
    public IvParameterSpec retrieveInitialisationVector(){
        IvParameterSpec initialisationVector = null;
        try {
            Connection retrieveIV = DriverManager.getConnection("jdbc:sqlite:E:\\storage.db");
            PreparedStatement ivRetrieval = retrieveIV.prepareStatement("SELECT initialisation_vector FROM keystorage;");
            ResultSet key = ivRetrieval.executeQuery();
            byte[] ivBytes = key.getBytes("initialisation_vector");
            initialisationVector = new IvParameterSpec(ivBytes);
            retrieveIV.close();
            ivRetrieval.close();
            key.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return initialisationVector;
    }

    //use this method to retrieve the secret key of the user from their pen drive.
    //this key can then be used to decrypt the passwords the user has stored in the database.
    //which then allows user to see the actual passwords.
    public SecretKey retrieveKey() {
        SecretKey secretKey = null;
        try {
            Connection retrieveKey = DriverManager.getConnection("jdbc:sqlite:E:\\storage.db");
            PreparedStatement keyRetrieval = retrieveKey.prepareStatement("SELECT secret_key FROM keystorage;");
            ResultSet key = keyRetrieval.executeQuery();
            byte[] keyBytes = key.getBytes("secret_key");
            secretKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
            retrieveKey.close();
            keyRetrieval.close();
            key.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return secretKey;
    }
}


