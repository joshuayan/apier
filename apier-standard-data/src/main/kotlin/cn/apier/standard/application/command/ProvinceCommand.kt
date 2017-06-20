package cn.apier.standard.application.command

/**
 * Created by yanjunhua on 2017/6/20.
 */


data class CreateProvinceCommand(val uid: String, val countryId: String, val name: String, val description: String?)

data class UpdateProvinceCommand(val uid: String, val countryId: String, val name: String, val description: String?)

