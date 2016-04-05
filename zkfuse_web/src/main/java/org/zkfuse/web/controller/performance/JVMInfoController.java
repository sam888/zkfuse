package org.zkfuse.web.controller.performance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;

/**
 * Created with IntelliJ IDEA.
 *
 * Date: 27/07/13
 */
@Slf4j
@Controller
@Scope("prototype")
public class JVMInfoController extends SelectorComposer<Component> {

    @Wire
    private Label usedMemory;

    @Wire
    private Label freeMemory;

    @Wire
    private Label totalMemory;

    @Wire
    private Label maxMemory;

    private static final String MEMORY_UNIT = " MB";

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        populateJVMInformation();
    }

    public void populateJVMInformation() {
        int mb = 1024 * 1024;

        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();

        Long usedMemoryVal = (runtime.totalMemory() - runtime.freeMemory()) / mb;
        Long freeMemoryVal = runtime.freeMemory() / mb;
        Long totalMemoryVal = runtime.totalMemory() / mb;
        Long maxMemoryVal = runtime.maxMemory() / mb;

        usedMemory.setValue(usedMemoryVal != null ? usedMemoryVal.toString() + MEMORY_UNIT : "Error");
        freeMemory.setValue(freeMemoryVal != null ? freeMemoryVal.toString() + MEMORY_UNIT : "Error");
        totalMemory.setValue(totalMemoryVal != null ? totalMemoryVal.toString() + MEMORY_UNIT : "Error");
        maxMemory.setValue(maxMemoryVal != null ? maxMemoryVal.toString() + MEMORY_UNIT : "Error");

    }
}
