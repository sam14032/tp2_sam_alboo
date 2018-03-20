package ca.csf.mobile1.tests.application;

import android.app.Application;
import android.support.test.InstrumentationRegistry;


/**
 * Lot de « ViewMatcher » pour des tests Espresso visant les composants de niveau « Application ».
 */
public class ApplicationActions {

    /**
     * Retourne l'objet {@link Application} de l'application en cours.
     *
     * @param ignored Classe de l'objet {@link Application} espéré.
     * @param <T>     Classe de l'objet {@link Application}.
     * @return Objet {@link Application} de l'application en cours.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Application> T theApplication(Class<T> ignored) {
        return (T) InstrumentationRegistry.getTargetContext().getApplicationContext();
    }

}
