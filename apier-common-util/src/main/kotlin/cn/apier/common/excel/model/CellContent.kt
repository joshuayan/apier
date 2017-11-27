package cn.apier.common.excel.model


data class CellContent(val rowNumber: Int, val colNumber: Int, val content: String, val contentType: CellContentType)