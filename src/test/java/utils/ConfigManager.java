package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Logger log = LogManager.getLogger(ConfigManager.class);
    private static Properties properties = new Properties();
    private static ConfigManager instance;

    private ConfigManager() { loadProperties(); }

    public static ConfigManager getInstance() {
        if (instance == null) instance = new ConfigManager();
        return instance;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) { log.error("No se encontro config.properties"); return; }
            properties.load(input);
            log.info("config.properties cargado");
        } catch (IOException e) {
            log.error("Error: " + e.getMessage());
        }
    }

    public String get(String key)   { return properties.getProperty(key); }
    public int getInt(String key)   { return Integer.parseInt(properties.getProperty(key)); }

    public String getUrl()              { return get("url"); }
    public String getBrowser()          { return get("browser"); }
    public int getImplicitWait()        { return getInt("implicit.wait"); }
    public int getExplicitWait()        { return getInt("explicit.wait"); }
    public int getPageLoadTimeout()     { return getInt("page.load.timeout"); }
    public String getValidUser()        { return get("valid.user"); }
    public String getValidPass()        { return get("valid.pass"); }
    public String getInvalidUser()      { return get("invalid.user"); }
    public String getInvalidPass()      { return get("invalid.pass"); }
    public String getRegFirstname()     { return get("reg.firstname"); }
    public String getRegLastname()      { return get("reg.lastname"); }
    public String getRegEmail()         { return get("reg.email"); }
    public String getRegAddress()       { return get("reg.address"); }
    public String getRegCity()          { return get("reg.city"); }
    public String getRegPostcode()      { return get("reg.postcode"); }
    public String getRegLoginname()     { return get("reg.loginname"); }
    public String getRegPassword()      { return get("reg.password"); }
    public String getCurrencyUSD()      { return get("currency.usd"); }
    public String getCurrencyEUR()      { return get("currency.eur"); }
    public String getCurrencyGBP()      { return get("currency.gbp"); }
}
