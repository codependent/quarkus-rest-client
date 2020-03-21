# quarkus-rest-client project

`ExampleResource ` Offers one endpoint `http://localhost:8080/echo` that calls `https://postman-echo.com/get`
using a RestClient.

Run the application with:

```
./mvnw quarkus:dev
```

## Master branch

RestClient as @ApplicationScoped injected with CDI. Native image construction fails:

```
./mvnw package -Pnative -Dquarkus.native.container-build=true 
```

```
Error: No instances of sun.security.provider.NativePRNG are allowed in the image heap as this class should be initialized at image runtime. To see how this object got instantiated use -H:+TraceClassInitialization.
Detailed message:
Trace:  object java.security.SecureRandom
        object sun.security.ssl.SSLContextImpl$TLSContext
        object sun.security.ssl.SSLSocketFactoryImpl
        object org.apache.http.conn.ssl.SSLConnectionSocketFactory
        object java.util.concurrent.ConcurrentHashMap$Node
        object java.util.concurrent.ConcurrentHashMap$Node[]
        object java.util.concurrent.ConcurrentHashMap
        object org.apache.http.config.Registry
        object org.apache.http.impl.conn.DefaultHttpClientConnectionOperator
        object org.apache.http.impl.conn.PoolingHttpClientConnectionManager
        object org.apache.http.impl.client.HttpClientBuilder$2
        object java.lang.Object[]
        object java.util.ArrayList
        object org.apache.http.impl.client.InternalHttpClient
        object org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient43Engine
        object org.jboss.resteasy.client.jaxrs.internal.ResteasyClientImpl
        object org.codependent.RestClient_ClientProxy
        object org.codependent.RestClient_Bean
        object java.lang.Object[]
        object java.util.ArrayList
        object io.quarkus.arc.impl.ArcContainerImpl
        object io.quarkus.arc.runtime.ArcRecorder$2
        field io.quarkus.resteasy.common.runtime.QuarkusInjectorFactory.CONTAINER

com.oracle.svm.core.util.UserError$UserException: No instances of sun.security.provider.NativePRNG are allowed in the image heap as this class should be initialized at image runtime. To see how this object got instantiated use -H:+TraceClassInitialization.
Detailed message:
Trace:  object java.security.SecureRandom
        object sun.security.ssl.SSLContextImpl$TLSContext
        object sun.security.ssl.SSLSocketFactoryImpl
        object org.apache.http.conn.ssl.SSLConnectionSocketFactory
        object java.util.concurrent.ConcurrentHashMap$Node
        object java.util.concurrent.ConcurrentHashMap$Node[]
        object java.util.concurrent.ConcurrentHashMap
        object org.apache.http.config.Registry
        object org.apache.http.impl.conn.DefaultHttpClientConnectionOperator
        object org.apache.http.impl.conn.PoolingHttpClientConnectionManager
        object org.apache.http.impl.client.HttpClientBuilder$2
        object java.lang.Object[]
        object java.util.ArrayList
        object org.apache.http.impl.client.InternalHttpClient
        object org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient43Engine
        object org.jboss.resteasy.client.jaxrs.internal.ResteasyClientImpl
        object org.codependent.RestClient_ClientProxy
        object org.codependent.RestClient_Bean
        object java.lang.Object[]
        object java.util.ArrayList
        object io.quarkus.arc.impl.ArcContainerImpl
        object io.quarkus.arc.runtime.ArcRecorder$2
        field io.quarkus.resteasy.common.runtime.QuarkusInjectorFactory.CONTAINER

```

## nocdi branch

RestClient instantiated manually (no CDI). Native image construction works:

```
[INFO] [io.quarkus.deployment.QuarkusAugmentor] Quarkus augmentation completed in 72716ms
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  01:14 min
[INFO] Finished at: 2020-03-21T12:48:07+01:00
[INFO] ------------------------------------------------------------------------
```
