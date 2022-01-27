module com.example.withmodules {
	requires org.opensaml.core;

	requires spring.boot;
	requires spring.boot.autoconfigure;

	requires spring.context;
	requires spring.security.core;
	requires spring.security.saml2.service.provider;
	requires spring.web;

	exports com.example.withmodules;
}