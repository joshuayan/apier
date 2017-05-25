package cn.apier.auth.application.service;

import cn.apier.auth.domain.model.ClientApplication;
import cn.apier.auth.domain.repository.ClientApplicationRepository;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Created by yanjunhua on 2017/5/24.
 */
@Service
public class AuthService
{
    @Autowired
    private ClientApplicationRepository clientApplicationRepository;

    public boolean validateTokenRequest(String appKey, String ts, String sign)
    {
//        ClientApplication clientApplication = this.clientApplicationRepository.findByAppKey(appKey);

        Flowable.create(flowableEmitter ->
        {
            flowableEmitter.onNext(this.clientApplicationRepository.findByAppKey(appKey));
            flowableEmitter.onComplete();
        }, BackpressureStrategy.BUFFER).filter(o -> Objects.nonNull(o)).subscribe(o ->{});

        return false;
    }
}
