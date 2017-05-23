package cn.apier.common.util;


import cn.apier.common.exception.CommonException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.apier.common.api.Result;
import cn.apier.common.exception.BaseException;
import cn.apier.common.exception.CommonErrorCodes;
import cn.apier.common.page.CommonPageInfo;
import cn.apier.common.page.PageRequest;
import cn.apier.common.util.func.ConditionalExceptionFunc;
import cn.apier.common.util.func.LoadCheckExceptionFunc;
import cn.apier.common.util.func.LoadConditionThenFunc;
import com.netflix.spectator.api.Registry;
import com.netflix.spectator.api.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by yanjunhua on 16/5/30.
 */
final public class ExecuteTool
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteTool.class);
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(20);

    /**
     * 用try获取RuntimeException,并封装成Result返回
     *
     * @param runnable
     * @return
     */
    public static Result executeWithTry(Runnable runnable)
    {
        Result result = null;
        try
        {
            runnable.run();
            result = Result.OK();
        } catch (BaseException ex)
        {
            LOGGER.info("Got  BaseException:[{}]", ex);
            result = Result.FAIL(ex.getCode(), ex.getMessage(),ex.getData());
        } catch (RuntimeException ex)
        {
            LOGGER.info("Got  RuntimeException:[{}]", ex);
            result = checkRuntimeException(ex);
        }
        return result;
    }

    /**
     * 异步执行查询并用try封装，查询结果通过callback返回
     * 注意：无法获取线程变量，需要线程变量请传入
     *
     * @param supplierFunc
     * @param callback
     * @param <R>
     */

    public static <R> void asyncExecuteQueryWithTry(Supplier<R> supplierFunc, Consumer<R> callback)
    {
        Objects.requireNonNull(callback, "Call back can not be Null");
        EXECUTOR_SERVICE.submit(() ->
        {
            try
            {
                R result = supplierFunc.get();
                callback.accept(result);
            } catch (RuntimeException ex)
            {
                throw ex;
            }
        });
    }

    /**
     * 异步执行功能，并在成功后执行callback
     * 注意：无法获取线程变量，需要线程变量请传入
     *
     * @param func
     * @param callback
     */
    public static void asyncExecuteThen(Runnable func, Runnable callback)
    {
        EXECUTOR_SERVICE.submit(() ->
        {
            try
            {
                func.run();
                if (Objects.nonNull(callback))
                {
                    callback.run();
                }
            } catch (RuntimeException ex)
            {
                throw ex;
            }
        });
    }

    /**
     * 异步执行
     * 注意：无法获取线程变量，需要线程变量请传入
     *
     * @param func
     */
    public static void asyncExecuteThen(Runnable func)
    {
        asyncExecuteThen(func, null);
    }


    private static Result checkRuntimeException(RuntimeException ex)
    {
        Result result = null;

        Throwable throwable = ex.getCause();
        if (throwable instanceof BaseException)
        {
            BaseException baseException = (BaseException) throwable;
            result = Result.FAIL(baseException.getCode(), "");
        } else
        {
            LOGGER.info("Got Unknown Exception:[{}]", ex);
            result = Result.FAIL(CommonErrorCodes.COMMON_UNKNOWN_EXCEPTION, ex.getMessage());
        }
        return result;
    }

    public static <R> R executeQueryWithTimer(Supplier<R> func, Registry registry, String name, String... tags)
    {

        R result = null;
        ResultContainer<R> resultContainer = new ResultContainer<R>();
        Timer timer = registry.timer(name, tags);
        timer.record(() ->
        {
            R funcResult = func.get();
            resultContainer.setResult(funcResult);
        });
        return resultContainer.getResult();
    }

    /**
     * 同步执行查询功能，返回结果封装到Result
     *
     * @param supplierFunc
     * @return
     */
    public static Result executeQueryWithTry(Supplier supplierFunc)
    {
        Result result = null;
        try
        {
            Object data = supplierFunc.get();
            result = Result.OK(data);
        } catch (BaseException ex)
        {
            LOGGER.info("Got  Exception:[{}]", ex.getCode());
            result = Result.FAIL(ex.getCode(), "");
        } catch (RuntimeException ex)
        {
            result = checkRuntimeException(ex);
        }
        return result;
    }

    /**
     * conditionFunc返回true时抛出异常
     *
     * @param conditionFunc 检查的条件函数
     * @param exceptionFunc 提供抛出的异常
     */

    public static void conditionalException(BooleanSupplier conditionFunc, Supplier<? extends BaseException> exceptionFunc)
    {
//        if (conditionFunc.getAsBoolean())
//        {
//            BaseException newException = exceptionFunc.get();
//            LOGGER.info("Condition is True, throw exception [{}]", newException.getCode());
//            throw newException;
//        }
        ifTrue(conditionFunc).exception(exceptionFunc).execute();
    }


    @Deprecated
    public static <T> void loadThenProcessFlow(Supplier<T> loadFunc, Consumer<T>... processFuncs)
    {
        loadThenProcessFlow(loadFunc, null, processFuncs);
    }

    @Deprecated
    public static <T, R extends RuntimeException> void loadThenProcessFlow(Supplier<T> loadFunc, Function<T, R> exceptionFunc, Consumer<T>... processFuncs)
    {
        Objects.requireNonNull(loadFunc);
        LoadConditionThenFunc<T> tLoadConditionThenFunc = new LoadConditionThenFunc<T>(loadFunc);
        if (Objects.nonNull(exceptionFunc))
        {
            tLoadConditionThenFunc.exception(exceptionFunc);
        }
        Arrays.asList(processFuncs).forEach(tConsumer -> tLoadConditionThenFunc.then(tConsumer));
        tLoadConditionThenFunc.execute();
    }

    /**
     * 加载数据后进行异常判断和后续处理
     *
     * @param loadFunc
     * @param <T>
     * @return
     */
    public static <T> LoadConditionThenFunc<T> loadAndThen(Supplier<T> loadFunc)
    {
        return new LoadConditionThenFunc(loadFunc);
    }

    /**
     * 新建数据后进行异常判断和后续处理
     *
     * @param loadFunc
     * @param <T>
     * @return
     * @see #loadAndThen
     */
    public static <T> LoadConditionThenFunc<T> newInstance(Supplier<T> loadFunc)
    {
        return new LoadConditionThenFunc(loadFunc);
    }


    /**
     * 满足条件抛出异常
     *
     * @param conditionFunc
     * @return
     */
    public static ConditionalExceptionFunc ifTrue(BooleanSupplier conditionFunc)
    {
        return new ConditionalExceptionFunc(conditionFunc);
    }


    /**
     * 加载数据后进行异常判断
     *
     * @param loadFunc
     * @param <T>
     * @return
     */
    public static <T> LoadCheckExceptionFunc<T> loadAndCheck(Supplier<T> loadFunc)
    {
        return new LoadCheckExceptionFunc<T>(loadFunc);
    }

    /**
     * 参数为空抛出参数异常
     */
    public static void checkParameterNonNull(Object para)
    {
        conditionalException(() -> Objects.isNull(para), () -> CommonException.parameterError());
    }

    /**
     * 检查字符串是否为Null或者空串，如果是就抛出异常
     *
     * @param para
     */
    public static void checkStringParameterNonNullOrEmpty(String para)
    {
        conditionalException(() -> Objects.isNull(para) || para.isEmpty(), () -> CommonException.parameterError());
    }


    public static <T extends List> T queryPageData(Supplier<T> queryFunc)
    {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        String queryUrl = httpServletRequest.getRequestURL().toString();


        String strPageNo = null;
        String strPageSize = null;
        Map<String, String[]> stringStringMap = httpServletRequest.getParameterMap();
        if (!stringStringMap.isEmpty())
        {
            List<String> paras = new ArrayList<>();
            stringStringMap.keySet().forEach(s ->
            {
                if (!"pageNo".equals(s) && !"pageSize".equals(s))
                {
                    String[] vs = stringStringMap.get(s);
                    for (String v : vs)
                    {
                        paras.add(s + "=" + v);
                    }
                }
            });

            if (stringStringMap.containsKey("pageNo"))
            {
                String[] pageNos = stringStringMap.get("pageNo");
                if (pageNos.length > 0)
                {
                    strPageNo = pageNos[0];
                    LOGGER.debug("Found PageNo:[{}]", strPageNo);
                }
            }

            if (stringStringMap.containsKey("pageSize"))
            {
                String[] pageSizes = stringStringMap.get("pageSize");
                if (pageSizes.length > 0)
                {
                    strPageSize = pageSizes[0];
                    LOGGER.debug("Found PageSize:[{}]", strPageSize);
                }
            }

            String paraStr = String.join("&", paras);
            queryUrl = queryUrl + "?" + paraStr;
        }


        int pageNo = 1;
        int pageSize = 10;

        if (Objects.nonNull(strPageNo))
        {
            try
            {
                pageNo = Integer.valueOf(strPageNo);
            } catch (NumberFormatException ex)
            {
                LOGGER.warn("Parameter [pageNo] is not Number,skip");
            }
        }

        if (Objects.nonNull(strPageSize))
        {
            try
            {
                pageSize = Integer.valueOf(strPageSize);
            } catch (NumberFormatException ex)
            {
                LOGGER.warn("Parameter [strPageSize] is not Number,skip");
            }
        }


        PageRequest pageRequest = PageRequest.validateAndBuild(pageNo, pageSize);
        PageHelper.startPage(pageRequest.getPageNo(), pageRequest.getPageSize());
        T result = queryFunc.get();
        PageInfo pageInfo = new PageInfo(result);

        CommonPageInfo commonPageInfo = new CommonPageInfo(queryUrl, pageInfo);

        servletRequestAttributes.setAttribute("commonPageInfo", commonPageInfo, RequestAttributes.SCOPE_REQUEST);

        return result;
    }

}
