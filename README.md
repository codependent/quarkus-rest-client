# quarkus-rest-client project

`ExampleResource` offers one endpoint `http://localhost:8080/echo` that calls `https://postman-echo.com/get`
using a RestClient.

Run the application with:

```
./mvnw quarkus:dev
```

## Master branch

`RestClient` as `@ApplicationScoped` is injected with CDI into `ExampleResource`. Native image construction fails:

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

## master-additional branch

Same as master branch but including the recommended fix described in the documentation: https://quarkus.io/guides/writing-native-applications-tips#delaying-class-initialization

pom.xml:

```
<quarkus.native.additional-build-args>--initialize-at-run-time=org.codependent.RestClient,-H:+TraceClassInitialization</quarkus.native.additional-build-args>
```

```
./mvnw package -Pnative -Dquarkus.native.container-build=true 
```

```
com.oracle.svm.core.util.UserError$UserException: Classes that should be initialized at run time got initialized during image building:
 org.codependent.RestClient the class was requested to be initialized at build time (from the command line). io.quarkus.runner.ApplicationImpl caused initialization of this class with the following trace: 
        at org.codependent.RestClient.<clinit>(RestClient.java)
        at java.lang.Class.forName0(Native Method)
        at java.lang.Class.forName(Class.java:348)
        at org.codependent.RestClient_Bean.<init>(RestClient_Bean.zig:135)
        at io.quarkus.arc.setup.Default_ComponentsProvider.addBeans1(Default_ComponentsProvider.zig:106)
        at io.quarkus.arc.setup.Default_ComponentsProvider.getComponents(Default_ComponentsProvider.zig:38)
        at io.quarkus.arc.impl.ArcContainerImpl.<init>(ArcContainerImpl.java:103)
        at io.quarkus.arc.Arc.initialize(Arc.java:20)
        at io.quarkus.arc.runtime.ArcRecorder.getContainer(ArcRecorder.java:35)
        at io.quarkus.deployment.steps.ArcProcessor$generateResources20.deploy_0(ArcProcessor$generateResources20.zig:72)
        at io.quarkus.deployment.steps.ArcProcessor$generateResources20.deploy(ArcProcessor$generateResources20.zig:36)
        at io.quarkus.runner.ApplicationImpl.<clinit>(ApplicationImpl.zig:338)

org.codependent.RestClient_ClientProxy the class was requested to be initialized at build time (subtype of org.codependent.RestClient). io.quarkus.runner.ApplicationImpl caused initialization of this class with the following trace: 
        at org.codependent.RestClient_ClientProxy.<clinit>(RestClient_ClientProxy.zig)
        at org.codependent.RestClient_Bean.<init>(RestClient_Bean.zig:164)
        at io.quarkus.arc.setup.Default_ComponentsProvider.addBeans1(Default_ComponentsProvider.zig:106)
        at io.quarkus.arc.setup.Default_ComponentsProvider.getComponents(Default_ComponentsProvider.zig:38)
        at io.quarkus.arc.impl.ArcContainerImpl.<init>(ArcContainerImpl.java:103)
        at io.quarkus.arc.Arc.initialize(Arc.java:20)
        at io.quarkus.arc.runtime.ArcRecorder.getContainer(ArcRecorder.java:35)
        at io.quarkus.deployment.steps.ArcProcessor$generateResources20.deploy_0(ArcProcessor$generateResources20.zig:72)
        at io.quarkus.deployment.steps.ArcProcessor$generateResources20.deploy(ArcProcessor$generateResources20.zig:36)
        at io.quarkus.runner.ApplicationImpl.<clinit>(ApplicationImpl.zig:338)


        at com.oracle.svm.core.util.UserError.abort(UserError.java:65)
        at com.oracle.svm.hosted.classinitialization.ConfigurableClassInitialization.checkDelayedInitialization(ConfigurableClassInitialization.java:510)
        at com.oracle.svm.hosted.classinitialization.ClassInitializationFeature.duringAnalysis(ClassInitializationFeature.java:187)
        at com.oracle.svm.hosted.NativeImageGenerator.lambda$runPointsToAnalysis$8(NativeImageGenerator.java:710)
        at com.oracle.svm.hosted.FeatureHandler.forEachFeature(FeatureHandler.java:63)
        at com.oracle.svm.hosted.NativeImageGenerator.runPointsToAnalysis(NativeImageGenerator.java:710)
        at com.oracle.svm.hosted.NativeImageGenerator.doRun(NativeImageGenerator.java:530)
        at com.oracle.svm.hosted.NativeImageGenerator.lambda$run$0(NativeImageGenerator.java:445)
        at java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec(ForkJoinTask.java:1386)
        at java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:289)
        at java.util.concurrent.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1056)
        at java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1692)
        at java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:157)
```


## nocdi branch

RestClient instantiated manually (no CDI). Native image construction works:

```
./mvnw package -Pnative -Dquarkus.native.container-build=true 
```

```
[INFO] [io.quarkus.deployment.QuarkusAugmentor] Quarkus augmentation completed in 72716ms
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  01:14 min
[INFO] Finished at: 2020-03-21T12:48:07+01:00
[INFO] ------------------------------------------------------------------------
```
