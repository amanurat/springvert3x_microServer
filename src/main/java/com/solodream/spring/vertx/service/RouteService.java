package com.solodream.spring.vertx.service;

import com.alibaba.fastjson.JSON;
import com.solodream.spring.vertx.common.DistanceUtil;
import com.solodream.spring.vertx.common.SoloConstants;
import com.solodream.spring.vertx.jpa.domain.PoiInfoDto;
import com.solodream.spring.vertx.jpa.domain.RouteContractInfoDto;
import com.solodream.spring.vertx.mapper.PoiInfoMapper;
import com.solodream.spring.vertx.mapper.RouteInfoMapper;
import com.solodream.spring.vertx.req.client.GetRouteDetailReq;
import com.solodream.spring.vertx.resp.poi.GetPoiListResp;
import com.solodream.spring.vertx.resp.poi.GetRoutePoiListResp;
import com.solodream.spring.vertx.resp.poi.RoutePoiInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by young on 16/1/1.
 */
@Service
public class RouteService {
    @Autowired
    private RouteInfoMapper routeInfoMapper;
    @Autowired
    private PoiInfoMapper poiInfoMapper;

    public GetPoiListResp from(GetRouteDetailReq request) {
        RouteContractInfoDto routeContractInfoDto = new RouteContractInfoDto();
        if (request.getCustomerId() != null) {
            routeContractInfoDto.setCustomerId(Integer.parseInt(request.getCustomerId()));
        }
        if (request.getContractId() != null) {
            routeContractInfoDto.setContractId(Integer.parseInt(request.getContractId()));
        }
        List<RouteContractInfoDto> dtos = routeInfoMapper.querys(routeContractInfoDto, request.getKeyword());
        List<GetPoiListResp.PoiInfo> routeSet = new ArrayList<GetPoiListResp.PoiInfo>();
        dtos.forEach((RouteContractInfoDto dto) -> {
            List<PoiInfoDto> pois = JSON.parseArray(dto.getExtend(), PoiInfoDto.class);
            pois.forEach((PoiInfoDto poi) -> {
                        GetPoiListResp.PoiInfo poiresp = new GetPoiListResp.PoiInfo();
                        poiresp.setLongitude(poi.getLongitude());
                        poiresp.setLatitude(poi.getLatitude());
                        poiresp.setPostCode(poi.getPostcode());
                        poiresp.setAddress(poi.getAddress());
                        poiresp.setRemark(poi.getDescription());
                        poiresp.setCompanyId(poi.getCompanyId());
                        poiresp.setCustomerId(poi.getCustomerId());
                        poiresp.setId(poi.getId());
                        routeSet.add(poiresp);
                    }
            );

        });
        GetPoiListResp resp = new GetPoiListResp();
        resp.setDataList(routeSet);
        resp.setTotal(String.valueOf(routeSet.size()));
        return resp;
    }

    public GetPoiListResp to(GetRouteDetailReq request) {
        RouteContractInfoDto routeContractInfoDto = new RouteContractInfoDto();
        if (request.getCustomerId() != null) {
            routeContractInfoDto.setCustomerId(Integer.parseInt(request.getCustomerId()));
        }
        if (request.getContractId() != null) {
            routeContractInfoDto.setContractId(Integer.parseInt(request.getContractId()));
        }
        List<RouteContractInfoDto> dtos = routeInfoMapper.querys(routeContractInfoDto, request.getKeyword());
        String fromId = request.getFromId();
        PoiInfoDto fromPoi = poiInfoMapper.get(Integer.parseInt(fromId));
        String startlat = fromPoi.getLatitude();
        String startlng = fromPoi.getLongitude();
        List<GetPoiListResp.PoiInfo> routeSet = new ArrayList<GetPoiListResp.PoiInfo>();
        dtos.forEach((RouteContractInfoDto dto) -> {
            List<PoiInfoDto> pois = JSON.parseArray(dto.getExtend(), PoiInfoDto.class);
            boolean mark = false;
            for (PoiInfoDto poi : pois) {
                if (DistanceUtil.checkRange(startlng, startlat, poi.getLongitude(), poi.getLatitude(), 200)) {
                    mark = true;
                    continue;
                }
                if (mark) {
                    GetPoiListResp.PoiInfo poiresp = new GetPoiListResp.PoiInfo();
                    poiresp.setLongitude(poi.getLongitude());
                    poiresp.setLatitude(poi.getLatitude());
                    poiresp.setPostCode(poi.getPostcode());
                    poiresp.setAddress(poi.getAddress());
                    poiresp.setRemark(poi.getDescription());
                    poiresp.setCompanyId(poi.getCompanyId());
                    poiresp.setCustomerId(poi.getCustomerId());
                    poiresp.setId(poi.getId());
                    routeSet.add(poiresp);
                }
            }
        });
        GetPoiListResp resp = new GetPoiListResp();
        resp.setDataList(routeSet);
        resp.setTotal(String.valueOf(routeSet.size()));
        return resp;
    }


    public GetRoutePoiListResp list(GetRouteDetailReq request) {
        RouteContractInfoDto routeContractInfoDto = new RouteContractInfoDto();
        if (request.getCustomerId() != null) {
            routeContractInfoDto.setCustomerId(Integer.parseInt(request.getCustomerId()));
        }
        if (request.getContractId() != null) {
            routeContractInfoDto.setContractId(Integer.parseInt(request.getContractId()));
        }
        List<RouteContractInfoDto> dtos = routeInfoMapper.querys(routeContractInfoDto, request.getKeyword());
        List<GetPoiListResp.PoiInfo> routeSet = new ArrayList<>();
        List<RoutePoiInfo> dataList = new ArrayList<RoutePoiInfo>();
        RoutePoiInfo info = new RoutePoiInfo();
        dtos.stream().forEach((RouteContractInfoDto dto) -> {
            List<PoiInfoDto> pois = JSON.parseArray(dto.getExtend(), PoiInfoDto.class);

            PoiInfoDto poi = pois.get(0);
            RoutePoiInfo.RoutePoi poiresp = new RoutePoiInfo.RoutePoi();
            poiresp.setLongitude(poi.getLongitude());
            poiresp.setLatitude(poi.getLatitude());
            poiresp.setPostCode(poi.getPostcode());
            poiresp.setAddress(poi.getAddress());
            poiresp.setRemark(poi.getDescription());
            poiresp.setCompanyId(poi.getCompanyId());
            poiresp.setCustomerId(poi.getCustomerId());
            poiresp.setId(poi.getId());
            info.setFrom(poiresp);


            PoiInfoDto poilast = pois.get(pois.size() - 1);
            RoutePoiInfo.RoutePoi poiresplast = new RoutePoiInfo.RoutePoi();
            poiresplast.setLongitude(poilast.getLongitude());
            poiresplast.setLatitude(poilast.getLatitude());
            poiresplast.setPostCode(poilast.getPostcode());
            poiresplast.setAddress(poilast.getAddress());
            poiresplast.setRemark(poilast.getDescription());
            poiresplast.setCompanyId(poilast.getCompanyId());
            poiresplast.setCustomerId(poilast.getCustomerId());
            poiresplast.setId(poilast.getId());
            info.setTo(poiresplast);

            dataList.add(info);
        });
        GetRoutePoiListResp resp = new GetRoutePoiListResp();
        resp.setDataList(dataList);
        resp.setTotal(SoloConstants.DEFAULT_TOTAL);
        return resp;
    }
}
