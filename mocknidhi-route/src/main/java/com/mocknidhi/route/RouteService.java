package com.mocknidhi.route;

import com.mocknidhi.persistence.entity.Mock;

import java.util.List;

/**
 * Created by anilkumartalla on 2/8/17.
 */
public interface RouteService {

    void deploy(List<Mock> mockList);

    void undeploy();

}
