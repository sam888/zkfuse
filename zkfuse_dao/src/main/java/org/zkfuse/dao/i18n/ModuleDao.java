package org.zkfuse.dao.i18n;

import org.zkfuse.dao.IBasicDao;
import org.zkfuse.model.i18n.Module;

import javax.persistence.EntityNotFoundException;

/**
 *
 * @author Samuel Huang
 */
public interface ModuleDao extends IBasicDao<Module, Long> {
    
    Module getModuleByName( String name );
    
    void deleteModule( String name ) throws EntityNotFoundException;

    void deleteModule( Module module ) throws EntityNotFoundException;

}
