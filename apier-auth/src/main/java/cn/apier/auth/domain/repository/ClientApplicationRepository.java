package cn.apier.auth.domain.repository;

import cn.apier.auth.domain.model.ClientApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by yanjunhua on 2017/5/24.
 */
@Repository
public interface ClientApplicationRepository extends JpaRepository<ClientApplication, String>
{
    ClientApplication findByAppKey(String appKey);
}
