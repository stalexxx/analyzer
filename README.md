# Transaction analyzer

### Quick run

`mvn exec:java -Dexec.mainClass="com.aostrovskiy.MainKt" -Dexec.args='--from="20/08/2018 12:00:00" --to="20/08/2018 13:00:00" --csv=test1.csv --merchant="Kwik-E-Mart"'`

### Command line params

* --csv - absolute or relative path to file
* --to - the beginning of the range (format: dd/MM/yyyy HH:mm:ss) 
* --from - the ending of the range (format: dd/MM/yyyy HH:mm:ss)
* --merchant - merchant to analyze for