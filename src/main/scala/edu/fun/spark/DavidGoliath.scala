package edu.fun.spark

import edu.fun.spark.Player._
import org.apache.spark.sql.{Encoder, Encoders, SparkSession}

object DavidGoliath {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.appName("David or Goliath").getOrCreate()
    val file = spark.read.textFile(args(0)).cache()

    val data = file.filter(!_.startsWith("id"))  // remove header

    implicit val gameEncoder: Encoder[Game] = Encoders.bean[Game](classOf[Game])

    val result = data
      .map(s => Game.fromColumns(s.split(",")))
      .rdd.groupBy(davidOrGoliath)
      .mapValues(_.size)
      .reduceByKey(_ + _)
      .collect()

    result.foreach(v => println(s"${v._1} won ${v._2}"))

    spark.stop()
  }

  private def davidOrGoliath(game: Game): String = {
    if ((game.black_rating > game.white_rating && BLACK.getValue.equals(game.winner)) || (game.black_rating < game.white_rating && WHITE.getValue.equals(game.winner))) {
      "Goliath"
    } else {
      "David"
    }
  }
}
