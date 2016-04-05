package org.i18n.web.vm;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.zkfuse.model.i18n.ResourceBundle;

/**
 * To be cleaned up latter
 */
public class ResourceBundleValidatorTest {
    
    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @Ignore
    public void testLocaleIsNull() {

        ResourceBundle rb = new ResourceBundle();
        rb.setResourceBundleName("test");
        
        Set<ConstraintViolation<ResourceBundle>> constraintViolations =
            validator.validate(rb);

        System.out.println("constraintViolations size: " + constraintViolations.size());
        assertFalse( constraintViolations.isEmpty());
        
        // for ( violationSet.iterator()
        for ( Iterator<ConstraintViolation<ResourceBundle>> iter = constraintViolations.iterator(); iter.hasNext(); ) {
            System.out.println( "error msg: " + iter.next().getMessage() );
        }

    }
    
    @Test
    @Ignore
    public void testLocaleIsNull2() {
        
        // Property property = ctx.getProperty();
        // String propertyName = property.getProperty();
        // Object propertyValue = property.getValue();
        // Set<ConstraintViolation<E>> violationSet =
        // validator.validateValue(clazz, propertyName, propertyValue);

        ResourceBundle rb = new ResourceBundle();
        rb.setResourceBundleName("test");
        
        String propertyName = "locale";
//        Object propertyValue = null;
        String propertyValue = "";
        // validator.validateValue(clazz, propertyName, propertyValue);
        
//        Set<ConstraintViolation<ResourceBundle>> constraintViolations = validator.validate(rb);
        Set<ConstraintViolation<ResourceBundle>> constraintViolations = validator.validateValue(ResourceBundle.class, propertyName, propertyValue);
        
        System.out.println("constraintViolations size: " + constraintViolations.size());
        assertFalse( constraintViolations.isEmpty());
        
        // for ( violationSet.iterator()
        for ( Iterator<ConstraintViolation<ResourceBundle>> iter = constraintViolations.iterator(); iter.hasNext(); ) {
            System.out.println( "error msg: " + iter.next().getMessage() );
        }
        
    }

}
