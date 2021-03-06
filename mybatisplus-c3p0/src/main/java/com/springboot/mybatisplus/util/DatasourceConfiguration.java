package com.springboot.mybatisplus.util;

import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author:liming
 * @Description:
 * @Date:create in 2018/7/3   15:06
 */
@SpringBootConfiguration
public class DatasourceConfiguration {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    @Value("${spring.datasource.username}")
    private String jdbcUser;
    @Value("${spring.datasource.password}")
    private String jdbcPassword;

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUser(jdbcUser);
        dataSource.setPassword(jdbcPassword);
        // 关闭连接后不自动提交
        dataSource.setAutoCommitOnClose(false);
        return dataSource;
    }

    @Value("${mapper.xml.config.path}")
    private String mapperXMLConfigPath;
    @Value("${mapper.package.path}")
    private String mapperPackagePath;

    @Value("${mybatis-plus.global-config.table-prefix}")
    private String  tablePrefix;
    @Value("${mybatis-plus.global-config.id-type}")
    private int  idType;
    @Value("${mybatis-plus.global-config.db-column-underline}")
    private boolean  dbColumnnUnderline;

    @Bean
    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean() throws IOException {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String packageXMLConfigPath = PathMatchingResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + mapperXMLConfigPath;

        GlobalConfiguration globalConfig = new GlobalConfiguration();
        globalConfig.setIdType(idType); //主键生成策略
        globalConfig.setTablePrefix(tablePrefix);  //表前缀
        globalConfig.setDbColumnUnderline(dbColumnnUnderline);  //驼峰下划线
        sqlSessionFactory.setGlobalConfig(globalConfig);
        // 设置mapper 对应的XML 文件的路径
        sqlSessionFactory.setMapperLocations(resolver.getResources(packageXMLConfigPath));
        // 设置mapper 接口所在的包
        sqlSessionFactory.setTypeAliasesPackage(mapperPackagePath);
        // 设置数据源
        sqlSessionFactory.setDataSource(dataSource());

        return sqlSessionFactory;
    }
}
