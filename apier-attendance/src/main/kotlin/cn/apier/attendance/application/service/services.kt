package cn.apier.attendance.application.service

import cn.apier.attendance.query.dao.AttendanceUserEntryRepository
import com.github.stuxuhai.jpinyin.PinyinHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class UserFixService {

    @Autowired
    private lateinit var userEntryRepository: AttendanceUserEntryRepository

    fun fixMnemonic() {
        this.userEntryRepository.findByMnemonicIsNull().parallelStream().forEach {
            val mnemonic = PinyinHelper.getShortPinyin(it.name)
            it.mnemonic = mnemonic
            this.userEntryRepository.save(it)
        }
    }
}
