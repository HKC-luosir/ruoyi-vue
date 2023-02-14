package invest.lixinger.macro.nationalDebt;

import invest.lixinger.macro.nationalDebt.VO.nationalDebtResult_DataVO;
import invest.lixinger.macro.nationalDebt.VO.nationalDebtResult_RootVO;
import invest.lixinger.ruoyi.entity.CNDebtVO;
import invest.lixinger.ruoyi.entity.USDebtVO;
import invest.lixinger.utils.netRequest;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class request_nationDebt {

    /**+
     * 计算美国国债收益率，不使用25天前，否则会报超过指针
     */
    @Test
    public static nationalDebtResult_RootVO requestUSDebt(String startDate) throws IOException, ParseException {
        InputStream inputStream = request_nationDebt.class.getClassLoader().getResourceAsStream("indexReqParam.yml");
        Map indexReqParam = new Yaml().load(inputStream);
        String macroNationalDebtURL = (String) indexReqParam.get("macroNationalDebtURL");
        String paramJson = getParam_nationDebtUS.getNationDebtUSParamJson(startDate);
        String resultJson = netRequest.jsonNetPost(macroNationalDebtURL, paramJson);

        return (nationalDebtResult_RootVO) getResult_nationDebtUS.getResultObj(resultJson);
    }

    /**
     * 计算中国国债收益率，不使用25天前，否则会报超过指针
     */
    public static nationalDebtResult_RootVO requestCNDebt(String startDate) throws Exception {
        InputStream inputStream = request_nationDebt.class.getClassLoader().getResourceAsStream("indexReqParam.yml");
        Map indexReqParam = new Yaml().load(inputStream);
        String macroNationalDebtURL = (String) indexReqParam.get("macroNationalDebtURL");
        String paramJson = getParam_nationDebtCN.getNationDebtCNParamJson(startDate);
        String resultJson = netRequest.jsonNetPost(macroNationalDebtURL, paramJson);

        return (nationalDebtResult_RootVO) getResult_nationDebtUS.getResultObj(resultJson);
    }

    /**
     * 该数据只有当resultObj完整，计算才没有问题，像更新某个范围a的数据时，只会计算a范围的数据百分位
     * 计算当天的国债百分位位置
     * 由于可能出现倒挂等极端情况，所以同时统计了2、3、5、10四种期限的国债，用平均更准确
     * 20、30年的百分位太低不适合
     * 7年期没有数据所以没有使用
     */
    public static Map<String, String> calcultePos(nationalDebtResult_RootVO resultObj) throws Exception {

        List<nationalDebtResult_DataVO> resultData = resultObj.getData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        nationalDebtResult_DataVO latestDayVO = resultData.get(0);
        Map<String, String> map = new HashMap<>();
        // 当期日期
        map.put("date", sdf.format(sdf.parse(latestDayVO.getDate())));
        // -----------------------------------------计算2年期--------------------------------------------
        List<Double> list2 = new ArrayList<>();
        for (nationalDebtResult_DataVO resultDatum : resultData) {
            list2.add(resultDatum.getMir_y2());
        }
        Collections.sort(list2);
        // 2年期国债，最新日期数值
        map.put("2debtValue", String.valueOf(latestDayVO.getMir_y2()));
        // 2年期国债，最新值百分位
        map.put("2debtPos", String.valueOf(list2.indexOf(latestDayVO.getMir_y2()) / (double) list2.size()));
        // -----------------------------------------计算3年期--------------------------------------------
        List<Double> list3 = new ArrayList<>();
        for (nationalDebtResult_DataVO resultDatum : resultData) {
            list3.add(resultDatum.getMir_y3());
        }
        Collections.sort(list3);
        // 3年期国债，最新日期数值
        map.put("3debtValue", String.valueOf(latestDayVO.getMir_y3()));
        // 3年期国债，最新值百分位
        map.put("3debtPos", String.valueOf(list3.indexOf(latestDayVO.getMir_y3()) / (double) list3.size()));

        // -----------------------------------------计算5年期--------------------------------------------
        List<Double> list5 = new ArrayList<>();
        for (nationalDebtResult_DataVO resultDatum : resultData) {
            list5.add(resultDatum.getMir_y5());
        }
        Collections.sort(list5);
        // 5年期国债，最新日期数值
        map.put("5debtValue", String.valueOf(latestDayVO.getMir_y5()));
        // 5年期国债，最新值百分位
        map.put("5debtPos", String.valueOf(list5.indexOf(latestDayVO.getMir_y5()) / (double) list5.size()));
        // -----------------------------------------计算10年期--------------------------------------------
        List<Double> list10 = new ArrayList<>();
        for (nationalDebtResult_DataVO datum : resultData) {
            list10.add(datum.getMir_y10());
        }
        Collections.sort(list10);

        // 10年期国债，最新日期数值
        map.put("10debtValue", String.valueOf(latestDayVO.getMir_y10()));
        // 10年期国债，最新值百分位
        map.put("10debtPos", String.valueOf(list10.indexOf(latestDayVO.getMir_y10()) / (double) list10.size()));
        // ------------------------------------------计算2、3、5、10期限的平均值----------------------------------
        double totalPos = Double.parseDouble(map.get("10debtPos")) + Double.parseDouble(map.get("2debtPos")) + Double.parseDouble(map.get("3debtPos")) + Double.parseDouble(map.get("5debtPos"));
        double averagePos = totalPos / 4;
        map.put("averagePos", String.valueOf(averagePos));
        // ----------------------------
        System.out.println("10年期债券数值：" + new DecimalFormat("0.00%").format(Double.valueOf(map.get("10debtValue"))));
        System.out.println("10年期债券百分位：" + new DecimalFormat("0.00%").format(Double.valueOf(map.get("10debtPos"))));
        System.out.println("平均后的债券百分位为：" + new DecimalFormat("0.00%").format(Double.valueOf(map.get("averagePos"))));

        return map;
    }

    /**
     * 计算数据库的国债百分位位置
     * 由于可能出现倒挂等极端情况，所以同时统计了2、3、5、10四种期限的国债，用平均更准确
     * 20、30年的百分位太低不适合
     * 7年期没有数据所以没有使用
     */
    public static Map<String, String> calculteDBPosCN(List<CNDebtVO> cnDebtDataRangeVOList) throws Exception {

        Map<String, String> map = new HashMap<>();
        CNDebtVO latestDayVO = cnDebtDataRangeVOList.get(cnDebtDataRangeVOList.size() - 1);

        // -----------------------------------------计算2年期--------------------------------------------
        List<Double> list2 = new ArrayList<>();
        for (CNDebtVO resultDatum : cnDebtDataRangeVOList) {
            list2.add(resultDatum.getY2());
        }
        Collections.sort(list2);
        // 2年期国债，最新值百分位
        map.put("2debtPos", String.valueOf(list2.indexOf(latestDayVO.getY2()) / (double) list2.size()));

        // -----------------------------------------计算3年期--------------------------------------------
        List<Double> list3 = new ArrayList<>();
        for (CNDebtVO resultDatum : cnDebtDataRangeVOList) {
            list3.add(resultDatum.getY3());
        }
        Collections.sort(list3);
        // 3年期国债，最新值百分位
        map.put("3debtPos", String.valueOf(list3.indexOf(latestDayVO.getY3()) / (double) list3.size()));

        // -----------------------------------------计算5年期--------------------------------------------
        List<Double> list5 = new ArrayList<>();
        for (CNDebtVO resultDatum : cnDebtDataRangeVOList) {
            list5.add(resultDatum.getY5());
        }
        Collections.sort(list5);
        // 5年期国债，最新值百分位
        map.put("5debtPos", String.valueOf(list5.indexOf(latestDayVO.getY5()) / (double) list5.size()));

        // -----------------------------------------计算10年期--------------------------------------------
        List<Double> list10 = new ArrayList<>();
        for (CNDebtVO datum : cnDebtDataRangeVOList) {
            list10.add(datum.getY10());
        }
        Collections.sort(list10);

        // 10年期国债，最新值百分位
        map.put("10debtPos", String.valueOf(list10.indexOf(latestDayVO.getY10()) / (double) list10.size()));

        // ------------------------------------------计算2、3、5、10期限的平均值----------------------------------
        double totalPos = Double.parseDouble(map.get("10debtPos")) + Double.parseDouble(map.get("2debtPos")) + Double.parseDouble(map.get("3debtPos")) + Double.parseDouble(map.get("5debtPos"));
        double averagePos = totalPos / 4;
        map.put("averagePos", String.valueOf(averagePos));
        // ----------------------------
        System.out.println("2年期债券百分位：" + new DecimalFormat("0.00%").format(Double.valueOf(map.get("2debtPos"))));
        System.out.println("3年期债券百分位：" + new DecimalFormat("0.00%").format(Double.valueOf(map.get("3debtPos"))));
        System.out.println("5年期债券百分位：" + new DecimalFormat("0.00%").format(Double.valueOf(map.get("5debtPos"))));
        System.out.println("10年期债券百分位：" + new DecimalFormat("0.00%").format(Double.valueOf(map.get("10debtPos"))));
        System.out.println("平均后的债券百分位为：" + new DecimalFormat("0.00%").format(Double.valueOf(map.get("averagePos"))));

        return map;
    }

    /**
     * 计算数据库的国债百分位位置
     * 由于可能出现倒挂等极端情况，所以同时统计了2、3、5、10四种期限的国债，用平均更准确
     * 20、30年的百分位太低不适合
     * 7年期没有数据所以没有使用
     */
    public static Map<String, String> calculteDBPosUS(List<USDebtVO> USDebtDataRangeVOList) throws Exception {

        Map<String, String> map = new HashMap<>();
        USDebtVO latestDayVO = USDebtDataRangeVOList.get(USDebtDataRangeVOList.size() - 1);

        // -----------------------------------------计算2年期--------------------------------------------
        List<Double> list2 = new ArrayList<>();
        for (USDebtVO resultDatum : USDebtDataRangeVOList) {
            list2.add(resultDatum.getY2());
        }
        Collections.sort(list2);
        // 2年期国债，最新值百分位
        map.put("2debtPos", String.valueOf(list2.indexOf(latestDayVO.getY2()) / (double) list2.size()));

        // -----------------------------------------计算3年期--------------------------------------------
        List<Double> list3 = new ArrayList<>();
        for (USDebtVO resultDatum : USDebtDataRangeVOList) {
            list3.add(resultDatum.getY3());
        }
        Collections.sort(list3);
        // 3年期国债，最新值百分位
        map.put("3debtPos", String.valueOf(list3.indexOf(latestDayVO.getY3()) / (double) list3.size()));

        // -----------------------------------------计算5年期--------------------------------------------
        List<Double> list5 = new ArrayList<>();
        for (USDebtVO resultDatum : USDebtDataRangeVOList) {
            list5.add(resultDatum.getY5());
        }
        Collections.sort(list5);
        // 5年期国债，最新值百分位
        map.put("5debtPos", String.valueOf(list5.indexOf(latestDayVO.getY5()) / (double) list5.size()));

        // -----------------------------------------计算10年期--------------------------------------------
        List<Double> list10 = new ArrayList<>();
        for (USDebtVO datum : USDebtDataRangeVOList) {
            list10.add(datum.getY10());
        }
        Collections.sort(list10);

        // 10年期国债，最新值百分位
        map.put("10debtPos", String.valueOf(list10.indexOf(latestDayVO.getY10()) / (double) list10.size()));

        // ------------------------------------------计算2、3、5、10期限的平均值----------------------------------
        double totalPos = Double.parseDouble(map.get("10debtPos")) + Double.parseDouble(map.get("2debtPos")) + Double.parseDouble(map.get("3debtPos")) + Double.parseDouble(map.get("5debtPos"));
        double averagePos = totalPos / 4;
        map.put("averagePos", String.valueOf(averagePos));
        // ----------------------------
        System.out.println("2年期债券百分位：" + new DecimalFormat("0.00%").format(Double.valueOf(map.get("2debtPos"))));
        System.out.println("3年期债券百分位：" + new DecimalFormat("0.00%").format(Double.valueOf(map.get("3debtPos"))));
        System.out.println("5年期债券百分位：" + new DecimalFormat("0.00%").format(Double.valueOf(map.get("5debtPos"))));
        System.out.println("10年期债券百分位：" + new DecimalFormat("0.00%").format(Double.valueOf(map.get("10debtPos"))));
        System.out.println("平均后的债券百分位为：" + new DecimalFormat("0.00%").format(Double.valueOf(map.get("averagePos"))));

        return map;
    }
}
