package cn.apier.standard.common

/**
 * Created by yanjunhua on 2017/6/14.
 */


object ModelRepository {
//    fun <M, E> loadModel(dataProvider: DataProvider<E>, builder: ModelBuilder<M, E>, id: String): M =
//            builder.build(dataProvider.data(id))


    fun <M, E> loadModel(id: String, dataProvider: (id: String) -> E, builder: (e: E) -> M): M = builder(dataProvider(id))

}


