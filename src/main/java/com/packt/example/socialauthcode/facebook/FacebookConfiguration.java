package com.packt.example.socialauthcode.facebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.facebook.api.Facebook;

@Configuration
@EnableSocial
@ComponentScan
//@EnableConfigurationProperties(EnhancedFacebookProperties.class)
public class FacebookConfiguration extends SocialAutoConfigurerAdapter {

	@Autowired
	private EnhancedFacebookProperties properties;

	@Bean
	@ConditionalOnMissingBean(Facebook.class)
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public Facebook facebook(ConnectionRepository repository) {
		Connection<Facebook> connection = repository
				.findPrimaryConnection(Facebook.class);
		return connection != null ? connection.getApi() : null;
	}

	@Override
	protected ConnectionFactory<?> createConnectionFactory() {
		return new CustomFacebookConnectionFactory(this.properties.getAppId(),
				this.properties.getAppSecret(), this.properties.getApiVersion());
	}

	@Bean
	public ConnectController connectController(
			ConnectionFactoryLocator factoryLocator,
			ConnectionRepository repository) {

		ConnectController controller = new ConnectController(
			factoryLocator, repository);
		controller.setApplicationUrl("http://localhost:8080");
		return controller;
	}
}
