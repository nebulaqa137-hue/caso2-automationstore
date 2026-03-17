package utils;

import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

/**
 * Utility - RandomDataHelper
 * Genera datos aleatorios para pruebas usando JavaFaker
 */
public class RandomDataHelper {

    private static final Logger log = LogManager.getLogger(RandomDataHelper.class);
    private static final Faker faker = new Faker(new Locale("en"));
    private static final long TIMESTAMP = System.currentTimeMillis();

    private RandomDataHelper() {}

    public static String randomUsername() {
        String user = faker.name().firstName().toLowerCase()
                    + "_" + TIMESTAMP % 10000;
        log.info("Username generado: " + user);
        return user;
    }

    public static String randomPassword() {
        return "Pass" + faker.number().digits(4) + "!";
    }

    public static String randomEmail() {
        return faker.internet().emailAddress()
               .replace("@", "_" + TIMESTAMP % 10000 + "@");
    }

    public static String randomFirstName() {
        return faker.name().firstName();
    }

    public static String randomLastName() {
        return faker.name().lastName();
    }

    public static String randomPhone() {
        return faker.phoneNumber().cellPhone();
    }

    public static String randomCity() {
        return faker.address().city();
    }

    public static String randomAddress() {
        return faker.address().streetAddress();
    }

    public static String randomPostcode() {
        return faker.address().zipCode().replaceAll("[^0-9]", "").substring(0, 5);
    }

    public static void logGeneratedData(String firstName, String lastName,
                                        String email, String loginname, String password) {
        log.info("=== DATOS RANDOM GENERADOS ===");
        log.info("First Name: " + firstName);
        log.info("Last Name : " + lastName);
        log.info("Email     : " + email);
        log.info("Login     : " + loginname);
        log.info("Password  : " + password);
        log.info("Timestamp : " + TIMESTAMP);
        log.info("==============================");
    }
}
