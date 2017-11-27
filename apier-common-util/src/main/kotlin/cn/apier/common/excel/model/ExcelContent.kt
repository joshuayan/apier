package cn.apier.common.excel.model

import java.util.*
import kotlin.collections.ArrayList

class ExcelContent(var name: String = "", var path: String = "") {

    private val rows: MutableList<RowContent> = ArrayList()

    fun addRow(row: RowContent) {
        this.rows.add(row)
    }

    fun rows() = Collections.unmodifiableList(this.rows)

    fun rowCount() = this.rows.size
    override fun toString(): String {
        return "ExcelContent(name='$name', path='$path', rows=$rows)"
    }


}