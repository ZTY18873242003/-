
package com.zty.xiaomi.server.Service.Order;

import com.zty.xiaomi.server.Dao.Order;
import com.zty.xiaomi.server.Entity.Address.Addre;
import com.zty.xiaomi.server.Entity.Cart.cartProduct;
import com.zty.xiaomi.server.Entity.Cart.cartProductVoList;
import com.zty.xiaomi.server.Entity.Order.OrdCreaParm;
import com.zty.xiaomi.server.Entity.Order.OrdFina;
import com.zty.xiaomi.server.Entity.Order.OrderList;
import com.zty.xiaomi.server.Entity.Order.orderItemVoList;
import com.zty.xiaomi.server.Entity.User;
import com.zty.xiaomi.server.Service.Cart.CartServiceImp;
import com.zty.xiaomi.server.Service.RegLogin.RegLogServiceImp;
import com.zty.xiaomi.server.utils.OrderNumUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private RegLogServiceImp regLogServiceImp;
    @Autowired
    private CartServiceImp cartServiceImp;

    @Override
    public SqlSession getSqlSession() throws IOException {
        String resource = "mybatis-config.xml";//通过流处理获取sqlSessionFactory创建一个实例
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();//获取SqlSession实例
        return session;
    }

    public OrderList creatOrder(OrdCreaParm ordCreaParm) throws IOException {
        SqlSession sqlSession = getSqlSession();
        OrderList orderList = new OrderList();
        User user = regLogServiceImp.getUserByUserName(ordCreaParm.getName());
        String userid = user.getUserid();

        int orderNumber = OrderNumUtil.getOrderNumber();
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        int payment=0;
        List<cartProduct> cartproducts = cartServiceImp.getCartProduct(userid).getList();
        for(cartProduct cartProductvo:cartproducts){
            payment+=cartProductvo.getProductTotalPrice();
        }


        orderList.setOrderNo(orderNumber);
        orderList.setPayment(payment);
        orderList.setStatus(10);//表示未支付的状态，支付完后要改值
        orderList.setStatusDesc("未支付");
        orderList.setCreateTime(createTime);
        orderList.setImageHost("www.mi.com");
        orderList.setShippingId(ordCreaParm.getShippingId());
        orderList.setReceiverName(getAdd(userid,ordCreaParm.getShippingId()).getReceiverName());
        orderList.setShippingVo(getAdd(userid,ordCreaParm.getShippingId()));

        sqlSession.getMapper(Order.class).insOrder(orderNumber,payment,1,"在线支付",0,10,"未支付",
                "","",createTime.trim(),"www.mi.com",ordCreaParm.getShippingId(),getAdd(userid,ordCreaParm.getShippingId()).getReceiverName(),
                getAdd(userid,ordCreaParm.getShippingId()).getReceiverMobile(),getAdd(userid,ordCreaParm.getShippingId()).getReceiverProvince(),
                getAdd(userid,ordCreaParm.getShippingId()).getReceiverCity(),getAdd(userid,ordCreaParm.getShippingId()).getReceiverAddress(),
                getAdd(userid,ordCreaParm.getShippingId()).getReceiverZip());
        sqlSession.commit();
        sqlSession.close();
        return orderList;
    }

    @Override
    public List<orderItemVoList> getOrderItems(String userid) throws IOException {
        SqlSession sqlSession = getSqlSession();
        List<orderItemVoList> orderItems = sqlSession.getMapper(Order.class).getOrderItems(userid);
        return orderItems;
    }

    @Override
    public Addre getAdd(String userid, int id) throws IOException {
        SqlSession sqlSession = getSqlSession();
        Addre addre = sqlSession.getMapper(Order.class).getAddre(userid, id);
        return addre;
    }

    @Override
    public OrdFina getOrderById(int id) throws IOException {
        SqlSession sqlSession = getSqlSession();
        OrdFina orderById = sqlSession.getMapper(Order.class).getOrderById(id);
        return orderById;
    }
}
