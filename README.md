# json-schema-validator-samples

# Background

JSON is usually the goto schema-less data format these days. You represent 
the data in the form of Domain Model objects and use a SerDes like Jackson to
convert the data formats over the wire. But as the applications and data 
grow, it's inevitable to NOT care about the structure of the data. There comes
into picture the JSON Schema. We can define our constraints and rules in the 
Schema and these can be enforced/validated agains the JSON data using the 
json-schema-validator library or similar.

# Purpose

* To learn how JSON Schema works and craft a few schemas.
* Learn to use the json-schema-validator library to validate arbitrary JSON 
data against the schema defined above.

# Build/Run

It's a standard Maven Java project, so run the following commands.

* mvn -e clean package
* java -jar target/json-schema-validator-samples-1.0-SNAPSHOT.jar
