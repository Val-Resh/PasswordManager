# Password Manager!

Corrently works with Windows machines. Compatibility wasn't the focus. Rather, it was about creating a full fledge project.
Master password is saved as a salted hash. Meanwhile, accounts password is encrypted with a physical pen drive as authentication. 

The compiled version of the Password Manager is at *PasswordManager/PasswordManager/out/artifacts/PasswordManager_jar/* which includes the SQLite database.<br>
Requires a Java Virtual Environment to run. This can be downloaded at https://www.oracle.com/java/technologies/downloads/

## Password Documentation:

```
gen        | Usage: gen length              | Example: gen 9                   | Generates a random password of desired length.
-------------------------------------------------------------------------------------------------------------------------------
logout     | Usage: logout                  |                                  | Logs the user out.                           
-------------------------------------------------------------------------------------------------------------------------------
exit       | Usage: exit                    |                                  | Exit the application.                       
-------------------------------------------------------------------------------------------------------------------------------
goto       | Usage: goto website            | Example: goto www.facebook.com   | Opens the website in default browser          
--------------------------------------------------------------------------------------------------------------------------------
add        | Usage: add                     |                                  | Use to add to the database. It will open a prompt
           |                                |                                  | with different parameters that need to be added
           |                                |                                  | such as website/username/password.
--------------------------------------------------------------------------------------------------------------------------------
showall    | Usage: showall                 |                                  | Shows all the stored website/username/passwords.
--------------------------------------------------------------------------------------------------------------------------------
showith    | Usage: showith -flag username  | Example: showith -u username123  | Find the credentials for a specific username or
           |                                |                                  | website.
           |                                |                                  | Flags:
           |                                |                                  | -u for username
           |                                |                                  | -w for website
--------------------------------------------------------------------------------------------------------------------------------
del        | Usage: del ID                  | Example: del 2                   | Deletes a row from database with ID.
--------------------------------------------------------------------------------------------------------------------------------
