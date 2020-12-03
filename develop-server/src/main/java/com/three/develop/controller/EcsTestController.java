package com.three.develop.controller;

import com.three.common.enums.StatusEnum;
import com.three.common.vo.JsonData;
import com.three.develop.entity.Area;
import com.three.develop.entity.Bay;
import com.three.develop.entity.Container;
import com.three.develop.repository.AreaRepository;
import com.three.develop.repository.BayRepository;
import com.three.develop.repository.ContainerRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Api(value = "堆场俯视图", tags = "堆场俯视图")
@RestController
@RequestMapping("/ecs/test")
public class EcsTestController {

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private BayRepository bayRepository;

    @Autowired
    private ContainerRepository containerRepository;


    @ApiOperation(value = "箱区-倍位信息", notes = "")
    @GetMapping("/areaBays")
    public JsonData<List<Area>> areaBays() {
        List<Area> areaList = areaRepository.findAllByStatusOrderByAreaNo(StatusEnum.OK.getCode());
        for (Area area : areaList) {
            List<Bay> bayList = bayRepository.findAllByAreaNoAndStatusOrderByBayNo(area.getAreaNo(), StatusEnum.OK.getCode());
            area.setBayList(bayList);
        }
        return new JsonData<>(areaList);
    }

    @ApiOperation(value = "在场箱信息", notes = "")
    @GetMapping("/containers")
    public JsonData<List<Container>> query() {
        List<Container> containerList = containerRepository.findAllByStatus(StatusEnum.OK.getCode());
        return new JsonData<>(containerList);
    }

    @ApiOperation(value = "新增场箱位", notes = "")
    @GetMapping("/insertBay")
    public JsonData<List<Bay>> insertBay() {
        List<Bay> bayList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            int b = i * 2 + 1;
            Bay bay = new Bay();
            bay.setAreaNo("40");
            bay.setBayNo(String.format("%02d", b));
            bay.setRowNum(10);
            bay.setTierNum(6);
            bayList.add(bay);
        }
        bayRepository.saveAll(bayList);
        return new JsonData<>(bayList);
    }

    @ApiOperation(value = "查看回放信息", notes = "")
    @GetMapping("/queryBack")
    public JsonData<String> queryBack() {
        String path = "[{\"agvId\":\"801\",\"road\":" +
                "[{\"time\":0,\"point\":{\"x\":220,\"y\":450},\"duration\":1300,\"rotate\":180}," +
                "{\"time\":1,\"point\":{\"x\":220,\"y\":150},\"duration\":3000,\"rotate\":200}," +
                "{\"time\":2,\"point\":{\"x\":1000,\"y\":150},\"duration\":13000,\"rotate\":90}]}," +
                "{\"agvId\":\"802\",\"road\":" +
                "[{\"time\":0,\"point\":{\"x\":570,\"y\":500},\"duration\":1300,\"rotate\":180}," +
                "{\"time\":1,\"point\":{\"x\":570,\"y\":170},\"duration\":3000,\"rotate\":200}," +
                "{\"time\":2,\"point\":{\"x\":890,\"y\":170},\"duration\":13000,\"rotate\":90}," +
                "{\"time\":3,\"point\":{\"x\":890,\"y\":100},\"duration\":1300,\"rotate\":90}," +
                "{\"time\":4,\"point\":{\"x\":700,\"y\":100},\"duration\":1300,\"rotate\":90}]}," +
                "{\"agvId\":\"803\",\"road\":" +
                "[{\"time\":0,\"point\":{\"x\":780,\"y\":300},\"duration\":1300,\"rotate\":180}," +
                "{\"time\":1,\"point\":{\"x\":780,\"y\":130},\"duration\":3000,\"rotate\":200}," +
                "{\"time\":2,\"point\":{\"x\":120,\"y\":130},\"duration\":13000,\"rotate\":90}," +
                "{\"time\":3,\"point\":{\"x\":120,\"y\":450},\"duration\":13000,\"rotate\":90}]}," +
                "{\"agvId\":\"804\",\"road\":" +
                "[{\"time\":0,\"point\":{\"x\":120,\"y\":450},\"duration\":1300,\"rotate\":0}," +
                "{\"time\":1,\"point\":{\"x\":120,\"y\":150},\"duration\":3000,\"rotate\":90}," +
                "{\"time\":2,\"point\":{\"x\":800,\"y\":150},\"duration\":13000,\"rotate\":90}]}" +
                "]";

        return new JsonData<>(path);
    }

}
