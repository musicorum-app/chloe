package com.musicorumapp.generator.themes

import de.umass.lastfm.ImageSize
import de.umass.lastfm.Period
import de.umass.lastfm.User
import java.awt.image.BufferedImage
import java.io.File
import java.math.BigInteger
import java.net.URL
import java.security.MessageDigest
import javax.imageio.ImageIO


class Grid {
    private val API_KEY = ""
    private val SIZE = 7
    private val dir = File("./images")

    fun generate(user: String): BufferedImage {
        val startTime = System.currentTimeMillis()
        val chart = User.getTopAlbums(user, Period.OVERALL, API_KEY)
        val items = chart.iterator()

        val coverSize = 300
        val image = BufferedImage(coverSize * SIZE, coverSize * SIZE, BufferedImage.TYPE_INT_RGB)
        val graphics = image.createGraphics()

        for (i in 0 until SIZE) {
            for (j in 0 until SIZE) {
                if (items.hasNext()) {
                    val x = coverSize * j
                    val y = coverSize * i
                    val url = items.next().getImageURL(ImageSize.EXTRALARGE)
                    val img = getImage(url)
                    println(getSize(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()))
                    graphics.drawImage(img, x, y, coverSize, coverSize, null)
                }
            }
        }

        System.gc()

        graphics.dispose()
        val endTime = System.currentTimeMillis()
        println(endTime - startTime)
        //ImageIO.write(image, "jpg", File(dir.absolutePath + File.separator + "result_grid.jpg"))
        System.gc()
        println(getSize(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()))
        return image
    }

    fun getSize(size: Long): String? {
        val n: Long = 1000
        var s = ""
        val kb = size / n.toDouble()
        val mb = kb / n
        val gb = mb / n
        val tb = gb / n
        if (size < n) {
            s = "$size Bytes"
        } else if (size >= n && size < n * n) {
            s = String.format("%.2f", kb) + " KB"
        } else if (size >= n * n && size < n * n * n) {
            s = String.format("%.2f", mb) + " MB"
        } else if (size >= n * n * n && size < n * n * n * n) {
            s = String.format("%.2f", gb) + " GB"
        } else if (size >= n * n * n * n) {
            s = String.format("%.2f", tb) + " TB"
        }
        return s
    }

    private fun hashImage(url: String): String {
        val md = MessageDigest.getInstance("SHA-1")

        val messageDigest = md.digest(url.toByteArray())
        val no = BigInteger(1, messageDigest)

        return no.toString(32).toUpperCase()
    }

    private fun getImage(url: String): BufferedImage {
        if (url === "") {
            return BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB)
        }
        val hash = hashImage(url)

        if (!dir.exists()) dir.mkdir()
        val file = File(dir.absolutePath + File.separator + "${hash}.png")

        return if (!file.exists()) {
            val stream = URL(url)
            val image = ImageIO.read(stream)
            ImageIO.write(image, "png", file)

            image
        } else {
            ImageIO.read(file)
        }
    }
}