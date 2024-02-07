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
sbt "gatling:testOnly com.comcast.commerce.supportAPI"
```

Number of requests and batch size can optionally be specified as parameters:
```
sbt "gatling:testOnly com.comcast.commerce.supportAPI" -DAT_ONCE_USER=1 -DUSER_COUNT=10 -DTEST_TIME=1 -DRAMP_TIME=1 -Drc=10 -DBEARER="Bearer eyJraWQiOiJzYXQtcHJvZC1rMS0xMDI0IiwiYWxnIjoiUlMyNTYifQ.eyJqdGkiOiJiZjFlN2YzNi1mMTUxLTQzMmMtYWVhOS01NThlN2Y2ODliZWUiLCJpc3MiOiJzYXRzLXByb2R1Y3Rpb24iLCJzdWIiOiJ4MTpjb21tZXJjZS1wcm9kLXRlc3Q6ZTA4ZmNhIiwiaWF0IjoxNzA3MzM1NDAwLCJuYmYiOjE3MDczMzU0MDAsImV4cCI6MTcwNzQyMTgwMywidmVyc2lvbiI6IjEuMCIsImFsbG93ZWRSZXNvdXJjZXMiOnsiYWxsb3dlZFBhcnRuZXJzIjpbImNvbWNhc3QiLCJjb21jYXN0LWRldiIsImNveCIsImNveC1kZXYiLCJ4Z2xvYmFsIl19LCJjYXBhYmlsaXRpZXMiOlsieDE6Y29hc3Q6cHJlZmRzOnJlYWQiLCJ4MTpjdWJpdHM6Ym9hdHM6d3JpdGU6YXBwcyIsIngxOmN1Yml0czpib2F0czp3cml0ZTp0ZW5hbnRzIiwieDE6eGJvOnRpdGFuOmRlbGV0ZSIsIngxOnhibzp0aXRhbjplZGl0IiwieDE6eGJvOnRpdGFuOnJlYWQiLCJ4MTp4bXA6aW50ZXJuYWw6cHVibGlzaCIsIngxOnhtcDppbnRlcm5hbDpyZWFkIiwieDE6eG1wOnJlYWQiLCJ4MTp4bXA6d3JpdGUiXSwiYXVkIjpbXX0.aLZtUUPukRcujDLoJH-1sZKgvp1jdQHKhLVngnZcJk3Gik-j9TgklObtviOm3rsWfiE-P8Ys-UCH8Je2nD1gS9r3DiHnpUTNOlkP1DAyz93jhWHsxOwLsRqCU0Tilz6t7X2_YE-kSCfX9AWWxWxpdO8ymTIJh5l0d_zXoOx8t0o"```

Report:
```
sbt gatling:lastReport
```
