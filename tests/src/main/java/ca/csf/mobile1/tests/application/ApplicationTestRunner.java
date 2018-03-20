package ca.csf.mobile1.tests.application;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

import static org.mockito.Mockito.spy;

/**
 * « AndroidJUnitRunner » permettant de remplacer l'objet {@link Application} par un mock.
 */
public class ApplicationTestRunner extends AndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader classLoader, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return spy(super.newApplication(classLoader, className, context));
    }

}
