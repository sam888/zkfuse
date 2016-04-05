package org.zkfuse.service.i18n;

import org.zkfuse.model.i18n.Module;
import org.zkfuse.service.IGenericService;

/**
 * @author Samuel Huang
 */
public interface ModuleService extends IGenericService<Module, Long> {

    Module getModuleByName(String name);

    void deleteModule(String name);

    void deleteModule(Module module);
}
