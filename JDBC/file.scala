// val file = sc.textFile("/home/tigergraph/data/reply.csv")
// val df = file.map(_.split("|")).map{case Array(a,b,c) => (a,b,c)}.toDF("creationDate","Comment1ID","Comment2ID")

val df = spark.read.format("csv").option("header", "true").option("delimiter", "|").load("/home/tigergraph/data/reply.csv")

println(df.count())
df.show()

val t1 = System.nanoTime
// invoke loading job
df.write.mode("overwrite").format("jdbc").options(
  Map(
    "driver" -> "com.tigergraph.jdbc.Driver",
    "url" -> "jdbc:tg:http://127.0.0.1:14240",
    "dbtable" -> "job load_reply", // loading job name
    "username" -> "tigergraph",
    "password" -> "usbank100",
    "debug" -> "0",
    "batchsize" -> "8192",
    "ip_list" -> "10.128.0.10,10.128.0.11,10.128.0.12,10.128.0.13,10.128.0.14,10.128.0.15",
    "filename" -> "file_Comment_replyOf_Comment", // filename defined in the loading job
    "sep" -> "|", // separator between columns
    "eol" -> "\n", // End Of Line
    "schema" -> "creationDate,Comment1ID,Comment2ID", // column definition
    "graph" -> "ldbc_snb",
    "version" -> "3.2.2")).save()
val duration = (System.nanoTime - t1) / 1e9d
print(duration)
