# Another yet cron expression parser

### Description 

This program is responsible for explaining given cron expression (cron table).

Cron table format:

```
# ┌───────────── minute (0 - 59)
# │ ┌───────────── hour (0 - 23)
# │ │ ┌───────────── day of the month (1 - 31)
# │ │ │ ┌───────────── month (1 - 12)
# │ │ │ │ ┌───────────── day of the week (0 - 6) (Sunday to Saturday)
# │ │ │ │ │                                   
# │ │ │ │ │
# │ │ │ │ │
# * * * * * command to execute
```

Allowed special characters `,` `-` `/`:

* Commas used to separate items of a list. For example, `5,45` indicates a list 5, 45.
* Minus sign defines ranges. For example, `0-59` indicates every minute between 0 and 59, inclusive.
* Slashes can be combined with ranges to specify step values. For example, `0-10/2` in the minute's field indicates 0, 2, 4, 6, 8, 10. It is shorthand for the more verbose form 0, 15, 30, 45. 

### Dependencies

* Vanilla Java, no extra libraries
* [JDK 1.8+](https://openjdk.java.net/install/)
* [Maven](https://maven.apache.org/install.html)

### Build and Run

```bash
cd cron-expression-parser
mvn package
cd target
java -jar cron-expression-parser-1.0-jar-with-dependencies.jar "*/15 0 1,15 * 1-5 /usr/bin/find"
minute        0 15 30 45
hour          0
day of month  1 15
month         1 2 3 4 5 6 7 8 9 10 11 12
day of week   1 2 3 4 5
command       /usr/bin/find
```

Cron parts separated by whitespace.