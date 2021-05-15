package neo


import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.{DataFrame, SparkSession}


trait IrisWrapper {

  lazy val session: SparkSession = {
    SparkSession
      .builder()
      .master("local")
      .appName("iris-pipeline")
      .getOrCreate()
  }

  def irisDataset(): DataFrame ={
    val iris_df = session
      .read
      .option("header",value = true)
      .option("inferSchema", value = true)
      .csv("resources/IRIS.csv")

    iris_df.show(5)

    val assembler = new VectorAssembler()
      .setInputCols(Array("sepal_length", "sepal_width", "petal_length", "petal_width"))
      .setOutputCol("features")

    val v_iris_df = assembler.transform(iris_df)
    v_iris_df.show(5)

    val indexer = new StringIndexer().setInputCol("species").setOutputCol("label")
    val i_v_iris_df = indexer.fit(v_iris_df).transform(v_iris_df)
    println("indexer")
    i_v_iris_df.show(5)

    i_v_iris_df
  }

  val dataSetPath = "resources/"

  val irisFeatures_CategoryOrSpecies_IndexedLabel: (String, String, String) =
    ("iris-features-column", "iris-species-column", "label")

  def buildDataFrame(dataSet: String): DataFrame = {
    def getRows: Array[(org.apache.spark.ml.linalg.Vector, String)] = {
      session.sparkContext.textFile(dataSet).flatMap {
        partitionLine => partitionLine.split("\n")
          .toList
      }.map(_.split(","))
        .collect.drop(1)
        .map(row => (Vectors.dense(row(0).toDouble, row(1).toDouble, row(2).toDouble, row(3).toDouble), row(4)))

    }

    val dataFrame = session
      .createDataFrame(getRows)
      .toDF(irisFeatures_CategoryOrSpecies_IndexedLabel._1, irisFeatures_CategoryOrSpecies_IndexedLabel._2)
    dataFrame
  }
}
