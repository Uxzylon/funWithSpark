package edu.fun.spark

import org.apache.spark.sql.SparkSession

object Main {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.appName("Simple Application").getOrCreate()
    val file = spark.read.textFile(args(0)).cache()
    val numAs = file.filter(line => line.contains("a")).count()
    val numBs = file.filter(line => line.contains("b")).count()
    println(s"Lines with a: $numAs, Lines with b: $numBs")
    spark.stop()
  }
}
