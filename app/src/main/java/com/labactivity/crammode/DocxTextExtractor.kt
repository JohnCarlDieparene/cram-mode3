package com.labactivity.crammode

import org.w3c.dom.Document
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipInputStream
import javax.xml.parsers.DocumentBuilderFactory

object DocxTextExtractor {
    fun extractText(docxFile: File): String {
        val sb = StringBuilder()
        ZipInputStream(FileInputStream(docxFile)).use { zip ->
            var entry = zip.nextEntry
            while (entry != null) {
                if (entry.name == "word/document.xml") {
                    val dbFactory = DocumentBuilderFactory.newInstance()
                    val dBuilder = dbFactory.newDocumentBuilder()
                    val doc: Document = dBuilder.parse(zip)
                    doc.documentElement.normalize()

                    val nodes = doc.getElementsByTagName("w:t")
                    for (i in 0 until nodes.length) {
                        val node = nodes.item(i)
                        sb.append(node.textContent)
                    }
                    break
                }
                entry = zip.nextEntry
            }
        }
        return sb.toString()
    }
}
