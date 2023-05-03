# Stagger - API
## Application properties
File: application.properties
```
spring.config.import=application_development.properties, application_production.properties
spring.profiles.active=@spring.profiles.active@
spring.application.name="Stagger API"
```
File: application_development.properties
```
spring.config.activate.on-profile=development

# Database setting
spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:mem:stagger;NON_KEYWORDS=USER;DATABASE_TO_LOWER=TRUE;MODE=LEGACY
spring.datasource.username=sa
spring.datasource.password=
spring.data.jpa.repositories.bootstrap-mode=default
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.hibernate.ddl-auto=none

# Authentication setting
jwt.secret="#JWT_SECRET#"

# CORS setting
application.client.url=#CLIENT_URL#
```
File: application_production.properties
```
spring.config.activate.on-profile=production

# Database setting
spring.datasource.url=#DATASOURCE_URL#
spring.datasource.username=#DATASOURCE_USER#
spring.datasource.password=#DATASOURCE_PW#
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

# Authentication setting
jwt.secret="#JWT_SECRET#"

# CORS setting
application.client.url=#CLIENT_URL#
```
