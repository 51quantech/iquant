package com.iquant.controller;

import com.iquant.service.LabService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yonggangli on 2016/9/1.
 */
@Controller
@RequestMapping(value = "/json")
public class StockController {

    private final static Logger LOG = Logger.getLogger(StockController.class);

    @Autowired
    private LabService labService;

    private final String URL_PREFIX = "http://service1.mrd.sohuno.com";
    private final String STOCK_URL_PREFIX = "";

    @RequestMapping(value = "/stock_getStockInfoAll")
    public void getStockInfoAll(HttpServletResponse response) throws IOException {
        String result = "{\"Message\":\"Message success\",\"data\":[{\"infoId\":\"000971\",\"infoName\":\"高升控股\",\"infostName\":\"gskg\"},{\"infoId\":\"000970\",\"infoName\":\"中科三环\",\"infostName\":\"zksh\"},{\"infoId\":\"600186\",\"infoName\":\"莲花健康\",\"infostName\":\"lhjk\"},{\"infoId\":\"600187\",\"infoName\":\"国中水务\",\"infostName\":\"gzsw\"},{\"infoId\":\"600188\",\"infoName\":\"兖州煤业\",\"infostName\":\"yzmy\"},{\"infoId\":\"600189\",\"infoName\":\"吉林森工\",\"infostName\":\"jlsg\"},{\"infoId\":\"600170\",\"infoName\":\"上海建工\",\"infostName\":\"shjg\"},{\"infoId\":\"600171\",\"infoName\":\"上海贝岭\",\"infostName\":\"shbl\"},{\"infoId\":\"600172\",\"infoName\":\"黄河旋风\",\"infostName\":\"hhxf\"},{\"infoId\":\"600173\",\"infoName\":\"卧龙地产\",\"infostName\":\"wldc\"},{\"infoId\":\"600175\",\"infoName\":\"美都能源\",\"infostName\":\"mdny\"},{\"infoId\":\"600176\",\"infoName\":\"中国巨石\",\"infostName\":\"zgjs\"},{\"infoId\":\"600177\",\"infoName\":\"雅戈尔\",\"infostName\":\"yge\"},{\"infoId\":\"600178\",\"infoName\":\"东安动力\",\"infostName\":\"dadl\"},{\"infoId\":\"600179\",\"infoName\":\"*ST黑化\",\"infostName\":\"sthh\"},{\"infoId\":\"600160\",\"infoName\":\"巨化股份\",\"infostName\":\"jhgf\"},{\"infoId\":\"600161\",\"infoName\":\"天坛生物\",\"infostName\":\"ttsw\"},{\"infoId\":\"600162\",\"infoName\":\"香江控股\",\"infostName\":\"xjkg\"},{\"infoId\":\"600163\",\"infoName\":\"中闽能源\",\"infostName\":\"zmny\"},{\"infoId\":\"600165\",\"infoName\":\"新日恒力\",\"infostName\":\"xrhl\"},{\"infoId\":\"600166\",\"infoName\":\"福田汽车\",\"infostName\":\"ftqc\"},{\"infoId\":\"600167\",\"infoName\":\"联美控股\",\"infostName\":\"lmkg\"},{\"infoId\":\"600168\",\"infoName\":\"武汉控股\",\"infostName\":\"whkg\"},{\"infoId\":\"600169\",\"infoName\":\"太原重工\",\"infostName\":\"tyzg\"},{\"infoId\":\"600149\",\"infoName\":\"廊坊发展\",\"infostName\":\"lffz\"},{\"infoId\":\"600150\",\"infoName\":\"中国船舶\",\"infostName\":\"zgcb\"},{\"infoId\":\"600151\",\"infoName\":\"航天机电\",\"infostName\":\"htjd\"},{\"infoId\":\"600152\",\"infoName\":\"维科精华\",\"infostName\":\"wkjh\"},{\"infoId\":\"600153\",\"infoName\":\"建发股份\",\"infostName\":\"jfgf\"},{\"infoId\":\"600155\",\"infoName\":\"宝硕股份\",\"infostName\":\"bsgf\"},{\"infoId\":\"600156\",\"infoName\":\"华升股份\",\"infostName\":\"hsgf\"},{\"infoId\":\"600157\",\"infoName\":\"永泰能源\",\"infostName\":\"ytny\"},{\"infoId\":\"600158\",\"infoName\":\"中体产业\",\"infostName\":\"ztcy\"},{\"infoId\":\"600159\",\"infoName\":\"大龙地产\",\"infostName\":\"dldc\"},{\"infoId\":\"000410\",\"infoName\":\"沈阳机床\",\"infostName\":\"syjc\"},{\"infoId\":\"000413\",\"infoName\":\"东旭光电\",\"infostName\":\"dxgd\"},{\"infoId\":\"000412\",\"infoName\":\"ST五环\",\"infostName\":\"stwh\"},{\"infoId\":\"000411\",\"infoName\":\"英特集团\",\"infostName\":\"ytjt\"},{\"infoId\":\"000418\",\"infoName\":\"小天鹅Ａ\",\"infostName\":\"xtea\"},{\"infoId\":\"000417\",\"infoName\":\"合肥百货\",\"infostName\":\"hfbh\"},{\"infoId\":\"000416\",\"infoName\":\"民生控股\",\"infostName\":\"mskg\"},{\"infoId\":\"000415\",\"infoName\":\"渤海金控\",\"infostName\":\"bhjk\"},{\"infoId\":\"000419\",\"infoName\":\"通程控股\",\"infostName\":\"tckg\"},{\"infoId\":\"603299\",\"infoName\":\"井神股份\",\"infostName\":\"jsgf\"},{\"infoId\":\"000403\",\"infoName\":\"ST生化\",\"infostName\":\"stsh\"},{\"infoId\":\"000402\",\"infoName\":\"金融街\",\"infostName\":\"jrj\"},{\"infoId\":\"000401\",\"infoName\":\"冀东水泥\",\"infostName\":\"jdsn\"},{\"infoId\":\"000400\",\"infoName\":\"许继电气\",\"infostName\":\"xjdq\"},{\"infoId\":\"000407\",\"infoName\":\"胜利股份\",\"infostName\":\"slgf\"},{\"infoId\":\"000406\",\"infoName\":\"大明退市\",\"infostName\":\"dmts\"},{\"infoId\":\"000405\",\"infoName\":\"ST鑫光\",\"infostName\":\"stxg\"},{\"infoId\":\"000404\",\"infoName\":\"华意压缩\",\"infostName\":\"hyys\"},{\"infoId\":\"000409\",\"infoName\":\"山东地矿\",\"infostName\":\"sddk\"},{\"infoId\":\"000408\",\"infoName\":\"*ST金源\",\"infostName\":\"stjy\"},{\"infoId\":\"603288\",\"infoName\":\"海天味业\",\"infostName\":\"htwy\"},{\"infoId\":\"300498\",\"infoName\":\"温氏股份\",\"infostName\":\"wsgf\"},{\"infoId\":\"000430\",\"infoName\":\"张家界\",\"infostName\":\"zjj\"},{\"infoId\":\"300499\",\"infoName\":\"高澜股份\",\"infostName\":\"glgf\"},{\"infoId\":\"000421\",\"infoName\":\"南京公用\",\"infostName\":\"njgy\"},{\"infoId\":\"000420\",\"infoName\":\"吉林化纤\",\"infostName\":\"jlhx\"},{\"infoId\":\"000425\",\"infoName\":\"徐工机械\",\"infostName\":\"xgjx\"},{\"infoId\":\"000423\",\"infoName\":\"东阿阿胶\",\"infostName\":\"deej\"},{\"infoId\":\"000422\",\"infoName\":\"湖北宜化\",\"infostName\":\"hbyh\"},{\"infoId\":\"000429\",\"infoName\":\"粤高速Ａ\",\"infostName\":\"ygsa\"},{\"infoId\":\"000428\",\"infoName\":\"华天酒店\",\"infostName\":\"htjd\"},{\"infoId\":\"000426\",\"infoName\":\"兴业矿业\",\"infostName\":\"xyky\"},{\"infoId\":\"603268\",\"infoName\":\"松发股份\",\"infostName\":\"sfgf\"},{\"infoId\":\"002237\",\"infoName\":\"恒邦股份\",\"infostName\":\"hbgf\"},{\"infoId\":\"002238\",\"infoName\":\"天威视讯\",\"infostName\":\"twsx\"},{\"infoId\":\"002239\",\"infoName\":\"奥特佳\",\"infostName\":\"atj\"},{\"infoId\":\"002233\",\"infoName\":\"塔牌集团\",\"infostName\":\"tpjt\"},{\"infoId\":\"002234\",\"infoName\":\"民和股份\",\"infostName\":\"mhgf\"},{\"infoId\":\"002235\",\"infoName\":\"安妮股份\",\"infostName\":\"angf\"},{\"infoId\":\"002236\",\"infoName\":\"大华股份\",\"infostName\":\"dhgf\"},{\"infoId\":\"002240\",\"infoName\":\"威华股份\",\"infostName\":\"whgf\"},{\"infoId\":\"002241\",\"infoName\":\"歌尔股份\",\"infostName\":\"gegf\"},{\"infoId\":\"002242\",\"infoName\":\"九阳股份\",\"infostName\":\"jygf\"},{\"infoId\":\"002243\",\"infoName\":\"通产丽星\",\"infostName\":\"tclx\"},{\"infoId\":\"002226\",\"infoName\":\"江南化工\",\"infostName\":\"jnhg\"},{\"infoId\":\"002227\",\"infoName\":\"奥特迅\",\"infostName\":\"atx\"}],\"status\":\"success\",\"ts\":1473508983774}";
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/Json; charset=utf-8");
        response.getWriter().print(result);
    }

    @RequestMapping(value = "/stock_getPlateInfo")
    public void getPlateInfo(HttpServletResponse response) throws IOException {
        String result = "";
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/Json; charset=utf-8");
        response.getWriter().print(result);
    }

    @RequestMapping(value = "/stock_getStockInfo")
    public void getStockInfo(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String value = request.getParameter("value");
        String result = "";
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/Json; charset=utf-8");
        response.getWriter().print(result);
    }

}
