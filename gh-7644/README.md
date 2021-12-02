# Resource Chaining with Spring Security

This sample demonstrates that Spring Security does not break resource chaining when `enableSessionUrlRewriting` is `false`.

To use, start up the application with:

```bash
./mvnw spring-boot:run
```

And then navigate to `http://localhost:8080`.

In the resulting HTML, the link to `test.css` contains the appropriate hash that is added by `ResourceUrlEncodingFilter`.