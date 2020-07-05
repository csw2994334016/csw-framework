package com.three.resource_jpa.jpa.base.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanManager {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory;

    public void registerBean(String beanName, Class clazz) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        this.defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
    }

    public Object getBeanByName(String name) {
        return applicationContext.getBean(name);
    }

    public void removeBean(String beanName) {
        defaultListableBeanFactory.removeBeanDefinition(beanName);
    }
}
