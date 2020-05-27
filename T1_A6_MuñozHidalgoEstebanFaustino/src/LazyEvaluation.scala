import java.io._
import scala.concurrent.duration._
import scala.util.Random

object LazyEvaluation {
  def main(args: Array[String]): Unit = {
    //El archivo ya se encuentra lleno.
    //llenar
    val sour = scala.io.Source.fromFile("src/Archivo.txt").getLines()
    lectura(sour)
    val sour2 = scala.io.Source.fromFile("src/Archivo.txt").getLines()
    lecturaCBV(sour2)
  }

  // Se cargan los datos para llenarse
  def llenar() {
    val tam: Int = 100000
    val data = new Array[String](tam)
    for (x <- 0 to tam - 1) {
      for (i <- 0 to 200) {
        if (i == 0) {
          data(x) = "" + Random.nextInt(2)
        } else {
          data(x) = data(x) + Random.nextInt(2)
        }
      }
    }
    printToFile(new File("src/Archivo.txt")) { p =>
      data.foreach(p.println)
    }
    println("Termino llenado")
  }

  //Cargar los datos
  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try { op(p) } finally { p.close() }
  }

  //Leer los datos con CBN
  def lectura(x: => Iterator[String]): Unit = {
    val t_ini = System.nanoTime()
    var cadena: String = ""
    var se: Int = 0
    while (x.hasNext) {
      cadena = x.next().toString()
      se = se + analisis(cadena)
    }
    val t_fin = System.nanoTime()
    val duration = Duration(t_fin - t_ini, NANOSECONDS)
    println("Tiempo de analisis con CBN: " + duration.toSeconds + " segundos")
    println("Secuencias de unos encontrados: " + se)
  }

  //Analisis CBN
  def analisis(cadena: => String): Int = {
    var aux: Int = 0
    var aux2: Int = 0
    for (x <- 0 to (cadena.length() - 1)) {
      if (cadena.substring(x, x + 1) == "1") {
        aux = aux + 1
        if (aux == 5) {
          aux2 = aux2 + 1
        }
      } else {
        aux = 0
      }
    }
    aux2
  }

  //Analisis CBV
  def analisisCBV(cadena: String): Int = {
    var aux: Int = 0
    var aux2: Int = 0
    for (x <- 0 to (cadena.length() - 1)) {
      if (cadena.substring(x, x + 1) == "1") {
        aux = aux + 1
        if (aux == 5) {
          aux2 = aux2 + 1
        }
      } else {
        aux = 0
      }
    }
    aux2
  }
  def lecturaCBV(x: Iterator[String]): Unit = {
    val t_ini = System.nanoTime()
    var cadena: String = ""
    var se: Int = 0
    while (x.hasNext) {
      cadena = x.next().toString()
      se = se + analisisCBV(cadena)
    }
    val t_fin = System.nanoTime()
    val duration = Duration(t_fin - t_ini, NANOSECONDS)
    println("Tiempo de analisis con CBV: " + duration.toSeconds + " segundos")
    println("Secuencias de unos encontrados: " + se)
  }

}
