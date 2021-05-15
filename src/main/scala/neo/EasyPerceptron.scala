package neo

import org.apache.spark.ml.classification.MultilayerPerceptronClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator


object EasyPerceptron extends IrisWrapper {

  def main(args: Array[String]): Unit = {

    val dataSet = irisDataset()
    val splits = dataSet.randomSplit(Array(0.6, 0.4), seed = 1234L)
    val train = splits(0)
    val test = splits(1)

    // specify layers for the neural network:
    val layers = Array[Int](4, 5, 4, 3)

    // create the trainer and set its parameters
    val trainer = new MultilayerPerceptronClassifier()
      .setLayers(layers)
      .setBlockSize(128)
      .setSeed(1234L)
      .setMaxIter(100)

    // train the model
    val model = trainer.fit(train)

    //save model
    model.save("model")

    val pred_df = model.transform(test)
    pred_df.show(20)

    val predictionAndLabels = pred_df.select("prediction", "label")

    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("label")
      .setPredictionCol("prediction")
      .setMetricName("accuracy")

    println(s"Test set accuracy = ${evaluator.evaluate(predictionAndLabels)}")

  }

  def getType(v:Double): String ={
    v match {
      case 0.0 => "Iris-setosa"
      case 1.0 => "Iris-versicolor"
      case _ => "Iris-virginica"
    }
  }



}
