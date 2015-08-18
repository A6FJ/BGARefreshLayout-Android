package cn.bingoogolapple.refreshlayout.demo.diaoyu.honeyant;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dongjianbo on 15-4-8.
 */
public class HttpTaskResult {
    public int code; // 状态码，大于0为正常 小于零为异常
    public String message; // 提示信息，由后端返回的统一提示信息
    public String tipmsg; // 用于开发人员查看的提示信息，暂未启用
    public JsonObject data; // 返回数据，json对象，包含详细的后端处理数据
    public JsonArray dataJsonArray;

    public void parse(JsonObject jsonObject) {

        this.code = jsonObject.get("code").getAsInt();
        this.message = jsonObject.get("message").getAsString();
        this.tipmsg = jsonObject.get("tipmsg").getAsString();

        JsonElement dataObj = jsonObject.get("data");
        if(dataObj.isJsonArray()){
            this.dataJsonArray = dataObj.getAsJsonArray();
        }
        if(dataObj.isJsonObject()){
            this.data = dataObj.getAsJsonObject();
        }

        ApiCommonData.getInstance().parseJson(jsonObject.getAsJsonObject("common"));
    }

    public static void parseModelItemArray(JSONArray jsonArray, ArrayList<HAModel> modelList, String modelItemClassName) {
        if (jsonArray != null) {
            try {
                Class<HAModelItem> modelItemClass = (Class<HAModelItem>) Class.forName(modelItemClassName);
                int count = jsonArray.length();
                for (int i = 0; i < count; i++) {
                    JSONObject itemObject = jsonArray.optJSONObject(i);
                    HAModelItem item = modelItemClass.newInstance();
                    item.parseJson(itemObject);
                    modelList.add((HAModel)item);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
