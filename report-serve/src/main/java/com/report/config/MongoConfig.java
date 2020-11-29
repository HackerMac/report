package com.report.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * Created by mingku on 2020/11/20.
 */

@Configuration
public class MongoConfig {

    //@Bean
    //public MongoTemplate getMongoTemplate(MongoDatabaseFactory dbFactory,MappingMongoConverter converter) {
    //    MongoTemplate template = new MongoTemplate(dbFactory, converter);
    //    return template;
    //}

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory factory,
                                                       MongoMappingContext context,
                                                       BeanFactory beanFactory) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        mappingConverter.setCustomConversions(beanFactory.getBean(MongoCustomConversions.class));
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));//去掉默认mapper添加的_class
        //mappingConverter.setCustomConversions(customConversions());//添加自定义的转换器
        return mappingConverter;
    }

    //@Bean
    //public MongoCustomConversions customConversions() {
    //    List list = new ArrayList();
    //    list.add(new TimestampConverter());
    //    return new MongoCustomConversions(list);
    //}

}
