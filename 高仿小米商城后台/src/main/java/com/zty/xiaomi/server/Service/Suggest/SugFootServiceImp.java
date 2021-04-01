package com.zty.xiaomi.server.Service.Suggest;

import com.zty.xiaomi.server.Dao.Suggest;
import com.zty.xiaomi.server.Entity.Suggest.SuggestFoot;
import com.zty.xiaomi.server.utils.SqlSessionUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class SugFootServiceImp implements SugFootService{
    @Override
    public SqlSession getSqlSession() throws IOException {
        String resource = "mybatis-config.xml";//通过流处理获取sqlSessionFactory创建一个实例
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();//获取SqlSession实例
        return session;
    }

    @Override
    public List<SuggestFoot> getSugFoot() throws IOException {

        SqlSession sqlSession = SqlSessionUtil.getInstance();
        List<SuggestFoot> suggestFoots = sqlSession.getMapper(Suggest.class).getSuggest();
        return suggestFoots;
    }
}
