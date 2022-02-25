# Coroutine Advice

When a Kotlin-based controller uses the `suspend` keyword, the return type does not seem amenable to post-processing in a method interceptor since the method has not yet been invoked.

In Spring Security, we try and overcome this by using `CoroutineUtils.invokeSuspendFunction`, but this only makes things partially work since it bypasses `MethodInvocation#proceed`, thus skipping any remaining method interceptors.

In the project, you can see `OneMethodInterceptor` and `TwoMethodInterceptor` which are simplified versions of method interceptors that Spring Security has. The question is how to post-process the return value from a method invocation when it's a suspending function.

If you do

```bash
http :8080/helloSuspend
```

You'll see that the call is correctly intercepted, but there is no way to intercept the return value and analyze it.

This is shown in

```bash
http :8080/helloSuspendDenied
```

In that the call is erroneously allowed.