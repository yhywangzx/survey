package com.hassdata.survey.controller.city;

import com.hassdata.survey.dto.CityDTO;
import com.hassdata.survey.dto.CountyDTO;
import com.hassdata.survey.po.City;
import com.hassdata.survey.po.County;
import com.hassdata.survey.po.Province;
import com.hassdata.survey.service.CityService;
import com.hassdata.survey.service.CountyService;
import com.hassdata.survey.service.ProvinceService;
import com.hassdata.survey.util.ServerResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("system")
@Scope("prototype")
@Controller
public class CityController {

    @Resource
    private ProvinceService provinceService;

    @Resource
    private CountyService countyService;

    @Resource
    private CityService cityService;


    @RequestMapping(value = "getCityCenter", method = RequestMethod.GET)
    public String getCityCenter() {
        return "system/city/citycenter";
    }


    @RequestMapping(value = "getCountyAdd",method = RequestMethod.GET)
    public String getCountyAdd(ModelMap map){
        List<City> cityList=cityService.getAll(null);
        map.addAttribute(cityList);
        return "system/city/addCounty";
    }


    @RequestMapping(value = "addCounty",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCounty(County county){
        countyService.save(county);
        return ServerResponse.createBySuccessMessage("添加成功");
    }


    @RequestMapping(value = "countyDel",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse countyDel(Integer id){
        countyService.delete(id);
        return ServerResponse.createBySuccessMessage("删除成功");
    }

    @RequestMapping(value = "getEditorCounty",method = RequestMethod.GET)
    public String getEditorCounty(Integer id,ModelMap map){
        County county=countyService.find(id);
        List<City> cityList=cityService.getAll(null);
        map.addAttribute(cityList);
        map.addAttribute(county);
        return "system/city/editorCounty";
    }

    @RequestMapping(value = "editorCounty",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse editorCounty(County county){
        countyService.updateParams(county);
        return ServerResponse.createBySuccessMessage("修改成功");
    }


    @RequestMapping(value = "getCountyList", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getCountyList(@RequestParam(required = false, defaultValue = "1") Integer page, @RequestParam(required = false, defaultValue = "30") Integer limit) {
        long count = countyService.getScrollCount(null);
        List<County> countyList = countyService.getScrollData(null, "id desc", (page - 1) * limit, limit);
        List<CountyDTO> countyDTOS = new ArrayList<>();
        CountyDTO countyDTO = null;
        for (County c : countyList) {
            countyDTO = new CountyDTO();
            countyDTO.setId(c.getId());
            countyDTO.setCountyname(c.getCountyname());
            City city = cityService.find(c.getCityid());
            countyDTO.setCityname(city.getCityname());
            countyDTO.setProvincename(provinceService.find(city.getProvinceid()).getProvincename());
            countyDTOS.add(countyDTO);
        }
        return ServerResponse.createBySuccessForLayuiTable("成功", countyDTOS, count);
    }


    @RequestMapping(value = "getAddCity", method = RequestMethod.GET)
    public String getAddCity(ModelMap map) {
        List<Province> provinceList = provinceService.getAll(null);
        map.addAttribute(provinceList);
        return "system/city/addCity";
    }


    @RequestMapping(value = "addCity", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCity(City city) {
        cityService.save(city);
        return ServerResponse.createBySuccessMessage("添加成功");
    }

    @RequestMapping(value = "getEditorCity", method = RequestMethod.GET)
    public String getEditorCity(Integer id, ModelMap map) {
        City city = cityService.find(id);
        List<Province> provinceList = provinceService.getAll(null);
        map.addAttribute(provinceList);
        map.addAttribute(city);
        return "system/city/editorCity";
    }

    @RequestMapping(value = "editorCity", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse editorCity(City city) {
        cityService.updateParams(city);
        return ServerResponse.createBySuccessMessage("修改成功");
    }


    @RequestMapping(value = "cityDel", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse cityDel(Integer id) {
        cityService.delete(id);
        countyService.deleteByCityId(id);
        return ServerResponse.createBySuccessMessage("删除成功");
    }


    @RequestMapping(value = "getCityList", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getCityList(@RequestParam(required = false, defaultValue = "1") Integer page, @RequestParam(required = false, defaultValue = "30") Integer limit) {
        long count = cityService.getScrollCount(null);
        List<City> cityList = cityService.getScrollData(null, "id desc", (page - 1) * limit, limit);
        List<CityDTO> cityDTOS = new ArrayList<>();
        CityDTO cityDTO = null;
        for (City c : cityList) {
            cityDTO = new CityDTO();
            cityDTO.setId(c.getId());
            cityDTO.setCityname(c.getCityname());
            cityDTO.setProvincename(provinceService.find(c.getProvinceid()).getProvincename());
            cityDTOS.add(cityDTO);
        }
        return ServerResponse.createBySuccessForLayuiTable("成功", cityDTOS, count);
    }


    @RequestMapping(value = "getAddProvince", method = RequestMethod.GET)
    public String getAddProvince() {
        return "system/city/addProvince";
    }

    @RequestMapping(value = "addProvince", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addProvince(Province province) {
        provinceService.save(province);
        return ServerResponse.createBySuccessMessage(province.getProvincename() + "添加成功");
    }

    @RequestMapping(value = "getEditorProvince", method = RequestMethod.GET)
    public String getEditorProvince(Integer id, ModelMap map) {
        Province province = provinceService.find(id);
        map.addAttribute(province);
        return "system/city/editorProvince";
    }

    @RequestMapping(value = "editorProvince", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse editorProvince(Province province) {
        provinceService.updateParams(province);
        return ServerResponse.createBySuccessMessage(province.getProvincename() + "修改成功");
    }


    @RequestMapping(value = "provinceDel", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse provinceDel(Integer id) {
        provinceService.delete(id);
        City city=new City();
        city.setProvinceid(id);
        List<City> cityList=cityService.getAll(city);
        cityService.deleteByProvinceId(id);
        for(City c : cityList){
            countyService.deleteByCityId(c.getId());
        }
        return ServerResponse.createBySuccessMessage("删除成功");
    }


    @RequestMapping(value = "getProvinceList", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getProvinceList(@RequestParam(required = false, defaultValue = "1") Integer page, @RequestParam(required = false, defaultValue = "30") Integer limit) {
        long count = provinceService.getScrollCount(null);
        List<Province> countyList = provinceService.getScrollData(null, "id desc", (page - 1) * limit, limit);
        return ServerResponse.createBySuccessForLayuiTable("成功", countyList, count);
    }




    @RequestMapping(value = "getCityByProvinceId",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getCityByProvinceId(Integer provinceid){
        City city=new City();
        city.setProvinceid(provinceid);
        List<City> cities=cityService.getAll(city);
        return ServerResponse.createBySuccess(cities);
    }

    @RequestMapping(value = "getCountyByCityId",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getCountyByCityId(Integer cityid){
        County county=new County();
        county.setCityid(cityid);
        List<County> counties=countyService.getAll(county);
        return ServerResponse.createBySuccess(counties);
    }


}
