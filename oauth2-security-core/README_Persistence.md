# 📘 Persistence Module – Auto-Configuration Guide

This persistence module offers flexible and modular auto-configuration for DataSource connections, supporting both local databases and AWS Aurora with IAM authentication. It leverages Spring Boot's conditional annotations to enable or disable configurations based on application properties, ensuring seamless integration across diverse environments.

## 🚀 Features

- **Auto-Configuration**: Automatically configures DataSource beans based on specified properties.
- **Conditional Activation**: Utilizes `@ConditionalOnProperty` to activate configurations only when `core.database.enabled=true`.
- **Support for Multiple Environments**:
  - **Local Development**: Configures `LocalDataSourceFactory` for local databases.
  - **AWS Aurora**: Configures `AuroraConnectionFactory` using IAM authentication.
- **Bean Overriding**: Allows downstream applications to override default beans using `@ConditionalOnMissingBean`.

## ⚙️ Configuration

### 1. Enabling the Persistence Module

To activate the persistence module, set the following property in your `application.properties` or `application.yml`:

```properties
core.database.enabled=true
```

If this property is set to `false` or omitted, the module's auto-configuration will be disabled.

### 2. Setting Up DataSource Connections

#### a. Local Database Configuration

For local development environments, configure the following properties:

```properties
# Enable the persistence module
core.database.enabled=true

# Specify the database type
core.database.type=local

# JDBC URL for the local database
core.database.url=jdbc:postgresql://localhost:5432/mydb

# Database credentials
core.database.username=localuser
core.database.password=localpassword

# JDBC driver class name
core.database.driver-class-name=org.postgresql.Driver
```

#### b. AWS Aurora with IAM Authentication

For AWS Aurora databases using IAM authentication, configure the following properties:

```properties
# Enable the persistence module
core.database.enabled=true

# Specify the database type
core.database.type=aurora

# AWS Region where the Aurora cluster is hosted
core.database.region=us-east-1

# Aurora cluster endpoint
core.database.endpoint=your-cluster.cluster-abcdefg.us-east-1.rds.amazonaws.com

# Database port
core.database.port=5432

# Database name
core.database.database-name=mydb

# IAM database user
core.database.username=iam_db_user

# JDBC driver class name
core.database.driver-class-name=software.amazon.jdbc.Driver

# HikariCP specific configurations
spring.datasource.hikari.data-source-properties.wrapperPlugins=iam
spring.datasource.hikari.data-source-properties.iamRegion=us-east-1
```

**Note**: Ensure that IAM authentication is enabled for your Aurora database and that the IAM user has the necessary permissions.

### 3. Excluding Spring Boot's Default DataSource Auto-Configuration

To prevent conflicts with Spring Boot's default DataSource auto-configuration, exclude the following classes in your main application class:

```java
@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

Alternatively, you can exclude them via properties:

```properties
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
```

### 4. Overriding Default Beans

The module defines beans with `@ConditionalOnMissingBean`, allowing you to provide custom implementations.

**Example**:

```java
@Configuration
public class CustomDataSourceConfig {

    @Bean
    public DataSource dataSource() {
        // Define and return your custom DataSource
    }
}
```

By defining a bean of the same type, Spring Boot will prioritize your custom bean over the default provided by the module.

## 🧪 Testing the Configuration

- **Verify Bean Initialization**: Ensure that the appropriate `DataSource` bean is initialized based on your configuration.
- **Check Connection**: Test the database connection to confirm successful integration.
- **Monitor Logs**: Review application logs for any errors or warnings related to the DataSource configuration.

## 📚 Additional Resources

- [AWS RDS IAM Authentication Documentation](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/UsingWithRDS.IAMDBAuth.html)
- [Spring Boot DataSource Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#application-properties.data)

---

For further assistance or inquiries, please contact the maintainers of this module.
