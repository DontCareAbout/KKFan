package us.dontcareabout.kkfan.server;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//XXX 這是還沒找到如何用 Spring 解決「用外部設定檔決定 DB 位置」而多搞出來的東西
//看起來應該只需要 dataSource()，但實測的 SSCCE 卻是這樣，缺一不可、也沒法用 annotation 設定
//因為連 workaround 都算不上，所以目前懶得深究... [逃]
//ref: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.java-config
@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
class SpringConfig {

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		String workspace = Setting.workspace();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:file:" + workspace + "/database/h2");
		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("us.dontcareabout.kkfan.shared.vo");
		factory.setDataSource(dataSource());
		return factory;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory);
		return txManager;
	}
}