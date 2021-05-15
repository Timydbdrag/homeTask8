package neo

import org.apache.spark.ml.classification.MultilayerPerceptronClassificationModel
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object EasyPredict {
  val spark: SparkSession = {
    SparkSession
      .builder()
      .master("local")
      .appName("iris-pipeline")
      .getOrCreate()
  }

  def testPerceptron(data: DataFrame): Dataset[String] = {
    try {
      val model = MultilayerPerceptronClassificationModel
        .load("model")

      val assembler1 = new VectorAssembler()
        .setInputCols(Array("sepal_length", "sepal_width", "petal_length", "petal_width"))
        .setOutputCol("features")

      val v_iris_df1 = assembler1.transform(data)

      val predict = model.transform(v_iris_df1)

      def g = predict.select("sepal_length", "sepal_width", "petal_length", "petal_width", "prediction")

      import spark.implicits._

      def r = g
        .map(el =>
          el(0).toString + "," +
            el(1).toString + "," +
            el(2).toString + "," +
            el(3).toString + "," +
            getType(el(4).toString.toDouble)
        )

      r
    }
  }

  def getType(v: Double): String = {
    v match {
      case 0.0 => "Iris-setosa"
      case 1.0 => "Iris-versicolor"
      case _ => "Iris-virginica"
    }
  }
}
