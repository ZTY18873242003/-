package com.zty.xiaomi.server.Service.Order;

import com.zty.xiaomi.server.Entity.Address.Addre;
import com.zty.xiaomi.server.Entity.Order.*;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;

public interface OrderService {

    SqlSession getSqlSession() throws IOException;

    OrderList creatOrder(OrdCreaParm ordCreaParm) throws IOException;

    List<orderItemVoList> getOrderItems(String userid) throws IOException;

    Addre getAdd(String userid, int id) throws IOException;

    OrdFina getOrderById(int id) throws IOException;

    List<UserOrdList> getOrderList(String userid) throws IOException;

    List<UserOrdItemList> getOrderListItems(int orderId) throws IOException;

}
