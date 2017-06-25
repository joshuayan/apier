import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yanjunhua on 2017/5/24.
 */
public class TestMain
{
    private static Logger logger= LoggerFactory.getLogger(TestMain.class);
    public static void main(String[] args)
    {
        Flowable.range(1,100).observeOn(Schedulers.newThread()).subscribe(integer -> {
//            Thread.sleep(500);
            logger.debug("get {}",integer);
        });
    }
}
