package com.icloud.my_portfolio.resources.factory;

import com.icloud.my_portfolio.resources.service.ResourcesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UrlFilterMetadataFactoryBean implements FactoryBean<LinkedHashMap<RequestMatcher, List<ConfigAttribute>>> {

    private final ResourcesService resourcesService;
    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap;

    @Override
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getObject() throws Exception {
        if (this.requestMap == null) {
            init();
        }

        return this.requestMap;
    }

    private void init() {
        this.requestMap = resourcesService.getResourcesMap();
    }

    @Override
    public Class<?> getObjectType() {
        return LinkedHashMap.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
