package neo

import neo.EasyPredict.testPerceptron
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.streaming.Trigger.ProcessingTime

object HandlerKafka {

  val spark: SparkSession = {
    SparkSession
      .builder()
      .master("local")
      .appName("iris-pipeline")
      .getOrCreate()
  }

  def main(args: Array[String]): Unit = {
    import spark.implicits._

    val df = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:29092")
      .option("subscribe", "input")
      .load()


    val res2 = df.selectExpr( "CAST(value AS STRING)").as[String]

    def arr2 = res2.map(
      _.replace("\"", "")
      .split(","))
      .map(e => (e(0).trim.toDouble,e(1).trim.toDouble,e(2).trim.toDouble,e(3).trim.toDouble))
      .toDF("sepal_length", "sepal_width", "petal_length", "petal_width")


    def response = testPerceptron(arr2)

/*    response.writeStream
      .format("console")
      .trigger(ProcessingTime(2000))
      .start()
      .awaitTermination()*/

    val ds = response
      .selectExpr("CAST(value AS STRING)")
      .writeStream
      .format("kafka")
      .trigger(ProcessingTime(1000))
      .option("kafka.bootstrap.servers", "localhost:29092")
      .option("topic", "output")
      .option("checkpointLocation", "location_2")
      .start()

    ds.awaitTermination()

  }

  case class Test(a:String, b:String)
}

