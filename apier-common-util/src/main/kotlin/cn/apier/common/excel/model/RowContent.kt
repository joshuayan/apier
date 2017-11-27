package cn.apier.common.excel.model

import java.util.*
import kotlin.collections.ArrayList

class RowContent(val rowNumber: Int) {

    private val cells: MutableList<CellContent> = ArrayList()

    fun addCell(cell: CellContent) {
        this.cells.add(cell)
    }

    fun cells(): List<CellContent> = Collections.unmodifiableList(this.cells)
    override fun toString(): String {
        return "RowContent(rowNumber=$rowNumber, cells=$cells)"
    }

}