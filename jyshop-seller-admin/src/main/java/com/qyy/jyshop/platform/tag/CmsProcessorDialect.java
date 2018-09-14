package com.qyy.jyshop.platform.tag;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;

@Component
public class CmsProcessorDialect extends AbstractDialect {
    
    @Override
    public String getPrefix() {
        return "jy";
    }
    @Override
    public Set<IProcessor> getProcessors() {
        final Set<IProcessor> processors = new HashSet<>();
        processors.add(new DictionaryTag());
        processors.add(new OrderTag());
        return processors;
    }
}
