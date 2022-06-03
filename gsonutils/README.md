# GSONUTILS

Java 18 precludes marshalling of java.time classes with GSON unless a TypeAdapter is registered.

This project is one class, that registers an adapter for all of the java.time classes and provides
static methods to marshal objects containing fields of these types as well as some convenience 
methods for 

* pretty printing
* json to and from yaml
* objects from json

    import org.javautil.gsonutils.GsonUtils

    String json = GsonUtils.toJson(someObject);


