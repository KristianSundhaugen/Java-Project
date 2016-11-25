package no.ntnu.imt3281.I18N;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class will handle the i18n
 * @author oivindk, copied from your i18n, small changes for our program
 *
 */

public class I18N {
	private static class SynchronizedHolder {
		/**
		 * The unused variable i18n is used to store the instance to prevent it from being deleted when there is no reference to it
		 */
		@SuppressWarnings("unused")
		static I18N i18n = new I18N();
		static ResourceBundle bundle;
	}

    /**
     * Constructor sets English to default language
     */
    private I18N() {

		Locale newLocale = new Locale("en", "US");
		Locale.setDefault(newLocale);

		SynchronizedHolder.bundle = ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n",
				Locale.getDefault());
	}

	/**
	 * Change language
	 * @param language, sets the language
	 */
	public static void setLanguage(String language) {
		Locale locale = new Locale(language);
 
		SynchronizedHolder.bundle = ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n", locale);
 
	}

	/**
	 * Change language and country
	 * @param language, set the language
	 * @param country, set the country
	 */
	public static void setLanguageCountry(String language, String country) {
		Locale locale = new Locale(language, country);
		SynchronizedHolder.bundle = ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n",
				locale);
	}

	/**
	 * Get resource bundle
	 * @return ResourceBundle for application
	 */
	public static ResourceBundle getBundle() {
		return SynchronizedHolder.bundle;
	}	
}
