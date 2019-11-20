Run analyzer tool quick run:

mvn exec:java -Dexec.mainClass="com.aostrovskiy.MainKt" -Dexec.args='--from="20/08/2018 12:00:00" --to="20/08/2018 13:00:00" --csv=test1.csv --merchant="Kwik-E-Mart"'

Command line params:

--csv - absolute or relative path to file
--to and --from - time line (format: dd/MM/yyyy HH:mm:ss)
--merchant - merchant to analyze for