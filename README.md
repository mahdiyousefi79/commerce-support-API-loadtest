## commerce-load-test - performance test suite

Load test scenario for testing Commerce get entitlements. 

### How to Run

You must log into the appropriate AWS environment using aadawscli.
The scenario will load attributes from application.conf, and retrieve secrets from AWS Secrets Manager,
using the attribute ```${env}.aws.secrets```.

Note: these tests use HTTPS TLSv1.3, which requires Java 11 or above.

All tests:
```
sbt gatling:test
```

Single test:
```
sbt "gatling:testOnly com.comcast.commerce.GetEntitlements"
```

Number of requests and batch size can optionally be specified as parameters:
```
sbt "gatling:testOnly com.comcast.commerce.GetEntitlements" -DUSER_COUNT=1 -DTEST_TIME=1 -DRAMP_TIME=1 -DBEARER="Bearer <token>"
```

Report:
```
sbt gatling:lastReport
```
