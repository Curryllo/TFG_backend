@file:Suppress("SpellCheckingInspection")
package org.unizar.tfg_backend.infraestructure.repositories

import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import org.jfree.chart.ChartFactory
import org.jfree.chart.JFreeChart
import org.jfree.chart.axis.CategoryAxis
import org.jfree.chart.axis.CategoryLabelPositions
import org.jfree.chart.plot.CategoryPlot
import org.jfree.chart.plot.PiePlot
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.renderer.category.BarRenderer
import org.jfree.chart.renderer.category.LineAndShapeRenderer
import org.jfree.chart.title.TextTitle
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.data.general.DefaultPieDataset
import org.springframework.stereotype.Service
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import org.jfree.chart.renderer.category.StackedBarRenderer
import org.jfree.chart.renderer.category.StandardBarPainter
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class ServicioPdf(
    private val servicioMinIO: ServicioS3Impl
) {
    fun generarInforme(): ByteArray {
        val datosHumanos = servicioMinIO.leerCSV("tfg-curro-s3", "datosLimpiosHumanos.csv")
        val datosMonitoreo = servicioMinIO.leerCSV("tfg-curro-s3", "datosLimpios.csv")
        val datosGarrapatas = servicioMinIO.leerCSV("tfg-curro-s3", "datosLimpiosGarrapatas.csv")


        val outputStream = ByteArrayOutputStream()
        val document = Document(PageSize.A4, 36f, 36f, 54f, 36f)


        val titleFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 20f)
        val subtitleFont = FontFactory.getFont(FontFactory.TIMES, 18f)
        val apartado = FontFactory.getFont(FontFactory.TIMES, 14f)

        PdfWriter.getInstance(document, outputStream)
        document.open()

        document.add(Paragraph("Informe Mensual - ${java.time.LocalDate.now()} ", titleFont))
        document.add(Paragraph("Generado por el Sistema Integral de Gestión, Explotación y Alerta sobre Enfermedades Vectoriales", subtitleFont))

        document.add(Chunk.NEWLINE)
        document.add(Paragraph("A continuación se muestran las estadísticas referentes a Casos Humanos:", apartado))
        document.add(Chunk.NEWLINE)

        document.add(graficoCasosPorPais(datosHumanos))
        document.add(Chunk.NEWLINE)

        document.add(graficoCasosOrigen(datosHumanos))
        document.add(Chunk.NEWLINE)

        document.add(graficoBarrasEdadCasos(datosHumanos))
        document.add(Chunk.NEWLINE)

        document.add(graficoBarrasEdadTotal(datosHumanos))
        document.add(Chunk.NEWLINE)

        document.add(graficoRelacionAnoSexoCasos(datosHumanos))
        document.add(Chunk.NEWLINE)

        document.add(Paragraph("A continuación se muestran las estadísticas referentes a  Monitoreo Entomológico:", apartado))
        document.add(Chunk.NEWLINE)

        document.add(graficosVectoresBarras(datosMonitoreo))
        document.add(Chunk.NEWLINE)

        document.add(graficoAnilloVectores(datosMonitoreo))
        document.add(Chunk.NEWLINE)
        document.add(Chunk.NEWLINE)

        document.add(Paragraph("A continuación se muestran las estadísticas referentes a Garrapatas:", apartado))
        document.add(Chunk.NEWLINE)

        document.add(graficoBarrasGarrapatas(datosGarrapatas, true))
        document.add(Chunk.NEWLINE)

        document.add(graficoBarrasFechaGarrapatas(datosGarrapatas))
        document.add(Chunk.NEWLINE)

        document.add(graficoAnilloGarrapatas(datosGarrapatas))
        document.add(Chunk.NEWLINE)

        document.close()
        return outputStream.toByteArray()
    }

    private fun estiloFondo(plot: CategoryPlot){
        plot.backgroundPaint = Color.WHITE
        plot.isOutlineVisible = false
        plot.isDomainGridlinesVisible = false
        plot.isRangeGridlinesVisible = true
        plot.rangeGridlinePaint = Color(229, 231, 235)

    }

    private fun graficoCasosPorPais(datos: List<Map<String, String>>): Image {
        val dataset = DefaultCategoryDataset()

        datos.groupBy { caso ->
            val pais = caso["pais"]?.trim()
            if (pais.isNullOrEmpty()) "Desconocido" else pais
        }
            .mapValues { it.value.size }
            .forEach { (pais, count) ->
                dataset.addValue(count.toDouble(), "Casos", pais)
            }

        val chart = ChartFactory.createBarChart(
            "Casos registrados por país",
            "País",
            "Número de casos",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        )

        chart.title.font = Font("SansSerif", Font.BOLD, 12)

        val plot = chart.categoryPlot
        estiloFondo(plot)

        val renderer = plot.renderer as BarRenderer
        renderer.barPainter = StandardBarPainter()
        renderer.setShadowVisible(false)
        renderer.isDrawBarOutline = false
        renderer.setSeriesPaint(0, Color(59, 130, 246))

        estilizarBarras(chart)
        return chartToImage(chart)
    }

    private fun graficoCasosOrigen(datos: List<Map<String, String>>): Image {
        val dataset = DefaultPieDataset<String>()

        datos.groupBy { caso ->
            val pais = caso["pais"]?.trim() ?: ""

            if (pais.equals("España", ignoreCase = true)) {
                "Autóctono"
            } else {
                "Importado"
            }
        }
            .mapValues { it.value.size }
            .forEach { (origen, count) ->
                dataset.setValue(origen, count)
            }

        val chart = ChartFactory.createPieChart(
            "Origen de los casos registrados",
            dataset,
            true,
            true,
            false
        )

        chart.title.font = Font("SansSerif", Font.BOLD, 12)

        chart.addSubtitle(TextTitle("Autóctonos (España) vs Importados", Font("SansSerif", Font.PLAIN, 12)))

        val plot = chart.plot as PiePlot<*>
        plot.labelGenerator = null
        plot.backgroundPaint = Color.WHITE
        plot.isOutlineVisible = false
        plot.shadowPaint = null
        plot.sectionOutlinesVisible = false
        plot.setSectionPaint("Autóctono", Color(239, 68, 68))
        plot.setSectionPaint("Importado", Color(59, 130, 246))


        return chartToImage(chart)

    }

    private fun graficoBarrasEdadCasos(datos: List<Map<String, String>>): Image {
        val rangos = listOf("0-14", "15-24", "25-34", "35-44", "45-54", "55-64", "65+")

        val conteoPorRango = mutableMapOf<String, MutableMap<String, Int>>()
        rangos.forEach { conteoPorRango[it] = mutableMapOf() }

        datos.forEach { caso ->
            val edadStr = caso["edad"]?.trim()
            val edad = edadStr?.toIntOrNull() ?: 0

            val claveRango = when {
                edad <= 14 -> "0-14"
                edad <= 24 -> "15-24"
                edad <= 34 -> "25-34"
                edad <= 44 -> "35-44"
                edad <= 54 -> "45-54"
                edad <= 64 -> "55-64"
                else -> "65+"
            }

            val enfermedadRaw = caso["enfermedad"]?.trim()
            val enfermedad = if (enfermedadRaw.isNullOrEmpty()) "Desconocida" else enfermedadRaw

            val mapaEnfermedades = conteoPorRango[claveRango]!!
            mapaEnfermedades[enfermedad] = mapaEnfermedades.getOrDefault(enfermedad, 0) + 1
        }

        val enfermedadesUnicas = conteoPorRango.values
            .flatMap { it.keys }
            .distinct()
            .sorted()

        val dataset = DefaultCategoryDataset()

        enfermedadesUnicas.forEach { enf ->
            rangos.forEach { rango ->
                val cantidad = conteoPorRango[rango]?.get(enf) ?: 0
                dataset.addValue(cantidad.toDouble(), enf, rango)
            }
        }

        val chart: JFreeChart = ChartFactory.createStackedBarChart(
            "Casos de enfermedades por grupos de edad",
            "Rango de Edad",
            "Número de Casos",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        )

        val coloresModernos = listOf(
            Color(59, 130, 246),  // Azul brillante (el mismo de tu otro gráfico)
            Color(16, 185, 129),  // Verde esmeralda
            Color(245, 158, 11),  // Naranja ámbar
            Color(239, 68, 68),   // Rojo coral
            Color(139, 92, 246),  // Morado
            Color(14, 165, 233)   // Azul cielo
        )



        chart.title.font = Font("SansSerif", Font.BOLD, 12)

        val plot = chart.plot as CategoryPlot
        estiloFondo(plot)

        val renderer = plot.renderer as StackedBarRenderer
        renderer.barPainter = StandardBarPainter() // Elimina el degradado 3D feo
        renderer.setShadowVisible(false)           // Elimina las sombras
        renderer.isDrawBarOutline = false

        for (i in enfermedadesUnicas.indices) {
            renderer.setSeriesPaint(i, coloresModernos[i % coloresModernos.size])
        }

        estilizarBarras(chart)

        return chartToImage(chart)
    }

    private fun graficoBarrasEdadTotal(datos: List<Map<String, String>>): Image {

        val rangos = listOf("0-14", "15-24", "25-34", "35-44", "45-54", "55-64", "65+")
        val contadores = mutableMapOf<String, Int>()
        rangos.forEach { contadores[it] = 0 }

        datos.forEach { caso ->
            val edadStr = caso["edad"]?.trim()
            val edad = edadStr?.toIntOrNull() ?: 0

            val claveRango = when {
                edad <= 14 -> "0-14"
                edad <= 24 -> "15-24"
                edad <= 34 -> "25-34"
                edad <= 44 -> "35-44"
                edad <= 54 -> "45-54"
                edad <= 64 -> "55-64"
                else -> "65+"
            }

            contadores[claveRango] = contadores[claveRango]!! + 1
        }

        val dataset = DefaultCategoryDataset()
        rangos.forEach { rango ->
            dataset.addValue(contadores[rango]!!.toDouble(), "Casos Totales", rango)
        }

        val chart: JFreeChart = ChartFactory.createBarChart(
            "Casos totales registrados por edad",
            "Rango de Edad",
            "Número de Casos",
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        )

        chart.title.font = Font("SansSerif", Font.BOLD, 12)

        val plot = chart.plot as CategoryPlot
        estiloFondo(plot)

        val renderer = plot.renderer as BarRenderer
        renderer.barPainter = StandardBarPainter()
        renderer.setShadowVisible(false)
        renderer.isDrawBarOutline = false
        renderer.setSeriesPaint(0, Color(59, 130, 246))

        return chartToImage(chart)
    }

    private fun graficoRelacionAnoSexoCasos(datos: List<Map<String, String>>): Image {
        val datosAgrupados = mutableMapOf<String, IntArray>()

        datos.forEach { caso ->
            val fecha = caso["fechacaso"]?.trim()

            if (fecha.isNullOrEmpty() || fecha.length < 4) return@forEach

            val ano = fecha.substring(0, 4) // Extraemos "YYYY"
            val sexo = caso["sexo"]?.trim()?.uppercase()

            val contadores = datosAgrupados.getOrPut(ano) { intArrayOf(0, 0, 0) }

            contadores[0]++

            if (sexo == "H") {
                contadores[1]++
            } else if (sexo == "M") {
                contadores[2]++
            }
        }

        val anosOrdenados = datosAgrupados.keys.sorted()

        val dataset = DefaultCategoryDataset()
        for (ano in anosOrdenados) {
            val contadores = datosAgrupados[ano]!!
            dataset.addValue(contadores[0].toDouble(), "Ambos", ano)
            dataset.addValue(contadores[1].toDouble(), "Hombres", ano)
            dataset.addValue(contadores[2].toDouble(), "Mujeres", ano)
        }

        val chart: JFreeChart = ChartFactory.createLineChart(
            "Distribución de casos totales según sexo y año",
            "Año",
            "Número de Casos",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        )

        chart.title.font = Font("SansSerif", Font.BOLD, 12)

        val plot = chart.plot as CategoryPlot
        estiloFondo(plot)

        val renderer = plot.renderer as LineAndShapeRenderer

        renderer.defaultShapesVisible = true

        renderer.defaultStroke = BasicStroke(2.0f)

        return chartToImage(chart)
    }

    private fun graficosVectoresBarras(datos: List<Map<String, String>>): Image {
        val dataset = DefaultCategoryDataset()

        datos.groupBy { caso ->
            val vector = caso["vector"]?.trim()
            if (vector.isNullOrEmpty()) "Desconocido" else vector
        }
            .mapValues { entrada ->
                entrada.value.sumOf { caso ->
                    caso["numero"]?.toIntOrNull() ?: 0
                }
            }
            .forEach { (vector, totalMuestras) ->
                dataset.addValue(totalMuestras.toDouble(), "Muestras", vector)
            }

        val chart = ChartFactory.createBarChart(
            "Muestras obtenidas por vectores",
            "Vector",
            "Número de muestras",
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        )

        chart.title.font = Font("SansSerif", Font.BOLD, 12)

        val plot = chart.plot as CategoryPlot
        estiloFondo(plot)

        val renderer = plot.renderer as BarRenderer
        renderer.barPainter = StandardBarPainter()
        renderer.setShadowVisible(false)
        renderer.isDrawBarOutline = false
        renderer.setSeriesPaint(0, Color(59, 130, 246))

        estilizarBarras(chart)
        return chartToImage(chart)
    }

    private fun graficoAnilloVectores(datos: List<Map<String, String>>): Image {
        val dataset = DefaultPieDataset<String>()

        datos.groupBy { caso ->
            val generoRaw = caso["genero"]?.trim() ?: "Desconocido"


            if (generoRaw == "H") {
                "Hembras"
            } else if (generoRaw == "M") {
                "Machos"
            } else {
                "Desconocido"
            }
        }
            .mapValues { entrada ->
                entrada.value.sumOf { caso ->
                    caso["numero"]?.trim()?.toIntOrNull() ?: 0
                }
            }
            .forEach { (genero, sumaTotal) ->
                dataset.setValue(genero, sumaTotal.toDouble())
            }

        val chart = ChartFactory.createRingChart(
            "Proporción por Género",
            dataset,
            true,
            true,
            false
        )

        chart.title.font = Font("SansSerif", Font.BOLD, 12)

        val plot = chart.plot as PiePlot<*>
        plot.labelGenerator = null
        plot.backgroundPaint = Color.WHITE
        plot.isOutlineVisible = false
        plot.shadowPaint = null
        plot.sectionOutlinesVisible = false

        return chartToImage(chart)
    }


    private fun graficoBarrasGarrapatas(datos: List<Map<String, String>>, modoPorcentaje: Boolean): Image {
        val datosAgrupados = datos.groupBy { caso ->
            val key = caso.keys.firstOrNull { it.equals("especie", ignoreCase = true) }
            val especieStr = if (key != null) caso[key]?.toString()?.trim() else null

            if (especieStr.isNullOrEmpty() || especieStr.equals("null", ignoreCase = true)) "Desconocida" else especieStr
        }.map { (especie, casos) ->
            var humano = 0
            var animal = 0

            casos.forEach { caso ->
                val hKey = caso.keys.firstOrNull { it.equals("enHumano", ignoreCase = true) }
                val aKey = caso.keys.firstOrNull { it.equals("animal", ignoreCase = true) }

                val valHumano = if (hKey != null) caso[hKey]?.toString()?.trim() else null
                val valAnimal = if (aKey != null) caso[aKey]?.toString()?.trim() else null

                if (valHumano.equals("Y", ignoreCase = true)) humano++
                if (!valAnimal.isNullOrEmpty()) animal++
            }

            Triple(especie, humano, animal)
        }.sortedByDescending { it.second + it.third }

        val dataset = DefaultCategoryDataset()
        for ((especie, humano, animal) in datosAgrupados) {
            dataset.addValue(humano.toDouble(), "En Humanos", especie)
            dataset.addValue(animal.toDouble(), "En Animales", especie)
        }

        val tituloEjeY = if (modoPorcentaje) "Porcentaje (%)" else "Cantidad Absoluta"
        val chart: JFreeChart = ChartFactory.createStackedBarChart(
            "Especies de garrapatas recogidas",
            "Especie",
            tituloEjeY,
            dataset,
            PlotOrientation.VERTICAL,
            true,
            false,
            false
        )

        chart.title.font = Font("SansSerif", Font.BOLD, 12)

        val plot = chart.plot as CategoryPlot
        estiloFondo(plot)

        return chartToImage(chart)
    }

    private val MESES = listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")

    private fun obtenerValor(mapa: Map<String, String>, clave: String): String? {
        return mapa.entries.firstOrNull { it.key.equals(clave, ignoreCase = true) }?.value
    }

    private fun graficoBarrasFechaGarrapatas(datos: List<Map<String, String>>): Image {
        val dataset = DefaultCategoryDataset()

        val datosProcesados = datos.mapNotNull { caso ->
            val fechaStr = obtenerValor(caso, "fechaRecogida")?.trim()
            if (fechaStr.isNullOrEmpty() || fechaStr.equals("null", ignoreCase = true)) return@mapNotNull null

            try {
                val sinHora = fechaStr.substringBefore(" ").substringBefore("T")

                val fechaNormalizada = sinHora.replace("/", "-")

                val fecha = if (fechaNormalizada.substringBefore("-").length == 4) {
                    LocalDate.parse(fechaNormalizada)
                } else {
                    LocalDate.parse(fechaNormalizada, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                }

                val clave = "${fecha.year}-${fecha.monthValue.toString().padStart(2, '0')}"
                val etiqueta = "${MESES[fecha.monthValue - 1]} ${fecha.year}"

                Pair(clave, etiqueta)
            } catch (e: Exception) {
                println("Omitiendo fecha inválida: $fechaStr")
                null
            }
        }
            .groupBy { it.first }
            .map { (clave, lista) ->
                val etiqueta = lista.first().second
                val cantidad = lista.size
                Triple(clave, etiqueta, cantidad)
            }
            .sortedBy { it.first }

        for ((_, etiqueta, cantidad) in datosProcesados) {
            dataset.addValue(cantidad.toDouble(), "Garrapatas recogidas", etiqueta)
        }

        val chart: JFreeChart = ChartFactory.createBarChart(
            "Garrapatas recogidas por mes",
            "Mes",
            "Nº de recogidas",
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        )

        chart.title.font = Font("SansSerif", Font.BOLD, 12)

        val plot = chart.plot as CategoryPlot
        estiloFondo(plot)

        val renderer = plot.renderer as BarRenderer
        renderer.barPainter = StandardBarPainter()
        renderer.setShadowVisible(false)
        renderer.isDrawBarOutline = false
        renderer.setSeriesPaint(0, Color(59, 130, 246))

        return chartToImage(chart)
    }

    private fun graficoAnilloGarrapatas(datos: List<Map<String, String>>): Image {
        val dataset = DefaultPieDataset<String>()

        datos.groupBy { caso ->
            val especie = caso["especie"]?.trim() ?: "Desconocida"
            especie
        }
            .mapValues { it.value.size }
            .forEach { (especie, count) ->
                dataset.setValue(especie, count)
            }

        val chart = ChartFactory.createRingChart(
            "Especies de garrapatas recogidas",
            dataset,
            true,
            true,
            false
        )

        chart.title.font = Font("SansSerif", Font.BOLD, 12)

        val plot = chart.plot as PiePlot<*>
        plot.labelGenerator = null
        plot.backgroundPaint = Color.WHITE
        plot.isOutlineVisible = false
        plot.shadowPaint = null
        plot.sectionOutlinesVisible = false


        return chartToImage(chart)

    }

    private fun estilizarBarras(chart: JFreeChart) {
        val plot = chart.plot as? CategoryPlot ?: return
        val axis = plot.domainAxis as? CategoryAxis ?: return
        axis.categoryLabelPositions = CategoryLabelPositions.UP_45
    }

    private fun chartToImage(chart: JFreeChart, width: Int = 500, height: Int = 300): Image {
        val buffered: BufferedImage = chart.createBufferedImage(width, height)
        val baos = ByteArrayOutputStream()
        ImageIO.write(buffered, "PNG", baos)
        return Image.getInstance(baos.toByteArray()).also {
            it.scaleToFit(width.toFloat(), height.toFloat()) // Ancho máximo A4 con márgenes
        }
    }


}