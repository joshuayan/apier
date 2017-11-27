package cn.apier.common.excel

import cn.apier.common.excel.model.CellContent
import cn.apier.common.excel.model.CellContentType
import cn.apier.common.excel.model.ExcelContent
import cn.apier.common.excel.model.RowContent
import cn.apier.common.util.DateTimeUtil
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.apache.poi.hssf.usermodel.HSSFDateUtil
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File

object ExcelParser {

    private val defaultDateFormat = "yyyy-MM-dd HH:mm:ss"

    fun parseFile(file: File, skipTitle: Boolean = false): ExcelContent {
        val result = ExcelContent(file.name, file.absolutePath)

        if (file.exists()) {
            when (file.extension) {
                "xlsx" -> {
                    val workbook = XSSFWorkbook(file)
                    val sheet0 = workbook.getSheetAt(0)

                    runBlocking {
                        sheet0.rowIterator().forEach {

                            val tmpc = launch(coroutineContext) {

                                if (!skipTitle || it.rowNum != 0) {

                                    val row = RowContent(it.rowNum)
                                    it.cellIterator().forEach {
                                        row.addCell(buildCellContent(it))
                                    }
                                    result.addRow(row)
                                }
                            }
                            tmpc.join()
                        }

                    }
                }
                "xsl" -> {
                }
            }
        }

        return result
    }

    fun parseFromPath(path: String, skipTitle: Boolean = false): ExcelContent = parseFile(File(path), skipTitle)


    private fun buildCellContent(cell: Cell): CellContent {

        var content: String = ""
        var cellType: CellContentType = CellContentType.NULL
        when (cell.cellTypeEnum) {
            CellType.BLANK -> {
                content = ""
                cellType = CellContentType.NULL
            }
            CellType.STRING -> {
                content = cell.stringCellValue
                cellType = CellContentType.STRING
            }
            CellType.NUMERIC -> {
                val tmpContent = cell.numericCellValue
                content = "$tmpContent"
                cellType = CellContentType.NUMERIC
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    cellType = CellContentType.DATE
//                    val formatStr = cell.cellStyle.dataFormatString
                    cell.cellStyle.dataFormat
                    content = DateTimeUtil.formatDate(cell.dateCellValue, defaultDateFormat)

                }

            }

            CellType.BOOLEAN -> {
                val tmpContent = cell.booleanCellValue
                content = "$tmpContent"
                cellType = CellContentType.STRING
            }


            else -> {

            }
        }

        return CellContent(cell.rowIndex, cell.columnIndex, content, cellType)

    }

}